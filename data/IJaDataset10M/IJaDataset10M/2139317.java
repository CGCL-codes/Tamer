package org.watij.webspec.dsl;

/**
 * Created by IntelliJ IDEA.
 * User: bknorr
 * Date: Nov 29, 2010
 * Time: 10:10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class Child extends Find {

    protected Child(Tag tag) {
        super(tag);
    }

    public Tag tag(String tagName) {
        return ((Tag) finder).child(tagName);
    }
}
