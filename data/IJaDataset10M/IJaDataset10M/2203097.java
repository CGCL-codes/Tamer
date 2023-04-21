package com.ipolyglot.webapp.util.mainpage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.ipolyglot.Constants;
import com.ipolyglot.model.Language;
import com.ipolyglot.model.Lesson;
import com.ipolyglot.service.LessonManager;
import com.ipolyglot.service.LessonsRelativeIdsWithImages;
import com.ipolyglot.webapp.util.LanguageUtil;
import com.ipolyglot.webapp.util.ResourceBundleUtil;

/**
 * Utility class for the main page's lesson pane 
 */
public class LessonPaneUtil {

    static final String MENU_START = "<li><a class='qmparent' href=''>%TEXT &raquo;</a><ul>";

    static final String MENU_END = "</ul></li>";

    static final String MENU_ITEM_START = "<li><a href='%LINK'>";

    static final String MENU_ITEM_END = "</a></li>";

    static final String MENU_DIVIDER_ITEM = "<li><span class='qmdivider qmdividerx' ></span></li>";

    static final String MENU_OTHER_ITEM = "<li><a class='qmparent' href='javascript:void(0);'><fmt:message key='Other'/></a>";

    /** Returns the next random lesson's short id */
    public static String getNextRandomLessonShortId() {
        List<String> relativeLessonIdsWithImages = new ArrayList<String>();
        relativeLessonIdsWithImages.addAll(LessonsRelativeIdsWithImages.LESSON_RELATIVE_IDS_WITH_IMAGES);
        Collections.shuffle(relativeLessonIdsWithImages);
        String randomRelativeLessonIdWithImages = relativeLessonIdsWithImages.get(0);
        return randomRelativeLessonIdWithImages;
    }

    /**
	 * Returns true if the session contains the selected language pair, and these languages
	 * are with pictures
	 */
    public static boolean isSessionWithLanguages(HttpServletRequest request) {
        Language wordLang = getWordLanguage(request);
        if (wordLang == null) {
            return false;
        }
        String wordLangId = wordLang.getId();
        List<Language> langList = LanguageUtil.getDistinctLanguagesOfNegativeLessons(request.getSession().getServletContext());
        for (Language lang : langList) {
            if (wordLangId.equals(lang.getId())) {
                return true;
            }
        }
        return false;
    }

    public static String getRandomLessonShortId(HttpServletRequest request) {
        return (String) request.getAttribute("randomLessonId");
    }

    public static Language getWordLanguage(HttpServletRequest request) {
        Language wordLanguage = (Language) request.getAttribute(Constants.WORD_LANGUAGE);
        if (wordLanguage == null) {
            wordLanguage = (Language) request.getSession().getAttribute(Constants.WORD_LANGUAGE);
        }
        return wordLanguage;
    }

    public static Language getTranslationLanguage(HttpServletRequest request) {
        Language language = (Language) request.getAttribute(Constants.TRANSLATION_LANGUAGE);
        if (language == null) {
            language = (Language) request.getSession().getAttribute(Constants.TRANSLATION_LANGUAGE);
        }
        return language;
    }

