package com.thegreatchina.im.msn;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

public class Contact implements Comparable<Contact> {

    public static Logger logger = Logger.getLogger(Contact.class);

    private String accountName;

    private String nickName;

    private Group[] groups;

    private boolean allow;

    private boolean block;

    private boolean forward;

    private boolean reverse;

    private Status status = Status.HIDDEN;

    public Contact() {
    }

    public static Contact fromILNString(String string) {
        Contact contact = new Contact();
        int start = "ILN".length() + 2;
        int end = string.indexOf(" ", start);
        start = end + 1;
        end = string.indexOf(" ", start);
        String st = string.substring(start, end);
        contact.status = Status.getStatus(st);
        logger.debug("fromILNString:" + st + "--" + string);
        start = end + 1;
        end = string.indexOf(" ", start);
        st = string.substring(start, end);
        contact.accountName = st;
        return contact;
    }

    public static Contact fromLSTString(String string) {
        Contact contact = new Contact();
        int start = "LST".length() + 1;
        int end = string.indexOf(" ", start);
        String st = string.substring(start, end);
        contact.accountName = st;
        start = end + 1;
        end = string.indexOf(" ", start);
        st = string.substring(start, end);
        try {
            contact.nickName = URLDecoder.decode(st, "utf-8");
        } catch (UnsupportedEncodingException e) {
            contact.nickName = st;
        }
        int listNumber = 0;
        String groupString = "";
        start = end + 1;
        end = string.indexOf(" ", start);
        if (end < 0) {
            listNumber = Integer.parseInt(string.substring(start));
        } else {
            listNumber = Integer.parseInt(string.substring(start, end));
            groupString = string.substring(end + 1);
        }
        contact.forward = (listNumber % 2 == 1) ? true : false;
        listNumber = listNumber / 2;
        contact.allow = (listNumber % 2 == 1) ? true : false;
        listNumber = listNumber / 2;
        contact.block = (listNumber % 2 == 1) ? true : false;
        listNumber = listNumber / 2;
        contact.reverse = (listNumber % 2 == 1) ? true : false;
        StringTokenizer token = new StringTokenizer(groupString, ",");
        contact.groups = new Group[token.countTokens()];
        for (int i = 0; i < contact.groups.length; i++) {
            String element = (String) token.nextElement();
            int g = Integer.parseInt(element);
            contact.groups[i] = new Group(g);
        }
        return contact;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public boolean isForward() {
        return forward;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

    public Group[] getGroups() {
        return groups;
    }

    public void setGroups(Group[] groups) {
        this.groups = groups;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public String toString() {
        String string = accountName + "," + nickName + "," + status;
        if ((groups != null) && (groups.length > 0)) {
            for (int i = 0; i < groups.length; i++) {
                string += "," + groups[i];
            }
        }
        return string;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStatusName() {
        return status.name();
    }

    public void copy(Contact c) {
        this.accountName = c.accountName;
        this.nickName = c.nickName;
        this.groups = c.groups;
        this.allow = c.allow;
        this.block = c.block;
        this.forward = c.forward;
        this.reverse = c.reverse;
        this.status = c.status;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Contact)) {
            return false;
        }
        Contact c = (Contact) obj;
        return this.accountName.equalsIgnoreCase(c.accountName);
    }

    public int compareTo(Contact c) {
        return this.accountName.compareToIgnoreCase(c.accountName);
    }

    public enum Status {

        AVAILABLE("NLN"), BUSY("BSY"), IDLE("IDL"), RIGHT_BACK("BRB"), AWAY("AWY"), ON_THE_PHONE("PHN"), OUT_TO_LUNCH("LUN"), HIDDEN("HDN");

        private String status;

        Status(String s) {
            this.status = s;
        }

        public static Status getStatus(String str) {
            if ("NLN".equalsIgnoreCase(str)) {
                return Status.AVAILABLE;
            } else if ("IDL".equalsIgnoreCase(str)) {
                return Status.IDLE;
            } else if ("BSY".equalsIgnoreCase(str)) {
                return Status.BUSY;
            } else if ("AWY".equalsIgnoreCase(str)) {
                return Status.AWAY;
            } else if ("PHN".equalsIgnoreCase(str)) {
                return Status.ON_THE_PHONE;
            } else if ("LUN".equalsIgnoreCase(str)) {
                return Status.OUT_TO_LUNCH;
            } else if ("BRB".equalsIgnoreCase(str)) {
                return Status.RIGHT_BACK;
            } else {
                return Status.HIDDEN;
            }
        }

        public boolean equal(Object obj) {
            return this.status.equals(obj);
        }

        public String toString() {
            return status;
        }

        public static void main(String args[]) {
            Contact.Status s1 = Status.AWAY;
            Contact.Status s2 = Status.AVAILABLE;
            System.out.println(s1 == s2);
        }
    }
}
