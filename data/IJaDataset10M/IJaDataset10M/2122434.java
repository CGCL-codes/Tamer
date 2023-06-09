package com.ibm.uima.examples.opennlp.annotator;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import opennlp.tools.lang.english.TreebankParser;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserME;
import opennlp.tools.util.Span;
import org.apache.uima.analysis_engine.ResultSpecification;
import org.apache.uima.analysis_engine.annotator.AnnotatorConfigurationException;
import org.apache.uima.analysis_engine.annotator.AnnotatorContext;
import org.apache.uima.analysis_engine.annotator.AnnotatorInitializationException;
import org.apache.uima.analysis_engine.annotator.AnnotatorProcessException;
import org.apache.uima.analysis_engine.annotator.JTextAnnotator_ImplBase;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import com.ibm.uima.examples.opennlp.Sentence;
import com.ibm.uima.examples.opennlp.SyntaxAnnotation;
import com.ibm.uima.examples.opennlp.Token;
import org.apache.uima.jcas.JCas;

/**
 * UIMA Analysis Engine that invokes the OpenNLP Parser. The OpenNLP Parser
 * generates PennTreeBank style syntax tags. These tags are mapped into
 * annotation types according to the tag mapping table (MAPPINGS_PARAM)
 * parameter and corresponding annotations are created in the CAS. The directory
 * containing the various model files used by the OpenNLP Parser must also be
 * specified as a parameter (MODEL_DIR_PARAM).
 * 
 */
public class Parser extends JTextAnnotator_ImplBase {

    /** Parse tag mappings array parameter name. */
    private static final String MAPPINGS_PARAM = "ParseTagMappings";

    /** Model directory parameter name. */
    private static final String MODEL_DIR_PARAM = "ModelDirectory";

    /** Use tag dictionary flag parameter name. */
    private static final String USE_TAG_DICT_PARAM = "UseTagDictionary";

    /** Case sensitive tag dictionary flag parameter name. */
    private static final String CASE_INSESNITIVE_TD_PARAM = "CaseSensitiveTagDictionary";

    /** Beam size paramter name. */
    private static final String BEAM_SIZE_PARAM = "BeamSize";

    /** Advance percentage parameter name. */
    private static final String ADV_PERCENT_PARAM = "AdvancePercentage";

    /** Name to use for this Analysis Engine component. */
    private static final String COMPONENT_NAME = "OpenNLP Parser";

    /** The OpenNLP parser */
    private ParserME parser;

    /**
     * Hashtable for characters that must be escaped because they have special
     * meaning for the parser.
     */
    private Hashtable escapeMap = new Hashtable();

    /**
     * Table to keep track of span offsets when characters are escaped. Required to
     * properly set spans in parse annotations.
     */
    private OffsetMap offsetMap = new OffsetMap();

    /**
     * Hash that maps parse tags to the constructor for the corresponding
     * annotation type class.
     */
    private Hashtable parseTagMap = new Hashtable();

    public void initialize(AnnotatorContext aContext) throws AnnotatorInitializationException, AnnotatorConfigurationException {
        super.initialize(aContext);
        try {
            String[] mappingStrings = null;
            mappingStrings = (String[]) aContext.getConfigParameterValue(MAPPINGS_PARAM);
            if (mappingStrings == null) {
                throw new AnnotatorConfigurationException();
            }
            loadMappings(mappingStrings);
            String modelDirName = (String) aContext.getConfigParameterValue(MODEL_DIR_PARAM);
            File modelDir = new File(modelDirName);
            if (!modelDir.isDirectory()) {
                throw new AnnotatorConfigurationException();
            }
            boolean useTagDictionary = false;
            boolean caseSensitiveTagDictionary = false;
            int beamSize = ParserME.defaultBeamSize;
            double advancePercentage = ParserME.defaultAdvancePercentage;
            Boolean useTagDictP = (Boolean) aContext.getConfigParameterValue(USE_TAG_DICT_PARAM);
            if (useTagDictP != null) useTagDictionary = useTagDictP.booleanValue();
            Boolean caseSensitiveTagDictP = (Boolean) aContext.getConfigParameterValue(CASE_INSESNITIVE_TD_PARAM);
            if (caseSensitiveTagDictP != null) caseSensitiveTagDictionary = caseSensitiveTagDictP.booleanValue();
            Integer beamSizeInt = (Integer) aContext.getConfigParameterValue(BEAM_SIZE_PARAM);
            if (beamSizeInt != null) beamSize = beamSizeInt.intValue();
            Float advPercentFlt = (Float) aContext.getConfigParameterValue(ADV_PERCENT_PARAM);
            if (advPercentFlt != null) advancePercentage = advPercentFlt.doubleValue();
            parser = TreebankParser.getParser(modelDirName, useTagDictionary, caseSensitiveTagDictionary, beamSize, advancePercentage);
            parser.setErrorReporting(false);
        } catch (Exception e) {
            throw new AnnotatorInitializationException(e);
        }
        initEscapeMap();
    }

