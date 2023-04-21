package com.loribel.commons.business.test;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.loribel.commons.GB_FwkInitializer;
import com.loribel.commons.abstraction.ENCODING;
import com.loribel.commons.abstraction.GB_Memento;
import com.loribel.commons.business.GB_BODataRandomTools;
import com.loribel.commons.business.GB_BODataTools;
import com.loribel.commons.business.GB_BOFactoryTools;
import com.loribel.commons.business.GB_BOMementoTools;
import com.loribel.commons.business.GB_BOPropertyFilterTools;
import com.loribel.commons.business.GB_BORandomTools;
import com.loribel.commons.business.GB_BOTestTools;
import com.loribel.commons.business.GB_BOTools;
import com.loribel.commons.business.GB_BOXmlTools;
import com.loribel.commons.business.abstraction.GB_BOData;
import com.loribel.commons.business.abstraction.GB_BOFactory;
import com.loribel.commons.business.abstraction.GB_BOProperty;
import com.loribel.commons.business.abstraction.GB_BOShort;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObjectSet;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.FTools;
import com.loribel.commons.util.GB_BeanTools;
import com.loribel.commons.util.GB_ReflectTools;
import com.loribel.commons.util.GB_XmlTools;
import com.loribel.commons.util.STools;

/**
 * Generic Test.  
 *
 * @author Gregory Borelli
 */
public abstract class GB_BOTestAbstract extends TestCase {

    public GB_BOTestAbstract(String s) {
        super(s);
    }

