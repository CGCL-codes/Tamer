package uk.ac.shef.oak.iracema.rune;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import uk.ac.shef.oak.iracema.Iracema;
import uk.ac.shef.oak.iracema.document.Document;
import uk.ac.shef.oak.iracema.features.token.StringFeature;
import uk.ac.shef.oak.iracema.features.token.TokenFeature;
import uk.ac.shef.oak.iracema.filters.FilterUtils;
import uk.ac.shef.oak.iracema.filters.boundary.BoundaryFilter;
import uk.ac.shef.oak.iracema.filters.boundary.TagBoundaryFilter;
import uk.ac.shef.oak.iracema.filters.window.StartEndTagWindowFilter;
import uk.ac.shef.oak.iracema.filters.window.WindowFilter;
import uk.ac.shef.oak.iracema.instance.TextInstance;
import uk.ac.shef.oak.iracema.utils.IracemaTestsUtils;
import uk.ac.shef.wit.runes.Runes;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionCannotHandle;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchContent;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchStructure;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionRuneExecution;
import uk.ac.shef.wit.runes.rune.RuneFiller;
import uk.ac.shef.wit.runes.rune.RuneProxy;
import uk.ac.shef.wit.runes.rune.nlp.RuneTextAnnotationsExtractor;
import uk.ac.shef.wit.runes.rune.nlp.RuneTextTokeniser;
import uk.ac.shef.wit.runes.rune.nlp.RuneTextUntagger;
import uk.ac.shef.wit.text.tokeniser.Tokeniser;
import uk.ac.shef.wit.text.tokeniser.TokeniserOpenNLP;
import junit.framework.TestCase;

public class RuneTokenInstanceFactoryL1Tests extends TestCase {

    private TextInstance m_dr;

    private TextInstance m_john;

    private TextInstance m_smith;

    private TextInstance m_has;

    private TextInstance m_come;

    private TextInstance m_all;

    private TextInstance m_the;

    private TextInstance m_way;

    private TextInstance m_from;

    private TextInstance m_south;

    private TextInstance m_africa;

    private TextInstance m_to;

    private TextInstance m_study;

    private TextInstance m_information;

    private TextInstance m_extraction;

    private TextInstance m_in;

    private TextInstance m_england;

    private TextInstance m_dot;

    private HashMap<String, List<TextInstance>> m_positiveInstances = new HashMap<String, List<TextInstance>>();

    private HashMap<String, List<TextInstance>> m_negativeInstances = new HashMap<String, List<TextInstance>>();

