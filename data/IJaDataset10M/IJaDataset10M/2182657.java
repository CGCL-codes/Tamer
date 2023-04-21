package org.dyndns.doujindb.db.cayenne;

import org.dyndns.doujindb.db.cayenne.auto._Artist;

@SuppressWarnings("serial")
public class Artist extends _Artist {

    @Override
    protected void postAdd() {
        super.setJapaneseName("");
        super.setTranslatedName("");
        super.setRomanjiName("");
        super.setWeblink("");
        super.setRecycled(false);
    }
}
