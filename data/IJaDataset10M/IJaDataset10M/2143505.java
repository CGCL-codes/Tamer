package com.liferay.portal.tools;

import com.liferay.util.FileUtil;
import com.liferay.util.ListUtil;
import com.liferay.util.StringPool;
import com.liferay.util.StringUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.tools.ant.DirectoryScanner;

/**
 * <a href="XHTMLComplianceFormatter.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Charles May
 *
 */
public class XHTMLComplianceFormatter {

    public static void main(String[] args) {
        XHTMLComplianceFormatter xcf = new XHTMLComplianceFormatter();
        xcf.formatJSP();
    }

    /**
	 *	Formats all JSPs in docroot (excluding portlet JSPs) to conform to
	 *	XHTML specification
	 */
    public void formatJSP() {
        try {
            String basedir = "../portal/portal-web/docroot/html/";
            List list = new ArrayList();
            DirectoryScanner ds = new DirectoryScanner();
            ds.setIncludes(new String[] { "**\\*.jsp" });
            ds.setExcludes(new String[] { "portlet\\**" });
            ds.setBasedir(basedir);
            ds.scan();
            list.addAll(ListUtil.fromArray(ds.getIncludedFiles()));
            String[] files = (String[]) list.toArray(new String[list.size()]);
            for (int i = 0; i < files.length; i++) {
                File file = new File(basedir + files[i]);
                System.out.println("Processing file: " + file.getPath());
                String content = FileUtil.read(file);
                String newContent = _formatJSPContent(files[i], content);
                if (newContent.indexOf("<html") != -1 && newContent.indexOf("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">") == -1) {
                    newContent = StringUtil.replace(newContent, "<html", "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html");
                }
                newContent = _formatTags(newContent);
                for (int j = 0; j < _FIND_VALUES.length; j++) {
                    newContent = StringUtil.replace(newContent, _FIND_VALUES[j], _REPLACE_VALUES[j]);
                }
                if ((newContent != null) && !content.equals(newContent)) {
                    File outputFile = new File(StringUtil.replace(basedir, "/portal/", "/xhtml_output/portal/") + files[i]);
                    FileUtil.write(outputFile, newContent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 *	Formats all HTML tags found in the JSP
	 */
    private String _formatTags(String content) throws IOException {
        StringBuffer sb = new StringBuffer();
        boolean foundBeginForm = false;
        boolean foundEndForm = false;
        boolean processingJava = false;
        BufferedReader br = new BufferedReader(new StringReader(content));
        int lineCount = 0;
        String line = null;
        while ((line = br.readLine()) != null) {
            lineCount++;
            if ("<%".equals(line.trim()) || "<%!".equals(line.trim())) {
                processingJava = true;
            } else if ("%>".equals(line.trim())) {
                processingJava = false;
            }
            if (!processingJava) {
                int beginIndex = 0;
                int singleTagIndex = 0;
                for (int i = 0; i < _SINGLE_TAGS.length; i++) {
                    _populateSingleTagPositions(line, _SINGLE_TAGS[i], 0);
                }
                if (_SINGLE_TAG_POSITIONS.size() > 0) {
                    Collections.sort(_SINGLE_TAG_POSITIONS);
                    line = _addClosingsToSingleTags(line);
                    _SINGLE_TAG_POSITIONS.clear();
                }
                if (line.indexOf("<form") != -1) {
                    foundBeginForm = true;
                    line = _replaceFormNameWithId(line);
                } else if (line.indexOf("</form>") != -1) {
                    foundEndForm = true;
                }
                beginIndex = line.indexOf("<img");
                if (beginIndex != -1) {
                    String preTag = line.substring(0, beginIndex);
                    line = preTag + _formatImgTags(line.substring(beginIndex, line.length()));
                }
                beginIndex = line.indexOf("<font");
                if (beginIndex != -1) {
                    String preTag = line.substring(0, beginIndex);
                    line = preTag + _convertFontTagToSpan(line.substring(beginIndex, line.length()));
                }
                beginIndex = line.indexOf("<embed");
                if (beginIndex != -1) {
                    String preTag = line.substring(0, beginIndex);
                    line = preTag + _convertEmbedToObject(line.substring(beginIndex, line.length()));
                }
            }
            if (line.trim().length() == 0) {
                line = StringPool.BLANK;
            }
            line = StringUtil.trimTrailing(line);
            if (foundEndForm) {
                sb.append("</p>").append("\n");
                foundEndForm = false;
            }
            sb.append(line).append("\n");
            if (foundBeginForm) {
                sb.append("<p>").append("\n");
                foundBeginForm = false;
            }
        }
        br.close();
        String newContent = sb.toString();
        if (newContent.endsWith("\n")) {
            newContent = newContent.substring(0, newContent.length() - 1);
        }
        return newContent;
    }

    /**
	 *	Record all positions of single tags. This will be used in the next method
	 *	to close all single tags
	 */
    private void _populateSingleTagPositions(String line, String tagName, int startIndex) throws IOException {
        if (startIndex < line.length()) {
            int tagIndex = line.indexOf("<" + tagName, startIndex);
            if (tagIndex != -1) {
                _SINGLE_TAG_POSITIONS.add(new Integer(tagIndex));
                _populateSingleTagPositions(line, tagName, tagIndex + 1);
            }
        }
    }

    /**
	 *	Iterate through the single tag positions list and add closings to all
	 *	single tags
	 */
    private String _addClosingsToSingleTags(String line) throws IOException {
        int beginIndex = ((Integer) _SINGLE_TAG_POSITIONS.get(0)).intValue();
        int endIndex = 0;
        String newLine = line.substring(0, beginIndex);
        int numTags = _SINGLE_TAG_POSITIONS.size();
        for (int i = 0; i < numTags; i++) {
            beginIndex = ((Integer) _SINGLE_TAG_POSITIONS.get(i)).intValue();
            endIndex = _getTagEndIndex(line, beginIndex + 1);
            newLine += line.substring(beginIndex, endIndex) + "/>";
            if (i + 1 < numTags) {
                int nextIndex = ((Integer) _SINGLE_TAG_POSITIONS.get(i + 1)).intValue();
                if (nextIndex - (endIndex + 1) > 1) {
                    newLine += line.substring(endIndex + 1, nextIndex);
                }
            }
        }
        return newLine;
    }

    /**
	 *	Replace all "name" attributes with "id" attribute in forms
	 */
    private String _replaceFormNameWithId(String line) throws IOException {
        int beginIndex = line.indexOf("<form");
        int endIndex = _getTagEndIndex(line, beginIndex);
        String formTag = line.substring(beginIndex, endIndex + 1);
        formTag = StringUtil.replace(formTag, " name=", " id=");
        String preTag = line.substring(0, beginIndex);
        String postTag = line.substring(endIndex + 1, line.length());
        return preTag + formTag + postTag;
    }

    /**
	 *	Reconstruct img tag and only allow DTD acceptable and required attributes
	 * 	TODO: Move all other attributes into CSS (e.g., vspace, border, hspace, etc.)
	 */
    private String _formatImgTags(String line) throws IOException {
        String newLine = "";
        if (line.startsWith("<img")) {
            int endIndex = line.indexOf("/>");
            String imgTag = line.substring(0, endIndex + 2);
            String imgNewTag = "<img ";
            imgNewTag += _getAttribute("src", imgTag, endIndex);
            String nameAttribute = _getAttribute("name", "id", imgTag, endIndex);
            if (nameAttribute != null) {
                imgNewTag += " " + nameAttribute;
            }
            String titleAttribute = _getAttribute("title", imgTag, endIndex);
            if (titleAttribute != null) {
                imgNewTag += " " + titleAttribute;
            }
            String widthAttribute = _getAttribute("width", imgTag, endIndex);
            if (widthAttribute != null) {
                imgNewTag += " " + widthAttribute;
            }
            String heightAttribute = _getAttribute("height", imgTag, endIndex);
            if (heightAttribute != null) {
                imgNewTag += " " + heightAttribute;
            }
            String onMouseOutAttribute = _getAttribute("onMouseOut", imgTag, endIndex);
            if (onMouseOutAttribute != null) {
                imgNewTag += " " + onMouseOutAttribute;
            }
            String onMouseOverAttribute = _getAttribute("onMouseOver", imgTag, endIndex);
            if (onMouseOverAttribute != null) {
                imgNewTag += " " + onMouseOverAttribute;
            }
            String altAttribute = _getAttribute("alt", imgTag, endIndex);
            if (altAttribute != null) {
                imgNewTag += " " + altAttribute;
            } else {
                imgNewTag += " alt=\"\"";
            }
            newLine = imgNewTag + "/>";
            if (endIndex + 1 < line.length()) {
                newLine += _formatImgTags(line.substring(endIndex + 2, line.length()));
            }
        } else {
            int endIndex = line.indexOf("<img");
            if (endIndex == -1) {
                endIndex = line.length();
            }
            newLine = line.substring(0, endIndex);
            if (endIndex < line.length()) {
                newLine += _formatImgTags(line.substring(endIndex, line.length()));
            }
        }
        return newLine;
    }

    /**
	 *	Convert all font tags to span tags, and convert all font attributes to
	 * 	span's style attribute
	 */
    private String _convertFontTagToSpan(String line) throws IOException {
        String newLine = "";
        if (line.startsWith("<font")) {
            int endIndex = _getTagEndIndex(line, 0);
            String fontTag = line.substring(0, endIndex + 1);
            String spanTag = "<span";
            String classAttribute = _getAttribute("class", fontTag, endIndex);
            if (classAttribute != null) {
                spanTag += " " + classAttribute;
            }
            spanTag += " " + _getStyleAttributes(fontTag);
            newLine = spanTag + ">";
            if (endIndex + 1 < line.length()) {
                newLine += _convertFontTagToSpan(line.substring(endIndex + 1, line.length()));
            }
        } else {
            int endIndex = line.indexOf("<font");
            if (endIndex == -1) {
                endIndex = line.length();
            }
            newLine = line.substring(0, endIndex);
            if (endIndex < line.length()) {
                newLine += _convertFontTagToSpan(line.substring(endIndex, line.length()));
            }
        }
        return newLine;
    }

    /**
	 *	Get font attributes and convert them to span's style attribute
	 */
    private String _getStyleAttributes(String fontTag) throws IOException {
        boolean foundStyle = false;
        String styleTag = "";
        int attribIndex = 0;
        attribIndex = fontTag.indexOf("face=\"");
        if (attribIndex != -1) {
            attribIndex += 6;
            String faceAttribute = fontTag.substring(attribIndex, fontTag.length());
            int endIndex = attribIndex + _getAttributeEndIndex(faceAttribute);
            styleTag += "font-family: " + fontTag.substring(attribIndex, endIndex) + "; ";
            foundStyle = true;
        }
        attribIndex = fontTag.indexOf("color=\"");
        if (attribIndex != -1) {
            attribIndex += 7;
            String colorAttribute = fontTag.substring(attribIndex, fontTag.length());
            int endIndex = attribIndex + _getAttributeEndIndex(colorAttribute);
            styleTag += "color: " + fontTag.substring(attribIndex, endIndex) + "; ";
            foundStyle = true;
        }
        attribIndex = fontTag.indexOf("size=\"");
        if (attribIndex != -1) {
            attribIndex += 6;
            String sizeAttribute = fontTag.substring(attribIndex, fontTag.length());
            int endIndex = attribIndex + _getAttributeEndIndex(sizeAttribute);
            int size = Integer.parseInt(fontTag.substring(attribIndex, endIndex));
            String fontSize = "";
            if (size == 1) {
                fontSize = "8pt";
            } else if (size == 2) {
                fontSize = "10pt";
            } else if (size == 3) {
                fontSize = "12pt";
            } else if (size == 4) {
                fontSize = "14pt";
            } else if (size == 5) {
                fontSize = "18pt";
            } else if (size == 6) {
                fontSize = "24pt";
            } else if (size == 7) {
                fontSize = "36pt";
            }
            styleTag += "font-size: " + fontSize + "; ";
            foundStyle = true;
        }
        if (foundStyle) {
            styleTag = styleTag.substring(0, styleTag.length() - 2);
            styleTag = "style=\"" + styleTag + "\"";
        } else if (fontTag.indexOf("style=\"") != -1) {
            int beginIndex = fontTag.indexOf("style=");
            int endIndex = fontTag.indexOf("\"", beginIndex + 7);
            styleTag = fontTag.substring(beginIndex, endIndex + 1);
        }
        return styleTag;
    }

    /**
	 *	Convert embed tags to object tags
	 *	TODO: Currently only converts to audio/wav object tag. Need to take into
	 *		  account other possibilities
	 */
    private String _convertEmbedToObject(String line) throws IOException {
        String newLine = "";
        if (line.startsWith("<embed")) {
            int endIndex = line.indexOf("/>");
            String embedTag = line.substring(0, endIndex + 2);
            String objectTag = "<object ";
            String srcAttribute = _getAttribute("src", "data", embedTag, endIndex);
            if (srcAttribute != null) {
                objectTag += srcAttribute;
            }
            newLine = objectTag + " type=\"audio/wav\"/>";
            if (endIndex + 1 < line.length()) {
                newLine += _convertEmbedToObject(line.substring(endIndex + 2, line.length()));
            }
        } else {
            int endIndex = line.indexOf("<embed");
            if (endIndex == -1) {
                endIndex = line.length();
            }
            newLine = line.substring(0, endIndex);
            if (endIndex < line.length()) {
                newLine += _convertEmbedToObject(line.substring(endIndex, line.length()));
            }
        }
        return newLine;
    }

    /**
	 *	Returns a String representation of a particular attribute in a tag.
	 *	Performs logic to recognize Java code within tag and disregard it.
	 *	Replaces an old attribute name with a new one if supplied.
	 */
    private String _getAttribute(String oldAttributeName, String newAttributeName, String tag, int endIndex) throws IOException {
        String attributeValue = newAttributeName + "=\"";
        int attribBeginIndex = tag.indexOf(oldAttributeName);
        if (attribBeginIndex != -1) {
            attribBeginIndex += oldAttributeName.length() + 2;
            int attribEndIndex = tag.indexOf("\"", attribBeginIndex);
            int openJavaIndex = tag.indexOf("<%=", attribBeginIndex);
            if (openJavaIndex != -1 && openJavaIndex < attribEndIndex) {
                attribEndIndex = openJavaIndex + 3 + _getAttributeEndIndex(tag.substring(openJavaIndex + 3, tag.length()));
            }
            attributeValue += tag.substring(attribBeginIndex, attribEndIndex + 1);
        } else {
            attributeValue = null;
        }
        return attributeValue;
    }

    /**
	 *	If only one attribute name is supplied, it will be used as the new
	 *	attribute name as well
	 */
    private String _getAttribute(String attributeName, String tag, int endIndex) throws IOException {
        return _getAttribute(attributeName, attributeName, tag, endIndex);
    }

    /**
	 *	Returns the position in the tag where a tag's attribute ends. Takes into
	 *	account inline Java code.
	 */
    private int _getAttributeEndIndex(String tag) throws IOException {
        int closeJavaIndex = tag.indexOf("%>");
        int endIndex = tag.indexOf("\"", closeJavaIndex);
        int openJavaIndex = tag.indexOf("<%=");
        if (openJavaIndex != -1 && openJavaIndex < endIndex) {
            endIndex = openJavaIndex + 3 + _getAttributeEndIndex(tag.substring(openJavaIndex + 3));
        }
        return endIndex;
    }

    /**
	 *	Returns the position in the line where a tag ends (before the > or />). Takes into
	 *	account inline Java code.
	 */
    private int _getTagEndIndex(String line, int startIndex) throws IOException {
        int endIndex = line.indexOf(">", startIndex);
        if (endIndex == -1) {
            endIndex = line.length();
        } else {
            String endType = line.substring(endIndex - 1, endIndex);
            if ("%".equals(endType)) {
                endIndex = _getTagEndIndex(line, endIndex + 1);
            } else if ("/".equals(endType)) {
                endIndex = endIndex - 1;
            }
        }
        return endIndex;
    }

    private String _formatJSPContent(String fileName, String content) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new StringReader(content));
        int lineCount = 0;
        String line = null;
        while ((line = br.readLine()) != null) {
            lineCount++;
            int x = line.indexOf("\"<%=");
            int y = line.indexOf("%>\"", x);
            boolean hasTagLibrary = false;
            for (int i = 0; i < _TAG_LIBRARIES.length; i++) {
                if (line.indexOf("<" + _TAG_LIBRARIES[i] + ":") != -1) {
                    hasTagLibrary = true;
                    break;
                }
            }
            if ((x != -1) && (y != -1) && hasTagLibrary) {
                String regexp = line.substring(x, y + 3);
                if (regexp.indexOf("\\\"") == -1) {
                    regexp = regexp.substring(1, regexp.length() - 1);
                    if (regexp.indexOf("\"") != -1) {
                        line = line.substring(0, x) + "'" + regexp + "'" + line.substring(y + 3, line.length());
                    }
                }
            }
            if (line.trim().length() == 0) {
                line = StringPool.BLANK;
            }
            line = StringUtil.trimTrailing(line);
            sb.append(line).append("\n");
        }
        br.close();
        String newContent = sb.toString();
        if (newContent.endsWith("\n")) {
            newContent = newContent.substring(0, newContent.length() - 1);
        }
        return newContent;
    }

    private static final String[] _TAG_LIBRARIES = new String[] { "c", "html", "jsp", "liferay", "portlet", "struts", "tiles" };

    private static final String[] _SINGLE_TAGS = new String[] { "br", "img", "meta", "link", "input", "hr", "embed" };

    private static final String[] _FIND_VALUES = new String[] { "&", "&amp;nbsp;", "&amp;&amp;", "onSubmit=", "onClick=", "onChange=", "onMouseOut=", "onMouseOver=", "script language=\"JavaScript\"", "/font", "<DIV", "<A ", "</DIV>", "</A>" };

    private static final String[] _REPLACE_VALUES = new String[] { "&amp;", "&nbsp;", "&&", "onsubmit=", "onclick=", "onchange=", "onmouseout=", "onmouseover=", "script type=\"text/javascript\"", "/span", "<div", "<a ", "</div>", "</a>" };

    private static List _SINGLE_TAG_POSITIONS = new ArrayList();
}
