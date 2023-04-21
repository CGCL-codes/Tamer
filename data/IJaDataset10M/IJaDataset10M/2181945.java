package ca.ucalgary.cpsc.ebe.fitClipse.ui.testResults.model;

import java.util.Date;
import java.util.LinkedList;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.model.WikiPageModel;

public class TestSuiteResult {

    private LinkedList<SingleTestResult> children = new LinkedList<SingleTestResult>();

    private ResultRoot parent = null;

    private String qName;

    private Date date;

    private String title;

    private long ID;

    private boolean suite;

    private String sName;

    public TestSuiteResult(ResultRoot parent, String qName, Date date, boolean suite) {
        this.parent = parent;
        this.qName = qName;
        this.date = date;
        this.suite = suite;
        if (suite) {
            sName = "[SUITE] ";
        } else {
            sName = "[TEST] ";
        }
        title = sName + qName + " " + date;
    }

    public TestSuiteResult(String name) {
        this.qName = name;
    }

    public String getQName() {
        return qName;
    }

    public void setQName(String qName) {
        this.qName = qName;
    }

    public ResultRoot getParent() {
        return parent;
    }

    public void setParent(ResultRoot parent) {
        this.parent = parent;
    }

    public LinkedList<SingleTestResult> getChildren() {
        return this.children;
    }

    public void addChild(SingleTestResult child) {
        children.add(child);
    }

    public void removeChild(SingleTestResult child) {
        children.remove(child);
    }

    public String getTitle() {
        return title;
    }

    public boolean isSuite() {
        return suite;
    }

    public void setSuiteID(long ID) {
        this.setID(ID);
        for (SingleTestResult child : children) {
            child.setSuiteID(ID);
        }
    }

    public long getID() {
        return ID;
    }

    public void setID(long id) {
        ID = id;
    }

    public void removeChildren() {
        children = null;
    }
}
