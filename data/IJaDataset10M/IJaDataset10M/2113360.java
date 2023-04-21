package org.tonguetied.datatransfer.importing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.tonguetied.datatransfer.common.FormatType;
import org.tonguetied.datatransfer.importing.ImportException.ImportErrorCode;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.Country.CountryCode;
import org.tonguetied.keywordmanagement.Language.LanguageCode;
import org.tonguetied.keywordmanagement.Translation.TranslationState;

/**
 * This class parses excel spreadsheets in the format for 
 * {@link FormatType#xlsLanguage}. A map of {@link Keyword}s and their 
 * {@link Translation}s are built by processing each cell of the spread sheet.
 * 
 * 
 * @author bsion
 *
 */
public class ExcelLanguageCentricParser implements ExcelParser {

    private SSTRecord sstrec;

    private List<Language> languages;

    private Map<String, Keyword> keywords;

    private Keyword keyword;

    private Translation baseTranslation;

    private int lastColOfRow;

    private KeywordService keywordService;

    private List<ImportErrorCode> errorCodes;

    private static final Logger logger = Logger.getLogger(ExcelLanguageCentricParser.class);

    /**
     * Create a new instance of ExcelDataParser.
     * 
     * @param keywordService
     */
    public ExcelLanguageCentricParser(KeywordService keywordService) {
        this.languages = new ArrayList<Language>();
        this.keywords = new HashMap<String, Keyword>();
        this.keywordService = keywordService;
        this.errorCodes = new ArrayList<ImportErrorCode>();
    }

    /**
     * This method listens for incoming records and handles them as required.
     * 
     * @param record The record that was found while reading.
     */
    public void processRecord(Record record) {
        if (record == null) {
            if (logger.isInfoEnabled()) logger.info("no record to process");
        } else {
            switch(record.getSid()) {
                case BOFRecord.sid:
                    if (!(record instanceof BOFRecord)) throw new ImportException("unknown excel element", null);
                    final BOFRecord bof = (BOFRecord) record;
                    if (bof.getType() == BOFRecord.TYPE_WORKBOOK) {
                        if (logger.isInfoEnabled()) logger.info("Processing excel workbook");
                    } else if (bof.getType() == BOFRecord.TYPE_WORKSHEET) {
                        if (logger.isInfoEnabled()) logger.info("recordsize = " + bof.getRecordSize() + ", required version = " + bof.getRequiredVersion());
                    }
                    break;
                case BoundSheetRecord.sid:
                    if (!(record instanceof BoundSheetRecord)) throw new ImportException("unknown excel element", null);
                    final BoundSheetRecord bsr = (BoundSheetRecord) record;
                    if (logger.isDebugEnabled()) logger.debug("processing sheet: " + bsr.getSheetname());
                    break;
                case RowRecord.sid:
                    if (!(record instanceof RowRecord)) throw new ImportException("unknown excel element", null);
                    final RowRecord rowrec = (RowRecord) record;
                    lastColOfRow = rowrec.getLastCol();
                    break;
                case NumberRecord.sid:
                    if (!(record instanceof NumberRecord)) throw new ImportException("unknown excel element", null);
                    final NumberRecord numrec = (NumberRecord) record;
                    logger.warn("Cell [" + numrec.getRow() + "," + numrec.getColumn() + "] expecting a string value not numeric: " + numrec.getValue() + ". Ignoring value");
                    break;
                case SSTRecord.sid:
                    if (!(record instanceof SSTRecord)) throw new ImportException("unknown excel element", null);
                    sstrec = (SSTRecord) record;
                    if (logger.isDebugEnabled()) {
                        logger.debug("file contains " + sstrec.getNumUniqueStrings() + " unique strings");
                    }
                    break;
                case LabelSSTRecord.sid:
                    if (!(record instanceof LabelSSTRecord)) throw new ImportException("unknown excel element", null);
                    final LabelSSTRecord lrec = (LabelSSTRecord) record;
                    if (lrec.getRow() == 0) {
                        processHeader(lrec);
                    } else {
                        if (lrec.getColumn() == 0) {
                            String keywordStr = sstrec.getString(lrec.getSSTIndex()).getString();
                            loadKeyword(keywordStr);
                        } else if (lrec.getColumn() == 1) {
                            keyword.setContext(sstrec.getString(lrec.getSSTIndex()).getString());
                        } else if (lrec.getColumn() == 2) {
                            baseTranslation = new Translation();
                            baseTranslation.setKeyword(keyword);
                            String name = sstrec.getString(lrec.getSSTIndex()).getString();
                            Bundle bundle = keywordService.getBundleByName(name);
                            baseTranslation.setBundle(bundle);
                        } else if (lrec.getColumn() == 3) {
                            String colHeader = sstrec.getString(lrec.getSSTIndex()).getString();
                            String[] headers = colHeader.split(":");
                            CountryCode code = CountryCode.valueOf(headers[0]);
                            Country country = keywordService.getCountry(code);
                            baseTranslation.setCountry(country);
                        } else {
                            Language language = languages.get(lrec.getColumn() - 4);
                            String value = sstrec.getString(lrec.getSSTIndex()).getString();
                            Translation translation = baseTranslation.deepClone();
                            if (language.getCode() == LanguageCode.zht) {
                                language = keywordService.getLanguage(LanguageCode.zh);
                                Country country = keywordService.getCountry(CountryCode.TW);
                                translation.setCountry(country);
                            }
                            translation.setLanguage(language);
                            translation.setState(TranslationState.UNVERIFIED);
                            translation.setValue(value);
                            keyword.addTranslation(translation);
                        }
                        if (isLastColumn(lrec.getColumn())) {
                            keywords.put(keyword.getKeyword(), keyword);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @param keywordStr
     */
    private void loadKeyword(final String keywordStr) {
        keyword = keywords.get(keywordStr);
        if (keyword == null) {
            if (logger.isDebugEnabled()) logger.debug("creating new keyword instance");
            keyword = new Keyword();
            keyword.setKeyword(keywordStr);
        }
    }

    /**
     * Column 0 keyword
     * Column 1 context
     * Column 2 Bundle
     * Column 3 Country
     * Column 4..n Languages
     * @param lrec
     */
    private void processHeader(LabelSSTRecord lrec) {
        if (lrec.getColumn() > 3) {
            String colHeader = sstrec.getString(lrec.getSSTIndex()).getString();
            String[] headers = colHeader.split(":");
            LanguageCode code = LanguageCode.valueOf(headers[0]);
            Language language;
            if (LanguageCode.zht == code) {
                language = new Language();
                language.setCode(code);
                language.setName("Traditional Chinese");
            } else {
                language = keywordService.getLanguage(code);
            }
            languages.add(language);
        }
    }

    /**
     * Determine if the column is the last column of the row in the spreadsheet.
     * 
     * @param columnNum the column number to evaluate
     * @return <code>true</code> if the column is the last column, 
     * <code>false</code> otherwise
     */
    public boolean isLastColumn(final short columnNum) {
        return lastColOfRow - 1 == columnNum;
    }

    /**
     * @return the list of {@link Language}s used in this file, or an empty 
     * list if no {@link Language}s were specified
     */
    protected List<Language> getLanguages() {
        return this.languages;
    }

    public List<ImportErrorCode> getErrorCodes() {
        return errorCodes;
    }

    public Map<String, Keyword> getKeywords() {
        return keywords;
    }
}
