package org.wizard4j.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wizard4j.flowchart.Flowchart;
import org.wizard4j.WizardFactory;
import org.xml.sax.InputSource;
import junit.framework.TestCase;
import org.wizard4j.flowchart.EBuiltInFlowchartPresentationMode;
import org.wizard4j.util.File2String;
import org.wizard4j.validation.WizardValidator;

public class TestFlowchartExpansion extends TestCase {

    private static Logger logger = LoggerFactory.getLogger(TestFlowchartExpansion.class);

    private WizardValidator wv;

    protected void setUp() {
        wv = WizardFactory.newWizardValidator();
    }

    public void testNonsenseExample() {
        logger.info("Start testNonsenseExample");
        String xmlFileName = "file:test/flowchart.nonsense.xml";
        try {
            Flowchart flowchart = wv.validateFlowchart(new InputSource(xmlFileName));
            String simpleTextTree = flowchart.getBuiltInPresentation(EBuiltInFlowchartPresentationMode.TREE_TEXT);
            logger.debug(simpleTextTree);
            assertEquals(File2String.getFileContent("test/flowchart.presentation.simpleTextTree.nonsense.txt"), simpleTextTree);
            flowchart.expand();
            String expandedSimpleTextTree = flowchart.getBuiltInPresentation(EBuiltInFlowchartPresentationMode.EXTREE_TEXT);
            logger.debug(expandedSimpleTextTree);
            flowchart.setSelectedOnly(true);
            String expandedXmlTree = flowchart.getBuiltInPresentation(EBuiltInFlowchartPresentationMode.EXTREE_XML);
            logger.debug(expandedXmlTree);
            assertEquals(File2String.getFileContent("test/flowchart.presentation.simpleTextExpandedTree.nonsense.txt"), expandedSimpleTextTree);
        } catch (Exception e) {
            logger.error("", e);
            fail();
        }
    }

    public void testNonsenseExampleFragment() {
        logger.info("Start testNonsenseExampleFragment");
        String xmlFileName = "file:test/flowchart.nonsense.fragment.xml";
        try {
            Flowchart flowchart = wv.validateFlowchart(new InputSource(xmlFileName));
            String simpleExpandedTextTree = flowchart.getBuiltInPresentation(EBuiltInFlowchartPresentationMode.EXTREE_TEXT);
            logger.debug(simpleExpandedTextTree);
            assertEquals(File2String.getFileContent("test/flowchart.presentation.simpleTextExpandedTree.nonsense.txt"), simpleExpandedTextTree);
        } catch (Exception e) {
            logger.error("", e);
            fail();
        }
    }

    public void testNonsenseExampleFragment2() {
        logger.info("Start testNonsenseExampleFragment2");
        String xmlFileName = "file:test/flowchart.nonsense.fragment2.xml";
        try {
            Flowchart flowchart = wv.validateFlowchart(new InputSource(xmlFileName));
            String simpleExpandedTextTree = flowchart.getBuiltInPresentation(EBuiltInFlowchartPresentationMode.EXTREE_TEXT);
            logger.debug(simpleExpandedTextTree);
            assertEquals(File2String.getFileContent("test/flowchart.presentation.simpleTextExpandedTree.nonsense.txt"), simpleExpandedTextTree);
        } catch (Exception e) {
            logger.error("", e);
            fail();
        }
    }
}
