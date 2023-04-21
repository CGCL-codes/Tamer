package pt.igeo.snig.mig.editor.ui.recordEditor;

import java.awt.Component;
import pt.igeo.snig.mig.editor.config.ConfigManager;
import pt.igeo.snig.mig.editor.constants.Constants;
import pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.FormFillerController;
import pt.igeo.snig.mig.editor.ui.recordEditor.htmlView.HtmlViewController;
import pt.igeo.snig.mig.editor.ui.recordEditor.massEditor.MassEditorController;
import pt.igeo.snig.mig.editor.ui.recordEditor.xmlView.XmlViewController;
import fi.mmm.yhteinen.swing.core.YApplicationEvent;
import fi.mmm.yhteinen.swing.core.YController;

/**
 * Controller for record editor component.
 * 
 * @author Ant�nio Silva
 * @version $Revision: 11328 $
 * @since 1.0
 */
public class RecordEditorController extends YController {

    /** the controller view */
    private RecordEditorView recordEditorView = new RecordEditorView();

    /** Form filler */
    private FormFillerController formFillerController = new FormFillerController();

    /** Html viewer */
    private HtmlViewController htmlViewController = new HtmlViewController();

    /** Xml viewer */
    private XmlViewController xmlViewController = new XmlViewController();

    /** Mass viewer */
    private MassEditorController massEditorController = new MassEditorController();

    /** default constructor */
    public RecordEditorController() {
        setUpMVC(null, recordEditorView);
        int lastEditorSelection = ConfigManager.getInstance().getLastEditor();
        recordEditorView.addComponents((Component) formFillerController.getView(), (Component) htmlViewController.getView(), (Component) xmlViewController.getView(), (Component) massEditorController.getView());
        addChildren();
        this.register(Constants.translateEvent);
        recordEditorView.setSelectedIndex(lastEditorSelection);
    }

    /**
	 * Adding child controllers. This creates hierarchical structure for controllers, which could be used for
	 * implementing chain of responsibility pattern.
	 */
    private void addChildren() {
        this.addChild(formFillerController);
        this.addChild(htmlViewController);
        this.addChild(xmlViewController);
        this.addChild(massEditorController);
    }

    /**
	 * Handling of Global events.
	 * 
	 * @param ev
	 */
    public void receivedApplicationEvent(YApplicationEvent ev) {
        if (ev.getName() == Constants.translateEvent) {
            recordEditorView.translate();
        }
    }

    /**
	 * Saves current selected tab
	 */
    public void editorTabSelectionChanged() {
        ConfigManager.getInstance().setLastEditor(recordEditorView.getSelectedIndex());
        if (recordEditorView.getSelectedIndex() == 2) {
            sendApplicationEvent(new YApplicationEvent(Constants.htmlFocus));
        } else {
            sendApplicationEvent(new YApplicationEvent(Constants.htmlOutOfFocus));
        }
    }

    /**
	 * 
	 * @return true if unsaved changes exist
	 */
    public boolean hasUnsavedChanges() {
        return (formFillerController.getChanged() && formFillerController.hasRecords());
    }

    /**
	 * Saves configuration
	 */
    public void saveConfig() {
        formFillerController.saveConfig();
    }
}
