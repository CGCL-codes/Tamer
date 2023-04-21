package org.pubcurator.analyzers.metamap.annotators;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.metamap.uima.ts.Candidate;
import org.metamap.uima.ts.Mapping;
import org.metamap.uima.ts.Phrase;
import org.metamap.uima.ts.Span;
import org.pubcurator.analyzers.metamap.misc.CategoryMapper;
import org.pubcurator.uima.annotator.AnalyzerAnnotator;
import org.pubcurator.uima.config.Category;
import org.pubcurator.uima.definitions.PredefinedIdentifierTypes;
import org.pubcurator.uima.ts.PubSpan;
import org.pubcurator.uima.ts.PubTerm;

/**
 * @author Kai Schlamp (schlamp@gmx.de)
 *
 */
public class MetaMapAnnotator extends AnalyzerAnnotator {

    private static final String ABBREVIATIONS_FILE = "abbreviations.properties";

    private CategoryMapper categoryMapper;

    private Map<String, String> abbreviationsMap;

    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext);
        initMappings();
        categoryMapper = new CategoryMapper();
    }

    private void initMappings() throws ResourceInitializationException {
        try {
            Properties properties = new Properties();
            properties.load(new BufferedInputStream(getContext().getResourceAsStream(ABBREVIATIONS_FILE)));
            abbreviationsMap = new HashMap<String, String>();
            for (Object key : properties.keySet()) {
                String ui = ((String) key).trim();
                String abbr = ((String) properties.get(ui)).trim();
                abbreviationsMap.put(abbr, ui);
            }
        } catch (Exception e) {
            throw new ResourceInitializationException(e);
        }
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        Map<Candidate, List<PubTerm>> candidateTermsMap = new HashMap<Candidate, List<PubTerm>>();
        List<PubTerm> termList = new ArrayList<PubTerm>();
        FSIterator<Annotation> iterator = jcas.getAnnotationIndex(Phrase.type).iterator();
        while (iterator.hasNext()) {
            Phrase phrase = (Phrase) iterator.next();
            for (int i = 0; i < phrase.getMappings().size(); i++) {
                Mapping mapping = (Mapping) phrase.getMappings(i);
                for (int j = 0; j < mapping.getCandidates().size(); j++) {
                    Candidate candidate = (Candidate) mapping.getCandidates(j);
                    FSArray pubSpans = new FSArray(jcas, candidate.getSpans().size());
                    for (int l = 0; l < candidate.getSpans().size(); l++) {
                        Span span = (Span) candidate.getSpans(l);
                        PubSpan pubSpan = createSpan(jcas, span.getBegin(), span.getEnd());
                        pubSpans.set(l, pubSpan);
                    }
                    for (int k = 0; k < candidate.getSemanticTypes().size(); k++) {
                        String semanticTypeAbbr = candidate.getSemanticTypes(k);
                        String semanticTypeUi = abbreviationsMap.get(semanticTypeAbbr);
                        List<Category> categories = categoryMapper.getCategories(semanticTypeUi);
                        if (categories.isEmpty()) {
                            getContext().getLogger().log(Level.INFO, "No categories for Semantic Type were found: " + semanticTypeUi);
                            continue;
                        }
                        for (Category category : categories) {
                            boolean unique = true;
                            for (PubTerm term : termList) {
                                if (term.getBegin() == candidate.getBegin() && term.getEnd() == candidate.getEnd()) {
                                    if (term.getCategoryName().equals(category.getName())) {
                                        unique = false;
                                        break;
                                    }
                                }
                            }
                            if (!unique) {
                                continue;
                            }
                            PubTerm term = createTerm(jcas, candidate.getBegin(), candidate.getEnd(), category.getName(), candidate.getScore(), true);
                            term.setSpans(pubSpans);
                            FSArray identifiers = new FSArray(jcas, 2);
                            identifiers.set(0, createIdentifier(jcas, PredefinedIdentifierTypes.UMLS_CONCEPT, candidate.getConcept().getPreferredName(), null));
                            identifiers.set(1, createIdentifier(jcas, PredefinedIdentifierTypes.UMLS_CUI, candidate.getConcept().getCui(), null));
                            term.setIdentifiers(identifiers);
                            addToCandidateTermsMap(candidateTermsMap, candidate, term);
                            termList.add(term);
                        }
                    }
                }
            }
        }
    }

    private void addToCandidateTermsMap(Map<Candidate, List<PubTerm>> candidateTermsMap, Candidate candidate, PubTerm term) {
        if (!candidateTermsMap.containsKey(candidate)) {
            List<PubTerm> terms = new ArrayList<PubTerm>();
            terms.add(term);
            candidateTermsMap.put(candidate, terms);
        } else {
            candidateTermsMap.get(candidate).add(term);
        }
    }
}
