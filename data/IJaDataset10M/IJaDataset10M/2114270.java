package coollemon.dataBase;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import coollemon.kernel.*;

public class XML extends DataFormat {

    public static XML xml = new XML();

    private XML() {
    }

    ;

    public boolean writeFile(ContactManager cm, String name) {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(name));
            pw.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
            pw.println("<phoneMe>");
            pw.println("\t<Contacts>");
            ArrayList<Contact> cons = cm.getContacts();
            for (int i = 0; i < cons.size(); i++) {
                pw.println(parseContact(cons.get(i)));
            }
            pw.println("\t</Contacts>");
            pw.println("</phoneMe>");
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public String parseContact(Contact con) {
        String ret = null;
        String name = xmlFormatString(con.getName());
        String phoneNumber = con.getPhoneNumber();
        String fixedTel = con.getFixedTel();
        String email = xmlFormatString(con.getEmail());
        String qq = con.getQq();
        String nick = xmlFormatString(con.getNick());
        String sex = "";
        if (con.getSex() == 1) sex = "M"; else if (con.getSex() == 2) sex = "F";
        String birthday = con.getBirthday().toString();
        String icon = xmlFormatString(con.getIcon());
        String address = xmlFormatString(con.getAddress());
        String workplace = xmlFormatString(con.getWorkplace());
        String zipCode = con.getZipCode();
        String homePage = xmlFormatString(con.getHomepage());
        String otherWay = "";
        ArrayList<String> ow = con.getOtherWay();
        for (int i = 0; i < ow.size(); i++) {
            otherWay = otherWay + ow.get(i) + ";";
        }
        otherWay = xmlFormatString(otherWay);
        String normal = "";
        ArrayList<String> nn = con.getNormalNames();
        for (int i = 0; i < nn.size(); i++) {
            normal = normal + nn.get(i) + ";";
        }
        normal = xmlFormatString(normal);
        String relation = "";
        ArrayList<RelationTag> rts = con.getRelation();
        ArrayList<String> relationNames = null;
        String reTagName = null;
        for (int i = 0; i < rts.size(); i++) {
            relationNames = rts.get(i).getContactNames(con);
            reTagName = rts.get(i).getName();
            for (int j = 0; j < relationNames.size(); j++) {
                relation = relation + "(" + reTagName + "," + con.getName() + "," + relationNames.get(i) + ");";
            }
        }
        ret = "\t\t<Contact>\n\t\t\t<name>" + name + "</name>\n\t\t\t<phoneNumber>" + phoneNumber + "</phoneNumber>\n\t\t\t<fixedTel>" + fixedTel + "</fixedTel>\n\t\t\t<email>" + email + "</email>\n\t\t\t<qq>" + qq + "</qq>\n\t\t\t<nick>" + nick + "</nick>\n\t\t\t<sex>" + sex + "</sex>" + "\n\t\t\t<birthDay>" + birthday + "</birthDay>\n\t\t\t<icon>" + icon + "</icon>\n\t\t\t<address>" + address + "</address>\n\t\t\t<workplace>" + workplace + "</workplace>\n\t\t\t<zipCode>" + zipCode + "</zipCode>\n\t\t\t<homePage>" + homePage + "</homePage>\n\t\t\t<otherWay>" + otherWay + "</otherWay>\n\t\t\t<normal>" + normal + "</normal>\n\t\t\t<relation>" + relation + "</relation>\n\t\t</Contact>\n";
        return ret;
    }

    public String xmlFormatString(String ori) {
        if (ori == null) return "";
        String ret = ori.trim();
        ret.replaceAll("&", "&amp");
        ret.replaceAll("<", "&lt");
        ret.replaceAll(">", "&gt");
        ret.replaceAll("\\\"", "&quot");
        ret.replaceAll("\\'", "&apos");
        return ret;
    }

    public String antiXMLFormatString(String ori) {
        if (ori == null) return "";
        String ret = ori.trim();
        ret.replaceAll("&apos", "\'");
        ret.replaceAll("&quot", "\\");
        ret.replaceAll("&gt", ">");
        ret.replaceAll("&lt", "<");
        ret.replaceAll("&amp", "&");
        return ret;
    }

