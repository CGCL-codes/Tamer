package tests.com.ivis.xprocess.abbot.suites;

import junit.framework.Test;
import junit.framework.TestSuite;
import tests.com.ivis.xprocess.abbot.TestReset;
import tests.com.ivis.xprocess.abbot.explorerview.TestAddRemoveFromPlan;
import tests.com.ivis.xprocess.abbot.explorerview.TestCloseUncloseTask;
import tests.com.ivis.xprocess.abbot.explorerview.TestCompositeIcons;
import tests.com.ivis.xprocess.abbot.explorerview.TestContextMenus;
import tests.com.ivis.xprocess.abbot.explorerview.TestDefaultElementsNonDeleteable;
import tests.com.ivis.xprocess.abbot.explorerview.TestExplorerFilter;
import tests.com.ivis.xprocess.abbot.explorerview.TestIconUpdate;
import tests.com.ivis.xprocess.abbot.explorerview.TestMakeActiveInActive;
import tests.com.ivis.xprocess.abbot.explorerview.TestOrderOfAppearanceInExplorerView;
import tests.com.ivis.xprocess.abbot.explorerview.TestPrioritization;
import tests.com.ivis.xprocess.abbot.explorerview.TestShowInBurndown;
import tests.com.ivis.xprocess.abbot.explorerview.TestShowInPersonalPlanner;
import tests.com.ivis.xprocess.abbot.explorerview.TestUniqueWrappers;
import tests.com.ivis.xprocess.abbot.explorerview.TestUsingUIAction;

public class ExplorerViewSuite {

    public static Test suite() {
        TestSuite result = new TestSuite("Explorer View Suite");
        result.addTestSuite(TestReset.class);
        result.addTestSuite(TestContextMenus.class);
        result.addTestSuite(TestDefaultElementsNonDeleteable.class);
        result.addTestSuite(TestOrderOfAppearanceInExplorerView.class);
        result.addTestSuite(TestUsingUIAction.class);
        result.addTestSuite(TestReset.class);
        result.addTestSuite(TestShowInPersonalPlanner.class);
        result.addTestSuite(TestIconUpdate.class);
        result.addTestSuite(TestShowInBurndown.class);
        result.addTestSuite(TestMakeActiveInActive.class);
        result.addTestSuite(TestAddRemoveFromPlan.class);
        result.addTestSuite(TestCloseUncloseTask.class);
        result.addTestSuite(TestPrioritization.class);
        result.addTestSuite(TestExplorerFilter.class);
        result.addTestSuite(TestUniqueWrappers.class);
        result.addTestSuite(TestCompositeIcons.class);
        return result;
    }
}
