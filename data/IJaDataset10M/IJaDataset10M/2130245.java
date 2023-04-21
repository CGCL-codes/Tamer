package ca.ucalgary.cpsc.ebe.fitClipse.testResults.history;

import java.util.List;
import aaftt.Test;
import aaftt.TestResult;
import aaftt.TestResultStorage;
import ca.ucalgary.cpsc.ebe.fitClipse.persistence.XMLPersistenceUtils;

/**
 * The Class FitClipseTestHistoryCreator.
 */
public class FitClipseTestHistoryCreator {

    /**
	 * Gets the test history page.
	 * 
	 * @param test
	 *            the test
	 * 
	 * @return the test history page
	 */
    public String getTestHistoryPage(Test test) {
        TestResultStorage testResultStorage = XMLPersistenceUtils.getCurrentTestResultStorage();
        List<TestResult> testResults = testResultStorage.get(test.getUniqueID());
        return generateHTMLContent(test, testResults);
    }

    /**
	 * Generate html content.
	 * 
	 * @param test
	 *            the test
	 * @param testResults
	 *            the test results
	 * 
	 * @return the string
	 */
    public String generateHTMLContent(Test test, List<TestResult> testResults) {
        StringBuffer content = new StringBuffer();
        System.out.println("inside graphPage: htmlUrl is:" + "htmlUrl");
        content.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.DTD\">\n<html>\n<head><title>This is the history for test: ").append(test.getUniqueID()).append(" </title>\n");
        content.append("<script type=\"javascript\">");
        content.append("funtion displayHTML(var string) {");
        content.append(" alert(string);");
        content.append("</script>");
        content.append("</head>\n<body>\n");
        content.append("<div>\n<h3>Test Name: ").append(test.getUniqueID()).append("</h3>\n");
        content.append("<h4>Test Result History Chart: </h4>\n");
        content.append("<img align \"center\" src=\"").append("chartUrl").append("\"/>\n");
        content.append("<br><br><br><br><table border=\"1\" cellpadding=\"5\" cellspacing=\"5\">\n<tr>\n");
        content.append("<th></th><th>Status</th><th>Date Run</th><th>Right</th><th>Wrong</th><th>Exceptions</th><th>Ignored</th><th>Details</th>\n</tr>");
        int i = 0;
        for (TestResult testResult : testResults) {
            String passFailLabel = "Pass";
            String passFailStyle = "background: rgb(77, 252, 1)";
            if (testResult.getNumWrong() > 0) {
                passFailStyle = "background: rgb(248, 10, 1)";
                passFailLabel = "Fail";
            } else if (testResult.getNumExceptions() > 0) {
                passFailStyle = "background: rgb(248, 253, 26)";
                passFailLabel = "Exception";
            } else if (testResult.getNumRight() > 0) {
                passFailStyle = "background: rgb(77, 252, 1)";
                passFailLabel = "Pass";
            }
            content.append("<tr>\n <td>").append(i).append("</td><td style=\"").append(passFailStyle).append("\">").append(passFailLabel).append("</td><td>").append(testResult.getEndTime()).append("</td><td>").append(testResult.getNumRight()).append("</td><td>").append(testResult.getNumWrong()).append("</td><td>").append(testResult.getNumExceptions()).append("</td><td>").append(testResult.getNumIgnored()).append("</td><td><FORM action=\"").append("testResult.getTestOutput()").append("\" method=\"post\">\n<INPUT type=\"hidden\" name=\"tid\" value=\"").append("test.getId()").append("\">\n").append("<INPUT type=\"submit\" name=\"testHtml\" value=\"Test HTML\" onClick=\"displayHTML('this is some html')\">\n</FORM>").append("</td>\n</tr>");
        }
        content.append("\n</table><br><br><br></div>\n</body>\n</html>");
        return content.toString();
    }
}
