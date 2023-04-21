package crawler;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import model.Member;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import service.*;
import view.CrawlerDialog;

class Parser {

    protected Member superMember;

    protected Vector contactMembers = new Vector();

    public LiveCrawler crawler;

    public Parser() {
    }

    public Parser(LiveCrawler crawler, Member superMember) {
        this.setSuperMember(superMember);
        this.crawler = crawler;
    }

    public String contactMembersToString() {
        if (contactMembers == null | contactMembers.size() <= 0) return "\nNO CONTACTS!";
        StringBuffer buffer = new StringBuffer();
        buffer.append("\nKONTAKTE:\n");
        Iterator itr = contactMembers.iterator();
        while (itr.hasNext()) {
            buffer.append(((Member) itr.next()).getKeyname());
            if (itr.hasNext()) {
                buffer.append("\n");
            }
        }
        return buffer.toString();
    }

    public String toString() {
        return superMember.toString() + this.contactMembersToString();
    }

    public void addContactMember(Member m) {
        this.contactMembers.add(m);
    }

    public Member getSuperMember() {
        return superMember;
    }

    public int getContactMembersCount() {
        return this.contactMembers.size();
    }

    protected void setSuperMember() {
    }

    protected void setSuperMember(Member m) {
        this.superMember = m;
    }

