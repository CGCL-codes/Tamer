package org.sergioveloso.spott.config;

/**
 * @author SMV
 * 
 * The mappings are used by the FrontController to 
 * determine what resource to forward based on the
 * return code (int) of the Command called.
 * 
 */
public class DispatchProperties {

    private static final int resourceCount = 30;

    public static final int RET_NOT_FOUND = 0;

    public static final int RET_LOGIN_PAGE = 1;

    public static final int RET_LAST_PAGE = 2;

    public static final int RET_DEFAULT = 3;

    public static final int RET_TEST = 4;

    public static final int RET_ARTIFACTS = 5;

    public static final int RET_ARTIFACT_DETAIL = 6;

    public static final int RET_MODULE_DETAIL = 7;

    public static final int RET_VERSION_DETAIL = 8;

    public static final int RET_PERSONS = 9;

    public static final int RET_PERSON_DETAIL = 10;

    public static final int RET_ARTIFACT_ADD = 11;

    public static final int RET_LOGGED_OUT_PAGE = 12;

    public static final int RET_AUTH_DENIED = 13;

    public static final int RET_TEST_PROJECT = 14;

    public static final int RET_TEST_PROJECTS = 15;

    public static final int RET_TEST_CASE = 16;

    public static final int RET_TEST_CASES = 17;

    public static final int RET_TEST_SUITE = 18;

    public static final int RET_TEST_SUITES = 19;

    public static final int RET_TSI_CATEGORIES = 20;

    public static final int RET_TSI_CATEGORY = 21;

    public static final int RET_TEST_RUN = 22;

    public static final int RET_TEST_RUNS = 23;

    public static final int RET_TESTER_OUTLOOK = 24;

    public static final int RET_TESTER_WORK_LIST = 25;

    public static final int RET_TEST_REGISTER = 26;

    public static final int RET_START_ANALYZING = 27;

    public static final int RET_ANALYZE_TESTRUN_LIST = 28;

    public static final int RET_ANALYZE_TESTRUN = 29;

    public static String[] resourceLocations = null;

    static {
        resourceLocations = new String[resourceCount];
        resourceLocations[RET_NOT_FOUND] = "";
        resourceLocations[RET_LOGIN_PAGE] = "";
        resourceLocations[RET_LOGGED_OUT_PAGE] = "/bye.html";
        resourceLocations[RET_LAST_PAGE] = "/view/";
        resourceLocations[RET_DEFAULT] = "/view/mainPage.jsp";
        resourceLocations[RET_TEST] = "/view/testPage.jsp";
        resourceLocations[RET_ARTIFACTS] = "/view/artifacts.jsp";
        resourceLocations[RET_ARTIFACT_DETAIL] = "/view/artifact.jsp";
        resourceLocations[RET_MODULE_DETAIL] = "/view/module.jsp";
        resourceLocations[RET_VERSION_DETAIL] = "/view/version.jsp";
        resourceLocations[RET_PERSONS] = "/view/persons.jsp";
        resourceLocations[RET_PERSON_DETAIL] = "/view/person.jsp";
        resourceLocations[RET_ARTIFACT_ADD] = "/view/artifactAdd.jsp";
        resourceLocations[RET_AUTH_DENIED] = "../authDenied.html";
        resourceLocations[RET_TEST_PROJECT] = "/view/testProject.jsp";
        resourceLocations[RET_TEST_PROJECTS] = "/view/testProjects.jsp";
        resourceLocations[RET_TEST_CASE] = "/view/testCase.jsp";
        resourceLocations[RET_TEST_CASES] = "/view/testCases.jsp";
        resourceLocations[RET_TEST_SUITE] = "/view/testSuite.jsp";
        resourceLocations[RET_TEST_SUITES] = "/view/testSuites.jsp";
        resourceLocations[RET_TSI_CATEGORIES] = "/view/tscategories.jsp";
        resourceLocations[RET_TSI_CATEGORY] = "/view/tscategory.jsp";
        resourceLocations[RET_TEST_RUN] = "/view/testRun.jsp";
        resourceLocations[RET_TEST_RUNS] = "/view/testRuns.jsp";
        resourceLocations[RET_TESTER_OUTLOOK] = "/view/testerOutlook.jsp";
        resourceLocations[RET_TESTER_WORK_LIST] = "/view/testerWorkList.jsp";
        resourceLocations[RET_TEST_REGISTER] = "/view/testRegister.jsp";
        resourceLocations[RET_START_ANALYZING] = "/view/startAnalyzing.jsp";
        resourceLocations[RET_ANALYZE_TESTRUN_LIST] = "/view/analyzing_testRunList.jsp";
        resourceLocations[RET_ANALYZE_TESTRUN] = "/view/analyzing_testRunDetail.jsp";
    }
}
