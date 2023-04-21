package org.uimafit.examples.tutorial.ex1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.uima.jcas.JCas;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.TypeCapability;
import org.uimafit.examples.tutorial.type.RoomNumber;

/**
 * Here, the type capabilities are annotated using the @TypeCapability
 * annotation which are used by the AnalysisEngineFactory to specify this
 * information in the descriptors returned by, e.g.
 * AnalysisEngineFactory.createPrimitive().
 *
 * This class was copied from the uimaj-examples project and modified in
 * following ways:
 * <ul>
 * <li>The package name was changed to org.uimafit.tutorial.ex1</li>
 * <li>The super class was changed to
 * org.uimafit.component.JCasAnnotator_ImplBase</li>
 * <li>The class is annotated with org.uimafit.descriptor.TypeCapability</li>
 * </ul>
 *
 * @author unknown
 *
 */
@TypeCapability(outputs = { "org.apache.uima.tutorial.RoomNumber", "org.apache.uima.tutorial.RoomNumber:building" })
public class RoomNumberAnnotator extends JCasAnnotator_ImplBase {

    private Pattern mYorktownPattern = Pattern.compile("\\b[0-4]\\d-[0-2]\\d\\d\\b");

    private Pattern mHawthornePattern = Pattern.compile("\\b[G1-4][NS]-[A-Z]\\d\\d\\b");

    /**
	 * @see JCasAnnotator_ImplBase#process(JCas)
	 */
    @Override
    public void process(JCas aJCas) {
        String docText = aJCas.getDocumentText();
        Matcher matcher = mYorktownPattern.matcher(docText);
        while (matcher.find()) {
            RoomNumber annotation = new RoomNumber(aJCas, matcher.start(), matcher.end());
            annotation.setBuilding("Yorktown");
            annotation.addToIndexes();
        }
        matcher = mHawthornePattern.matcher(docText);
        while (matcher.find()) {
            RoomNumber annotation = new RoomNumber(aJCas, matcher.start(), matcher.end());
            annotation.setBuilding("Hawthorne");
            annotation.addToIndexes();
        }
    }
}
