package org.qtitools.mathqurate.view;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.LayoutConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.imsglobal.xsd.imsqti_v2p1.AssessmentItemType;
import org.qtitools.mathqurate.controller.DefaultController;
import org.qtitools.mathqurate.model.AssessmentItemTreeModel;
import org.qtitools.mathqurate.model.AttribValue;
import org.qtitools.mathqurate.model.MQContentPackage;
import org.qtitools.mathqurate.model.MQMetadata;
import org.qtitools.mathqurate.model.MQModel;
import org.qtitools.mathqurate.model.MQOutcomeDeclaration;
import org.qtitools.mathqurate.model.MQResponseDeclaration;
import org.qtitools.mathqurate.model.MQTemplateDeclaration;
import org.qtitools.mathqurate.model.MQTemplateResponseProcessing;
import org.qtitools.mathqurate.utilities.CPBuildException;
import org.qtitools.mathqurate.utilities.JAXBCommentFactory;
import org.qtitools.qti.node.content.ItemBody;
import org.qtitools.qti.node.item.AssessmentItem;
import org.qtitools.qti.rendering.Renderer;
import org.qtitools.validatr.model.ValidatorModel;
import org.qtitools.validatr.panel.MainPanel;
import org.qtitools.validatr.settings.Settings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The Class MQMain. The main window for Mathqurate
 * 
 * @author James Annesley <j.annesley@kingston.ac.uk>
 */
public class MQMain extends AbstractApplicationWindow {

    private static String passedFilename;

    private static boolean spartacusMode = false;

    private MQMain dis = this;

    /**
	 * The Class AboutAction.
	 */
    private class AboutAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new about action.
		 * 
		 * @param window
		 *            the window
		 */
        public AboutAction(ApplicationWindow window) {
            _window = window;
            setText("About MathQurate");
            setToolTipText("About MathQurate");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_app_katuberling.png")));
        }

