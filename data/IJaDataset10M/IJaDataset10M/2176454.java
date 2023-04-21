package org.exteca.categorisation;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.exteca.categorisation.xml.SAXDocumentParser;
import org.exteca.language.LanguageException;
import org.exteca.language.Marker;
import org.exteca.language.MorphemeProcessor;
import org.exteca.language.Morpher;
import org.exteca.language.Tokeniser;
import org.exteca.language.markers.SentenceMarker;
import org.exteca.language.mealy.MealyRulesReader;
import org.exteca.language.mealy.MealyTokeniser;
import org.exteca.language.morphers.AfixStemmer;
import org.exteca.language.morphers.Caser;
import org.exteca.language.morphers.StemRulesReader;
import org.exteca.pattern.RuleLoader;

/**
 * Creates CategorisationEngine based on configurable input
 * 
 * @author Mauro Talevi
 */
public class CategorisationEngineFactory {

    private Reader tokeniserRules;

    private Map stemmerRules;

    /**
	 * Creates a CategorisationEngineFactory
	 */
    public CategorisationEngineFactory() {
        this.tokeniserRules = getReader("/language/english.tok");
        this.stemmerRules = new HashMap();
        stemmerRules.put("stem", getReader("/language/english.stm"));
        stemmerRules.put("plural", getReader("/language/englishplural.stm"));
    }

    /**
	 * Creates a CategorisationEngineFactory
	 * 
	 * @param tokeniserRules the Reader of the tokeniser rules
	 * @param stemmerRules a Map of Reader of the stemmer rules, indexed by the
	 *            stemmer name
	 */
    public CategorisationEngineFactory(Reader tokeniserRules, Map stemmerRules) {
        this.tokeniserRules = tokeniserRules;
        this.stemmerRules = stemmerRules;
    }

    /**
	 * Creates a CategorisationEngine
	 * 
	 * @return A CategorisationEngine
	 * @throws CategorisationException if fails to create engine components
	 */
    public CategorisationEngine createCategorisationEngine() throws CategorisationException {
        try {
            Tokeniser tokeniser = createTokeniser(tokeniserRules);
            MarkupProcessor markupProcessor = createMarkupProcessor();
            MorphemeProcessor morphemeProcessor = createMorphemeProcessor(stemmerRules);
            RuleLoader ruleLoader = new RuleLoader(morphemeProcessor, tokeniser);
            RuleProcessor ruleProcessor = new RuleProcessor(morphemeProcessor, ruleLoader);
            return new CategorisationEngine(new SAXDocumentParser(tokeniser), markupProcessor, ruleProcessor);
        } catch (LanguageException e) {
            throw new CategorisationException("Failed to create engine components", e);
        }
    }

    /**
	 * Creates a FSM tokeniser with the specified rules
	 * 
	 * @return
	 */
    private Tokeniser createTokeniser(Reader tokeniserRules) throws LanguageException {
        MealyRulesReader reader = new MealyRulesReader();
        reader.read(tokeniserRules);
        return new MealyTokeniser(reader);
    }

    /**
	 * Creates MarkupProcessor with a sentence marker
	 * 
	 * @return
	 */
    private MarkupProcessor createMarkupProcessor() {
        SentenceMarker sentenceMarker = new SentenceMarker();
        return new MarkupProcessor(new Marker[] { sentenceMarker });
    }

    /**
	 * Create MorphemeProcessor with the specified stemmer rules
	 * 
	 * @return
	 */
    private MorphemeProcessor createMorphemeProcessor(Map stemmerRules) throws LanguageException {
        List morphers = new ArrayList();
        for (Iterator i = stemmerRules.keySet().iterator(); i.hasNext(); ) {
            String name = (String) i.next();
            StemRulesReader reader = new StemRulesReader();
            reader.read((Reader) stemmerRules.get(name));
            Morpher morpher = new AfixStemmer(name, reader);
            morphers.add(morpher);
        }
        morphers.add(new Caser());
        return new MorphemeProcessor((Morpher[]) morphers.toArray(new Morpher[morphers.size()]));
    }

    private Reader getReader(final String resource) {
        return new InputStreamReader(getClass().getResourceAsStream(resource));
    }
}
