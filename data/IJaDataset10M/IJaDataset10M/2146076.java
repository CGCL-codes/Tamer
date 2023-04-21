package com.skillworld.webapp.web.services;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.skillworld.webapp.model.department.rank.Rank;
import com.skillworld.webapp.model.languageservice.LanguageService;

public class I18n {

    private final Locale en = new Locale("en");

    private final Locale pt = new Locale("pt");

    private final Locale defaultLocale = en;

    private final List<Locale> supportedLocales;

    private final List<Locale> knownLocales;

    private final Map<Rank, Map<Locale, String>> rank_i18n;

    public I18n(LanguageService languageService) {
        supportedLocales = languageService.listSupportedLanguages();
        knownLocales = languageService.listISOLanguages();
        Map<Locale, String> novice_i18n = new HashMap<Locale, String>();
        novice_i18n.put(en, "Novice");
        novice_i18n.put(pt, "Novato");
        Map<Locale, String> beginner_i18n = new HashMap<Locale, String>();
        beginner_i18n.put(en, "Beginner");
        beginner_i18n.put(pt, "Principiante");
        Map<Locale, String> intermediate_i18n = new HashMap<Locale, String>();
        intermediate_i18n.put(en, "Intermediate");
        intermediate_i18n.put(pt, "Intermédio");
        Map<Locale, String> master_i18n = new HashMap<Locale, String>();
        master_i18n.put(en, "Master");
        master_i18n.put(pt, "Mestre");
        Map<Locale, String> mastercoach_i18n = new HashMap<Locale, String>();
        mastercoach_i18n.put(en, "Master Coach");
        mastercoach_i18n.put(pt, "Master Coach");
        rank_i18n = new HashMap<Rank, Map<Locale, String>>();
        rank_i18n.put(Rank.NOVICE, novice_i18n);
        rank_i18n.put(Rank.BEGINNER, beginner_i18n);
        rank_i18n.put(Rank.INTERMEDIATE, intermediate_i18n);
        rank_i18n.put(Rank.MASTER, master_i18n);
        rank_i18n.put(Rank.MASTER_COACH, mastercoach_i18n);
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public List<Locale> getSupportedLocales() {
        return supportedLocales;
    }

    public List<Locale> getKnownLocales() {
        return knownLocales;
    }

    public Locale selectLocale(String code) {
        if (code == null) {
            return defaultLocale;
        }
        if (code.length() != 2 && code.length() != 5) {
            throw new IllegalArgumentException();
        }
        String code2 = code.substring(0, 2);
        Locale desiredLocale = new Locale(code2);
        if (supportedLocales.contains(desiredLocale)) {
            return desiredLocale;
        } else {
            return defaultLocale;
        }
    }

    public String displayRank(Rank rank, Locale locale) {
        return rank_i18n.get(rank).get(locale);
    }
}