        @Override
        public void run() {
            controller.getHelp();
        }
    }

    /**
	 * The Class AboutAction.
	 */
    private class SimpleChoiceQuickAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new about action.
		 * 
		 * @param window
		 *            the window
		 */
        public SimpleChoiceQuickAction(ApplicationWindow window) {
            _window = window;
            setText("Quick Simple Choice Editor");
            setToolTipText("Create a simple choice (multiple choice) question using the quick editor window.");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-wizard.png")));
        }

        @Override
        public void run() {
            String newXml = SimpleChoiceWizard.makeMCQ();
            File f = new File(MQMain.tmpdirpath + File.separator + "fromMCQWiz.xml");
            try {
                FileUtils.writeStringToFile(f, newXml, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller.importAssessmentItemType(f.getAbsolutePath());
        }
    }

    /**
	 * The Class PreferencesAction.
	 */
    private class PreferencesAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new about action.
		 * 
		 * @param window
		 *            the window
		 */
        public PreferencesAction(ApplicationWindow window) {
            _window = window;
        }

        @Override
        public void run() {
            if (settingsWindow != null) {
                settingsWindow.close();
            }
            settingsWindow = new SettingsWindow(windowMathQurateMain.getShell());
            settingsWindow.open();
        }
    }

    /**
	 * The Class HelpAction.
	 */
    private class HelpAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new help action.
		 * 
		 * @param window
		 *            the window
		 */
        public HelpAction(ApplicationWindow window) {
            _window = window;
            setText("Help browser");
            setToolTipText("Help browser");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_app_susehelpcenter.png")));
        }

        @Override
        public void run() {
            HelpView helpView = new HelpView(getShell());
            helpView.open();
            helpView.getShell().setSize(1024, 768);
            helpView.getShell().setLocation(0, 0);
        }
    }

    /**
	 * The Class SettingsWindow.
	 */
    private class SettingsWindowAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new exit action.
		 * 
		 * @param window
		 *            the window
		 */
        public SettingsWindowAction(ApplicationWindow window) {
            _window = window;
            setText("Options");
            setToolTipText("Change options for MAEngine, Minibix and taxonomy");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_app_Network_Connection_Manager.png")));
        }

        @Override
        public void run() {
            if (settingsWindow != null) {
                settingsWindow.close();
            }
            settingsWindow = new SettingsWindow(windowMathQurateMain.getShell());
            settingsWindow.open();
        }
    }

    /**
	 * The Class ExitAction.
	 */
    private class ExitAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new exit action.
		 * 
		 * @param window
		 *            the window
		 */
        public ExitAction(ApplicationWindow window) {
            _window = window;
            setText("E&xit");
            setToolTipText("Exit application");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_action_exit.png")));
        }

        @Override
        public void run() {
            _window.close();
        }
    }

    /**
	 * The Class ExportMinibixAction.
	 */
    private class ExportMinibixAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new export minibix action.
		 * 
		 * @param window
		 *            the window
		 */
        public ExportMinibixAction(ApplicationWindow window) {
            _window = window;
            setText("Export question to Minibix");
            setToolTipText("Export question to Minibix");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_app_sharemanager.png")));
        }

        @Override
        public void run() {
            controller.getMQContentPackageAndPost();
        }
    }

    /**
	 * The Class ExportXMLAction.
	 */
    private class ExportXMLAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new export XML action.
		 * 
		 * @param window
		 *            the window
		 */
        public ExportXMLAction(ApplicationWindow window) {
            _window = window;
            setText("Export question as XML");
            setToolTipText("Export question as XML");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_device_usbpendrive_unmount.png")));
        }

        @Override
        public void run() {
            FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
            fd.setText("Export question as XML");
            fd.setFilterPath(lastdir);
            String[] filterExt = { "*.xml" };
            fd.setFilterExtensions(filterExt);
            String selected = fd.open();
            if (selected != null) {
                if (selected.length() > 0) {
                    if (selected.length() < 5) {
                        selected = selected.concat(".xml");
                    } else if (selected.length() > 5) {
                        if (selected.substring(selected.length() - 4).equals(".xml")) {
                        } else {
                            selected = selected.concat(".xml");
                        }
                    }
                    File file = new File(selected);
                    if (file.exists()) {
                        MessageBox mb = new MessageBox(fd.getParent(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
                        mb.setMessage(selected + " already exists. Do you want to replace it?");
                        if (mb.open() == SWT.NO) {
                            return;
                        }
                    }
                    lastdir = file.getAbsolutePath().replace(file.getName(), "");
                    controller.exportAssessmentItemType(selected);
                }
            }
        }
    }

    /**
	 * The Class ImportMinibixAction.
	 */
    private class ImportMinibixAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new import minibix action.
		 * 
		 * @param window
		 *            the window
		 */
        public ImportMinibixAction(ApplicationWindow window) {
            _window = window;
            setText("Import question from Minibix");
            setToolTipText("Import question from Minibix");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_app_network.png")));
        }

        @Override
        public void run() {
            controller.importFromMinibix();
            itemBodyView.close();
        }
    }

    /**
	 * The Class ImportXMLAction.
	 */
    private class ImportXMLAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new import xml action.
		 * 
		 * @param window
		 *            the window
		 */
        public ImportXMLAction(ApplicationWindow window) {
            _window = window;
            setText("Import question as XML");
            setToolTipText("Import question as XML");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_device_usbpendrive_mount.png")));
        }

        @Override
        public void run() {
            FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
            fd.setText("Import question as XML");
            fd.setFilterPath(lastdir);
            String[] filterExt = { "*.xml" };
            fd.setFilterExtensions(filterExt);
            String selected = fd.open();
            if (selected != null) {
                if (selected.length() > 0) {
                    if (selected.length() > 5) {
                        if (selected.substring(selected.length() - 4).equals(".xml")) {
                            File file = new File(selected);
                            lastdir = file.getAbsolutePath().replace(file.getName(), "");
                            Object[] validationResults = JAXBCommentFactory.validateAgainstSchema(new File(selected), true);
                            if (validationResults == null) {
                                controller.importAssessmentItemType(selected);
                                itemBodyView.close();
                            } else {
                                int lineNo = (Integer) validationResults[0] - 1;
                                int colNo = (Integer) validationResults[1];
                                String message = (String) validationResults[2];
                                message = "This is not a valid QTI2.1/MA XML file. Incorrect XML at line " + lineNo + ", column " + colNo + "\n\n" + "The parsing error was: " + message;
                                MessageBox mb = new MessageBox(fd.getParent(), SWT.ERROR | SWT.OK);
                                mb.setMessage("Error in XML");
                                mb.setMessage(message);
                                mb.open();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * The Class ItemBodyAction.
	 */
    private class ItemBodyAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new item body action.
		 * 
		 * @param window
		 *            the window
		 */
        public ItemBodyAction(ApplicationWindow window) {
            _window = window;
            setText("Question body");
            setToolTipText("Question body");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_action_view_detailed.png")));
        }

        @Override
        public void run() {
            itemBodyView._reloadDesignViewAction.run();
            itemBodyView.getShell().setVisible(true);
            itemBodyView.getShell().setActive();
        }
    }

    /**
	 * The Class LoadAction.
	 */
    private class LoadAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new load action.
		 * 
		 * @param window
		 *            the window
		 */
        public LoadAction(ApplicationWindow window) {
            _window = window;
            setText("Load");
            setToolTipText("Load content package");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_filesystem_folder_yellow.png")));
        }

        @Override
        public void run() {
            FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
            fd.setText("Open content package");
            fd.setFilterPath(lastdir);
            String[] filterExt = { "*.zip" };
            fd.setFilterExtensions(filterExt);
            String selected = fd.open();
            if (selected != null) {
                if (selected.length() > 0) {
                    if (selected.length() > 5) {
                        if (selected.substring(selected.length() - 4).equals(".zip")) {
                            File file = new File(selected);
                            lastdir = file.getAbsolutePath().replace(file.getName(), "");
                            Object[] bits = { file };
                            controller.importContentPackage(bits);
                            itemBodyView.close();
                        }
                    }
                }
            }
        }
    }

    /**
	 * The Class MetadataAction.
	 */
    private class MetadataAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new metadata action.
		 * 
		 * @param window
		 *            the window
		 */
        public MetadataAction(ApplicationWindow window) {
            _window = window;
            setText("Meta-data");
            setToolTipText("Meta-data");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_app_winprops.png")));
        }

        @Override
        public void run() {
            controller.getMQMetadata();
            metadataView.getShell().setVisible(true);
            metadataView.getShell().setActive();
        }
    }

    /**
	 * The Class MyContentProvider.
	 */
    private class MyContentProvider implements ITreeContentProvider {

        public void dispose() {
        }

        public Object[] getChildren(Object parentElement) {
            return getElements(parentElement);
        }

        public Object[] getElements(Object inputElement) {
            return ((AssessmentItemTreeModel) inputElement).child.toArray();
        }

        public Object getParent(Object element) {
            if (element == null) {
                return null;
            }
            return ((AssessmentItemTreeModel) element).parent;
        }

        public boolean hasChildren(Object element) {
            return ((AssessmentItemTreeModel) element).child.size() > 0;
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    /**
	 * The Class NewAction.
	 */
    private class NewAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new new action.
		 * 
		 * @param window
		 *            the window
		 */
        public NewAction(ApplicationWindow window) {
            _window = window;
            setText("New");
            setToolTipText("New question");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_action_filenew.png")));
        }

        @Override
        public void run() {
            controller.newAssessmentItem();
        }
    }

    /**
	 * The Class ProcessingEditorChooserAction.
	 */
    private class ProcessingEditorChooserAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a processing editor chooser action dialog.
		 * 
		 * @param window
		 *            the window
		 */
        public ProcessingEditorChooserAction(ApplicationWindow window) {
            _window = window;
            setText("Question processing");
            setToolTipText("Question processing. Choose this for variable declarations and response and template processing");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_app_package_settings.png")));
        }

        @Override
        public void run() {
            controller.processingEditor();
        }
    }

    /**
	 * The Class RedoAction.
	 */
    private class RedoAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new redo action.
		 * 
		 * @param window
		 *            the window
		 */
        public RedoAction(ApplicationWindow window) {
            _window = window;
            setText("Redo");
            setToolTipText("Redo");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_action_reload_redo.png")));
        }

        @Override
        public void run() {
            controller.setRedo();
        }
    }

    /**
	 * The Class RendererAction.
	 */
    private class RendererAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new renderer action.
		 * 
		 * @param window
		 *            the window
		 */
        public RendererAction(ApplicationWindow window) {
            _window = window;
            setText("Test question");
            setToolTipText("Test the question with the embedded question rendering engine");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_app_clicknrun.png")));
        }

        @Override
        public void run() {
            controller.renderAssessmentItem();
        }
    }

    /**
	 * The Class SaveAction.
	 */
    private class SaveAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new save action.
		 * 
		 * @param window
		 *            the window
		 */
        public SaveAction(ApplicationWindow window) {
            _window = window;
            setText("Save");
            setToolTipText("Save content package");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_app_ark2.png")));
        }

        @Override
        public void run() {
            controller.getMQContentPackage();
        }
    }

    /**
	 * The Class UndoAction.
	 */
    private class UndoAction extends Action {

        /** The _window. */
        ApplicationWindow _window;

        /**
		 * Instantiates a new undo action.
		 * 
		 * @param window
		 *            the window
		 */
        public UndoAction(ApplicationWindow window) {
            _window = window;
            setText("Undo");
            setToolTipText("Undo");
            setImageDescriptor(ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/32px-Crystal_Clear_action_reload_undo.png")));
        }

        @Override
        public void run() {
            controller.setUndo();
        }
    }

    /** Logger. */
    public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MQMain.class);

    /** The _help action. */
    private HelpAction _helpAction = new HelpAction(this);

    /** The _about action. */
    private AboutAction _aboutAction = new AboutAction(this);

    /** The simple choice quick action */
    private SimpleChoiceQuickAction _simpleChoiceQuickAction = new SimpleChoiceQuickAction(this);

    /** The _preferences action */
    private PreferencesAction _preferencesAction = new PreferencesAction(this);

    /** The _settings window action */
    private SettingsWindowAction _settingsWindowAction = new SettingsWindowAction(this);

    /** The _exit action. */
    private ExitAction _exitAction = new ExitAction(this);

    /** The _new action. */
    private NewAction _newAction = new NewAction(this);

    /** The _load action. */
    private LoadAction _loadAction = new LoadAction(this);

    /** The _save action. */
    private SaveAction _saveAction = new SaveAction(this);

    /** The _import xml action. */
    private ImportXMLAction _importXMLAction = new ImportXMLAction(this);

    /** The _export xml action. */
    private ExportXMLAction _exportXMLAction = new ExportXMLAction(this);

    /** The _import minibix action. */
    private ImportMinibixAction _importMinibixAction = new ImportMinibixAction(this);

    /** The _export minibix action. */
    private ExportMinibixAction _exportMinibixAction = new ExportMinibixAction(this);

    /** The _undo action. */
    private UndoAction _undoAction = new UndoAction(this);

    /** The _redo action. */
    private RedoAction _redoAction = new RedoAction(this);

    /** The _metadata action. */
    private MetadataAction _metadataAction = new MetadataAction(this);

    /** The _item body action. */
    private ItemBodyAction _itemBodyAction = new ItemBodyAction(this);

    /** The _template processing action. */
    private ProcessingEditorChooserAction _processingEditorChooserAction = new ProcessingEditorChooserAction(this);

    /** The _renderer action. */
    private RendererAction _rendererAction = new RendererAction(this);

    private ToolBarManager fileBar;

    /** The mathqurate model. */
    static MQModel mathQurateModel;

    /** The window mathqurate main. */
    static MQMain windowMathQurateMain;

    /** The tree viewer. */
    private static TreeViewer treeViewer;

    /** The standard logfile */
    private static Text standardLog;

    /** The error log */
    private static Text errorLog;

    /** The tree item assessment item progress. */
    private static AssessmentItemTreeModel treeItemAssessmentItemProgress;

    /** The JQI renderer. */
    static Renderer JQTIrenderer = null;

    /** The JQI assessment item. */
    static AssessmentItem JQTIassessmentItem = null;

    /** The JQI item body. */
    static ItemBody JQTIitemBody = null;

    /** The last used directory. */
    private static String lastdir = null;

    /** The item body view. */
    private static ItemBodyView itemBodyView = null;

    /** The feedback block view. */
    static FeedbackBlockView feedbackBlockView = null;

    /** The feedback block view. */
    static TemplateBlockView templateBlockView = null;

    /** The view to enter maths using laTeX Math Mode. */
    static SnuggleTeXView snuggleTexView = null;

    /** The choice interaction view. */
    static ChoiceInteractionView choiceInteractionView = null;

    /** The feedback inline view. */
    static FeedbackInlineView feedbackInlineView = null;

    /** The template inline view. */
    static TemplateInlineView templateInlineView = null;

    /** The simple choice view. */
    static SimpleChoiceView simpleChoiceView = null;

    /** The end attempt interaction view. */
    static EndAttemptInteractionView endAttemptInteractionView = null;

    /** The math entry interaction view. */
    static MathEntryInteractionView mathEntryInteractionView = null;

    /** The text entry interaction view. */
    static TextEntryInteractionView textEntryInteractionView = null;

    /** The printed variable view. */
    static PrintedVariableView printedVariableView = null;

    /** The outcome declarations view. */
    static OutcomeDeclarationView outcomeDeclarationView = null;

    /** The template declarations view. */
    static TemplateDeclarationView templateDeclarationView = null;

    /** The response declarations view. */
    static ResponseDeclarationView responseDeclarationView = null;

    /** The inline choice interaction view. */
    static InlineChoiceInteractionView inlineChoiceInteractionView = null;

    /** The inline choice  view. */
    static InlineChoiceView inlineChoiceView = null;

    /** The metadata view. */
    static MetadataView metadataView = null;

    static SettingsWindow settingsWindow = null;

    /** The tmpdirpath. */
    public static String tmpdirpath;

    /** The tmpdir. 
	 * Stores the temporary directory as a file */
    static File tmpdir;

    /** The tmp url.
	 * Stores the temporary directory as a URL */
    public static URL tmpURL;

    /** The Design tmpdirpath.
	 * A modified <i>tempdir</i> for the design process */
    static String DesignTmpdirpath;

    /** The Design tmpdir url.
	 * A modified <i>tempURL</i> for the design process  */
    static URL DesignTmpdirURL;

    /** The interaction toolbar. */
    protected ToolBarManager interactionBar;

    /** The design edit toolbar. */
    protected ToolBarManager editBar;

    /**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
    public static void main(String[] args) {
        MQMain.logger.debug("MQMain: main");
        if (MQModel.consoleToFile) {
            try {
                FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + File.separator + ".mqerrors");
                PrintStream ps = new PrintStream(fos);
                System.setErr(ps);
            } catch (FileNotFoundException e) {
            }
            try {
                FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + File.separator + ".mqlog");
                PrintStream ps = new PrintStream(fos);
                System.setOut(ps);
            } catch (FileNotFoundException e) {
            }
        }
        MQMain.logger.debug("MQMain: main");
        if (args.length > 0) {
            MQMain.logger.info("MQMain: startup arguments ");
            try {
                if (args[0].contains("-open")) passedFilename = args[1]; else passedFilename = args[0];
                MQMain.logger.info("MQMain: " + passedFilename + " as startup file");
                spartacusMode = true;
            } catch (Exception e) {
            }
            if (!new File(passedFilename).exists()) {
                MQMain.logger.info("MQMain: but it doesn't exist - ignoring");
                passedFilename = null;
                spartacusMode = false;
            }
        } else {
            MQMain.logger.info("MQMain: no startup arguments");
        }
        String originalPassedFilename = passedFilename;
        char c;
        logger.info("system name=" + System.getProperty("os.name"));
        logger.info("system architecture=" + System.getProperty("os.arch"));
        logger.info("Java version=" + System.getProperty("java.version"));
        File file;
        if (System.getProperty("os.arch").equals("amd64")) {
        } else {
            String xulrunnerpath;
            file = new File(System.getProperty("user.home") + File.separator + ".mqxulrunner");
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                StringWriter sw = new StringWriter();
                while (fileInputStream.available() > 0) {
                    c = (char) fileInputStream.read();
                    sw.append(c);
                }
                xulrunnerpath = sw.toString();
                MQMain.logger.info("xulrunnerpath=" + xulrunnerpath);
                System.setProperty("org.eclipse.swt.browser.XULRunnerPath", xulrunnerpath);
                sw.close();
                fileInputStream.close();
            } catch (IOException e) {
                if (System.getProperty("os.name").equals("Mac OS X")) {
                    xulrunnerpath = "/Library/Frameworks/XUL.framework/Versions/Current";
                    if (new File(xulrunnerpath).exists()) {
                        MQMain.logger.info(".mqxulrunner file not found - but Mac OS detected - can use default framework dir");
                        MQMain.logger.info("xulrunnerpath=" + xulrunnerpath);
                    } else {
                        MQMain.logger.error("MQMain (main) running on OSX, no .mqxulrunner in home dir and xulrunner not in obvious frameworks dir", e);
                    }
                } else {
                    MQMain.logger.error("MQMain (main) setting xulrunnerpath", e);
                }
            }
        }
        file = new File(System.getProperty("user.home") + File.separator + ".mqtinymce");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            StringWriter sw = new StringWriter();
            while (fileInputStream.available() > 0) {
                c = (char) fileInputStream.read();
                sw.append(c);
            }
            tinymcepath = sw.toString();
            sw.close();
            fileInputStream.close();
        } catch (IOException e) {
            if (System.getProperty("os.name").equals("Mac OS X")) {
                MQMain.logger.info(".mqtinymce not found, but OSX detected - attempting to use tiny_mce in Frameworks");
                tinymcepath = "/Library/Frameworks";
                if (new File(tinymcepath).exists()) {
                    MQMain.logger.info(".mqxulrunner file not found - but Mac OS detected - can use default framework dir");
                    MQMain.logger.info("mqtinymcepath=" + tinymcepath);
                } else {
                    MQMain.logger.error("MQMain (main) running on OSX, no .mqtinymce in home dir and tiny_mce not in obvious frameworks dir", e);
                }
            } else {
                MQMain.logger.error("MQMain (main) setting mqtinymce", e);
            }
        }
        c = tinymcepath.charAt(tinymcepath.length() - 1);
        if (c == File.separatorChar) {
            tinymcepath = tinymcepath.concat("tiny_mce" + File.separator + "base" + File.separator + "base.htm");
        } else {
            tinymcepath = tinymcepath.concat(File.separator + "tiny_mce" + File.separator + "base" + File.separator + "base.htm");
        }
        MQMain.logger.info("tinymcepath=" + tinymcepath);
        lastdir = System.getProperty("user.home");
        mathQurateModel = new MQModel();
        windowMathQurateMain = new MQMain(null);
        Splash splash = new Splash();
        splash.hashCode();
        controller.addModel(mathQurateModel);
        controller.addView(windowMathQurateMain);
        windowMathQurateMain.setBlockOnOpen(true);
        if (MQModel.consoleToFile) {
            Thread logMon = windowMathQurateMain.monitorLogFiles();
            try {
                windowMathQurateMain.open();
            } catch (SWTException e) {
            }
            logMon.stop();
        } else {
            try {
                windowMathQurateMain.open();
            } catch (SWTException e) {
            }
        }
        if (spartacusMode) {
            MessageBox mb = new MessageBox(new Shell(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
            mb.setMessage("Do you want to save any changes to the question back to the test?");
            if (mb.open() == SWT.YES) {
                if ((originalPassedFilename.toLowerCase().endsWith("xml")) || (originalPassedFilename.toLowerCase().endsWith("qtiquestion"))) {
                    controller.exportAssessmentItemType(originalPassedFilename);
                } else {
                    System.out.println("not an XML");
                    controller.getMQContentPackage();
                }
            }
        }
        Display.getCurrent().dispose();
        System.exit(0);
    }

    /** Monitors the log files for changes and updates the appropriate tabs on the MQMain window */
    private Thread monitorLogFiles() {
        Thread th = new Thread("log file monitor") {

            public void run() {
                File errorLog = new File(System.getProperty("user.home") + File.separator + ".mqerrors");
                File standardLog = new File(System.getProperty("user.home") + File.separator + ".mqlog");
                long errorMod = errorLog.lastModified();
                long standardMod = standardLog.lastModified();
                for (; ; ) {
                    if (errorMod != errorLog.lastModified()) {
                        getShell().getDisplay().asyncExec(new Runnable() {

                            public void run() {
                                updateErrorLog();
                            }
                        });
                        errorMod = errorLog.lastModified();
                    }
                    if (standardMod != standardLog.lastModified()) {
                        getShell().getDisplay().asyncExec(new Runnable() {

                            public void run() {
                                updateStandardLog();
                            }
                        });
                        standardMod = standardLog.lastModified();
                    }
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        th.start();
        return th;
    }

    @Override
    protected void initializeBounds() {
        getShell().setSize(800, 480);
        getShell().setLocation(0, 0);
    }

    /**
	 * Instantiates a new mQ main.
	 * 
	 * @param shell
	 *            the shell
	 */
    public MQMain(Shell shell) {
        super(shell);
        addMenuBar();
        addCoolBar(SWT.NONE);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Mathqurate");
        if (System.getProperty("os.name").equals("Mac OS X")) {
            CocoaUIEnhancer enhancer = new CocoaUIEnhancer("Mathqurate");
            enhancer.hookApplicationMenu(Display.getCurrent(), _aboutAction, _preferencesAction);
        }
        ImageDescriptor MQIcon = ImageDescriptor.createFromURL(MQMain.class.getClassLoader().getResource("org/qtitools/mathqurate/resources/icon.gif"));
        Image image = MQIcon.createImage();
        shell.setImage(image);
        itemBodyView = new ItemBodyView(shell);
        controller.addView(itemBodyView);
        itemBodyView.open();
        itemBodyView.getShell().setVisible(false);
        feedbackBlockView = new FeedbackBlockView(shell);
        controller.addView(feedbackBlockView);
        feedbackBlockView.open();
        feedbackBlockView.getShell().setVisible(false);
        templateBlockView = new TemplateBlockView(shell);
        controller.addView(templateBlockView);
        templateBlockView.open();
        templateBlockView.getShell().setVisible(false);
        feedbackInlineView = new FeedbackInlineView(shell);
        controller.addView(feedbackInlineView);
        feedbackInlineView.open();
        feedbackInlineView.getShell().setVisible(false);
        templateInlineView = new TemplateInlineView(shell);
        controller.addView(templateInlineView);
        templateInlineView.open();
        templateInlineView.getShell().setVisible(false);
        snuggleTexView = new SnuggleTeXView(shell);
        controller.addView(snuggleTexView);
        snuggleTexView.open();
        snuggleTexView.getShell().setVisible(false);
        choiceInteractionView = new ChoiceInteractionView(shell);
        controller.addView(choiceInteractionView);
        choiceInteractionView.open();
        choiceInteractionView.getShell().setVisible(false);
        simpleChoiceView = new SimpleChoiceView(shell);
        controller.addView(simpleChoiceView);
        simpleChoiceView.open();
        simpleChoiceView.getShell().setVisible(false);
        endAttemptInteractionView = new EndAttemptInteractionView(shell);
        controller.addView(endAttemptInteractionView);
        endAttemptInteractionView.open();
        endAttemptInteractionView.getShell().setVisible(false);
        mathEntryInteractionView = new MathEntryInteractionView(shell);
        controller.addView(mathEntryInteractionView);
        mathEntryInteractionView.open();
        mathEntryInteractionView.getShell().setVisible(false);
        textEntryInteractionView = new TextEntryInteractionView(shell);
        controller.addView(textEntryInteractionView);
        textEntryInteractionView.open();
        textEntryInteractionView.getShell().setVisible(false);
        templateDeclarationView = new TemplateDeclarationView(shell);
        controller.addView(templateDeclarationView);
        templateDeclarationView.open();
        templateDeclarationView.getShell().setVisible(false);
        outcomeDeclarationView = new OutcomeDeclarationView(shell);
        controller.addView(outcomeDeclarationView);
        outcomeDeclarationView.open();
        outcomeDeclarationView.getShell().setVisible(false);
        responseDeclarationView = new ResponseDeclarationView(shell);
        controller.addView(responseDeclarationView);
        responseDeclarationView.open();
        responseDeclarationView.getShell().setVisible(false);
        metadataView = new MetadataView(shell);
        controller.addView(metadataView);
        metadataView.open();
        metadataView.getShell().setVisible(false);
        printedVariableView = new PrintedVariableView(shell);
        controller.addView(printedVariableView);
        printedVariableView.open();
        printedVariableView.getShell().setVisible(false);
        inlineChoiceInteractionView = new InlineChoiceInteractionView(shell);
        controller.addView(inlineChoiceInteractionView);
        inlineChoiceInteractionView.open();
        inlineChoiceInteractionView.getShell().setVisible(false);
        inlineChoiceView = new InlineChoiceView(shell);
        controller.addView(inlineChoiceView);
        inlineChoiceView.open();
        inlineChoiceView.getShell().setVisible(false);
        choiceInteractionView.getShell().setSize(480, 680);
        choiceInteractionView.getShell().setLocation(0, 0);
        itemBodyView.getShell().setSize(800, 600);
        itemBodyView.getShell().setLocation(0, 0);
        feedbackBlockView.getShell().setSize(640, 700);
        feedbackBlockView.getShell().setLocation(0, 0);
        templateBlockView.getShell().setSize(640, 700);
        templateBlockView.getShell().setLocation(0, 0);
        feedbackInlineView.getShell().setSize(640, 700);
        feedbackInlineView.getShell().setLocation(0, 0);
        templateInlineView.getShell().setSize(640, 700);
        templateInlineView.getShell().setLocation(0, 0);
        mathEntryInteractionView.getShell().setSize(400, 320);
        mathEntryInteractionView.getShell().setLocation(0, 0);
        metadataView.getShell().setSize(850, 500);
        metadataView.getShell().setLocation(0, 0);
        outcomeDeclarationView.getShell().setSize(800, 480);
        outcomeDeclarationView.getShell().setLocation(0, 0);
        printedVariableView.getShell().setSize(400, 250);
        printedVariableView.getShell().setLocation(0, 0);
        responseDeclarationView.getShell().setSize(800, 480);
        responseDeclarationView.getShell().setLocation(0, 0);
        simpleChoiceView.getShell().setSize(640, 480);
        simpleChoiceView.getShell().setLocation(0, 0);
        snuggleTexView.getShell().setSize(500, 500);
        snuggleTexView.getShell().setLocation(0, 0);
        templateDeclarationView.getShell().setSize(800, 480);
        templateDeclarationView.getShell().setLocation(0, 0);
        textEntryInteractionView.getShell().setSize(400, 250);
        textEntryInteractionView.getShell().setLocation(0, 0);
        endAttemptInteractionView.getShell().setSize(480, 250);
        endAttemptInteractionView.getShell().setLocation(0, 0);
        inlineChoiceView.getShell().setSize(640, 480);
        inlineChoiceView.getShell().setLocation(0, 0);
        inlineChoiceInteractionView.getShell().setSize(640, 480);
        inlineChoiceInteractionView.getShell().setLocation(0, 0);
    }

    @Override
    protected Control createContents(Composite parent) {
        if (!MQModel.consoleToFile) {
            treeViewer = new TreeViewer(parent);
            treeViewer.setLabelProvider(new LabelProvider());
            treeViewer.setContentProvider(new MyContentProvider());
            treeViewer.setInput(createModel("assessmentItem"));
        }
        if (MQModel.consoleToFile) {
            TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
            tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
            treeViewer = new TreeViewer(tabFolder);
            treeViewer.setLabelProvider(new LabelProvider());
            treeViewer.setContentProvider(new MyContentProvider());
            treeViewer.setInput(createModel("assessmentItem"));
            standardLog = new Text(tabFolder, SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP | SWT.BORDER);
            updateStandardLog();
            errorLog = new Text(tabFolder, SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP | SWT.BORDER);
            updateErrorLog();
            TabItem ibTreeTab = new TabItem((TabFolder) tabFolder, SWT.NONE);
            ibTreeTab.setText("Item Body Tree");
            ibTreeTab.setControl(treeViewer.getControl());
            TabItem standardLogTab = new TabItem((TabFolder) tabFolder, SWT.NONE);
            standardLogTab.setText("Standard Log");
            standardLogTab.setControl(standardLog);
            TabItem errorLogTab = new TabItem((TabFolder) tabFolder, SWT.NONE);
            errorLogTab.setText("Error Log");
            errorLogTab.setControl(errorLog);
        }
        windowMathQurateMain._newAction.run();
        GridLayoutFactory.fillDefaults().numColumns(1).margins(LayoutConstants.getMargins()).generateLayout(parent);
        return (parent);
    }

    public static void updateErrorLog() {
        updateLogPane(errorLog, System.getProperty("user.home") + File.separator + ".mqerrors");
    }

    public static void updateStandardLog() {
        updateLogPane(standardLog, System.getProperty("user.home") + File.separator + ".mqlog");
    }

    private static void updateLogPane(Text textPane, String filename) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            StringWriter sw = new StringWriter();
            while (fileInputStream.available() > 0) {
                char c = (char) fileInputStream.read();
                sw.append(c);
            }
            String logText = sw.toString();
            textPane.setText(logText);
            textPane.setSelection(textPane.getCharCount(), textPane.getCharCount());
            textPane.showSelection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected MenuManager createMenuManager() {
        MenuManager menuManager = new MenuManager();
        MenuManager fileMenu = new MenuManager("&File");
        fileMenu.add(_newAction);
        fileMenu.add(_loadAction);
        fileMenu.add(_saveAction);
        fileMenu.add(new Separator());
        fileMenu.add(_importXMLAction);
        fileMenu.add(_exportXMLAction);
        fileMenu.add(_importMinibixAction);
        fileMenu.add(_exportMinibixAction);
        if (!(System.getProperty("os.name").equals("Mac OS X"))) {
            fileMenu.add(new Separator());
            fileMenu.add(_settingsWindowAction);
            fileMenu.add(new Separator());
            fileMenu.add(_exitAction);
        }
        if (!spartacusMode) menuManager.add(fileMenu);
        MenuManager editMenu = new MenuManager("&Edit");
        editMenu.add(_undoAction);
        editMenu.add(_redoAction);
        menuManager.add(editMenu);
        MenuManager viewMenu = new MenuManager("&Window");
        viewMenu.add(_simpleChoiceQuickAction);
        viewMenu.add(_metadataAction);
        viewMenu.add(_itemBodyAction);
        viewMenu.add(_processingEditorChooserAction);
        viewMenu.add(_rendererAction);
        menuManager.add(viewMenu);
        MenuManager helpMenu = new MenuManager("&Help");
        helpMenu.add(_helpAction);
        if (!(System.getProperty("os.name").equals("Mac OS X"))) {
            helpMenu.add(new Separator());
            helpMenu.add(_aboutAction);
        }
        menuManager.add(helpMenu);
        return menuManager;
    }

    /**
	 * Creates the model.
	 * 
	 * @param title
	 *            the title
	 * 
	 * @return the assessment item tree model
	 */
    private AssessmentItemTreeModel createModel(String title) {
        MQMain.logger.debug("MQMain: createModel");
        ArrayList<String> child = new ArrayList<String>();
        child.add("Response declaration");
        child.add("Outcome declaration");
        child.add("Template declaration");
        child.add("Question body");
        child.add("Template and Response processing");
        treeItemAssessmentItemProgress = new AssessmentItemTreeModel("", null);
        AssessmentItemTreeModel assessmentItem = new AssessmentItemTreeModel(title, treeItemAssessmentItemProgress);
        treeItemAssessmentItemProgress.child.add(assessmentItem);
        AssessmentItemTreeModel tmp;
        for (int i = 0; i < child.size(); i++) {
            tmp = new AssessmentItemTreeModel(child.get(i), assessmentItem);
            assessmentItem.child.add(tmp);
        }
        return treeItemAssessmentItemProgress;
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public void modelPropertyChange(final PropertyChangeEvent evt) {
        MQMain.logger.debug("MQMain: modelPropertyChange: " + evt.getPropertyName());
        if (evt.getPropertyName().equals(DefaultController.SET_ASSESSMENTITEM_PROPERTY)) {
            AssessmentItemType assessmentItemType = (AssessmentItemType) evt.getNewValue();
            String title = assessmentItemType.getTitle();
            String id = assessmentItemType.getIdentifier();
            try {
                if (treeViewer != null) {
                    treeViewer.getTree().getItem(0).setText(title);
                    treeViewer.expandAll();
                    treeViewer.refresh(treeItemAssessmentItemProgress, false);
                }
                getShell().setText("MathQurate - " + title + " : " + id);
                itemBodyView.getShell().setText("Question Body : " + title + " : " + id);
            } catch (SWTException e) {
                e.printStackTrace();
            }
        }
        if (evt.getPropertyName().equals(DefaultController.GET_MQRESPONSEDECLARATION_PROPERTY)) {
            ArrayList<MQResponseDeclaration> responseDeclarationHelperArrayList = (ArrayList<MQResponseDeclaration>) evt.getNewValue();
            Iterator<MQResponseDeclaration> iterator = responseDeclarationHelperArrayList.iterator();
            if (treeViewer != null) {
                TreeItem treeItem = treeViewer.getTree().getItem(0);
                AssessmentItemTreeModel top = (AssessmentItemTreeModel) treeItem.getData();
                AssessmentItemTreeModel secondchild = (AssessmentItemTreeModel) top.child.get(0);
                secondchild.child.clear();
                AssessmentItemTreeModel tmp;
                while (iterator.hasNext()) {
                    MQResponseDeclaration declarationHelper = iterator.next();
                    String id = declarationHelper.getIdentifier();
                    tmp = new AssessmentItemTreeModel(id, secondchild);
                    secondchild.child.add(tmp);
                }
                treeViewer.expandAll();
                treeViewer.refresh(treeItemAssessmentItemProgress, false);
            }
        }
        if (evt.getPropertyName().equals(DefaultController.GET_MQOUTCOMEDECLARATION_PROPERTY)) {
            ArrayList<MQOutcomeDeclaration> outcomeDeclarationHelperArrayList = (ArrayList<MQOutcomeDeclaration>) evt.getNewValue();
            Iterator<MQOutcomeDeclaration> iterator = outcomeDeclarationHelperArrayList.iterator();
            if (treeViewer != null) {
                TreeItem treeItem = treeViewer.getTree().getItem(0);
                AssessmentItemTreeModel top = (AssessmentItemTreeModel) treeItem.getData();
                AssessmentItemTreeModel thirdchild = (AssessmentItemTreeModel) top.child.get(1);
                thirdchild.child.clear();
                AssessmentItemTreeModel tmp;
                while (iterator.hasNext()) {
                    MQOutcomeDeclaration declarationHelper = iterator.next();
                    String id = declarationHelper.getIdentifier();
                    tmp = new AssessmentItemTreeModel(id, thirdchild);
                    thirdchild.child.add(tmp);
                }
                treeViewer.expandAll();
                treeViewer.refresh(treeItemAssessmentItemProgress, false);
            }
        }
        if (evt.getPropertyName().equals(DefaultController.GET_MQTEMPLATEDECLARATION_PROPERTY)) {
            ArrayList<MQTemplateDeclaration> templateDeclarationHelperArrayList = (ArrayList<MQTemplateDeclaration>) evt.getNewValue();
            Iterator<MQTemplateDeclaration> iterator = templateDeclarationHelperArrayList.iterator();
            if (treeViewer != null) {
                TreeItem treeItem = treeViewer.getTree().getItem(0);
                AssessmentItemTreeModel top = (AssessmentItemTreeModel) treeItem.getData();
                AssessmentItemTreeModel firstchild = (AssessmentItemTreeModel) top.child.get(2);
                firstchild.child.clear();
                AssessmentItemTreeModel tmp;
                while (iterator.hasNext()) {
                    MQTemplateDeclaration declarationHelper = iterator.next();
                    String id = declarationHelper.getIdentifier();
                    tmp = new AssessmentItemTreeModel(id, firstchild);
                    firstchild.child.add(tmp);
                }
                treeViewer.expandAll();
                treeViewer.refresh(treeItemAssessmentItemProgress, false);
            }
        }
        if (evt.getPropertyName().equals(DefaultController.GET_MQITEMBODY_PROPERTY)) {
            String contentXML = (String) evt.getNewValue();
            if (treeViewer != null) {
                TreeItem treeItem = treeViewer.getTree().getItem(0);
                AssessmentItemTreeModel top = (AssessmentItemTreeModel) treeItem.getData();
                AssessmentItemTreeModel sixthchild = (AssessmentItemTreeModel) top.child.get(3);
                sixthchild.child.clear();
                int chars = 0;
                chars = contentXML.length() - imsqtinamespaces.length();
                if (chars < 0) chars = 0;
                AssessmentItemTreeModel assessmentItemHelper = new AssessmentItemTreeModel("Item Body has " + chars + " characters", (AssessmentItemTreeModel) treeItem.getData());
                sixthchild.child.add(assessmentItemHelper);
                treeViewer.expandAll();
                treeViewer.refresh(treeItemAssessmentItemProgress, false);
            }
        }
        if (evt.getPropertyName().equals(DefaultController.RENDER_ITEM_PROPERTY)) {
            try {
                String contentXml = (String) evt.getNewValue();
                RendererView rendererView = new RendererView(getShell());
                rendererView.uploadXml(contentXml);
                rendererView.open();
            } catch (Exception e) {
                MQMain.logger.error("MQMain (property change) RENDER_ITEM_PROPERTY", e);
            }
        }
        if (evt.getPropertyName().equals(DefaultController.GET_CONTENTPACKAGE_PROPERTY)) {
            MQContentPackage contentPackageHelper = (MQContentPackage) evt.getNewValue();
            String selected;
            FileDialog fd = null;
            if (spartacusMode) {
                selected = getPassedFilename();
            } else {
                fd = new FileDialog(getShell(), SWT.SAVE);
                fd.setText("Save content package");
                fd.setFilterPath(lastdir);
                String[] filterExt = { "*.zip" };
                fd.setFilterExtensions(filterExt);
                selected = fd.open();
                if (selected != null) {
                    if (selected.length() > 0) {
                        if (selected.length() < 5) {
                            selected = selected.concat(".zip");
                        } else if (selected.length() > 5) {
                            if (selected.substring(selected.length() - 4).equals(".zip")) {
                            } else {
                                selected = selected.concat(".zip");
                            }
                        }
                    }
                }
            }
            File file = new File(selected);
            if (file.exists() && !spartacusMode) {
                MessageBox mb = new MessageBox(fd.getParent(), SWT.ICON_WARNING | SWT.YES | SWT.NO);
                mb.setMessage(selected + " already exists. Do you want to replace it?");
                if (mb.open() == SWT.NO) {
                    return;
                }
            }
            lastdir = file.getAbsolutePath().replace(file.getName(), "");
            MQModel.mathqurateObjectFactory.makeZipFromCP(contentPackageHelper, file);
        }
        if (evt.getPropertyName().equals(DefaultController.GET_CONTENTPACKAGEANDPOST_PROPERTY)) {
            MQContentPackage contentPackageHelper = (MQContentPackage) evt.getNewValue();
            File file;
            try {
                file = File.createTempFile("minibixpost", ".zip", MQMain.tmpdir);
                lastdir = file.getAbsolutePath().replace(file.getName(), "");
                ContentPackageExportView contentPackageExportView = new ContentPackageExportView(getShell(), contentPackageHelper, file);
                contentPackageExportView.setBlockOnOpen(true);
                contentPackageExportView.open();
                controller.setContentPackageMetadata(contentPackageHelper);
            } catch (IOException e) {
                MQMain.logger.error("Couldn't create temporary file for posting content package", e);
            }
        }
        if (evt.getPropertyName().equals(DefaultController.SET_CALL_TRP)) {
            MQContentPackage contentPackageHelper = (MQContentPackage) evt.getNewValue();
            ProcessingEditorChooserView processingEditorChooserView = new ProcessingEditorChooserView(getShell(), contentPackageHelper);
            processingEditorChooserView.open();
            processingEditorChooserView.getShell().setSize(480, 400);
            processingEditorChooserView.getShell().setLocation(0, 0);
        }
        if (evt.getPropertyName().equals(DefaultController.IMPORT_FROMMINIBIX_PROPERTY)) {
            MQContentPackage contentPackageHelper = (MQContentPackage) evt.getNewValue();
            ContentPackageImportView contentPackageImportView = new ContentPackageImportView(getShell(), contentPackageHelper, this);
            contentPackageImportView.open();
        }
        if (evt.getPropertyName().equals(DefaultController.GET_MQTEMPLATERESPONSEPROCESSING_PROPERTY)) {
            MQTemplateResponseProcessing templateProcessingHelper = (MQTemplateResponseProcessing) evt.getNewValue();
            MQContentPackage metadata = templateProcessingHelper.getMetadata();
            try {
                System.setProperty("sun.awt.noerasebackground", "true");
                ApplicationContext context = new ClassPathXmlApplicationContext("org/qtitools/validatr/Validatr.xml");
                Settings settings = (Settings) context.getBean("settings");
                settings.load(null);
                logger.info("validatr is looking here:" + settings.getLocation().getPath());
                MainPanel mainPanel = (MainPanel) context.getBean("mainPanel");
                ValidatorModel validatorModel = (ValidatorModel) context.getBean("validatorModel");
                String javaVersion = System.getProperty("java.version");
                if (javaVersion.startsWith("1.6")) javaVersion = "linuxfail";
                if (javaVersion.startsWith("1.7")) javaVersion = "linuxfail";
                if ((System.getProperty("os.name").contains("Linux")) && (javaVersion.equals("linuxfail"))) {
                    if (MessageDialog.openConfirm(getShell(), "Known issue on Linux and Java 6!", "There is a known issue on Linux and versions of Java from 6 onwards, due\n" + "to a bug in Java for this platform. This may manifest itself in Mathqurate\n" + "hanging after the response/template processing screen has been used. A\n" + "workaround is in place, but this cannot be tested on all possible versions\n" + "of Linux. Make sure you have saved your work first!\n\nDo you want to proceed?")) {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                        final ResponseTemplateProcessingViewSwingOnly ProcessingView = new ResponseTemplateProcessingViewSwingOnly(context, settings, validatorModel, mainPanel, metadata);
                        Thread th = new Thread("Waiting for Swing to finish") {

                            public void run() {
                                MQMain.logger.info("Waiting for swing");
                                ProcessingView.toFront();
                                while (ProcessingView.isVisible()) {
                                    try {
                                        this.sleep(2000);
                                    } catch (InterruptedException e) {
                                        this.stop();
                                    }
                                }
                                MQMain.logger.info("Swing done - result " + ProcessingView.result);
                                if (ProcessingView.result != null) {
                                    Display.getDefault().asyncExec(new Runnable() {

                                        public void run() {
                                            controller.importAssessmentItemType(ProcessingView.result);
                                            controller.switchRP();
                                        }
                                    });
                                }
                                this.stop();
                            }
                        };
                        th.start();
                    }
                } else {
                    ResponseTemplateProcessingView ProcessingView = new ResponseTemplateProcessingView(getShell(), context, settings, validatorModel, mainPanel, metadata);
                    ProcessingView.open();
                    ProcessingView.getShell().setSize(1024, 768);
                    ProcessingView.getShell().setLocation(0, 0);
                }
            } catch (ClassNotFoundException e) {
                MQMain.logger.error("error when opening Validatr editor", e);
            } catch (InstantiationException e) {
                MQMain.logger.error("error when opening Validatr editor", e);
            } catch (IllegalAccessException e) {
                MQMain.logger.error("error when opening Validatr editor", e);
            } catch (UnsupportedLookAndFeelException e) {
                MQMain.logger.error("error when opening Validatr editor", e);
            }
        }
        if (evt.getPropertyName().equals(DefaultController.NEW_ASSESSMENTITEM_PROPERTY)) {
        }
        if (evt.getPropertyName().equals(DefaultController.GET_MQMETADATA_PROPERTY)) {
        }
        if (evt.getPropertyName().equals(DefaultController.SET_MARSHALL_ERROR_PROPERTY)) {
            String s = (String) evt.getNewValue();
            MessageBox messageBox = new MessageBox(getShell(), SWT.OK | SWT.ICON_WARNING);
            messageBox.setMessage("Error in " + s + " design. Make sure you have laid out your design correctly. E.g." + " A Choice Interaction must be placed within a Layer or Table Cell.");
            messageBox.open();
        }
        if (evt.getPropertyName().equals(DefaultController.GET_HELP_PROPERTY)) {
            String version = (String) evt.getNewValue();
            About about = new About(getShell(), version);
            about.open();
            about.getShell().setSize(400, 400);
            about.getShell().setLocation(0, 0);
        }
        if (evt.getPropertyName().equals(DefaultController.SET_UNMARSHALL_ERROR_PROPERTY)) {
            String s = (String) evt.getNewValue();
            MessageBox messageBox = new MessageBox(getShell(), SWT.OK | SWT.ICON_WARNING);
            messageBox.setMessage("Error: " + s);
            messageBox.open();
        }
    }

    @Override
    public int open() {
        MQMain.logger.debug("MQMain: open");
        this.create();
        if (passedFilename != null) {
            if (passedFilename.toLowerCase().endsWith("zip")) {
                Object[] bits = { new File(passedFilename) };
                controller.importContentPackage(bits);
            } else {
                controller.importAssessmentItemType(passedFilename);
            }
        }
        return super.open();
    }

    @Override
    protected CoolBarManager createCoolBarManager(int style) {
        CoolBarManager cbm = new CoolBarManager(style);
        createCoolBars(SWT.FLAT);
        cbm.add(fileBar);
        cbm.add(editBar);
        cbm.add(interactionBar);
        return cbm;
    }

    protected void createCoolBars(int style) {
        fileBar = new ToolBarManager(style);
        if (!spartacusMode) {
            fileBar.add(_newAction);
            fileBar.add(_loadAction);
            fileBar.add(_saveAction);
            fileBar.add(new Separator());
            fileBar.add(_importXMLAction);
            fileBar.add(_exportXMLAction);
            fileBar.add(_importMinibixAction);
            fileBar.add(_exportMinibixAction);
            fileBar.add(new Separator());
            fileBar.add(_settingsWindowAction);
        }
        editBar = new ToolBarManager(style);
        editBar.add(_undoAction);
        editBar.add(_redoAction);
        interactionBar = new ToolBarManager(style);
        interactionBar.add(_simpleChoiceQuickAction);
        interactionBar.add(_metadataAction);
        interactionBar.add(_itemBodyAction);
        interactionBar.add(_processingEditorChooserAction);
        interactionBar.add(_rendererAction);
    }

    public static void setPassedFilename(String passedFilename) {
        MQMain.passedFilename = passedFilename;
    }

    public static String getPassedFilename() {
        return MQMain.passedFilename;
    }
}
