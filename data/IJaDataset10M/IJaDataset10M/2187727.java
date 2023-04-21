package com.ipolyglot.service;

import java.util.List;
import com.ipolyglot.dao.LanguageDAO;
import com.ipolyglot.model.Language;
import com.ipolyglot.model.LanguagesUsageData;

/**
 * @author mishag
 */
public interface LanguageManager {

    public void setLanguageDAO(LanguageDAO dao);

    public Language getLanguage(String id);

    public List<Language> getLanguages();

    public Language getLanguageByNumeralId(String numeralId);

    public List<LanguagesUsageData> getLanguagesUsageDataList(String username);

    public Language getLanguageByIdOrName(String idOrName);

    public List<Language> getDistinctLanguagesFromLanguagesUsageData(List<LanguagesUsageData> languagesUsageDataList);

    public boolean isAudioEnabled(String languageId);

    public List<Language> getDistinctLanguagesOfNegativeLessons();
}
