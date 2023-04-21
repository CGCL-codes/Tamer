package test.de.unibi.techfak.bibiserv.biodom.warning;

import java.util.List;
import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import de.unibi.techfak.bibiserv.biodom.warning.BioDOMWarningBox;
import de.unibi.techfak.bibiserv.biodom.warning.BioDOMWarningBoxInterface;

/**
 * This is the test for the BioDOMWarningBox and BioDOMWarningBoxInterface.
 * 
 * @author Kai Loewenthal <kloewent@techfak.uni-bielefeld.de>
 * @version 0.1
 * @date 2006-02-20
 */
public class BioDOMWarningBoxInterfaceTest extends TestCase {

    BioDOMWarningBoxInterface warningBox;

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BioDOMWarningBoxInterfaceTest.class);
    }

    @BeforeClass
    protected void setUp() throws Exception {
        super.setUp();
        warningBox = new BioDOMWarningBox();
    }

    @AfterClass
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testAppendWarning() {
        Assert.assertFalse(warningBox.containsWarning());
        warningBox.appendWarning(this, "my warning!");
        Assert.assertTrue(warningBox.containsWarning());
        Assert.assertEquals(warningBox.getMessage(), this.getClass().getName() + " - my warning!" + System.getProperty("line.separator"));
        warningBox.appendWarning(this, "my warning 2!");
        String myTestParent = new String();
        warningBox.appendWarning(myTestParent, "my special warning!");
        List<String> warningList = warningBox.getWarnings();
        Assert.assertEquals(warningList.size(), 3);
        Assert.assertTrue(warningList.contains(this.getClass().getName() + " - my warning!" + System.getProperty("line.separator")));
        Assert.assertTrue(warningList.contains(this.getClass().getName() + " - my warning 2!" + System.getProperty("line.separator")));
        Assert.assertTrue(warningList.contains(myTestParent.getClass().getName() + " - my special warning!" + System.getProperty("line.separator")));
    }

    @Test
    public void testGetWarnings() {
        List<String> warnings = warningBox.getWarnings();
        Assert.assertNull(warnings);
        warningBox.appendWarning(this, "my warning!");
        warnings = warningBox.getWarnings();
        Assert.assertNotNull(warnings);
        Assert.assertEquals(warnings.size(), 1);
        Assert.assertEquals(warnings.get(0), this.getClass().getName() + " - my warning!" + System.getProperty("line.separator"));
        warningBox.appendWarning(this, "my second warning!");
        String myTestParent = new String();
        warningBox.appendWarning(myTestParent, "my special warning!");
        warnings = warningBox.getWarnings(true);
        Assert.assertNotNull(warnings);
        Assert.assertEquals(warnings.size(), 3);
        Assert.assertTrue(warnings.contains(this.getClass().getName() + " - my warning!" + System.getProperty("line.separator")));
        Assert.assertTrue(warnings.contains(this.getClass().getName() + " - my second warning!" + System.getProperty("line.separator")));
        Assert.assertTrue(warnings.contains(myTestParent.getClass().getName() + " - my special warning!" + System.getProperty("line.separator")));
        warnings = warningBox.getWarnings(myTestParent.getClass());
        Assert.assertNotNull(warnings);
        Assert.assertEquals(warnings.size(), 1);
        Assert.assertEquals(warnings.get(0), myTestParent.getClass().getName() + " - my special warning!" + System.getProperty("line.separator"));
        warnings = warningBox.getWarnings(myTestParent.getClass(), false);
        Assert.assertNotNull(warnings);
        Assert.assertEquals(warnings.size(), 1);
        Assert.assertEquals(warnings.get(0), myTestParent.getClass().getName() + " - my special warning!" + System.getProperty("line.separator"));
        warnings = warningBox.getWarnings();
        Assert.assertNotNull(warnings);
        Assert.assertEquals(warnings.size(), 2);
        Assert.assertTrue(warnings.contains(this.getClass().getName() + " - my warning!" + System.getProperty("line.separator")));
        Assert.assertTrue(warnings.contains(this.getClass().getName() + " - my second warning!" + System.getProperty("line.separator")));
        warningBox.appendWarning(myTestParent, "my new special warning!");
        warnings = warningBox.getWarnings(false);
        Assert.assertNotNull(warnings);
        Assert.assertEquals(warnings.size(), 3);
        Assert.assertTrue(warnings.contains(this.getClass().getName() + " - my warning!" + System.getProperty("line.separator")));
        Assert.assertTrue(warnings.contains(this.getClass().getName() + " - my second warning!" + System.getProperty("line.separator")));
        Assert.assertTrue(warnings.contains(myTestParent.getClass().getName() + " - my new special warning!" + System.getProperty("line.separator")));
        warnings = warningBox.getWarnings();
        Assert.assertNull(warnings);
    }

    @Test
    public void testGetMessage() {
        String warning = warningBox.getMessage();
        Assert.assertNull(warning);
        warningBox.appendWarning(this, "my warning!");
        warning = warningBox.getMessage();
        Assert.assertNotNull(warning);
        Assert.assertEquals(warning, this.getClass().getName() + " - my warning!" + System.getProperty("line.separator"));
        warningBox.appendWarning(this, "my second warning!");
        warning = warningBox.getMessage(true);
        Assert.assertNotNull(warning);
        Assert.assertEquals(warning, this.getClass().getName() + " - my warning!" + System.getProperty("line.separator") + this.getClass().getName() + " - my second warning!" + System.getProperty("line.separator"));
        String myTestParent = new String();
        warningBox.appendWarning(myTestParent, "my special warning!");
        warning = warningBox.getMessage(myTestParent.getClass());
        Assert.assertNotNull(warning);
        Assert.assertEquals(warning, myTestParent.getClass().getName() + " - my special warning!" + System.getProperty("line.separator"));
        warning = warningBox.getMessage(myTestParent.getClass(), false);
        Assert.assertNotNull(warning);
        Assert.assertEquals(warning, myTestParent.getClass().getName() + " - my special warning!" + System.getProperty("line.separator"));
        warning = warningBox.getMessage("#WARNING# - #CLASS#");
        Assert.assertNotNull(warning);
        Assert.assertEquals(warning, "my warning! - " + this.getClass().getName() + "my second warning! - " + this.getClass().getName());
        warningBox.appendWarning(this, "my new special warning!");
        warning = warningBox.getMessage(false);
        Assert.assertNotNull(warning);
        Assert.assertEquals(warning, this.getClass().getName() + " - my warning!" + System.getProperty("line.separator") + this.getClass().getName() + " - my second warning!" + System.getProperty("line.separator") + this.getClass().getName() + " - my new special warning!" + System.getProperty("line.separator"));
        warning = warningBox.getMessage();
        Assert.assertNull(warning);
    }

    @Test
    public void testContainsWarning() {
        Assert.assertFalse(warningBox.containsWarning());
        warningBox.appendWarning(this, "warning");
        Assert.assertTrue(warningBox.containsWarning());
        String myTestParent = new String();
        Assert.assertFalse(warningBox.containsWarning(myTestParent.getClass()));
        warningBox.appendWarning(myTestParent, "warning");
        Assert.assertTrue(warningBox.containsWarning(myTestParent.getClass()));
        Assert.assertTrue(warningBox.containsWarning(this.getClass()));
        warningBox.clearWarnings();
        Assert.assertFalse(warningBox.containsWarning());
        Assert.assertFalse(warningBox.containsWarning(myTestParent.getClass()));
    }

    @Test
    public void testCountWarnings() {
        Assert.assertEquals(warningBox.countWarnings(), 0);
        Assert.assertEquals(warningBox.countWarnings(this.getClass()), 0);
        warningBox.appendWarning(this, "warning");
        Assert.assertEquals(warningBox.countWarnings(), 1);
        Assert.assertEquals(warningBox.countWarnings(this.getClass()), 1);
        String myTestParent = new String();
        warningBox.appendWarning(myTestParent, "warning");
        Assert.assertEquals(warningBox.countWarnings(), 2);
        Assert.assertEquals(warningBox.countWarnings(this.getClass()), 1);
        Assert.assertEquals(warningBox.countWarnings(myTestParent.getClass()), 1);
        warningBox.appendWarning(this, "secound warning");
        Assert.assertEquals(warningBox.countWarnings(), 3);
        Assert.assertEquals(warningBox.countWarnings(this.getClass()), 2);
        Assert.assertEquals(warningBox.countWarnings(myTestParent.getClass()), 1);
        warningBox.clearWarnings();
        Assert.assertEquals(warningBox.countWarnings(), 0);
        Assert.assertEquals(warningBox.countWarnings(this.getClass()), 0);
        Assert.assertEquals(warningBox.countWarnings(myTestParent.getClass()), 0);
    }
}
