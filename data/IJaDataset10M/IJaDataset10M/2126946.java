package net.zarubsys.unianalyzer.dataviews.lpt.gui.conf.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.zarubsys.unianalyzer.dataminers.lpt.gui.parameterObject.LPTMeasuringSettings;
import net.zarubsys.unianalyzer.dataminers.lpt.gui.parameterObject.PortSettings;
import net.zarubsys.unianalyzer.dataminers.lpt.gui.parameterObject.PreviewData;
import net.zarubsys.unianalyzer.dataminers.lpt.gui.parameterObject.StartStopData;
import net.zarubsys.unianalyzer.dataminers.lpt.gui.plugin.LPTGUIPlugin;
import net.zarubsys.unianalyzer.dataminers.lpt.pin.CustomizedPin;
import net.zarubsys.unianalyzer.dataminers.lpt.pin.IPin;
import net.zarubsys.unianalyzer.dataminers.lpt.pin.PinHelper;
import net.zarubsys.unianalyzer.dataminers.lpt.wizard.utils.LPTConsts;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.EditorPart;

/**
 * LptConfEditor
 *
 * @author  &lt;A HREF=&quot;mailto:lukas.zaruba@media-solutions.cz&quot;&gt;Lukas Zaruba&lt;/A&gt;, MEDIA SOLUTIONS CZECH REPUBLIC Ltd.
 * @version $Revision$ $Date$
 */
public class LptConfEditor extends EditorPart {

    private LPTMeasuringSettings originalMeasuringSettings;

    private LPTMeasuringSettings measuringSettings;

    private Label[] pins = new Label[8];

    private Button[] pinDescs = new Button[8];

    private Text[] pinNames = new Text[8];

    private Text name;

    private Text description;

    private Combo addressCombo;

    private Combo offsetCombo;

    private Text portFrequency;

    private Combo unitsCombo;

    private Label imageLabel;

    private Button automatic;

    private Composite startTriggerCompostite;

    private Combo pinCombo;

    private Spinner maxWaitingIntSpinner;

    private Combo maxWaitingIntCombo;

    private Button rising;

    private Button falling;

    private Button timeStop;

    private Button samplesStop;

    private Text measureTime;

    private Combo measureTimeUnits;

    private Text sampleCountText;

    private List<Control> timeStopDetail = new ArrayList<Control>(2);

    private List<Control> sampleStopDetail = new ArrayList<Control>(2);

    private Set<Control> controlsToEnableDisable = null;

    private final UseTriggerListener useTriggerlistener = new UseTriggerListener();

    private final RisingFallingSelectionListener risingFallingListener = new RisingFallingSelectionListener();

    private final StopRadioListener stopRadioListener = new StopRadioListener();

    private Button manual;

    private Label errorMessage;

    private final SelectionListener checkBoxListener = new CheckBoxListener();

    private final ModifyListener textBoxListener = new TextBoxListener();

    private boolean isDirty = false;

    private static final Logger log = Logger.getLogger(LptConfEditor.class);

    private boolean initSequence = false;

    private Composite pinNames1;

    private Composite pinNames2;

    public void doSave(IProgressMonitor monitor) {
        IPathEditorInput pathInput = (IPathEditorInput) getEditorInput();
        File file = pathInput.getPath().toFile();
        saveInternal(monitor, file);
        swapMeasuringSettings(monitor);
        monitor.done();
    }

