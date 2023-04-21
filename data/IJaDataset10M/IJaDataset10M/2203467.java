package org.maveryx.report.reader;

/**
 * @author Alfonso Nocella
 */
public class TestHeader {

    private static String testName;

    private static String testID;

    private static String startTime;

    private static String owner;

    private static TestHeader instance = null;

    public static TestHeader getInstance() {
        if (instance == null) {
            instance = new TestHeader();
        }
        return instance;
    }

    private TestHeader() {
    }

    public void setTestName(String name) {
        testName = name;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestID(String id) {
        testID = id;
    }

    public String getTestID() {
        return testID;
    }

    public void setStartTime(String time) {
        startTime = time;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setOwner(String tester) {
        owner = tester;
    }

    public String gerOwner() {
        return owner;
    }
}
