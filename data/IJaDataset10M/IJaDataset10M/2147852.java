package org.wsmostudio.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.*;
import org.eclipse.ui.*;
import org.wsmostudio.ui.WsmoUIPlugin;
import org.wsmostudio.ui.editors.text.WSMOTextEditor;

/**
 * A preference page which defines different colors for the syntax highlighting in the
 * WSMO Text editor.
 *
 * @author not attributable
 * @version $Revision: 545 $ $Date: 2006-01-26 06:57:14 -0500 (Thu, 26 Jan 2006) $
 */
public class TextEditorColorsPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private static final String TEXT_EDITOR_SYSTEM_COLOR = "$WSMOTextEditor$system_color";

    private static final String TEXT_EDITOR_KEY_COLOR = "$WSMOTextEditor$key_color";

    private static final String TEXT_EDITOR_AUX_COLOR = "$WSMOTextEditor$aux_color";

    private static final String TEXT_EDITOR_STR_COLOR = "$WSMOTextEditor$str_color";

    private static final String TEXT_EDITOR_COMM_COLOR = "$WSMOTextEditor$comm_color";

    private static final String TEXT_EDITOR_DEFAULT_COLOR = "$WSMOTextEditor$def_color";

    static final String STYLE_SUFFIX = "_style";

    private ColorFieldEditor systemChooser, keywordsChooser, auxwordsChooser, stringsChooser, commentsChooser, defaultChooser;

    public TextEditorColorsPage() {
        super("Text Editor Syntax Coloring", FieldEditorPreferencePage.GRID);
        setPreferenceStore(WsmoUIPlugin.getDefault().getPreferenceStore());
        initColors();
    }

    protected void createFieldEditors() {
        getFieldEditorParent().setLayout(new GridLayout(4, false));
        systemChooser = new TextStyleFieldEditor(TEXT_EDITOR_SYSTEM_COLOR, "System Words : ", getFieldEditorParent());
        addField(systemChooser);
        keywordsChooser = new TextStyleFieldEditor(TEXT_EDITOR_KEY_COLOR, "Keywords : ", getFieldEditorParent());
        addField(keywordsChooser);
        auxwordsChooser = new TextStyleFieldEditor(TEXT_EDITOR_AUX_COLOR, "Auxiliary Words : ", getFieldEditorParent());
        addField(auxwordsChooser);
        stringsChooser = new TextStyleFieldEditor(TEXT_EDITOR_STR_COLOR, "String Values : ", getFieldEditorParent());
        addField(stringsChooser);
        commentsChooser = new TextStyleFieldEditor(TEXT_EDITOR_COMM_COLOR, "Comments : ", getFieldEditorParent());
        addField(commentsChooser);
        defaultChooser = new TextStyleFieldEditor(TEXT_EDITOR_DEFAULT_COLOR, "Default : ", getFieldEditorParent());
        addField(defaultChooser);
    }

    public void init(IWorkbench workbench) {
    }

    public boolean performOk() {
        boolean stat = super.performOk();
        doUpdateOpenedEditors();
        return stat;
    }

    protected void performDefaults() {
        PreferenceConverter.setDefault(getPreferenceStore(), TEXT_EDITOR_SYSTEM_COLOR, WSMOTextEditor.SYSTEM_COLOR);
        PreferenceConverter.setDefault(getPreferenceStore(), TEXT_EDITOR_KEY_COLOR, WSMOTextEditor.KEY_COLOR);
        PreferenceConverter.setDefault(getPreferenceStore(), TEXT_EDITOR_AUX_COLOR, WSMOTextEditor.AUX_COLOR);
        PreferenceConverter.setDefault(getPreferenceStore(), TEXT_EDITOR_STR_COLOR, WSMOTextEditor.STRING_COLOR);
        PreferenceConverter.setDefault(getPreferenceStore(), TEXT_EDITOR_COMM_COLOR, WSMOTextEditor.COMMENT_COLOR);
        PreferenceConverter.setDefault(getPreferenceStore(), TEXT_EDITOR_DEFAULT_COLOR, WSMOTextEditor.DEFAULT_COLOR);
        getPreferenceStore().setDefault(TEXT_EDITOR_SYSTEM_COLOR + STYLE_SUFFIX, WSMOTextEditor.SYSTEM_STYLE);
        getPreferenceStore().setDefault(TEXT_EDITOR_KEY_COLOR + STYLE_SUFFIX, WSMOTextEditor.KEY_STYLE);
        getPreferenceStore().setDefault(TEXT_EDITOR_AUX_COLOR + STYLE_SUFFIX, WSMOTextEditor.AUX_STYLE);
        getPreferenceStore().setDefault(TEXT_EDITOR_STR_COLOR + STYLE_SUFFIX, WSMOTextEditor.STRING_STYLE);
        getPreferenceStore().setDefault(TEXT_EDITOR_COMM_COLOR + STYLE_SUFFIX, WSMOTextEditor.COMMENT_STYLE);
        getPreferenceStore().setDefault(TEXT_EDITOR_DEFAULT_COLOR + STYLE_SUFFIX, WSMOTextEditor.DEFAULT_STYLE);
        super.performDefaults();
    }

    private void initColors() {
        initColorEntry(TEXT_EDITOR_SYSTEM_COLOR, WSMOTextEditor.SYSTEM_COLOR, WSMOTextEditor.SYSTEM_STYLE);
        initColorEntry(TEXT_EDITOR_KEY_COLOR, WSMOTextEditor.KEY_COLOR, WSMOTextEditor.KEY_STYLE);
        initColorEntry(TEXT_EDITOR_AUX_COLOR, WSMOTextEditor.AUX_COLOR, WSMOTextEditor.AUX_STYLE);
        initColorEntry(TEXT_EDITOR_STR_COLOR, WSMOTextEditor.STRING_COLOR, WSMOTextEditor.STRING_STYLE);
        initColorEntry(TEXT_EDITOR_COMM_COLOR, WSMOTextEditor.COMMENT_COLOR, WSMOTextEditor.COMMENT_STYLE);
        initColorEntry(TEXT_EDITOR_DEFAULT_COLOR, WSMOTextEditor.DEFAULT_COLOR, WSMOTextEditor.DEFAULT_STYLE);
    }

    private void doUpdateOpenedEditors() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page == null) {
            return;
        }
        IEditorReference[] editors = page.getEditorReferences();
        if (editors == null) {
            return;
        }
        for (int i = 0; i < editors.length; i++) {
            IEditorPart editor = editors[i].getEditor(true);
            if (editor != null && editor instanceof WSMOTextEditor) {
                ((WSMOTextEditor) editor).doUpdateView();
            }
        }
    }

    private void initColorEntry(String key, RGB colorInfo, int style) {
        if (getPreferenceStore().contains(key)) {
            return;
        }
        PreferenceConverter.setValue(getPreferenceStore(), key, colorInfo);
        PreferenceConverter.setDefault(getPreferenceStore(), key, colorInfo);
        getPreferenceStore().setValue(key + STYLE_SUFFIX, style);
        getPreferenceStore().setDefault(key + STYLE_SUFFIX, style);
    }

    public void dispose() {
        systemChooser.dispose();
        keywordsChooser.dispose();
        auxwordsChooser.dispose();
        stringsChooser.dispose();
        commentsChooser.dispose();
        defaultChooser.dispose();
    }

    public static RGB getSystemColor() {
        if (false == WsmoUIPlugin.getDefault().getPreferenceStore().contains(TEXT_EDITOR_SYSTEM_COLOR)) {
            return WSMOTextEditor.SYSTEM_COLOR;
        }
        return PreferenceConverter.getColor(WsmoUIPlugin.getDefault().getPreferenceStore(), TEXT_EDITOR_SYSTEM_COLOR);
    }

    public static int getSystemStyle() {
        if (false == WsmoUIPlugin.getDefault().getPreferenceStore().contains(TEXT_EDITOR_SYSTEM_COLOR + STYLE_SUFFIX)) {
            return WSMOTextEditor.SYSTEM_STYLE;
        }
        return WsmoUIPlugin.getDefault().getPreferenceStore().getInt(TEXT_EDITOR_SYSTEM_COLOR + STYLE_SUFFIX);
    }

    public static RGB getKeywordsColor() {
        if (false == WsmoUIPlugin.getDefault().getPreferenceStore().contains(TEXT_EDITOR_KEY_COLOR)) {
            return WSMOTextEditor.KEY_COLOR;
        }
        return PreferenceConverter.getColor(WsmoUIPlugin.getDefault().getPreferenceStore(), TEXT_EDITOR_KEY_COLOR);
    }

    public static int getKeywordsStyle() {
        if (false == WsmoUIPlugin.getDefault().getPreferenceStore().contains(TEXT_EDITOR_KEY_COLOR + STYLE_SUFFIX)) {
            return WSMOTextEditor.KEY_STYLE;
        }
        return WsmoUIPlugin.getDefault().getPreferenceStore().getInt(TEXT_EDITOR_KEY_COLOR + STYLE_SUFFIX);
    }

    public static RGB getAuxwordsColor() {
        if (false == WsmoUIPlugin.getDefault().getPreferenceStore().contains(TEXT_EDITOR_AUX_COLOR)) {
            return WSMOTextEditor.AUX_COLOR;
        }
        return PreferenceConverter.getColor(WsmoUIPlugin.getDefault().getPreferenceStore(), TEXT_EDITOR_AUX_COLOR);
    }

    public static int getAuxwordsStyle() {
        if (false == WsmoUIPlugin.getDefault().getPreferenceStore().contains(TEXT_EDITOR_AUX_COLOR + STYLE_SUFFIX)) {
            return WSMOTextEditor.AUX_STYLE;
        }
        return WsmoUIPlugin.getDefault().getPreferenceStore().getInt(TEXT_EDITOR_AUX_COLOR + STYLE_SUFFIX);
    }

    public static RGB getStringsColor() {
        if (false == WsmoUIPlugin.getDefault().getPreferenceStore().contains(TEXT_EDITOR_STR_COLOR)) {
            return WSMOTextEditor.STRING_COLOR;
        }
        return PreferenceConverter.getColor(WsmoUIPlugin.getDefault().getPreferenceStore(), TEXT_EDITOR_STR_COLOR);
    }

    public static int getStringsStyle() {
        if (false == WsmoUIPlugin.getDefault().getPreferenceStore().contains(TEXT_EDITOR_STR_COLOR + STYLE_SUFFIX)) {
            return WSMOTextEditor.STRING_STYLE;
        }
        return WsmoUIPlugin.getDefault().getPreferenceStore().getInt(TEXT_EDITOR_STR_COLOR + STYLE_SUFFIX);
    }

    public static RGB getCommentColor() {
        if (false == WsmoUIPlugin.getDefault().getPreferenceStore().contains(TEXT_EDITOR_COMM_COLOR)) {
            return WSMOTextEditor.COMMENT_COLOR;
        }
        return PreferenceConverter.getColor(WsmoUIPlugin.getDefault().getPreferenceStore(), TEXT_EDITOR_COMM_COLOR);
    }

    public static int getCommentStyle() {
        if (false == WsmoUIPlugin.getDefault().getPreferenceStore().contains(TEXT_EDITOR_COMM_COLOR + STYLE_SUFFIX)) {
            return WSMOTextEditor.COMMENT_STYLE;
        }
        return WsmoUIPlugin.getDefault().getPreferenceStore().getInt(TEXT_EDITOR_COMM_COLOR + STYLE_SUFFIX);
    }

    public static RGB getDefaultColor() {
        if (false == WsmoUIPlugin.getDefault().getPreferenceStore().contains(TEXT_EDITOR_DEFAULT_COLOR)) {
            return WSMOTextEditor.DEFAULT_COLOR;
        }
        return PreferenceConverter.getColor(WsmoUIPlugin.getDefault().getPreferenceStore(), TEXT_EDITOR_DEFAULT_COLOR);
    }

    public static int getDefaultStyle() {
        if (false == WsmoUIPlugin.getDefault().getPreferenceStore().contains(TEXT_EDITOR_DEFAULT_COLOR + STYLE_SUFFIX)) {
            return WSMOTextEditor.DEFAULT_STYLE;
        }
        return WsmoUIPlugin.getDefault().getPreferenceStore().getInt(TEXT_EDITOR_DEFAULT_COLOR + STYLE_SUFFIX);
    }
}
