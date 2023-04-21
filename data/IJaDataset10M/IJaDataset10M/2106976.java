package com.dyuproject.protostuff.parser;

import java.io.File;
import java.util.Collection;
import junit.framework.TestCase;

/**
 * Tests for annotations on messages, enums, fields, services, rpc methods, extensions
 *
 * @author David Yu
 * @created Dec 30, 2010
 */
public class AnnotationTest extends TestCase {

    public void testIt() throws Exception {
        File f = ProtoParserTest.getFile("com/dyuproject/protostuff/parser/test_annotations.proto");
        assertTrue(f.exists());
        Proto proto = new Proto(f);
        ProtoUtil.loadFrom(f, proto);
        Message person = proto.getMessage("Person");
        assertNotNull(person);
        Annotation defaultPerson = person.getAnnotation("DefaultPerson");
        assertNotNull(defaultPerson);
        assertEquals("Anonymous Coward", defaultPerson.getValue("name"));
        Field<?> age = person.getField("age");
        assertNotNull(age);
        Annotation defaultAge = age.getAnnotation("DefaultAge");
        assertNotNull(defaultAge);
        assertTrue(defaultAge.getParams().isEmpty());
        EnumGroup gender = person.getNestedEnumGroup("Gender");
        assertNotNull(gender);
        Annotation defaultGender = gender.getAnnotation("DefaultGender");
        assertEquals("MALE", defaultGender.getValue("value"));
        EnumGroup.Value male = gender.getValue(0);
        assertNotNull(male);
        Annotation maleA = male.getAnnotation("Alias");
        assertNotNull(maleA);
        assertEquals("m", maleA.getValue("value"));
        EnumGroup.Value female = gender.getValue(1);
        assertNotNull(female);
        Annotation femaleA = female.getAnnotation("Alias");
        assertNotNull(femaleA);
        assertEquals("f", femaleA.getValue("value"));
        Message listRequest = person.getNestedMessage("ListRequest");
        assertNotNull(listRequest);
        Annotation nestedMessageAnnotation = listRequest.getAnnotation("NestedMessageAnnotation");
        assertNotNull(nestedMessageAnnotation);
        assertTrue(nestedMessageAnnotation.getParams().isEmpty());
        Message response = listRequest.getNestedMessage("Response");
        assertNotNull(response);
        Annotation deeperMessageAnnotation = response.getAnnotation("DeeperMessageAnnotation");
        assertNotNull(deeperMessageAnnotation);
        assertTrue(deeperMessageAnnotation.getParams().isEmpty());
        Field<?> personField = response.getField("person");
        assertNotNull(personField);
        Annotation deeperMessageFieldAnnotation = personField.getAnnotation("DeeperMessageFieldAnnotation");
        assertNotNull(deeperMessageFieldAnnotation);
        assertTrue(deeperMessageFieldAnnotation.getParams().size() == 2);
        assertEquals(false, deeperMessageFieldAnnotation.getValue("nullable"));
        assertEquals(Float.valueOf(1.1f), deeperMessageFieldAnnotation.getValue("version"));
        Collection<Extension> extensions = proto.getExtensions();
        assertTrue(extensions.size() == 1);
        Extension extendPerson = extensions.iterator().next();
        assertNotNull(extendPerson);
        Annotation personExtras = extendPerson.getAnnotation("PersonExtras");
        assertNotNull(personExtras);
        assertTrue(personExtras.getParams().isEmpty());
        Field<?> country = extendPerson.getField("country");
        assertNotNull(country);
        Annotation validate = country.getAnnotation("Validate");
        assertNotNull(validate);
        assertTrue(validate.getParams().isEmpty());
        Service personService = proto.getService("PersonService");
        assertNotNull(personService);
        assertTrue(personService.getAnnotationMap().size() == 2);
        Annotation someServiceAnnotation = personService.getAnnotation("SomeServiceAnnotation");
        Annotation anotherServiceAnnotation = personService.getAnnotation("AnotherServiceAnnotation");
        assertTrue(someServiceAnnotation != null && someServiceAnnotation.getParams().isEmpty());
        assertTrue(anotherServiceAnnotation != null && anotherServiceAnnotation.getParams().isEmpty());
        Service.RpcMethod put = personService.getRpcMethod("Put");
        assertNotNull(put);
        Annotation authRequired = put.getAnnotation("AuthRequired");
        assertNotNull(authRequired);
        assertTrue(authRequired.getParams().size() == 1);
        assertEquals("admin", authRequired.getValue("role"));
    }
}
