package net.sf.myra.datamining.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import junit.framework.TestCase;
import net.sf.myra.datamining.io.ArffHelper;
import net.sf.myra.datamining.io.ExtendedArffHelper;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 2164 $ $Date:: 2009-06-15 14:58:29#$
 */
public class ClassHierarchyTest extends TestCase {

    public void testLeaf() {
        try {
            Dataset dataset = new ArffHelper().read(new InputStreamReader(getClass().getResourceAsStream("/example2.arff")));
            ClassHierarchy hierarchy = dataset.getMetadata().getClassHierarchy();
            assertEquals("D", hierarchy.leaf("A", "B", "D"));
            assertEquals("G", hierarchy.leaf("A", "C", "F", "G"));
            assertEquals("F", hierarchy.leaf("A", "C", "F"));
            assertEquals(2, hierarchy.leaves("A", "B", "C", "F").size());
            assertEquals(3, hierarchy.leaves("A", "B", "C", "F", "E").size());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    public void testClone() {
        try {
            Dataset dataset = new ArffHelper().read(new InputStreamReader(getClass().getResourceAsStream("/example2.arff")));
            ClassHierarchy h1 = dataset.getMetadata().getClassHierarchy();
            ClassHierarchy h2 = h1.clone();
            assertTrue(h1.toString().equals(h2.toString()));
            h1.remove("D");
            assertFalse(h1.toString().equals(h2.toString()));
            h2.remove("D");
            assertTrue(h1.toString().equals(h2.toString()));
            dataset = new ExtendedArffHelper().read(new InputStreamReader(getClass().getResourceAsStream("/large-hierarchy.arffx")));
            h1 = dataset.getMetadata().getClassHierarchy();
            h2 = h1.clone();
            assertTrue(h1.toString().equals(h2.toString()));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    public void testWeigh() {
        try {
            Dataset dataset = new ArffHelper().read(new InputStreamReader(getClass().getResourceAsStream("/example.arff")));
            ClassHierarchy h = dataset.getMetadata().getClassHierarchy();
            h.weigh();
            assertEquals(0.75, h.getRoot().getWeight());
            assertEquals(0.75, h.find("B").getWeight());
            assertEquals(0.75, h.find("C").getWeight());
            assertEquals(0.5625, h.find("E").getWeight());
            assertEquals(0.5625, h.find("F").getWeight());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    public void testSerialization() {
        try {
            Dataset dataset = new ArffHelper().read(new InputStreamReader(getClass().getResourceAsStream("/example.arff")));
            ClassHierarchy h = dataset.getMetadata().getClassHierarchy();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(h);
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(output.toByteArray()));
            ClassHierarchy clone = (ClassHierarchy) in.readObject();
            assertEquals(h.toString(), clone.toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
