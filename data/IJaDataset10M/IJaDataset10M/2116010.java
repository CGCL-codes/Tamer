package RichNesse;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class RichToFit {

    public static String ConvertRichToFit(String richXml) {
        richXml = CleanupRichTextBeforeXmlLoad(richXml);
        try {
            Element topElement = XMLHelper.CreateRichTextElement(richXml);
            String fitText = ConvertElement(topElement, false);
            fitText = fitText.replace("\r", "").replaceAll("\n{4,}", "\n\n\n").replaceAll("\n", "~~~").replaceAll("~~~", "\r\n").replaceAll(XMLHelper.AMPERSAND_SUBSTITUTE, "&").replaceAll(XMLHelper.LEFT_ESCAPE, "<").replaceAll("&quot;", "\"").replaceAll(XMLHelper.RIGHT_ESCAPE, ">");
            if (fitText.endsWith("\r\n") && !topElement.getLastChild().getNodeName().equals("br")) fitText = fitText.substring(0, fitText.length() - 2);
            return fitText;
        } catch (Exception ex) {
            return XMLHelper.ExceptionString(ex);
        }
    }

    private static String CheckForCentered(Element element) {
        if (element.hasAttribute("style")) {
            if (element.getAttribute("style").indexOf("text-align: center") > -1 || element.getAttribute("style").indexOf("text-align:center") > -1) {
                return "!c ";
            }
        }
        return "";
    }

    private static String CleanupRichTextBeforeXmlLoad(String rich) {
        if (rich.substring(rich.length() - 3).equals("<p>")) {
            rich = rich.substring(0, rich.length() - 3);
        }
        rich = rich.replaceAll("<tbody>", "").replaceAll("</tbody>", "").replaceAll("&nbsp;", " ").replaceAll("&amp;", "&").replaceAll("&", XMLHelper.AMPERSAND_SUBSTITUTE);
        return rich;
    }

    private static String ConvertElement(Element element, boolean unformattedTable) {
        String fitText = "";
        String tagName = element.getTagName();
        if (tagName.equals(XMLHelper.DOC_ELEMENT)) {
            fitText += ConvertElementChildren(element, true, unformattedTable, false);
        } else if (tagName.equals("table")) {
            unformattedTable = !element.hasAttribute(XMLHelper.UNFORMATTED) || element.getAttribute(XMLHelper.UNFORMATTED).equals("true");
            fitText += (unformattedTable ? "!" : "");
            fitText += ConvertElementChildren(element, false, unformattedTable);
            fitText = EnsureEndsWithLineBreak(fitText, element);
        } else if (tagName.equals("tr")) {
            fitText += "|";
            fitText += ConvertElementChildren(element, false, unformattedTable);
            fitText = EnsureEndsWithLineBreak(fitText, element);
        } else if (tagName.equals("td")) {
            fitText += CheckForCentered(element) + ConvertElementChildren(element, true, unformattedTable);
            fitText += "|";
        } else if (unformattedTable) {
            fitText += (" " + ConvertElementChildren(element, true, unformattedTable)).trim();
        } else if (tagName.equals("ul") || tagName.equals("ol")) {
            fitText += ConvertElementChildren(element, false, unformattedTable);
            if (!element.getParentNode().getNodeName().equals("ul") && !element.getParentNode().getNodeName().equals("ol") && !element.getParentNode().getNodeName().equals("li")) fitText = fitText.substring(1) + "\n";
        } else if (tagName.equals("li")) {
            fitText += "\n" + GetListItemDepth(element);
            if (element.getParentNode().getNodeName().equals("ul")) fitText += "* "; else fitText += "1 ";
            fitText += ConvertElementChildren(element, true, unformattedTable);
        } else if (tagName.equals("b")) {
            fitText += "'''";
            fitText += ConvertElementChildren(element, true, unformattedTable);
            fitText += "'''";
        } else if (tagName.equals("p") || tagName.equals("br")) {
            fitText += ConvertElementChildren(element, true, unformattedTable);
            fitText = EnsureEndsWithLineBreak(fitText, element);
        } else if (tagName.equals("i")) {
            fitText += "''";
            fitText += ConvertElementChildren(element, true, unformattedTable);
            fitText += "''";
        } else if (tagName.equals("img")) {
            if (element.hasAttribute("src")) {
                fitText += "!img " + element.getAttribute("src");
            }
        } else if (tagName.equals("a")) {
            if (element.hasAttribute("href")) {
                fitText += element.getAttribute("href");
            } else {
                fitText += ConvertElementChildren(element, true, unformattedTable);
            }
        } else if (tagName.equals("strike")) {
            fitText += "--";
            fitText += ConvertElementChildren(element, true, unformattedTable);
            fitText += "--";
        } else if (tagName.equals("div")) {
            fitText += "!c ";
            fitText += ConvertElementChildren(element, true, unformattedTable);
            fitText = EnsureEndsWithLineBreak(fitText, element);
        } else if (tagName.equals("h1")) {
            fitText += "!1 " + CheckForCentered(element);
            fitText += ConvertElementChildren(element, true, unformattedTable);
            fitText = EnsureEndsWithLineBreak(fitText, element);
        } else if (tagName.equals("h2")) {
            fitText += "!2 " + CheckForCentered(element);
            fitText += ConvertElementChildren(element, true, unformattedTable);
            fitText = EnsureEndsWithLineBreak(fitText, element);
        } else if (tagName.equals("h3")) {
            fitText += "!3 " + CheckForCentered(element);
            fitText += ConvertElementChildren(element, true, unformattedTable);
            fitText = EnsureEndsWithLineBreak(fitText, element);
        } else if (tagName.equals("hr")) {
            fitText += "----";
        } else if (tagName.equals("pre")) {
            fitText += "{{{";
            fitText += ReplaceTabForRich(ConvertElementChildren(element, true, false));
            fitText += "}}}";
        } else {
            fitText += ConvertElementChildren(element, true, unformattedTable);
        }
        return fitText;
    }

    private static String ConvertElementChildren(Element element, boolean getText, boolean unformattedTable) {
        return ConvertElementChildren(element, getText, unformattedTable, true);
    }

    private static String ConvertElementChildren(Element element, boolean getText, boolean unformattedTable, boolean keepLineBreaks) {
        String fitText = "";
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node instanceof Element) {
                fitText += ConvertElement((Element) node, unformattedTable);
            } else {
                if (getText) {
                    if (node instanceof Text) {
                        Text text = (Text) node;
                        String nodeText = text.getNodeValue();
                        if (element.getNodeName().equals(XMLHelper.DOC_ELEMENT)) {
                            if (nodeText.startsWith("?")) {
                                if (nodeText.length() > 1) nodeText = nodeText.substring(1); else nodeText = "";
                            }
                        }
                        if (!keepLineBreaks) {
                            nodeText = nodeText.replaceAll("\r", "").replaceAll("\n", "");
                        }
                        fitText += nodeText;
                    }
                }
            }
        }
        return fitText;
    }

    private static String EnsureEndsWithLineBreak(String fitText, Element element) {
        if (!IsInTableCell(element)) {
            if (fitText.length() == 0 || !fitText.substring(fitText.length() - 1, fitText.length()).equals("\n")) {
                fitText += "\r\n";
            }
        }
        return fitText;
    }

    private static boolean IsInTableCell(Element element) {
        while (element != null) {
            if (element.getNodeName().equals("td")) {
                return true;
            }
            if (element.getParentNode() == null || !(element.getParentNode() instanceof Element)) break;
            element = (Element) element.getParentNode();
        }
        return false;
    }

    private static String GetListItemDepth(Element element) {
        int depth = 0;
        while (true) {
            String parentName = element.getParentNode().getNodeName();
            if (parentName.equals("ul") || parentName.equals("ol")) {
                depth++;
            } else if (!parentName.equals("li")) {
                break;
            }
            element = (Element) element.getParentNode();
        }
        String spaces = "";
        for (int i = 0; i < depth; i++) spaces += " ";
        return spaces;
    }

    public static String ReplaceTabForRich(String in) {
        return in.replaceAll(XMLHelper.RICH_TAB_SUBSTITUTE, "\t");
    }
}
