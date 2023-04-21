package com.idna.dm.service.execution;

import static org.junit.Assert.assertSame;
import org.junit.Test;
import com.idna.common.domain.decisionmatrix.Outcome;
import com.idna.common.utils.ClasspathXmlFileImporter;
import com.idna.dm.domain.impl.Rule.RuleType;
import com.idna.dm.util.IntegrationTestViewee;
import com.idna.dm.util.xml.filters.DecisionOutcomeRetriever;

/**
 * Performs an end to end test on Test Decision 125.
 * <p>
 * For instruction on how to use this approach and to learn how to create new Test Decision subclasses, @see
 * {@linkplain BaseDecisionExecutionServicesIntegrationTest}.
 * 
 * @author vinay.nayak
 * 
 */
public class TestDecision125ExecutionServicesIntegrationTest extends BaseDecisionExecutionServicesIntegrationTest {

    private Outcome actualOutcome;

    @Override
    public void onSetUp() throws Exception {
        specifyUser(IntegrationTestViewee.JAVA_DEVELOPER);
        expectedRuleTypesOrder = new RuleType[] { RuleType.BASIC_EXPRESSION_WRAPPER };
        isActivityLoggable = false;
    }

    @Test
    public void runDecisionMatrixResponsePacketNotGettingWrittenForER_Match() throws Exception {
        expectedOutcome = Outcome.ACCEPT;
        inputXml = ClasspathXmlFileImporter.importXMLFromClasspath("/xml-test-packets/merging-and-response-generation/CORPVIII-1289.xml");
        String xmlResponse = decisionMatrixRunner.executeDecision(metaData, inputXml);
        logger.trace(xmlResponse);
        actualOutcome = DecisionOutcomeRetriever.getDecisionPrimaryOutcome(xmlResponse);
        assertSame(expectedOutcome, actualOutcome);
    }
}
