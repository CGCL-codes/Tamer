package com.luxoft.fitpro.adapters.html;

import java.util.Date;
import com.luxoft.fitpro.adapters.messages.AdaptersMessages;
import com.luxoft.fitpro.core.testresult.Constants;
import fit.Counts;

public class HtmlPrinter {

    private Constants constants = new Constants();

    public String createSingleIndexTable(String fileName, String countsCell, String id) {
        StringBuffer singleIndexTable = new StringBuffer();
        singleIndexTable.append("<table><tr><td></td><td>");
        singleIndexTable.append(createLink(fileName, id));
        singleIndexTable.append("</td>");
        singleIndexTable.append(countsCell);
        singleIndexTable.append("</tr></table>");
        return singleIndexTable.toString();
    }

    public String closeCollapsableBlock() {
        StringBuffer collpsableFrame = new StringBuffer();
        collpsableFrame.append("</ul>");
        collpsableFrame.append("</div></div></div>");
        String res = collpsableFrame.toString();
        return res;
    }

    public String startExpandedCollapsableBlock(String testName, String collapsableId) {
        return startCollapsableBlock(testName, collapsableId, constants.getCollapsableOpenImagePath(), "collapsable");
    }

    private String startCollapsableBlock(String testName, String collapsableId, String imagePath, String visibility) {
        StringBuffer collpsableFrame = new StringBuffer();
        collpsableFrame.append("<div class=\"main\">");
        collpsableFrame.append("<div class=\"setup\">");
        collpsableFrame.append("<a href=\"javascript:toggleCollapsable('-" + collapsableId + "');\">");
        collpsableFrame.append("<img src=\"" + imagePath + "\" class=\"left\" id=\"img-" + collapsableId + "\"/>");
        collpsableFrame.append("</a>");
        collpsableFrame.append("<i>" + testName + "</i>");
        collpsableFrame.append("<div class=\"" + visibility + "\" id=\"-" + collapsableId + "\">");
        collpsableFrame.append("<ul>");
        return collpsableFrame.toString();
    }

    public String startCollapsedCollapsableBlock(String testName, String collapsableId) {
        return startCollapsableBlock(testName, collapsableId, constants.getCollapsableClosedImagePath(), AdaptersMessages.getMessage("fit.runner.hidden"));
    }

    public String createCountsCell(Counts counts) {
        return colour("td", counts) + counts + "</td>";
    }

    String colour(String tag, Counts counts) {
        if (counts.wrong > 0) {
            return colorTag(tag, AdaptersMessages.getMessage("fit.runner.fail"));
        } else if (counts.exceptions > 0) {
            return colorTag(tag, AdaptersMessages.getMessage("fit.runner.error"));
        } else if (counts.right > 0) {
            return colorTag(tag, AdaptersMessages.getMessage("fit.runner.pass"));
        } else {
            return colorTag(tag, AdaptersMessages.getMessage("fit.runner.ignore"));
        }
    }

    String colorTag(String tag, String color) {
        return "<" + tag + " class=\"" + color + "\"" + ">";
    }

    public String createLink(String linkName, String id) {
        return "<a href=\"#" + id + "\">" + linkName + "</a>";
    }

    public final String createCollapsableJs() {
        StringBuffer javaScript = new StringBuffer();
        javaScript.append("<script language=\"JavaScript\" type=\"text/javascript\">\n");
        javaScript.append("var collapsableOpenCss = \"collapsable\"\n");
        javaScript.append("var collapsableClosedCss = \"hidden\"\n");
        javaScript.append("var collapsableOpenImg = \"" + constants.getCollapsableOpenImagePath() + "\"\n");
        javaScript.append("var collapsableClosedImg = \"" + constants.getCollapsableClosedImagePath() + "\"\n");
        javaScript.append("function toggleCollapsable(id){\n");
        javaScript.append("var div = document.getElementById(id);\n");
        javaScript.append("var img = document.getElementById(\"img\" + id);\n");
        javaScript.append("if(div.className.indexOf(collapsableClosedCss) != -1) {\n");
        javaScript.append("div.className = collapsableOpenCss;\n");
        javaScript.append("img.src = collapsableOpenImg;\n");
        javaScript.append("} else {\n");
        javaScript.append("div.className = collapsableClosedCss;\n");
        javaScript.append("img.src = collapsableClosedImg;\n");
        javaScript.append("} }\n");
        javaScript.append("</script>\n");
        return javaScript.toString();
    }

