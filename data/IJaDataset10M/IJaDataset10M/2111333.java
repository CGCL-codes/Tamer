package hu.schmidtsoft.parser.language.impl;

import hu.schmidtsoft.parser.language.ILanguage;
import hu.schmidtsoft.parser.language.ITerm;
import hu.schmidtsoft.parser.tokenizer.impl.LanguageParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class InitNumericIds {

    Language language;

    List<ITerm> terms;

    private List<ITerm> getTerms() {
        return terms;
    }

    private InitNumericIds(ILanguage language, List<ITerm> terms) {
        this.language = (Language) language;
        this.terms = terms;
    }

    public static void initNumericIds(ILanguage language, List<ITerm> terms) throws LanguageParseException {
        new InitNumericIds(language, terms).doIt();
    }

    private void doIt() throws LanguageParseException {
        createMap();
        initNumericIds();
    }

    private void initNumericIds() throws LanguageParseException {
        {
            Set<String> referenced = new HashSet<String>();
            if (language.getRootName() == null) {
                throw new LanguageParseException("Error in language definition: root name is not defined!");
            }
            addReferenced(referenced, language.getRootName());
            addReferenced(referenced, "EOF");
            for (String s : language.getTokenFilterDef().getToFilter()) {
                addReferenced(referenced, s);
            }
            Set<String> toDrop = getTokensToDrop(referenced);
            System.out.println("" + referenced);
            System.out.println("" + toDrop);
            int ctr = 0;
            List<ITerm> termsA = new ArrayList<ITerm>();
            for (ITerm t : getTerms()) {
                if (!toDrop.contains(t.getName())) {
                    ((Term) t).setId(ctr);
                    termsA.add(t);
                    t.initialize(termMap);
                    t.setLanguage(language);
                    ctr++;
                }
            }
            language.setTerms(termsA.toArray(new ITerm[0]));
        }
        ITerm mroot = termMap.get(language.getRootName());
        language.setRootTerm(mroot);
    }

    private Set<String> getTokensToDrop(Set<String> referenced) {
        Set<String> toDrop = new TreeSet<String>();
        for (ITerm term : getTerms()) {
            if (!referenced.contains(term.getName())) {
                toDrop.add(term.getName());
            }
        }
        return toDrop;
    }

    private void addReferenced(Set<String> referenced, String name) throws LanguageParseException {
        if (referenced.add(name)) {
            Term t = (Term) termMap.get(name);
            if (t == null) {
                throw new LanguageParseException("Language error: Term is not defined: " + name);
            }
            for (String s : t.getSubsStr()) {
                addReferenced(referenced, s);
            }
        }
    }

    TreeMap<String, ITerm> termMap;

    private void createMap() {
        termMap = new TreeMap<String, ITerm>();
        for (ITerm t : terms) {
            termMap.put(t.getName(), t);
        }
    }
}
