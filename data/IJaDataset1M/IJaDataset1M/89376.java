package krico.javali.model;

import java.util.Date;
import krico.util.ObjectRelation;

/**
 * This is the object relation used by JavaliFolder to sort messages
 */
public class MessageObjectRelation extends ObjectRelation {

    ObjectRelation primary = null;

    ObjectRelation relations[] = null;

    boolean ascending = false;

    public MessageObjectRelation(int sortBy, int defaults[], boolean ascending) {
        this.ascending = ascending;
        primary = ALL[sortBy];
        boolean wrong = false;
        relations = new ObjectRelation[defaults.length];
        for (int i = 0; i < relations.length; i++) if (defaults[i] < 0 || defaults[i] >= ALL.length) relations[i] = OR_NULL; else relations[i] = ALL[defaults[i]];
    }

    public ObjectRelation.Comparison compare(Object o1, Object o2) {
        Comparison cmp = primary.compare(o1, o2);
        if (cmp == EQ) for (int i = 0; i < relations.length && cmp == EQ; i++) cmp = relations[i].compare(o1, o2);
        if (cmp != EQ) if (ascending) return cmp; else return cmp == LT ? GT : LT;
        return cmp;
    }

    /**
   * An object relation that allways returns EQ (used as pseudo NULL)
   */
    public static final ObjectRelation OR_NULL = new ObjectRelation() {

        public ObjectRelation.Comparison compare(Object o1, Object o2) {
            return EQ;
        }
    };

