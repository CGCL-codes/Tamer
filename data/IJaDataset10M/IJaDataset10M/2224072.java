package org.eclipse.tptp.test.tools.web.criterion.ba.engine;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.tptp.test.tools.web.lib.utils.WebTESTBasicStaticUtils;
import org.eclipse.tptp.test.tools.web.runner.engine.BasicCriterionEngineInput;
import org.eclipse.tptp.test.tools.web.runner.engine.CriterionTypeEngineProvider;
import org.eclipse.tptp.test.tools.web.runner.engine.ICriterionTypeEngine;
import org.eclipse.tptp.test.tools.web.runner.engine.IEngineInput;
import org.eclipse.tptp.test.tools.web.runner.engine.result.CriterionExecutionResult;
import org.eclipse.tptp.test.tools.web.runner.engine.result.EResultVerdict;
import org.eclipse.tptp.test.tools.web.runner.modelproxy.BasicCriterionReadProxy;
import org.eclipse.tptp.test.tools.web.runner.modelproxy.CriterionReadProxy;
import org.eclipse.tptp.test.tools.web.runner.modelproxy.GenericCriterionReadProxy;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class BACriterionEngine implements ICriterionTypeEngine {

    public static Log log = LogFactory.getLog(BACriterionEngine.class);

    public static final String PROPERTY_VALUE_XPATH_NAME = "valueXPath";

    public static final String PROPERTY_NAME_XPATH_NAME = "nameXPath";

    public static final String PROPERTY_ELEMENT_XPATH_NAME = "elementXPath";

    public static final String PROPERTY_TEST_BA_SEQUENCE = "testBASequence";

    protected CriterionTypeEngineProvider factory;

    protected DocumentBuilder builder;

    public void setCriterionEngineFactory(CriterionTypeEngineProvider factory2) {
        factory = factory2;
    }

    public CriterionExecutionResult runCriterion(CriterionReadProxy criterion, IEngineInput input) {
        CriterionExecutionResult result = new CriterionExecutionResult(criterion, input);
        try {
            if (builder == null) {
                try {
                    builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    result.setVerdict(EResultVerdict.Error);
                    result.addReason("ParserConfigurationException");
                    result.addText(e.getMessage());
                    log.error("ParserConfigurationException", e);
                    e.printStackTrace();
                }
            }
            GenericCriterionReadProxy htmlCrit = (GenericCriterionReadProxy) criterion;
            String xpathName = (String) htmlCrit.properties.get(PROPERTY_NAME_XPATH_NAME);
            String xpathValue = (String) htmlCrit.properties.get(PROPERTY_VALUE_XPATH_NAME);
            String xpathElement = (String) htmlCrit.properties.get(PROPERTY_ELEMENT_XPATH_NAME);
            boolean useSequenceOrder = Boolean.parseBoolean((String) htmlCrit.properties.get(PROPERTY_TEST_BA_SEQUENCE));
            executeXPath(result, htmlCrit.criterions, input, xpathElement, xpathName, xpathValue, useSequenceOrder);
            result.assignVerdict();
        } catch (RuntimeException ex) {
            result.setVerdict(EResultVerdict.Error);
            result.addReason("Runtime Excpetion");
            result.addText(ex.getMessage());
            log.error("Runtime Excpetion", ex);
            ex.printStackTrace();
        }
        return result;
    }

    private CriterionExecutionResult executeXPath(CriterionExecutionResult result, ArrayList childs, IEngineInput input, String xpathElement, String xpathName, String xpathValue, boolean testSequence) {
        if (xpathName == null || xpathElement == null || xpathElement.length() == 0 || xpathValue == null) {
            result.setVerdict(EResultVerdict.Error);
            result.addReason("Wrong BA Criterion");
            result.addText("A property is empty: [XPath Element: " + xpathElement + ", XPath Name: " + xpathName + ", XPath Value: " + xpathValue + "]");
            return result;
        }
        if (childs == null || childs.size() == 0) {
            return result;
        }
        boolean trim = ((BasicCriterionReadProxy) childs.get(0)).trimmed;
        boolean normalize = ((BasicCriterionReadProxy) childs.get(0)).normalized;
        Document node;
        LinkedHashMap foundPairs;
        boolean sequenceWrong = false;
        String sequenceErrorReason = "";
        String sequenceErrorTxt = "";
        try {
            node = builder.parse(new InputSource(new StringReader(input.getXML())));
            BAExpressionResult res = new BAExpressionResult(node, xpathElement, xpathName, xpathValue);
            foundPairs = BAEngineUtils.runPairExtraction(xpathElement, xpathName, xpathValue, node, res, trim, normalize);
            removeUnused(foundPairs, childs);
            if (!res.errorOccured) {
                for (Iterator it = childs.iterator(); it.hasNext(); ) {
                    BasicCriterionReadProxy child = (BasicCriterionReadProxy) it.next();
                    CriterionExecutionResult resultChild = new CriterionExecutionResult(child, input);
                    String name = child.name;
                    String resValue = (String) foundPairs.get(name);
                    if (child.trimmed) resValue = resValue.trim();
                    if (child.normalized) resValue = WebTESTBasicStaticUtils.normalize(resValue);
                    System.out.println("Supposed Pair: " + name + ", " + child.value);
                    System.out.println("Found: " + resValue);
                    BasicCriterionEngineInput inputBasic = new BasicCriterionEngineInput(input, resValue);
                    resultChild = factory.getCriterionEngine("Basic Criterion").runCriterion(child, inputBasic);
                    result.addChildResult(resultChild);
                    if (testSequence && !sequenceWrong) {
                        int pos = childs.indexOf(child);
                        if (pos > -1 && pos < foundPairs.size()) {
                            String nameOnPos = (String) foundPairs.keySet().toArray(new String[foundPairs.size()])[pos];
                            if (!nameOnPos.equals(name)) {
                                sequenceWrong = true;
                                sequenceErrorReason = "Wrong Sequence Order on Position " + (pos + 1);
                                sequenceErrorTxt = "Found Business Attribute with name '" + nameOnPos + "' instead of '" + name + "'!";
                            }
                        }
                    }
                }
            } else {
                result.setVerdict(EResultVerdict.Error);
                result.addReason("Error while executing BA Criterion XPath!");
                result.addText(res.errorMsg);
            }
        } catch (TransformerException e) {
            result.setVerdict(EResultVerdict.Error);
            result.addReason("Wrong XML: " + e.getMessage());
            result.addText("XML Input: " + input.getXML());
        } catch (SAXException e1) {
            result.setVerdict(EResultVerdict.Error);
            result.addReason("Wrong XML: " + e1.getMessage());
            result.addText("XML Input: " + input.getXML());
        } catch (IOException e1) {
            result.setVerdict(EResultVerdict.Error);
            result.addReason("Wrong XML: " + e1.getMessage());
            result.addText("XML Input: " + input.getXML());
        }
        if (testSequence && sequenceWrong) {
            result.setVerdict(EResultVerdict.Failure);
            result.addReason(sequenceErrorReason);
            result.addText(sequenceErrorTxt);
        }
        return result;
    }

    private void removeUnused(LinkedHashMap foundPairs, ArrayList childs) {
        if (childs == null || foundPairs == null) return;
        if (foundPairs.size() == 0) return;
        if (childs.size() == 0) {
            foundPairs.clear();
            return;
        }
        Object[] names = foundPairs.keySet().toArray();
        for (int i = 0; i < names.length; i++) {
            String nextName = names[i].toString();
            boolean found = false;
            for (Iterator it = childs.iterator(); it.hasNext(); ) {
                BasicCriterionReadProxy child = (BasicCriterionReadProxy) it.next();
                if (child.name.equals(nextName)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                foundPairs.remove(nextName);
            }
        }
    }
}
