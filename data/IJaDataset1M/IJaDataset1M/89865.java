package tests.com.ivis.xprocess.abbot.suites;

import junit.framework.Test;
import junit.framework.TestSuite;
import tests.com.ivis.xprocess.abbot.TestReset;
import tests.com.ivis.xprocess.abbot.update.TestScheduledOrder;
import tests.com.ivis.xprocess.abbot.update.TestUpdateDefaultHoursPerDay;
import tests.com.ivis.xprocess.abbot.update.TestUpdateFolder;
import tests.com.ivis.xprocess.abbot.update.TestUpdateFormType;
import tests.com.ivis.xprocess.abbot.update.TestUpdateGatewayType;
import tests.com.ivis.xprocess.abbot.update.TestUpdateOrganizationAvailability;
import tests.com.ivis.xprocess.abbot.update.TestUpdateOrganizationPriceRecord;
import tests.com.ivis.xprocess.abbot.update.TestUpdateOverheadTask;
import tests.com.ivis.xprocess.abbot.update.TestUpdatePersonAvailability;
import tests.com.ivis.xprocess.abbot.update.TestUpdatePortfolio;
import tests.com.ivis.xprocess.abbot.update.TestUpdatePrioritizedList;
import tests.com.ivis.xprocess.abbot.update.TestUpdateProcess;
import tests.com.ivis.xprocess.abbot.update.TestUpdateProject;
import tests.com.ivis.xprocess.abbot.update.TestUpdateProjectAvailability;
import tests.com.ivis.xprocess.abbot.update.TestUpdateRoleType;
import tests.com.ivis.xprocess.abbot.update.TestUpdateTask;
import tests.com.ivis.xprocess.abbot.update.TestUpdateTaskFolder;
import tests.com.ivis.xprocess.abbot.update.TestUpdateUIAction;

public class UpdateElementSuite {

    public static Test suite() {
        TestSuite result = new TestSuite("Update Element Test Suite");
        result.addTestSuite(TestReset.class);
        result.addTestSuite(TestUpdateOverheadTask.class);
        result.addTestSuite(TestUpdateGatewayType.class);
        result.addTestSuite(TestUpdateRoleType.class);
        result.addTestSuite(TestUpdateFormType.class);
        result.addTestSuite(TestUpdateUIAction.class);
        result.addTestSuite(TestReset.class);
        result.addTestSuite(TestUpdateProjectAvailability.class);
        result.addTestSuite(TestUpdatePersonAvailability.class);
        result.addTestSuite(TestUpdateOrganizationAvailability.class);
        result.addTestSuite(TestUpdateTask.class);
        result.addTestSuite(TestReset.class);
        result.addTestSuite(TestUpdateTaskFolder.class);
        result.addTestSuite(TestUpdateProject.class);
        result.addTestSuite(TestUpdateProcess.class);
        result.addTestSuite(TestUpdateFolder.class);
        result.addTestSuite(TestUpdatePrioritizedList.class);
        result.addTestSuite(TestReset.class);
        result.addTestSuite(TestUpdatePortfolio.class);
        result.addTestSuite(TestUpdateDefaultHoursPerDay.class);
        result.addTestSuite(TestUpdateOrganizationPriceRecord.class);
        result.addTestSuite(TestScheduledOrder.class);
        return result;
    }
}
