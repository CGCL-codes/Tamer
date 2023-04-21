package net.sf.refactorit.test.refactorings;

import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinModifier;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.encapsulatefield.EncapsulateField;
import net.sf.refactorit.source.format.FormatSettings;
import net.sf.refactorit.test.RwRefactoringTestUtils;
import org.apache.log4j.Category;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test driver for
 * {@link net.sf.refactorit.refactorings.encapsulatefield.EncapsulateField}.
 *
 *
 * @author Tanel Alumae
 */
public class EncapsulateFieldTest extends RefactoringTestCase {

    /** Logger instance. */
    private static final Category cat = Category.getInstance(EncapsulateFieldTest.class.getName());

    public EncapsulateFieldTest(String name) {
        super(name);
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite(EncapsulateFieldTest.class);
        suite.setName("EncapsulateField tests");
        return suite;
    }

    protected void setUp() throws Exception {
        FormatSettings.applyStyle(new FormatSettings.AqrisStyle());
    }

    public String getTemplate() {
        return "EncapsulateField/<stripped_test_name>/<in_out>";
    }

    private void performEncapsulate(String typeName, String fieldName, String getterName, String setterName, boolean preConditionsWarningExpected, boolean userInputWarningsExpected) throws Exception {
        performEncapsulate(typeName, fieldName, getterName, setterName, preConditionsWarningExpected, userInputWarningsExpected, -1, true, true);
    }

    private void performEncapsulate(String typeName, String fieldName, String getterName, String setterName, boolean preConditionsWarningExpected, boolean userInputWarningsExpected, int fieldVisibility) throws Exception {
        performEncapsulate(typeName, fieldName, getterName, setterName, preConditionsWarningExpected, userInputWarningsExpected, fieldVisibility, true, true);
    }

    private void performEncapsulate(String typeName, String fieldName, String getterName, String setterName, boolean preConditionsWarningExpected, boolean userInputWarningsExpected, int fieldVisibility, boolean read, boolean write) throws Exception {
        cat.info("Testing " + getStrippedTestName());
        final Project project = getMutableProject();
        BinTypeRef aRef = project.findTypeRefForName(typeName);
        BinField field = aRef.getBinCIType().getDeclaredField(fieldName);
        final EncapsulateField encapsulator = new EncapsulateField(new NullContext(project), field);
        RefactoringStatus status = encapsulator.checkPreconditions();
        assertTrue("Preconditions check status is ok: " + status.getAllMessages(), preConditionsWarningExpected ? !status.isErrorOrFatal() : status.isOk());
        encapsulator.setGetterName(getterName);
        encapsulator.setSetterName(setterName);
        encapsulator.setUsages(encapsulator.getAllUsages());
        encapsulator.setFieldVisibility(fieldVisibility);
        encapsulator.setEncapsulateRead(read);
        encapsulator.setEncapsulateWrite(write);
        status = encapsulator.checkUserInput();
        assertTrue("User input check status is ok: " + status.getAllMessages(), userInputWarningsExpected ? !status.isErrorOrFatal() : status.isOk());
        status = encapsulator.apply();
        assertTrue("Encapsulated successfully: " + status == null ? "" : status.getAllMessages(), status == null || status.isOk());
        RwRefactoringTestUtils.assertSameSources("Encapsulated field", getExpectedProject(), project);
        cat.info("SUCCESS");
    }

    public void testSimple() throws Exception {
        performEncapsulate("A", "test", "getTest", "setTest", false, false, BinModifier.PRIVATE);
    }

    public void testCustomAccessorName() throws Exception {
        performEncapsulate("A", "test", "get_test", "set_test", false, false, BinModifier.PACKAGE_PRIVATE);
    }

    public void testExpressions() throws Exception {
        performEncapsulate("A", "test", "getTest", "setTest", false, false, BinModifier.PRIVATE);
    }

    public void testStatic() throws Exception {
        performEncapsulate("A", "test", "getTest", "setTest", false, false);
    }

    public void testFinal() throws Exception {
        performEncapsulate("A", "test", "getTest", "setTest", false, false, -1, true, false);
    }

    public void testbug1527() throws Exception {
        performEncapsulate("A", "test", "getTest", "setTest", false, true, BinModifier.PRIVATE);
    }

    public void testMultipleAssignment() throws Exception {
        performEncapsulate("Test", "i", "getI", "setI", false, false);
    }
}
