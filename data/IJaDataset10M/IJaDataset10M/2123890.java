package edu.collablab.brenthecht.wikapidia.language;

import java.util.HashMap;
import java.util.Vector;
import edu.collablab.brenthecht.wikapidia.WikapidiaDatabase;
import edu.collablab.brenthecht.wikapidia.dbentity.Outlink;

public class CzechLanguage extends Language {

    public CzechLanguage(int priority) {
        super(priority);
    }

    @Override
    protected String getCategoryTranslation() {
        return "Kategorie";
    }

    @Override
    protected String getDisambiguationTranslation() {
        return "rozcestník";
    }

    protected String getInternalDisambiguationFlag() {
        return "Rozcestník";
    }

    @Override
    public String getLanguageCode() {
        return "cs";
    }

    @Override
    protected String getImageTranslation() {
        return "Image";
    }

    @Override
    protected String getExternalLinksTranslation() {
        return "Externí odkazy";
    }

    @Override
    protected String getCenturyPatternString() {
        return null;
    }

    @Override
    protected String getDayMonthPatternString() {
        return null;
    }

    @Override
    protected String getDecadePatternString() {
        return null;
    }

    @Override
    protected String getMilleniumPatternString() {
        return null;
    }

    @Override
    protected String getMonthPatternString() {
        return null;
    }

    @Override
    protected String getYearPatternString() {
        return null;
    }

    @Override
    protected String getMonthYearPatternString() {
        return null;
    }

    @Override
    protected Vector<Outlink> findSubarticles(HashMap<String, Outlink> foundLinks, String text, int startUnivID, String startTitle, int snippetNumber, WikapidiaDatabase db, String languageCode, boolean isDisambiguationPage) {
        return null;
    }

    @Override
    protected String getRedirectPrefix() {
        return "#PŘESMĚRUJ";
    }
}
