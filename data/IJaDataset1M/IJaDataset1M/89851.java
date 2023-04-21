package uk.ac.wlv.clg.nlp.termannotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;
import org.apache.lucene.index.IndexReader;
import uk.ac.wlv.clg.nlp.entityannotator.HybridEntityAnnotator;
import uk.ac.wlv.clg.nlp.misc.AnnotationPosition;
import uk.ac.wlv.clg.nlp.misc.Annotator;

/**
 * A class that annotates NL terms in a text. It aggregates several
 * 			{@link HybridEntityAnnotator} objects, one for each of the recognised term types:
 * 			<i>movie genre</i>, <i>credit card</i>, <i>price value</i> and <i>facility</i>. 
 * 			The instances are extracted 
 * 			from the GB RDF data and used to populate inverted indexes
 * 			that can be used to perform fast fuzzy searches. 
 *   
 * @author idornescu
 * @see HybridEntityAnnotator
 * @version SVN $Rev$ by $Author$
 */
public class StandardTermAnnotator implements Annotator {

    /** the set of aggregated annotator objects */
    static ArrayList<Annotator> annotators;

    /**
	 * Construct a standard English term annotator for the QALL-ME Framework
	 * @param prefix the absolute path to the directory containing the facility index
	 */
    public StandardTermAnnotator(String prefix) {
        init(prefix);
    }

    /**
	 * Construct a standard English term annotator for the QALL-ME Framework
	 * Tries to access the facility index in the {@code "/res"} directory
	 */
    public StandardTermAnnotator() {
        init("/res");
    }

    /**
	 * Initialise the underlying annotators
	 * @param pathPrefix the path of the directory containing the 
	 * 		{@code "index_facility"} Lucene index.
	 */
    protected void init(String pathPrefix) {
        if (annotators == null) {
            annotators = new ArrayList<Annotator>();
            annotators.add(new GenreAnnotator());
            annotators.add(new CreditcardAnnotator());
            annotators.add(new PricevalueAnnotator());
            try {
                String indexdir = pathPrefix + "/index_facility";
                if (indexdir != null) {
                    IndexReader sitefacility_reader = IndexReader.open(indexdir);
                    HybridEntityAnnotator tmp = new HybridEntityAnnotator(sitefacility_reader, false, AnnotationPosition.FACILITY);
                    tmp.setMinHybridSimilarity(0.8);
                    tmp.setMinSimilarity(0.8);
                    annotators.add(tmp);
                }
            } catch (IOException e) {
                System.err.println("Could not load the Facility index: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public TreeSet<AnnotationPosition> annotateInstances(String sentence) {
        TreeSet<AnnotationPosition> annotations = new TreeSet<AnnotationPosition>();
        for (Annotator annotator : annotators) annotations.addAll(annotator.annotateInstances(sentence));
        return annotations;
    }
}
