package com.arcucomp.xmgel;

import java.util.*;
import com.arcucomp.xmlplayground.XMLPlayground_i;
import com.arcucomp.util.*;
import com.arcucomp.xmgel.XMgeL;

public class XMGeLEasyTester {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java com.arcucomp.util.FileToStringLoader <filename>");
            System.exit(0);
        }
        int i = 0;
        String xslStr = "";
        xslStr = xslStr + "<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>\n";
        xslStr = xslStr + "<!-- from Chris Coleman XSL Tutorial -->\n";
        xslStr = xslStr + "<xsl:template match=\"/xslTutorial\">\n";
        xslStr = xslStr + "	<HTML>\n";
        xslStr = xslStr + "	<xsl:apply-templates/>\n";
        xslStr = xslStr + "	</HTML>\n";
        xslStr = xslStr + "</xsl:template>\n";
        xslStr = xslStr + "<xsl:template match=\"list\">\n";
        xslStr = xslStr + "	<xsl:apply-templates select=\"entry\"/>\n";
        xslStr = xslStr + "</xsl:template>\n";
        xslStr = xslStr + "<xsl:template match=\"entry\">\n";
        xslStr = xslStr + "	<xsl:value-of select=\".\"/>\n";
        xslStr = xslStr + "	<xsl:value-of select=\"@name\"/>\n";
        xslStr = xslStr + "	<xsl:text>: </xsl:text>\n";
        xslStr = xslStr + "</xsl:template>\n";
        xslStr = xslStr + "</xsl:stylesheet>\n";
        String xmlStr = "";
        xmlStr = xmlStr + "<?xml version=\"1.0\"?>\n";
        xmlStr = xmlStr + "<!-- from Chris Coleman XSL Tutorial -->\n";
        xmlStr = xmlStr + "<xslTutorial >\n";
        xmlStr = xmlStr + "<list>\n";
        xmlStr = xmlStr + "<entry name=\"A\"/> \n";
        xmlStr = xmlStr + "<entry name=\"B\"/> \n";
        xmlStr = xmlStr + "<entry name=\"C\"/> \n";
        xmlStr = xmlStr + "<entry name=\"D\"/> \n";
        xmlStr = xmlStr + "</list>\n";
        xmlStr = xmlStr + "</xslTutorial>\n";
        String xslStr2 = "";
        xslStr2 = xslStr2 + "<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>\n";
        xslStr2 = xslStr2 + "<!-- from Chris Coleman XSL Tutorial -->\n";
        xslStr2 = xslStr2 + "<xsl:template match=\"/xslTutorial\">\n";
        xslStr2 = xslStr2 + "	<HTML>\n";
        xslStr2 = xslStr2 + "	<xsl:apply-templates/>\n";
        xslStr2 = xslStr2 + "	</HTML>\n";
        xslStr2 = xslStr2 + "</xsl:template>\n";
        xslStr2 = xslStr2 + "<xsl:template match=\"list\">\n";
        xslStr2 = xslStr2 + "	<xsl:apply-templates select=\"entry\"/>\n";
        xslStr2 = xslStr2 + "</xsl:template>\n";
        xslStr2 = xslStr2 + "<xsl:template match=\"entry\">\n";
        xslStr2 = xslStr2 + "	<xsl:value-of select=\".\"/>\n";
        xslStr2 = xslStr2 + "	<xsl:value-of select=\"@name\"/>\n";
        xslStr2 = xslStr2 + "	<xsl:text>, </xsl:text>\n";
        xslStr2 = xslStr2 + "</xsl:template>\n";
        xslStr2 = xslStr2 + "</xsl:stylesheet>\n";
        try {
            int status = 0;
            XMgeL xmleverywhere = new XMgeL();
            XMLPlayground_i xmlplay = new XMLPlayground_i();
            String xmlCommandFile = args[0];
            FileToStringLoader f = new FileToStringLoader(xmlCommandFile);
            String tmpxecommandList = f.getString();
            String testStringOut = xmleverywhere.XG(tmpxecommandList, "");
        } catch (Exception ex) {
            System.err.println("Caught an exception." + ex);
            ex.printStackTrace();
        }
    }
}
