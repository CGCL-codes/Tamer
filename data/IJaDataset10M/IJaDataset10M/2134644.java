package net.wimpi.pim.contact.io.vcard;

import java.util.Iterator;
import net.wimpi.pim.Pim;
import net.wimpi.pim.contact.model.Communications;
import net.wimpi.pim.contact.model.Contact;
import net.wimpi.pim.contact.model.PhoneNumber;
import net.wimpi.pim.util.StringUtil;
import net.wimpi.pim.util.versitio.versitItem;
import net.wimpi.pim.util.versitio.versitToken;

/**
 * Class implementing a handler for the <tt>TEL</tt>
 * item of the vCard Mime directory profile
 * standard specification.<p>
 *
 * For reference see RFC 2426:<br>
 * 3.3.1 TEL Type Definition<br>
 * <p>
 * This implementation is able to read v2.1 and
 * to read/write v3.0.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
class TELItemHandler implements ItemHandler {

    public void handleItem(Contact ct, versitItem item) {
        boolean pref = false;
        PhoneNumber tel = Pim.getContactModelFactory().createPhoneNumber();
        tel.setNumber(item.getDecodedValue());
        String[] components = item.getParameter(versitToken.TYPE);
        if (components == null) {
            components = item.getParameter(versitToken.DEFAULT);
        }
        if (components != null) {
            for (int i = 0; i < components.length; i++) {
                String type = components[i].toUpperCase();
                if (type.equals(versitToken.TYPE_HOME)) {
                    tel.setHome(true);
                } else if (type.equals(versitToken.TYPE_WORK)) {
                    tel.setWork(true);
                } else if (type.equals(versitToken.TYPE_VOICE)) {
                    tel.setVoice(true);
                } else if (type.equals(versitToken.TYPE_FAX)) {
                    tel.setFax(true);
                } else if (type.equals(versitToken.TYPE_MSG)) {
                    tel.setMessaging(true);
                } else if (type.equals(versitToken.TYPE_CELL)) {
                    tel.setCellular(true);
                } else if (type.equals(versitToken.TYPE_PAGER)) {
                    tel.setPager(true);
                } else if (type.equals(versitToken.TYPE_BBS)) {
                    tel.setBBS(true);
                } else if (type.equals(versitToken.TYPE_MODEM)) {
                    tel.setMODEM(true);
                } else if (type.equals(versitToken.TYPE_CAR)) {
                    tel.setCellular(true);
                } else if (type.equals(versitToken.TYPE_ISDN)) {
                    tel.setISDN(true);
                } else if (type.equals(versitToken.TYPE_PREF)) {
                    tel.setPreferred(true);
                } else if (type.equals(versitToken.TYPE_VIDEO)) {
                    tel.setVideo(true);
                } else if (type.equals(versitToken.TYPE_CAR)) {
                    tel.setCar(true);
                } else if (type.equals(versitToken.TYPE_PCS)) {
                    tel.setPCS(true);
                } else if (type.equals(versitToken.TYPE_PREF)) {
                    pref = true;
                }
            }
        } else {
            tel.setVoice(true);
        }
        Communications comm = ct.getCommunications();
        if (comm == null) {
            comm = Pim.getContactModelFactory().createCommunications();
            ct.setCommunications(comm);
        }
        comm.addPhoneNumber(tel);
        if (pref) {
            comm.setPreferredPhoneNumber(tel);
        }
    }

    public versitItem[] createItems(Contact ct) {
        if (!ct.hasCommunications()) {
            return new versitItem[0];
        }
        Communications comm = ct.getCommunications();
        versitItem[] items = new versitItem[comm.getPhoneNumberCount()];
        int i = 0;
        for (Iterator iter = comm.getPhoneNumbers(); iter.hasNext(); i++) {
            PhoneNumber tel = (PhoneNumber) iter.next();
            if (tel == null) {
                return new versitItem[0];
            }
            String telnr = tel.getNumber();
            if (!StringUtil.isValidString(telnr)) {
                return new versitItem[0];
            }
            versitItem item = new versitItem(versitToken.TEL);
            if (tel.isPreferred()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_PREF);
            }
            if (tel.isWork()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_WORK);
            }
            if (tel.isHome()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_HOME);
            }
            if (tel.isVoice()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_VOICE);
            }
            if (tel.isFax()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_FAX);
            }
            if (tel.isMessaging()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_MSG);
            }
            if (tel.isCellular()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_CELL);
            }
            if (tel.isPager()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_PAGER);
            }
            if (tel.isBBS()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_BBS);
            }
            if (tel.isMODEM()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_MODEM);
            }
            if (tel.isISDN()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_ISDN);
            }
            if (tel.isVideo()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_VIDEO);
            }
            if (tel.isPCS()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_PCS);
            }
            if (tel.isCar()) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_CAR);
            }
            if (comm.isPreferredPhoneNumber(tel)) {
                item.addParameter(versitToken.TYPE, versitToken.TYPE_PREF);
            }
            item.setValue(telnr);
            items[i] = item;
        }
        return items;
    }
}