    /**
     * Processes the parse tag mappaings parameter. The constructor for each
     * class identified in the array is loaded and stored in the mapping
     * hashtable, using the label provided in the parameter as the key.
     * 
     * @param mappingStrings
     *            Array of mapping strings of the form "tag,class"
     * @throws AnnotatorConfigurationException
     */
    private void loadMappings(String[] mappingStrings) throws AnnotatorConfigurationException {
        for (int i = 0; i < mappingStrings.length; i++) {
            String[] mappingPair = mappingStrings[i].split(",");
            if (mappingPair.length < 2) throw new AnnotatorConfigurationException();
            String parseTag = mappingPair[0];
            String className = mappingPair[1];
            Constructor annotationConstructor;
            Class annotationClass;
            try {
                annotationClass = Class.forName(className);
                annotationConstructor = annotationClass.getConstructor(new Class[] { JCas.class });
            } catch (Exception e) {
                throw new AnnotatorConfigurationException(e);
            }
            parseTagMap.put(parseTag, annotationConstructor);
        }
    }

    public void process(JCas aJCas, ResultSpecification aResultSpec) throws AnnotatorProcessException {
        try {
            ArrayList wordList = new ArrayList();
            StringBuffer sentenceBuffer = new StringBuffer();
            offsetMap.clear();
            AnnotationIndex sentenceIndex = (AnnotationIndex) aJCas.getJFSIndexRepository().getAnnotationIndex(Sentence.type);
            AnnotationIndex tokenIndex = (AnnotationIndex) aJCas.getJFSIndexRepository().getAnnotationIndex(Token.type);
            FSIterator sentenceIterator = sentenceIndex.iterator();
            while (sentenceIterator.hasNext()) {
                Sentence sentence = (Sentence) sentenceIterator.next();
                wordList.clear();
                sentenceBuffer.setLength(0);
                int mapIdx = 0;
                FSIterator tokenIterator = tokenIndex.subiterator(sentence);
                while (tokenIterator.hasNext()) {
                    Token token = (Token) tokenIterator.next();
                    String word = escapeToken(token.getCoveredText());
                    int start = sentenceBuffer.length();
                    int end = start + word.length();
                    int origIdx = token.getBegin();
                    for (mapIdx = start; mapIdx <= end; mapIdx++) {
                        offsetMap.putMapping(mapIdx, origIdx);
                        if (origIdx < token.getEnd()) origIdx++;
                    }
                    sentenceBuffer.append(word + " ");
                    wordList.add(word);
                }
                if (sentenceBuffer.length() == 0) continue;
                String sentenceText = sentenceBuffer.substring(0, sentenceBuffer.length() - 1);
                Parse parse = new Parse(sentenceText, new Span(0, sentenceText.length()), "INC", 1, null);
                int tokenStart = 0;
                int tokenEnd = 0;
                Iterator wordIterator = wordList.iterator();
                while (wordIterator.hasNext()) {
                    String word = (String) wordIterator.next();
                    tokenEnd = tokenStart + word.length();
                    parse.insert(new Parse(sentenceText, new Span(tokenStart, tokenEnd), ParserME.TOK_NODE, 0));
                    tokenStart = tokenEnd + 1;
                }
                parse = parser.parse(parse);
                makeAnnotations(parse, aJCas, sentence.getSentenceID());
            }
        } catch (Exception e) {
        }
    }

    /**
     * Initializes the table of characters that must be "escaped". These
     * characters have special meaning to the parser, so they are replaced with
     * a special string, which is understood by the parser to represent that
     * character.
     */
    private void initEscapeMap() {
        escapeMap.put("(", "-LRB-");
        escapeMap.put(")", "-RRB-");
        escapeMap.put("{", "-LCB-");
        escapeMap.put("}", "-RCB-");
        escapeMap.put("[", "-LSB-");
        escapeMap.put("]", "-RSB-");
    }

