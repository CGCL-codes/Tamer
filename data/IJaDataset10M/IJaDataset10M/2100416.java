package com.plus.fcentre.jobfilter.web.helper.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import com.plus.fcentre.jobfilter.domain.Agency;
import com.plus.fcentre.jobfilter.domain.Agent;
import com.plus.fcentre.jobfilter.domain.NewVacancy;
import com.plus.fcentre.jobfilter.domain.PendingVacancy;
import com.plus.fcentre.jobfilter.domain.TestDataHelper;
import com.plus.fcentre.jobfilter.service.VacancyManager;

/**
 * Test case for {@link MenuModelDataHelperImpl}.
 * 
 * @author Steve Jefferson
 */
public class MenuModelDataHelperImplTest extends TestCase {

    private MenuModelDataHelperImpl helper;

    private VacancyManager vacancyManager;

    private List<NewVacancy> newVacancies;

    private List<PendingVacancy> pendingVacancies;

    /**
	 * Construct test case.
	 * 
	 * @param testCaseName test case name.
	 */
    public MenuModelDataHelperImplTest(String testCaseName) {
        super(testCaseName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        helper = new MenuModelDataHelperImpl();
        vacancyManager = createMock(VacancyManager.class);
        helper.setVacancyManager(vacancyManager);
        Agency agency = TestDataHelper.createAgency1();
        Agent agent = TestDataHelper.createAgent1(agency);
        NewVacancy newVacancy1 = TestDataHelper.createNewVacancy1(agent);
        NewVacancy newVacancy2 = TestDataHelper.createNewVacancy2(agent);
        newVacancies = new ArrayList<NewVacancy>();
        newVacancies.add(newVacancy1);
        newVacancies.add(newVacancy2);
        NewVacancy tmpNewVacancy = TestDataHelper.createNewVacancy3(agent);
        PendingVacancy pendingVacancy = tmpNewVacancy.makePending();
        pendingVacancies = new ArrayList<PendingVacancy>();
        pendingVacancies.add(pendingVacancy);
    }

    @Override
    protected void tearDown() throws Exception {
        verify(vacancyManager);
        super.tearDown();
    }

    /**
	 * Signal end of mock programming.
	 */
    private void endMockProgramming() {
        replay(vacancyManager);
    }

    /**
	 * Test vacancy manager property.
	 */
    public final void testVacancyManager() {
        endMockProgramming();
        assertEquals(vacancyManager, helper.getVacancyManager());
    }

    /**
	 * Test model data population.
	 */
    public final void testPopulateModel() {
        expect(vacancyManager.findAllNewVacancies()).andReturn(newVacancies);
        expect(vacancyManager.findAllPendingVacancies()).andReturn(pendingVacancies);
        endMockProgramming();
        Map<String, Object> model = helper.getModelData();
        assertEquals(model.size(), 2);
        assertEquals(model.get("numNewVacancies"), newVacancies.size());
        assertEquals(model.get("numPendingVacancies"), pendingVacancies.size());
    }
}
