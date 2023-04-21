package com.enerjy.analyzer.eclipse;

import java.io.File;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import com.enerjy.analyzer.IGuiFactory;
import com.enerjy.analyzer.java.IJavaRule;
import com.enerjy.common.util.JUnitUtils;
import com.enerjy.common.util.XmlUtils;

public class DocVerifier extends TestCase {

    private final String rule;

    DocVerifier(String rule) {
        this.rule = rule;
        setName(rule + ": Documentation");
    }

    @Override
    protected void runTest() throws Throwable {
        File root = JUnitUtils.findSourceRoot("Analyzer/Plugin/Eclipse");
        File check = new File(root.getParentFile().getParentFile(), "help/rules/JAVA" + rule + ".html");
        assertTrue("Missing doc file: " + check, check.exists());
        Document doc = XmlUtils.parseXmlDocument(check);
        XPath xpath = XPathFactory.newInstance().newXPath();
        String title = (String) xpath.evaluate("//title", doc, XPathConstants.STRING);
        assertEquals("Title", "Enerjy Code Analysis - JAVA" + rule, title);
        IJavaRule instance = (IJavaRule) Class.forName("com.enerjy.analyzer.java.rules.T" + rule).newInstance();
        assertEquals("Bad rule number", "JAVA" + rule, findString(xpath, doc, "//div[@id='ruleNumber']"));
        assertEquals("Bad rule description", instance.getName().substring(9).toLowerCase(), findString(xpath, doc, "//div[@id='ruleDesc']").toLowerCase());
        boolean needsConfig = null != instance.createPreferencePanel(new GuiFactory());
        boolean hasConfig = null != xpath.evaluate("//h1[.='Configuration']", doc, XPathConstants.NODE);
        if (needsConfig) {
            assertTrue("Missing configuration text", hasConfig);
        } else {
            assertFalse("Unnecessary configuration text", hasConfig);
        }
    }

    private static String findString(XPath xpath, Document doc, String expression) throws XPathExpressionException {
        String answer = xpath.evaluate(expression, doc);
        if (null == answer) {
            return answer;
        }
        return answer.trim();
    }

    private static class GuiFactory implements IGuiFactory {

        private static final Object TOKEN = new Object();

        public Object createMultilineText(String property, String prompt) {
            return TOKEN;
        }

        public Object createNameRegExBuilder(String property, String prompt) {
            return TOKEN;
        }

        public Object createScopeSelection(String property, String prompt) {
            return TOKEN;
        }

        public Object createSingleInt(String property, String prompt) {
            return TOKEN;
        }

        public Object createYesNoRadio(String property, String prompt) {
            return TOKEN;
        }

        public IGuiFactory createPanel() {
            return this;
        }
    }
}
