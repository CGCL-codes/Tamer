package de.kugihan.dictionaryformids.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.*;
import de.kugihan.dictionaryformids.general.*;
import de.kugihan.dictionaryformids.dataaccess.*;
import de.kugihan.dictionaryformids.translation.*;
import de.kugihan.dictionaryformids.dataaccess.fileaccess.*;
import java.util.*;
import de.kugihan.dictionaryformids.hmi_common.content.*;
import de.kugihan.dictionaryformids.dataaccess.content.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TranslationLayerGWT implements EntryPoint, TranslationExecutionCallback {

    public static ContentParser contentParserObj;

    /**
	  * This is the entry point method.
    */
    public void onModuleLoad() {
        UtilJs utilObj = new UtilJs();
        Util.setUtil(utilObj);
        exportStaticMethods();
        exportPredefinedContent();
        contentParserObj = new ContentParser();
        try {
            HTRInputStream.setBaseDirectory(getBaseDirectory());
            FileAccessHandler.setDictionaryDataFileISAccess(new HTRInputStreamAccess());
            CsvFile.fileStorageReader = new HTRFileStorageReader();
            DictionaryDataFile.initValues(false);
            exportDictionaryDataFileDataStructures();
            TranslationExecution.setTranslationExecutionCallback(this);
        } catch (Exception e) {
            Util.getUtil().log(e);
        }
    }

    public void deletePreviousTranslationResult() {
        callDeletePreviousTranslationResultJs();
    }

    public void newTranslationResult(TranslationResult resultOfTranslation) {
        callNewTranslationResultJs(resultOfTranslation);
    }

    public static void executeTranslation(String toBeTranslatedWordTextInputParam, JsArrayBoolean inputLanguagesParam, JsArrayBoolean outputLanguagesParam, boolean executeInBackgroundParam, int maxHitsParam, int durationForCancelSearchParam) throws DictionaryException {
        boolean[] inputLanguages = new boolean[inputLanguagesParam.length()];
        boolean[] outputLanguages = new boolean[outputLanguagesParam.length()];
        for (int i = 0; i < inputLanguagesParam.length(); ++i) {
            inputLanguages[i] = inputLanguagesParam.get(i);
        }
        for (int i = 0; i < outputLanguagesParam.length(); ++i) {
            outputLanguages[i] = outputLanguagesParam.get(i);
        }
        TranslationParameters translationParametersObj = new TranslationParameters(toBeTranslatedWordTextInputParam, inputLanguages, outputLanguages, executeInBackgroundParam, maxHitsParam, durationForCancelSearchParam);
        TranslationExecution.executeTranslation(translationParametersObj);
    }

    public static StringColourItemText determineItemsFromContent(TextOfLanguage contentText, boolean changeInputAndOutputContent, boolean isInput) {
        StringColourItemText stringColourItemText = null;
        try {
            stringColourItemText = contentParserObj.determineItemsFromContent(contentText, changeInputAndOutputContent, isInput);
        } catch (Exception e) {
            Util.getUtil().log(e);
        }
        return stringColourItemText;
    }

    protected static native void callDeletePreviousTranslationResultJs();

    protected static native void callNewTranslationResultJs(TranslationResult resultOfTranslation);

    protected static native void exportStaticMethods();

    protected static native void exportDictionaryDataFileDataStructures();

    protected static native void exportPredefinedContent();

    protected static native String getCurrentURLJs();

    protected String getBaseDirectory() throws DictionaryException {
        final char pathSeparator = '/';
        String url = getCurrentURLJs();
        int pos = url.lastIndexOf(pathSeparator);
        if (pos < 0) throw new DictionaryException("URL could not be parsed");
        return url.substring(0, pos);
    }
}
