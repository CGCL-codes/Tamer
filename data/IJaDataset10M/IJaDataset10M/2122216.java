package edu.columbia.hypercontent.editors.vcard.commands;

import edu.columbia.hypercontent.CMSException;
import edu.columbia.hypercontent.editors.BaseSessionData;
import edu.columbia.hypercontent.editors.ICommand;
import edu.columbia.hypercontent.editors.vcard.SessionData;
import edu.columbia.hypercontent.editors.vcard.VCardHelper;
import edu.columbia.hypercontent.editors.vcard.model.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: alex
 * Date: Aug 29, 2003
 * Time: 5:13:18 PM
 * To change this template use Options | File Templates.
 */
public class UpdateVCard implements ICommand, IVCardConstants {

    protected static String getParm(SessionData session, String name) {
        String parm = null;
        String test = session.runtimeData.getParameter(name);
        if (test != null && test.trim().length() > 0) {
            parm = test;
        }
        return parm;
    }

    public void execute(BaseSessionData baseSession) throws Exception {
        SessionData session = (SessionData) baseSession;
        String FNs = session.runtimeData.getParameter(FN);
        if (FNs != null) {
            session.card.getObject(FN).setValue(FNs);
        }
        String HP = session.runtimeData.getParameter("honorificPrefix");
        if (HP != null) {
            session.card.getN().honorificPrefixes = new String[] { HP };
        }
        String fam = session.runtimeData.getParameter("familyName");
        if (fam != null) {
            session.card.getN().familyName = new String[] { fam };
        }
        String giv = session.runtimeData.getParameter("givenName");
        if (giv != null) {
            session.card.getN().givenName = new String[] { giv };
        }
        String add = session.runtimeData.getParameter("additionalName");
        if (add != null) {
            session.card.getN().additionalNames = new String[] { add };
        }
        String suf = session.runtimeData.getParameter("honorificSuffix");
        if (suf != null) {
            session.card.getN().honorificSuffixes = new String[] { suf };
        }
        String title = session.runtimeData.getParameter("TITLE");
        if (giv != null) {
            session.card.getObject(TITLE).setValue(title);
        }
        String nick = session.runtimeData.getParameter("NICKNAME");
        if (nick != null) {
            session.card.getObject(NICKNAME).setValue(nick);
        }
        String url = session.runtimeData.getParameter("URL");
        if (url != null) {
            session.card.getObject(URL).setValue(url);
        }
        String sort = session.runtimeData.getParameter("SORT_STRING");
        if (sort != null) {
            session.card.getObject(SORT_STRING).setValue(sort);
        }
        String uid = session.runtimeData.getParameter("UID");
        if (uid != null) {
            session.card.getObject(UID).setValue(uid);
        }
        String note = session.runtimeData.getParameter("NOTE");
        if (note != null) {
            session.card.getObject(NOTE).setValue(note);
        }
        int adrnum = 1;
        String prefix = "adr" + adrnum + ":";
        if (session.runtimeData.getParameter(prefix + "streetAddress") != null) {
            session.card.delete(session.card.getAdrs());
            while (session.runtimeData.getParameter(prefix + "streetAddress") != null) {
                processAddress(session, prefix);
                adrnum++;
                prefix = "adr" + adrnum + ":";
            }
        }
        int emailnum = 1;
        prefix = "email" + emailnum + ":";
        String e = session.runtimeData.getParameter(prefix + "address");
        if (e != null) {
            session.card.delete(session.card.getObjects(session.card.EMAIL));
            while (e != null) {
                processEmail(session, prefix);
                emailnum++;
                prefix = "email" + emailnum + ":";
                e = session.runtimeData.getParameter(prefix + "address");
            }
        }
        int telnum = 1;
        prefix = "tel" + telnum + ":";
        String t = session.runtimeData.getParameter(prefix + "phoneNumber");
        if (t != null) {
            session.card.delete(session.card.getObjects(session.card.TEL));
            while (t != null) {
                processTel(session, prefix);
                telnum++;
                prefix = "tel" + telnum + ":";
                t = session.runtimeData.getParameter(prefix + "phoneNumber");
            }
        }
        processOrg(session);
        VCardHelper.updateSessionFromVCard(session);
    }

