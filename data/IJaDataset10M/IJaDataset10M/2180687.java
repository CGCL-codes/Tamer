package vdoclet.ejb;

import junit.framework.TestCase;
import vdoclet.docinfo.*;
import java.util.*;

public class EjbInfoPrimaryKey_Test extends Ejb_BaseTest {

    public EjbInfoPrimaryKey_Test(String name) {
        super(name);
    }

    public void testPrimaryKeyClassNameSimple() {
        ClassInfo srcClass = new ClassInfo("demo.FubarBean");
        EjbInfo ejb = new EjbInfo(srcClass);
        assertEquals("demo.FubarKey", ejb.getPrimaryKeyClassName());
    }

    public void testPrimaryKeyClassNameExplicit() {
        ClassInfo srcClass = new ClassInfo("demo.FubarBean");
        srcClass.addTag(EjbTags.KEY_CLASS, "demo.AKeyBrayKey");
        EjbInfo ejb = new EjbInfo(srcClass);
        assertEquals("demo.AKeyBrayKey", ejb.getPrimaryKeyClassName());
    }

    public void testPrimaryKeyClassNameExplicitPrefix() {
        ClassInfo srcClass = new ClassInfo("demo.FubarBean");
        srcClass.addTag(EjbTags.BASE_NAME, "SumfinElts");
        EjbInfo ejb = new EjbInfo(srcClass);
        assertEquals("SumfinEltsKey", ejb.getPrimaryKeyClassName());
    }

    public void testPrimaryKeyRequired() {
        ClassInfo srcClass = new ClassInfo("demo.FubarBean");
        srcClass.addTag(EjbTags.KEY_GENERATE, "yup");
        EjbInfo ejb = new EjbInfo(srcClass);
        assertTrue(ejb.isPrimaryKeyRequired());
    }

    public void testPrimaryKeyNotRequired() {
        ClassInfo srcClass = new ClassInfo("demo.FubarBean");
        EjbInfo ejb = new EjbInfo(srcClass);
        assertTrue(!ejb.isPrimaryKeyRequired());
    }

    public void testGetPrimaryKeyFieldsWithMethod() {
        ClassInfo srcClass = new ClassInfo("demo.FubarBean");
        srcClass.addTag(EjbTags.KEY_GENERATE, "yup");
        MethodInfo getIdMethod = new MethodInfo("int", "getId");
        getIdMethod.addTag(EjbTags.KEY_FIELD, "yup");
        srcClass.addMethod(getIdMethod);
        EjbInfo ejb = new EjbInfo(srcClass);
        assertEquals(1, ejb.getPrimaryKeyFields().size());
    }

    public void testGetPrimaryKeyFieldsWithField() {
        ClassInfo srcClass = new ClassInfo("demo.FubarBean");
        srcClass.addTag(EjbTags.KEY_GENERATE, "yup");
        srcClass.addTag(EjbTags.KEY_FIELD, "int id");
        EjbInfo ejb = new EjbInfo(srcClass);
        assertEquals(1, ejb.getPrimaryKeyFields().size());
        FieldInfo keyField = (FieldInfo) ejb.getPrimaryKeyFields().iterator().next();
        assertEquals(vdoclet.beaninfo.PropertyInfo.class, keyField.getClass());
        assertEquals("int", keyField.getType());
        assertEquals("id", keyField.getName());
    }

    public void testGetPrimaryKey() {
        ClassInfo srcClass = new ClassInfo("demo.FubarBean");
        srcClass.addTag(EjbTags.KEY_GENERATE, "yup");
        MethodInfo getIdMethod = new MethodInfo("int", "getId");
        getIdMethod.addTag(EjbTags.KEY_FIELD, "yup");
        srcClass.addMethod(getIdMethod);
        EjbInfo ejb = new EjbInfo(srcClass);
        ClassInfo keyClass = ejb.getPrimaryKey();
        assertNotNull(keyClass);
        List keyFields = keyClass.getFields();
        assertEquals(1, keyFields.size());
    }
}