    /**
   * An object relation to sort messages by number field
   */
    public static final ObjectRelation OR_NUMBER = new ObjectRelation() {

        public ObjectRelation.Comparison compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return EQ;
            if (o1 == null) return LT;
            if (o2 == null) return GT;
            JavaliMessage m1 = (JavaliMessage) o1;
            JavaliMessage m2 = (JavaliMessage) o2;
            if (m1.getMessageNumber() == m2.getMessageNumber()) return EQ;
            if (m1.getMessageNumber() < m2.getMessageNumber()) return LT;
            return GT;
        }
    };

    /**
   * An object relation to sort messages by BCC field (uses the toString method on addresses)
   */
    public static final ObjectRelation OR_BCC = new ObjectRelation() {

        public ObjectRelation.Comparison compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return EQ;
            if (o1 == null) return LT;
            if (o2 == null) return GT;
            JavaliMessage m1 = (JavaliMessage) o1;
            JavaliMessage m2 = (JavaliMessage) o2;
            String m1str = null;
            String m2str = null;
            JavaliAddress m1a[] = m1.getBCC();
            JavaliAddress m2a[] = m2.getBCC();
            if (m1a == null && m2a == null) return EQ;
            if (m1a == null) return LT;
            if (m2a == null) return GT;
            m1str = "";
            m2str = "";
            for (int i = 0; i < m1a.length; i++) m1str += m1a[i].toString();
            for (int i = 0; i < m2a.length; i++) m2str += m2a[i].toString();
            int cmp = m1str.compareTo(m2str);
            if (cmp == 0) return EQ; else if (cmp < 0) return LT; else return GT;
        }
    };

    /**
   * An object relation to sort messages by CC field (uses the toString method on addresses)
   */
    public static final ObjectRelation OR_CC = new ObjectRelation() {

        public ObjectRelation.Comparison compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return EQ;
            if (o1 == null) return LT;
            if (o2 == null) return GT;
            JavaliMessage m1 = (JavaliMessage) o1;
            JavaliMessage m2 = (JavaliMessage) o2;
            String m1str = null;
            String m2str = null;
            JavaliAddress m1a[] = m1.getCC();
            JavaliAddress m2a[] = m2.getCC();
            if (m1a == null && m2a == null) return EQ;
            if (m1a == null) return LT;
            if (m2a == null) return GT;
            m1str = "";
            m2str = "";
            for (int i = 0; i < m1a.length; i++) m1str += m1a[i].toString();
            for (int i = 0; i < m2a.length; i++) m2str += m2a[i].toString();
            int cmp = m1str.compareTo(m2str);
            if (cmp == 0) return EQ; else if (cmp < 0) return LT; else return GT;
        }
    };

    /**
   * An object relation to sort messages by from field (uses the toString method on addresses)
   */
    public static final ObjectRelation OR_FROM = new ObjectRelation() {

        public ObjectRelation.Comparison compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return EQ;
            if (o1 == null) return LT;
            if (o2 == null) return GT;
            JavaliMessage m1 = (JavaliMessage) o1;
            JavaliMessage m2 = (JavaliMessage) o2;
            String m1str = null;
            String m2str = null;
            JavaliAddress m1a[] = m1.getFrom();
            JavaliAddress m2a[] = m2.getFrom();
            if (m1a == null && m2a == null) return EQ;
            if (m1a == null) return LT;
            if (m2a == null) return GT;
            m1str = "";
            m2str = "";
            for (int i = 0; i < m1a.length; i++) m1str += m1a[i].toString();
            for (int i = 0; i < m2a.length; i++) m2str += m2a[i].toString();
            int cmp = m1str.compareTo(m2str);
            if (cmp == 0) return EQ; else if (cmp < 0) return LT; else return GT;
        }
    };

    public static final ObjectRelation OR_RECEIVED_DATE = new ObjectRelation() {

        public ObjectRelation.Comparison compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return EQ;
            if (o1 == null) return LT;
            if (o2 == null) return GT;
            JavaliMessage m1 = (JavaliMessage) o1;
            JavaliMessage m2 = (JavaliMessage) o2;
            Date d1 = m1.getSentDate();
            Date d2 = m2.getSentDate();
            if (d1 == null && d2 == null) return EQ;
            if (d1 == null) return LT;
            if (d2 == null) return GT;
            int cmp = d1.compareTo(d2);
            if (cmp == 0) return EQ; else if (cmp < 0) return LT; else return GT;
        }
    };

    public static final ObjectRelation OR_SENT_DATE = new ObjectRelation() {

        public ObjectRelation.Comparison compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return EQ;
            if (o1 == null) return LT;
            if (o2 == null) return GT;
            JavaliMessage m1 = (JavaliMessage) o1;
            JavaliMessage m2 = (JavaliMessage) o2;
            Date d1 = m1.getSentDate();
            Date d2 = m2.getSentDate();
            if (d1 == null && d2 == null) return EQ;
            if (d1 == null) return LT;
            if (d2 == null) return GT;
            int cmp = d1.compareTo(d2);
            if (cmp == 0) return EQ; else if (cmp < 0) return LT; else return GT;
        }
    };

    public static final ObjectRelation OR_SIZE = new ObjectRelation() {

        public ObjectRelation.Comparison compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return EQ;
            if (o1 == null) return LT;
            if (o2 == null) return GT;
            JavaliMessage m1 = (JavaliMessage) o1;
            JavaliMessage m2 = (JavaliMessage) o2;
            int s1 = m1.getSize();
            int s2 = m2.getSize();
            if (s1 == s2) return EQ; else if (s1 < s2) return LT; else return GT;
        }
    };

    public static final ObjectRelation OR_SUBJECT = new ObjectRelation() {

        public ObjectRelation.Comparison compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return EQ;
            if (o1 == null) return LT;
            if (o2 == null) return GT;
            JavaliMessage m1 = (JavaliMessage) o1;
            JavaliMessage m2 = (JavaliMessage) o2;
            String d1 = m1.getSubject();
            String d2 = m2.getSubject();
            if (d1 == null && d2 == null) return EQ;
            if (d1 == null) return LT;
            if (d2 == null) return GT;
            int cmp = d1.compareTo(d2);
            if (cmp == 0) return EQ; else if (cmp < 0) return LT; else return GT;
        }
    };

    /**
   * An object relation to sort messages by BCC field (uses the toString method on addresses)
   */
    public static final ObjectRelation OR_TO = new ObjectRelation() {

        public ObjectRelation.Comparison compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return EQ;
            if (o1 == null) return LT;
            if (o2 == null) return GT;
            JavaliMessage m1 = (JavaliMessage) o1;
            JavaliMessage m2 = (JavaliMessage) o2;
            String m1str = null;
            String m2str = null;
            JavaliAddress m1a[] = m1.getTO();
            JavaliAddress m2a[] = m2.getTO();
            if (m1a == null && m2a == null) return EQ;
            if (m1a == null) return LT;
            if (m2a == null) return GT;
            m1str = "";
            m2str = "";
            for (int i = 0; i < m1a.length; i++) m1str += m1a[i].toString();
            for (int i = 0; i < m2a.length; i++) m2str += m2a[i].toString();
            int cmp = m1str.compareTo(m2str);
            if (cmp == 0) return EQ; else if (cmp < 0) return LT; else return GT;
        }
    };

    public static final ObjectRelation OR_ANSWERED = new ObjectRelation() {

        public ObjectRelation.Comparison compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return EQ;
            if (o1 == null) return LT;
            if (o2 == null) return GT;
            JavaliMessage m1 = (JavaliMessage) o1;
            JavaliMessage m2 = (JavaliMessage) o2;
            boolean b1 = m1.isAnswered();
            boolean b2 = m2.isAnswered();
            if (b1 && b2) return EQ;
            if (!b1) return LT;
            if (!b2) return GT;
            return EQ;
        }
    };

    public static final ObjectRelation OR_NEW = new ObjectRelation() {

        public ObjectRelation.Comparison compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return EQ;
            if (o1 == null) return LT;
            if (o2 == null) return GT;
            JavaliMessage m1 = (JavaliMessage) o1;
            JavaliMessage m2 = (JavaliMessage) o2;
            boolean b1 = m1.isNew();
            boolean b2 = m2.isNew();
            if (b1 && b2) return EQ;
            if (!b1) return LT;
            if (!b2) return GT;
            return EQ;
        }
    };

    public static final ObjectRelation OR_UNREAD = new ObjectRelation() {

        public ObjectRelation.Comparison compare(Object o1, Object o2) {
            if (o1 == null && o2 == null) return EQ;
            if (o1 == null) return LT;
            if (o2 == null) return GT;
            JavaliMessage m1 = (JavaliMessage) o1;
            JavaliMessage m2 = (JavaliMessage) o2;
            boolean b1 = m1.isUnread();
            boolean b2 = m2.isUnread();
            if (b1 && b2) return EQ;
            if (!b1) return LT;
            if (!b2) return GT;
            return EQ;
        }
    };

    /**
   * This array constains all ObjectRelations int a very defined order
   */
    public static final ObjectRelation ALL[] = new ObjectRelation[] { OR_NUMBER, OR_BCC, OR_CC, OR_FROM, OR_RECEIVED_DATE, OR_SENT_DATE, OR_SIZE, OR_SUBJECT, OR_TO, OR_ANSWERED, OR_NEW, OR_UNREAD };
}
