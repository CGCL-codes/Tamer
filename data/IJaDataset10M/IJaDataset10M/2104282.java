package edu.ucdavis.cs.dblp.data.keywords;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.Dictionary;
import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.ExactDictionaryChunker;
import com.aliasi.dict.TrieDictionary;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.google.common.base.Function;
import com.google.common.base.Join;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import edu.ucdavis.cs.dblp.ServiceLocator;
import edu.ucdavis.cs.dblp.analyzers.TokenizerService;
import edu.ucdavis.cs.dblp.data.DblpKeywordDao;
import edu.ucdavis.cs.dblp.data.Keyword;
import edu.ucdavis.cs.dblp.data.Publication;
import edu.ucdavis.cs.dblp.data.PublicationContent;
import edu.ucdavis.cs.dblp.experts.SearchService;
import edu.ucdavis.cs.dblp.text.SimplePub;

/**
 * @author pfishero
 *
 */
@Service("keywordRecognizer")
@Transactional(propagation = Propagation.REQUIRED)
public class SimpleKeywordRecognizer implements KeywordRecognizer {

    private static final Logger logger = Logger.getLogger(SimpleKeywordRecognizer.class);

    private static final String TYPE = "DBLP_KEYWORD";

    private Dictionary<String> keywordDict;

    private DblpKeywordDao dao;

    private TokenizerService tokenizer;

    private BiMap<String, String> acronymMap = Maps.newHashBiMap();

    private static final Map<String, Keyword> KW_CACHE = Maps.newConcurrentHashMap();

    @Resource
    private SearchService searchService;

    @PostConstruct
    public void populateKeywordDictionary() {
        Set<String> stopAcronyms = Sets.newHashSet("IT", "USE", "IM", "ITS", "ROD", "NP", "AS", "MR", "TE", "OU", "AN", "AD", "AND", "THE", "KEYWORD", "PAPER", "CAN", "TWO");
        if (keywordDict == null) {
            Preconditions.checkState(dao != null, "keyword dao must be set prior to populating keyword dictonary");
            logger.info("populating keyword dictonary");
            keywordDict = new TrieDictionary<String>();
            final double score = 0.0;
            Multiset<Integer> wordCounts = new HashMultiset<Integer>();
            Pattern trailingAcronym = Pattern.compile("^([^()]+)\\(([A-Z][a-zA-Z/-]+)\\)$");
            for (Keyword keyword : Sets.newHashSet(dao.findAll())) {
                keyword.setKeyword(keyword.getKeyword().replace("&amp;", "&"));
                keyword.setKeyword(keyword.getKeyword().replace("&amp", "&"));
                keyword.setKeyword(keyword.getKeyword().replaceAll("&[^\\s]+", ""));
                keyword.setKeyword(keyword.getKeyword().replaceAll("\\s+", " "));
                Matcher m = trailingAcronym.matcher(keyword.getKeyword());
                if (m.matches()) {
                    if (m.groupCount() == 2) {
                        String expandedForm;
                        String acronym;
                        String captureOne = m.group(1).trim();
                        String captureTwo = m.group(2).trim();
                        if (captureOne.length() <= captureTwo.length()) {
                            acronym = captureOne;
                            expandedForm = captureTwo;
                        } else {
                            acronym = captureTwo;
                            expandedForm = captureOne;
                        }
                        assert acronym.length() <= expandedForm.length();
                        if (!stopAcronyms.contains(acronym.toUpperCase().trim())) {
                            logger.debug("acronym " + acronym + " = " + expandedForm);
                            if (!acronymMap.containsKey(acronym) && !acronymMap.containsValue(acronym) && !acronymMap.containsKey(expandedForm) && !acronymMap.containsValue(expandedForm)) {
                                acronymMap.put(acronym, expandedForm);
                            } else {
                                logger.info("not inserting existing key into acronymMap.  key=" + acronym);
                            }
                            addKwToDict(score, expandedForm);
                            addKwToDict(score, acronym);
                        }
                    } else {
                        logger.error("trailing acronym regex didn't work!!");
                    }
                } else {
                    if (keyword.getKeyword().length() > 2 && !stopAcronyms.contains(keyword.getKeyword().toUpperCase().trim())) {
                        addKwToDict(score, keyword.getKeyword());
                        if (logger.isDebugEnabled()) {
                            String[] words = keyword.getKeyword().split("\\s+");
                            wordCounts.add(words.length);
                            if (words.length > 5) {
                                logger.debug(">5 words = " + Join.join(" ", words));
                            }
                        }
                    }
                }
            }
            logger.debug("populated dictionary with " + keywordDict.size() + " keywords");
            if (logger.isDebugEnabled()) {
                for (Integer wordNum : wordCounts.elementSet()) {
                    logger.debug("keywords with " + wordNum + " word(s) = " + wordCounts.count(wordNum));
                }
            }
        } else {
            for (String excludedAcronym : stopAcronyms) {
                if (acronymMap.containsKey(excludedAcronym)) {
                    logger.debug("removing excluded acronym " + excludedAcronym + " from serialized acronym map");
                    acronymMap.remove(excludedAcronym);
                } else if (acronymMap.containsValue(excludedAcronym)) {
                    logger.debug("removing excluded acronym " + excludedAcronym + " from serialized acronym map");
                    acronymMap.inverse().remove(excludedAcronym);
                }
            }
        }
    }

