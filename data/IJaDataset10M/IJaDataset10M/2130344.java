package org.marcont.rulegenerator.mobile.model;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * 
 * @author piotr
 */
public class Action {

    /**
     * Subject
     */
    protected String subject;

    /**
     * Predicate
     */
    protected String predicate;

    /**
     * Object
     */
    protected String object;

    /**
     * rdf:datatype of the object
     */
    protected String objectDatatype;

    /**
     * xml:lang of the object
     */
    protected String objectXmlLang;

    /**
     * The type of the node
     */
    private String objectType;

    /** Creates a new instance of Action */
    public Action() {
        subject = "";
        predicate = "";
        object = "";
        objectType = "resource";
    }

    Action(DataInputStream is) throws IOException {
        subject = is.readUTF();
        predicate = is.readUTF();
        object = is.readUTF();
        objectDatatype = is.readUTF();
        objectXmlLang = is.readUTF();
        objectType = is.readUTF();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getObjectDatatype() {
        return objectDatatype;
    }

    public void setObjectDatatype(String objectDatatype) {
        this.objectDatatype = objectDatatype;
    }

    public String getObjectXmlLang() {
        return objectXmlLang;
    }

    public void setObjectXmlLang(String objectXmlLang) {
        this.objectXmlLang = objectXmlLang;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("\"");
        buf.append(subject);
        buf.append("\" \"");
        buf.append(predicate);
        buf.append("\" \"");
        buf.append(object);
        buf.append("\"");
        return buf.toString();
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
}
