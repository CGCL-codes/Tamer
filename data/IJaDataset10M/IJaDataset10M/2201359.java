package com.plus.fcentre.jobfilter.web.controller;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import com.plus.fcentre.jobfilter.web.helper.MenuModelDataHelper;

/**
 * Test case abstract base class for form web MVC controller.
 * 
 * @author Steve Jefferson
 */
public abstract class FormControllerAbstractTest extends TestCase {

    private FormController controller;

    private MenuModelDataHelper menuModelDataHelper;

    private Map<String, Object> menuModelData;

    private int numNewVacancies;

    private int numPendingVacancies;

    /**
	 * Construct test case.
	 * 
	 * @param testCaseName test case name.
	 */
    public FormControllerAbstractTest(String testCaseName) {
        super(testCaseName);
    }

    /**
	 * Set up test fixture.
	 * 
	 * @param controller controller to test.
	 * @throws Exception if set-up failed for an unexpected reason.
	 */
    protected void setUp(FormController controller) throws Exception {
        super.setUp();
        this.controller = controller;
        menuModelDataHelper = createMock(MenuModelDataHelper.class);
        controller.setMenuModelDataHelper(menuModelDataHelper);
        menuModelData = new HashMap<String, Object>();
        numNewVacancies = 4;
        menuModelData.put("numNewVacancies", numNewVacancies);
        numPendingVacancies = 7;
        menuModelData.put("numPendingVacancies", numPendingVacancies);
    }

    @Override
    protected void tearDown() throws Exception {
        verify(menuModelDataHelper);
        super.tearDown();
    }

    /**
	 * End mock programming phase.
	 */
    void endMockProgramming() {
        replay(menuModelDataHelper);
    }

    /**
	 * Test menuModelDataHelper property.
	 */
    public final void testMenuModelDataHelper() {
        endMockProgramming();
        assertSame(controller.getMenuModelDataHelper(), menuModelDataHelper);
    }

    /**
	 * Program mocks with expectations for retieving common reference data.
	 */
    protected void programCommonRefDataExpectations() {
        expect(menuModelDataHelper.getModelData()).andReturn(menuModelData);
    }

    /**
	 * Validate provided model as containing entries from common reference data
	 * programmed in above expectations.
	 * 
	 * @param refData reference data to validate.
	 */
    protected void validateCommonRefData(Map<?, ?> refData) {
        assertEquals(refData.get("numNewVacancies"), new Integer(numNewVacancies));
        assertEquals(refData.get("numPendingVacancies"), new Integer(numPendingVacancies));
    }

    /**
	 * Retrieve number of common reference data elements to expect in a model.
	 * 
	 * @return number of model data elements.
	 */
    protected int getNumCommonRefDataElements() {
        return 2;
    }
}
