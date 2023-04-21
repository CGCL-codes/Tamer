package com.planet_ink.coffee_mud.core.smtp;

import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.threads.ServiceEngine;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import com.planet_ink.coffee_mud.core.exceptions.*;
import com.planet_ink.coffee_mud.Libraries.interfaces.*;

@SuppressWarnings("unchecked")
public class SMTPserver extends Thread implements Tickable {

    public String ID() {
        return "SMTPserver";
    }

    public String name() {
        return "SMTPserver";
    }

    public CMObject newInstance() {
        try {
            return (CMObject) getClass().newInstance();
        } catch (Exception e) {
            return new SMTPserver(mud);
        }
    }

    public void initializeClass() {
    }

    public CMObject copyOf() {
        try {
            return (SMTPserver) this.clone();
        } catch (Exception e) {
            return newInstance();
        }
    }

    public int compareTo(CMObject o) {
        return CMClass.classID(this).compareToIgnoreCase(CMClass.classID(o));
    }

    public long tickStatus = STATUS_NOT;

    public long getTickStatus() {
        return tickStatus;
    }

    public long lastAllProcessing = System.currentTimeMillis();

    public CMProps page = null;

    public static final float HOST_VERSION_MAJOR = (float) 1.0;

    public static final float HOST_VERSION_MINOR = (float) 0.0;

    public static Hashtable webMacros = null;

    public static CMProps iniPage = null;

    public ServerSocket servsock = null;

    public boolean isOK = false;

    private MudHost mud;

    private static boolean displayedBlurb = false;

    private static String domain = "coffeemud";

    private static DVector journals = null;

    private HashSet oldEmailComplaints = new HashSet();

    public static final String ServerVersionString = "CoffeeMud SMTPserver/" + HOST_VERSION_MAJOR + "." + HOST_VERSION_MINOR;

    public SMTPserver() {
        super("SMTP");
        mud = null;
        isOK = false;
        setDaemon(true);
    }

    public SMTPserver(MudHost a_mud) {
        super("SMTP");
        mud = a_mud;
        if (!initServer()) isOK = false; else isOK = true;
        setDaemon(true);
    }

    public MudHost getMUD() {
        return mud;
    }

    public String domainName() {
        return domain;
    }

    public String mailboxName() {
        return CMProps.getVar(CMProps.SYSTEM_MAILBOX);
    }

    public Properties getCommonPropPage() {
        if (iniPage == null || !iniPage.loaded) {
            iniPage = new CMProps("web/common.ini");
            if (!iniPage.loaded) Log.errOut("SMTPserver", "Unable to load common.ini!");
        }
        return iniPage;
    }

    protected boolean initServer() {
        if (!loadPropPage()) {
            Log.errOut(getName(), "SMTPserver unable to read ini file.");
            return false;
        }
        if (CMProps.getVar(CMProps.SYSTEM_MUDDOMAIN).toLowerCase().length() == 0) {
            Log.errOut(getName(), "Set your coffeemud.ini parameter: DOMAIN");
            return false;
        }
        if (page.getStr("PORT").length() == 0) {
            Log.errOut(getName(), "Set your coffeemud.ini parameter: PORT");
            return false;
        }
        domain = CMProps.getVar(CMProps.SYSTEM_MUDDOMAIN).toLowerCase();
        String mailbox = page.getStr("MAILBOX");
        if (mailbox == null) mailbox = "";
        CMProps.setVar(CMProps.SYSTEM_MAILBOX, mailbox.trim());
        CMProps.setIntVar(CMProps.SYSTEMI_MAXMAILBOX, getMaxMsgs());
        CMProps.setBoolVar(CMProps.SYSTEMB_EMAILFORWARDING, CMath.s_bool(page.getStr("FORWARD")));
        String journalStr = page.getStr("JOURNALS");
        if ((journalStr == null) || (journalStr.length() > 0)) {
            Vector V = CMParms.parseCommas(journalStr, true);
            if (V.size() > 0) {
                journals = new DVector(5);
                for (int v = 0; v < V.size(); v++) {
                    String s = ((String) V.elementAt(v)).trim();
                    String parm = "";
                    int x = s.indexOf("(");
                    if ((x > 0) && (s.endsWith(")"))) {
                        parm = s.substring(x + 1, s.length() - 1).trim();
                        s = s.substring(0, x).trim();
                    }
                    if (!journals.contains(s)) {
                        Vector PV = CMParms.parseSpaces(parm, true);
                        StringBuffer crit = new StringBuffer("");
                        boolean forward = false;
                        boolean subscribeOnly = false;
                        boolean keepAll = false;
                        for (int pv = 0; pv < PV.size(); pv++) {
                            String ps = (String) PV.elementAt(pv);
                            if (ps.equalsIgnoreCase("forward")) forward = true; else if (ps.equalsIgnoreCase("subscribeonly")) subscribeOnly = true; else if (ps.equalsIgnoreCase("keepall")) keepAll = true; else crit.append(s + " ");
                        }
                        journals.addElement(s, Boolean.valueOf(forward), Boolean.valueOf(subscribeOnly), Boolean.valueOf(keepAll), crit.toString().trim());
                    }
                }
            }
        }
        if (!displayedBlurb) {
            displayedBlurb = true;
        }
        if (mailbox.length() == 0) Log.sysOut(getName(), "Player mail box system is disabled.");
        return true;
    }

