package org.openemed.CTS;

import org.openemed.CTSVAPI.ConceptProperty;
import org.openemed.CTSVAPI.ConceptDesignation;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: dwf
 * Date: Jul 14, 2005
 * Time: 6:36:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Property implements java.io.Serializable {

    private String code;

    private String id;

    private String language;

    private String value;

    private String presentationFormat;

    private String mimeType = "text/plain";

    private String dataType;

    private boolean isPreferred;

    private ArrayList attributes = new ArrayList();

    public Property() {
    }

    public Property(String name, String id) {
        this.code = name;
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPresentationFormat(String presentationFormat) {
        this.presentationFormat = presentationFormat;
    }

    public String getPresentationFormat() {
        return presentationFormat;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setIsPreferred(boolean isPreferred) {
        this.isPreferred = isPreferred;
    }

    public boolean isPreferred() {
        return isPreferred;
    }

    public void addAttribute(NameValue att) {
        attributes.add(att);
    }

    public NameValue getAttribute(String name) {
        for (int i = 0; i < attributes.size(); i++) {
            NameValue nv = (NameValue) attributes.get(i);
            if (nv.getName().equals(name)) return nv;
        }
        return null;
    }

    public ConceptProperty getConceptProperty() {
        return new ConceptProperty(code, value, language, mimeType);
    }

    public ConceptDesignation getConceptDesignation() {
        return new ConceptDesignation(value, language, isPreferred);
    }
}
