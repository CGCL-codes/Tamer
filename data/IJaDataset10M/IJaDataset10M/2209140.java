package org.google.translate.desktop.settings.history;

import org.google.translate.api.v2.core.model.Language;
import org.google.translate.desktop.utils.Cache;
import org.google.translate.desktop.utils.SwingUtils;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

public class HistoryTableModel extends DefaultTableModel {

    public static final String DATE_COLUMN = SwingUtils.getMessage("historyDialog.historyTable.date");

    public static final String SOURCE_LANGUAGE = SwingUtils.getMessage("historyDialog.historyTable.sourceLanguage");

    public static final String TARGET_LANGUAGE = SwingUtils.getMessage("historyDialog.historyTable.targetLanguage");

    public static final String ORIGINAL_TEXT = SwingUtils.getMessage("historyDialog.historyTable.originalText");

    public static final String TRANSLATED_TEXT = SwingUtils.getMessage("historyDialog.historyTable.translatedText");

    public static final String[] COLUMN_NAMES = new String[] { DATE_COLUMN, SOURCE_LANGUAGE, TARGET_LANGUAGE, ORIGINAL_TEXT, TRANSLATED_TEXT };

    public HistoryTableModel() {
        super(COLUMN_NAMES, 0);
        Cache cache = Cache.instance();
        for (Map.Entry<Cache.HistoryKey, String> entry : cache.getHistory().entrySet()) {
            Vector<Object> row = new Vector<Object>(COLUMN_NAMES.length);
            Cache.HistoryKey historyKey = entry.getKey();
            row.add(new Date(historyKey.getTimestamp()));
            row.add(getLanguageName(historyKey.getOriginalLanguage()));
            row.add(getLanguageName(historyKey.getTranslatedLanguage()));
            row.add(historyKey.getOriginalText());
            row.add(entry.getValue());
            addRow(row);
        }
    }

    private String getLanguageName(String languageCode) {
        Language language = Cache.instance().getLanguage(languageCode);
        return (language != null && language.getName() != null) ? language.getName() : languageCode;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
