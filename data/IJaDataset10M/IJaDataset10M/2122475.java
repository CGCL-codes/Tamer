package nl.multimedian.eculture.annocultor.core.vocabularies;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nl.multimedian.eculture.annocultor.core.common.Language.Lang;

/**
 * Directory of people, where each term has its specific context of lifetime
 * years, etc.
 * 
 * @author Borys Omelayenko
 * 
 */
public abstract class VocabularyOfPeople extends AbstractVocabulary {

    /**
	 * Person lookup with optional context.
	 * 
	 * @param names
	 *          all names
	 * @param birthDate
	 *          date as String (maybe, just a year)
	 * @param deathDate
	 *          date as String (maybe, just a year)
	 * @param liveDate
	 *          a date in live
	 * @param placeOfBirth
	 * @param placeOfDeath
	 * @return person code
	 */
    public Term lookupPerson(List<String> labels, Lang lang, String birthDate, String deathDate, String liveDate, String landOfBirth, String placeOfBirth, String landOfDeath, String placeOfDeath) throws Exception {
        return findByLabel(labels.get(0), liveDate == null ? new UlanDisambiguationContext(birthDate, deathDate, false) : new UlanDisambiguationContext(liveDate, deathDate, true));
    }

    public Term lookupPerson(String labels, Lang lang, String birthDate, String deathDate, String liveDate, String landOfBirth, String placeOfBirth, String landOfDeath, String placeOfDeath) throws Exception {
        List<String> l = new ArrayList<String>();
        l.add(labels.toLowerCase());
        return lookupPerson(l, lang, birthDate, deathDate, liveDate, landOfBirth, placeOfBirth, landOfDeath, placeOfDeath);
    }

    public VocabularyOfPeople(String name, Lang lang) {
        super(name, lang);
    }

    Calendar now = new GregorianCalendar();

    private static class UlanDisambiguationContext extends DisambiguationContext {

        String birthDate;

        String deathDate;

        boolean isLiveDate;

        public UlanDisambiguationContext(String birthDate, String deathDate, boolean isLiveDate) {
            super(null, null, null);
            this.birthDate = birthDate;
            this.deathDate = deathDate;
            this.isLiveDate = isLiveDate;
        }
    }

    @Override
    public String onNormaliseLabel(String label, NormaliseCaller caller) throws Exception {
        String lbl = label;
        int idx = lbl.indexOf(',');
        if (idx > 0 && idx < lbl.length() && idx == lbl.lastIndexOf(',')) {
            lbl = lbl.substring(idx + 1) + " " + lbl.substring(0, idx);
        }
        return super.onNormaliseLabel(lbl.toLowerCase(), caller);
    }

    private String toString(Set<Integer> s) {
        String result = "";
        for (Integer i : s) {
            if (result.length() != 0) result += ",";
            result += i;
        }
        return result;
    }

    private Set<Integer> years(String property, Term term) throws Exception {
        Set<Integer> result = new HashSet<Integer>();
        String y = term.getProperty(property);
        int year = Integer.parseInt(y);
        if (year < now.get(Calendar.YEAR)) result.add(year);
        return result;
    }

