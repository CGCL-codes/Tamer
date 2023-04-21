package org.pubcurator.analyzers.range.annotators;

import java.net.URL;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.XMLInputSource;
import org.junit.Test;
import org.pubcurator.analyzers.range.Activator;

/**
 * @author Kai Schlamp (schlamp@gmx.de)
 *
 */
public class RangeAnnotatorTest {

    private static final String text = "Development of antineoplastic gene therapies is impaired by a paucity of transcription control elements with efficient, cancer cell-specific activity.";

    @Test
    public void testProcessJCas() throws Exception {
        URL url = Activator.getDefault().getBundle().getResource("desc/RangeAE.xml");
        XMLInputSource in = new XMLInputSource(url);
        ResourceSpecifier specifier = UIMAFramework.getXMLParser().parseResourceSpecifier(in);
        AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(specifier);
        JCas jcas = ae.newJCas();
        jcas.setDocumentText(text);
        ae.process(jcas);
    }
}
