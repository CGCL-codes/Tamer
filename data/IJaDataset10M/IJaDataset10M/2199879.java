package raptor.pref.page;

import org.apache.commons.lang.WordUtils;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import raptor.Raptor;
import raptor.international.L10n;
import raptor.pref.PreferenceKeys;

public class ChatConsolePage extends FieldEditorPreferencePage {

    protected static L10n local = L10n.getInstance();

    public static final String[][] CONSOLE_CHARS = { { local.getString("char100t"), "100000" }, { local.getString("char250t"), "250000" }, { local.getString("char500t"), "500000" }, { local.getString("char1000t"), "1000000" } };

    public ChatConsolePage() {
        super(GRID);
        setTitle(local.getString("chatCons"));
        setPreferenceStore(Raptor.getInstance().getPreferences());
    }

    @Override
    protected void createFieldEditors() {
        ComboFieldEditor consoleChars = new ComboFieldEditor(PreferenceKeys.CHAT_MAX_CONSOLE_CHARS, local.getString("chatConsP1"), CONSOLE_CHARS, getFieldEditorParent());
        addField(consoleChars);
        ColorFieldEditor inputTextBackground = new ColorFieldEditor(PreferenceKeys.CHAT_INPUT_BACKGROUND_COLOR, local.getString("chatConsP2"), getFieldEditorParent());
        addField(inputTextBackground);
        ColorFieldEditor consoleBackground = new ColorFieldEditor(PreferenceKeys.CHAT_CONSOLE_BACKGROUND_COLOR, local.getString("chatConsP3"), getFieldEditorParent());
        addField(consoleBackground);
        FontFieldEditor inputFont = new FontFieldEditor(PreferenceKeys.CHAT_INPUT_FONT, local.getString("chatConsP4"), getFieldEditorParent());
        addField(inputFont);
        ColorFieldEditor outputTextBackground = new ColorFieldEditor(PreferenceKeys.CHAT_OUTPUT_BACKGROUND_COLOR, local.getString("chatConsP5"), getFieldEditorParent());
        addField(outputTextBackground);
        ColorFieldEditor promptColor = new ColorFieldEditor(PreferenceKeys.CHAT_PROMPT_COLOR, local.getString("chatConsP6"), getFieldEditorParent());
        addField(promptColor);
        ColorFieldEditor outputTextForeground = new ColorFieldEditor(PreferenceKeys.CHAT_OUTPUT_TEXT_COLOR, local.getString("chatConsP7"), getFieldEditorParent());
        addField(outputTextForeground);
        addField(new FontFieldEditor(PreferenceKeys.CHAT_OUTPUT_FONT, local.getString("chatConsP8"), getFieldEditorParent()));
        ColorFieldEditor linkTextColor = new ColorFieldEditor(PreferenceKeys.CHAT_LINK_UNDERLINE_COLOR, local.getString("chatConsP9"), getFieldEditorParent());
        addField(linkTextColor);
        ColorFieldEditor quoteUnderlineColor = new ColorFieldEditor(PreferenceKeys.CHAT_QUOTE_UNDERLINE_COLOR, local.getString("chatConsP10"), getFieldEditorParent());
        addField(quoteUnderlineColor);
        Label warningLabel = new Label(getFieldEditorParent(), SWT.NONE);
        warningLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
        warningLabel.setText(WordUtils.wrap(local.getString("chatConsP11"), 70));
    }
}