    private void assertNullOrEmpty(String propertyName, Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof Collection<?>) {
            assertEquals(propertyName + " - Collection", 0, ((Collection<?>) value).size());
        } else if (value instanceof Map<?, ?>) {
            assertEquals(propertyName + " - Collection", 0, ((Map<?, ?>) value).size());
        } else {
            assertEquals(propertyName + " - Array", 0, ((Object[]) value).length);
        }
    }

    /**
     * Check the eqality of the two BusinessObject.
     * <ul>
     *   <li>Check null
     *   <li>Check class
     *   <li>GB_BOTools.equalsBO()
     * </ul>
     */
    protected void checkBOEquals(String a_index, GB_SimpleBusinessObject a_bo1, GB_SimpleBusinessObject a_bo2) throws Exception {
        GB_BOTestTools.assertEqualsBO(a_index, a_bo1, a_bo2);
    }

    /**
     * Check the equality of the two BusinessObject.
     * <ul>
     *   <li>Check null
     *   <li>Check class
     *   <li>GB_BOTools.equalsBO()
     * </ul>
     */
    protected void checkBOEquals(String a_index, GB_SimpleBusinessObject a_bo1, String a_xml) {
        GB_BOTestTools.assertEqualsBO(a_index, a_bo1, a_xml);
    }

    protected void checkBOsEquals(String a_index, GB_SimpleBusinessObject[] a_bos, GB_SimpleBusinessObject[] a_bos2) throws Exception {
        assertEquals(a_index, a_bos.length, a_bos2.length);
        int len = CTools.getSize(a_bos);
        for (int i = 0; i < len; i++) {
            checkBOEquals(a_index + ".1", a_bos[i], a_bos2[i]);
        }
    }

    public abstract String getBOName();

    public abstract List getBOsForTest();

    public List getBOsGeneric() {
        String l_boName = getBOName();
        return GB_BOTestTools.getBOsGenericForTest(l_boName);
    }

    public List getBOsGenericNotNull() {
        List retour = new ArrayList();
        String l_boName = getBOName();
        retour.add(GB_BORandomTools.newBusinessObjectRandom(l_boName, false));
        retour.add(GB_BORandomTools.newBusinessObjectRandom(l_boName, false, false));
        retour.add(GB_BORandomTools.newBusinessObjectRandom(l_boName, false, true));
        return retour;
    }

    public Class getClassBO() {
        String l_boName = getBOName();
        GB_SimpleBusinessObject l_bo = GB_BOTools.newBO(l_boName);
        if (l_bo == null) {
            return null;
        }
        return l_bo.getClass();
    }

    public Class getClassData() {
        GB_BOData l_data = GB_BODataTools.newBOData(getBOName());
        if (l_data == null) {
            return null;
        }
        return l_data.getClass();
    }

    public GB_SimpleBusinessObjectSet newBo() {
        GB_BOFactory l_factory = GB_BOFactoryTools.getFactory();
        GB_SimpleBusinessObjectSet retour = (GB_SimpleBusinessObjectSet) l_factory.newBusinessObject(getBOName());
        return retour;
    }

    public GB_BOData newData() {
        GB_BOData retour = GB_BODataTools.newBOData(getBOName());
        return retour;
    }

    @Override
    public void setUp() {
        GB_FwkInitializer.initAll();
    }

    /**
     * Teste la presence d'un constructeur prive
     * Teste l'appel pour le test coverage.
     */
    public void test_BOPropertyConstructor() throws Exception {
        Class l_classBO = getClassBO();
        assertNotNull("Class not found!", l_classBO);
        Class[] l_inerClasses = l_classBO.getClasses();
        Class l_inerClassBOProperty = null;
        int len = CTools.getSize(l_inerClasses);
        for (int i = 0; i < len; i++) {
            Class l_inerClasse = l_inerClasses[i];
            if (l_inerClasse.getName().endsWith("BO_PROPERTY")) {
                l_inerClassBOProperty = l_inerClasse;
            }
        }
        assertNotNull("Class BO_PROPERTY not found!", l_inerClassBOProperty);
        Constructor l_constructor = l_inerClassBOProperty.getDeclaredConstructor(null);
        assertNotNull("Constructor private BO_PROPERTY not found !", l_constructor);
        assertEquals(l_constructor.getModifiers(), Modifier.PRIVATE);
        l_constructor.setAccessible(true);
        l_constructor.newInstance(null);
    }

    public void test_ClassData_boName() {
        GB_BOData l_data = newData();
        String l_boName = l_data.boName();
        assertEquals(getBOName(), l_boName);
    }

    public void test_getBOName() {
        GB_SimpleBusinessObject retour = newBo();
        String l_boName = retour.getBOName();
        assertEquals(getBOName(), l_boName);
    }

    public void test_newBOData() throws Exception {
        String boName = getBOName();
        GB_BOData data = GB_BODataTools.newBOData(boName);
        assertNotNull(boName + ".notNull", data);
    }

    public void test_newBODataRandom() throws Exception {
        String boName = getBOName();
        Object data = GB_BODataRandomTools.newBODataRandom(boName, true, true);
        assertNotNull(boName + " Random notNull", data);
    }

    public void test_newBODataRandomNotNullValues() throws Exception {
        String boName = getBOName();
        Object data = GB_BODataRandomTools.newBODataRandom(boName, false, false);
        assertNotNull(boName + " Random notNull", data);
        GB_BOProperty[] properties = GB_BOTools.getProperties(boName);
        int len = CTools.getSize(properties);
        for (int i = 0; i < len; i++) {
            GB_BOProperty property = properties[i];
            String propertyName = property.getName();
            Class<?> type = property.getType();
            if (!property.isBusinessObject()) {
                Object value = GB_BeanTools.getProperty(data, propertyName, type);
                assertNotNull(propertyName, value);
            } else {
                if (property.isPropertyMap()) {
                    String name = propertyName + "s";
                    Object value = GB_BeanTools.getProperty(data, name, type);
                    assertNullOrEmpty(propertyName, value);
                } else if (property.isPropertyMulti()) {
                    String name = propertyName + "s";
                    Object value = GB_BeanTools.getProperty(data, name, type);
                    assertNullOrEmpty(propertyName, value);
                } else {
                    Object value = GB_BeanTools.getProperty(data, propertyName, type);
                    assertNullOrEmpty(propertyName, value);
                }
            }
        }
    }

    public void test_newBORandomNotNullValues() throws Exception {
        String boName = getBOName();
        GB_SimpleBusinessObject bo = GB_BORandomTools.newBusinessObjectRandom(boName, false, false);
        assertNotNull(boName + " Random notNull", bo);
        GB_BOProperty[] properties = GB_BOTools.getProperties(boName);
        int len = CTools.getSize(properties);
        for (int i = 0; i < len; i++) {
            GB_BOProperty property = properties[i];
            String propertyName = property.getName();
            Object value = bo.getPropertyValue(propertyName);
            if (!property.isBusinessObject()) {
                assertNotNull(propertyName, value);
            } else {
                assertNullOrEmpty(propertyName, value);
            }
        }
    }

    public void testBoClass() {
        Class l_class = getClassBO();
        assertNotNull(l_class);
    }

    public void testCopyValues() throws Throwable {
        List l_bos = getBOsForTest();
        int len = CTools.getSize(l_bos);
        for (int i = 0; i < len; i++) {
            GB_SimpleBusinessObjectSet l_bo = (GB_SimpleBusinessObjectSet) l_bos.get(i);
            testCopyValues(i, l_bo);
        }
    }

    /**
     * Test CopyValues.
     */
    protected void testCopyValues(int a_index, GB_SimpleBusinessObjectSet a_bo) throws Throwable {
        GB_BOTools.removeValues(a_bo, GB_BOPropertyFilterTools.FILTER_MULTI, true);
        GB_BOFactory l_bOFactory = GB_BOFactoryTools.getFactory();
        GB_SimpleBusinessObjectSet l_boResult = (GB_SimpleBusinessObjectSet) l_bOFactory.newBusinessObject(a_bo.getBOName());
        GB_BOTools.copyValues(l_boResult, a_bo, true);
        checkBOEquals(a_index + ".1", a_bo, l_boResult);
    }

    /**
     * Check if default values is applied when use the factory.
     */
    public void testDefaultValues() throws Throwable {
        GB_BOFactory l_factory = GB_BOFactoryTools.getFactory();
        GB_SimpleBusinessObject l_bo = l_factory.newBusinessObject(getBOName());
        GB_SimpleBusinessObjectSet l_bo2 = (GB_SimpleBusinessObjectSet) l_factory.newBusinessObject(getBOName());
        GB_BOTools.removeAllValues(l_bo2);
        GB_BOTools.updateWithDefaultValues(l_bo2);
        checkBOEquals("1", l_bo, l_bo2);
    }

    public void testFactory() throws Throwable {
        List l_bos = getBOsForTest();
        int len = CTools.getSize(l_bos);
        for (int i = 0; i < len; i++) {
            GB_SimpleBusinessObject l_bo = (GB_SimpleBusinessObject) l_bos.get(i);
            testFactory(i, l_bo);
        }
    }

    protected void testFactory(int a_index, GB_SimpleBusinessObject a_bo) throws Throwable {
        String l_boName = a_bo.getBOName();
        GB_SimpleBusinessObject l_bo = GB_BOFactoryTools.getFactory().newBusinessObject(l_boName);
        assertEquals("" + a_index, a_bo.getClass(), l_bo.getClass());
    }

    public void testMemento() throws Throwable {
        List l_bos = getBOsForTest();
        int len = CTools.getSize(l_bos);
        for (int i = 0; i < len; i++) {
            GB_SimpleBusinessObjectSet l_bo = (GB_SimpleBusinessObjectSet) l_bos.get(i);
            testMemento(i, l_bo);
        }
    }

    /**
     * testMemento.
     */
    protected void testMemento(int a_index, GB_SimpleBusinessObjectSet a_bo) throws Throwable {
        String l_xml = GB_BOXmlTools.toStringXml(a_bo);
        GB_Memento l_memento = GB_BOMementoTools.newMemento(a_bo, true);
        GB_BOTools.updateWithDefaultValues(a_bo);
        l_memento.restore();
        checkBOEquals(a_index + ".1", a_bo, l_xml);
        l_memento = GB_BOMementoTools.newMemento(a_bo, true);
        GB_BOTools.removeAllValues(a_bo);
        l_memento.restore();
        checkBOEquals(a_index + ".2", a_bo, l_xml);
    }

    public void testNewNode() throws Throwable {
        List l_bos = getBOsForTest();
        int len = CTools.getSize(l_bos);
        for (int i = 0; i < len; i++) {
            GB_SimpleBusinessObject l_bo = (GB_SimpleBusinessObject) l_bos.get(i);
            testNewNode(i, l_bo);
        }
    }

    /**
     * testNewNode.
     */
    protected void testNewNode(int a_index, GB_SimpleBusinessObject a_bo) throws Throwable {
        GB_BOFactory l_factory = GB_BOFactoryTools.getFactory();
        Document l_doc = GB_XmlTools.newDocument();
        Node l_node = l_factory.newNode(a_bo, l_doc);
        l_doc.appendChild(l_node);
        GB_SimpleBusinessObject[] l_bos = l_factory.loadFromDocument(l_doc, "/*", null);
        assertEquals(a_index + ".1 - check size", 1, l_bos.length);
        checkBOEquals(a_index + ".2", a_bo, l_bos[0]);
    }

    public void testToXml() throws Throwable {
        List l_bos = getBOsForTest();
        GB_SimpleBusinessObject l_bo;
        int len = CTools.getSize(l_bos);
        for (int i = 0; i < len; i++) {
            l_bo = (GB_SimpleBusinessObject) l_bos.get(i);
            testToXml(i, l_bo, ENCODING.ISO_8859_1);
            testToXml(i, l_bo, ENCODING.UTF8);
        }
    }

    /**
     * testWriteXmlFile.
     */
    protected void testToXml(int a_index, GB_SimpleBusinessObject a_bo, String a_encoding) throws Throwable {
        if (a_bo == null) {
            return;
        }
        String l_xml = GB_BOXmlTools.toStringXml(a_bo, a_encoding, false);
        String l_xml2 = GB_BOXmlTools.toStringXml(a_bo, a_encoding, true);
        assertNotNull("l_xml", l_xml);
        assertNotNull("l_xml2", l_xml2);
    }

    public void testWriteXmlFile() throws Throwable {
        List l_bos = getBOsForTest();
        GB_SimpleBusinessObject[] l_bo1 = new GB_SimpleBusinessObject[1];
        int len = CTools.getSize(l_bos);
        for (int i = 0; i < len; i++) {
            l_bo1[0] = (GB_SimpleBusinessObject) l_bos.get(i);
            testWriteXmlFile(i, l_bo1, ENCODING.ISO_8859_1);
            testWriteXmlFile(i, l_bo1, ENCODING.UTF8);
        }
        GB_SimpleBusinessObject[] l_boArray = (GB_SimpleBusinessObject[]) l_bos.toArray(new GB_SimpleBusinessObject[l_bos.size()]);
        testWriteXmlFile(len, l_boArray, ENCODING.ISO_8859_1);
        testWriteXmlFile(len, l_boArray, ENCODING.UTF8);
    }

    /**
     * testWriteXmlFile.
     */
    protected void testWriteXmlFile(int a_index, GB_SimpleBusinessObject[] a_bos, String a_encoding) throws Throwable {
        GB_BOFactory l_factory = GB_BOFactoryTools.getFactory();
        File l_file = FTools.getTempFile("~bo.xml");
        l_factory.writeXmlFile(l_file, a_bos[0], a_encoding);
        GB_SimpleBusinessObject[] l_bos = l_factory.loadFromXmlFile(l_file);
        assertEquals(a_index + ".1 - check size", 1, l_bos.length);
        checkBOEquals(a_index + ".2", a_bos[0], l_bos[0]);
        l_factory.writeXmlFile(l_file, a_bos, a_encoding);
        l_bos = l_factory.loadFromXmlFile(l_file);
        assertEquals(a_index + ".3 - check size", a_bos.length, l_bos.length);
        checkBOsEquals(a_index + ".4", a_bos, l_bos);
    }

    public void testXXXData() throws Throwable {
        List l_bos = getBOsGenericNotNull();
        int len = CTools.getSize(l_bos);
        for (int i = 0; i < len; i++) {
            GB_SimpleBusinessObject l_bo = (GB_SimpleBusinessObject) l_bos.get(i);
            testXXXData(i, l_bo);
        }
    }

    protected void testXXXData(int a_index, GB_SimpleBusinessObject a_bo) throws Throwable {
        if (getClassData() == null) {
            if (a_bo != null) {
                System.out.println("Ignore test testXXXData for bo: " + a_bo.getBOName());
            }
            return;
        }
        testXXXDataCopy(a_index, a_bo);
        String l_boName = getBOName();
        GB_SimpleBusinessObject l_bo4 = GB_BORandomTools.newBusinessObjectRandom(l_boName, true, true);
        GB_BODataTools.copyValuesWithData(l_bo4, a_bo);
        checkBOEquals(a_index + ".4", a_bo, l_bo4);
        GB_SimpleBusinessObject l_bo5 = GB_BORandomTools.newBusinessObjectRandom(l_boName, true, true);
        GB_BOData l_data = GB_BODataTools.toBOData(a_bo);
        l_data.updateBO(l_bo5);
        checkBOEquals(a_index + ".5", a_bo, l_bo5);
    }

    protected void testXXXDataCopy(int a_index, GB_SimpleBusinessObject a_bo) throws Throwable {
        if (getClassData() == null) {
            if (a_bo != null) {
                System.out.println("Ignore test testXXXData for bo: " + a_bo.getBOName());
            }
            return;
        }
        GB_BOTools.setPrimitiveValues((GB_SimpleBusinessObjectSet) a_bo, true);
        String l_boName = getBOName();
        String l_className = getClassData().getName();
        GB_BOData l_data = (GB_BOData) GB_ReflectTools.newInstance(l_className);
        l_data.updateFromBO(a_bo);
        GB_SimpleBusinessObject l_bo = GB_BOFactoryTools.getFactory().newBusinessObject(l_boName);
        l_data.updateBO(l_bo);
        checkBOEquals(a_index + ".1", a_bo, l_bo);
        GB_SimpleBusinessObject l_bo2 = GB_BOTools.newBO(a_bo.getBOName());
        GB_BODataTools.copyValuesWithData(l_bo2, a_bo);
        checkBOEquals(a_index + ".2", a_bo, l_bo2);
        GB_SimpleBusinessObject l_bo3 = GB_BODataTools.cloneWithData(a_bo);
        checkBOEquals(a_index + ".3", a_bo, l_bo3);
    }

    /**
     * Initalise 2 BO avec et sans valeurs par default
     * Passage par Data.
     * Comparaison des 2 BO resultants.
     */
    public void testXXXDataDefaultValues() throws Throwable {
        String l_className = getClassData().getName();
        if (l_className == null) {
            return;
        }
        String l_boName = getBOName();
        Class l_boClass = getClassBO();
        GB_SimpleBusinessObject l_bo = (GB_SimpleBusinessObject) l_boClass.newInstance();
        GB_SimpleBusinessObject l_bo2 = (GB_SimpleBusinessObject) l_boClass.newInstance();
        GB_BOTools.setPrimitiveValues((GB_SimpleBusinessObjectSet) l_bo2, true);
        GB_BOData l_data = (GB_BOData) GB_ReflectTools.newInstance(l_className);
        l_data.updateFromBO(l_bo);
        GB_BOData l_data2 = (GB_BOData) GB_ReflectTools.newInstance(l_className);
        l_data2.updateFromBO(l_bo2);
        l_bo = GB_BOFactoryTools.getFactory().newBusinessObject(l_boName);
        l_data.updateBO(l_bo);
        l_bo2 = GB_BOFactoryTools.getFactory().newBusinessObject(l_boName);
        l_data2.updateBO(l_bo2);
        checkBOEquals("ckeckEquals", l_bo, l_bo2);
    }

    public void testXXXShort() throws Throwable {
        String l_boName = getBOName();
        if (!l_boName.endsWith("Short")) {
            return;
        }
        String l_boName2 = STools.removeEnd(l_boName, "Short");
        GB_BOShort l_boShort1 = (GB_BOShort) GB_BORandomTools.newBusinessObjectRandom(l_boName, false);
        GB_BOShort l_boShort2 = (GB_BOShort) GB_BORandomTools.newBusinessObjectRandom(l_boName, false);
        GB_SimpleBusinessObject l_bo = GB_BORandomTools.newBusinessObjectRandom(l_boName2, false);
        l_boShort1.updateFromBO(l_bo);
        l_boShort2.updateFromBO(l_bo);
        checkBOEquals("1", l_boShort1, l_boShort2);
    }
}
