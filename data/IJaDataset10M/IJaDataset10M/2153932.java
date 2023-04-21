package org.apache.jetspeed.xml.api.jcm;

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.DocumentHandler;

/**
 * 
 * @version $Revision$ $Date$
**/
public class Image implements java.io.Serializable {

    private java.lang.String _title;

    private java.lang.String _url;

    private java.lang.String _link;

    private java.lang.String _description;

    private int _width;

    /**
     * keeps track of state for field: _width
    **/
    private boolean _has_width;

    private int _height;

    /**
     * keeps track of state for field: _height
    **/
    private boolean _has_height;

    public Image() {
        super();
    }

    /**
    **/
    public void deleteHeight() {
        this._has_height = false;
    }

    /**
    **/
    public void deleteWidth() {
        this._has_width = false;
    }

    /**
    **/
    public java.lang.String getDescription() {
        return this._description;
    }

    /**
    **/
    public int getHeight() {
        return this._height;
    }

    /**
    **/
    public java.lang.String getLink() {
        return this._link;
    }

    /**
    **/
    public java.lang.String getTitle() {
        return this._title;
    }

    /**
    **/
    public java.lang.String getUrl() {
        return this._url;
    }

    /**
    **/
    public int getWidth() {
        return this._width;
    }

    /**
    **/
    public boolean hasHeight() {
        return this._has_height;
    }

    /**
    **/
    public boolean hasWidth() {
        return this._has_width;
    }

    /**
    **/
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * 
     * @param description
    **/
    public void setDescription(java.lang.String description) {
        this._description = description;
    }

    /**
     * 
     * @param height
    **/
    public void setHeight(int height) {
        this._height = height;
        this._has_height = true;
    }

    /**
     * 
     * @param link
    **/
    public void setLink(java.lang.String link) {
        this._link = link;
    }

    /**
     * 
     * @param title
    **/
    public void setTitle(java.lang.String title) {
        this._title = title;
    }

    /**
     * 
     * @param url
    **/
    public void setUrl(java.lang.String url) {
        this._url = url;
    }

    /**
     * 
     * @param width
    **/
    public void setWidth(int width) {
        this._width = width;
        this._has_width = true;
    }

    /**
     * 
     * @param reader
    **/
    public static org.apache.jetspeed.xml.api.jcm.Image unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.apache.jetspeed.xml.api.jcm.Image) Unmarshaller.unmarshal(org.apache.jetspeed.xml.api.jcm.Image.class, reader);
    }

    /**
    **/
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
