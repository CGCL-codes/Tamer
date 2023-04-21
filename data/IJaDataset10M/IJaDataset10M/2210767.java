package net.jfipa.xml_test.fipa;

import junit.framework.*;
import junit.textui.*;
import net.jfipa.xml.Scanner;
import net.jfipa.xml.Parser;
import net.jfipa.xml.ParserException;
import net.jfipa.xml.Token;
import net.jfipa.io.DataSource;
import net.jfipa.xml.fipa.*;
import java.util.HashMap;
import java.util.TreeMap;
import java.io.IOException;
import org.apache.log4j.*;

public class MessageParserTest extends TestCase {

    MessageParser _messageParser;

    Scanner _scanner;

    public MessageParserTest(String name) {
        super(name);
    }

    protected void setUp() {
        _messageParser = new MessageParser();
        _scanner = new Scanner();
    }

    /***************************************************
     * TEST METHODS
     ***************************************************/
    public void testParseMessageAttributes() {
        try {
            System.out.println("\n ==> MessageParsertest.testParse()");
            DataSource src = null;
            src = new DataSource("<?xml version=\"1.0\" ?>" + "<fipa-message act=\"hei og h�\" conversation-id=\"3243\"> " + " <content href=\"some contenturl\" >" + " somecontent" + "</content>" + " <language href=\"some language\" >" + " somelanguage" + "</language>" + " <content-language-encoding href=\"some contentlangencurl\" >" + " content lang ecn" + "</content-language-encoding>" + " <ontology href=\"some ontologyurl\" >" + " some ontology" + "</ontology>" + " <reply-with href=\"some reply with\" >" + " some reply-with" + "</reply-with>" + " <protocol href=\"some protocol\" >" + " some protocol" + "</protocol>" + " <conversation-id href=\"conv id\" >" + " some conv id" + "</conversation-id>" + "<reply-by time=\"20010829\" href=\"reply-by url\" />" + "<in-reply-to href=\"in-reply-to url\">" + " some in-reply-to.." + "</in-reply-to>" + "<reply-to>" + "<agent-identifier>" + " <name refid=\"some ref to an agent id that receives..\" />" + " <addresses>" + "  <url href=\"http://foo.com/reply-to\" />" + " </addresses>" + " <user-defined>" + "  blabla user-defined2" + " </user-defined>" + "</agent-identifier>" + "</reply-to>" + "<receiver>" + "<agent-identifier>" + " <name refid=\"some ref to an agent id that receives..\" />" + " <addresses>" + "  <url href=\"http://foo.com/reply-to\" />" + " </addresses>" + " <user-defined>" + "  blabla user-defined2" + " </user-defined>" + "</agent-identifier>" + "</receiver>" + "<sender>" + "<agent-identifier>" + " <name refid=\"some ref to an agent id that receives..\" />" + " <addresses>" + "  <url href=\"http://foo.com/reply-to\" />" + " </addresses>" + " <user-defined>" + "  blabla user-defined2" + " </user-defined>" + "</agent-identifier>" + "</sender>" + "</fipa-message>");
            _scanner.setSource(src);
            _messageParser.setScanner(_scanner);
            _messageParser.start();
            StringBuffer url = new StringBuffer();
            Message message = _messageParser.parse();
            System.out.println("message:");
            System.out.println(message);
            System.out.println(" <== Messageparsertest.testParse()");
        } catch (Exception e) {
            System.err.println("testParse() failed, exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static Test suite() {
        return new TestSuite(MessageParserTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
