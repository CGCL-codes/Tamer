package org.xaware.ide.xadev.gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xaware.ide.shared.UserPrefs;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.datamodel.InputParameterData;
import org.xaware.ide.xadev.datamodel.JDOMContent;
import org.xaware.ide.xadev.datamodel.JDOMContentFactory;
import org.xaware.ide.xadev.datamodel.MapTable;
import org.xaware.ide.xadev.datamodel.MapTableContentProvider;
import org.xaware.ide.xadev.datamodel.MapTreeNode;
import org.xaware.ide.xadev.datamodel.XMLTreeNode;
import org.xaware.ide.xadev.gui.dialog.XMLGetter;
import org.xaware.ide.xadev.gui.editor.BizdocInputHelper;
import org.xaware.ide.xadev.gui.mapper.MapperHelper;
import org.xaware.ide.xadev.gui.model.ExecuteArguments;
import org.xaware.ide.xadev.gui.model.ExecuteBizViewDocument;
import org.xaware.ide.xadev.gui.model.IExecBizViewResults;
import org.xaware.ide.xadev.wizard.WizardController;
import org.xaware.ide.xadev.wizard.WizardException;
import org.xaware.ide.xadev.wizard.WizardFactory;
import org.xaware.ide.xadev.wizard.WizardPanelComponent;
import org.xaware.ide.xadev.wizardpanels.ExecWizardPanel;
import org.xaware.ide.xadev.wizardpanels.SOAPResponseMapper;
import org.xaware.ide.xadev.wizardpanels.XMLMapper;
import org.xaware.server.common.XMLNamespaceUtil;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * Mapper Screen
 * 
 * @author Balaji C S
 * @version 1.0
 */
public class XMLMapperPanel implements SelectionListener, IExecBizViewResults {

    /** Translator used for Localization */
    public static final Translator translator = XA_Designer_Plugin.getTranslator();

    /** Holds Reference to Namespace. */
    private static final Namespace ns = XAwareConstants.xaNamespace;

    /** Shell instance */
    private Shell shell;

    /** Represents the Logger. */
    private final XAwareLogger logger = XAwareLogger.getXAwareLogger(XMLMapperPanel.class.getName());

    /** Target Tree Handler */
    protected MapDNDTreeHandler targetTree;

    /** Source Tree Handler */
    protected MapDNDTreeHandler sourceTree;

    /** Input Parameters */
    protected Vector inputParams;

    /** Source Button Title String */
    protected String sourceButtonStr;

    /** Target Button Title String */
    protected String targetButtonStr;

    /** Source Tree Element */
    protected Element sourceRootElement;

    /** Target Tree Element */
    protected Element targetRootElement;

    /** Target Tree New Element */
    protected Element newTargetRootElement;

    /** To Show Descriptions or not */
    protected boolean showDescriptions;

    /** To Show Plus sign on Mappings or not */
    protected boolean showPlusOnMapping;

    /** table instance */
    protected MapReportTable myTable;

    /** tablecontent provider instance */
    protected MapTableContentProvider myTableContentProvider;

    /** Export Button */
    protected Button exportBtn;

    /** Append CheckBox */
    protected Button appendChk;

    /** Wizard Panel Component */
    protected WizardPanelComponent wizPanComp;

    /** Target Button */
    protected Button targetBtn;

    /** Auto Generate Button */
    protected Button autoGenerateBtn;

    /** Auto-map Button */
    private Button autoMapBtn;

    /** Source Button */
    protected Button sourceBtn;

    /** Predicted Results Button */
    protected Button predResltsBtn;

    /** Get Predicted Results String */
    protected String getPredRsltsStr = "Execute...";

    protected String cancelPredRsltsStr = "Cancel";

    /** Scroll Composite for the tree composite */
    protected ScrolledComposite sc;

    /** Composite for the Top Message */
    protected Composite messageComp;

    /** Data Source Descritption */
    protected String dataSourceDesc;

    /** Data Source Descritption */
    protected String mainTitleStr;

    /** Message Label */
    protected Label messageLbl;

    /** Composite for the undo, Reset and SetRepeating buttons */
    protected Composite undoResetComp;

    /** Undo Button */
    protected Button undoBtn;

    /** Reset Button */
    protected Button resetBtn;

    /** Set Repeating Button */
    protected Button setRepeatingBtn;

    /** Main SashForm for the Source and Target Tree */
    protected SashForm mainSashFrm;

    /** Composite for the Source Tree */
    protected Composite sourceComp;

    /** Source title string */
    protected String sourceTitleStr;

    /** Target title string */
    protected String targetTitleStr;

    /** Input Parameter String */
    protected String ipParamStr;

    /** Source Label */
    protected Label sourceLbl;

    /** Composite for Source Button */
    protected Composite sourceButtonComp;

    /** Composite for Target Tree */
    protected Composite compositeTarget;

    /** Target Label */
    protected Label targetLbl;

    /** Compsite for Target Button */
    protected Composite targetButtonComp;

    /** Main Composite for the Mapper page */
    protected Composite parent;

    /** InputParameter Composite */
    private Composite inputParamComp;

    /** Mapper Group */
    private Group mapperGrp;

    /** InputParam List */
    protected DNDListHandler inputParamListHandler;

    private boolean isBuildSourceResults = false;

    private boolean isExecute = false;

    private boolean replaceSourceRootWithDotInPath = false;

