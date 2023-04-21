package com.madzone.wiki.gae.ds.model;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class PageAttribute {

    @Persistent
    @PrimaryKey
    private Key key;
}
