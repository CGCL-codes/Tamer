package org.uimafit.examples.tutorial.ex6;

import static org.uimafit.factory.AnalysisEngineFactory.createAnalysisEngineDescription;
import static org.uimafit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.uimafit.factory.ExternalResourceFactory.bindResource;
import static org.uimafit.factory.ExternalResourceFactory.createExternalResourceDescription;
import static org.uimafit.factory.TypeSystemDescriptionFactory.createTypeSystemDescription;
import static org.uimafit.util.JCasUtil.select;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.AnalysisComponent;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ExternalResourceDescription;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ExternalResource;
import org.uimafit.descriptor.TypeCapability;
import org.uimafit.examples.tutorial.type.Meeting;
import org.uimafit.examples.tutorial.type.UimaMeeting;
import org.uimafit.factory.AggregateBuilder;

/**
 * Example annotator that iterates over Meeting annotations and annotates a
 * meeting as a UimaMeeting if a UIMA acronym occurs in close proximity to that
 * meeting. When combined in an aggregate TAE with the UimaAcronymAnnotator,
 * demonstrates the use of the ResourceManager to share data between annotators.
 * 
 * @author unknown
 */
@TypeCapability(inputs = "org.apache.uima.tutorial.Meeting", outputs = "org.apache.uima.tutorial.UimaMeeting")
public class UimaMeetingAnnotator extends JCasAnnotator_ImplBase {

    static final String RESOURCE_UIMA_TERM_TABLE = "UimaTermTable";

    @ExternalResource(key = RESOURCE_UIMA_TERM_TABLE)
    private StringMapResource mMap;

    /**
	 * @see AnalysisComponent#initialize(UimaContext)
	 */
    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext);
        try {
            mMap = (StringMapResource) getContext().getResourceObject("UimaTermTable");
        } catch (ResourceAccessException e) {
            throw new ResourceInitializationException(e);
        }
    }

    /**
	 * @see JCasAnnotator_ImplBase#process(JCas)
	 */
    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        String text = aJCas.getDocumentText();
        List<UimaMeeting> uimaMeetings = new ArrayList<UimaMeeting>();
        for (Meeting meeting : select(aJCas, Meeting.class)) {
            int begin = meeting.getBegin() - 50;
            int end = meeting.getEnd() + 50;
            if (begin < 0) {
                begin = 0;
            }
            if (end > text.length()) {
                end = text.length();
            }
            String window = text.substring(begin, end);
            StringTokenizer tokenizer = new StringTokenizer(window, " \t\n\r.<.>/?\";:[{]}\\|=+()!");
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if (mMap.get(token) != null) {
                    UimaMeeting annot = new UimaMeeting(aJCas, meeting.getBegin(), meeting.getEnd());
                    annot.setRoom(meeting.getRoom());
                    annot.setDate(meeting.getDate());
                    annot.setStartTime(meeting.getStartTime());
                    annot.setEndTime(meeting.getEndTime());
                    uimaMeetings.add(annot);
                    break;
                }
            }
        }
        for (UimaMeeting meeting : uimaMeetings) {
            meeting.addToIndexes();
        }
    }

    public static void main(String[] args) throws Exception {
        File outputDirectory = new File("src/main/resources/org/uimafit/tutorial/ex6/");
        outputDirectory.mkdirs();
        TypeSystemDescription tsd = createTypeSystemDescription("org.uimafit.tutorial.type.TypeSystem");
        AnalysisEngineDescription aed = createPrimitiveDescription(UimaMeetingAnnotator.class, tsd);
        aed.toXML(new FileOutputStream(new File(outputDirectory, "UimaMeetingAnnotator.xml")));
        AggregateBuilder builder = new AggregateBuilder();
        builder.add(createAnalysisEngineDescription("org.uimafit.tutorial.ex6.UimaAcronymAnnotator"));
        builder.add(createAnalysisEngineDescription("org.uimafit.tutorial.ex6.UimaMeetingAnnotator"));
        AnalysisEngineDescription aggregate = builder.createAggregateDescription();
        ExternalResourceDescription erd = createExternalResourceDescription("UimaAcronymTableFile", StringMapResource_impl.class, "file:org/uimafit/tutorial/ex6/uimaAcronyms.txt");
        bindResource(aggregate, RESOURCE_UIMA_TERM_TABLE, erd);
        aggregate.toXML(new FileOutputStream(new File(outputDirectory, "UimaMeetingDetectorTAE.xml")));
    }
}
