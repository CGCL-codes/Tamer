package simpleorm.simplewebapp.eg.simple;

import simpleorm.simplewebapp.core.*;
import simpleorm.simplewebapp.scalarFields.WFieldString;
import simpleorm.simplewebapp.scalarFields.WFieldInteger;
import java.util.*;

/**
 * A simple demonstration of a List page.
 * The implementation of the logic is very manual, see WUserAutoListPage
 * for a more automated version.<p>
 *
 * (This is subtyped by WManualListPage and WAutoListPage just to
 * demonstrate two ways of implementing JSPs.  This style would not normally be
 * subtyped at all.)
 */
class WListPage extends WPage {

    public final WListPageletEg pagelet = new WListPageletEg(this);

    @Override
    protected void onInitialize() throws Exception {
        WTestDatabase.db.beginTransaction();
    }

    @Override
    protected void onFinalize() throws Exception {
        WTestDatabase.db.endTransaction();
    }

    public static class WListPageletEg extends WPageletList {

        final WFieldString nameWord = addField(searchFields, new WFieldString("nameWord"));

        final WField id = addField(listFields, new WFieldString("id").setNotRetrieved(true));

        final WField name = addField(listFields, new WFieldString("name").setNotRetrieved(true));

        final WField color = addField(listFields, new WFieldString("color").setNotRetrieved(true));

        final WField count = addField(listFields, new WFieldInteger("count").setNotRetrieved(true));

        Class<? extends WPage> crudClass;

        public WListPageletEg(WPage wpage) {
            super(wpage, "list");
        }

        Iterator iter;

        @Override
        public void onWasSubmitted() throws Exception {
            iter = WTestDatabase.db.getIterator(nameWord.getText(), sorter.getText());
        }

        @Override
        public boolean onListRow() {
            if (iter == null || !iter.hasNext()) return false;
            WBeanUtils.retrieveBeanProperties(iter.next(), listFields.getFields().values());
            listFields.setAnchorHRefFields(id);
            getPage().logPage();
            return true;
        }
    }
}
