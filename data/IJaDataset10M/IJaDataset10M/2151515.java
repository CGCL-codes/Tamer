package com.genia.toolbox.projects.toolbox_basics_project.spring.manager.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import com.genia.toolbox.basics.process.Comparators;
import com.genia.toolbox.persistence.criteria.Criteria;
import com.genia.toolbox.persistence.dao.AbstractDAO;
import com.genia.toolbox.persistence.exception.PersistenceException;
import com.genia.toolbox.projects.toolbox_basics_project.bean.model.Language;
import com.genia.toolbox.projects.toolbox_basics_project.bean.model.impl.LanguageImpl;
import com.genia.toolbox.projects.toolbox_basics_project.spring.manager.LanguageManager;

/**
 * implementation of {@link LanguageManager}.
 */
public class LanguageManagerImpl extends AbstractDAO implements LanguageManager {

    /**
   * cache to retrieve language by their {@link Locale}.
   */
    private final Map<Locale, Long> languageByLocale = new HashMap<Locale, Long>();

    /**
   * returns all {@link Language}s.
   * 
   * @return all {@link Language}s
   * @throws PersistenceException
   *           when an error occured
   * @see com.genia.toolbox.projects.toolbox_basics_project.spring.manager.LanguageDao#getAll()
   */
    @Transactional(readOnly = true)
    public List<Language> getAll() throws PersistenceException {
        List<Language> res = new ArrayList<Language>(simpleFind(getCriteria(Language.class)));
        Collections.sort(res, Comparators.ORDERED_COMPARATOR);
        return res;
    }

    /**
   * returns the {@link Language} associated to the current
   * {@link java.util.Locale}.
   * 
   * @return the {@link Language} associated to the current
   *         {@link java.util.Locale}
   * @throws PersistenceException
   *           when an error occured
   * @see com.genia.toolbox.projects.toolbox_basics_project.spring.manager.LanguageDao#getCurrentLanguage()
   */
    public Language getCurrentLanguage() throws PersistenceException {
        return getLanguage(LocaleContextHolder.getLocale());
    }

    /**
   * returns a {@link Language} identified by its identifier.
   * 
   * @param identifier
   *          the identifier of the {@link Language} to return
   * @return a {@link Language} identified by its identifier
   * @see com.genia.toolbox.projects.toolbox_basics_project.spring.manager.LanguageManager#getLanguage(java.lang.Long)
   */
    public Language getLanguage(Long identifier) {
        return get(LanguageImpl.class, identifier);
    }

    /**
   * retrieve a {@link Language} from the cache.
   * 
   * @param locale
   *          the {@link Locale} of the {@link Language} to retrieve
   * @return the {@link Language} having exactly the given {@link Locale}.
   */
    private Language getCachedLanguage(Locale locale) {
        Long idLanguage = languageByLocale.get(locale);
        if (idLanguage == null) {
            return null;
        }
        Language language = get(LanguageImpl.class, idLanguage);
        if (language == null || !ObjectUtils.nullSafeEquals(locale, language.getLocale())) {
            languageByLocale.remove(locale);
            return null;
        }
        return language;
    }

    /**
   * add a {@link Language} to the cache.
   * 
   * @param language
   *          the {@link Language} to add
   */
    private void cacheLanguage(Language language) {
        if (language.getIdentifier() == null) {
            return;
        }
        synchronized (languageByLocale) {
            languageByLocale.put(language.getLocale(), language.getIdentifier());
        }
    }

    /**
   * returns a {@link Language} identified by its locale.
   * 
   * @param locale
   *          the locale of the {@link Language} to return
   * @return a {@link Language} identified by its locale
   * @throws PersistenceException
   *           when an error occured
   */
    @Transactional(readOnly = true)
    public Language getLanguage(Locale locale) throws PersistenceException {
        Language cachedLanguage = getCachedLanguage(locale);
        if (cachedLanguage != null) {
            return cachedLanguage;
        }
        Language firstRes = null;
        Language res = null;
        for (Language language : getAll()) {
            if (ObjectUtils.nullSafeEquals(language.getLocale(), locale)) {
                cacheLanguage(language);
                return language;
            }
            if (firstRes == null) {
                firstRes = language;
            } else {
                if (ObjectUtils.nullSafeEquals(language.getLocale().getLanguage(), locale.getLanguage())) {
                    res = language;
                }
            }
        }
        if (res != null) {
            return res;
        }
        return firstRes;
    }

    /**
   * returns the default {@link Language}.
   * 
   * @return the default {@link Language}
   * @throws PersistenceException
   *           when an error occured
   * @see com.genia.toolbox.projects.toolbox_basics_project.spring.manager.LanguageManager#getDefaultLanguage()
   */
    public Language getDefaultLanguage() throws PersistenceException {
        List<Language> languages = getAll();
        if (languages.isEmpty()) {
            return null;
        }
        return languages.get(0);
    }

    /**
   * returns a {@link Language} having exactly the given locale. If the language
   * does not exist, it will be created.
   * 
   * @param locale
   *          The {@link Locale} to get to {@link Language} for
   * @return a {@link Language} having exactly the given locale. If the language
   *         does not exist, it will be created
   * @throws PersistenceException
   *           if an error occured
   * @see com.genia.toolbox.projects.toolbox_basics_project.spring.manager.LanguageManager#getOrCreateLanguage(java.util.Locale)
   */
    public Language getOrCreateLanguage(Locale locale) throws PersistenceException {
        Language cachedLanguage = getCachedLanguage(locale);
        if (cachedLanguage != null) {
            return cachedLanguage;
        }
        Criteria<Language> criteria = getCriteria(Language.class);
        criteria.addRestriction(equals(getPropertyField(Language.NAME_LOCALE), getConstantField(locale)));
        Language language = simpleFindUnique(criteria);
        if (language == null) {
            language = new LanguageImpl();
            language.setLocale(locale);
            save((LanguageImpl) language);
        }
        cacheLanguage(language);
        return language;
    }
}
