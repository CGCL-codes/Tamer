package com.sysdelphia.bindings.mismo.title;

import java.util.ArrayList;

public class EbeddedFile {

    protected String _ID;

    protected String _Name;

    protected String _Description;

    protected String _Extension;

    protected String _Version;

    protected String _EncodingType;

    protected String mimeType;

    public void addDOCUMENT(Document document) {
        documentList.add(document);
    }

    public Document getDOCUMENT(int index) {
        return (Document) documentList.get(index);
    }

    public int sizeDOCUMENTList() {
        return documentList.size();
    }

    public String get_ID() {
        return this._ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String get_Name() {
        return this._Name;
    }

    public void set_Name(String _Name) {
        this._Name = _Name;
    }

    public String get_Description() {
        return this._Description;
    }

    public void set_Description(String _Description) {
        this._Description = _Description;
    }

    public String get_Extension() {
        return this._Extension;
    }

    public void set_Extension(String _Extension) {
        this._Extension = _Extension;
    }

    public String get_Version() {
        return this._Version;
    }

    public void set_Version(String _Version) {
        this._Version = _Version;
    }

    public String get_EncodingType() {
        return this._EncodingType;
    }

    public void set_EncodingType(String _EncodingType) {
        this._EncodingType = _EncodingType;
    }

    public String getMIMEType() {
        return this.mimeType;
    }

    public void setMIMEType(String mimeType) {
        this.mimeType = mimeType;
    }

    protected ArrayList documentList = new ArrayList();
}