    protected static void processOrg(SessionData session) throws CMSException {
        int orgnum = 1;
        String prefix = "org" + orgnum;
        String o = session.runtimeData.getParameter(prefix);
        if (o != null) {
            session.card.delete(session.card.getObjects(session.card.ORG));
            VCardStructuredList org = (VCardStructuredList) session.card.newObject(session.card.ORG);
            ArrayList units = new ArrayList();
            while (o != null) {
                units.add(o);
                orgnum++;
                prefix = "org" + orgnum;
                o = session.runtimeData.getParameter(prefix);
            }
            org.setList((String[]) units.toArray(new String[0]));
        }
    }

    protected static void processEmail(SessionData session, String prefix) throws CMSException {
        VCardObject o = session.card.newObject(session.card.EMAIL);
        o.setValue(session.runtimeData.getParameter(prefix + "address"));
        o.getParameters().add(o.TYPE, "internet");
        String pref = session.runtimeData.getParameter("email:preferred");
        if (pref != null && pref.equals(prefix)) {
            o.getParameters().add(o.TYPE, "pref");
        }
    }

    protected static void processTel(SessionData session, String prefix) throws CMSException {
        VCardObject o = session.card.newObject(session.card.TEL);
        o.setValue(session.runtimeData.getParameter(prefix + "phoneNumber"));
        String pref = session.runtimeData.getParameter("tel:preferred");
        if (pref != null && pref.equals(prefix)) {
            o.getParameters().add(o.TYPE, "pref");
        }
        String type = session.runtimeData.getParameter(prefix + "type");
        if (type != null) {
            o.getParameters().add(o.TYPE, type);
        }
    }

    protected static void processAddress(SessionData session, String prefix) {
        VCardAdr adr = session.card.newAdr();
        String poBox = session.runtimeData.getParameter(prefix + "poBox");
        if (poBox != null) {
            adr.poBox = poBox;
        }
        String extendedAddress = session.runtimeData.getParameter(prefix + "extendedAddress");
        if (extendedAddress != null) {
            adr.extendedAddress = extendedAddress;
        }
        String streetAddress = session.runtimeData.getParameter(prefix + "streetAddress");
        if (streetAddress != null) {
            adr.streetAddress = streetAddress;
        }
        String locality = session.runtimeData.getParameter(prefix + "locality");
        if (locality != null) {
            adr.locality = locality;
        }
        String region = session.runtimeData.getParameter(prefix + "region");
        if (region != null) {
            adr.region = region;
        }
        String postalCode = session.runtimeData.getParameter(prefix + "postalCode");
        if (postalCode != null) {
            adr.postalCode = postalCode;
        }
        String countryName = session.runtimeData.getParameter(prefix + "countryName");
        if (countryName != null) {
            adr.countryName = countryName;
        }
        VCardObjectParameters parms = adr.getParameters();
        String t1 = session.runtimeData.getParameter(prefix + "type1");
        if (t1 != null && t1.trim().length() > 0) {
            parms.add(parms.TYPE, t1);
        }
        String t2 = session.runtimeData.getParameter(prefix + "type2");
        if (t2 != null && t2.trim().length() > 0) {
            parms.add(parms.TYPE, t2);
        }
        String t3 = session.runtimeData.getParameter(prefix + "type3");
        if (t3 != null && t3.trim().length() > 0) {
            parms.add(parms.TYPE, t3);
        }
        String t4 = session.runtimeData.getParameter(prefix + "type4");
        if (t4 != null && t4.trim().length() > 0) {
            parms.add(parms.TYPE, t4);
        }
        String t5 = session.runtimeData.getParameter(prefix + "type5");
        if (t5 != null && t5.trim().length() > 0) {
            parms.add(parms.TYPE, t5);
        }
        String pref = session.runtimeData.getParameter("adr:pref");
        if (pref != null && pref.equals(prefix)) {
            parms.add(parms.TYPE, "pref");
        }
    }
}