    private void saveInternal(IProgressMonitor monitor, File file) {
        boolean valid = validate();
        monitor.worked(1);
        if (!valid) {
            MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Configuration contains error(s)!", null, "Do you want to save your settings anyway?\nNote: This settings cannot be run until it will be fixed.", MessageDialog.WARNING, new String[] { "Yes", "No" }, 1);
            boolean saveAnyway = dialog.open() == 0;
            if (!saveAnyway) return;
        }
        monitor.beginTask("Saving...", 4);
        monitor.worked(1);
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new FileOutputStream(file));
            os.writeObject(measuringSettings);
        } catch (FileNotFoundException e) {
            log.error("Error while saving measuring settings.", e);
        } catch (IOException e) {
            log.error("Error while saving measuring settings.", e);
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                log.error("Error while closing output stream of measuring settings.", e);
            }
        }
    }

    private void swapMeasuringSettings(IProgressMonitor monitor) {
        try {
            originalMeasuringSettings = (LPTMeasuringSettings) measuringSettings.clone();
        } catch (CloneNotSupportedException e) {
            log.error("Error while cloning measuringSettings.", e);
        }
        monitor.worked(1);
        isDirty = false;
        firePropertyChange(PROP_DIRTY);
    }

    public void doSaveAs() {
        SaveAsDialog dialog = new SaveAsDialog(Display.getCurrent().getActiveShell());
        dialog.setOriginalName(getEditorInput().getName());
        if (dialog.open() != SaveAsDialog.OK) {
            return;
        }
        IPath ws = Platform.getLocation();
        IPath resPath = dialog.getResult();
        IPath path = ws.append(resPath);
        File file = path.toFile();
        if (!file.exists()) {
            boolean created = false;
            try {
                created = file.createNewFile();
            } catch (IOException e) {
                MessageDialog errorDialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Cannot create file", null, "File \"" + file.getAbsolutePath() + "\" cannot be created.\nError message: " + e.getMessage(), MessageDialog.ERROR, new String[] { "OK" }, 0);
                errorDialog.open();
                return;
            }
            if (!created) {
                MessageDialog errorDialog = new MessageDialog(Display.getCurrent().getActiveShell(), "Cannot create file", null, "File \"" + file.getAbsolutePath() + "\" cannot be created.", MessageDialog.ERROR, new String[] { "OK" }, 0);
                errorDialog.open();
            }
        }
        saveInternal(new NullProgressMonitor(), file);
        IFile resFile = ResourcesPlugin.getWorkspace().getRoot().getFile(resPath);
        try {
            resFile.refreshLocal(1, new NullProgressMonitor());
        } catch (CoreException e) {
            log.error("Error refreshing Workspace...", e);
        }
    }

    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        if (!(input instanceof IPathEditorInput)) {
            throw new PartInitException("Invalid input: Must be IPathEditorInput");
        }
        setInput(input);
        IPathEditorInput pathInput = (IPathEditorInput) input;
        measuringSettings = getMeasuringSettings(pathInput);
        try {
            originalMeasuringSettings = (LPTMeasuringSettings) measuringSettings.clone();
        } catch (CloneNotSupportedException e) {
            log.error("Error while cloning measuringSettings.", e);
        }
        setPartName(pathInput.getName());
    }

    private LPTMeasuringSettings getMeasuringSettings(IPathEditorInput pathInput) throws PartInitException {
        File file = pathInput.getPath().toFile();
        ObjectInputStream ois = null;
        LPTMeasuringSettings result = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            result = (LPTMeasuringSettings) ois.readObject();
        } catch (FileNotFoundException e) {
            log.error("Error reading measuring settings.", e);
            throw new PartInitException("Error reading measuring settings.", e);
        } catch (IOException e) {
            log.error("Error reading measuring result.", e);
            throw new PartInitException("Error reading measuring settings.", e);
        } catch (ClassNotFoundException e) {
            log.error("Error reading measuring result.", e);
            throw new PartInitException("Error reading measuring settings.", e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    log.error("Error closing measuring settings input stream.", e);
                    throw new PartInitException("Error closing measuring settings input stream.", e);
                }
            }
        }
        return result;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public boolean isSaveAsAllowed() {
        return true;
    }

    public void createPartControl(Composite parent) {
        Label label;
        GridData gd;
        GridLayout layout;
        Composite composite = new Composite(parent, SWT.NONE);
        gd = new GridData(SWT.BEGINNING, SWT.FILL, false, true);
        gd.widthHint = 550;
        composite.setLayoutData(gd);
        composite.setLayout(new GridLayout(1, false));
        errorMessage = new Label(composite, SWT.NONE);
        gd = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
        gd.widthHint = 550;
        gd.heightHint = 15;
        errorMessage.setLayoutData(gd);
        errorMessage.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
        label = new Label(composite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText("Name:");
        name = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
        gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        gd.widthHint = 300;
        name.setLayoutData(gd);
        name.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (initSequence) return;
                Text t = (Text) e.widget;
                measuringSettings.setName(t.getText());
                validate();
            }
        });
        label = new Label(composite, SWT.NONE);
        gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        label.setLayoutData(gd);
        label.setText("Description:");
        description = new Text(composite, SWT.MULTI | SWT.LEAD | SWT.BORDER);
        gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        gd.widthHint = 450;
        gd.heightHint = 50;
        description.setLayoutData(gd);
        description.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (initSequence) return;
                Text t = (Text) e.widget;
                measuringSettings.setDescription(t.getText());
                validate();
            }
        });
        ExpandBar expandBar = new ExpandBar(composite, SWT.V_SCROLL);
        gd = new GridData(SWT.BEGINNING, SWT.FILL, false, true);
        gd.widthHint = 550;
        expandBar.setLayoutData(gd);
        Composite portSettingsComposite = new Composite(expandBar, SWT.NONE);
        portSettingsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        layout = new GridLayout(3, false);
        layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
        layout.verticalSpacing = 10;
        portSettingsComposite.setLayout(layout);
        label = new Label(portSettingsComposite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText("Port address:");
        label = new Label(portSettingsComposite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText("Port offset:");
        label = new Label(portSettingsComposite, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText("Frequency:");
        addressCombo = new Combo(portSettingsComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
        gd.widthHint = 150;
        addressCombo.setLayoutData(gd);
        addressCombo.setItems(LPTConsts.getAddressLabels());
        addressCombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (initSequence) return;
                Combo c = (Combo) e.widget;
                measuringSettings.getPortSettings().setPortNumber(LPTConsts.getAddressValues()[c.getSelectionIndex()]);
                validate();
            }
        });
        offsetCombo = new Combo(portSettingsComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
        gd.widthHint = 150;
        offsetCombo.setLayoutData(gd);
        offsetCombo.setItems(LPTConsts.getPortOffsets());
        offsetCombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (initSequence) return;
                int index = ((Combo) e.widget).getSelectionIndex();
                measuringSettings.getPortSettings().setPortOffset(index);
                PreviewData pData = PreviewData.getPreviewData(index);
                List<CustomizedPin> cPins = PreviewData.createCustomizedPins(pData);
                measuringSettings.setCustomizedPins(cPins);
                setupPreviewData();
                validate();
            }
        });
        Composite portSettingsFrequencyComposite = new Composite(portSettingsComposite, SWT.NONE);
        portSettingsFrequencyComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        portSettingsFrequencyComposite.setLayout(new GridLayout(2, false));
        portFrequency = new Text(portSettingsFrequencyComposite, SWT.SINGLE | SWT.BORDER);
        gd = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
        gd.widthHint = 60;
        portFrequency.setLayoutData(gd);
        portFrequency.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (initSequence) return;
                try {
                    String text = ((Text) e.widget).getText();
                    measuringSettings.getPortSettings().setFrequency(Integer.parseInt(text));
                } catch (NumberFormatException error) {
                    measuringSettings.getPortSettings().setFrequency(-1);
                }
                validate();
            }
        });
        unitsCombo = new Combo(portSettingsFrequencyComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        gd.widthHint = 70;
        unitsCombo.setLayoutData(gd);
        unitsCombo.setItems(LPTConsts.getFrequencyUnitsLabels());
        unitsCombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (initSequence) return;
                Combo tempCombo = (Combo) e.widget;
                int index = tempCombo.getSelectionIndex();
                PortSettings portSettings = measuringSettings.getPortSettings();
                portSettings.setUnitsComboValue(LPTConsts.getFrequencyUnitsValues()[index]);
                portSettings.setUnitsComboValueDescription(LPTConsts.getFrequencyUnitsLabels()[index]);
                validate();
            }
        });
        Composite portImageComposite = new Composite(portSettingsComposite, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        portImageComposite.setLayoutData(gd);
        portImageComposite.setLayout(new GridLayout(1, false));
        imageLabel = new Label(portImageComposite, SWT.NONE);
        gd = new GridData(SWT.CENTER, SWT.CENTER, false, false);
        gd.widthHint = 50;
        gd.heightHint = 180;
        imageLabel.setLayoutData(gd);
        Composite pinDescComposite = new Composite(portSettingsComposite, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.horizontalSpan = 2;
        pinDescComposite.setLayoutData(gd);
        pinDescComposite.setLayout(new GridLayout(2, false));
        Composite pins1 = new Composite(pinDescComposite, SWT.NONE);
        pins1.setLayout(new GridLayout(1, false));
        pins1.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        createPins(pins1, 0, 4);
        Composite pins2 = new Composite(pinDescComposite, SWT.NONE);
        pins2.setLayout(new GridLayout(1, false));
        pins2.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        createPins(pins2, 4, 4);
        ExpandItem portSettingsItem = new ExpandItem(expandBar, SWT.NONE);
        portSettingsItem.setText("PortSettings");
        portSettingsItem.setControl(portSettingsComposite);
        portSettingsItem.setHeight(portSettingsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        portSettingsItem.setExpanded(true);
        Composite pinNamesComposite = new Composite(expandBar, SWT.NONE);
        pinNamesComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout gdl = new GridLayout(2, false);
        gdl.horizontalSpacing = 40;
        pinNamesComposite.setLayout(gdl);
        pinNames1 = new Composite(pinNamesComposite, SWT.NONE);
        pinNames1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        pinNames1.setLayout(new GridLayout(2, false));
        pinNames2 = new Composite(pinNamesComposite, SWT.NONE);
        pinNames2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        pinNames2.setLayout(new GridLayout(2, false));
        createPinNames();
        ExpandItem pinNamesItem = new ExpandItem(expandBar, SWT.NONE);
        pinNamesItem.setText("PinNames");
        pinNamesItem.setControl(pinNamesComposite);
        pinNamesItem.setHeight(pinNamesComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        pinNamesItem.setExpanded(true);
        Composite triggerComposite = new Composite(expandBar, SWT.NONE);
        triggerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout gl = new GridLayout(1, false);
        gl.verticalSpacing = 12;
        triggerComposite.setLayout(gl);
        Group group = new Group(triggerComposite, SWT.NONE);
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        group.setLayout(new GridLayout(1, false));
        group.setText("Start");
        manual = new Button(group, SWT.RADIO);
        manual.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        manual.setText("Start manually");
        manual.addSelectionListener(useTriggerlistener);
        automatic = new Button(group, SWT.RADIO);
        automatic.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        automatic.setText("Use trigger");
        automatic.addSelectionListener(useTriggerlistener);
        startTriggerCompostite = new Composite(group, SWT.BORDER);
        startTriggerCompostite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        startTriggerCompostite.setLayout(new GridLayout(2, false));
        Composite leftTrigger = new Composite(startTriggerCompostite, SWT.NONE);
        leftTrigger.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        leftTrigger.setLayout(new GridLayout(2, false));
        label = new Label(leftTrigger, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText("Pin:");
        pinCombo = new Combo(leftTrigger, SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        gd.widthHint = 140;
        pinCombo.setLayoutData(gd);
        pinCombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (initSequence) return;
                int index = ((Combo) e.widget).getSelectionIndex();
                PinHelper pinHelper = PinHelper.getInstance();
                List<CustomizedPin> filteredPins = pinHelper.getCheckedPins(measuringSettings.getCustomizedPins());
                measuringSettings.getStartStopData().setStartTriggerPin(filteredPins.get(index));
                validate();
            }
        });
        label = new Label(leftTrigger, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText("Waiting int.:");
        Composite maxWaitingComposite = new Composite(leftTrigger, SWT.NONE);
        maxWaitingComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));
        maxWaitingComposite.setLayout(new GridLayout(2, false));
        maxWaitingIntSpinner = new Spinner(maxWaitingComposite, SWT.BORDER);
        gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        gd.widthHint = 50;
        maxWaitingIntSpinner.setLayoutData(gd);
        maxWaitingIntSpinner.setIncrement(1);
        maxWaitingIntSpinner.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (initSequence) return;
                int interval;
                try {
                    String text = ((Spinner) e.widget).getText();
                    interval = Integer.parseInt(text);
                } catch (NumberFormatException error) {
                    interval = -1;
                }
                measuringSettings.getStartStopData().setMaxWaitingInterval(interval);
                validate();
            }
        });
        maxWaitingIntCombo = new Combo(maxWaitingComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(SWT.BEGINNING, SWT.CENTER, true, false);
        gd.widthHint = 50;
        maxWaitingIntCombo.setLayoutData(gd);
        maxWaitingIntCombo.setItems(LPTConsts.getTimeUnitsLabels());
        maxWaitingIntCombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (initSequence) return;
                int index = ((Combo) e.widget).getSelectionIndex();
                int units = LPTConsts.getTimeUnitsValues()[index];
                measuringSettings.getStartStopData().setMaxWaitingIntervalUnits(units);
                validate();
            }
        });
        Composite rightTrigger = new Composite(startTriggerCompostite, SWT.NONE);
        rightTrigger.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        rightTrigger.setLayout(new GridLayout(2, false));
        label = new Label(rightTrigger, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));
        label.setText("Triger type:");
        Composite edgeComposite = new Composite(rightTrigger, SWT.NONE);
        edgeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        edgeComposite.setLayout(new GridLayout(1, false));
        rising = new Button(edgeComposite, SWT.RADIO);
        rising.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));
        rising.setText("Rising edge");
        rising.addSelectionListener(risingFallingListener);
        falling = new Button(edgeComposite, SWT.RADIO);
        falling.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));
        falling.setText("Falling edge");
        falling.addSelectionListener(risingFallingListener);
        Group group2 = new Group(triggerComposite, SWT.NONE);
        group2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        group2.setLayout(new GridLayout(1, false));
        group2.setText("Stop");
        Composite stopComposite = new Composite(group2, SWT.NONE);
        stopComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        stopComposite.setLayout(new GridLayout(1, false));
        timeStop = new Button(stopComposite, SWT.RADIO);
        timeStop.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        timeStop.addSelectionListener(stopRadioListener);
        timeStop.setText("Time");
        samplesStop = new Button(stopComposite, SWT.RADIO);
        samplesStop.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        samplesStop.addSelectionListener(stopRadioListener);
        samplesStop.setText("Sample count");
        Composite composite2 = new Composite(stopComposite, SWT.BORDER);
        composite2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite2.setLayout(new GridLayout(2, false));
        measureTime = new Text(composite2, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd.widthHint = 50;
        measureTime.setLayoutData(gd);
        timeStopDetail.add((Control) measureTime);
        measureTime.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (initSequence) return;
                try {
                    String text = ((Text) e.widget).getText();
                    measuringSettings.getStartStopData().setMeasureTime(Integer.parseInt(text));
                } catch (NumberFormatException error) {
                    measuringSettings.getStartStopData().setMeasureTime(-1);
                }
                validate();
            }
        });
        measureTimeUnits = new Combo(composite2, SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd.widthHint = 50;
        measureTimeUnits.setLayoutData(gd);
        measureTimeUnits.setItems(LPTConsts.getTimeUnitsLabels());
        measureTimeUnits.select(2);
        timeStopDetail.add((Control) measureTimeUnits);
        measureTimeUnits.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (initSequence) return;
                int index = ((Combo) e.widget).getSelectionIndex();
                int units = LPTConsts.getTimeUnitsValues()[index];
                measuringSettings.getStartStopData().setMeasureTimeUnits(units);
                validate();
            }
        });
        sampleCountText = new Text(composite2, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
        sampleCountText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        sampleStopDetail.add((Control) sampleCountText);
        sampleCountText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                if (initSequence) return;
                int sampleCount;
                try {
                    String text = ((Text) e.widget).getText();
                    sampleCount = Integer.parseInt(text);
                } catch (NumberFormatException error) {
                    sampleCount = -1;
                }
                measuringSettings.getStartStopData().setSampleCount(sampleCount);
                validate();
            }
        });
        label = new Label(composite2, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText("sample(s)");
        sampleStopDetail.add((Control) label);
        ExpandItem triggerItem = new ExpandItem(expandBar, SWT.NONE);
        triggerItem.setText("Trigger");
        triggerItem.setControl(triggerComposite);
        triggerItem.setHeight(triggerComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        triggerItem.setExpanded(true);
        setupValues();
    }

    private void createPinNames() {
        createPinNames(pinNames1, 0, 4);
        createPinNames(pinNames2, 4, 4);
    }

    private void createPinNames(Composite pinsComposite, int from, int count) {
        for (int i = 0; i < count; i++) {
            Button button = new Button(pinsComposite, SWT.CHECK);
            GridData gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
            gd.widthHint = 150;
            button.setLayoutData(gd);
            button.addSelectionListener(checkBoxListener);
            pinDescs[from + i] = button;
            Text text = new Text(pinsComposite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
            text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
            text.addModifyListener(textBoxListener);
            pinNames[from + i] = text;
        }
    }

    private void createPins(Composite group, int from, int count) {
        for (int i = 0; i < count; i++) {
            Label label = new Label(group, SWT.NONE);
            GridData gd = new GridData(SWT.BEGINNING, SWT.TOP, false, false);
            gd.widthHint = 150;
            label.setLayoutData(gd);
            label.setText(String.valueOf(i));
            pins[from + i] = label;
        }
    }

    public void setFocus() {
    }

    public boolean validate() {
        boolean newIsDirty = !originalMeasuringSettings.equals(measuringSettings);
        if (newIsDirty != isDirty) {
            isDirty = newIsDirty;
            firePropertyChange(PROP_DIRTY);
        }
        PortSettings ps = measuringSettings.getPortSettings();
        int resultFrequency = ps.getFrequency() * ps.getUnitsComboValue();
        if (resultFrequency < 1 || resultFrequency > 400000) {
            setUserOutput("Frequency has to be in range between 1 Hz to 400 KHz");
            return false;
        }
        StartStopData ssd = measuringSettings.getStartStopData();
        if (ssd.isUseStartTrigger()) {
            int waitingInterval = ssd.getMaxWaitingInterval() * ssd.getMaxWaitingIntervalUnits();
            if (waitingInterval < 100 || waitingInterval > 60000000) {
                setUserOutput("Maximal waiting interval has to be between 100 µs and 60 s.");
                return false;
            }
        }
        if (ssd.isStopByTime()) {
            int time = ssd.getMeasureTime() * ssd.getMeasureTimeUnits();
            if (time < 100 || time > 60000000) {
                setUserOutput("Measuring time has to be between 100 µs and 60 s.");
                return false;
            }
        } else {
            int sampleCount = ssd.getSamplesCount();
            if (sampleCount < 100 || sampleCount > 100000000) {
                setUserOutput("Sample count has to be between 100 and 100 000 000.");
                return false;
            }
        }
        boolean oneOrMoreChecked = false;
        for (CustomizedPin cPin : measuringSettings.getCustomizedPins()) {
            oneOrMoreChecked |= cPin.isChecked();
            if (cPin.getCustomizedName().length() > 8) {
                setUserOutput("Length of pin description (\"" + cPin.getCustomizedName() + "\") cannot be longer than 8 chars.");
                return false;
            }
        }
        if (!oneOrMoreChecked) {
            setUserOutput("At least one pin has to be checked for measuring.");
            return false;
        }
        setUserOutput(null);
        return true;
    }

    private void setUserOutput(String output) {
        if (output == null) {
            errorMessage.setText("");
            return;
        }
        errorMessage.setText(output);
    }

    private void setStartTriggerEnabled(boolean enable) {
        if (controlsToEnableDisable == null) {
            controlsToEnableDisable = getControls(startTriggerCompostite);
        }
        startTriggerCompostite.setEnabled(enable);
        for (Control control : controlsToEnableDisable) {
            control.setEnabled(enable);
        }
    }

    private Set<Control> getControls(Control control) {
        if (control instanceof Composite) {
            Set<Control> result = new HashSet<Control>();
            Control[] controls = ((Composite) control).getChildren();
            result.addAll(Arrays.asList(controls));
            for (Control c : controls) {
                result.addAll(getControls(c));
            }
            return result;
        } else {
            return Collections.emptySet();
        }
    }

    private void setupValues() {
        initSequence = true;
        String measuringName = measuringSettings.getName();
        if (measuringName != null) {
            name.setText(measuringName);
        }
        String measuringDescription = measuringSettings.getDescription();
        if (measuringDescription != null) {
            description.setText(measuringDescription);
        }
        int portNumber = measuringSettings.getPortSettings().getPortNumber();
        String portString = "0x" + Integer.toHexString(portNumber);
        setupComboByString(addressCombo, portString);
        offsetCombo.select(measuringSettings.getPortSettings().getPortOffset());
        String frequencyText = String.valueOf(measuringSettings.getPortSettings().getFrequency());
        portFrequency.setText(frequencyText);
        setupComboByString(unitsCombo, measuringSettings.getPortSettings().getUnitsComboValueDescription());
        setupPreviewData();
        setupCustomizedPins();
        setupStartNStop();
        initSequence = false;
    }

    private void updatePins(List<CustomizedPin> pins) {
        PinHelper pinHelper = PinHelper.getInstance();
        List<CustomizedPin> filteredPins = pinHelper.getCheckedPins(pins);
        pinCombo.setItems(pinHelper.convertCustomizedPinsToStrings(filteredPins));
        pinCombo.select(0);
    }

    private void setupPreviewData() {
        PreviewData previewData = PreviewData.getPreviewData(measuringSettings.getPortSettings().getPortOffset());
        imageLabel.setImage(LPTGUIPlugin.getDefault().getImage(previewData));
        for (int i = 0; i < 8; i++) {
            Label pinLabel = pins[i];
            IPin pinDesc = previewData.getPin(i);
            pinLabel.setText(pinDesc.getLabel());
            pinLabel.setEnabled(!pinDesc.isReserved());
        }
    }

    private void setupStartNStop() {
        maxWaitingIntSpinner.setMaximum(999);
        maxWaitingIntSpinner.setMinimum(1);
        setupMaxWaitingInt();
        setupStartStopRadios();
        updatePins(measuringSettings.getCustomizedPins());
        pinCombo.select(measuringSettings.getStartStopData().getStartTriggerPin().getOrderInByte());
        setupStop();
        setStartTriggerEnabled(measuringSettings.getStartStopData().isUseStartTrigger());
    }

    private void setupStartStopRadios() {
        boolean risingEdge = measuringSettings.getStartStopData().isRisingEdge();
        rising.setSelection(risingEdge);
        falling.setSelection(!risingEdge);
        boolean stopByTime = measuringSettings.getStartStopData().isStopByTime();
        timeStop.setSelection(stopByTime);
        samplesStop.setSelection(!stopByTime);
        enableDisableStopDetail(stopByTime);
        boolean useStartTrigger = measuringSettings.getStartStopData().isUseStartTrigger();
        automatic.setSelection(useStartTrigger);
        manual.setSelection(!useStartTrigger);
        setStartTriggerEnabled(useStartTrigger);
    }

    private void setupMaxWaitingInt() {
        int value = measuringSettings.getStartStopData().getMaxWaitingInterval();
        maxWaitingIntSpinner.setSelection(value);
        int units = measuringSettings.getStartStopData().getMaxWaitingIntervalUnits();
        int index = getTimeUnitsIndex(units);
        maxWaitingIntCombo.select(index);
    }

    private void setupStop() {
        measureTime.setText(String.valueOf(measuringSettings.getStartStopData().getMeasureTime()));
        measureTimeUnits.select(getTimeUnitsIndex(measuringSettings.getStartStopData().getMeasureTimeUnits()));
        sampleCountText.setText(String.valueOf(measuringSettings.getStartStopData().getSamplesCount()));
    }

    private int getTimeUnitsIndex(int value) {
        return LPTConsts.findValueIndex(LPTConsts.getTimeUnitsValues(), value);
    }

    private void setupCustomizedPins() {
        int i = 0;
        for (CustomizedPin pin : measuringSettings.getCustomizedPins()) {
            Button pinButton = pinDescs[i];
            pinButton.setText(pin.getLabel() + " - ");
            boolean enabled = !pin.isReserved();
            boolean checked = pin.isChecked();
            pinButton.setSelection(checked);
            pinButton.setGrayed(!enabled);
            pinButton.setEnabled(enabled);
            Text pinText = pinNames[i];
            pinText.setEnabled(enabled && checked);
            pinText.setText(pin.getCustomizedName());
            i++;
        }
    }

    private void setupComboByString(Combo combo, String textValue) {
        List<String> values = Arrays.asList(combo.getItems());
        int index = values.indexOf(textValue);
        if (index < 0) throw new RuntimeException("Unknown value \"" + textValue + "\"");
        combo.select(index);
    }

    private class UseTriggerListener implements SelectionListener {

        public void widgetDefaultSelected(SelectionEvent e) {
        }

        public void widgetSelected(SelectionEvent e) {
            if (initSequence) return;
            boolean enable = (e.widget == automatic && ((Button) e.widget).getSelection() == true);
            measuringSettings.getStartStopData().setUseStartTrigger(enable);
            setStartTriggerEnabled(enable);
            validate();
        }
    }

    private void enableDisableStopDetail(boolean enable) {
        for (Control control : timeStopDetail) {
            control.setEnabled(enable);
        }
        for (Control control : sampleStopDetail) {
            control.setEnabled(!enable);
        }
    }

    private final class StopRadioListener implements SelectionListener {

        public void widgetDefaultSelected(SelectionEvent e) {
        }

        public void widgetSelected(SelectionEvent e) {
            if (initSequence) return;
            boolean enable = (e.widget == timeStop && ((Button) e.widget).getSelection() == true);
            measuringSettings.getStartStopData().setStopByTime(enable);
            enableDisableStopDetail(enable);
            validate();
        }
    }

    private final class RisingFallingSelectionListener implements SelectionListener {

        public void widgetDefaultSelected(SelectionEvent e) {
        }

        public void widgetSelected(SelectionEvent e) {
            if (initSequence) return;
            boolean isRisignSelected = (e.widget == rising && ((Button) e.widget).getSelection() == true);
            measuringSettings.getStartStopData().setRisingEdge(isRisignSelected);
            validate();
        }
    }

    private class CheckBoxListener implements SelectionListener {

        public void widgetDefaultSelected(SelectionEvent e) {
        }

        public void widgetSelected(SelectionEvent e) {
            if (initSequence) return;
            for (int i = 0; i < pinDescs.length; i++) {
                if (pinDescs[i] == (e.getSource())) {
                    Button b = pinDescs[i];
                    boolean selection = b.getSelection();
                    measuringSettings.getCustomizedPins().get(i).setChecked(selection);
                    pinNames[i].setEnabled(selection);
                    break;
                }
            }
            validate();
        }
    }

    private class TextBoxListener implements ModifyListener {

        public void modifyText(ModifyEvent e) {
            if (initSequence) return;
            for (int i = 0; i < pinNames.length; i++) {
                if (pinNames[i] == (e.getSource())) {
                    Text t = pinNames[i];
                    measuringSettings.getCustomizedPins().get(i).setCustomizedName(t.getText());
                    break;
                }
            }
            validate();
        }
    }
}