    /**
     * Creates a new XMLMapperPanel object with the specified parameters.
     * 
     * @param parent
     *            Parent Composite on which the XMLMapperPanel is to be placed
     * @param showMappingReport
     *            true if MappingReport has to be visible, false otherwise
     * @param wizPanComp
     *            Wizard Panel Component
     */
    public XMLMapperPanel(final Composite parent, final boolean showMappingReport, final WizardPanelComponent wizPanComp) {
        this(parent, translator.getString("To map source XML to target XML, drag elements/attributes from the Source tree to the Target tree."), translator.getString("Source:"), translator.getString("Target:"), translator.getString("Edit..."), translator.getString("Edit..."), translator.getString("Input Parameters:"), new Element("Source"), new Element("Target"), new Vector(), showMappingReport, wizPanComp);
    }

    /**
     * Creates a new XMLMapperPanel object with the specified parameters.
     * 
     * @param parent
     *            Parent Composite on which the XMLMapperPanel is to be placed
     * @param sourceRoot
     *            Source Root Node
     * @param targetRoot
     *            Target Root Node
     * @param params
     *            Input Parameters
     * @param showMappingReport
     *            To Show Mapping Report or not
     * @param wizPanComp
     *            Wizard Panel Component
     */
    public XMLMapperPanel(final Composite parent, final Element sourceRoot, final Element targetRoot, final Vector params, final boolean showMappingReport, final WizardPanelComponent wizPanComp) {
        this(parent, translator.getString("To map source XML to target XML, drag elements/attributes from the Source tree to the Target tree."), translator.getString("Source:"), translator.getString("Target:"), translator.getString("Edit..."), translator.getString("Edit..."), translator.getString("Input Parameters:"), sourceRoot, targetRoot, params, showMappingReport, wizPanComp);
    }

    /**
     * Creates a new XMLMapperPanel object with the specified parameters.
     * 
     * @param parent
     *            Parent Composite on which the XMLMapperPanel is to be placed
     * @param sourceRoot
     *            Source Root Node
     * @param targetRoot
     *            Target Root Node
     * @param params
     *            Input Parameters
     * @param sourceTitle
     *            Source Tree Title
     * @param targetTitle
     *            Target Tree Title
     * @param showMappingReport
     *            To Show Mapping Report or not
     * @param wizPanComp
     *            Wizard Panel Component
     */
    public XMLMapperPanel(final Composite parent, final Element sourceRoot, final Element targetRoot, final Vector params, final String sourceTitle, final String targetTitle, final boolean showMappingReport, final WizardPanelComponent wizPanComp) {
        this(parent, translator.getString("To map source XML to target XML, drag elements/attributes from the Source tree to the " + "Target tree."), sourceTitle, targetTitle, translator.getString("Edit..."), translator.getString("Edit..."), translator.getString("Input Parameters:"), sourceRoot, targetRoot, params, showMappingReport, wizPanComp);
    }

    /**
     * Creates a new XMLMapperPanel object with the specified parameters.
     * 
     * @param parent
     *            Parent Composite on which the XMLMapperPanel is to be placed
     * @param title
     *            Main Title String
     * @param source
     *            Source Message
     * @param target
     *            target Message
     * @param sourceButton
     *            Source Button Title
     * @param targetButton
     *            Target Button Title
     * @param paramStr
     *            Parameter String
     * @param sourceRoot
     *            Source Root Node
     * @param targetRoot
     *            Target Root Node
     * @param inputParams
     *            Input Parameters
     * @param showMappingReport
     *            To Show Mapping Report or not
     * @param wizPanComp
     *            Wizard Panel Component
     */
    public XMLMapperPanel(final Composite parent, final String title, final String source, final String target, final String sourceButton, final String targetButton, final String paramStr, final Element sourceRoot, final Element targetRoot, final Vector inputParams, final boolean showMappingReport, final WizardPanelComponent wizPanComp) {
        this.parent = parent;
        mainTitleStr = title;
        sourceTitleStr = source;
        targetTitleStr = target;
        sourceButtonStr = sourceButton;
        targetButtonStr = targetButton;
        ipParamStr = paramStr;
        this.wizPanComp = wizPanComp;
        this.inputParams = inputParams;
        this.sourceRootElement = sourceRoot;
        this.targetRootElement = targetRoot;
        showDescriptions = UserPrefs.showAuditReport() && showMappingReport;
        shell = wizPanComp.getPageComposite().getShell();
        createComponents();
    }

    /**
     * Creates a new XMLMapperPanel object with the specified parameters.
     * 
     * @param parent
     *            Parent Composite on which the XMLMapperPanel is to be placed
     * @param title
     *            Main Title String
     * @param source
     *            Source Message
     * @param target
     *            target Message
     * @param sourceButton
     *            Source Button Title
     * @param targetButton
     *            Target Button Title
     * @param paramStr
     *            Parameter String
     * @param sourceRoot
     *            Source Root Node
     * @param targetRoot
     *            Target Root Node
     * @param inputParams
     *            Input Parameters
     * @param showMappingReport
     *            To Show Mapping Report or not
     * @param wizPanComp
     *            Wizard Panel Component
     * @param createComp
     *            To Create Composite or not
     */
    public XMLMapperPanel(final Composite parent, final String title, final String source, final String target, final String sourceButton, final String targetButton, final String paramStr, final Element sourceRoot, final Element targetRoot, final Vector inputParams, final boolean showMappingReport, final WizardPanelComponent wizPanComp, final boolean createComp) {
        mainTitleStr = title;
        sourceTitleStr = source;
        targetTitleStr = target;
        sourceButtonStr = sourceButton;
        targetButtonStr = targetButton;
        ipParamStr = paramStr;
        this.wizPanComp = wizPanComp;
        this.inputParams = inputParams;
        this.sourceRootElement = sourceRoot;
        this.targetRootElement = targetRoot;
        showDescriptions = UserPrefs.showAuditReport() && showMappingReport;
        shell = wizPanComp.getPageComposite().getShell();
        if (createComp == true) {
            createComponents();
        }
    }

