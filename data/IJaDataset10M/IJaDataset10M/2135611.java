package org.gromurph.util;

import java.awt.Component;
import java.awt.Container;
import java.io.*;
import java.util.*;
import javax.swing.JComponent;
import junit.framework.*;

/**
 * extends Junit testcase to supply some gromurph specific checks
 */
public class UtilTestCase extends TestCase {

    public static Test suite(Class c) {
        return new TestSuite(c);
    }

    public static void baseMain(Class c) {
        org.gromurph.util.Util.setTesting(true);
        String[] testCases = { c.getName() };
        junit.textui.TestRunner.main(testCases);
    }

    public UtilTestCase(String name) {
        super(name);
    }

    /**
     * creates new baseobject using the default constructor
     * then runs the xml read/write test
     */
    protected boolean xmlEquals(BaseObject obj) {
        return xmlEquals(obj, null);
    }

    /**
     * creates new baseobject using the default constructor
     * then runs the xml read/write test
     */
    protected boolean xmlEquals(BaseObject obj, Object toproot) {
        try {
            BaseObject obj2 = (BaseObject) obj.getClass().newInstance();
            return xmlEquals(obj, obj2, toproot);
        } catch (Exception e) {
            return false;
        }
    }

    protected String toXml(BaseObject obj) {
        try {
            return new String(xmlObjectToByteArray("Test", obj));
        } catch (IOException e) {
            return (e.toString());
        }
    }

    protected String toFromXml(BaseObject obj) {
        try {
            byte[] ba = xmlObjectToByteArray("Test", obj);
            BaseObject obj2 = (BaseObject) obj.getClass().newInstance();
            xmlByteArrayToObject(obj2, ba, null);
            return new String(xmlObjectToByteArray("Test", obj2));
        } catch (Exception e) {
            return (e.toString());
        }
    }

    /**
     * writes an obj to a test file in XML format, then reads it back
     * into a second object and compares the two for equality
     */
    protected boolean xmlEquals(BaseObject obj, BaseObject obj2, Object toproot) {
        try {
            if (obj == null || obj2 == null) return false;
            byte[] ba = null;
            if (!xmlToFile) {
                ba = xmlObjectToByteArray("Test", obj);
                xmlByteArrayToObject(obj2, ba, toproot);
            } else {
                StringWriter writer = new StringWriter();
                obj.xmlWriteToWriter(writer, "Test");
                writer.close();
                StringReader reader = new StringReader(writer.toString());
                obj2.xmlReadFromReader(reader, toproot);
            }
            if (obj.junitEquals(obj2)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return false;
        }
    }

    protected static boolean xmlToFile = true;

    public static void setXmlToFile(boolean t) {
        xmlToFile = t;
    }

    public void xmlObjectToObject(BaseObject src, BaseObject dest) {
        String tag = "OtoO";
        byte[] xmlbytes = null;
        try {
            xmlbytes = xmlObjectToByteArray(tag, src);
            xmlByteArrayToObject(dest, xmlbytes, null);
        } catch (Exception ex) {
        }
    }

    public static boolean xmlByteArrayToObject(BaseObject obj, byte[] ba, Object toproot) throws IOException {
        try {
            obj.xmlReadFromReader(new InputStreamReader(new BufferedInputStream(new ByteArrayInputStream(ba))), toproot);
            return true;
        } catch (java.io.IOException e) {
            throw e;
        } catch (Exception saxEx2) {
        }
        return false;
    }

    /**
     * writes to disk in xml format
     * @param tag the XML tag for the parent of the object
     * @param obj the object to be sent to a file
     *
     * @throws IOException if unable to open the file
     */
    public byte[] xmlObjectToByteArray(String tag, BaseObject obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Writer w = new OutputStreamWriter(bos);
        obj.xmlWriteToWriter(w, tag);
        return bos.toByteArray();
    }

    /**
     * Quick wrapper interface for passing a "command" to run in a test
     * such as assertException
     */
    public interface Runner {

        public void run() throws Exception;
    }

    /**
     * Tests to ensure that a user specified command generates a
     * specific exception instance (or subclass thereof)
     *
     * @param exeClass the Class of the expected exception
     * @param exec The runner to be executed, but firing exec.run()
     *
     */
    public void assertException(Class excClass, Runner exec) {
        StringBuffer failMsg = new StringBuffer();
        try {
            exec.run();
            failMsg.append("Expected exception, ");
            failMsg.append(excClass.getName());
            failMsg.append(", was not thrown");
            fail(failMsg.toString());
        } catch (Exception exc) {
            failMsg.append("UnExpected exception, ");
            failMsg.append(exc);
            failMsg.append(" thrown");
            if (!(excClass.isInstance(exc))) fail(failMsg.toString());
        }
    }

    /**
     * returns a map by component name (string) of the components found
     * in a Module (and its various sub-panels).
     */
    Map findMemberComponents(BaseEditor m) {
        return m.findMemberComponents();
    }

    /**
     * recursive routine that builds a map of components in a container and
     * its sub-containers. The returned map contains a list for each
     * unique JComponent class found (the map's key is the Class) and
     * the element is a vector of compoents of that class
     */
    Map<Class, List<JComponent>> buildComponentMap(Map<Class, List<JComponent>> parentMap, Container container) {
        Map<Class, List<JComponent>> localMap = (parentMap == null) ? new HashMap<Class, List<JComponent>>() : parentMap;
        if (container == null) return localMap;
        Component[] cArray = container.getComponents();
        for (int i = 0; i < cArray.length; i++) {
            if (cArray[i] instanceof JComponent) {
                JComponent jc = (JComponent) cArray[i];
                List<JComponent> clist = localMap.get(jc.getClass());
                if (clist == null) {
                    clist = new ArrayList<JComponent>(5);
                    localMap.put(jc.getClass(), clist);
                }
                clist.add(jc);
                buildComponentMap(localMap, jc);
            }
        }
        return localMap;
    }

    /**
     * internal test/debug routine to display a map of components to the console
     */
    public void showComponents(Map componentMap) {
        Iterator t = componentMap.keySet().iterator();
        while (t.hasNext()) {
            String name = (String) t.next();
            JComponent jc = (JComponent) componentMap.get(name);
            System.out.print(name);
            System.out.print(":  ");
            System.out.println(jc.getClass().getName());
        }
    }

    /**
     * internal test/debug routine to show the Component maps of
     * component classes and the size of the component.
     */
    public void showMapSizes(Map componentMap) {
        Iterator keys = componentMap.keySet().iterator();
        while (keys.hasNext()) {
            Class k = (Class) keys.next();
            List l = (List) componentMap.get(k);
            System.out.print(k.getName());
            System.out.print(":  ");
            System.out.println(l.size());
        }
    }

    public void testDummy() {
    }
}
