package coollemon.dataBase;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import coollemon.kernel.*;
import au.com.bytecode.opencsv.*;

public class CSV extends DataFormat {

    /**
	 * @param args
	 */
    public static CSV csv = new CSV();

    private CSV() {
    }

    ;

    public ContactManager readFile(String filename) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        try {
            CSVReader reader = new CSVReader(new FileReader(filename), ',');
            List list = reader.readAll();
            for (int i = 1; i < list.size(); i++) {
                String[] line = (String[]) list.get(i);
                String name = line[0];
                String phoneNum = line[1];
                String fixedTel = line[2];
                String email = line[3];
                String qq = line[4];
                String nick = line[5];
                String sexStr = line[6];
                int sex = 0;
                if (sexStr.equalsIgnoreCase("M")) sex = 1; else if (sexStr.equalsIgnoreCase("F")) sex = 2; else ;
                BirthDay birth = new BirthDay(line[7]);
                String icon = line[8];
                String addr = line[9];
                String workplace = line[10];
                String zipCode = line[11];
                String homePage = line[12];
                ArrayList<String> otherWay = new ArrayList<String>();
                String others = line[13];
                String regex = "([^;]+);*";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(others);
                while (matcher.find()) {
                    String str = matcher.group(1);
                    otherWay.add(str);
                }
                ArrayList<NormalTag> normal = new ArrayList<NormalTag>();
                String normals = line[14];
                matcher = pattern.matcher(normals);
                while (matcher.find()) {
                    String str = matcher.group(1);
                    normal.add(NormalTag.createNormalTag(str));
                }
                Contact contact = Contact.createContact(name, phoneNum, fixedTel, email, qq, nick, sex, birth, icon, addr, workplace, zipCode, homePage, otherWay, normal, null);
                contacts.add(contact);
                String relations = line[15];
                System.out.println(relations);
                regex = "(<([^;]+):([^;]+):([^;]+)>);*";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(relations);
                while (matcher.find()) {
                    String tag = matcher.group(2);
                    String name1 = matcher.group(3);
                    String name2 = matcher.group(4);
                    RelationTag relationTag = RelationTag.createRelationTag(tag);
                    ArrayList<Contact> B = new ArrayList<Contact>();
                    for (int j = 0; j < contacts.size(); j++) {
                        if (contacts.get(j).getName().equals(name2)) B.add(contacts.get(j));
                    }
                    System.out.println(B.size());
                    if (contact.getName().equals(name1)) {
                        for (int k = 0; k < B.size(); k++) {
                            ContactManager.addTagToContact(new Pair<Contact, Contact>(contact, B.get(k)), relationTag);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContactManager(contacts);
    }

    public boolean writeFile(ContactManager conM, String filename) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(filename), ',');
            ArrayList<String[]> lines = new ArrayList<String[]>();
            String[] head = { "name", "phoneNumber", "fixedTel", "email", "qq", "nick", "sex", "birthday", "icon", "address", "workplace", "zipCode", "homePage", "otherWay", "normal", "relation" };
            lines.add(head);
            ArrayList<Contact> contacts = conM.getContacts();
            for (int i = 0; i < contacts.size(); i++) {
                Contact contact = contacts.get(i);
                String name = contact.getName();
                String phoneNum = contact.getPhoneNumber();
                String fixedTel = contact.getFixedTel();
                String email = contact.getEmail();
                String qq = contact.getQq();
                String nick = contact.getNick();
                String sexStr = "";
                switch(contact.getSex()) {
                    case 1:
                        sexStr = "M";
                        break;
                    case 2:
                        sexStr = "F";
                        break;
                    default:
                        break;
                }
                String birthday = contact.getBirthday().toString();
                String icon = contact.getIcon();
                String addr = contact.getAddress();
                String workplace = contact.getWorkplace();
                String zipCode = contact.getZipCode();
                String homePage = contact.getHomepage();
                ArrayList<String> otherWays = contact.getOtherWay();
                String otherWayStr = "";
                if (otherWays.size() > 0) {
                    for (int j = 0; j < otherWays.size() - 1; j++) {
                        otherWayStr += otherWays.get(j);
                        otherWayStr += ";";
                    }
                    otherWayStr += otherWays.get(otherWays.size() - 1);
                }
                ArrayList<NormalTag> normals = contact.getNormal();
                String normalStr = "";
                if (normals.size() > 0) {
                    for (int j = 0; j < normals.size() - 1; j++) {
                        normalStr += normals.get(j).getName();
                        normalStr += ";";
                    }
                    normalStr += normals.get(normals.size() - 1).getName();
                }
                ArrayList<RelationTag> relations = contact.getRelation();
                String relationStr = "";
                for (int j = 0; j < relations.size(); j++) {
                    RelationTag relationTag = relations.get(j);
                    ArrayList<Contact> cons = relationTag.getContacts(contact);
                    for (int k = 0; k < cons.size(); k++) {
                        relationStr = relationStr + "<" + relationTag.getName() + ":" + contact.getName() + ":" + cons.get(k).getName() + ">" + ";";
                    }
                }
                if (!relationStr.isEmpty()) relationStr = relationStr.substring(0, relationStr.length() - 1);
                String[] line = { name, phoneNum, fixedTel, email, qq, nick, sexStr, birthday, icon, addr, workplace, zipCode, homePage, otherWayStr, normalStr, relationStr };
                lines.add(line);
            }
            writer.writeAll(lines);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        ContactManager conM = csv.readFile("./data/Test.csv");
        csv.writeFile(conM, "./data/Test_write.csv");
        ContactManager conM1 = csv.readFile("./data/Test_write.csv");
        csv.writeFile(conM1, "./data/Test_write1.csv");
    }
}
