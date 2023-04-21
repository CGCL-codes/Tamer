package coollemon.kernel;

import java.io.*;
import java.util.*;
import coollemon.dataBase.*;

/**
 * ��ϵ�˹����࣬�ṩһϵ�а�ȫ�Ľӿ�
 * @author Haivey
 *
 */
public class ContactManager implements Serializable {

    private static final long serialVersionUID = -5449133047335262593L;

    private ArrayList<Contact> contacts = new ArrayList<Contact>();

    private static ContactManager manager = new ContactManager();

    public ContactManager() {
    }

    public ContactManager(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public static ContactManager getManager() {
        return manager;
    }

    public void add(Contact con) {
        contacts.add(con);
    }

    /** A simple version addNewContact, 5 parameters
	 * You can change the Contact through various set functions in Contact class
	 * 
	 * @param name
	 * @param phoneNum
	 * @param fixedTel
	 * @param email
	 * @param qq
	 * @return
	 * @throws NoNameException
	 * @throws NoInfoException
	 * @throws InfoFormatException
	 */
    public Contact addNewContact(String name, String phoneNum, String fixedTel, String email, String qq) throws NoNameException, NoInfoException, InfoFormatException {
        return addNewContact(name, phoneNum, fixedTel, email, qq, null, 0, null, null, null, null, null, null, null, null, null);
    }

    /**
	 * This function has 16 parameters
	 */
    public Contact addNewContact(String name, String phoneNum, String fixedTel, String email, String qq, String nick, int sex, BirthDay birth, String icon, String addr, String workplace, String zipCode, String homePage, ArrayList<String> otherWay, ArrayList<NormalTag> normal, ArrayList<RelationTag> relation) throws NoNameException, NoInfoException, InfoFormatException {
        Contact con = Contact.createContact(name, phoneNum, fixedTel, email, qq, nick, sex, birth, icon, addr, workplace, zipCode, homePage, otherWay, normal, relation);
        this.add(con);
        return con;
    }

    /**
	 * �ϲ�cm����ǰContactManager
	 * @param cm
	 */
    public void mergeContactManager(ContactManager cm) {
        for (int i = 0; i < cm.contacts.size(); i++) {
            this.contacts.add(cm.contacts.get(i));
        }
    }

    /**
	 * �õ�������ϵ������
	 * @return
	 */
    public ArrayList<String> getContactNames() {
        ArrayList<String> ret = new ArrayList<String>();
        for (int i = 0; i < contacts.size(); i++) {
            ret.add(contacts.get(i).getName());
        }
        return ret;
    }

    /**
	 * ����ϵ�˴���ϵ���б��г���ɾ��
	 * @param con
	 * @return
	 */
    public boolean deleteContact(Contact con) {
        ArrayList<NormalTag> normal = con.getNormal();
        ArrayList<RelationTag> relation = con.getRelation();
        try {
            for (int i = 0; i < normal.size(); i++) {
                deleteTagFromContact(con, normal.get(i));
            }
            for (int i = 0; i < relation.size(); i++) {
                ArrayList<Contact> Bs = relation.get(i).getContacts(con);
                deleteRelationTagFromContact(con, Bs, relation.get(i));
            }
            contacts.remove(con);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
	 * ͨ��id����ɾ��
	 * @param id
	 * @return
	 */
    public boolean deleteById(int id) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId() == id) {
                return deleteContact(contacts.get(i));
            }
        }
        return false;
    }

    /**
	 * ��tag��ӵ�con��
	 * @param con
	 * @param tag
	 * @throws ClassNotMatchException
	 */
    public static void addTagToContact(ContactBehave con, Tag tag) throws ClassNotMatchException {
        con.addTag(tag);
        tag.addContact(con);
    }

    /**
	 * ɾ��con�е�tag
	 * @param con
	 * @param tag
	 * @throws ClassNotMatchException
	 */
    public static void deleteTagFromContact(ContactBehave con, Tag tag) throws ClassNotMatchException {
        tag.deleteContact(con);
        con.deleteTag(tag);
    }

    /**
	 * Ϊ��ϵ��ǩ������ṩ����ӿ�
	 * @param con1
	 * @param con2
	 * @param tag
	 * @throws ClassNotMatchException
	 */
    public static void addRelationTagToContact(Contact con1, Contact con2, RelationTag tag) throws ClassNotMatchException {
        addTagToContact(new Pair<Contact, Contact>(con1, con2), tag);
    }

    /**
	 * ��ӹ�ϵ��ǩ�ķ���ӿ�
	 * @param conA
	 * @param conB
	 * @param tag
	 * @throws ClassNotMatchException
	 */
    public static void addRelationTagToContact(Contact conA, ArrayList<Contact> conB, RelationTag tag) throws ClassNotMatchException {
        for (int i = 0; i < conB.size(); i++) {
            addTagToContact(new Pair<Contact, Contact>(conA, conB.get(i)), tag);
        }
    }

