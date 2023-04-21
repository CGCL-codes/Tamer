package net.sf.zcatalog.db;

import net.sf.zcatalog.ProgressObserver;
import net.sf.zcatalog.xml.jaxb.Meta;

/**
 *
 * @author Alessandro Zigliani
 */
public class MetaRecipientStub implements ZCatMetaRecipient {

    @Override
    public void begin(Meta root) {
    }

    @Override
    public void put(Meta parent, Meta child) {
    }

    @Override
    public ZCatObject commit(ProgressObserver o) throws Exception {
        return null;
    }
}
