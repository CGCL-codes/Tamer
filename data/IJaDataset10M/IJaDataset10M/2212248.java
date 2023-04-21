package fitnesse.responders.fitClipse;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import fitnesse.FitNesseContext;
import fitnesse.authentication.SecureOperation;
import fitnesse.authentication.SecureReadOperation;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.http.SimpleResponse;
import fitnesse.responders.SecureResponder;
import fitnesse.wiki.PageCrawler;
import fitnesse.wiki.PathParser;
import fitnesse.wiki.WikiPage;
import fitnesse.wiki.WikiPagePath;

public class FitClipseTestResultHistoryGraphPageResponder implements SecureResponder {

    String regex = "FitClipse.ProjectS(.+)\\.";

    public SecureOperation getSecureOperation() {
        return new SecureReadOperation();
    }

    @SuppressWarnings("unchecked")
    public Response makeResponse(FitNesseContext context, Request request) throws Exception {
        String quilifiedName = request.getResource();
        String chartUrl = request.getRequestUri().substring(0, request.getRequestUri().lastIndexOf("Page"));
        WikiPage root = context.root;
        WikiPagePath path = PathParser.parse(quilifiedName);
        PageCrawler crawler = root.getPageCrawler();
        WikiPage currentPage = crawler.getPage(root, path);
        String pageQName = getPageQName(root, currentPage);
        FitClipseGraphProvider imageProvider = new FitClipseGraphProvider();
        List<FitTestEntity> result = imageProvider.getTestHistoryForPage(pageQName);
        String content = createContent(currentPage.getName(), pageQName, chartUrl, result);
        SimpleResponse response = new SimpleResponse();
        response.setContent(content);
        return response;
    }

    private String createContent(String pageName, String pageQName, String chartUrl, List result) {
        StringBuffer content = new StringBuffer();
        String htmlUrl = getHtmlUrl(chartUrl);
        System.out.println("inside graphPage: htmlUrl is:" + htmlUrl);
        content.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.DTD\">\n<html>\n<head><title>This is the history for test: ").append(pageName).append(" </title>\n</head>\n<body>\n");
        content.append("<div>\n<h3>Test Name: ").append(pageName).append("</h3>\n");
        content.append("<h4>Test Result History Chart: </h4>\n");
        content.append("<img align \"center\" src=\"").append(chartUrl).append("\"/>\n");
        content.append("<br><br><br><br><table border=\"1\" cellpadding=\"5\" cellspacing=\"5\">\n<tr>\n");
        content.append("<th></th><th>Status</th><th>Date Run</th><th>Right</th><th>Wrong</th><th>Exceptions</th><th>Ignored</th><th>Details</th>\n</tr>");
        int i = 0;
        for (int j = 0; j < result.size(); j++) {
            FitTestEntity test = (FitTestEntity) result.get(j);
            i++;
            String passFailLabel = "Pass";
            String passFailStyle = "background: rgb(77, 252, 1)";
            if (test.getNumWrong() > 0) {
                passFailStyle = "background: rgb(248, 10, 1)";
                passFailLabel = "Fail";
            } else if (test.getNumExceptions() > 0) {
                passFailStyle = "background: rgb(248, 253, 26)";
                passFailLabel = "Exception";
            } else if (test.getNumRight() > 0) {
                passFailStyle = "background: rgb(77, 252, 1)";
                passFailLabel = "Pass";
            }
            content.append("<tr>\n <td>").append(i).append("</td><td style=\"").append(passFailStyle).append("\">").append(passFailLabel).append("</td><td>").append(test.getEndTime()).append("</td><td>").append(test.getNumRight()).append("</td><td>").append(test.getNumWrong()).append("</td><td>").append(test.getNumExceptions()).append("</td><td>").append(test.getNumIgnored()).append("</td><td><FORM action=\"").append(htmlUrl).append("\" method=\"post\">\n<INPUT type=\"hidden\" name=\"tid\" value=\"").append(test.getId()).append("\">\n").append("<INPUT type=\"submit\" name=\"testHtml\" value=\"Test HTML\">\n</FORM>").append("</td>\n</tr>");
        }
        content.append("\n</table><br><br><br></div>\n</body>\n</html>");
        return content.toString();
    }

    private String getHtmlUrl(String chartUrl) {
        int index = chartUrl.lastIndexOf("Chart");
        String htmlUrl = chartUrl.substring(0, index);
        htmlUrl = htmlUrl + "Html";
        return htmlUrl;
    }

    private String getPageQName(WikiPage root, WikiPage currentPage) throws Exception {
        StringBuffer contentBuffer = new StringBuffer();
        while (!currentPage.equals(root)) {
            contentBuffer.insert(0, currentPage.getName() + ".");
            currentPage = currentPage.getParent();
        }
        System.out.println("inside result history: full name is: " + contentBuffer.toString());
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(contentBuffer.toString());
        String pageQName = null;
        if (match.find()) {
            pageQName = match.group(1);
        }
        return pageQName;
    }
}