    public String getAnEmailJournal(String journal) {
        if (journals == null) return null;
        journal = CMStrings.replaceAll(journal, "_", " ");
        for (int i = 0; i < journals.size(); i++) {
            if (journal.equalsIgnoreCase((String) journals.elementAt(i, 1))) return (String) journals.elementAt(i, 1);
        }
        return null;
    }

    public boolean isAForwardingJournal(String journal) {
        if (journals == null) return false;
        for (int i = 0; i < journals.size(); i++) {
            if (journal.equalsIgnoreCase((String) journals.elementAt(i, 1))) return ((Boolean) journals.elementAt(i, 2)).booleanValue();
        }
        return false;
    }

    public boolean isASubscribeOnlyJournal(String journal) {
        if (journals == null) return false;
        for (int i = 0; i < journals.size(); i++) {
            if (journal.equalsIgnoreCase((String) journals.elementAt(i, 1))) return ((Boolean) journals.elementAt(i, 3)).booleanValue();
        }
        return false;
    }

    public boolean isAKeepAllJournal(String journal) {
        if (journals == null) return false;
        for (int i = 0; i < journals.size(); i++) {
            if (journal.equalsIgnoreCase((String) journals.elementAt(i, 1))) return ((Boolean) journals.elementAt(i, 4)).booleanValue();
        }
        return false;
    }

    public String getJournalCriteria(String journal) {
        if (journals == null) return "";
        for (int i = 0; i < journals.size(); i++) {
            if (journal.equalsIgnoreCase((String) journals.elementAt(i, 1))) return (String) journals.elementAt(i, 5);
        }
        return "";
    }

    protected boolean loadPropPage() {
        if (page == null || !page.loaded) {
            String fn = "web/email.ini";
            page = new CMProps(getCommonPropPage(), fn);
            if (!page.loaded) {
                Log.errOut(getName(), "failed to load " + fn);
                return false;
            }
        }
        return true;
    }

    public void run() {
        int q_len = 6;
        Socket sock = null;
        boolean serverOK = false;
        if (!isOK) return;
        if ((page == null) || (!page.loaded)) {
            Log.errOut(getName(), "ERROR: SMTPserver will not run with no properties. Shutting down.");
            isOK = false;
            return;
        }
        if (page.getInt("BACKLOG") > 0) q_len = page.getInt("BACKLOG");
        InetAddress bindAddr = null;
        if (page.getStr("BIND") != null && page.getStr("BIND").length() > 0) {
            try {
                bindAddr = InetAddress.getByName(page.getStr("BIND"));
            } catch (UnknownHostException e) {
                Log.errOut(getName(), "ERROR: Could not bind to address " + page.getStr("BIND"));
            }
        }
        try {
            servsock = new ServerSocket(page.getInt("PORT"), q_len, bindAddr);
            Log.sysOut(getName(), "Started on port: " + page.getInt("PORT"));
            if (bindAddr != null) Log.sysOut(getName(), "Bound to: " + bindAddr.toString());
            serverOK = true;
            while (true) {
                sock = servsock.accept();
                while (CMLib.threads().isAllSuspended()) Thread.sleep(1000);
                if (CMProps.getBoolVar(CMProps.SYSTEMB_MUDSTARTED)) {
                    ProcessSMTPrequest W = new ProcessSMTPrequest(sock, this, page);
                    W.equals(W);
                } else {
                    sock.getOutputStream().write(("421 Mud down.. try later.\r\n").getBytes());
                    sock.getOutputStream().flush();
                    sock.close();
                }
                sock = null;
            }
        } catch (Exception e) {
            Log.errOut(getName(), e.getMessage());
            if (!serverOK) isOK = false;
        }
        try {
            if (servsock != null) servsock.close();
            if (sock != null) sock.close();
        } catch (IOException e) {
        }
    }