    protected void setUp() {
        List<TextInstance> all = new ArrayList<TextInstance>();
        m_dr = new TextInstance();
        m_dr.setStartOffset(1);
        m_dr.setEndOffset(4);
        m_dr.setValue("String_0", "String_0_Dr.");
        m_dr.setValue("String_+1", "String_+1_John");
        all.add(m_dr);
        m_john = new TextInstance();
        m_john.setStartOffset(5);
        m_john.setEndOffset(9);
        m_john.setValue("String_0", "String_0_John");
        m_john.setValue("String_-1", "String_-1_Dr.");
        m_john.setValue("String_+1", "String_+1_Smith");
        all.add(m_john);
        m_smith = new TextInstance();
        m_smith.setStartOffset(10);
        m_smith.setEndOffset(15);
        m_smith.setValue("String_0", "String_0_Smith");
        m_smith.setValue("String_-1", "String_-1_John");
        m_smith.setValue("String_+1", "String_+1_has");
        all.add(m_smith);
        m_has = new TextInstance();
        m_has.setStartOffset(16);
        m_has.setEndOffset(19);
        m_has.setValue("String_0", "String_0_has");
        m_has.setValue("String_-1", "String_-1_Smith");
        m_has.setValue("String_+1", "String_+1_come");
        all.add(m_has);
        m_come = new TextInstance();
        m_come.setStartOffset(20);
        m_come.setEndOffset(24);
        m_come.setValue("String_0", "String_0_come");
        m_come.setValue("String_-1", "String_-1_has");
        m_come.setValue("String_+1", "String_+1_all");
        all.add(m_come);
        m_all = new TextInstance();
        m_all.setStartOffset(25);
        m_all.setEndOffset(28);
        m_all.setValue("String_0", "String_0_all");
        m_all.setValue("String_-1", "String_-1_come");
        m_all.setValue("String_+1", "String_+1_the");
        all.add(m_all);
        m_the = new TextInstance();
        m_the.setStartOffset(29);
        m_the.setEndOffset(32);
        m_the.setValue("String_0", "String_0_the");
        m_the.setValue("String_-1", "String_-1_all");
        m_the.setValue("String_+1", "String_+1_way");
        all.add(m_the);
        m_way = new TextInstance();
        m_way.setStartOffset(33);
        m_way.setEndOffset(36);
        m_way.setValue("String_0", "String_0_way");
        m_way.setValue("String_-1", "String_-1_the");
        m_way.setValue("String_+1", "String_+1_from");
        all.add(m_way);
        m_from = new TextInstance();
        m_from.setStartOffset(37);
        m_from.setEndOffset(41);
        m_from.setValue("String_0", "String_0_from");
        m_from.setValue("String_-1", "String_-1_way");
        m_from.setValue("String_+1", "String_+1_South");
        all.add(m_from);
        m_south = new TextInstance();
        m_south.setStartOffset(42);
        m_south.setEndOffset(47);
        m_south.setValue("String_0", "String_0_South");
        m_south.setValue("String_-1", "String_-1_from");
        m_south.setValue("String_+1", "String_+1_Africa");
        all.add(m_south);
        m_africa = new TextInstance();
        m_africa.setStartOffset(48);
        m_africa.setEndOffset(54);
        m_africa.setValue("String_0", "String_0_Africa");
        m_africa.setValue("String_-1", "String_-1_South");
        m_africa.setValue("String_+1", "String_+1_to");
        all.add(m_africa);
        m_to = new TextInstance();
        m_to.setStartOffset(55);
        m_to.setEndOffset(57);
        m_to.setValue("String_0", "String_0_to");
        m_to.setValue("String_-1", "String_-1_Africa");
        m_to.setValue("String_+1", "String_+1_study");
        all.add(m_to);
        m_study = new TextInstance();
        m_study.setStartOffset(58);
        m_study.setEndOffset(63);
        m_study.setValue("String_0", "String_0_study");
        m_study.setValue("String_-1", "String_-1_to");
        m_study.setValue("String_+1", "String_+1_Information");
        all.add(m_study);
        m_information = new TextInstance();
        m_information.setStartOffset(64);
        m_information.setEndOffset(75);
        m_information.setValue("String_0", "String_0_Information");
        m_information.setValue("String_-1", "String_-1_study");
        m_information.setValue("String_+1", "String_+1_Extraction");
        all.add(m_information);
        m_extraction = new TextInstance();
        m_extraction.setStartOffset(76);
        m_extraction.setEndOffset(86);
        m_extraction.setValue("String_0", "String_0_Extraction");
        m_extraction.setValue("String_-1", "String_-1_Information");
        m_extraction.setValue("String_+1", "String_+1_in");
        all.add(m_extraction);
        m_in = new TextInstance();
        m_in.setStartOffset(87);
        m_in.setEndOffset(89);
        m_in.setValue("String_0", "String_0_in");
        m_in.setValue("String_-1", "String_-1_Extraction");
        m_in.setValue("String_+1", "String_+1_England");
        all.add(m_in);
        m_england = new TextInstance();
        m_england.setStartOffset(90);
        m_england.setEndOffset(97);
        m_england.setValue("String_0", "String_0_England");
        m_england.setValue("String_-1", "String_-1_in");
        m_england.setValue("String_+1", "String_+1_.");
        all.add(m_england);
        m_dot = new TextInstance();
        m_dot.setStartOffset(97);
        m_dot.setEndOffset(98);
        m_dot.setValue("String_0", "String_0_.");
        m_dot.setValue("String_-1", "String_-1_England");
        all.add(m_dot);
        List<TextInstance> personStartPositive = new ArrayList<TextInstance>();
        personStartPositive.add(m_john);
        List<TextInstance> personEndPositive = new ArrayList<TextInstance>();
        personEndPositive.add(m_smith);
        List<TextInstance> locationStartPositive = new ArrayList<TextInstance>();
        locationStartPositive.add(m_south);
        locationStartPositive.add(m_england);
        List<TextInstance> locationEndPositive = new ArrayList<TextInstance>();
        locationEndPositive.add(m_africa);
        locationEndPositive.add(m_england);
        m_positiveInstances.put("start > person_identifier", personStartPositive);
        m_positiveInstances.put("end > person_identifier", personEndPositive);
        m_positiveInstances.put("start > locational_identifier", locationStartPositive);
        m_positiveInstances.put("end > locational_identifier", locationEndPositive);
        List<TextInstance> personStartNegative = new ArrayList<TextInstance>(all);
        personStartNegative.remove(m_john);
        List<TextInstance> personEndNegative = new ArrayList<TextInstance>(all);
        personEndNegative.remove(m_smith);
        List<TextInstance> locationStartNegative = new ArrayList<TextInstance>(all);
        locationStartNegative.remove(m_south);
        locationStartNegative.remove(m_england);
        List<TextInstance> locationEndNegative = new ArrayList<TextInstance>(all);
        locationEndNegative.remove(m_africa);
        locationEndNegative.remove(m_england);
        m_negativeInstances.put("start > person_identifier", personStartNegative);
        m_negativeInstances.put("end > person_identifier", personEndNegative);
        m_negativeInstances.put("start > locational_identifier", locationStartNegative);
        m_negativeInstances.put("end > locational_identifier", locationEndNegative);
    }

