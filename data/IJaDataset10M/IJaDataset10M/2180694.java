package eu.fbk.hlt.common.etaf;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.fbk.hlt.common.etaf package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _EntailmentCorpus_QNAME = new QName("", "entailment-corpus");

    private static final QName _Texts_QNAME = new QName("", "texts");

    private static final QName _Paragraph_QNAME = new QName("", "paragraph");

    private static final QName _Clusters_QNAME = new QName("", "clusters");

    private static final QName _Pair_QNAME = new QName("", "pair");

    private static final QName _Data_QNAME = new QName("", "data");

    private static final QName _Questions_QNAME = new QName("", "questions");

    private static final QName _Collection_QNAME = new QName("", "collection");

    private static final QName _Rules_QNAME = new QName("", "rules");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.fbk.hlt.common.etaf
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EntailmentRulesGroup }
     * 
     */
    public EntailmentRulesGroup createEntailmentRulesGroup() {
        return new EntailmentRulesGroup();
    }

    /**
     * Create an instance of {@link EntailmentRules }
     * 
     */
    public EntailmentRules createEntailmentRules() {
        return new EntailmentRules();
    }

    /**
     * Create an instance of {@link AnswerPart }
     * 
     */
    public AnswerPart createAnswerPart() {
        return new AnswerPart();
    }

    /**
     * Create an instance of {@link RelationCluster }
     * 
     */
    public RelationCluster createRelationCluster() {
        return new RelationCluster();
    }

    /**
     * Create an instance of {@link EntailmentRule }
     * 
     */
    public EntailmentRule createEntailmentRule() {
        return new EntailmentRule();
    }

    /**
     * Create an instance of {@link Sentence }
     * 
     */
    public Sentence createSentence() {
        return new Sentence();
    }

    /**
     * Create an instance of {@link AttributeHolder }
     * 
     */
    public AttributeHolder createAttributeHolder() {
        return new AttributeHolder();
    }

    /**
     * Create an instance of {@link QuestionAnswer }
     * 
     */
    public QuestionAnswer createQuestionAnswer() {
        return new QuestionAnswer();
    }

    /**
     * Create an instance of {@link Document }
     * 
     */
    public Document createDocument() {
        return new Document();
    }

    /**
     * Create an instance of {@link Tree }
     * 
     */
    public Tree createTree() {
        return new Tree();
    }

    /**
     * Create an instance of {@link Paragraph }
     * 
     */
    public Paragraph createParagraph() {
        return new Paragraph();
    }

    /**
     * Create an instance of {@link GroupQuestion }
     * 
     */
    public GroupQuestion createGroupQuestion() {
        return new GroupQuestion();
    }

    /**
     * Create an instance of {@link GroupPattern }
     * 
     */
    public GroupPattern createGroupPattern() {
        return new GroupPattern();
    }

    /**
     * Create an instance of {@link Template }
     * 
     */
    public Template createTemplate() {
        return new Template();
    }

    /**
     * Create an instance of {@link AnnotatedText }
     * 
     */
    public AnnotatedText createAnnotatedText() {
        return new AnnotatedText();
    }

    /**
     * Create an instance of {@link Relation }
     * 
     */
    public Relation createRelation() {
        return new Relation();
    }

    /**
     * Create an instance of {@link AnnotatedTexts }
     * 
     */
    public AnnotatedTexts createAnnotatedTexts() {
        return new AnnotatedTexts();
    }

    /**
     * Create an instance of {@link Attribute }
     * 
     */
    public Attribute createAttribute() {
        return new Attribute();
    }

    /**
     * Create an instance of {@link ProcessedQuestion }
     * 
     */
    public ProcessedQuestion createProcessedQuestion() {
        return new ProcessedQuestion();
    }

    /**
     * Create an instance of {@link RelationClusters }
     * 
     */
    public RelationClusters createRelationClusters() {
        return new RelationClusters();
    }

    /**
     * Create an instance of {@link Semantics }
     * 
     */
    public Semantics createSemantics() {
        return new Semantics();
    }

    /**
     * Create an instance of {@link Edge }
     * 
     */
    public Edge createEdge() {
        return new Edge();
    }

    /**
     * Create an instance of {@link Node }
     * 
     */
    public Node createNode() {
        return new Node();
    }

    /**
     * Create an instance of {@link TextObjects }
     * 
     */
    public TextObjects createTextObjects() {
        return new TextObjects();
    }

    /**
     * Create an instance of {@link QuestionCollection }
     * 
     */
    public QuestionCollection createQuestionCollection() {
        return new QuestionCollection();
    }

    /**
     * Create an instance of {@link Word }
     * 
     */
    public Word createWord() {
        return new Word();
    }

    /**
     * Create an instance of {@link Entity }
     * 
     */
    public Entity createEntity() {
        return new Entity();
    }

    /**
     * Create an instance of {@link Exportable }
     * 
     */
    public Exportable createExportable() {
        return new Exportable();
    }

    /**
     * Create an instance of {@link EntailmentCorpus }
     * 
     */
    public EntailmentCorpus createEntailmentCorpus() {
        return new EntailmentCorpus();
    }

    /**
     * Create an instance of {@link EntailmentPair }
     * 
     */
    public EntailmentPair createEntailmentPair() {
        return new EntailmentPair();
    }

    /**
     * Create an instance of {@link TextCollection }
     * 
     */
    public TextCollection createTextCollection() {
        return new TextCollection();
    }

    /**
     * Create an instance of {@link QuestionAnalysis }
     * 
     */
    public QuestionAnalysis createQuestionAnalysis() {
        return new QuestionAnalysis();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntailmentCorpus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "entailment-corpus")
    public JAXBElement<EntailmentCorpus> createEntailmentCorpus(EntailmentCorpus value) {
        return new JAXBElement<EntailmentCorpus>(_EntailmentCorpus_QNAME, EntailmentCorpus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnnotatedTexts }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "texts")
    public JAXBElement<AnnotatedTexts> createTexts(AnnotatedTexts value) {
        return new JAXBElement<AnnotatedTexts>(_Texts_QNAME, AnnotatedTexts.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Paragraph }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "paragraph")
    public JAXBElement<Paragraph> createParagraph(Paragraph value) {
        return new JAXBElement<Paragraph>(_Paragraph_QNAME, Paragraph.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RelationClusters }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "clusters")
    public JAXBElement<RelationClusters> createClusters(RelationClusters value) {
        return new JAXBElement<RelationClusters>(_Clusters_QNAME, RelationClusters.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntailmentPair }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pair")
    public JAXBElement<EntailmentPair> createPair(EntailmentPair value) {
        return new JAXBElement<EntailmentPair>(_Pair_QNAME, EntailmentPair.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exportable }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "data")
    public JAXBElement<Exportable> createData(Exportable value) {
        return new JAXBElement<Exportable>(_Data_QNAME, Exportable.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QuestionCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "questions")
    public JAXBElement<QuestionCollection> createQuestions(QuestionCollection value) {
        return new JAXBElement<QuestionCollection>(_Questions_QNAME, QuestionCollection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TextCollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "collection")
    public JAXBElement<TextCollection> createCollection(TextCollection value) {
        return new JAXBElement<TextCollection>(_Collection_QNAME, TextCollection.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntailmentRules }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "rules")
    public JAXBElement<EntailmentRules> createRules(EntailmentRules value) {
        return new JAXBElement<EntailmentRules>(_Rules_QNAME, EntailmentRules.class, null, value);
    }
}
