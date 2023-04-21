package com.k_int.IR.Syntaxes;

import com.k_int.IR.*;
import org.w3c.dom.Document;

/**
 * XMLRecord. Use DOMTree if you want to create a fragment from a Document!
 */
public class XMLRecord implements InformationFragment {

    private String source_collection_name = null;

    private String orig_schema = null;

    private Object handle = null;

    private String orig = null;

    public XMLRecord(String source) {
        this.orig = source;
        System.out.println("in XMLRecord  ###################### $$$$$$$$$$$  " + source);
    }

    public XMLRecord(String source_collection_name, Object handle, String source) {
        this.source_collection_name = source_collection_name;
        this.handle = handle;
        this.orig = source;
        System.out.println("in XMLRecord  ###################### $$$$$$$$$$$  " + source);
    }

    public Object getOriginalObject() {
        return orig;
    }

    public String getOriginalObjectClassName() {
        return "Document";
    }

    public Document getDocument() {
        return null;
    }

    public String getDocumentSchema() {
        return orig_schema;
    }

    public String toString() {
        return orig.toString();
    }

    public String getSourceCollectionName() {
        return source_collection_name;
    }

    public Object getSourceFragmentID() {
        if (handle != null) return handle; else return null;
    }
}
