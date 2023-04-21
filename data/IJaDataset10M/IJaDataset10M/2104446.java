package net.sf.dynxform.form.schema;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import java.util.ArrayList;

/**
 * Class Report.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/08/11 17:32:54 $
 */
public class Report implements java.io.Serializable {

    /**
     * Field _guid
     */
    private java.lang.String _guid;

    /**
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field _parameterList
     */
    private java.util.ArrayList _parameterList;

    public Report() {
        super();
        _parameterList = new ArrayList();
    }

    /**
     * Method addParameter
     * 
     * @param vParameter
     */
    public void addParameter(Parameter vParameter) throws java.lang.IndexOutOfBoundsException {
        _parameterList.add(vParameter);
    }

    /**
     * Method addParameter
     * 
     * @param index
     * @param vParameter
     */
    public void addParameter(int index, Parameter vParameter) throws java.lang.IndexOutOfBoundsException {
        _parameterList.add(index, vParameter);
    }

    /**
     * Method clearParameter
     */
    public void clearParameter() {
        _parameterList.clear();
    }

    /**
     * Method enumerateParameter
     */
    public java.util.Enumeration enumerateParameter() {
        return new org.exolab.castor.util.IteratorEnumeration(_parameterList.iterator());
    }

    /**
     * Returns the value of field 'guid'.
     * 
     * @return the value of field 'guid'.
     */
    public java.lang.String getGuid() {
        return this._guid;
    }

    /**
     * Returns the value of field 'id'.
     * 
     * @return the value of field 'id'.
     */
    public java.lang.String getId() {
        return this._id;
    }

    /**
     * Method getParameter
     * 
     * @param index
     */
    public Parameter getParameter(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _parameterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        return (Parameter) _parameterList.get(index);
    }

    /**
     * Method getParameter
     */
    public Parameter[] getParameter() {
        int size = _parameterList.size();
        Parameter[] mArray = new Parameter[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Parameter) _parameterList.get(index);
        }
        return mArray;
    }

    /**
     * Method getParameterCount
     */
    public int getParameterCount() {
        return _parameterList.size();
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
     * Method removeParameter
     * 
     * @param vParameter
     */
    public boolean removeParameter(Parameter vParameter) {
        boolean removed = _parameterList.remove(vParameter);
        return removed;
    }

    /**
     * Sets the value of field 'guid'.
     * 
     * @param guid the value of field 'guid'.
     */
    public void setGuid(java.lang.String guid) {
        this._guid = guid;
    }

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id) {
        this._id = id;
    }

    /**
     * Method setParameter
     * 
     * @param index
     * @param vParameter
     */
    public void setParameter(int index, Parameter vParameter) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _parameterList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _parameterList.set(index, vParameter);
    }

    /**
     * Method setParameter
     * 
     * @param parameterArray
     */
    public void setParameter(Parameter[] parameterArray) {
        _parameterList.clear();
        for (int i = 0; i < parameterArray.length; i++) {
            _parameterList.add(parameterArray[i]);
        }
    }

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (net.sf.dynxform.form.schema.Report) Unmarshaller.unmarshal(net.sf.dynxform.form.schema.Report.class, reader);
    }

    /**
     * Method validate
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
