package org.xwt.annotils;

import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.xwt.domain.FieldAnnotation;
import org.xwt.domain.MultipleParamsAnnotation;
import org.xwt.domain.Person;
import org.xwt.domain.PersonChild;
import org.xwt.domain.UnUsedAnnotation;

public class AnnotationUtilUnitTest {

    private AnnotationUtil annotationUtil;

    @Before
    public void before() {
        annotationUtil = new AnnotationUtilDefaultImpl();
    }

    @Test
    public void testGetDeclaredFieldsWithAnnotationReturnsOneField() throws Exception {
        List<Field> fields = annotationUtil.getDeclaredFieldsWithAnnotation(FieldAnnotation.class, Person.class);
        assertEquals(2, fields.size());
    }

    @Test
    public void testGetDeclaredFieldsWithAnnotationReturnsNoFields() throws Exception {
        List<Field> fields = annotationUtil.getDeclaredFieldsWithAnnotation(UnUsedAnnotation.class, Person.class);
        assertEquals(0, fields.size());
    }

    @Test
    public void testGetDeclaredFieldsWithAnnotationAndOneParamReturnsOneField() throws Exception {
        List<Field> fields = annotationUtil.getDeclaredFieldsWithAnnotationParams(FieldAnnotation.class, Person.class, new String[] { "name" }, new Object[] { "domino" });
        assertEquals(1, fields.size());
    }

    @Test
    public void testGetDeclaredFieldsWithAnnotationMultipleParamsReturnsOneField() throws Exception {
        List<Field> fields = annotationUtil.getDeclaredFieldsWithAnnotationParams(MultipleParamsAnnotation.class, Person.class, new String[] { "name", "type" }, new Object[] { "ruleset", "safe" });
        assertEquals(1, fields.size());
    }

    @Test
    public void testGetDeclaredFieldsWithAnnotationMultipleParamsReturnsNoFields() throws Exception {
        List<Field> fields = annotationUtil.getDeclaredFieldsWithAnnotationParams(MultipleParamsAnnotation.class, Person.class, new String[] { "name", "type" }, new Object[] { "ruleset", "notsafe" });
        assertEquals(0, fields.size());
    }

    @Test
    public void testGetDeclaredFieldsWithAnnotationParamsReturnsNoField() throws Exception {
        List<Field> fields = annotationUtil.getDeclaredFieldsWithAnnotationParams(FieldAnnotation.class, Person.class, new String[] { "name" }, new Object[] { "what" });
        assertEquals(0, fields.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDecalaredFieldsWithAnnotationParamsInvalidParamValueLength() throws Exception {
        annotationUtil.getDeclaredFieldsWithAnnotationParams(FieldAnnotation.class, Person.class, new String[] { "name", "yikes" }, new Object[] { "what" });
    }

    @Test(expected = NullPointerException.class)
    public void testGetDecalaredFieldsWithAnnotationParamsNullParamValue() throws Exception {
        annotationUtil.getDeclaredFieldsWithAnnotationParams(FieldAnnotation.class, Person.class, null, null);
    }

    @Test
    public void testGetDeclaredFieldsWithAnnotationsFromSuperClass() throws Exception {
        PersonChild personChild = new PersonChild();
        List<Field> fields = annotationUtil.getDeclaredFieldsWithAnnotation(FieldAnnotation.class, PersonChild.class);
        assertEquals(2, fields.size());
    }

    @Test
    public void testGetMethodByName() throws Exception {
        Method method = annotationUtil.getMethodByName("setName", Person.class);
        assertEquals("setName", method.getName());
    }

    @Test
    public void testGetMethodByNameReturnsNull() throws Exception {
        Method method = annotationUtil.getMethodByName("noSuchMethod", Person.class);
        assertNull(method);
    }

    @Test
    public void testGetFieldByName() throws Exception {
        Field field = annotationUtil.getDeclaredField("name", Person.class);
        assertNotNull(field);
        assertEquals("name", field.getName());
    }

    @Test
    public void testGetFieldByNameWithInvalidFieldName() throws Exception {
        Field field = annotationUtil.getDeclaredField("nosuchfield", Person.class);
        assertNull(field);
    }

    @Test
    public void testGetFieldValue() throws Exception {
        Person person = new Person();
        person.setName("name");
        Object value = annotationUtil.getFieldValue(annotationUtil.getDeclaredField("name", Person.class), person);
        assertEquals("name", value);
    }

    @Test(expected = NullPointerException.class)
    public void testGetFieldValueWithInvalidField() throws Exception {
        Person person = new Person();
        person.setName("name");
        Object value = annotationUtil.getFieldValue(annotationUtil.getDeclaredField("name123", Person.class), person);
    }

    public void testSetFieldValue() throws Exception {
        Person person = new Person();
        annotationUtil.setFieldValue(annotationUtil.getDeclaredField("name", Person.class), person, "name");
        assertEquals("name", person.getName());
    }
}
