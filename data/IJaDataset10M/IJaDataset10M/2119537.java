package org.dant.ant.extension.tasks;

import java.util.*;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.dant.ant.extension.tasks.internal.RemoteResponse;

public class TestCase extends TestSuite {

    protected List nestedTasks = new LinkedList();

    protected List testsResutlsList = new LinkedList();

    protected TestSuite parent;

    protected String parentName;

    private int allCount;

    private int successCount;

    private int failCount;

    public void setOwningTestSuite(TestSuite testsuite) {
        this.parent = testsuite;
        this.resultdir = this.parent.getResultdir();
        this.printSummary = this.parent.getPrintsummary();
    }

    public TestSuite getOwningTestSuite() {
        return this.parent;
    }

    public String getName() {
        if (this.name == null) {
            return "default test case";
        }
        return this.name;
    }

    public List getTestsResultsList() {
        return testsResutlsList;
    }

    public void addTask(Task nestedTask) {
        nestedTasks.add(nestedTask);
    }

    public void execute() throws BuildException {
        System.out.println("Running TestCase: " + this.name);
        for (Iterator iter = nestedTasks.iterator(); iter.hasNext(); ) {
            Task task = (Task) iter.next();
            if (task instanceof RemoteTask) {
                RemoteTask remoteTask = (RemoteTask) task;
                remoteTask.setOwningTestCase(this);
                remoteTask.perform();
                exportResult(remoteTask.getResultsList());
                testsResutlsList.add(remoteTask.getResultsList());
            } else {
                task.perform();
            }
        }
        if (printSummary) {
            summarize();
            printSummary();
        }
    }

    protected void exportResult(List results) {
        boolean multisited = results.size() > 1;
        for (int i = 0; i < results.size(); i++) {
            RemoteResponse result = (RemoteResponse) results.get(i);
            result.setName(this.name);
            result.setPackageName(this.parent.getName());
            result.setTagType(RemoteResponse.TASK);
            result.setMultiSited(multisited);
            if (!result.isSuccess()) {
                this.isSuccess = false;
            }
            this.exportResult(result);
        }
    }

    public void summarize() {
        for (Iterator iter = testsResutlsList.iterator(); iter.hasNext(); ) {
            List resultsList = (List) iter.next();
            for (Iterator resl = resultsList.iterator(); resl.hasNext(); ) {
                RemoteResponse results = (RemoteResponse) resl.next();
                allCount++;
                if (results.isSuccess()) {
                    successCount++;
                } else {
                    failCount++;
                }
            }
        }
    }

    public void printSummary() {
        System.out.println("Tests run: " + allCount + " , Failures: " + failCount + " , Success: " + successCount);
    }
}