    /**
     * Creates the Mapper Composite
     */
    protected void createComponents() {
        wizPanComp.removeAll();
        final Element targetElement = (Element) targetRootElement.clone();
        final Composite parent = wizPanComp.getPageComposite();
        final Composite comp = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginTop = -6;
        gridLayout.marginLeft = -6;
        comp.setLayout(gridLayout);
        final Composite topComp = new Composite(comp, SWT.NONE);
        topComp.setLayout(new GridLayout());
        final SashForm mainSashFrm = new SashForm(comp, SWT.VERTICAL);
        mainSashFrm.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout msgGridLayout = new GridLayout(1, false);
        msgGridLayout.marginLeft = 0;
        msgGridLayout.marginTop = 0;
        messageComp = new Composite(topComp, SWT.NONE);
        msgGridLayout = new GridLayout(1, false);
        msgGridLayout.verticalSpacing = 10;
        msgGridLayout.horizontalSpacing = 10;
        GridData gridData = new GridData();
        messageComp.setLayout(gridLayout);
        messageComp.setLayoutData(gridData);
        messageLbl = new Label(messageComp, SWT.NONE);
        messageLbl.setText(mainTitleStr);
        gridData = new GridData();
        gridData.widthHint = 580;
        messageLbl.setLayoutData(gridData);
        undoResetComp = new Composite(topComp, SWT.NONE);
        gridLayout = new GridLayout(4, false);
        gridLayout.verticalSpacing = 10;
        gridLayout.horizontalSpacing = 10;
        gridData = new GridData();
        undoResetComp.setLayout(gridLayout);
        undoResetComp.setLayoutData(gridData);
        undoBtn = ControlFactory.createButton(undoResetComp, "Undo");
        undoBtn.addSelectionListener(this);
        resetBtn = ControlFactory.createButton(undoResetComp, "Reset");
        resetBtn.addSelectionListener(this);
        setRepeatingBtn = ControlFactory.createButton(undoResetComp, "Set Repeating");
        setRepeatingBtn.addSelectionListener(this);
        final SashForm treeSashFrm = new SashForm(mainSashFrm, SWT.HORIZONTAL);
        treeSashFrm.setLayoutData(new GridData(GridData.FILL_BOTH));
        final SashForm sashFrm2 = new SashForm(treeSashFrm, SWT.VERTICAL);
        sashFrm2.setLayoutData(new GridData(GridData.FILL_BOTH));
        sourceComp = new Composite(sashFrm2, SWT.BORDER);
        gridLayout = new GridLayout();
        sourceComp.setLayout(gridLayout);
        sourceComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        sourceLbl = new Label(sourceComp, SWT.NONE);
        sourceLbl.setText(sourceTitleStr);
        if (showDescriptions) {
            final Composite mapperComp = new Composite(mainSashFrm, SWT.BORDER);
            gridLayout = new GridLayout();
            mapperComp.setLayout(gridLayout);
            mapperGrp = new Group(mapperComp, SWT.NONE);
            mapperGrp.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
            mapperGrp.setText(translator.getString("Mapper Properties:"));
            mapperGrp.setLayout(new GridLayout());
            final Vector mapRows = new Vector();
            myTable = new MapReportTable(mapperGrp, mapRows);
            myTableContentProvider = myTable.getModel();
        }
        sourceTree = new MapDNDTreeHandler(sourceComp, new MapTreeNode(sourceRootElement), new XAMapTreeImageAndLabelProvider(new MapTreeNode(sourceRootElement)));
        sourceTree.setDragEnabled(true);
        sourceTree.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
        sourceButtonComp = new Composite(sourceComp, SWT.NONE);
        final GridLayout gl = new GridLayout();
        gl.numColumns = 2;
        sourceButtonComp.setLayout(gl);
        sourceTree.expandTree(4);
        final GridData buttonGridData = new GridData(GridData.FILL_HORIZONTAL);
        sourceButtonComp.setLayoutData(buttonGridData);
        sourceBtn = ControlFactory.createButton(sourceButtonComp, sourceButtonStr, false, new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
        sourceBtn.addSelectionListener(this);
        if (wizPanComp instanceof SOAPResponseMapper) {
            predResltsBtn = ControlFactory.createButton(sourceButtonComp, getPredRsltsStr, false, new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
            predResltsBtn.addSelectionListener(this);
        }
        final Vector<String> inputParamNames = new Vector<String>();
        final Iterator inputParamsItr = inputParams.iterator();
        inputParamComp = new Composite(sashFrm2, SWT.BORDER);
        gridLayout = new GridLayout();
        gridData = new GridData(GridData.FILL_BOTH);
        inputParamComp.setLayout(gridLayout);
        inputParamComp.setLayoutData(gridData);
        final Label inputParamLbl = new Label(inputParamComp, SWT.NONE);
        inputParamLbl.setText(ipParamStr);
        while (inputParamsItr.hasNext()) {
            final InputParameterData ipd = (InputParameterData) inputParamsItr.next();
            inputParamNames.add(ipd.getName());
        }
        if (inputParamNames.size() > 0) {
            inputParamListHandler = new DNDListHandler(inputParamComp, TextTransfer.getInstance(), inputParamNames);
            inputParamListHandler.setDragEnabled(true);
            inputParamListHandler.setDropEnabled(false);
            gridData = new GridData(GridData.FILL_BOTH);
            gridData.widthHint = 150;
            inputParamListHandler.list.setLayoutData(gridData);
        } else {
            inputParamComp.setVisible(false);
        }
        compositeTarget = new Composite(treeSashFrm, SWT.BORDER);
        gridLayout = new GridLayout(1, false);
        gridData = new GridData();
        gridData.widthHint = 330;
        gridData.heightHint = 300;
        compositeTarget.setLayout(gridLayout);
        compositeTarget.setLayoutData(gridData);
        targetLbl = new Label(compositeTarget, SWT.NONE);
        targetLbl.setText("Target (Output XML): ");
        final MapTreeNode targetNode = new MapTreeNode(targetElement);
        targetNode.setMapSourceXML((Element) sourceRootElement.clone());
        targetTree = new MapDNDTreeHandler(compositeTarget, targetNode, new XAMapTreeImageAndLabelProvider(targetNode), true, true, true);
        targetTree.refreshTree();
        targetTree.setDropEnabled(true);
        targetTree.setInputParams(inputParamNames);
        targetTree.expandTree(2);
        if (showPlusOnMapping) {
            targetTree.setShowPlusSign(true);
        }
        newTargetRootElement = (Element) targetRootElement.clone();
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = 250;
        targetTree.getTree().setLayoutData(gridData);
        targetButtonComp = new Composite(compositeTarget, SWT.NONE);
        gridLayout = new GridLayout(2, false);
        targetButtonComp.setLayout(gridLayout);
        targetButtonComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        targetBtn = ControlFactory.createButton(targetButtonComp, targetButtonStr, false, new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_END));
        targetBtn.addSelectionListener(this);
        autoMapBtn = ControlFactory.createButton(targetButtonComp, "Auto-map", false, new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
        autoMapBtn.addSelectionListener(this);
        if (showDescriptions) {
            targetTree.setMapTable(myTable);
            targetNode.setMapTable(myTable);
            targetTree.setMapTableContentProvider(myTableContentProvider);
            myTable.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
            final Composite exportComp = new Composite(mapperGrp, SWT.NONE);
            exportComp.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_CENTER));
            final GridLayout gridLayout_2 = new GridLayout();
            gridLayout_2.numColumns = 6;
            exportComp.setLayout(gridLayout_2);
            final Label empty1Lbl = new Label(exportComp, SWT.NONE);
            final GridData gridData_2 = new GridData(GridData.FILL_HORIZONTAL);
            gridData_2.horizontalSpan = 2;
            empty1Lbl.setLayoutData(gridData_2);
            appendChk = new Button(exportComp, SWT.CHECK);
            appendChk.setText("Append");
            exportBtn = ControlFactory.createButton(exportComp, "Export...", false, new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
            exportBtn.addSelectionListener(this);
            mainSashFrm.setWeights(new int[] { 3, 2 });
        }
        treeSashFrm.setWeights(new int[] { 1, 2 });
        wizPanComp.refresh();
    }

    /**
     * Shows Plus Sign
     */
    public void showPlusSign() {
        showPlusOnMapping = true;
        targetTree.setShowPlusSign(true);
    }

    /**
     * Returns the Source TreeHandler
     * 
     * @return TreeHandler
     */
    public DNDTreeHandler getSourceTree() {
        return sourceTree;
    }

    /**
     * Returns the Target TreeHandler
     * 
     * @return TreeHandler
     */
    public DNDTreeHandler getTargetTree() {
        return targetTree;
    }

    /**
     * Returns the InputParam ListHandler
     * 
     * @return InputParam ListHandler
     */
    public DNDListHandler getParamList() {
        return inputParamListHandler;
    }

    /**
     * Invoked when Next Button Pressed
     * 
     * @param shell
     *            Shell instance
     * 
     * @return true, if the screen is complete, else the next button is disabled
     */
    public boolean nextPressed(final Shell shell) {
        return true;
    }

    /**
     * Not used
     * 
     * @param e
     *            Selection Event
     */
    public void widgetDefaultSelected(final SelectionEvent e) {
    }

    /**
     * Fired when the widget is selected
     * 
     * @param e
     *            Selection Event
     */
    public void widgetSelected(final SelectionEvent e) {
        File outFile = null;
        boolean doOutput = false;
        if (e.getSource() == predResltsBtn) {
            if (wizPanComp instanceof ExecWizardPanel) {
                if (isExecute) {
                    isExecute = false;
                    predResltsBtn.setText(getPredRsltsStr);
                } else {
                    final WizardController wizCtrl = ((ExecWizardPanel) wizPanComp).getWizardController();
                    try {
                        isBuildSourceResults = true;
                        final XMLTreeNode data = (XMLTreeNode) wizCtrl.getFinishedData();
                        final Document dataElem = new Document((Element) data.getJDOMContent().getContent());
                        logger.debug(data.getJDOMContent().getFormattedXML());
                        final ExecuteArguments executeArgs = updateInputData(dataElem);
                        if (executeArgs.isOkPressed()) {
                            final ExecuteBizViewDocument ebvd = new ExecuteBizViewDocument("MapperWizardExecSoapMessage", executeArgs);
                            shell.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_WAIT));
                            isExecute = true;
                            predResltsBtn.setText(cancelPredRsltsStr);
                            ebvd.execute(this);
                        } else {
                            predResltsBtn.setText(getPredRsltsStr);
                        }
                    } catch (final WizardException e1) {
                        ControlFactory.showInfoDialog(translator.getString("Execution of SOAP message failed Exception"), "Exception: " + e1.getMessage());
                        logger.debug(e1);
                    }
                }
            }
        } else if (e.getSource() == sourceBtn) {
            if (targetTree.isAnythingChanged()) {
                final int choice = ControlFactory.showConfirmDialog(translator.getString("Editing source XML will undo your changes, if any, to the target tree. Continue?"));
                if (choice == Window.OK) {
                    changeXML(sourceTree, translator.getString("Edit Source XML"));
                }
            } else {
                changeXML(sourceTree, translator.getString("Edit Source XML"));
            }
            sourceBtn.forceFocus();
        } else if (e.getSource() == targetBtn) {
            changeXML(targetTree, translator.getString("Edit Target XML"));
            targetBtn.forceFocus();
        } else if (e.getSource() == autoMapBtn) {
            try {
                UserPrefs.getDefaultAutoMap().autoMap(sourceTree, targetTree, inputParamListHandler, WizardFactory.getDefaultInstance().getController().getMyDef());
            } catch (final WizardException ex) {
                logger.finest("Exception caught, while performing auto-mapping :" + ex);
                logger.printStackTrace(ex);
            }
        } else if (e.getSource() == undoBtn) {
            targetTree.undo();
            if (showPlusOnMapping) {
                targetTree.setShowPlusSign(true);
            }
        } else if (e.getSource() == resetBtn) {
            final int choice = ControlFactory.showConfirmDialog(translator.getString("Reset will undo all of your changes. Continue?"));
            if (choice == Window.OK) {
                createComponents();
                if ((wizPanComp != null) && wizPanComp.shouldResetFromBizComp()) {
                    ((XMLMapper) wizPanComp).resetInitFromBizComp();
                    wizPanComp.initFromBizCompRoot();
                }
                if (MapperHelper.getCurrentMapperProcessor() != null) {
                    MapperHelper.getCurrentMapperProcessor().rebuildMappingsHashTable();
                }
                undoBtn.setFocus();
            }
        } else if (e.getSource() == setRepeatingBtn) {
            final MapTreeNode sourceNode = (MapTreeNode) sourceTree.getSelectedNode();
            MapTreeNode targetNode = (MapTreeNode) targetTree.getSelectedNode();
            if (!okToSetRepeating(sourceNode, targetNode)) {
                return;
            }
            doSetRepeating(sourceNode, targetNode);
        } else if (e.getSource() == exportBtn) {
            doExport(outFile, doOutput);
        }
        targetTree.refreshTree();
    }

    /**
     * @param p_outFile
     * @param p_doOutput
     */
    protected void doExport(File p_outFile, boolean p_doOutput) {
        XAChooser chooser = null;
        chooser = new XAChooser(Display.getCurrent().getActiveShell(), XA_Designer_Plugin.getActiveEditedInternalFrameAbsoluteFilePath(), translator.getString("Export Mapper Properties To..."), SWT.SAVE);
        chooser.addDefaultFilter(XAFileConstants.TEXT);
        chooser.addFilter(XAFileConstants.TEXT);
        final String fileSuccessVal = chooser.open();
        if (fileSuccessVal != null) {
            p_outFile = new File(fileSuccessVal);
            if ((p_outFile != null) && p_outFile.exists()) {
                if (((appendChk != null) && !appendChk.getSelection())) {
                    final int decision = ControlFactory.showConfirmDialog(translator.getString("File exists. Replace?"), translator.getString("File exists"));
                    if (decision == Window.OK) {
                        p_doOutput = true;
                    } else if (decision == Window.CANCEL) {
                        p_doOutput = false;
                    }
                } else {
                    p_doOutput = true;
                }
            } else {
                p_doOutput = true;
            }
        } else if (fileSuccessVal == null) {
            p_doOutput = false;
            exportBtn.forceFocus();
        }
        if (p_doOutput) {
            try {
                boolean append = false;
                if ((appendChk != null) && appendChk.getSelection()) {
                    append = true;
                    if ((p_outFile != null) && !p_outFile.exists()) {
                        append = false;
                    }
                } else {
                    append = false;
                }
                final FileWriter out = new FileWriter(p_outFile, append);
                out.write(myTableContentProvider.getTableAsCSVString(append, dataSourceDesc).toCharArray());
                out.close();
            } catch (final Exception ex) {
                final String message = "Exception exporting mapper properties:" + ex.getMessage();
                logger.severe(message);
                ControlFactory.showInfoDialog(translator.getString("Could not export mapper properties."), message);
            }
        }
    }

    /**
     * @param p_sourceNode
     * @param p_targetNode
     */
    protected void doSetRepeating(final MapTreeNode p_sourceNode, MapTreeNode p_targetNode) {
        int index;
        int position;
        MapTreeNode mapNode;
        String mapMatchPathStr = generatePathString(p_sourceNode, showPlusOnMapping, !showPlusOnMapping, false, replaceSourceRootWithDotInPath);
        mapMatchPathStr = MapDNDPathHelper.adjustForParentMap(p_targetNode, mapMatchPathStr);
        final Element mapElement = new Element("map", ns);
        mapElement.setAttribute("map_match", mapMatchPathStr, ns);
        mapNode = new MapTreeNode(mapElement);
        final MapTreeNode targetParentNode = (MapTreeNode) p_targetNode.getParent();
        index = targetParentNode.getIndex(p_targetNode);
        final List tempChildList = ((Element) targetParentNode.myContent).getChildren();
        final ArrayList<Content> childList = new ArrayList<Content>();
        Iterator childrens = tempChildList.iterator();
        while (childrens.hasNext()) {
            childList.add((Content) ((Content) childrens.next()).clone());
        }
        targetParentNode.removeAllChildren();
        final Iterator iter = childList.iterator();
        position = targetParentNode.getChildCount();
        while (iter.hasNext()) {
            if (position != index) {
                targetParentNode.add(new MapTreeNode(iter.next()));
            } else {
                p_targetNode = new MapTreeNode(iter.next());
                mapNode.add(p_targetNode);
                targetParentNode.add(mapNode);
                targetTree.refreshTree(targetParentNode);
                targetTree.setLastNodeXAMapped(p_targetNode);
                targetTree.setLastXAMappedIndex(index);
                targetTree.setAnythingChanged(true);
            }
            position++;
        }
        targetTree.refreshTree();
        int row = targetTree.getRowID(targetParentNode);
        row = row + index + 1;
        targetTree.expandRow(row, 1);
        targetTree.expandRow(row + 1, 1);
        targetTree.expandRow(row + 2, 1);
        targetTree.setLastTreeAction(MapDNDTreeHandler.TREE_XA_MAP);
        if (MapperHelper.getCurrentMapperProcessor() != null) {
            MapperHelper.getCurrentMapperProcessor().rebuildMappingsHashTable();
        }
    }

    /**
     * Returns the Path
     * 
     * @param selectedTreePath
     *            Selected node
     * 
     * @return Path
     */
    public String generatePathString(final MapTreeNode selectedTreePath, final boolean showResultsAxis, final boolean showInputAxis, final boolean showDocAxis, final boolean replaceRootWithDot) {
        final String path = selectedTreePath.getXAPathString(showResultsAxis, showInputAxis, showDocAxis, replaceRootWithDot);
        return path;
    }

    /**
     * Returns true if Set Repeating is allowed, false otherwise
     * 
     * @param sourceNode
     *            Source Node
     * @param targetNode
     *            Target Node
     * 
     * @return true if Set Repeating is allowed, false otherwise
     */
    public boolean okToSetRepeating(final MapTreeNode sourceNode, final MapTreeNode targetNode) {
        final int style = SWT.OK | SWT.ICON_INFORMATION;
        if (sourceNode == null) {
            ControlFactory.showMessageDialog(translator.getString("Select an element from the source tree."), translator.getString("Information"), style);
            return false;
        }
        if (targetNode == null) {
            ControlFactory.showMessageDialog(translator.getString("Select an element from the target tree."), translator.getString("Information"), style);
            return false;
        }
        if (!(sourceNode.myContent instanceof Element)) {
            ControlFactory.showMessageDialog(translator.getString("Select an element from the source tree."), translator.getString("Information"), style);
            return false;
        }
        if (!(targetNode.myContent instanceof Element)) {
            ControlFactory.showMessageDialog(translator.getString("Select an element from the target tree."), translator.getString("Information"), style);
            return false;
        }
        if ((sourceNode.getParent() == null) || (targetNode.getParent() == null)) {
            ControlFactory.showMessageDialog(translator.getString("Do not select root element as repeating element."), translator.getString("Information"), style);
            return false;
        }
        if ((targetNode.myContent instanceof Element) && ((Element) targetNode.myContent).getQualifiedName().equals("xa:map")) {
            ControlFactory.showMessageDialog(translator.getString("Do not select xa:map as repeating element."), translator.getString("Information"), style);
            return false;
        }
        final MapTreeNode targetParent = (MapTreeNode) targetNode.getParent();
        if (((Element) targetParent.myContent).getQualifiedName().equals("xa:map")) {
            ControlFactory.showMessageDialog(translator.getString("Cannot set repeating on the selected target node as it is already marked as repeating."), translator.getString("Information"), style);
            return false;
        }
        return true;
    }

    /**
     * Checks if the node is mapped
     * 
     * @param node
     *            Node to be checked
     * 
     * @return true if Mapped, false otherwise
     */
    public boolean isAnyParentXAMap(final MapTreeNode node) {
        MapTreeNode parent = (MapTreeNode) node.getParent();
        while (parent != null) {
            if (((Element) parent.myContent).getQualifiedName().equals("xa:map")) {
                return true;
            }
            parent = (MapTreeNode) parent.getParent();
        }
        return false;
    }

    /**
     * Checks if the node is Child node is Mapped,
     * 
     * @param node
     *            Node to be checked
     * 
     * @return true if Child node is Mapped, false otherwise
     */
    public boolean isAnyChildXAMap(final MapTreeNode node) {
        final Enumeration enumeration = node.children();
        while ((enumeration != null) && enumeration.hasMoreElements()) {
            final MapTreeNode child = (MapTreeNode) enumeration.nextElement();
            if (child.myContent instanceof Element) {
                if (((Element) child.myContent).getQualifiedName().equals("xa:map") || (isAnyChildXAMap(child) == true)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the Selected File Path Node
     * 
     * @param selectedFilePath
     *            File Path
     * 
     * @return Element in the path
     */
    public Element getDocTemplate(final String selectedFilePath) {
        Element elem = new Element("NewElement");
        elem.setAttribute("visible", "yes", ns);
        try {
            if (selectedFilePath == null) {
                logger.finest("file is null");
            } else {
                logger.finest("file is not  null" + selectedFilePath.toString());
            }
            final SAXBuilder sb = new SAXBuilder();
            final Document doc = sb.build(new File(selectedFilePath).toURL());
            elem = doc.getRootElement();
        } catch (final Throwable ex) {
            logger.severe("Error reading bizdoc template file. Exception: " + ex.toString());
            logger.printStackTrace(ex);
        }
        return elem;
    }

    /**
     * Opens the Change XML Dialog Box
     * 
     * @param tree
     *            TreeHandler
     * @param title
     *            Title for the Dialog Box
     */
    public void changeXML(final MapDNDTreeHandler tree, final String title) {
        final XMLGetter getter = new XMLGetter(shell, title, null, tree, true);
        final int status = getter.open();
        if (status == Window.OK) {
            if (myTable != null) {
                while (myTableContentProvider.getRowCount() != 0) {
                    myTableContentProvider.removeSpecializedObject(0);
                }
            }
            try {
                final MapTreeNode returned = getter.makeChanges();
                if (!getter.isError()) {
                    tree.setRoot(returned);
                    tree.refreshTree();
                    tree.expandTree(4);
                    if (tree.equals(targetTree)) {
                        newTargetRootElement = (Element) ((Element) returned.myContent).clone();
                        tree.setAnythingChanged(false);
                    } else if (tree.equals(sourceTree)) {
                        if (targetTree.isAnythingChanged()) {
                            final MapTreeNode targTreeNode = (MapTreeNode) targetTree.getRoot();
                            final Element targetUnmappedElem = (Element) targTreeNode.myContent;
                            final Element unmappedJdomC = getUnmappedTargetNode(targetUnmappedElem);
                            targetTree.setRoot(new MapTreeNode(unmappedJdomC));
                            targetTree.setAnythingChanged(false);
                            targetTree.expandTree(2);
                        }
                        sourceRootElement = (Element) returned.myContent;
                    }
                    if (sourceRootElement != null) {
                        XMLNamespaceUtil.resolveNamespaceConflicts(sourceRootElement, newTargetRootElement);
                    }
                    targetTree.setInputXML(sourceRootElement);
                    final MapTreeNode sourceRootNode = (MapTreeNode) sourceTree.getRoot();
                    final Element sourceRootElement = (Element) sourceRootNode.myContent;
                    final MapTreeNode targetRootNode = (MapTreeNode) targetTree.getRoot();
                    targetRootNode.setMapSourceXML(sourceRootElement);
                    targetRootNode.setMapTable(myTable);
                }
            } catch (final Throwable ex) {
                final String message = "Exception opening XML:" + ex.toString();
                logger.severe(message);
                ControlFactory.showInfoDialog(translator.getString("Could not open XML."), message);
            }
        }
    }

    /**
     * Get the response node and remove all mappings, then return it as an XMLTreeNode
     * 
     * @param bcRootNode
     * @return
     */
    private Element getUnmappedTargetNode(final Element rootNode) {
        Element retNode = null;
        if (rootNode == null) {
            retNode = new Element("NewElement");
        } else {
            removeMappings(rootNode);
            retNode = rootNode;
        }
        return retNode;
    }

    /**
     * remove xa:copy attributes and any %var% values
     * 
     * @param elem
     * @return
     */
    private void removeMappings(final Element elem) {
        if (elem == null) {
            return;
        }
        final List attrs = elem.getAttributes();
        int size = attrs.size();
        for (int i = 0; i < size; i++) {
            final Attribute attr = (Attribute) attrs.get(i);
            if (attr.getName().equals(XAwareConstants.BIZCOMPONENT_ATTR_COPY)) {
                attr.detach();
            } else {
                String value = attr.getValue();
                value = value.replaceAll("%.*%", "");
                attr.setValue(value);
            }
        }
        setText(elem);
        final List children = elem.getChildren();
        size = children.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                removeMappings((Element) children.get(i));
            }
        }
        return;
    }

    /**
     * Sets the text values to the element.
     * 
     * @param elem
     *            element instance
     */
    private void setText(final Element elem) {
        String text = elem.getText();
        if (text != null) {
            text = text.replaceAll("%.*%", "");
        }
        final JDOMContent jc = JDOMContentFactory.createJDOMContent(elem);
        if (text != null) {
            jc.setText(text);
        }
    }

    /**
     * Returns the Source Root Node
     * 
     * @return Source Root Node
     */
    public MapTreeNode getSourceRootNode() {
        return (MapTreeNode) sourceTree.getRoot();
    }

    /**
     * Returns the Target Root Node
     * 
     * @return Target Root Node
     */
    public MapTreeNode getTargetRootNode() {
        MapTreeNode retNode;
        if (isBuildSourceResults) {
            isBuildSourceResults = false;
        }
        retNode = (MapTreeNode) ((MapTreeNode) targetTree.getRoot()).clone();
        final Element targElem = (Element) retNode.myContent;
        if ((targElem.getAttributes().size() == 0) && ("".equals(targElem.getTextTrim())) && targElem.getChildren().size() == 0) {
            targElem.setText(XAwareConstants.XAWARE_SUBSTITUTE_DELIMITER + XAwareConstants.XA_INPUT_URL + XAwareConstants.XAWARE_SUBSTITUTE_DELIMITER);
            return retNode;
        }
        retNode = (MapTreeNode) targetTree.getRoot();
        return retNode;
    }

    /**
     * Sets the DataSource Description
     * 
     * @param desc
     *            Description
     */
    public void setDataSourceDesc(final String desc) {
        dataSourceDesc = desc;
    }

    /**
     * Returns the map table
     * 
     * @return map table
     */
    public MapReportTable getMapTable() {
        return myTable;
    }

    /**
     * Sets the Source tree root node
     * 
     * @param root
     *            root node
     */
    public void setSourceTree(final Element root) {
        sourceTree.setRoot(new MapTreeNode(root));
    }

    /**
     * Sets the Target tree root node
     * 
     * @param root
     *            root node
     */
    public void setTargetTree(final Element root) {
        targetTree.setRoot(new MapTreeNode(root));
        final MapTreeNode sourceRootNode = (MapTreeNode) sourceTree.getRoot();
        final Element sourceRootElement = (Element) sourceRootNode.myContent;
        final MapTreeNode targetRootNode = (MapTreeNode) targetTree.getRoot();
        targetRootNode.setMapSourceXML(sourceRootElement);
        targetTree.setMapTable(myTable);
    }

    /**
     * Adds row to the map table
     * 
     * @param sourceStr
     *            Source String
     * @param targetStr
     *            Target String
     * @param descriptionStr
     *            Description String
     * @param ruleStr
     *            Rule String
     */
    public void addRowToMapTable(final String sourceStr, final String targetStr, final String descriptionStr, final String ruleStr) {
        if (myTableContentProvider != null) {
            final MapTable map = new MapTable(sourceStr, targetStr, descriptionStr, ruleStr);
            myTableContentProvider.addSpecializedObject(map);
        }
    }

    /**
     * The listener that gets the results from the executed Soap message
     * 
     * @param results
     * @param success
     */
    public void setBizViewResults(final Document results, final boolean success) {
        if (isExecute) {
            isExecute = false;
            predResltsBtn.setText(getPredRsltsStr);
            final XMLOutputter out = new XMLOutputter();
            final String outStr = out.outputString(results);
            logger.debug(outStr);
            final Element rootElem = results.getRootElement();
            if (success && rootElem != null) {
                final List children = rootElem.getChildren();
                if (children.size() > 0) {
                    final Element resultElem = (Element) children.get(0);
                    final String detectErr = "Error";
                    if (detectErr.startsWith(resultElem.getName())) {
                        final Element errDetailElem = resultElem.getChild("detail");
                        String message = "Execution of SOAP message failed: ";
                        if (errDetailElem != null) {
                            message += errDetailElem.getTextTrim();
                        }
                        logger.info(message);
                        ControlFactory.showInfoDialog(translator.getString("Execution of SOAP message failed, source tree unchanged."), message);
                    } else if (resultElem.getQualifiedName().equals("Element") && resultElem.getChildren().size() == 1) {
                        final Element r = (Element) resultElem.getChildren().get(0);
                        setSourceTree(r);
                        getSourceTree().expandTree(4);
                    } else {
                        final String resultStr = resultElem.getTextTrim();
                        final StringReader sr = new StringReader(resultStr);
                        final SAXBuilder sb = new SAXBuilder();
                        try {
                            final Document doc = sb.build(sr);
                            final Element r = doc.getRootElement();
                            setSourceTree(r);
                            getSourceTree().expandTree(4);
                        } catch (final JDOMException e) {
                            final String message = "Execution of SOAP message failed: ";
                            ControlFactory.showInfoDialog(translator.getString("Execution of SOAP message failed, source tree unchanged."), message);
                            logger.debug(e);
                        } catch (final IOException e) {
                            final String message = "Execution of SOAP message failed: ";
                            ControlFactory.showInfoDialog(translator.getString("Execution of SOAP message failed, source tree unchanged."), message);
                            logger.debug(e);
                        }
                    }
                } else {
                    final String msg = "ExecuteFailed: " + outStr;
                    logger.info(msg);
                    ControlFactory.showInfoDialog(translator.getString("Execution of SOAP message failed, source tree unchanged."), msg);
                }
            } else {
                final String message = "Execution of SOAP message failed, source tree unchanged.";
                logger.info("Execution of SOAP message failed");
                ControlFactory.showInfoDialog(translator.getString("Execution of SOAP message failed, source tree unchanged."), message);
            }
            shell.setCursor(Display.getCurrent().getSystemCursor(SWT.NORMAL));
        }
    }

    /**
     * Checks whether updation is required for input data.
     * 
     * @return boolean value.
     */
    private ExecuteArguments updateInputData(final Document doc) {
        final ExecuteArguments executeParameters = new ExecuteArguments(doc);
        final Element root = doc.getRootElement();
        final Vector<InputParameterData> newExecInputParams = BizdocInputHelper.extractInputParameters(root, new Hashtable<String, InputParameterData>());
        executeParameters.setExecInputParams(newExecInputParams);
        final DebugInputParameterEditor editor = new DebugInputParameterEditor(newExecInputParams, "", DebugInputParameterEditor.NONE);
        final XADialog dialog = new XADialog(XA_Designer_Plugin.getShell(), new XADialogOperation() {

            public boolean okPressed() {
                executeParameters.setOkPressed(true);
                executeParameters.setExecInputParams(editor.getInputParams());
                executeParameters.setExecAdditionalXML(editor.getAdditionalXML());
                executeParameters.setExecAdditionalType(editor.getType());
                executeParameters.setUserID(editor.getUserId());
                executeParameters.setPassword(editor.getPwd());
                return true;
            }

            public boolean cancelPressed() {
                executeParameters.setOkPressed(false);
                return true;
            }
        }, editor, "Edit Input Parameters For Execution", true, true);
        dialog.open();
        return executeParameters;
    }

    public void setReplaceSourceRootWithDotInPath(boolean p_replaceSourceRootWithDot) {
        replaceSourceRootWithDotInPath = p_replaceSourceRootWithDot;
        targetTree.setReplaceSourceRootWithDot(p_replaceSourceRootWithDot);
    }
}
