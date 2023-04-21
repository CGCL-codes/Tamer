package uk.ac.essex.common.lang.xml.castor;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision: 1.1 $ $Date: 2004/07/26 16:53:38 $
**/
public class AvailableLocales implements java.io.Serializable {

    private java.util.Vector _localeList;

    public AvailableLocales() {
        super();
        _localeList = new Vector();
    }

    /**
     * 
     * 
     * @param vLocale
    **/
    public void addLocale(Locale vLocale) throws java.lang.IndexOutOfBoundsException {
        _localeList.addElement(vLocale);
    }

    /**
     * 
     * 
     * @param index
     * @param vLocale
    **/
    public void addLocale(int index, Locale vLocale) throws java.lang.IndexOutOfBoundsException {
        _localeList.insertElementAt(vLocale, index);
    }

    /**
    **/
    public java.util.Enumeration enumerateLocale() {
        return _localeList.elements();
    }

    /**
     * 
     * 
     * @param index
    **/
    public Locale getLocale(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _localeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        return (Locale) _localeList.elementAt(index);
    }

    /**
    **/
    public Locale[] getLocale() {
        int size = _localeList.size();
        Locale[] mArray = new Locale[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Locale) _localeList.elementAt(index);
        }
        return mArray;
    }

    /**
    **/
    public int getLocaleCount() {
        return _localeList.size();
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
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
    **/
    public void removeAllLocale() {
        _localeList.removeAllElements();
    }

    /**
     * 
     * 
     * @param index
    **/
    public Locale removeLocale(int index) {
        java.lang.Object obj = _localeList.elementAt(index);
        _localeList.removeElementAt(index);
        return (Locale) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vLocale
    **/
    public void setLocale(int index, Locale vLocale) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _localeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _localeList.setElementAt(vLocale, index);
    }

    /**
     * 
     * 
     * @param localeArray
    **/
    public void setLocale(Locale[] localeArray) {
        _localeList.removeAllElements();
        for (int i = 0; i < localeArray.length; i++) {
            _localeList.addElement(localeArray[i]);
        }
    }

    /**
     * 
     * 
     * @param reader
    **/
    public static uk.ac.essex.common.lang.xml.castor.AvailableLocales unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (uk.ac.essex.common.lang.xml.castor.AvailableLocales) Unmarshaller.unmarshal(uk.ac.essex.common.lang.xml.castor.AvailableLocales.class, reader);
    }

    /**
    **/
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