    private String createAnchorTag(String name, String style, String testTitle, String id) {
        StringBuffer resultBuffer = new StringBuffer();
        resultBuffer.append("<div class=\"" + style + "\">");
        resultBuffer.append("<a name=\"");
        resultBuffer.append(id);
        resultBuffer.append("\"/>");
        resultBuffer.append(("<p>" + testTitle + ": " + name + "</p>"));
        resultBuffer.append("</div>");
        return resultBuffer.toString();
    }

    public String createFileAnchorTag(String name, String id) {
        return createAnchorTag(name, "test_output_name", AdaptersMessages.getMessage("fit.runner.test_file"), id);
    }

    public String createSuiteAnchorTag(String name, String id) {
        return createAnchorTag(name, "fit.runner.test_output_name", AdaptersMessages.getMessage("fit.runner.test_suite"), id);
    }

    public String printNameAndDescription(String name, String description) {
        StringBuffer output = new StringBuffer();
        output.append("<div class=\"test_output_name\">" + AdaptersMessages.getMessage("fit.runner.name{0}", name) + "</div>");
        if (null != description && !description.trim().equals("")) {
            output.append("<div class=\"test_output_name\">" + AdaptersMessages.getMessage("fit.runner.description{0}", description) + "</div>");
        }
        return output.toString();
    }

    public String startReport(String fileName, Date timestamp, String suiteFullPath) {
        String title = AdaptersMessages.getMessage("fit.runner.suite{0}run_at{1}", fileName, timestamp.toString());
        StringBuffer output = new StringBuffer();
        output.append("<html><head><title>" + title + "</title>\n");
        output.append("\t<link rel=\"stylesheet\" type=\"text/css\" href=\"css/fitnesse.css\" media=\"screen\"/>\n");
        output.append(" \t<link rel=\"stylesheet\" type=\"text/css\" href=\"css/fitnesse.css\" media=\"print\"/>\n");
        output.append(createCollapsableJs());
        output.append('\n');
        output.append("</head>\n");
        output.append("<body>\n");
        output.append("<div> " + AdaptersMessages.getMessage("fit.runner.suite_is{0}", suiteFullPath) + "</div>");
        return output.toString();
    }

    public String endReport() {
        return "\n</body></html>";
    }

    public String printSuiteRunnerPerformanceMetrics(Date runDate, long elapsedTime) {
        StringBuffer output = new StringBuffer();
        output.append("<div> " + AdaptersMessages.getMessage("fit.runner.suite_executed_at{0}elapsed_time{1}", runDate.toString(), formatElapsedTime(elapsedTime)) + "</div>");
        return output.toString();
    }

    private String formatElapsedTime(long time) {
        StringBuffer result = new StringBuffer();
        long days = time / (1000 * 60 * 60 * 24L);
        if (days > 0) {
            result.append(days).append(AdaptersMessages.getMessage("fit.runner.days"));
        }
        long remainder = time % (1000 * 60 * 60 * 24L);
        long hours = remainder / (1000 * 60 * 60L);
        if (hours > 0) {
            result.append(hours).append(AdaptersMessages.getMessage("fit.runner.hours"));
        }
        remainder = time % (1000 * 60 * 60L);
        long minutes = remainder / (1000 * 60L);
        if (minutes > 0) {
            result.append(minutes).append(AdaptersMessages.getMessage("fit.runner.minutes"));
        }
        remainder = time % (1000 * 60L);
        long seconds = remainder / (1000L);
        if (seconds > 0) {
            result.append(seconds).append(AdaptersMessages.getMessage("fit.runner.seconds"));
        }
        remainder = time % (1000);
        if (remainder > 0) {
            result.append(remainder).append(AdaptersMessages.getMessage("fit.runner.milliseconds"));
        } else if (result.length() == 0) {
            result.append(AdaptersMessages.getMessage("fit.runner.0_milliseconds"));
        }
        return result.toString();
    }
}
