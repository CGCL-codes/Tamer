package net.wimpi.pim.contact.io.vcard;

import net.wimpi.pim.contact.model.Contact;
import net.wimpi.pim.util.StringUtil;
import net.wimpi.pim.util.versitio.versitItem;
import net.wimpi.pim.util.versitio.versitToken;

/**
 * Class implementing a handler for the <tt>CATEGORIES</tt>
 * item of the vCard Mime directory profile
 * standard specification.<p>
 *
 * For reference see RFC 2426:<br>
 * 3.6.1 CATEGORIES Type Definition<br>
 * <p>
 * This implementation is able to read v2.1 and
 * to read/write v3.0.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
class CATEGORIESItemHandler implements ItemHandler {

    public void handleItem(Contact ct, versitItem item) {
        String[] multi = StringUtil.split(item.getDecodedValue(), ",");
        for (int n = 0; n < multi.length; n++) {
            ct.addCategory(multi[n]);
        }
    }

    public versitItem[] createItems(Contact ct) {
        String[] cats = ct.listCategories();
        if (StringUtil.isValidStringArray(cats)) {
            versitItem[] items = new versitItem[1];
            versitItem item = new versitItem(versitToken.CATEGORIES);
            item.setValue(StringUtil.joinList(cats));
            items[0] = item;
            return items;
        } else {
            return new versitItem[0];
        }
    }
}
