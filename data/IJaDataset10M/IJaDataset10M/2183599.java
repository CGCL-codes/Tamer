package org.dyndns.doujindb.db.cayenne;

import org.dyndns.doujindb.db.cayenne.auto._Content;

@SuppressWarnings("serial")
public class Content extends _Content {

    @Override
    protected void postAdd() {
        super.setTagName("");
        super.setInfo("");
        super.setRecycled(false);
    }
}