    public ContactManager readFile(String fileName) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        Document doc = null;
        try {
            File f = new File(fileName);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element root = doc.getDocumentElement();
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (!(node instanceof Element)) continue;
            Element element = (Element) node;
            if (element.getTagName().equals("Contacts")) {
                NodeList contactNodes = element.getChildNodes();
                for (int j = 0; j < contactNodes.getLength(); j++) {
                    Node nd = contactNodes.item(j);
                    if (!(nd instanceof Element)) continue;
                    Element contact = (Element) nd;
                    contacts.add(parseXMLToContact(contacts, contact));
                }
            }
        }
        return new ContactManager(contacts);
    }

    public Contact parseXMLToContact(ArrayList<Contact> cons, Element con) {
        String name = null;
        String phoneNum = null;
        String fixedTel = null;
        String email = null;
        String qq = null;
        String nick = null;
        int sex = 0;
        BirthDay birth = null;
        String icon = null;
        String addr = null;
        String workplace = null;
        String zipCode = null;
        String homePage = null;
        ArrayList<String> otherWay = new ArrayList<String>();
        ArrayList<NormalTag> normal = new ArrayList<NormalTag>();
        ArrayList<String> re = new ArrayList<String>();
        NodeList children = con.getChildNodes();
        int j = 0;
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (!(child instanceof Element)) continue;
            Element e = (Element) child;
            if (!checkNode(j, e.getTagName())) {
                i--;
                j++;
                continue;
            }
            Text textNode = (Text) e.getFirstChild();
            if (textNode == null) {
                j++;
                continue;
            }
            String text = textNode.getData();
            text = antiXMLFormatString(text);
            switch(j) {
                case 0:
                    name = text;
                    break;
                case 1:
                    phoneNum = text;
                    break;
                case 2:
                    fixedTel = text;
                    break;
                case 3:
                    email = text;
                    break;
                case 4:
                    qq = text;
                    break;
                case 5:
                    nick = text;
                    break;
                case 6:
                    if (text.equals("M")) sex = 1; else if (text.equals("F")) sex = 2; else sex = 0;
                    break;
                case 7:
                    birth = new BirthDay(text);
                    break;
                case 8:
                    icon = text;
                    break;
                case 9:
                    addr = text;
                    break;
                case 10:
                    workplace = text;
                    break;
                case 11:
                    zipCode = text;
                    break;
                case 12:
                    homePage = text;
                    break;
                case 13:
                    String[] other = text.split(";");
                    for (int k = 0; k < other.length; k++) {
                        if (other[k].equals("")) continue;
                        otherWay.add(other[k]);
                    }
                    break;
                case 14:
                    String[] norm = text.split(";");
                    for (int k = 0; k < norm.length; k++) {
                        if (norm[k].equals("")) continue;
                        normal.add(NormalTag.createNormalTag(norm[k]));
                    }
                    break;
                case 15:
                    String[] rel = text.split(";");
                    for (int k = 0; k < rel.length; k++) {
                        if (rel[k].equals("")) continue;
                        re.add(rel[k]);
                    }
                    break;
            }
            j++;
        }
        try {
            Contact ret = Contact.createContact(name, phoneNum, fixedTel, email, qq, nick, sex, birth, icon, addr, workplace, zipCode, homePage, otherWay, normal, null);
            for (int i = 0; i < re.size(); i++) {
                String[] rel = re.get(i).split("[(),]");
                ArrayList<Contact> B = new ArrayList<Contact>();
                RelationTag rt = null;
                String rtname = null;
                boolean conti = false;
                j = 0;
                for (int k = 0; k < rel.length; k++) {
                    rel[k] = rel[k].trim();
                    if (rel[k].equals("")) continue;
                    switch(j) {
                        case 0:
                            rtname = rel[k];
                            break;
                        case 1:
                            if (!rel[k].equals(ret.getName())) conti = true;
                            break;
                        case 2:
                            for (int kk = 0; kk < cons.size(); kk++) {
                                if (cons.get(kk).getName().equals(rel[k])) B.add(cons.get(kk));
                            }
                            break;
                    }
                    if (conti) break;
                    j++;
                }
                if (conti) continue;
                if (B.size() == 0) continue;
                rt = RelationTag.createRelationTag(rtname);
                for (int k = 0; k < B.size(); k++) {
                    ContactManager.addTagToContact(new Pair<Contact, Contact>(ret, B.get(k)), rt);
                }
            }
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkNode(int k, String name) {
        switch(k) {
            case 0:
                return name.equals("name");
            case 1:
                return name.equals("phoneNumber");
            case 2:
                return name.equals("fixedTel");
            case 3:
                return name.equals("email");
            case 4:
                return name.equals("qq");
            case 5:
                return name.equals("nick");
            case 6:
                return name.equals("sex");
            case 7:
                return name.equals("birthDay");
            case 8:
                return name.equals("icon");
            case 9:
                return name.equals("address");
            case 10:
                return name.equals("workplace");
            case 11:
                return name.equals("zipCode");
            case 12:
                return name.equals("homePage");
            case 13:
                return name.equals("otherWay");
            case 14:
                return name.equals("normal");
            case 15:
                return name.equals("relation");
        }
        return false;
    }

    public static void main(String[] args) {
        ContactManager conM = xml.readFile("./data/Test.xml");
        xml.writeFile(conM, "./data/Test_write.xml");
        ContactManager conM1 = xml.readFile("./data/Test_write.xml");
        xml.writeFile(conM1, "./data/Test_write1.xml");
        ContactManager conM2 = xml.readFile("./data/Test_write1.xml");
        xml.writeFile(conM2, "./data/Test_write2.xml");
        ContactManager conM3 = xml.readFile("./data/Test_write2.xml");
        xml.writeFile(conM3, "./data/Test_write3.xml");
    }
}
