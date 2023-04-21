package com.eastidea.qaforum.home;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import com.eastidea.qaforum.entity.ReportExecution;
import com.eastidea.qaforum.entity.TestCase;
import com.eastidea.qaforum.entity.TestRound;

@Name("reportExecutionHome")
public class ReportExecutionHome extends EntityHome<ReportExecution> {

    private static final long serialVersionUID = -53090833050887171L;

    @In(create = true)
    TestCaseHome testCaseHome;

    @In(create = true)
    TestRoundHome testRoundHome;

    public void setReportExecutionId(Long id) {
        setId(id);
    }

    public Long getReportExecutionId() {
        return (Long) getId();
    }

    @Override
    protected ReportExecution createInstance() {
        ReportExecution reportExecution = new ReportExecution();
        return reportExecution;
    }

    public void wire() {
        getInstance();
        TestCase testCase = testCaseHome.getDefinedInstance();
        if (testCase != null) {
            getInstance().setTestCase(testCase);
        }
        TestRound testRound = testRoundHome.getDefinedInstance();
        if (testRound != null) {
            getInstance().setTestRound(testRound);
        }
    }

    public boolean isWired() {
        return true;
    }

    public ReportExecution getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }
}
