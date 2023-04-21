package com.loribel.commons.business;

import java.util.Date;
import junit.framework.TestCase;
import com.loribel.commons.GB_FwkInitializer;
import com.loribel.commons.abstraction.GB_Validable;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObjectSet;
import com.loribel.commons.business.impl.bo.GB_BOMetaDataBO;
import com.loribel.commons.business.impl.bo.GB_BOPropertyBO;
import com.loribel.commons.util.GB_DateTools;

public class GB_BOValidationToolsTest extends TestCase {

    public GB_BOValidationToolsTest(String a_name) {
        super(a_name);
    }

    protected void setUp() {
        GB_FwkInitializer.initAll();
    }

    public void test_checkIntervalDate() {
        String l_boName = "BOTestIntervalDate";
        GB_BOMetaDataBO l_testMeta = new GB_BOMetaDataBO();
        l_testMeta.setId(l_boName);
        GB_BOPropertyBO l_propertyDateMin = new GB_BOPropertyBO();
        GB_BOPropertyBO l_propertyDateMax = new GB_BOPropertyBO();
        l_propertyDateMin.setName("dateMin");
        l_propertyDateMin.setLabel("default", "fr", "Date de d�but");
        l_propertyDateMin.setType(Date.class);
        l_propertyDateMax.setName("dateMax");
        l_propertyDateMax.setLabel("default", "fr", "Date de fin");
        l_propertyDateMax.setType(Date.class);
        l_testMeta.addBoProperty(l_propertyDateMin);
        l_testMeta.addBoProperty(l_propertyDateMax);
        GB_BOMetaDataFactoryTools.getFactory().addBOMetaData(l_testMeta);
        GB_SimpleBusinessObjectSet l_boPositif = (GB_SimpleBusinessObjectSet) GB_BOFactoryTools.getFactory().newBusinessObject(l_boName);
        l_boPositif.setPropertyValue("dateMin", GB_DateTools.newDate(2000, 01, 01));
        l_boPositif.setPropertyValue("dateMax", GB_DateTools.newDate(2001, 01, 01));
        GB_Validable l_validPositif = GB_BOValidationTools.checkIntervalDate(l_boPositif, "dateMin", "dateMax", true);
        if (l_validPositif.getValidStatus() == GB_Validable.ERROR) {
            int len = l_validPositif.getErrorMessages().length;
            for (int i = 0; i < len; i++) {
            }
        }
        GB_SimpleBusinessObjectSet l_boNegatif = (GB_SimpleBusinessObjectSet) GB_BOFactoryTools.getFactory().newBusinessObject(l_boName);
        l_boNegatif.setPropertyValue("dateMin", GB_DateTools.newDate(2005, 01, 01));
        l_boNegatif.setPropertyValue("dateMax", GB_DateTools.newDate(2000, 01, 01));
        GB_Validable l_validNegatif = GB_BOValidationTools.checkIntervalDate(l_boNegatif, "dateMin", "dateMax", true);
        if (l_validNegatif.getValidStatus() == GB_Validable.ERROR) {
            int len = l_validNegatif.getErrorMessages().length;
            for (int i = 0; i < len; i++) {
            }
        }
    }

