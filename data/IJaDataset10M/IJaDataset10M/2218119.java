package de.kugihan.dictionaryformids.hmi_java_me.mainform;

import de.kugihan.dictionaryformids.dataaccess.DictionaryDataFile;
import de.kugihan.dictionaryformids.general.DictionaryException;
import de.kugihan.dictionaryformids.hmi_java_me.DictionarySettings;
import de.kugihan.dictionaryformids.general.Util;
import de.kugihan.dictionaryformids.translation.TranslationExecution;

public class WordHistory {

    private int historySize;

    private String[] historyWord;

    private int[] fromLanguageHistory;

    private boolean[][] toLanguageHistory;

    private int curHistoryPos;

    private int maxHistoryPos;

    private boolean saveFlag;

    public WordHistory() {
        historySize = 10;
        historyWord = new String[historySize];
        fromLanguageHistory = new int[historySize];
        toLanguageHistory = new boolean[historySize][DictionaryDataFile.numberOfAvailableLanguages];
        curHistoryPos = -1;
        maxHistoryPos = -1;
        saveFlag = true;
    }

    public void saveHistoryWord() throws DictionaryException {
        if (saveFlag) {
            curHistoryPos++;
            if (curHistoryPos == historySize) {
                for (int i = 0; i < historySize - 1; i++) {
                    historyWord[i] = historyWord[i + 1];
                    fromLanguageHistory[i] = fromLanguageHistory[i + 1];
                    toLanguageHistory[i] = toLanguageHistory[i + 1];
                }
                curHistoryPos--;
            }
            historyWord[curHistoryPos] = MainForm.applicationMainForm.toBeTranslatedWordTextField.getString();
            fromLanguageHistory[curHistoryPos] = DictionarySettings.getInputLanguage();
            toLanguageHistory[curHistoryPos] = DictionarySettings.getOutputLanguage();
            maxHistoryPos = curHistoryPos;
        }
    }

    public void doBackWord() throws DictionaryException {
        if (curHistoryPos > 0) {
            curHistoryPos--;
            setCurHistoryWord();
        }
    }

    public void doForwardWord() throws DictionaryException {
        if (curHistoryPos < maxHistoryPos) {
            curHistoryPos++;
            setCurHistoryWord();
        }
    }

    protected void setCurHistoryWord() throws DictionaryException {
        saveFlag = false;
        MainForm.applicationMainForm.removeStartupDisplay();
        MainForm.applicationMainForm.toBeTranslatedWordTextField.setString(historyWord[curHistoryPos]);
        MainForm.applicationMainForm.dictionarySettingFormObj.setInputLanguage(fromLanguageHistory[curHistoryPos]);
        MainForm.applicationMainForm.dictionarySettingFormObj.setOutputLanguage(toLanguageHistory[curHistoryPos]);
        MainForm.applicationMainForm.translateWord(historyWord[curHistoryPos], false);
        saveFlag = true;
    }
}
