package zing.config.xmlobjects;

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
 * @version $Revision$ $Date$
**/
public class PostTest implements java.io.Serializable {

    private java.util.Vector _testDataList;

    public PostTest() {
        super();
        _testDataList = new Vector();
    }

    /**
     * 
     * 
     * @param vTestData
    **/
    public void addTestData(TestData vTestData) throws java.lang.IndexOutOfBoundsException {
        _testDataList.addElement(vTestData);
    }

    /**
     * 
     * 
     * @param index
     * @param vTestData
    **/
    public void addTestData(int index, TestData vTestData) throws java.lang.IndexOutOfBoundsException {
        _testDataList.insertElementAt(vTestData, index);
    }

    /**
    **/
    public java.util.Enumeration enumerateTestData() {
        return _testDataList.elements();
    }

    /**
     * 
     * 
     * @param index
    **/
    public TestData getTestData(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _testDataList.size())) {
            throw new IndexOutOfBoundsException();
        }
        return (TestData) _testDataList.elementAt(index);
    }

    /**
    **/
    public TestData[] getTestData() {
        int size = _testDataList.size();
        TestData[] mArray = new TestData[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (TestData) _testDataList.elementAt(index);
        }
        return mArray;
    }

    /**
    **/
    public int getTestDataCount() {
        return _testDataList.size();
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
    public void removeAllTestData() {
        _testDataList.removeAllElements();
    }

    /**
     * 
     * 
     * @param index
    **/
    public TestData removeTestData(int index) {
        java.lang.Object obj = _testDataList.elementAt(index);
        _testDataList.removeElementAt(index);
        return (TestData) obj;
    }

    /**
     * 
     * 
     * @param index
     * @param vTestData
    **/
    public void setTestData(int index, TestData vTestData) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _testDataList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _testDataList.setElementAt(vTestData, index);
    }

    /**
     * 
     * 
     * @param testDataArray
    **/
    public void setTestData(TestData[] testDataArray) {
        _testDataList.removeAllElements();
        for (int i = 0; i < testDataArray.length; i++) {
            _testDataList.addElement(testDataArray[i]);
        }
    }

    /**
     * 
     * 
     * @param reader
    **/
    public static zing.config.xmlobjects.PostTest unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (zing.config.xmlobjects.PostTest) Unmarshaller.unmarshal(zing.config.xmlobjects.PostTest.class, reader);
    }

    /**
    **/
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
