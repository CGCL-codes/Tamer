package org.vrspace.neurogrid;

import org.vrspace.util.Logger;

/**
A keyword.
*/
public class Keyword extends NGObject {

    public String keyword;

    public static final String[] _indexUnique = new String[] { "keyword" };

    public Keyword() {
    }

    public Keyword(String keyword) {
        if (keyword == null) throw new NullPointerException("Null keyword");
        this.keyword = keyword;
    }

    /**
  Returns this keyword + "\0", intended use in SortedSet.subSet() as interval end value
  */
    public NGObject next() {
        if (keyword == null) throw new NullPointerException("Null keyword");
        Keyword ret = new Keyword();
        ret.setFields(this);
        ret.db_id = this.db_id;
        ret.keyword = this.keyword + "\0";
        return ret;
    }
}
