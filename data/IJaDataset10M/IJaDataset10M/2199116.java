package ca.ucalgary.cpsc.ebe.fitClipse.tests.fit;

import fit.ColumnFixture;

public class TestNewTestPageColumn extends ColumnFixture {

    public String testName;

    public String testContent;

    public String valid() {
        try {
            TestUtil.testQName = testName;
            TestUtil.testContent = testContent;
        } catch (Exception e) {
            return "false";
        }
        return "true";
    }
}