    /**
	 * @param score
	 * @param keyword
	 */
    private void addKwToDict(final double score, String keyword) {
        keywordDict.addEntry(new DictionaryEntry<String>(keyword.trim(), TYPE, score));
        logger.trace("adding keyword to dictionary: " + keyword);
    }

    @PostConstruct
    public void initTokenizerService() {
        tokenizer = new TokenizerService();
    }

    @PostConstruct
    public void oneOffAcronymFixes() {
        if (!acronymMap.get("XML").toString().equalsIgnoreCase("eXtensible Markup Language")) {
            acronymMap.put("XML", "eXtensible Markup Language");
        }
        if (acronymMap.containsKey("O") && acronymMap.get("O").toString().equalsIgnoreCase("NlogN")) {
            acronymMap.remove("O");
        }
    }

    /**
	 * @param dao the Keyword DAO to set
	 */
    @Resource
    public void setDao(DblpKeywordDao dao) {
        this.dao = dao;
    }

    @Autowired(required = false)
    public void setSerializedKeywordDict(@Qualifier("serKeywordDict") org.springframework.core.io.Resource serKeywordDict) {
        try {
            logger.info("loading serialized version of keywordDict");
            ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(serKeywordDict.getInputStream()));
            this.keywordDict = (Dictionary<String>) ois.readObject();
            ois.close();
        } catch (IOException e) {
            logger.error("error while loading keywordDict: " + e);
        } catch (ClassNotFoundException cnfe) {
            logger.error("error while loading keywordDict: " + cnfe);
        }
    }

    @Autowired(required = false)
    public void setSerializedAcronymMap(@Qualifier("serAcronymMap") org.springframework.core.io.Resource serAcronymMap) {
        try {
            logger.info("loading serialized version of acronymMap");
            ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(serAcronymMap.getInputStream()));
            this.acronymMap = (BiMap<String, String>) ois.readObject();
            ois.close();
        } catch (IOException e) {
            logger.error("error while loading acronymMap: " + e);
        } catch (ClassNotFoundException cnfe) {
            logger.error("error while loading acronymMap: " + cnfe);
        }
    }

    private static final Comparator<Keyword> LENGTH_COMPARATOR = new Comparator<Keyword>() {

        public int compare(Keyword kw1, Keyword kw2) {
            return kw2.getKeyword().length() - kw1.getKeyword().length();
        }

        ;
    };

    @Override
    public Set<Keyword> findKeywordsIn(String text) {
        Preconditions.checkArgument(text != null);
        Set<Keyword> foundKeywords = Sets.newHashSet();
        Chunker chunker = new ExactDictionaryChunker(keywordDict, IndoEuropeanTokenizerFactory.FACTORY, true, false);
        Chunking chunking = chunker.chunk(text);
        String theText = chunking.charSequence().toString();
        List<Chunk> chunks = Lists.newArrayList(chunking.chunkSet());
        for (Chunk chunk : chunks) {
            int start = chunk.start();
            int end = chunk.end();
            String chunkText = theText.substring(start, end).trim();
            chunkText = chunkText.replaceAll("\\s+", " ");
            Keyword foundKeyword = new Keyword(chunkText);
            if (!KW_CACHE.containsKey(foundKeyword.toString().toUpperCase())) {
                KW_CACHE.put(foundKeyword.toString().toUpperCase(), foundKeyword);
            }
            foundKeyword = KW_CACHE.get(foundKeyword.toString().toUpperCase());
            foundKeywords.add(foundKeyword);
        }
        foundKeywords = disambiguateKeywords(foundKeywords);
        if (logger.isDebugEnabled()) {
            logger.debug("found " + foundKeywords.size() + " keywords: \n" + Join.join("\n", foundKeywords));
        }
        return foundKeywords;
    }

    private static final Function<Keyword, String> FN_KEYWORD_NAME = new Function<Keyword, String>() {

        @Override
        public String apply(Keyword keyword) {
            return keyword.getKeyword();
        }
    };

    private static final Function<String, Keyword> FN_KEYWORD_FROM_STR = new Function<String, Keyword>() {

        @Override
        public Keyword apply(String arg0) {
            Keyword foundKeyword = new Keyword(arg0);
            if (!KW_CACHE.containsKey(foundKeyword.toString().toUpperCase())) {
                KW_CACHE.put(foundKeyword.toString().toUpperCase(), foundKeyword);
            }
            foundKeyword = KW_CACHE.get(foundKeyword.toString().toUpperCase());
            return foundKeyword;
        }
    };

    private static final int DOC_FREQ_CUTOFF = 1000;

    private Set<Keyword> disambiguateKeywords(Set<Keyword> keywords) {
        List<String> keywordsList = Lists.newArrayList(Iterables.transform(keywords, FN_KEYWORD_NAME));
        Collections.sort(keywordsList, String.CASE_INSENSITIVE_ORDER);
        keywordsList = reduceKeywords(keywordsList);
        Set<Keyword> mungedKeywords = Sets.newHashSet(Iterables.transform(keywordsList, FN_KEYWORD_FROM_STR));
        return mungedKeywords;
    }

    private final Map<String, Keyword> canonicalForms = Maps.newHashMap();

    /**
	 * Removes duplicates (stemmed form) and removes keywords that are contained within 
	 * other keywords, for example, 'spatial', 'spatial database', 'spatial databases' would 
	 * be reduced to 'spatial databases'.
	 * 
	 * @param keywords
	 * @return
	 */
    public List<String> reduceKeywords(List<String> keywords) {
        List<String> keywordsList = Lists.newArrayList();
        Set<String> expandedKeywordsSet = Sets.newLinkedHashSet();
        Iterables.addAll(expandedKeywordsSet, Iterables.transform(keywords, new Function<String, String>() {

            @Override
            public String apply(String keyword) {
                if (SimpleKeywordRecognizer.this.getAcronymMap().containsKey(keyword)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("expanding " + keyword + " to " + SimpleKeywordRecognizer.this.getAcronymMap().get(keyword));
                    }
                    return SimpleKeywordRecognizer.this.getAcronymMap().get(keyword);
                } else {
                    return keyword;
                }
            }
        }));
        List<String> expandedKeywordsList = Lists.newLinkedList(expandedKeywordsSet);
        Collections.sort(expandedKeywordsList, String.CASE_INSENSITIVE_ORDER);
        Map<String, String> kwCanonicalMap = Maps.newHashMap();
        canonicalForms.clear();
        for (String keyword : expandedKeywordsList) {
            kwCanonicalMap.put(keyword, keyword);
            canonicalForms.put(keyword.toUpperCase(), new Keyword(keyword));
            if (acronymMap.inverse().containsKey(keyword)) {
                canonicalForms.put(acronymMap.inverse().get(keyword).toUpperCase(), new Keyword(keyword));
            }
            String cleanedKeyword = keyword.replaceAll("\\([A-Z]+\\)$", "").replaceAll("-", " ").trim();
            for (String otherKeyword : expandedKeywordsList) {
                String cleanedOtherKeyword = otherKeyword.replaceAll("\\([A-Z]+\\)$", "").replaceAll("-", " ").trim();
                if (keyword != otherKeyword) {
                    String otherKwStemLc = tokenizer.stemAllTokens(cleanedOtherKeyword.toLowerCase());
                    String kwStemLc = tokenizer.stemAllTokens(cleanedKeyword.toLowerCase());
                    if (otherKwStemLc.equals(kwStemLc)) {
                        if (cleanedOtherKeyword.length() > cleanedKeyword.length() || (cleanedOtherKeyword.length() == cleanedKeyword.length() && cleanedOtherKeyword.hashCode() >= cleanedKeyword.hashCode())) {
                            kwCanonicalMap.put(keyword, otherKeyword);
                            canonicalForms.put(keyword.toUpperCase(), new Keyword(otherKeyword));
                            if (acronymMap.inverse().containsKey(keyword)) {
                                canonicalForms.put(acronymMap.inverse().get(keyword).toUpperCase(), new Keyword(otherKeyword));
                            }
                        }
                    }
                }
            }
        }
        Iterables.addAll(keywordsList, Sets.newHashSet(kwCanonicalMap.values()));
        return keywordsList;
    }

    @Override
    public Collection<Publication> removeLowInformationKeywords(Collection<Publication> pubs) {
        for (Publication pub : pubs) {
            pub.getContent().setKeywords(Sets.newHashSet(removeLowInformationKeywords(pub.getContent().getKeywords())));
        }
        return pubs;
    }

    @Override
    public List<Keyword> removeLowInformationKeywords(Iterable<Keyword> keywords) {
        List<Keyword> refinedKeywords = Lists.newLinkedList(keywords);
        for (Iterator<Keyword> iter = refinedKeywords.iterator(); iter.hasNext(); ) {
            Keyword kw = iter.next();
            if (kw.getKeyword().length() >= 2 && !Character.isLowerCase(kw.getKeyword().charAt(1))) {
                logger.debug("keeping special case keyword: " + kw.getKeyword());
            } else {
                String kwTemp = kw.getKeyword().replace("_", " ").replace("-", " ").replace("\\", " ").replace("/", " ");
                String[] tokens = kwTemp.split("\\s+");
                boolean validFirstTwoChars = kwTemp.length() >= 2 && !Character.isLowerCase(kwTemp.charAt(1));
                int docFreq = searchService.getDocFrequency(kwTemp);
                if ((tokens.length < 2) && !validFirstTwoChars && docFreq > DOC_FREQ_CUTOFF) {
                    logger.debug("removing non-informative keyword: " + kwTemp + " (came from " + kw.getKeyword() + ')' + ',' + docFreq);
                    iter.remove();
                }
            }
        }
        return refinedKeywords;
    }

    public Iterable<SimplePub> produceSimpleControlledVocabulary(Iterable<SimplePub> pubs) {
        Set<Keyword> reducedKeywords = Sets.newHashSet(removeLowInformationKeywords(findKeywordsIn(Join.join(" ", Iterables.transform(pubs, SimplePub.FN_SIMPLPUB_KEYWORDS)))));
        logger.debug("after reduction - all pubs keyword count=" + reducedKeywords.size());
        if (logger.isDebugEnabled()) {
            logger.debug("reduced keywords: " + Join.join("\n", reducedKeywords));
        }
        for (SimplePub pub : pubs) {
            Set<Keyword> updatedKeywords = Sets.newHashSet();
            for (Keyword kw : pub.getKeywords()) {
                if (reducedKeywords.contains(canonicalForms.get(kw.getKeyword().toUpperCase()))) {
                    updatedKeywords.add(canonicalForms.get(kw.getKeyword().toUpperCase()));
                } else {
                    logger.debug("ignoring keyword not found in the controlled vocabulary: " + kw);
                }
            }
            pub.setKeywords(updatedKeywords);
            logger.debug("pub " + pub.getKey() + " now has " + pub.getKeywords().size() + " keyphrases");
        }
        return pubs;
    }

    @Override
    public Iterable<Publication> produceControlledVocabulary(Iterable<Publication> pubs) {
        Set<Keyword> keywords = Sets.newHashSet();
        for (Publication pub : pubs) {
            StringBuilder rawText = new StringBuilder();
            rawText.append(pub.getTitle()).append(" ");
            if (pub.getContent() != null) {
                rawText.append(pub.getContent().getAbstractText()).append(" ");
                rawText.append(Join.join(" ", pub.getContent().getKeywords())).append(" ");
            } else {
                pub.setContent(new PublicationContent());
            }
            final Set<Keyword> pubKws = findKeywordsIn(rawText.toString().trim());
            pub.getContent().setKeywords(pubKws);
        }
        return pubs;
    }

    @Override
    public BiMap<String, String> getAcronymMap() {
        return acronymMap;
    }

    public Dictionary<String> getKeywordDict() {
        return keywordDict;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = ServiceLocator.getInstance().getAppContext();
        KeywordRecognizer recognizer = (KeywordRecognizer) ctx.getBean("keywordRecognizer");
        File outputDir = new File("serialized");
        if (!outputDir.exists()) {
            logger.info("creating serialized output directory " + outputDir.toURI());
            outputDir.mkdir();
        }
        Dictionary<String> keywordDict = recognizer.getKeywordDict();
        BiMap<String, String> acronymMap = recognizer.getAcronymMap();
        ObjectOutputStream oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(new File(outputDir, "keywordDict"))));
        oos.writeObject(keywordDict);
        oos.close();
        oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(new File(outputDir, "acronymMap"))));
        oos.writeObject(acronymMap);
        oos.close();
    }
}