    public void testTokenInstanceFactory() {
        Document document = IracemaTestsUtils.getAktiveMediaDocument();
        assertNotNull(document);
        Set<BoundaryFilter> boundaryFilters = FilterUtils.getBoundaryFilterSet(new TagBoundaryFilter("Person_Identifier", TagBoundaryFilter.f_START_TAG_BOUNDARY_FILTER), new TagBoundaryFilter("Person_Identifier", TagBoundaryFilter.f_END_TAG_BOUNDARY_FILTER), new TagBoundaryFilter("Locational_Identifier", TagBoundaryFilter.f_START_TAG_BOUNDARY_FILTER), new TagBoundaryFilter("Locational_Identifier", TagBoundaryFilter.f_END_TAG_BOUNDARY_FILTER));
        HashSet<TokenFeature> tokenFeatures = new HashSet<TokenFeature>();
        tokenFeatures.add(new StringFeature());
        RuneTokenInstanceFactoryL1 instanceFactory = new RuneTokenInstanceFactoryL1(1, 1, boundaryFilters, tokenFeatures, true, false);
        RuneProxy instanceFactoryProxy = new RuneProxy(instanceFactory, "untagged_document");
        try {
            Runes.carve(new RuneFiller<Document[]>("iracema_documents", new Document[] { document }), new RuneFiller<Boolean>("use_lowercase_words", false), new RuneFiller<Boolean>("remove_stop_words", false), new RuneFiller<Boolean>("remove_punctuation", false), new RuneProxy(new RuneLoadDocuments(), "document"), new RuneProxy(new RuneTextAnnotationsExtractor(), "document"), new RuneProxy(new RuneTextUntagger(), "document"), new RuneFiller<Tokeniser>("tokeniser", new TokeniserOpenNLP(Iracema.s_OPEN_NLP_PATH)), new RuneProxy(new RuneTextTokeniser(), "untagged_document"), instanceFactoryProxy);
            HashMap<String, List<TextInstance>> positiveInstances = instanceFactory.getPositiveInstances();
            IracemaTestsUtils.compareMaps(m_positiveInstances, positiveInstances);
            HashMap<String, List<TextInstance>> negativeInstances = instanceFactory.getNegativeInstances();
            IracemaTestsUtils.compareMaps(m_negativeInstances, negativeInstances);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
