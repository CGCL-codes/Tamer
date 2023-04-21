package net.sf.sanity4j.model.diagnostic;

import java.util.Iterator;
import junit.framework.TestCase;

/**
 * DiagnosticCategory_Test - unit tests for {@link DiagnosticCategory}. 
 * 
 * @author Yiannis Paschalidis
 * @since Sanity4J 1.0
 */
public class DiagnosticCategory_Test extends TestCase {

    public void testConstructors() {
        DiagnosticCategory root = new DiagnosticCategory();
        assertNotNull("Name should not be null", root.getName());
        assertNull("Parent should be null", root.getParent());
    }

    public void testAddDiagnostic() {
        DiagnosticCategory root = new DiagnosticCategory();
        assertEquals("getLevel incorrect for root", 0, root.getLevel());
        addDiagnostic(root, "category/subcategory");
        assertTrue("root should not have diagnostics", root.getDiagnostics().isEmpty());
        DiagnosticCategory child = root.getSubCategories().get(0);
        assertEquals("Incorrect child name", "category", child.getName());
        assertTrue("Child should not have diagnostics", child.getDiagnostics().isEmpty());
        DiagnosticCategory grandChild = child.getSubCategories().get(0);
        assertEquals("Incorrect grandchild name", "subcategory", grandChild.getName());
        assertFalse("Grandchild should have a diagnostic", grandChild.getDiagnostics().isEmpty());
    }

    public void testIsRoot() {
        DiagnosticCategory root = new DiagnosticCategory();
        assertTrue("isRoot incorrect for root", root.isRoot());
        addDiagnostic(root, "dummy");
        DiagnosticCategory child = root.getSubCategories().get(0);
        assertFalse("isRoot incorrect for child", child.isRoot());
    }

    public void testGetLevel() {
        DiagnosticCategory root = new DiagnosticCategory();
        assertEquals("getLevel incorrect for root", 0, root.getLevel());
        addDiagnostic(root, "category/subcategory");
        DiagnosticCategory child = root.getSubCategories().get(0);
        assertEquals("getLevel incorrect for child", 1, child.getLevel());
        DiagnosticCategory grandChild = child.getSubCategories().get(0);
        assertEquals("getLevel incorrect for child", 2, grandChild.getLevel());
    }

    public void testGetParent() {
        DiagnosticCategory root = new DiagnosticCategory();
        assertNull("getParent incorrect for root", root.getParent());
        addDiagnostic(root, "dummy");
        DiagnosticCategory child = root.getSubCategories().get(0);
        assertEquals("getParent incorrect for child", root, child.getParent());
    }

    public void testGetDiagnosticCount() {
        DiagnosticCategory root = new DiagnosticCategory();
        addDiagnostic(root, "category/subcategory", Diagnostic.SEVERITY_MODERATE);
        addDiagnostic(root, "category", Diagnostic.SEVERITY_HIGH);
        DiagnosticCategory child = root.getSubCategories().get(0);
        DiagnosticCategory grandChild = child.getSubCategories().get(0);
        assertEquals("getDiagnosticCount incorrect for root", 2, root.getDiagnosticCount());
        assertEquals("getDiagnosticCount incorrect for child", 2, child.getDiagnosticCount());
        assertEquals("getDiagnosticCount incorrect for grandchild", 1, grandChild.getDiagnosticCount());
        assertEquals("getDiagnosticCount sev=all incorrect for root", 2, root.getDiagnosticCount(Diagnostic.SEVERITY_ALL));
        assertEquals("getDiagnosticCount sev=all incorrect for child", 2, child.getDiagnosticCount(Diagnostic.SEVERITY_ALL));
        assertEquals("getDiagnosticCount sev=all incorrect for grandchild", 1, grandChild.getDiagnosticCount(Diagnostic.SEVERITY_ALL));
        assertEquals("getDiagnosticCount sev=moderate incorrect for root", 1, root.getDiagnosticCount(Diagnostic.SEVERITY_MODERATE));
        assertEquals("getDiagnosticCount sev=moderate incorrect for child", 1, child.getDiagnosticCount(Diagnostic.SEVERITY_MODERATE));
        assertEquals("getDiagnosticCount sev=moderate incorrect for grandchild", 1, grandChild.getDiagnosticCount(Diagnostic.SEVERITY_MODERATE));
        assertEquals("getDiagnosticCount sev=high incorrect for root", 1, root.getDiagnosticCount(Diagnostic.SEVERITY_HIGH));
        assertEquals("getDiagnosticCount sev=high incorrect for child", 1, child.getDiagnosticCount(Diagnostic.SEVERITY_HIGH));
        assertEquals("getDiagnosticCount sev=high incorrect for grandchild", 0, grandChild.getDiagnosticCount(Diagnostic.SEVERITY_HIGH));
    }

    public void testSubCategoriesIterator() {
        DiagnosticCategory root = new DiagnosticCategory();
        assertEquals("getLevel incorrect for root", 0, root.getLevel());
        addDiagnostic(root, "category/subcategory1");
        addDiagnostic(root, "category/subcategory2");
        Iterator<DiagnosticCategory> iterator = root.subCategories();
        assertTrue("Iterator should have next", iterator.hasNext());
        Object next = iterator.next();
        assertTrue("Iterator next should be a DiagnosticCategory", next instanceof DiagnosticCategory);
        DiagnosticCategory child = (DiagnosticCategory) next;
        assertSame("Child's parent should be the root node", root, child.getParent());
        assertFalse("Iterator should not have next", iterator.hasNext());
        iterator = child.subCategories();
        assertTrue("Iterator should have next", iterator.hasNext());
        next = iterator.next();
        assertTrue("Iterator should have next", iterator.hasNext());
        next = iterator.next();
        assertFalse("Iterator should not have next", iterator.hasNext());
    }

    private void addDiagnostic(final DiagnosticCategory parent, final String category) {
        addDiagnostic(parent, category, Diagnostic.SEVERITY_MODERATE);
    }

    private void addDiagnostic(final DiagnosticCategory parent, final String category, final int severity) {
        Diagnostic diag = new Diagnostic() {

            public String[] getCategories() {
                return new String[] { category };
            }
        };
        diag.setSeverity(severity);
        parent.addDiagnostic(diag);
    }
}
