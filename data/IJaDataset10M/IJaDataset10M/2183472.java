package eu.annocultor.tagger.postprocessors;

import eu.annocultor.tagger.terms.Term;
import eu.annocultor.tagger.terms.TermList;
import eu.annocultor.tagger.vocabularies.DisambiguationContext;

/**
 * Hierarchical disambiguation by administrative division; 
 * many places are named the same and nested into each other: cities into regions, etc.
 * 
 * @author Borys Omelayenko
 * 
 */
public class AdminDivisionTermFilter extends TermFilter {

    private static final String VOCAB_ATTR_DIVISION = "division";

    private static final String VOCAB_ATTR_COUNTRY = "country";

    @Override
    public TermList disambiguate(TermList allTerms, DisambiguationContext disambiguationContext) throws Exception {
        if (allTerms.size() < 2) return allTerms;
        if (allTerms.isSameLabels()) {
            TermList selectedTerms = new TermList();
            Term largestPlace = null;
            String largestAdminDivision = null;
            for (Term term : allTerms) {
                if (largestPlace == null) {
                    largestPlace = term;
                }
                String adminDivision = term.getProperty(VOCAB_ATTR_DIVISION);
                String countryTerm = term.getProperty(VOCAB_ATTR_COUNTRY);
                String countryLargest = largestPlace.getProperty(VOCAB_ATTR_COUNTRY);
                if (countryTerm == null) {
                    return allTerms;
                }
                if (countryTerm.equals(countryLargest)) {
                    if (adminDivision == null || adminDivision.isEmpty()) {
                        return allTerms;
                    }
                    if (largestAdminDivision == null || adminDivision.compareToIgnoreCase(largestAdminDivision) > 0) {
                        largestAdminDivision = adminDivision;
                        largestPlace = term;
                    }
                }
            }
            if (largestAdminDivision != null && largestPlace != null) {
                selectedTerms = new TermList();
                selectedTerms.add(largestPlace);
                return selectedTerms;
            }
        }
        return allTerms;
    }
}