    public static Lesson getLesson(PageContext pageContext, String lessonFullId) {
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext());
        LessonManager lessonManager = (LessonManager) ctx.getBean("lessonManager");
        Lesson lesson = null;
        try {
            lesson = lessonManager.getLesson(lessonFullId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lesson;
    }

    public static boolean isLocaleInLanguages(String locale, Map<String, Language> sortedLangMap) {
        Iterator<Language> iter = sortedLangMap.values().iterator();
        while (iter.hasNext()) {
            Language lang = iter.next();
            String id = lang.getId();
            if (id.equals(locale)) {
                return true;
            }
        }
        return false;
    }

    public static Integer getLocaleNumeralId(String locale, Map<String, Language> sortedLangMap) {
        Iterator<Language> iter = sortedLangMap.values().iterator();
        while (iter.hasNext()) {
            Language lang = iter.next();
            String id = lang.getId();
            if (id.equals(locale)) {
                return lang.getNumeralId();
            }
        }
        return null;
    }

    public static String getLessonEnglishName(String lessonShortId, Map<String, Language> sortedLangMap, PageContext pageContext) {
        Integer enNumeralId = getLocaleNumeralId("en", sortedLangMap);
        Integer ruNumeralId = getLocaleNumeralId("ru", sortedLangMap);
        String lessonFullId = "-" + enNumeralId + ruNumeralId + lessonShortId;
        Lesson lesson = getLesson(pageContext, lessonFullId);
        String lessonName = lesson.getName();
        int n = lessonName.indexOf("-");
        return lessonName.substring(0, n);
    }

    public static String createLessonFullId(String lessonShortId, String wordLocale, String translationLocale, Map<String, Language> sortedLangMap) {
        Integer wordNumeralId = getLocaleNumeralId(wordLocale, sortedLangMap);
        Integer translationNumeralId = getLocaleNumeralId(translationLocale, sortedLangMap);
        String lessonFullId = "-" + wordNumeralId + translationNumeralId + lessonShortId;
        return lessonFullId;
    }

    public static String getLessonFullIdBySelectedLanguages(String lessonShortId, HttpServletRequest request) {
        Language wordLanguage = getWordLanguage(request);
        Integer wordNumeralId = wordLanguage.getNumeralId();
        Language translationLanguage = getTranslationLanguage(request);
        Integer translationNumeralId = translationLanguage.getNumeralId();
        String lessonFullId = "-" + wordNumeralId + translationNumeralId + lessonShortId;
        return lessonFullId;
    }

    /** Returns the lesson menu */
    public static String getLessonMenu(String randomLessonId, HttpServletRequest request) {
        String localeId = ResourceBundleUtil.getLocaleLanguageId(request);
        Map<String, Language> sortedLangMap = LanguageUtil.getDistinctLanguagesOfNegativeLessons(request.getSession().getServletContext(), localeId);
        String selectedMenuItem = "";
        int wordLangNumeralId = 0;
        int transLangNumeralId = 0;
        boolean sessionWithLanguages = LessonPaneUtil.isSessionWithLanguages(request);
        if (sessionWithLanguages) {
            wordLangNumeralId = LessonPaneUtil.getWordLanguage(request).getNumeralId();
            transLangNumeralId = LessonPaneUtil.getTranslationLanguage(request).getNumeralId();
        }
        String preferredLanguageId = localeId;
        String preferredMenuItem = "";
        if (sessionWithLanguages) {
            preferredLanguageId = LessonPaneUtil.getTranslationLanguage(request).getId();
        }
        String mainMenu = "";
        String languageMenu = "";
        Iterator<String> iter1 = sortedLangMap.keySet().iterator();
        while (iter1.hasNext()) {
            String langName1 = iter1.next();
            languageMenu = MENU_START.replace("%TEXT", langName1);
            Iterator<String> iter2 = sortedLangMap.keySet().iterator();
            String languagePairList = "";
            preferredMenuItem = "";
            while (iter2.hasNext()) {
                String langName2 = iter2.next();
                if (langName1.equals(langName2)) {
                    continue;
                }
                Language lang1 = sortedLangMap.get(langName1);
                Language lang2 = sortedLangMap.get(langName2);
                String lessonLink = "lesson-" + lang1.getNumeralId() + lang2.getNumeralId() + randomLessonId;
                String menuItem = MENU_ITEM_START.replace("%LINK", lessonLink) + langName1 + "-" + langName2 + MENU_ITEM_END;
                languagePairList = languagePairList + menuItem;
                if (lang2.getId().equals(preferredLanguageId)) {
                    preferredMenuItem = menuItem;
                }
                if (sessionWithLanguages && wordLangNumeralId == lang1.getNumeralId() && transLangNumeralId == lang2.getNumeralId()) {
                    selectedMenuItem = menuItem;
                }
            }
            if (!preferredMenuItem.equals("")) {
                languagePairList = preferredMenuItem + MENU_DIVIDER_ITEM + languagePairList;
            }
            languageMenu = languageMenu + languagePairList + MENU_END;
            mainMenu = mainMenu + languageMenu;
        }
        if (sessionWithLanguages && !selectedMenuItem.equals("")) {
            String otherString = ResourceBundleUtil.getResourceString(request, "Other");
            mainMenu = selectedMenuItem + MENU_DIVIDER_ITEM + MENU_START.replace("%TEXT", otherString) + mainMenu + MENU_END;
        }
        return mainMenu;
    }

    /** Returns the lesson list menu */
    public static String getLessonListMenu(HttpServletRequest request) {
        String localeId = ResourceBundleUtil.getLocaleLanguageId(request);
        Map<String, Language> sortedLangMap = LanguageUtil.getDistinctLanguagesOfNegativeLessons(request.getSession().getServletContext(), localeId);
        String selectedMenuItem = "";
        int wordLangNumeralId = 0;
        int transLangNumeralId = 0;
        boolean sessionWithLanguages = LessonPaneUtil.isSessionWithLanguages(request);
        if (sessionWithLanguages) {
            wordLangNumeralId = LessonPaneUtil.getWordLanguage(request).getNumeralId();
            transLangNumeralId = LessonPaneUtil.getTranslationLanguage(request).getNumeralId();
        }
        String preferredLanguageId = localeId;
        String preferredMenuItem = "";
        if (sessionWithLanguages) {
            preferredLanguageId = LessonPaneUtil.getTranslationLanguage(request).getId();
        }
        String mainMenu = "";
        String languageMenu = "";
        Iterator<String> iter1 = sortedLangMap.keySet().iterator();
        while (iter1.hasNext()) {
            String langName1 = iter1.next();
            languageMenu = MENU_START.replace("%TEXT", langName1);
            Iterator<String> iter2 = sortedLangMap.keySet().iterator();
            String languagePairList = "";
            preferredMenuItem = "";
            while (iter2.hasNext()) {
                String langName2 = iter2.next();
                if (langName1.equals(langName2)) {
                    continue;
                }
                Language lang1 = sortedLangMap.get(langName1);
                Language lang2 = sortedLangMap.get(langName2);
                String lessonLink = "lessons-" + lang1.getId() + "-" + lang2.getId();
                String menuItem = MENU_ITEM_START.replace("%LINK", lessonLink) + langName1 + "-" + langName2 + MENU_ITEM_END;
                languagePairList = languagePairList + menuItem;
                if (lang2.getId().equals(preferredLanguageId)) {
                    preferredMenuItem = menuItem;
                }
                if (sessionWithLanguages && wordLangNumeralId == lang1.getNumeralId() && transLangNumeralId == lang2.getNumeralId()) {
                    selectedMenuItem = menuItem;
                }
            }
            if (!preferredMenuItem.equals("")) {
                languagePairList = preferredMenuItem + MENU_DIVIDER_ITEM + languagePairList;
            }
            languageMenu = languageMenu + languagePairList + MENU_END;
            mainMenu = mainMenu + languageMenu;
        }
        if (sessionWithLanguages && !selectedMenuItem.equals("")) {
            String otherString = ResourceBundleUtil.getResourceString(request, "Other");
            mainMenu = selectedMenuItem + MENU_DIVIDER_ITEM + MENU_START.replace("%TEXT", otherString) + mainMenu + MENU_END;
        }
        return mainMenu;
    }

    public static String getImageUrl(ServletContext application, String lessonShortId) {
        String imageRootFolder = application.getInitParameter("imgRootFolder");
        String imageUrl = imageRootFolder + lessonShortId + ".jpg";
        return imageUrl;
    }

    /** Returns a link to the lesson list for the current selected languages pair */
    public static String getLessonListLink(HttpServletRequest request) {
        Language wordLanguage = getWordLanguage(request);
        Language translationLanguage = getTranslationLanguage(request);
        String link = "lessons-" + wordLanguage.getId() + "-" + translationLanguage.getId();
        return link;
    }
}
