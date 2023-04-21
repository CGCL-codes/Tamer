package org.sergioveloso.spott.command.testrun;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Calendar;
import org.sergioveloso.spott.model.db.*;
import org.sergioveloso.spott.model.entity.*;
import org.sergioveloso.spott.command.Command;
import org.sergioveloso.spott.config.DispatchProperties;

public class AddUpdateTestRegisterCommand extends Command {

    @Override
    public int execute(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        logRequestParameters(req);
        try {
            String theAction = req.getParameter("theaction");
            Long testRunId = new Long(req.getParameter("testRunID"));
            Long testSuiteItemId = new Long(req.getParameter("testSuiteItemID"));
            Person tester = helper().getCurrentUser(req);
            Date rightNow = Calendar.getInstance().getTime();
            boolean passed = ("passed".equals(req.getParameter("testRegisterPassed")));
            boolean untestable = ("untestable".equals(req.getParameter("testRegisterUntestable")));
            String comments = req.getParameter("testRegisterComment");
            float effort = Float.parseFloat(req.getParameter("testRegisterEffort"));
            if ("add".equals(theAction)) {
                HibernateUtil.beginTransaction();
                TestRun testRun = (TestRun) HibernateUtil.getSession().get(TestRun.class, testRunId);
                TestSuiteItem testSuiteItem = testRun.getTestProject().getTestSuite().getItemById(testSuiteItemId);
                TestRegister reg = new TestRegister(tester, testSuiteItem, rightNow, true, passed);
                reg.setTestRun(testRun);
                reg.setEffort(effort);
                reg.setLastUpdateDate(rightNow);
                reg.setUntestable(untestable);
                if (null != comments) {
                    reg.setComments(comments);
                }
                testRun.getRegisters().add(reg);
                HibernateUtil.getSession().saveOrUpdate(testRun);
                return new TesterWorkCommand().execute(req, res);
            } else if ("update".equals(theAction)) {
                HibernateUtil.beginTransaction();
                TestRun testRun = (TestRun) HibernateUtil.getSession().get(TestRun.class, testRunId);
                Long testRegisterId = new Long(req.getParameter("testRegisterID"));
                TestRegister reg = testRun.getRegisterById(testRegisterId);
                reg.setPassed(passed);
                reg.setEffort(effort);
                reg.setComments(comments);
                reg.setLastUpdateDate(rightNow);
                reg.setUntestable(untestable);
                HibernateUtil.getSession().saveOrUpdate(testRun);
                req.setAttribute("warningMessage", "Register has been updated.");
                return new TesterWorkCommand().execute(req, res);
            } else {
                return DispatchProperties.RET_TESTER_WORK_LIST;
            }
        } catch (Throwable t) {
            log("ERROR:" + t.getMessage());
            throw new ServletException("ERROR", t);
        }
    }
}
