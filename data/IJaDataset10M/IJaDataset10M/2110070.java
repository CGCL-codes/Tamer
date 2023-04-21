package net.sourceforge.eclipsex.preferences;

import java.io.IOException;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;

/**
 * @author Enrico Benedetti
 *
 */
public class EXPreferencesDefault implements EXPreferences {

    private static final String SDK_PATH = "sdkPath";

    public static final String MATCHING_BRACKETS = "matchingBrackets";

    public static final String MATCHING_BRACKETS_COLOR = "matchingBracketsColor";

    private static final String LINE_NUMBER_RULER = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_LINE_NUMBER_RULER;

    private static final String CURRENT_LINE = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_CURRENT_LINE;

    private static final String CURRENT_LINE_COLOR = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_CURRENT_LINE_COLOR;

    private static final String PRINT_MARGIN = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_PRINT_MARGIN;

    private static final String PRINT_MARGIN_COLUMN = AbstractDecoratedTextEditorPreferenceConstants.EDITOR_PRINT_MARGIN_COLUMN;

    private static final String AUTO_CONTENT_ASSISTANT = "autoContentAssistant";

    private String _sdkPath;

    private boolean _showBrackets;

    private boolean _showLineNumbers;

    private boolean _currentLineHighlight;

    private boolean _showPrintMargin;

    private boolean _autoContentAssistant;

    private final IPreferenceStore _store;

    public EXPreferencesDefault(final IPreferenceStore store) {
        _store = store;
        _sdkPath = store.getString(SDK_PATH);
        _showBrackets = store.getBoolean(MATCHING_BRACKETS);
        _showLineNumbers = store.getBoolean(LINE_NUMBER_RULER);
        _currentLineHighlight = store.getBoolean(CURRENT_LINE);
        _showPrintMargin = store.getBoolean(PRINT_MARGIN);
        _autoContentAssistant = store.getBoolean(AUTO_CONTENT_ASSISTANT);
    }

    public String getSdkPath() {
        return _sdkPath;
    }

    public boolean isShowBrackets() {
        return _showBrackets;
    }

    public boolean isShowLineNumbers() {
        return _showLineNumbers;
    }

    public boolean isCurrentLineHighlight() {
        return _currentLineHighlight;
    }

    public boolean isShowPrintMargin() {
        return _showPrintMargin;
    }

    public void setSdkPath(final String sdkPath) {
        _sdkPath = sdkPath;
    }

    public void setShowBrackets(final boolean showBrackets) {
        _showBrackets = showBrackets;
    }

    public void setShowLineNumbers(final boolean showLineNumbers) {
        _showLineNumbers = showLineNumbers;
    }

    public void setCurrentLineHighlight(final boolean currentLineHighlight) {
        _currentLineHighlight = currentLineHighlight;
    }

    public void setShowPrintMargin(final boolean showPrintMargin) {
        _showPrintMargin = showPrintMargin;
    }

    public void save() {
        _store.setValue(SDK_PATH, _sdkPath);
        _store.setValue(MATCHING_BRACKETS, _showBrackets);
        _store.setValue(LINE_NUMBER_RULER, _showLineNumbers);
        _store.setValue(CURRENT_LINE, _currentLineHighlight);
        _store.setValue(PRINT_MARGIN, _showPrintMargin);
        _store.setValue(AUTO_CONTENT_ASSISTANT, _autoContentAssistant);
        try {
            ((IPersistentPreferenceStore) _store).save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void performDefaults() {
        performDefaults(_store);
    }

    public static void performDefaults(final IPreferenceStore store) {
        store.setDefault(MATCHING_BRACKETS, true);
        store.setDefault(CURRENT_LINE, true);
        PreferenceConverter.setDefault(store, CURRENT_LINE_COLOR, new RGB(232, 242, 254));
        store.setDefault(PRINT_MARGIN, false);
        store.setDefault(PRINT_MARGIN_COLUMN, 80);
        store.setDefault(AUTO_CONTENT_ASSISTANT, true);
    }

    public boolean isAutoContentAssistant() {
        return _autoContentAssistant;
    }

    public void setAutoContentAssistant(final boolean autoContentAssistant) {
        _autoContentAssistant = autoContentAssistant;
    }
}