    @Override
    public TermList disambiguate(TermList terms, DisambiguationContext disambiguationContext) throws Exception {
        TermList results = new TermList();
        if (terms == null || terms.isEmpty()) return results;
        if (!(disambiguationContext instanceof UlanDisambiguationContext)) throw new Exception("Expected ULAN-specific disambiguation context. Apperantly, Ulan.disambiguate was called outside Ulan lookupPerson.");
        UlanDisambiguationContext udc = (UlanDisambiguationContext) disambiguationContext;
        for (Term term : terms) {
            Set<Integer> ulanBirthYears = years("birth", term);
            Set<Integer> ulanDeathYears = years("death", term);
            if (!ulanBirthYears.isEmpty() && !ulanDeathYears.isEmpty() && ulanBirthYears.iterator().next() + 100 == ulanDeathYears.iterator().next()) {
                ulanDeathYears.clear();
            }
            int reqBirthYear = 0;
            int reqDeathYear = 0;
            if (udc.birthDate != null) {
                if (udc.birthDate.matches("^\\d\\d\\d\\d(\\-(.+))?")) reqBirthYear = Integer.parseInt(udc.birthDate.substring(0, 4));
                if (reqBirthYear >= now.get(Calendar.YEAR)) {
                    reqBirthYear = 0;
                }
            }
            if (udc.deathDate != null) {
                if (udc.deathDate.matches("^\\d\\d\\d\\d(\\-(.+))?")) reqDeathYear = Integer.parseInt(udc.deathDate.substring(0, 4));
                if (reqDeathYear >= now.get(Calendar.YEAR)) {
                    reqDeathYear = 0;
                }
            }
            if (udc.isLiveDate) {
                if (checkDates(ulanBirthYears, ulanDeathYears, reqBirthYear, reqDeathYear, 3, true)) {
                    results.add(term);
                    term.setDisambiguatingComment(String.format("requested year of life %d matched ULAN lifetime (%s-%s)", reqBirthYear, toString(ulanBirthYears), toString(ulanDeathYears)));
                }
            } else {
                if (reqBirthYear == 0 && reqDeathYear == 0) {
                    results.add(term);
                    term.setDisambiguatingComment("Matching name only, no life dates provided");
                } else {
                    if (checkDates(ulanBirthYears, ulanDeathYears, reqBirthYear, reqDeathYear, 3, false)) {
                        results.add(term);
                        term.setDisambiguatingComment(String.format("requested lifetime (%d-%d) matched ULAN lifetime (%s-%s)", reqBirthYear, reqDeathYear, toString(ulanBirthYears), toString(ulanDeathYears)));
                    }
                }
                if (terms.size() == 1 && results.isEmpty()) {
                    {
                        results.add(term);
                        term.setConfidenceComment(String.format("requested lifetime (%d-%d) DID NOT really match ULAN lifetime (%s-%s)", reqBirthYear, reqDeathYear, toString(ulanBirthYears), toString(ulanDeathYears)));
                        term.setDisambiguatingComment("Maching name, and relaxed match on life dates");
                    }
                }
            }
        }
        return (results.size() == 1) ? results : new TermList();
    }

    static final int allDeadYear = 1850;

    private boolean checkDates(Set<Integer> ulanBirthYears, Set<Integer> ulanDeathYears, int reqBirthYear, int reqDeathYear, int toleranceMultiplier, boolean lifeDate) {
        int toleranceOnDeathYear = (2000 - reqDeathYear) * toleranceMultiplier / 100;
        int toleranceOnBirthYear = (2000 - reqBirthYear) * toleranceMultiplier / 100;
        if (lifeDate) {
            return (reqBirthYear >= (Collections.min(ulanBirthYears) - toleranceOnBirthYear)) && (ulanDeathYears.isEmpty() || reqBirthYear <= (Collections.max(ulanDeathYears) + toleranceOnDeathYear));
        } else {
            if (reqBirthYear <= allDeadYear && (ulanDeathYears == null || ulanDeathYears.isEmpty())) return false;
            if (reqBirthYear > 0) {
                if (!(ulanBirthYears.isEmpty() || (reqBirthYear >= (Collections.min(ulanBirthYears) - toleranceOnBirthYear)) && (reqBirthYear <= (Collections.max(ulanBirthYears) + toleranceOnBirthYear)))) return false;
            }
            if (reqDeathYear != 0) {
                if (!(reqBirthYear > allDeadYear && ulanDeathYears.isEmpty())) {
                    if (!(ulanDeathYears.isEmpty() || (reqDeathYear >= (Collections.min(ulanDeathYears) - toleranceOnDeathYear)) && (reqDeathYear <= (Collections.max(ulanDeathYears) + toleranceOnDeathYear)))) return false;
                }
            }
        }
        return true;
    }
}
