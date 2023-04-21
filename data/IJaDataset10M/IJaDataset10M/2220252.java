package org.sergioveloso.spott.command.testsuiteitem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import org.sergioveloso.spott.command.Command;
import org.sergioveloso.spott.config.DispatchProperties;
import org.sergioveloso.spott.exception.DAOException;
import org.sergioveloso.spott.model.entity.TestCase;
import org.sergioveloso.spott.model.entity.TestSuite;
import org.sergioveloso.spott.model.db.HibernateUtil;
import org.hibernate.Query;

public class ListTestCasesCommand extends Command {

    @Override
    @SuppressWarnings("unchecked")
    public int execute(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        logRequestParameters(req);
        String theAction = req.getParameter("theaction");
        List<TestCase> testCaseList = null;
        if ("manage".equals(theAction)) {
            log("Getting list of TestCases for display...");
            try {
                Query q = HibernateUtil.getSession().createQuery("from TestCase order by itemAlias");
                testCaseList = q.list();
            } catch (Throwable t) {
                throw new ServletException("ERROR", t);
            }
        } else if ("choose".equals(theAction)) {
            log("Getting list of TestCases for choosing...");
            try {
                Long testSuiteId = new Long(req.getParameter("testSuiteID"));
                TestSuite testSuite = (TestSuite) HibernateUtil.getSession().get(TestSuite.class, testSuiteId);
                try {
                    Query q = HibernateUtil.getSession().createQuery("from TestCase order by itemAlias");
                    testCaseList = q.list();
                    testCaseList.removeAll(testSuite.getItems());
                } catch (Throwable t) {
                    throw new ServletException("ERROR", t);
                }
                req.setAttribute("testSuite", testSuite);
            } catch (NumberFormatException nfe) {
                throw new ServletException("ListTestCasesCommand got NumberFormatException: " + nfe.getMessage(), nfe);
            } catch (DAOException de) {
                throw new ServletException("ListTestCasesCommand got DAOException: " + de.getMessage(), de);
            }
        }
        req.setAttribute("theaction", theAction);
        req.setAttribute("testCaseList", testCaseList);
        return DispatchProperties.RET_TEST_CASES;
    }
}
