package org.tonguetied.datatransfer.importing;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.tonguetied.datatransfer.importing.ImportException.ImportErrorCode;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.TranslationPredicate;
import org.tonguetied.keywordmanagement.Country.CountryCode;
import org.tonguetied.keywordmanagement.Language.LanguageCode;
import org.tonguetied.keywordmanagement.Translation.TranslationState;

/**
 * Base data importer that handles input in the Java resource or property file
 * type format. The resource file is read and entries are transformed into 
 * {@link Translation}s and added to the system. Implementers of this file will
 * determine how to handle the keys and translations.
 *   
 * @author bsion
 */
public abstract class AbstractPropertiesImporter extends AbstractSingleResourceImporter {

    /**
     * Create a new instance of PropertiesImporter.
     * 
     * @param keywordService the interface to keyword functions
     */
    public AbstractPropertiesImporter(KeywordService keywordService) {
        super(keywordService);
    }

    @Override
    protected void doImport(final byte[] input, final TranslationState state) throws ImportException {
        Map<Object, Object> properties = loadProperties(input);
        Keyword keyword;
        Translation translation;
        String value;
        for (Entry<Object, Object> entry : properties.entrySet()) {
            keyword = getKeywordService().getKeyword((String) entry.getKey());
            value = "".equals(entry.getValue()) ? null : (String) entry.getValue();
            if (keyword == null) {
                keyword = new Keyword();
                keyword.setKeyword((String) entry.getKey());
                translation = new Translation(getBundle(), getCountry(), getLanguage(), value, state);
                keyword.addTranslation(translation);
            } else {
                translation = findTranslation(keyword);
                if (translation == null) {
                    translation = new Translation(getBundle(), getCountry(), getLanguage(), value, state);
                    keyword.addTranslation(translation);
                } else {
                    translation.setState(state);
                    translation.setValue(value);
                }
            }
            getKeywordService().saveOrUpdate(keyword);
        }
        if (logger.isInfoEnabled()) logger.info("processed " + properties.size() + " translations");
    }

    /**
     * Loads the properties file into a format readable by the importer.
     * 
     * @param input the input byte array of the properties file contents.
     * @return a key value mapping of keywords and translations.
     */
    protected abstract Map<Object, Object> loadProperties(final byte[] input);

    /**
     * Find a translation from an existing keyword that matches the business 
     * keys.
     * 
     * @param keyword the existing keyword to search
     * @return the matching translation or <code>null</code> if no match is 
     * found
     */
    private Translation findTranslation(final Keyword keyword) {
        Translation translation = null;
        if (!keyword.getTranslations().isEmpty()) {
            final Predicate predicate = new TranslationPredicate(getBundle(), getCountry(), getLanguage());
            translation = (Translation) CollectionUtils.find(keyword.getTranslations(), predicate);
        }
        return translation;
    }

    /**
     * Validates the <code>fileName</code> to ensure that the fileName 
     * corresponds to an existing {@link Bundle}, {@link Country} and 
     * {@link Language}.
     * 
     */
    @Override
    protected void validate(final String fileName, final Bundle bundle, List<ImportErrorCode> errorCodes) throws ImportException {
        String[] tokens = fileName.split("_");
        CountryCode countryCode = null;
        LanguageCode languageCode = null;
        switch(tokens.length) {
            case 1:
                countryCode = CountryCode.DEFAULT;
                languageCode = LanguageCode.DEFAULT;
                break;
            case 2:
                if (isCountryCode(tokens[1])) {
                    countryCode = ImporterUtils.evaluateCountryCode(tokens[1], errorCodes);
                    languageCode = LanguageCode.DEFAULT;
                } else {
                    countryCode = CountryCode.DEFAULT;
                    languageCode = ImporterUtils.evaluateLanguageCode(tokens[1], errorCodes);
                }
                break;
            case 3:
                countryCode = ImporterUtils.evaluateCountryCode(tokens[2], errorCodes);
                languageCode = ImporterUtils.evaluateLanguageCode(tokens[1], errorCodes);
                break;
            default:
                errorCodes.add(ImportErrorCode.invalidNameFormat);
                break;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("bundle name = " + tokens[0]);
        }
        if (bundle == null) {
            setBundle(getKeywordService().getBundleByResourceName(tokens[0]));
            if (getBundle() == null) errorCodes.add(ImportErrorCode.unknownBundle);
        } else {
            setBundle(bundle);
        }
        this.setCountry(getKeywordService().getCountry(countryCode));
        if (getCountry() == null) errorCodes.add(ImportErrorCode.unknownCountry);
        this.setLanguage(getKeywordService().getLanguage(languageCode));
        if (getLanguage() == null) errorCodes.add(ImportErrorCode.unknownLanguage);
        if (!errorCodes.isEmpty()) logger.warn("Cannot process " + fileName + ". It contains " + errorCodes.size() + " errors");
    }

    /**
     * Determines if the string component is a country code or not.
     * 
     * @param code the code to evaluate
     * @return <code>true</code> if the string corresponds to a potential 
     * country code, <code>false</code> otherwise
     */
    protected boolean isCountryCode(String code) {
        boolean isCountryCode = false;
        if (code != null && !"".equals(code)) isCountryCode = Character.isUpperCase(code.charAt(0));
        return isCountryCode;
    }
}