    public void shutdown(Session S) {
        Log.sysOut(getName(), "Shutting down.");
        if (S != null) S.println(getName() + " shutting down.");
        if (getTickStatus() == Tickable.STATUS_NOT) tick(this, Tickable.TICKID_READYTOSTOP); else {
            int att = 0;
            while ((att < 100) && (getTickStatus() != Tickable.STATUS_NOT)) {
                try {
                    att++;
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
        }
        CMLib.killThread(this, 1000, 30);
    }

    public void shutdown() {
        shutdown(null);
    }

    protected boolean rightTimeToSendEmail(long email) {
        long curr = System.currentTimeMillis();
        Calendar IQE = Calendar.getInstance();
        IQE.setTimeInMillis(email);
        Calendar IQC = Calendar.getInstance();
        IQC.setTimeInMillis(curr);
        if (CMath.absDiff(email, curr) < (30 * 60 * 1000)) return true;
        while (IQE.before(IQC)) {
            if (CMath.absDiff(IQE.getTimeInMillis(), IQC.getTimeInMillis()) < (30 * 60 * 1000)) return true;
            IQE.add(Calendar.DATE, 1);
        }
        return false;
    }

    public Hashtable getMailingLists(Hashtable oldH) {
        if (oldH != null) return oldH;
        return Resources.getMultiLists("mailinglists.txt");
    }

    public boolean tick(Tickable ticking, int tickID) {
        if (tickStatus != STATUS_NOT) return true;
        boolean updatedMailingLists = false;
        Hashtable lists = null;
        tickStatus = STATUS_START;
        if ((tickID == Tickable.TICKID_READYTOSTOP) || (tickID == Tickable.TICKID_EMAIL)) {
            if (journals != null) for (int j = 0; j < journals.size(); j++) {
                String journalName = (String) journals.elementAt(j, 1);
                if (isAForwardingJournal(journalName)) {
                    boolean keepall = isAKeepAllJournal(journalName);
                    Vector msgs = CMLib.database().DBReadJournalMsgs(journalName);
                    for (int m = 0; m < msgs.size(); m++) {
                        JournalsLibrary.JournalEntry msg = (JournalsLibrary.JournalEntry) msgs.elementAt(m);
                        String to = msg.to;
                        if (to.equalsIgnoreCase("ALL")) {
                            long date = msg.update;
                            String from = msg.from;
                            String key = msg.key;
                            String subj = msg.subj;
                            String s = msg.msg.trim();
                            if ((subj.equalsIgnoreCase("subscribe")) || (s.equalsIgnoreCase("subscribe"))) {
                                CMLib.database().DBDeleteJournal(journalName, key);
                                if (CMLib.players().playerExists(from)) {
                                    lists = getMailingLists(lists);
                                    if (lists == null) lists = new Hashtable();
                                    Vector mylist = (Vector) lists.get(journalName);
                                    if (mylist == null) {
                                        mylist = new Vector();
                                        lists.put(journalName, mylist);
                                    }
                                    boolean found = false;
                                    for (int l = 0; l < mylist.size(); l++) if (((String) mylist.elementAt(l)).equalsIgnoreCase(from)) found = true;
                                    if (!found) {
                                        mylist.addElement(from);
                                        updatedMailingLists = true;
                                        if (CMProps.getBoolVar(CMProps.SYSTEMB_EMAILFORWARDING)) {
                                            String subscribeTitle = page.getStr("SUBSCRIBEDTITLE");
                                            if ((subscribeTitle == null) || (subscribeTitle.length() == 0)) subscribeTitle = "Subscribed";
                                            String subscribedMsg = page.getStr("SUBSCRIBEDMSG");
                                            if ((subscribedMsg == null) || (subscribedMsg.length() == 0)) subscribedMsg = "You are now subscribed to " + journalName + ". To unsubscribe, send an email with a subject of unsubscribe.";
                                            subscribeTitle = CMLib.coffeeFilter().fullInFilter(CMStrings.replaceAll(subscribeTitle, "<NAME>", journalName), false);
                                            subscribedMsg = CMLib.coffeeFilter().fullInFilter(CMStrings.replaceAll(subscribedMsg, "<NAME>", journalName), false);
                                            CMLib.database().DBWriteJournal(journalName, journalName, from, subscribeTitle, subscribedMsg);
                                        }
                                    }
                                }
                            } else if ((subj.equalsIgnoreCase("unsubscribe")) || (s.equalsIgnoreCase("unsubscribe"))) {
                                CMLib.database().DBDeleteJournal(journalName, key);
                                lists = getMailingLists(lists);
                                if (lists == null) continue;
                                Vector mylist = (Vector) lists.get(journalName);
                                if (mylist == null) continue;
                                for (int l = mylist.size() - 1; l >= 0; l--) if (((String) mylist.elementAt(l)).equalsIgnoreCase(from)) {
                                    mylist.removeElementAt(l);
                                    updatedMailingLists = true;
                                    if (CMProps.getBoolVar(CMProps.SYSTEMB_EMAILFORWARDING)) {
                                        String unsubscribeTitle = page.getStr("UNSUBSCRIBEDTITLE");
                                        if ((unsubscribeTitle == null) || (unsubscribeTitle.length() == 0)) unsubscribeTitle = "Subscribed";
                                        String unsubscribedMsg = page.getStr("UNSUBSCRIBEDMSG");
                                        if ((unsubscribedMsg == null) || (unsubscribedMsg.length() == 0)) unsubscribedMsg = "You are no longer subscribed to " + journalName + ". To subscribe again, send an email with a subject of subscribe.";
                                        unsubscribeTitle = CMLib.coffeeFilter().fullInFilter(CMStrings.replaceAll(unsubscribeTitle, "<NAME>", journalName), false);
                                        unsubscribedMsg = CMLib.coffeeFilter().fullInFilter(CMStrings.replaceAll(unsubscribedMsg, "<NAME>", journalName), false);
                                        CMLib.database().DBWriteJournal(journalName, journalName, from, unsubscribeTitle, unsubscribedMsg);
                                    }
                                }
                            } else {
                                if (date > lastAllProcessing) {
                                    lists = getMailingLists(lists);
                                    if (lists != null) {
                                        Vector mylist = (Vector) lists.get(journalName);
                                        if ((mylist != null) && (mylist.contains(from))) {
                                            for (int i = 0; i < mylist.size(); i++) {
                                                String to2 = (String) mylist.elementAt(i);
                                                if (CMProps.getBoolVar(CMProps.SYSTEMB_EMAILFORWARDING)) CMLib.database().DBWriteJournal(journalName, from, to2, subj, s);
                                            }
                                        } else CMLib.database().DBDeleteJournal(journalName, key);
                                    }
                                }
                                if (!keepall) CMLib.database().DBDeleteJournal(journalName, key); else {
                                    Calendar IQE = Calendar.getInstance();
                                    IQE.setTimeInMillis(date);
                                    IQE.add(Calendar.DATE, getJournalDays());
                                    if (IQE.getTimeInMillis() < System.currentTimeMillis()) CMLib.database().DBDeleteJournal(journalName, msg.key);
                                }
                            }
                        }
                    }
                }
            }
            if ((tickID == Tickable.TICKID_EMAIL) && (CMProps.getBoolVar(CMProps.SYSTEMB_EMAILFORWARDING))) {
                if ((mailboxName() != null) && (mailboxName().length() > 0)) {
                    Vector emails = CMLib.database().DBReadJournalMsgs(mailboxName());
                    processEmails(mailboxName(), emails, null, true);
                }
                if (journals != null) for (int j = 0; j < journals.size(); j++) {
                    String journalName = (String) journals.elementAt(j, 1);
                    if (isAForwardingJournal(journalName)) {
                        Vector emails = CMLib.database().DBReadJournalMsgs(journalName);
                        processEmails(journalName, emails, journalName, false);
                    }
                }
            }
            lastAllProcessing = System.currentTimeMillis();
            if ((updatedMailingLists) && (lists != null)) {
                Resources.updateMultiList("mailinglists.txt", lists);
                updatedMailingLists = false;
            }
        }
        System.gc();
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
        }
        tickStatus = STATUS_NOT;
        return true;
    }

    public boolean deleteEmailIfOld(String journalName, String key, long date, int days) {
        Calendar IQE = Calendar.getInstance();
        IQE.setTimeInMillis(date);
        IQE.add(Calendar.DATE, days);
        if (IQE.getTimeInMillis() < System.currentTimeMillis()) {
            CMLib.database().DBDeleteJournal(journalName, key);
            return true;
        }
        return false;
    }

    public void processEmails(String journalName, Vector emails, String overrideReplyTo, boolean usePrivateRules) {
        if (emails != null) for (int e = 0; e < emails.size(); e++) {
            JournalsLibrary.JournalEntry mail = (JournalsLibrary.JournalEntry) emails.elementAt(e);
            String key = mail.key;
            String from = mail.from;
            String to = mail.to;
            long date = mail.update;
            String subj = mail.subj;
            String msg = mail.msg.trim();
            if (to.equalsIgnoreCase("ALL") || (to.toUpperCase().trim().startsWith("MASK="))) continue;
            if (!rightTimeToSendEmail(date)) continue;
            MOB toM = CMLib.players().getLoadPlayer(to);
            if (toM == null) {
                Log.errOut("SMTPServer", "Invalid to address '" + to + "' in email: " + msg);
                CMLib.database().DBDeleteJournal(journalName, key);
                continue;
            }
            if ((toM.playerStats() != null) && (toM.playerStats().getIgnored().contains(from))) {
                CMLib.database().DBDeleteJournal(journalName, key);
                continue;
            }
            if ((usePrivateRules) && (!CMath.bset(mail.attributes, JournalsLibrary.JournalEntry.ATTRIBUTE_PROTECTED)) && (deleteEmailIfOld(journalName, key, date, getEmailDays()))) continue;
            if (CMath.bset(toM.getBitmap(), MOB.ATT_AUTOFORWARD)) continue;
            if ((toM.playerStats() == null) || (toM.playerStats().getEmail().length() == 0)) continue;
            SMTPLibrary.SMTPClient SC = null;
            try {
                if (CMProps.getVar(CMProps.SYSTEM_SMTPSERVERNAME).length() > 0) SC = CMLib.smtp().getClient(CMProps.getVar(CMProps.SYSTEM_SMTPSERVERNAME), SMTPLibrary.DEFAULT_PORT); else SC = CMLib.smtp().getClient(toM.playerStats().getEmail());
            } catch (BadEmailAddressException be) {
                if ((!usePrivateRules) && (!CMath.bset(mail.attributes, JournalsLibrary.JournalEntry.ATTRIBUTE_PROTECTED))) {
                    CMLib.database().DBDeleteJournal(journalName, key);
                    continue;
                }
                continue;
            } catch (java.io.IOException ioe) {
                if (!oldEmailComplaints.contains(toM.Name())) {
                    oldEmailComplaints.add(toM.Name());
                    Log.errOut("SMTPServer", "Unable to find '" + toM.playerStats().getEmail() + "' for '" + toM.name() + "'.");
                }
                if (!CMath.bset(mail.attributes, JournalsLibrary.JournalEntry.ATTRIBUTE_PROTECTED)) deleteEmailIfOld(journalName, key, date, getFailureDays());
                continue;
            }
            String replyTo = (overrideReplyTo != null) ? (overrideReplyTo) : from;
            try {
                SC.sendMessage(from + "@" + domainName(), replyTo + "@" + domainName(), toM.playerStats().getEmail(), usePrivateRules ? toM.playerStats().getEmail() : replyTo + "@" + domainName(), subj, CMLib.coffeeFilter().simpleOutFilter(msg));
                CMLib.database().DBDeleteJournal(journalName, key);
            } catch (java.io.IOException ioe) {
                if (deleteEmailIfOld(journalName, key, date, getFailureDays())) Log.errOut("SMTPServer", "Permanently unable to send to '" + toM.playerStats().getEmail() + "' for user '" + toM.name() + "': " + ioe.getMessage() + "."); else Log.errOut("SMTPServer", "Failure to send to '" + toM.playerStats().getEmail() + "' for user '" + toM.name() + "'.");
            }
        }
    }

    public void interrupt() {
        if (servsock != null) {
            try {
                servsock.close();
                servsock = null;
            } catch (IOException e) {
            }
        }
        super.interrupt();
    }

    public int getMaxMsgs() {
        String s = page.getStr("MAXMSGS");
        if (s == null) return Integer.MAX_VALUE;
        int x = CMath.s_int(s);
        if (x == 0) return Integer.MAX_VALUE;
        return x;
    }

    public int getEmailDays() {
        String s = page.getStr("EMAILDAYS");
        if (s == null) return (365 * 20);
        int x = CMath.s_int(s);
        if (x == 0) return (365 * 20);
        return x;
    }

    public int getJournalDays() {
        String s = page.getStr("JOURNALDAYS");
        if (s == null) return (365 * 20);
        int x = CMath.s_int(s);
        if (x == 0) return (365 * 20);
        return x;
    }

    public int getFailureDays() {
        String s = page.getStr("FAILUREDAYS");
        if (s == null) return (365 * 20);
        int x = CMath.s_int(s);
        if (x == 0) return (365 * 20);
        return x;
    }

    public long getMaxMsgSize() {
        String s = page.getStr("MAXMSGSIZE");
        if (s == null) return Long.MAX_VALUE;
        long x = CMath.s_long(s);
        if (x == 0) return Long.MAX_VALUE;
        return x;
    }
}
