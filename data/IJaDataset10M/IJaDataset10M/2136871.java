package com.iver.andami.plugins.config.generate;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ExtensionType.
 * 
 * @version $Revision: 15983 $ $Date: 2007-11-07 12:11:19 +0100 (Wed, 07 Nov 2007) $
 */
public class ExtensionType extends com.iver.andami.plugins.config.generate.SkinExtensionType implements java.io.Serializable {

    /**
     * Field _priority
     */
    private int _priority;

    /**
     * keeps track of state for field: _priority
     */
    private boolean _has_priority;

    /**
     * Field _active
     */
    private boolean _active;

    /**
     * keeps track of state for field: _active
     */
    private boolean _has_active;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _alwaysvisible
     */
    private boolean _alwaysvisible;

    /**
     * keeps track of state for field: _alwaysvisible
     */
    private boolean _has_alwaysvisible;

    public ExtensionType() {
        super();
    }

    /**
     * Method deleteActive
     */
    public void deleteActive() {
        this._has_active = false;
    }

    /**
     * Method deleteAlwaysvisible
     */
    public void deleteAlwaysvisible() {
        this._has_alwaysvisible = false;
    }

    /**
     * Method deletePriority
     */
    public void deletePriority() {
        this._has_priority = false;
    }

    /**
     * Returns the value of field 'active'.
     * 
     * @return the value of field 'active'.
     */
    public boolean getActive() {
        return this._active;
    }

    /**
     * Returns the value of field 'alwaysvisible'.
     * 
     * @return the value of field 'alwaysvisible'.
     */
    public boolean getAlwaysvisible() {
        return this._alwaysvisible;
    }

    /**
     * Returns the value of field 'description'.
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription() {
        return this._description;
    }

    /**
     * Returns the value of field 'priority'.
     * 
     * @return the value of field 'priority'.
     */
    public int getPriority() {
        return this._priority;
    }

    /**
     * Method hasActive
     */
    public boolean hasActive() {
        return this._has_active;
    }

    /**
     * Method hasAlwaysvisible
     */
    public boolean hasAlwaysvisible() {
        return this._has_alwaysvisible;
    }

    /**
     * Method hasPriority
     */
    public boolean hasPriority() {
        return this._has_priority;
    }

    /**
     * Method isValid
     */
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'active'.
     * 
     * @param active the value of field 'active'.
     */
    public void setActive(boolean active) {
        this._active = active;
        this._has_active = true;
    }

    /**
     * Sets the value of field 'alwaysvisible'.
     * 
     * @param alwaysvisible the value of field 'alwaysvisible'.
     */
    public void setAlwaysvisible(boolean alwaysvisible) {
        this._alwaysvisible = alwaysvisible;
        this._has_alwaysvisible = true;
    }

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description) {
        this._description = description;
    }

    /**
     * Sets the value of field 'priority'.
     * 
     * @param priority the value of field 'priority'.
     */
    public void setPriority(int priority) {
        this._priority = priority;
        this._has_priority = true;
    }

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.iver.andami.plugins.config.generate.ExtensionType) Unmarshaller.unmarshal(com.iver.andami.plugins.config.generate.ExtensionType.class, reader);
    }

    /**
     * Method validate
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
