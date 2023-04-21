package org.colombbus.tangara.ide.controller.action;

import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.colombbus.tangara.ide.model.Utils;
import org.colombbus.tangara.ide.model.codeeditor.CodeEditor;
import org.colombbus.tangara.ide.model.codeeditor.CodeEditorManager;
import org.colombbus.tangara.ide.model.codeeditor.DefaultCodeEditor;
import org.colombbus.tangara.ide.view.codeeditor.CodeEditorPane;
import org.colombbus.tangara.ide.view.codeeditor.CodeEditorTabManager;
import org.colombbus.tangara.ide.view.codeeditor.TabPanel;

/**
 * Save code editor action.
 *
 * @author Aurelien Bourdon <aurelien.bourdon@gmail.com>
 */
@SuppressWarnings("serial")
public class SaveCodeEditorAction extends TangaraAction {

    /** Class logger */
    private static final Logger LOG = Logger.getLogger(SaveCodeEditorAction.class);

    /** The associated {@link CodeEditorTabManager} */
    private CodeEditorTabManager codeEditorTabManager;

    /** The associated {@link CodeEditorManager} */
    private CodeEditorManager codeEditorManager;

    /**
	 * Create a new save code editor action with the associated
	 * {@link CodeEditorManager}.
	 *
	 * @param codeEditorTabManager
	 *            The associated {@link CodeEditorTabManager}.
	 * @throws IllegalArgumentException
	 *             Thrown if codeEditorManager is <code>null</code>.
	 */
    public SaveCodeEditorAction(CodeEditorTabManager codeEditorTabManager) throws IllegalArgumentException {
        Validate.notNull(codeEditorTabManager, "codeEditorManager argument is null");
        this.codeEditorTabManager = codeEditorTabManager;
        codeEditorManager = codeEditorTabManager.getCodeEditorManager();
        putValue(LARGE_ICON_KEY, RESOURCE_BUNDLE.getIcon("SaveCodeEditorAction.largeIcon"));
        putValue(SHORT_DESCRIPTION, RESOURCE_BUNDLE.getString("SaveCodeEditorAction.shortDescription"));
    }

    /**
	 * The saving file name's absolute path name. Only set by
	 * {@link #setAbsolutePathName()}
	 */
    private String absolutePathName;

    /**
	 * Set the code editor's absolute path name.
	 */
    private void setAbsolutePathName() {
        absolutePathName = selectWithFileChooser();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        CodeEditorPane codeEditorPane = (CodeEditorPane) codeEditorTabManager.getSelectedComponent();
        CodeEditor currentCodeEditor = codeEditorManager.getCurrentCodeEditor();
        currentCodeEditor.setScript(codeEditorPane.getText());
        try {
            absolutePathName = currentCodeEditor.getAbsolutePathName();
        } catch (IllegalStateException state) {
            setAbsolutePathName();
            if (absolutePathName == null) return;
            currentCodeEditor.setAbsolutePathName(absolutePathName);
        }
        if (!Utils.saveCodeEditor(currentCodeEditor)) {
            LOG.error("file not saved");
            return;
        }
        if (currentCodeEditor.isNeededSave()) ((DefaultCodeEditor) currentCodeEditor).setNeededSave(false); else throw new IllegalStateException("current code editor is needed to save");
        int indexOfCodeEditorPane = codeEditorTabManager.indexOfComponent(codeEditorPane);
        TabPanel tabPanel = (TabPanel) codeEditorTabManager.getTabComponentAt(indexOfCodeEditorPane);
        tabPanel.updateTitle();
    }

    /**
	 * Show a file chooser to get the file path to save.
	 * <p>
	 * Because this method create and show some Swing's components, <b>it should
	 * be into the AwT's EDT</b>.
	 * </p>
	 *
	 * @return The file path or <code>null</code> if user had canceled the
	 *         operation.
	 */
    private String selectWithFileChooser() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter tgrFilter = new FileNameExtensionFilter(RESOURCE_BUNDLE.getString("TangaraAction.extensionName"), RESOURCE_BUNDLE.getString("TangaraAction.extensionName"));
        chooser.addChoosableFileFilter(tgrFilter);
        int returnVal = chooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) return chooser.getSelectedFile().getAbsolutePath();
        return null;
    }
}
