package org.homedns.krolain.MochaJournal.Protocol;

import org.homedns.krolain.XMLRPC.*;
import java.util.Vector;

/**
 *
 * @author  jsmith
 */
public class XMLgetdaycounts extends XMLRPCObject {

    public static class Request extends XMLRPCLJ {

        private static final String[] m_ObjMember = { "usejournal" };

        public String m_usejournal = null;

        public Request() {
            super(m_ObjMember);
        }
    }

    public class XMLdaycount extends XMLRPCObject {

        public String m_date = null;

        public Integer m_count = null;

        public XMLdaycount() {
            super(null);
        }
    }

    public Vector m_daycounts = null;

    /** Creates a new instance of XMLgetdaycounts */
    public XMLgetdaycounts() {
        super(null);
    }

    public Object newStruct(String szMemberName) {
        if (szMemberName.compareToIgnoreCase("daycounts") == 0) return new XMLdaycount(); else return null;
    }
}
