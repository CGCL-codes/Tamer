package edu.collablab.brenthecht.wikapidia.language;

import java.util.HashMap;
import java.util.Vector;
import edu.collablab.brenthecht.wikapidia.WikapidiaDatabase;
import edu.collablab.brenthecht.wikapidia.dbentity.Outlink;

public class ArabicLanguage extends Language {

    public ArabicLanguage(int priority) {
        super(priority);
    }

    @Override
    protected String getInternalDisambiguationFlag() {
        return "توضيح";
    }

    @Override
    protected String getCategoryTranslation() {
        return "تصنيف";
    }

    @Override
    protected String getDisambiguationTranslation() {
        return "توضيح";
    }

    @Override
    public String getLanguageCode() {
        return "ar";
    }

    @Override
    protected String getImageTranslation() {
        return "ملف";
    }

    @Override
    protected String getExternalLinksTranslation() {
        return "external links";
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
    protected String getRedirectPrefix() {
        return "#تحويل";
    }

    @Override
    protected Vector<Outlink> findSubarticles(HashMap<String, Outlink> foundLinks, String text, int startUnivID, String startTitle, int snippetNumber, WikapidiaDatabase db, String languageCode, boolean isDisambiguationPage) {
        return null;
    }
}