    public void test_checkIntervalNumber() {
        String l_boName = "BOTestIntervalNumber";
        GB_BOMetaDataBO l_testMeta = new GB_BOMetaDataBO();
        l_testMeta.setId(l_boName);
        GB_BOPropertyBO l_propertyMin = new GB_BOPropertyBO();
        GB_BOPropertyBO l_propertyMax = new GB_BOPropertyBO();
        l_propertyMin.setName("min");
        l_propertyMin.setLabel("default", "fr", "Borne inf�rieur");
        l_propertyMin.setType(Date.class);
        l_propertyMax.setName("max");
        l_propertyMax.setLabel("default", "fr", "Borne sup�rieur");
        l_propertyMax.setType(Date.class);
        l_testMeta.addBoProperty(l_propertyMin);
        l_testMeta.addBoProperty(l_propertyMax);
        GB_BOMetaDataFactoryTools.getFactory().addBOMetaData(l_testMeta);
        GB_SimpleBusinessObjectSet l_boPositif = (GB_SimpleBusinessObjectSet) GB_BOFactoryTools.getFactory().newBusinessObject(l_boName);
        l_boPositif.setPropertyValue("min", new Integer(10));
        l_boPositif.setPropertyValue("max", new Integer(20));
        GB_Validable l_validPositif = GB_BOValidationTools.checkIntervalNumber(l_boPositif, "min", "max", true);
        if (l_validPositif.getValidStatus() == GB_Validable.ERROR) {
            int len = l_validPositif.getErrorMessages().length;
            for (int i = 0; i < len; i++) {
            }
        }
        GB_SimpleBusinessObjectSet l_boNegatif = (GB_SimpleBusinessObjectSet) GB_BOFactoryTools.getFactory().newBusinessObject(l_boName);
        l_boNegatif.setPropertyValue("min", new Integer(20));
        l_boNegatif.setPropertyValue("max", new Integer(10));
        GB_Validable l_validNegatif = GB_BOValidationTools.checkIntervalNumber(l_boNegatif, "min", "max", true);
        if (l_validNegatif.getValidStatus() == GB_Validable.ERROR) {
            int len = l_validNegatif.getErrorMessages().length;
            for (int i = 0; i < len; i++) {
            }
        }
    }

    public void test_checkIntervalString() {
        String l_boName = "BOTestIntervalString";
        GB_BOMetaDataBO l_testMeta = new GB_BOMetaDataBO();
        l_testMeta.setId(l_boName);
        GB_BOPropertyBO l_propertyMin = new GB_BOPropertyBO();
        GB_BOPropertyBO l_propertyMax = new GB_BOPropertyBO();
        l_propertyMin.setName("min");
        l_propertyMin.setLabel("default", "fr", "D�but");
        l_propertyMin.setType(Date.class);
        l_propertyMax.setName("max");
        l_propertyMax.setLabel("default", "fr", "Fin");
        l_propertyMax.setType(Date.class);
        l_testMeta.addBoProperty(l_propertyMin);
        l_testMeta.addBoProperty(l_propertyMax);
        GB_BOMetaDataFactoryTools.getFactory().addBOMetaData(l_testMeta);
        GB_SimpleBusinessObjectSet l_boPositif = (GB_SimpleBusinessObjectSet) GB_BOFactoryTools.getFactory().newBusinessObject(l_boName);
        l_boPositif.setPropertyValue("min", "aaa");
        l_boPositif.setPropertyValue("max", "bbb");
        GB_Validable l_validPositif = GB_BOValidationTools.checkIntervalString(l_boPositif, "min", "max", true, true);
        if (l_validPositif.getValidStatus() == GB_Validable.ERROR) {
            int len = l_validPositif.getErrorMessages().length;
            for (int i = 0; i < len; i++) {
            }
        }
        GB_SimpleBusinessObjectSet l_boNegatif = (GB_SimpleBusinessObjectSet) GB_BOFactoryTools.getFactory().newBusinessObject(l_boName);
        l_boNegatif.setPropertyValue("min", "bbb");
        l_boNegatif.setPropertyValue("max", "aaa");
        GB_Validable l_validNegatif = GB_BOValidationTools.checkIntervalString(l_boNegatif, "min", "max", true, true);
        if (l_validNegatif.getValidStatus() == GB_Validable.ERROR) {
            int len = l_validNegatif.getErrorMessages().length;
            for (int i = 0; i < len; i++) {
            }
        }
    }

