package com.sri.emo.wizard.selection;

import com.jcorporate.expresso.services.test.TestSystemInitializer;
import com.sri.emo.test.DatabaseTestFixture;
import com.sri.emo.test.EmoTestSuite;
import com.sri.emo.wizard.Wizard;
import com.sri.emo.wizard.WizardException;
import com.sri.emo.wizard.WizardPage;
import com.sri.emo.wizard.defaults.SequentialWizard;
import com.sri.emo.wizard.defaults.WizardTestModel;
import junit.framework.TestCase;
import com.jcorporate.expresso.core.security.SuperUser;
import com.jcorporate.expresso.core.registry.MutableRequestRegistry;

public class TestSelectionWizardFactory extends TestCase {

    int wizardId = 0;

    private EmoSelectionWizardFactory emoWizardFactory = null;

    private DatabaseTestFixture databaseTestFixture;

    private WizardTestModel testModel = null;

    public TestSelectionWizardFactory(String name) {
        super(name);
        try {
            TestSystemInitializer.setUp();
            new MutableRequestRegistry(TestSystemInitializer.getTestContext(), SuperUser.INSTANCE);
        } catch (Exception ex) {
            throw new RuntimeException("Error setting things up", ex);
        }
    }

    public void testBuildWizard() throws WizardException {
        Wizard wiz = emoWizardFactory.buildWizard();
        assertEquals(SequentialWizard.class, wiz.getClass());
        assertEquals(WizardTestModel.WIZ_TITLE + 0, wiz.getTitle());
        assertEquals(WizardTestModel.WIZ_SUMMARY, wiz.getSummary());
    }

    public void testBeginPage() throws WizardException {
        Wizard wiz = emoWizardFactory.buildWizard();
        WizardPage page = wiz.begin();
        assertTrue(page != null);
        assertEquals(page.getMetadata().getTitle(), WizardTestModel.WIZ_PAGES[0][WizardTestModel.INDEX_TITLE]);
        assertEquals(page.getMetadata().getDirective(), WizardTestModel.WIZ_PAGES[0][WizardTestModel.INDEX_DIRECTIVE]);
        assertEquals(page.getMetadata().getHelpText(), WizardTestModel.WIZ_PAGES[0][WizardTestModel.INDEX_HELPTEXT]);
    }

    private static final int SINGLE_STEP_SELECTION_WIZARD_ID = 14;

    public void testBuildSingleStepWizard() throws Exception {
        databaseTestFixture.setUp();
        emoWizardFactory = new EmoSelectionWizardFactory();
        emoWizardFactory.setWizardId(SINGLE_STEP_SELECTION_WIZARD_ID);
        Wizard wiz = emoWizardFactory.buildWizard();
        WizardPage page = wiz.begin();
        assertTrue(page != null);
        assertNull(page.getMetadata().getPrevious());
    }

    protected void setUp() throws Exception {
        super.setUp();
        testModel = new WizardTestModel(TestSystemInitializer.getTestContext());
        wizardId = testModel.intialize();
        emoWizardFactory = new EmoSelectionWizardFactory();
        emoWizardFactory.setWizardId(wizardId);
        databaseTestFixture = new DatabaseTestFixture(TestSystemInitializer.getTestContext(), EmoTestSuite.class.getResourceAsStream("WizardTestData.xml"));
    }

    protected void tearDown() throws Exception {
        emoWizardFactory = null;
        testModel.destroy();
        testModel = null;
        wizardId = 0;
        super.tearDown();
        databaseTestFixture.tearDown();
        databaseTestFixture = null;
    }
}
