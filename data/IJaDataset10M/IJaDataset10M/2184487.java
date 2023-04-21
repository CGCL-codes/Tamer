package net.firstpartners.sample.SpreadsheetServlet;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.firstpartners.drools.FileRuleLoader;
import net.firstpartners.drools.SpreadSheetRuleRunner;
import net.firstpartners.drools.data.RuleSource;
import net.firstpartners.spreadsheet.SpreadSheetOutputter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class SpreadsheetServletSample extends HttpServlet {

    private static final Logger log = Logger.getLogger(SpreadsheetServletSample.class.getName());

    public static final String EXCEL_DATA_FILE = "http://red-piranha.appspot.com/sampleresources/SpreadSheetServlet/chocolate-data.xls";

    public static final String[] RULES_FILES = new String[] { "http://red-piranha.appspot.com/sampleresources/SpreadSheetServlet/log-then-modify-rules.drl" };

    public static final String KNOWLEDGE_BASE_FILE = "http://red-piranha.appspot.com/sampleresources/SpreadSheetServlet/log-then-modify-rules.KnowledgeBase";

    private static final String EXCEL_LOG_WORKSHEET_NAME = "log";

    private final SpreadSheetRuleRunner commonSpreadsheetUtils = new SpreadSheetRuleRunner(new FileRuleLoader());

    private void logEnvironment() {
        StringBuffer output = new StringBuffer();
        Properties pr = System.getProperties();
        TreeSet propKeys = new TreeSet(pr.keySet());
        for (Iterator it = propKeys.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            output.append("" + key + "=" + pr.get(key) + "\n");
        }
        log.info(output.toString());
    }

    /**
	 * Standard Servlet Entry Point
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        logEnvironment();
        RuleSource ruleSource = new RuleSource();
        ruleSource.setKnowledgeBaseLocation(KNOWLEDGE_BASE_FILE);
        if (user != null) {
            resp.setContentType("application/vnd.ms-excel");
            resp.setHeader("Content-Disposition", "attachment; filename=result.xls");
            URL url = new URL(EXCEL_DATA_FILE);
            HSSFWorkbook wb;
            try {
                wb = commonSpreadsheetUtils.callRules(url, ruleSource, getExcelLogWorksheetName());
                SpreadSheetOutputter.outputToStream(wb, resp.getOutputStream());
            } catch (Exception e) {
                log.warning(e.getMessage());
                throw new ServletException(e);
            }
        } else {
            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }
    }

    public static String getExcelLogWorksheetName() {
        return EXCEL_LOG_WORKSHEET_NAME;
    }
}