    public void test_checkValues() {
        String l_boName = "BOTest";
        GB_BOMetaDataBO l_testMeta = new GB_BOMetaDataBO();
        l_testMeta.setId(l_boName);
        GB_BOPropertyBO l_propertyDataString = new GB_BOPropertyBO();
        GB_BOPropertyBO l_propertyDataNumber = new GB_BOPropertyBO();
        GB_BOPropertyBO l_propertyDataStringMulti = new GB_BOPropertyBO();
        GB_BOPropertyBO l_propertyDataNumberMap = new GB_BOPropertyBO();
        l_propertyDataString.setName("testString");
        l_propertyDataString.setLabel("default", "fr", "testString");
        l_propertyDataString.setType(String.class);
        l_propertyDataString.setMaxSize(10);
        l_propertyDataString.setMinSize(3);
        l_propertyDataString.setOptional(false);
        l_propertyDataString.setValidationRegex("[0-9]+");
        l_propertyDataNumber.setName("testNumber");
        l_propertyDataNumber.setLabel("default", "fr", "testNumber");
        l_propertyDataNumber.setType(Number.class);
        l_propertyDataNumber.setMaxValue(new Integer(20));
        l_propertyDataNumber.setMinValue(new Integer(10));
        l_propertyDataNumber.setOptional(false);
        l_propertyDataStringMulti.setName("testStringMulti");
        l_propertyDataStringMulti.setLabel("default", "fr", "testStringMulti");
        l_propertyDataStringMulti.setType(String.class);
        l_propertyDataStringMulti.setPropertyMulti(true);
        l_propertyDataStringMulti.setMaxSize(10);
        l_propertyDataStringMulti.setMinSize(3);
        l_propertyDataStringMulti.setOptional(false);
        l_propertyDataStringMulti.setValidationRegex("[0-9]+");
        l_propertyDataNumberMap.setName("testNumberMap");
        l_propertyDataNumberMap.setLabel("default", "fr", "testNumberMap");
        l_propertyDataNumberMap.setType(Number.class);
        l_propertyDataNumberMap.setPropertyMap(true);
        l_propertyDataNumberMap.setMaxValue(new Integer(20));
        l_propertyDataNumberMap.setMinValue(new Integer(10));
        l_propertyDataNumberMap.setOptional(false);
        l_testMeta.addBoProperty(l_propertyDataString);
        l_testMeta.addBoProperty(l_propertyDataNumber);
        l_testMeta.addBoProperty(l_propertyDataStringMulti);
        l_testMeta.addBoProperty(l_propertyDataNumberMap);
        GB_BOMetaDataFactoryTools.getFactory().addBOMetaData(l_testMeta);
        GB_SimpleBusinessObjectSet l_boNegatif = (GB_SimpleBusinessObjectSet) GB_BOFactoryTools.getFactory().newBusinessObject(l_boName);
        l_boNegatif.setPropertyValue("testString", "123456");
        l_boNegatif.setPropertyValue("testNumber", new Integer(15));
        l_boNegatif.addPropertyValue("testStringMulti", "123");
        l_boNegatif.addPropertyValue("testStringMulti", "45666");
        l_boNegatif.addPropertyValue("testStringMulti", "88888");
        l_boNegatif.putPropertyValueMap("testNumberMap", "1", new Integer(12));
        l_boNegatif.putPropertyValueMap("testNumberMap", "2", new Integer(15));
        l_boNegatif.putPropertyValueMap("testNumberMap", "3", new Integer(17));
        GB_Validable l_validNegatif = GB_BOValidationTools.checkValues(l_boNegatif);
        if (l_validNegatif.getValidStatus() == GB_Validable.ERROR) {
            int len = l_validNegatif.getErrorMessages().length;
            for (int i = 0; i < len; i++) {
            }
        }
        GB_SimpleBusinessObjectSet l_boPositif = (GB_SimpleBusinessObjectSet) GB_BOFactoryTools.getFactory().newBusinessObject(l_boName);
        l_boPositif.setPropertyValue("testString", "ab");
        l_boPositif.setPropertyValue("testNumber", new Integer(-10));
        l_boPositif.addPropertyValue("testStringMulti", "a");
        l_boPositif.addPropertyValue("testStringMulti", "asdasdasdadadasd");
        l_boPositif.addPropertyValue("testStringMulti", "888883213123123123");
        l_boPositif.addPropertyValue("testStringMulti", "83");
        l_boPositif.putPropertyValueMap("testNumberMap", "1", new Integer(-10));
        l_boPositif.putPropertyValueMap("testNumberMap", "2", new Integer(100));
        l_boPositif.putPropertyValueMap("testNumberMap", "3", new Integer(10));
        GB_Validable l_validPositif = GB_BOValidationTools.checkValues(l_boPositif);
        if (l_validPositif.getValidStatus() == GB_Validable.ERROR) {
            int len = l_validPositif.getErrorMessages().length;
            for (int i = 0; i < len; i++) {
            }
        }
    }
}