    /**
     * Escape the input token, if necessary. Consult the EscapeMap to see if the
     * input token is a character that must be escaped and, if so, return the
     * escape sequence. Otherwise, return the input token.
     * 
     * @param token
     *            The token to escape.
     * @return If token must be escaped, then the escaped token, otherwise the
     *         original token.
     */
    private String escapeToken(String token) {
        String newToken = (String) escapeMap.get(token);
        if (newToken == null) return token;
        return newToken;
    }

    /**
     * Create the parse annotations in the CAS corresponding to the results of
     * the OpenNLP parse.
     * 
     * @param parse
     *            The parse generated by the OpenNLP parser.
     * @param jCas
     *            The JCas in which to create the annotations.
     * @throws AnnotatorProcessException
     */
    private void makeAnnotations(Parse parse, JCas jCas, int sentenceID) throws AnnotatorProcessException {
        Span span = parse.getSpan();
        String tag = parse.getType();
        if (!tag.equals(ParserME.TOK_NODE)) {
            int start = offsetMap.getMapping(span.getStart());
            int end = offsetMap.getMapping(span.getEnd());
            Constructor annotationMaker = (Constructor) parseTagMap.get(tag);
            if (annotationMaker != null) {
                SyntaxAnnotation syntaxAnnot;
                try {
                    syntaxAnnot = (SyntaxAnnotation) annotationMaker.newInstance(new Object[] { jCas });
                } catch (Exception e) {
                    throw new AnnotatorProcessException(e);
                }
                syntaxAnnot.setBegin(start);
                syntaxAnnot.setEnd(end);
                syntaxAnnot.setComponentId(COMPONENT_NAME);
                syntaxAnnot.setLogProb((float) parse.getProb());
                syntaxAnnot.setSentenceID(sentenceID);
                syntaxAnnot.addToIndexes();
            }
            Parse[] children = parse.getChildren();
            for (int i = 0; i < children.length; i++) {
                makeAnnotations(children[i], jCas, sentenceID);
            }
        }
    }

    public String show(Parse parse) {
        Span span = parse.getSpan();
        if (parse.getType().equals(ParserME.TOK_NODE)) {
            return (parse.getText().substring(span.getStart(), span.getEnd()));
        }
        Parse[] children = parse.getChildren();
        if (children.length == 1) {
            Parse childParse = children[0];
            if (childParse.getType().equals(ParserME.TOK_NODE)) {
                return (show(childParse) + "/" + parse.getType());
            }
        }
        String retVal = "(" + parse.getType() + " ";
        for (int i = 0; i < children.length; i++) {
            retVal += show(children[i]) + " ";
        }
        return (retVal + ")");
    }

    public void printParse(Parse parse, String prefix) {
        System.out.println(prefix + "Label: " + parse.getLabel());
        System.out.println(prefix + "Type: " + parse.getType());
        Span span = parse.getSpan();
        System.out.println(prefix + "Span: " + span.getStart() + ":" + span.getEnd());
        System.out.println(prefix + "Text: " + parse.getText().substring(span.getStart(), span.getEnd()));
        Parse[] children = parse.getChildren();
        for (int i = 0; i < children.length; i++) {
            printParse(children[i], prefix + "  ");
        }
    }

    /**
     * Private class to hold span offset mappings. When the input text contains
     * special characters that must be escaped, the escape sequences are longer
     * than the original text. This table keeps track of modified span offsets
     * so that the results of the parse (performed on the length-modified text)
     * can be mapped back to the original text spans.
     */
    private class OffsetMap extends ArrayList {

        private static final long serialVersionUID = 1L;

        /**
         * Store a span mapping in the table.
         * 
         * @param index
         *            The new offset.
         * @param offset
         *            The original offset.
         */
        public void putMapping(int index, int offset) {
            Integer element = new Integer(offset);
            if (index < size()) {
                set(index, element);
            } else {
                for (int i = size(); i < index; i++) add(null);
                add(element);
            }
        }

        /**
         * Retrieve a span mapping from the table.
         * 
         * @param index
         *            The new offset.
         * @return The original offset.
         */
        public int getMapping(int index) {
            Integer element = (Integer) get(index);
            return element.intValue();
        }
    }
}
