package com.googlecode.bdoc.doc.report;

import com.googlecode.bdoc.doc.domain.Scenario;
import com.googlecode.bdoc.doc.domain.Statement;
import com.googlecode.bdoc.utils.CamelCaseToSentenceTranslator;

/**
 *  @author Per Otto Bergum Christensen
 */
public class ReportTestHelper {

    public static String scenarioPart(int partIndex, Scenario scenario) {
        String camelCaseDescription = scenario.getParts().get(partIndex).camelCaseDescription();
        return CamelCaseToSentenceTranslator.translate(camelCaseDescription);
    }

    public static String sentence(Statement statement) {
        return CamelCaseToSentenceTranslator.translate(statement.getSentence());
    }

    public static String sentence(String camelCaseSentence) {
        return CamelCaseToSentenceTranslator.translate(camelCaseSentence);
    }
}
