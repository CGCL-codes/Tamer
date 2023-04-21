package struts2gui.model;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Interceptor.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Interceptor implements java.io.Serializable {

    /**
     * Field _name.
     */
    private java.lang.Object _name;

    /**
     * Field _clazz.
     */
    private java.lang.Object _clazz;

    /**
     * Field _paramList.
     */
    private java.util.List<struts2gui.model.Param> _paramList;

    public Interceptor() {
        super();
        this._paramList = new java.util.ArrayList<struts2gui.model.Param>();
    }

    /**
     * 
     * 
     * @param vParam
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addParam(final struts2gui.model.Param vParam) throws java.lang.IndexOutOfBoundsException {
        this._paramList.add(vParam);
    }

    /**
     * 
     * 
     * @param index
     * @param vParam
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addParam(final int index, final struts2gui.model.Param vParam) throws java.lang.IndexOutOfBoundsException {
        this._paramList.add(index, vParam);
    }

    /**
     * Method enumerateParam.
     * 
     * @return an Enumeration over all possible elements of this
     * collection
     */
    public java.util.Enumeration<struts2gui.model.Param> enumerateParam() {
        return java.util.Collections.enumeration(this._paramList);
    }

    /**
     * Returns the value of field 'clazz'.
     * 
     * @return the value of field 'Clazz'.
     */
    public java.lang.Object getClazz() {
        return this._clazz;
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.Object getName() {
        return this._name;
    }

    /**
     * Method getParam.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the struts2gui.model.Param at the given
     * index
     */
    public struts2gui.model.Param getParam(final int index) throws java.lang.IndexOutOfBoundsException {
        if (index < 0 || index >= this._paramList.size()) {
            throw new IndexOutOfBoundsException("getParam: Index value '" + index + "' not in range [0.." + (this._paramList.size() - 1) + "]");
        }
        return (struts2gui.model.Param) _paramList.get(index);
    }

    /**
     * Method getParam.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public struts2gui.model.Param[] getParam() {
        struts2gui.model.Param[] array = new struts2gui.model.Param[0];
        return (struts2gui.model.Param[]) this._paramList.toArray(array);
    }

    /**
     * Method getParamCount.
     * 
     * @return the size of this collection
     */
    public int getParamCount() {
        return this._paramList.size();
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
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
     * Method iterateParam.
     * 
     * @return an Iterator over all possible elements in this
     * collection
     */
    public java.util.Iterator<struts2gui.model.Param> iterateParam() {
        return this._paramList.iterator();
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     */
    public void removeAllParam() {
        this._paramList.clear();
    }

    /**
     * Method removeParam.
     * 
     * @param vParam
     * @return true if the object was removed from the collection.
     */
    public boolean removeParam(final struts2gui.model.Param vParam) {
        boolean removed = _paramList.remove(vParam);
        return removed;
    }

    /**
     * Method removeParamAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public struts2gui.model.Param removeParamAt(final int index) {
        java.lang.Object obj = this._paramList.remove(index);
        return (struts2gui.model.Param) obj;
    }

    /**
     * Sets the value of field 'clazz'.
     * 
     * @param clazz the value of field 'clazz'.
     */
    public void setClazz(final java.lang.Object clazz) {
        this._clazz = clazz;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(final java.lang.Object name) {
        this._name = name;
    }

    /**
     * 
     * 
     * @param index
     * @param vParam
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setParam(final int index, final struts2gui.model.Param vParam) throws java.lang.IndexOutOfBoundsException {
        if (index < 0 || index >= this._paramList.size()) {
            throw new IndexOutOfBoundsException("setParam: Index value '" + index + "' not in range [0.." + (this._paramList.size() - 1) + "]");
        }
        this._paramList.set(index, vParam);
    }

    /**
     * 
     * 
     * @param vParamArray
     */
    public void setParam(final struts2gui.model.Param[] vParamArray) {
        _paramList.clear();
        for (int i = 0; i < vParamArray.length; i++) {
            this._paramList.add(vParamArray[i]);
        }
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled struts2gui.model.Interceptor
     */
    public static struts2gui.model.Interceptor unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (struts2gui.model.Interceptor) Unmarshaller.unmarshal(struts2gui.model.Interceptor.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

    @Override
    public String toString() {
        return getName().toString();
    }
}
