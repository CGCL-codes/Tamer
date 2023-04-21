package com.calipso.reportgenerator.reportdefinitions;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class DrillDownDefinitions.
 * 
 * @version $Revision$ $Date$
 */
public class DrillDownDefinitions implements java.io.Serializable {

    /**
     * Field _drillDownDefinitionList
     */
    private java.util.Vector _drillDownDefinitionList;

    public DrillDownDefinitions() {
        super();
        _drillDownDefinitionList = new Vector();
    }

    /**
     * Method addDrillDownDefinition
     * 
     * @param vDrillDownDefinition
     */
    public void addDrillDownDefinition(com.calipso.reportgenerator.reportdefinitions.DrillDownDefinition vDrillDownDefinition) throws java.lang.IndexOutOfBoundsException {
        _drillDownDefinitionList.addElement(vDrillDownDefinition);
    }

    /**
     * Method addDrillDownDefinition
     * 
     * @param index
     * @param vDrillDownDefinition
     */
    public void addDrillDownDefinition(int index, com.calipso.reportgenerator.reportdefinitions.DrillDownDefinition vDrillDownDefinition) throws java.lang.IndexOutOfBoundsException {
        _drillDownDefinitionList.insertElementAt(vDrillDownDefinition, index);
    }

    /**
     * Method enumerateDrillDownDefinition
     */
    public java.util.Enumeration enumerateDrillDownDefinition() {
        return _drillDownDefinitionList.elements();
    }

    /**
     * Method getDrillDownDefinition
     * 
     * @param index
     */
    public com.calipso.reportgenerator.reportdefinitions.DrillDownDefinition getDrillDownDefinition(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _drillDownDefinitionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        return (com.calipso.reportgenerator.reportdefinitions.DrillDownDefinition) _drillDownDefinitionList.elementAt(index);
    }

    /**
     * Method getDrillDownDefinition
     */
    public com.calipso.reportgenerator.reportdefinitions.DrillDownDefinition[] getDrillDownDefinition() {
        int size = _drillDownDefinitionList.size();
        com.calipso.reportgenerator.reportdefinitions.DrillDownDefinition[] mArray = new com.calipso.reportgenerator.reportdefinitions.DrillDownDefinition[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.calipso.reportgenerator.reportdefinitions.DrillDownDefinition) _drillDownDefinitionList.elementAt(index);
        }
        return mArray;
    }

    /**
     * Method getDrillDownDefinitionCount
     */
    public int getDrillDownDefinitionCount() {
        return _drillDownDefinitionList.size();
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
     * Method removeAllDrillDownDefinition
     */
    public void removeAllDrillDownDefinition() {
        _drillDownDefinitionList.removeAllElements();
    }

    /**
     * Method removeDrillDownDefinition
     * 
     * @param index
     */
    public com.calipso.reportgenerator.reportdefinitions.DrillDownDefinition removeDrillDownDefinition(int index) {
        java.lang.Object obj = _drillDownDefinitionList.elementAt(index);
        _drillDownDefinitionList.removeElementAt(index);
        return (com.calipso.reportgenerator.reportdefinitions.DrillDownDefinition) obj;
    }

    /**
     * Method setDrillDownDefinition
     * 
     * @param index
     * @param vDrillDownDefinition
     */
    public void setDrillDownDefinition(int index, com.calipso.reportgenerator.reportdefinitions.DrillDownDefinition vDrillDownDefinition) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _drillDownDefinitionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _drillDownDefinitionList.setElementAt(vDrillDownDefinition, index);
    }

    /**
     * Method setDrillDownDefinition
     * 
     * @param drillDownDefinitionArray
     */
    public void setDrillDownDefinition(com.calipso.reportgenerator.reportdefinitions.DrillDownDefinition[] drillDownDefinitionArray) {
        _drillDownDefinitionList.removeAllElements();
        for (int i = 0; i < drillDownDefinitionArray.length; i++) {
            _drillDownDefinitionList.addElement(drillDownDefinitionArray[i]);
        }
    }

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static com.calipso.reportgenerator.reportdefinitions.DrillDownDefinitions unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.calipso.reportgenerator.reportdefinitions.DrillDownDefinitions) Unmarshaller.unmarshal(com.calipso.reportgenerator.reportdefinitions.DrillDownDefinitions.class, reader);
    }

    /**
     * Method validate
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
