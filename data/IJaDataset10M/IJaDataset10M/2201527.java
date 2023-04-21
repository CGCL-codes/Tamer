package com.notuvy.util;

import java.util.Date;
import java.io.Writer;
import java.io.IOException;

/**
 * A utility to build a single tag for an SGML document, or to recurively
 * build the entire document.  Some HTML shortcuts are provided by the
 * inner classes.
 *
 * User: murali
 * Date: Aug 12, 2008
 * Time: 2:31:20 PM
 */
public class SgmlTag {

    private final com.notuvy.sgml.SgmlTag fDelegate;

    /**
     * @deprecated Use com.notuvy.sgml.SgmlTag instead.
     * @param pTag  Tag name
     */
    @Deprecated
    public SgmlTag(String pTag) {
        fDelegate = new com.notuvy.sgml.SgmlTag(pTag);
    }

    public SgmlTag content(Object pObject) {
        return (pObject instanceof SgmlTag) ? addSubTree((SgmlTag) pObject) : addText(pObject);
    }

    public SgmlTag addSubTree(SgmlTag pTag) {
        fDelegate.addSubTree(pTag.fDelegate);
        return this;
    }

    public SgmlTag addText(Object pObject) {
        fDelegate.addText(pObject);
        return this;
    }

    public SgmlTag attribute(String pKey, Object pVal) {
        fDelegate.attribute(pKey, pVal);
        return this;
    }

    public SgmlTag parent(SgmlTag pParent) {
        fDelegate.parent(pParent.fDelegate);
        return this;
    }

    public SgmlTag newChild(String pTag) {
        SgmlTag child = new SgmlTag(pTag);
        addSubTree(child);
        return child;
    }

    public SgmlTag writeTo(StringBuilder pBuilder) {
        fDelegate.writeTo(pBuilder);
        return this;
    }

    public SgmlTag writeTo(Writer pWriter) throws IOException {
        fDelegate.writeTo(pWriter);
        return this;
    }

    public String toString() {
        return fDelegate.toString();
    }

    public void write(StringBuilder pBuilder) {
        fDelegate.write(pBuilder);
    }

    public void write(Writer pWriter) throws IOException {
        fDelegate.write(pWriter);
    }

    public static class A extends SgmlTag {

        private A(Object pHref) {
            super("a");
            attribute("href", pHref);
        }

        public A(Object pHref, SgmlTag pSgmlTag) {
            this(pHref);
            addSubTree(pSgmlTag);
        }

        public A(Object pHref, String pText) {
            this(pHref);
            addText(pText);
        }
    }

    public static class Html extends SgmlTag {

        private final SgmlTag fHead;

        private final SgmlTag fBody;

        public Html(String pTitle) {
            super("html");
            fHead = newChild("head");
            fBody = newChild("body");
            getHead().newChild("title").addText(pTitle);
        }

        public SgmlTag getHead() {
            return fHead;
        }

        public SgmlTag getBody() {
            return fBody;
        }
    }

    public static class Timestamp extends SgmlTag {

        public Timestamp() {
            this("");
        }

        public Timestamp(String pPrefix) {
            super("p");
            attribute("class", "timestamp");
            addText(pPrefix);
            addText(new Date());
        }
    }
}
