package com.avaje.ebeaninternal.server.lucene;

import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import com.avaje.ebean.config.lucene.IndexDefnBuilder;
import com.avaje.ebean.config.lucene.IndexFieldDefn;
import com.avaje.ebean.config.lucene.IndexFieldDefn.Sortable;

public interface SpiIndexDefnHelper extends IndexDefnBuilder {

    public IndexFieldDefn addPrefixField(String prefix, String propertyName, Store store, Index index, Sortable sortable);

    public IndexFieldDefn addPrefixFieldConcat(String prefix, String fieldName, Store store, Index index, String[] propertyNames);
}
