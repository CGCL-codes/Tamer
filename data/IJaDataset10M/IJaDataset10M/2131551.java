package net.wimpi.pim.contact.io.vcard;

import net.wimpi.pim.Pim;
import net.wimpi.pim.contact.model.Contact;
import net.wimpi.pim.contact.model.OrganizationalIdentity;
import net.wimpi.pim.util.StringUtil;
import net.wimpi.pim.util.versitio.versitItem;
import net.wimpi.pim.util.versitio.versitToken;

/**
 * Class implementing a handler for the <tt>TITLE</tt>
 * item of the vCard Mime directory profile
 * standard specification.<p>
 *
 * For reference see RFC 2426:<br>
 * 3.5.1 TITLE Type Definition<br>
 * <p>
 * This implementation is able to read v2.1 and
 * to read/write v3.0.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
class TITLEItemHandler implements ItemHandler {

    public void handleItem(Contact ct, versitItem item) {
        OrganizationalIdentity orgid = ct.getOrganizationalIdentity();
        if (orgid == null) {
            orgid = Pim.getContactModelFactory().createOrganizationalIdentity();
            ct.setOrganizationalIdentity(orgid);
        }
        orgid.setTitle(item.getDecodedValue());
    }

    public versitItem[] createItems(Contact ct) {
        OrganizationalIdentity org = ct.getOrganizationalIdentity();
        if (org == null) {
            return new versitItem[0];
        }
        String title = org.getTitle();
        if (!StringUtil.isValidString(title)) {
            return new versitItem[0];
        }
        versitItem[] items = new versitItem[1];
        versitItem item = new versitItem(versitToken.TITLE);
        item.setValue(title);
        items[0] = item;
        return items;
    }
}
