package edu.opexcavator.nlp.ir;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import edu.opexcavator.model.DocumentImpl;
import edu.opexcavator.model.Token;
import edu.opexcavator.model.postype.Adjective;
import edu.opexcavator.model.postype.Noun;
import edu.opexcavator.model.postype.POSType;
import edu.opexcavator.nlp.NLPTestsHelper;
import edu.opexcavator.utils.BeanUtils;

/**
 * @author Jesica N. Fera
 *
 */
public class TestInformationExtractorImpl {

    private InformationExtractorImpl informationExtractor;

    private DocumentImpl theDocument = NLPTestsHelper.createSplittedAndPOSTaggedTestDocument();

    private DocumentImpl anotherDocument = NLPTestsHelper.createSplittedAndPOSTaggedDocument("Esta cámara no me gusto nada. La calidad de las imágenes es lamentable.");

    @Before
    public void setUp() {
        informationExtractor = new InformationExtractorImpl();
    }

    @Test
    public void testGetFrequencyListedWordsByPOS_adjectives() {
        POSType adjective = new Adjective();
        Map<Token, Double> returnedMap = informationExtractor.getFrequencyListedWordsByPOS(adjective, theDocument);
        Assert.assertTrue(!returnedMap.entrySet().isEmpty());
        for (Token token : returnedMap.keySet()) {
            if (token.getFullForm().equals("buena")) {
                return;
            }
        }
        fail();
    }

    @Test
    public void testGetFrequencyOrderedNouns() {
        POSType prototypeNoun = new Noun();
        List<Token> orderedNouns = informationExtractor.getFrequentNounsSorted(prototypeNoun, theDocument);
        Assert.assertTrue(!orderedNouns.isEmpty());
        Assert.assertEquals("cámara", orderedNouns.get(0).getFullForm());
    }

    @Test
    public void testGetFrequencyOrderedNounsLimited() {
        POSType prototypeNoun = new Noun();
        List<Token> orderedNouns = informationExtractor.getFrequentNounsSortedLimited(prototypeNoun, theDocument, 2.0d);
        assertEquals(1, orderedNouns.size());
        assertEquals("cámara", orderedNouns.get(0).getFullForm());
    }

    @Test
    public void testGetFrequencyOrderedNounsLimited_docCollection_repeatedDocs() {
        POSType prototypeNoun = new Noun();
        List<DocumentImpl> documentCollection = new ArrayList<DocumentImpl>();
        documentCollection.add(theDocument);
        DocumentImpl secondDocument = (DocumentImpl) BeanUtils.cloneObject(theDocument);
        documentCollection.add(secondDocument);
        List<Token> orderedNouns = informationExtractor.getFrequentNounsSortedLimited(prototypeNoun, documentCollection, 2.0d);
        assertEquals(1, orderedNouns.size());
        assertEquals("cámara", orderedNouns.get(0).getFullForm());
    }

    @Test
    public void testGetFrequencyOrderedNounsLimited_docCollection_differentDocs() {
        List<DocumentImpl> documentCollection = new ArrayList<DocumentImpl>();
        documentCollection.add(theDocument);
        documentCollection.add(anotherDocument);
        List<Token> orderedNouns = informationExtractor.getFrequentNounsSortedLimited(new Noun(), documentCollection, 2.0d);
        assertEquals(1, orderedNouns.size());
        assertEquals("cámara", orderedNouns.get(0).getFullForm());
    }
}