    /**
	 * Ϊ��ϵ��ǩ��ɾ���ṩ����ӿ�
	 * @param con1
	 * @param con2
	 * @param tag
	 * @throws ClassNotMatchException
	 */
    public static void deleteRelationTagFromContact(Contact con1, Contact con2, RelationTag tag) throws ClassNotMatchException {
        deleteTagFromContact(new Pair<Contact, Contact>(con1, con2), tag);
    }

    /**
	 * ɾ���ϵ��ǩ�ķ���ӿ�
	 * @param conA
	 * @param conB
	 * @param tag
	 * @throws ClassNotMatchException
	 */
    public static void deleteRelationTagFromContact(Contact conA, ArrayList<Contact> conB, RelationTag tag) throws ClassNotMatchException {
        for (int i = 0; i < conB.size(); i++) {
            deleteTagFromContact(new Pair<Contact, Contact>(conA, conB.get(i)), tag);
        }
    }

    public int size() {
        return this.contacts.size();
    }

    public ArrayList<Contact> getContacts() {
        return this.contacts;
    }

    public Contact findById(int id) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId() == id) return contacts.get(i);
        }
        return null;
    }

    public ContactManager findByName(String name) {
        ContactManager cm = new ContactManager();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getName().indexOf(name) != -1) cm.add(contacts.get(i));
        }
        return cm;
    }

    public ContactManager findByPhoneNumber(String phone) {
        ContactManager cm = new ContactManager();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getPhoneNumber().indexOf(phone) != -1) cm.add(contacts.get(i));
        }
        return cm;
    }

    public ContactManager findByFixedTel(String fix) {
        ContactManager cm = new ContactManager();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getFixedTel().indexOf(fix) != -1) cm.add(contacts.get(i));
        }
        return cm;
    }

    public ContactManager findByBirth(int y, int m, int d) {
        ContactManager cm = new ContactManager();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getBirthday().sameDay(y, m, d)) cm.add(contacts.get(i));
        }
        return cm;
    }

    public ContactManager findBySex(int sex) {
        if (sex < 1 || sex > 2) return new ContactManager();
        ContactManager cm = new ContactManager();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getSex() == sex) {
                cm.add(contacts.get(i));
            }
        }
        return cm;
    }

    public ContactManager findByEmail(String email) {
        ContactManager cm = new ContactManager();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getEmail().indexOf(email) != -1) cm.add(contacts.get(i));
        }
        return cm;
    }

    public ContactManager findByQQ(String qq) {
        ContactManager cm = new ContactManager();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getQq().indexOf(qq) != -1) cm.add(contacts.get(i));
        }
        return cm;
    }

    public ContactManager findNameBegin(String name) {
        ContactManager cm = new ContactManager();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getName().indexOf(name) == 0) cm.add(contacts.get(i));
        }
        return cm;
    }

    public ContactManager findPhoneNumberBegin(String phone) {
        ContactManager cm = new ContactManager();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getPhoneNumber().indexOf(phone) == 0) cm.add(contacts.get(i));
        }
        return cm;
    }

    public ContactManager findEmailBegin(String email) {
        ContactManager cm = new ContactManager();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getEmail().indexOf(email) == 0) cm.add(contacts.get(i));
        }
        return cm;
    }

    public ContactManager findQQBegin(String qq) {
        ContactManager cm = new ContactManager();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getQq().indexOf(qq) == 0) cm.add(contacts.get(i));
        }
        return cm;
    }

    public ContactManager findOtherContain(String other) {
        ContactManager cm = new ContactManager();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getOtherInfo().indexOf(other) != -1) cm.add(contacts.get(i));
        }
        return cm;
    }

    public void orderById() {
        Collections.sort(this.contacts, new Comparator<Contact>() {

            public int compare(Contact a, Contact b) {
                if (a.getId() < b.getId()) return -1;
                if (a.getId() > b.getId()) return 1;
                return 0;
            }

            public boolean equals(Object o) {
                return false;
            }
        });
    }

    public void orderByName() {
        Collections.sort(this.contacts, new Comparator<Contact>() {

            public int compare(Contact a, Contact b) {
                return a.getName().compareTo(b.getName());
            }

            public boolean equals(Object o) {
                return false;
            }
        });
    }

    public static ArrayList<String> parseOtherWay(String otherWay) {
        ArrayList<String> ret = new ArrayList<String>();
        String[] temp = otherWay.split(";|,|\\|");
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != null && temp[i].trim() != "") ret.add(temp[i]);
        }
        return ret;
    }

    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(manager);
            oos.writeObject(TagManager.tagManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readFile(String fileName) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            manager = (ContactManager) ois.readObject();
            TagManager.tagManager = (TagManager) ois.readObject();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        DataManager dataManager = DataManager.dataManager;
        manager = dataManager.readFile(fileName);
    }
}
