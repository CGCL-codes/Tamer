package com.perfmon.report;

import java.io.File;
import junit.framework.TestCase;
import com.perfmon.callstack.CumulativCall;
import com.perfmon.callstack.Layer;
import com.perfmon.callstack.MethodCall;
import com.perfmon.callstack.Workflow;
import com.perfmon.utils.PerfUtils;

public class WorkflowFormatterTest extends TestCase {

    public void testWriteWorkflow() throws Exception {
        File configFileLoc = PerfUtils.loadFileFromClasspath("workflow-def.properties");
        Workflow workflow = getWorkflow();
        workflow.getCumulativeMetrics().add(new CumulativCall("Cumulative Test", workflow.getCompletedWorkflowItems().get(0), workflow.getCompletedWorkflowItems().get(1), 40));
        final ExcelWriter writer = new ExcelWriter(configFileLoc.getParentFile(), workflow);
        final WorkflowFormatter formatter = new WorkflowFormatter(writer, workflow, 150, 3);
        formatter.write();
    }

    private Workflow getWorkflow() {
        final Workflow wf = new Workflow("Test-WF", "xx", "yy");
        addMethod(wf, "testMethod1", "com.test.Test1", 10);
        addMethod(wf, "testMethod2", "com.test.Test2", 300);
        addMethod(wf, "testMethod2", "com.test.Test2", 30);
        addMethod(wf, "testMethod2", "com.test.Test2", 200);
        addMethod(wf, "testMethod3", "com.test.Test3", 20);
        addMethod(wf, "testMethod3", "com.test.Test3", 40);
        return wf;
    }

    private void addMethod(final Workflow wf, String method, String clazz, long time) {
        MethodCall methodCall = new MethodCall(Layer.AS, method, clazz, 0L);
        methodCall.setEndTime(time);
        wf.getCompletedWorkflowItems().add(methodCall);
    }
}
