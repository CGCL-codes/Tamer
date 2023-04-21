package org.neodatis.odb.test.refactoring;

import java.util.Date;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;

public class TestRefactoring1 extends ByteCodeTest {

    public void start() throws Exception {
        resetDb();
    }

    public void step1() throws Exception {
        JavaAssistUtility jau = new JavaAssistUtility();
        String className = "Test3";
        String[] fieldNames = { "field1", "field2" };
        Class[] fieldTypes = { String.class, Integer.TYPE };
        Class c = jau.createClass(className, fieldNames, fieldTypes);
        ODB odb = null;
        try {
            odb = open();
            Object o = c.newInstance();
            setFieldValue(o, "field1", "step1:A string value");
            setFieldValue(o, "field2", new Integer("1"));
            odb.store(o);
            odb.close();
            testOk("step1");
            closeServer();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            if (odb != null) {
                odb.close();
            }
            closeServer();
            System.exit(1);
        } finally {
        }
    }

    public void step2() throws Exception {
        JavaAssistUtility jau = new JavaAssistUtility();
        String className = "Test3";
        String[] fieldNames = { "field1", "field2", "field3" };
        Class[] fieldTypes = { String.class, Integer.TYPE, Date.class };
        Class c = jau.createClass(className, fieldNames, fieldTypes);
        ODB odb = null;
        try {
            odb = open();
            Objects objects = odb.getObjects(c);
            System.out.println("size=" + objects.size());
            Object o = c.newInstance();
            setFieldValue(o, "field1", "step2:another string value");
            setFieldValue(o, "field2", new Integer("2"));
            setFieldValue(o, "field3", new Date());
            odb.store(o);
            testOk("step2");
            odb.close();
            odb = open();
            objects = odb.getObjects(c);
            odb.close();
            closeServer();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            if (odb != null) {
                odb.close();
            }
            closeServer();
            System.exit(1);
        } finally {
        }
    }

    public void step3() throws Exception {
        JavaAssistUtility jau = new JavaAssistUtility();
        String className = "Test3";
        String[] fieldNames = { "field2" };
        Class[] fieldTypes = { Integer.TYPE };
        Class c = jau.createClass(className, fieldNames, fieldTypes);
        ODB odb = null;
        try {
            odb = open();
            Objects objects = odb.getObjects(c);
            System.out.println("size=" + objects.size());
            Object o = c.newInstance();
            setFieldValue(o, "field2", new Integer("22"));
            odb.store(o);
            odb.close();
            closeServer();
            testOk("step3");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            if (odb != null) {
                odb.close();
            }
            closeServer();
            System.exit(1);
        } finally {
        }
    }

    public void step4() throws Exception {
        JavaAssistUtility jau = new JavaAssistUtility();
        String className = "Test3";
        String[] fieldNames = {};
        Class[] fieldTypes = {};
        Class c = jau.createClass(className, fieldNames, fieldTypes);
        ODB odb = null;
        try {
            odb = open();
            Objects objects = odb.getObjects(c);
            System.out.println("size=" + objects.size());
            Object o = c.newInstance();
            odb.store(o);
            odb.close();
            closeServer();
            testOk("step4");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            if (odb != null) {
                odb.close();
            }
            closeServer();
            System.exit(1);
        } finally {
        }
    }

    public static void main(String[] args) throws Exception {
        TestRefactoring1 tf = new TestRefactoring1();
        tf.execute(args);
    }
}
