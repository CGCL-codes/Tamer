package org.apache.axis2.rmi.databind;

import org.apache.axis2.rmi.Configurator;
import org.apache.axis2.rmi.databind.dto.ChildClass;
import org.apache.axis2.rmi.databind.service.Service3;
import org.apache.axis2.rmi.metadata.Operation;
import org.apache.axis2.rmi.metadata.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ExtensionRequestResponseTest extends RequestResponseTest {

    protected void setUp() throws Exception {
        this.serviceClass = Service3.class;
        this.serviceObject = new Service3();
        this.configurator = new Configurator();
        this.configurator.addExtension(ChildClass.class);
        this.processedMap = new HashMap();
        this.schemaMap = new HashMap();
        this.service = new Service(this.serviceClass, this.configurator);
        this.service.populateMetaData();
        this.service.generateSchema();
        this.javaObjectSerializer = new JavaObjectSerializer(service.getProcessedTypeMap(), this.service.getConfigurator(), this.service.getSchemaMap());
        this.xmlStreamParser = new XmlStreamParser(service.getProcessedTypeMap(), this.service.getConfigurator(), this.service.getSchemaMap());
    }

    public void testMethod1() {
        try {
            Operation operation = this.service.getOperation("method1");
            List inputObjects = new ArrayList();
            ChildClass childClass = new ChildClass();
            childClass.setParam1("test param1");
            childClass.setParam2(5);
            childClass.setParam3(23.45f);
            childClass.setParam4(34.5);
            inputObjects.add(childClass);
            Object[] objects = getInputObject(inputObjects, operation);
            ChildClass object = (ChildClass) operation.getJavaMethod().invoke(this.serviceObject, objects);
            ChildClass returnObject = (ChildClass) getReturnObject(object, operation);
            assertEquals(returnObject.getParam1(), "test param1");
            assertEquals(returnObject.getParam2(), 5);
            assertTrue(childClass.getParam3() == 23.45f);
            assertTrue(childClass.getParam4() == 34.5);
        } catch (Exception e) {
            fail();
        }
    }

    public void testMethod21() {
        try {
            Operation operation = this.service.getOperation("method2");
            List inputObjects = new ArrayList();
            inputObjects.add(new Object());
            Object[] objects = getInputObject(inputObjects, operation);
            Object object = operation.getJavaMethod().invoke(this.serviceObject, objects);
            Object returnObject = getReturnObject(object, operation);
            assertNotNull(returnObject);
        } catch (Exception e) {
            fail();
        }
    }

    public void testMethod22() {
        try {
            Operation operation = this.service.getOperation("method2");
            List inputObjects = new ArrayList();
            Date date = new Date();
            inputObjects.add(date);
            Object[] objects = getInputObject(inputObjects, operation);
            Date object = (Date) operation.getJavaMethod().invoke(this.serviceObject, objects);
            Date returnObject = (Date) getReturnObject(object, operation);
            assertEquals(returnObject.getDate(), date.getDate());
        } catch (Exception e) {
            fail();
        }
    }
}
