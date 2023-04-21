package org.proteomecommons.MSExpedite.SignalProcessing;

import java.util.LinkedList;

/**
 *
 * @author takis
 */
public class AttrAccessor {

    /** Creates a new instance of AttrAccssor */
    public AttrAccessor() {
    }

    public static String[] getUsernames(SettingsAttr sa) {
        UserAttr[] ua = sa.getUsers();
        String[] usernames = new String[ua.length];
        for (int i = 0; i < ua.length; i++) {
            usernames[i] = ua[i].getUsername();
        }
        return usernames;
    }

    public static MSLevelAttr getMSLevelAttr(SettingsAttr sa, String username, String instrumentAlias, int msOrder) {
        InstrumentAttr iattr = getInstrAttr(sa, username, instrumentAlias);
        MSLevelAttr msLevels[] = iattr.getMslevels();
        for (int i = 0; i < msLevels.length; i++) {
            int order = msLevels[i].getOrder();
            if (order == msOrder) {
                return msLevels[i];
            }
        }
        return null;
    }

    public static InstrumentAttr getInstrAttr(SettingsAttr sa, String username, String instrumentAlias) {
        UserAttr[] ua = sa.getUsers();
        for (int i = 0; i < ua.length; i++) {
            if (!ua[i].getUsername().equals(username)) continue;
            InstrumentAttr iattr[] = ua[i].getInstruments();
            for (int j = 0; j < iattr.length; j++) {
                if (iattr[j].getAlias().equals(instrumentAlias)) return iattr[j];
            }
        }
        return null;
    }

    public static InstrumentAttr[] getInstrAttrs(SettingsAttr sa, String username) {
        LinkedList<InstrumentAttr> l = new LinkedList<InstrumentAttr>();
        UserAttr[] ua = sa.getUsers();
        for (int i = 0; i < ua.length; i++) {
            if (!ua[i].getUsername().equals(username)) continue;
            InstrumentAttr iattr[] = ua[i].getInstruments();
            for (int j = 0; j < iattr.length; j++) {
                l.add(iattr[j]);
            }
        }
        return (InstrumentAttr[]) l.toArray(new InstrumentAttr[0]);
    }

    public static String[] getInstrShortDesc(SettingsAttr sa, String username) {
        LinkedList<String> l = new LinkedList<String>();
        UserAttr[] ua = sa.getUsers();
        for (int i = 0; i < ua.length; i++) {
            if (!ua[i].getUsername().equals(username)) continue;
            InstrumentAttr iattr[] = ua[i].getInstruments();
            for (int j = 0; j < iattr.length; j++) {
                l.add(iattr[j].getShortDescription());
            }
        }
        return (String[]) l.toArray(new String[0]);
    }

    public static String[] getMSLevelShortDescription(SettingsAttr sa, String username, String instrName) {
        LinkedList<String> l = new LinkedList<String>();
        UserAttr[] ua = sa.getUsers();
        for (int i = 0; i < ua.length; i++) {
            if (!ua[i].getUsername().equals(username)) continue;
            InstrumentAttr iattr[] = ua[i].getInstruments();
            for (int j = 0; j < iattr.length; j++) {
                String iname = iattr[j].getShortDescription();
                if (!iname.equals(instrName)) continue;
                MSLevelAttr level[] = iattr[j].getMslevels();
                for (int k = 0; k < level.length; k++) {
                    l.add(level[k].getName());
                }
            }
        }
        return (String[]) l.toArray(new String[0]);
    }

    public static MSFiltersAttr getPeakFiltersAttrs(SettingsAttr sa, String username, String instrName, String pdName) {
        UserAttr[] ua = sa.getUsers();
        for (int i = 0; i < ua.length; i++) {
            if (!ua[i].getUsername().equals(username)) continue;
            InstrumentAttr iattr[] = ua[i].getInstruments();
            for (int j = 0; j < iattr.length; j++) {
                if (!iattr[j].getShortDescription().equals(instrName)) continue;
                MSLevelAttr level[] = iattr[j].getMslevels();
                for (int k = 0; k < level.length; k++) {
                    String ln = level[k].getName();
                    if (!ln.equals(pdName)) continue;
                    return level[k].getFilter();
                }
            }
        }
        return null;
    }

    public static PeakDetectionDefinitionAttr getPeakDetectionAttrs(SettingsAttr sa, String username, String instrName, String pdName) {
        UserAttr[] ua = sa.getUsers();
        for (int i = 0; i < ua.length; i++) {
            if (!ua[i].getUsername().equals(username)) continue;
            InstrumentAttr iattr[] = ua[i].getInstruments();
            for (int j = 0; j < iattr.length; j++) {
                if (!iattr[j].getShortDescription().equals(instrName)) continue;
                MSLevelAttr level[] = iattr[j].getMslevels();
                for (int k = 0; k < level.length; k++) {
                    String ln = level[k].getName();
                    if (!ln.equals(pdName)) continue;
                    return level[k].getPeakDetectionDefinition();
                }
            }
        }
        return null;
    }
}