    public void initRootMember() {
        System.out.println("parser.initRootMember()");
        GetMethod page = new GetMethod("https://www.openbc.com/cgi-bin/user.fpl?op=home");
        String keyname = "";
        int id = -1;
        try {
            this.crawler.getClient().executeMethod(page);
            page.getParams().setContentCharset("UTF-8");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(new BufferedInputStream(page.getResponseBodyAsStream()), "UTF-8"));
            String line;
            System.out.println("https://www.openbc.com/cgi-bin/user.fpl?op=home");
            while ((line = buffer.readLine()) != null) {
                if (line.matches(".*<a href=\"/hp/.*")) {
                    String[] tmp = line.split("/");
                    keyname = tmp[2];
                    System.out.println("rootKeyname = " + keyname);
                    break;
                }
            }
            buffer.close();
            page.releaseConnection();
            page = new GetMethod("https://www.openbc.com/hp/" + keyname);
            this.crawler.getClient().executeMethod(page);
            page.getParams().setContentCharset("UTF-8");
            buffer = new BufferedReader(new InputStreamReader(new BufferedInputStream(page.getResponseBodyAsStream()), "UTF-8"));
            System.out.println("https://www.openbc.com/hp/" + keyname);
            while ((line = buffer.readLine()) != null) {
                if (line.matches("\\s+<a href=\"/hp/.*")) {
                    String[] tmp = line.split("/");
                    if (tmp[3].matches("\\d+\\.+\\d+.*")) {
                        id = Integer.parseInt(tmp[3].substring(tmp[3].indexOf(".") + 1, tmp[3].indexOf("\"")));
                        System.out.println("id = " + id);
                        break;
                    }
                }
            }
            buffer.close();
            page.releaseConnection();
            if (id > 0 & keyname.length() > 0) {
                this.superMember = new Member(id, keyname);
                this.superMember.setDisplayname(keyname);
            }
        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
            ExceptionAdmin.saveToDB(e, this.getSuperMember().getKeyname());
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
            ExceptionAdmin.saveToDB(e, this.getSuperMember().getKeyname());
        }
    }

    public void start(CrawlerDialog progress) {
        System.out.println("parser.start()");
        GetMethod page = new GetMethod("https://www.openbc.com/hp/" + this.getSuperMember().getKeyname());
        try {
            this.crawler.getClient().executeMethod(page);
            page.getParams().setContentCharset("UTF-8");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(new BufferedInputStream(page.getResponseBodyAsStream()), "UTF-8"));
            String line;
            System.out.println("https://www.openbc.com/hp/" + this.getSuperMember().getKeyname());
            while ((line = buffer.readLine()) != null) {
                if (line.matches(".*\\?id=.*") & superMember.getObcID() < 0) {
                    int id = Integer.parseInt(line.substring(line.indexOf("?id=") + 4, line.lastIndexOf(".")));
                    superMember.setObcID(id);
                    System.out.println("-> obcid: " + superMember.getObcID());
                    continue;
                }
                if (line.matches(".*/img/users/nobody_m.gif.*") | line.matches(".*/img/users/nobody_f.gif.*")) {
                    superMember.setHasPicture(false);
                }
                if (line.matches(".*<li>best.*")) {
                    superMember.setContactCount(line.substring(line.lastIndexOf(" ") + 1, line.length() - 5));
                    progress.setMember(superMember.getKeyname(), superMember.getContactCount());
                    continue;
                }
                if (line.matches(".*<li>Abrufe.*")) {
                    superMember.setCalls(line.substring(line.lastIndexOf(" ") + 1, line.length() - 5));
                    continue;
                }
                if (line.matches(".*<li>Mitglied.*")) {
                    superMember.setEntry(line.substring(line.lastIndexOf(" ") + 1, line.length() - 5));
                    continue;
                }
                if (line.matches(".*<li>Premium.*")) {
                    superMember.setIsPremium(true);
                    continue;
                }
                if (line.matches(".*<li>Aktivit.*")) {
                    line = line.charAt(line.indexOf("led_") + 4) + "";
                    superMember.setActivity(Integer.parseInt(line));
                    continue;
                }
                if (line.matches(".*div.*" + this.getSuperMember().getDisplayname() + ".*")) {
                    superMember.setDegree(line.substring(line.lastIndexOf("g>") + 2, line.length() - 6).trim());
                    continue;
                }
                if (line.matches(".*>Status</td>")) {
                    line = buffer.readLine();
                    superMember.setStatus(line.substring(line.indexOf("\">") + 2, line.length() - 5));
                }
                if (line.matches(".*Land/Region.*")) {
                    line = buffer.readLine();
                    superMember.setCountry(line.substring(line.indexOf("\">") + 2, line.length() - 5));
                }
                if (line.matches(".*px\">Sprachkenntnisse</td>")) {
                    buffer.readLine();
                    buffer.readLine();
                    line = buffer.readLine();
                    while (!line.matches(".*</td>.*")) {
                        if (!line.matches(".*<br />") && !line.trim().matches("")) {
                            superMember.addLanguages(line.trim() + "");
                        }
                        line = buffer.readLine();
                    }
                }
                if (line.matches(".*<a href=.*")) {
                    String[] tmp = line.trim().split("</a>");
                    for (int i = 0; i < tmp.length; i++) {
                        if (tmp[i].matches(".*&company=.*")) {
                            tmp[i] = tmp[i].substring(tmp[i].lastIndexOf(">") + 1);
                            superMember.addCompany(tmp[i] + "");
                            continue;
                        }
                        if (tmp[i].matches(".*&excompany=.*")) {
                            tmp[i] = tmp[i].substring(tmp[i].lastIndexOf(">") + 1);
                            superMember.addExcompany(tmp[i] + "");
                            continue;
                        }
                        if (tmp[i].matches(".*&extitle=.*")) {
                            tmp[i] = tmp[i].substring(tmp[i].lastIndexOf(">") + 1);
                            superMember.addExcompany("(" + tmp[i] + ")");
                            continue;
                        }
                        if (tmp[i].matches(".*&title=.*")) {
                            tmp[i] = tmp[i].substring(tmp[i].lastIndexOf(">") + 1);
                            superMember.addTitle(tmp[i] + "");
                            continue;
                        }
                        if (tmp[i].matches(".*&industry=.*")) {
                            tmp[i] = tmp[i].substring(tmp[i].lastIndexOf(">") + 1);
                            superMember.addIndustry(tmp[i] + "");
                            continue;
                        }
                        if (tmp[i].matches(".*&zip_code=.*")) {
                            tmp[i] = tmp[i].substring(tmp[i].lastIndexOf(">") + 1);
                            superMember.addZipcode(tmp[i] + "");
                        }
                        if (tmp[i].matches(".*&city=\\D.*")) {
                            tmp[i] = tmp[i].substring(tmp[i].lastIndexOf(">") + 1);
                            superMember.addCity(tmp[i] + "");
                            continue;
                        }
                        if (tmp[i].matches(".*&wants=.*")) {
                            tmp[i] = tmp[i].substring(tmp[i].lastIndexOf(">") + 1);
                            superMember.addWant(tmp[i] + "");
                            continue;
                        }
                        if (tmp[i].matches(".*&haves=.*")) {
                            tmp[i] = tmp[i].substring(tmp[i].lastIndexOf(">") + 1);
                            superMember.addHave(tmp[i] + "");
                            continue;
                        }
                        if (tmp[i].matches(".*&education=.*")) {
                            tmp[i] = tmp[i].substring(tmp[i].lastIndexOf(">") + 1);
                            superMember.addEducation(tmp[i] + "");
                            continue;
                        }
                        if (tmp[i].matches(".*&interests=.*")) {
                            tmp[i] = tmp[i].substring(tmp[i].lastIndexOf(">") + 1);
                            superMember.addInterest(tmp[i] + "");
                            continue;
                        }
                        if (tmp[i].matches(".*&org_member=.*")) {
                            tmp[i] = tmp[i].substring(tmp[i].lastIndexOf(">") + 1);
                            superMember.addOrgs(tmp[i] + "");
                            continue;
                        }
                        if (tmp[i].matches(".*/net.*")) {
                            tmp[i] = tmp[i].substring(tmp[i].lastIndexOf(">") + 1);
                            superMember.addForum(tmp[i] + "");
                            continue;
                        }
                        if (line.matches(".*<li>best.*")) {
                            superMember.setContactCount(line.substring(line.lastIndexOf(" ") + 1, line.length() - 5));
                            continue;
                        }
                        if (tmp[i].matches(".*/hp.*") && tmp[i].charAt(1) != 't' && tmp[i].charAt(1) != 'd' & this.superMember.contactsLocked()) {
                            this.superMember.setContactsLocked(false);
                            continue;
                        }
                    }
                }
            }
            buffer.close();
            page.releaseConnection();
            this.parseContactPages(progress);
            if (this.crawler.previewContacts) {
                insertIntoIDMembers();
            }
            if (this.saveToDB() | true) {
                if (this.getSuperMember().getObcID() == -1) {
                    Exception blub = new Exception();
                    ExceptionAdmin.saveToDB(blub, this.getSuperMember().getKeyname(), "phantom existiert nicht!");
                    try {
                        this.crawler.stop();
                        System.err.println("\nobcid==-1 / MyClient.LIMIT = -1; \n");
                    } catch (Exception e) {
                        ExceptionAdmin.saveToDB(e, this.getSuperMember().getKeyname());
                    }
                } else {
                    System.out.println("\nOK: " + this.getSuperMember().getObcID() + "\n");
                }
            } else {
                System.out.println("__OK: " + this.getSuperMember().getObcID() + "\n");
            }
        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
            ExceptionAdmin.saveToDB(e, this.getSuperMember().getKeyname());
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
            ExceptionAdmin.saveToDB(e, this.getSuperMember().getKeyname());
        }
    }

    protected void parseContactPages(CrawlerDialog progress) {
        if (this.superMember.getContactCount() <= 0) return;
        if (this.superMember.contactsLocked()) return;
        String url = "https://www.openbc.com/cgi-bin/obcpage.fpl?name=" + this.getSuperMember().getKeyname() + "&tab=7&offset=";
        int contactCount = this.getSuperMember().getContactCount();
        System.out.print("Kontakte: " + contactCount);
        double numConsD = Math.ceil(((double) contactCount) / 10);
        System.out.println("\t--> " + numConsD + " Kontaktseiten");
        String[] URLs = new String[(int) numConsD];
        for (int i = 0; i < URLs.length; i++) {
            URLs[i] = url + (i * 10);
        }
        ContactPageParser[] threads = new ContactPageParser[URLs.length];
        int threadsLimit = 20;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(threadsLimit);
        long start, end;
        int i;
        for (int j = 1; j <= (threads.length - (threads.length % threadsLimit)); j++) {
            i = j - 1;
            GetMethod get = new GetMethod(URLs[i]);
            get.getParams().setContentCharset("UTF-8");
            get.setFollowRedirects(true);
            threads[i] = new ContactPageParser(startSignal, doneSignal, this.crawler.getClient(), get, i, this);
            threads[i].start();
            if (j % threadsLimit == 0) {
                start = System.currentTimeMillis();
                startSignal.countDown();
                try {
                    doneSignal.await();
                    end = System.currentTimeMillis();
                    System.out.println(j + " von " + threads.length + " erledigt (" + (end - start) + "ms)");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    ExceptionAdmin.saveToDB(e, this.getSuperMember().getKeyname(), "threads[]-index:" + j);
                }
                startSignal = new CountDownLatch(1);
                doneSignal = new CountDownLatch(threadsLimit);
            }
        }
        int rest = threads.length % threadsLimit;
        if (rest != 0) {
            startSignal = new CountDownLatch(1);
            doneSignal = new CountDownLatch(rest);
            start = System.currentTimeMillis();
            for (i = threads.length - rest; i < threads.length; i++) {
                GetMethod get = new GetMethod(URLs[i]);
                get.getParams().setContentCharset("UTF-8");
                get.setFollowRedirects(true);
                threads[i] = new ContactPageParser(startSignal, doneSignal, this.crawler.getClient(), get, i, this);
                threads[i].start();
            }
            startSignal.countDown();
            try {
                doneSignal.await();
                end = System.currentTimeMillis();
                System.out.println(i + " von " + threads.length + " erledigt (" + (end - start) + "ms)");
            } catch (InterruptedException e) {
                e.printStackTrace();
                ExceptionAdmin.saveToDB(e, this.getSuperMember().getKeyname(), "threads[]-index:REST");
            }
        }
    }

    public boolean saveToDB() {
        boolean isOK = this.superMember.saveToDB();
        Iterator itr = this.contactMembers.iterator();
        while (itr.hasNext()) {
            Member m = ((Member) itr.next());
            this.superMember.link(m);
            isOK &= m.saveToDB();
        }
        return isOK;
    }

    public void insertIntoIDMembers() {
        System.out.println("insertIntoIDMembers()...");
        long ms = System.currentTimeMillis();
        Iterator it = this.contactMembers.iterator();
        int i = 0;
        while (it.hasNext()) {
            i++;
            try {
                String keyname = ((Member) it.next()).getKeyname();
                ResultSet r = DB.getResult("SELECT obcid FROM idmembers WHERE keyname LIKE '" + keyname + "'");
                if (r.next()) {
                    r.close();
                    System.out.println(" -> [" + i + "] ok: " + keyname);
                } else {
                    r.close();
                    int obcid = fetchObcID(keyname);
                    if (obcid > -1) {
                        DB.execute("INSERT INTO idmembers (obcid, keyname) VALUES (" + obcid + ", '" + keyname + "')");
                        System.out.println(" -> [" + i + "] " + obcid + ", '" + keyname + "'");
                    } else {
                        System.err.println(" -> [" + i + "] FAILED: " + obcid + ", '" + keyname + "'");
                    }
                }
            } catch (SQLException e) {
                System.out.println("SQL-Exception: insertIntoIDMembers():" + e.getMessage());
            }
        }
        System.out.println(i + " in " + (System.currentTimeMillis() - ms) + " ms");
    }

    protected int fetchObcID(String keyname) {
        int id = -1;
        GetMethod page = new GetMethod("https://www.openbc.com/hp/" + keyname);
        try {
            this.crawler.client.executeMethod(page);
            page.getParams().setContentCharset("UTF-8");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(new BufferedInputStream(page.getResponseBodyAsStream()), "UTF-8"));
            String line;
            System.out.print(".../hp/" + keyname);
            while ((line = buffer.readLine()) != null) {
                if (line.matches(".*\\?id=.*")) {
                    id = Integer.parseInt(line.substring(line.indexOf("?id=") + 4, line.lastIndexOf(".")));
                    break;
                }
            }
            buffer.close();
            page.releaseConnection();
        } catch (HttpException e) {
            System.err.println("fetchObcID: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("fetchObcID: " + e.getMessage());
            e.printStackTrace();
        }
        return id;
    }
}
