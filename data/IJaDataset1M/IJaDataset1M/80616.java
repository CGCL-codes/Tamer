package net.sf.ulmac.ui.preferences;

import net.sf.ulmac.core.ids.IPreferenceIds;
import net.sf.ulmac.core.listeners.WebsiteLinkListener;
import net.sf.ulmac.core.utils.FileUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

public class ExternalProgramsLamePreferencePage extends AbstractPreferencePage implements IWorkbenchPreferencePage {

    private enum EncodingQuality {

        CUSTOM("Custom"), EXTREME("Extreme"), INSANE("Insane"), NORMAL("Normal");

        private String fDisplay;

        private EncodingQuality(String display) {
            fDisplay = display;
        }

        public String getDisplay() {
            return fDisplay;
        }
    }

    private enum VariableBitrateMode {

        FAST("Fast"), STANDARD("Standard");

        private String fDisplay;

        private VariableBitrateMode(String display) {
            fDisplay = display;
        }

        public String getDisplay() {
            return fDisplay;
        }
    }

    public static final String ID = "net.sf.ulmac.ui.preferences.encodingLamePreferencePage";

    private Button fBtnCBR;

    private Button fBtnSetQualityManually;

    private Button fBtnTargetBitRate;

    private Button fBtnTargetQuality;

    private Combo fCboEncodingQuality;

    private Combo fCboVBRMode;

    private Composite fContainer;

    private String fFilePath = "";

    private Group fGrpCustomQuality;

    private Label fLblVBRMode;

    private Scale fSclTargetBitRate;

    private Scale fSclTargetQuality;

    private Composite fTargetBitRateContainer;

    private Composite fTargetQualityContainer;

    private Text fTxtCustomParameters;

    private Text fTxtLamePath;

    private Text fTxtTargetBitRate;

    private Text fTxtTargetQuality;

    public ExternalProgramsLamePreferencePage() {
        super();
    }

    @Override
    public Control createContents(final Composite parent) {
        fContainer = new Composite(parent, SWT.NULL);
        fContainer.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        fContainer.setLayout(new GridLayout(1, false));
        Composite filePathContainer = new Composite(fContainer, SWT.NULL);
        filePathContainer.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        filePathContainer.setLayout(new GridLayout(3, false));
        final Label lblLamePath = new Label(filePathContainer, SWT.NONE);
        lblLamePath.setText("Program Path:");
        lblLamePath.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 3, 1));
        fTxtLamePath = new Text(filePathContainer, SWT.BORDER);
        fTxtLamePath.setEditable(!System.getProperty("os.name").contains("Windows"));
        final Button btnLamePath = new Button(filePathContainer, SWT.PUSH);
        btnLamePath.setText("...");
        btnLamePath.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                fFilePath = FileUtils.getInstance().findFile("Select LAME Executable", null);
                if (FileUtils.isFileValid(fFilePath)) {
                    fTxtLamePath.setText(fFilePath);
                    fContainer.pack();
                }
            }
        });
        final Link lnkLame = new Link(filePathContainer, SWT.PUSH);
        lnkLame.setText("<a>Download LAME</a>");
        lnkLame.setForeground(new Color(null, 0, 0, 255));
        lnkLame.addListener(SWT.Selection, new WebsiteLinkListener("http://lame.sourceforge.net/links.php#Binaries"));
        Composite encodingQualityContainer = new Composite(fContainer, SWT.NULL);
        encodingQualityContainer.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        encodingQualityContainer.setLayout(new GridLayout(2, false));
        final Label lblEncodingQuality = new Label(encodingQualityContainer, SWT.NONE);
        lblEncodingQuality.setText("Encoding Quality:");
        lblEncodingQuality.pack();
        fCboEncodingQuality = new Combo(encodingQualityContainer, SWT.READ_ONLY);
        fCboEncodingQuality.setItems(new String[] { EncodingQuality.NORMAL.getDisplay(), EncodingQuality.EXTREME.getDisplay(), EncodingQuality.INSANE.getDisplay(), EncodingQuality.CUSTOM.getDisplay() });
        fCboEncodingQuality.setData(new EncodingQuality[] { EncodingQuality.NORMAL, EncodingQuality.EXTREME, EncodingQuality.INSANE, EncodingQuality.CUSTOM });
        fCboEncodingQuality.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                flexCustomQualityControls();
            }
        });
        fGrpCustomQuality = new Group(fContainer, SWT.NONE);
        fGrpCustomQuality.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        fGrpCustomQuality.setLayout(new GridLayout(4, false));
        fGrpCustomQuality.setText("Custom Quality Settings");
        fTargetBitRateContainer = new Composite(fGrpCustomQuality, SWT.NULL);
        fTargetBitRateContainer.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        fTargetBitRateContainer.setLayout(new GridLayout(2, false));
        fBtnTargetBitRate = new Button(fTargetBitRateContainer, SWT.RADIO);
        fBtnTargetBitRate.setText("Target bitrate");
        fBtnTargetBitRate.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (fBtnTargetBitRate.getSelection()) {
                    fBtnTargetQuality.setSelection(false);
                }
                flexCustomQualityControls();
            }
        });
        fTxtTargetBitRate = new Text(fTargetBitRateContainer, SWT.BORDER);
        fTxtTargetBitRate.setText("128 kbps");
        fTxtTargetBitRate.setEditable(false);
        fTargetQualityContainer = new Composite(fGrpCustomQuality, SWT.NULL);
        fTargetQualityContainer.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        fTargetQualityContainer.setLayout(new GridLayout(2, false));
        fBtnTargetQuality = new Button(fTargetQualityContainer, SWT.RADIO);
        fBtnTargetQuality.setText("VBR quality");
        fBtnTargetQuality.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (fBtnTargetQuality.getSelection()) {
                    fBtnTargetBitRate.setSelection(false);
                }
                flexCustomQualityControls();
            }
        });
        fTxtTargetQuality = new Text(fTargetQualityContainer, SWT.BORDER);
        fTxtTargetQuality.setLayoutData(new GridData(SWT.LEFT, SWT.DEFAULT, false, false, 1, 1));
        fTxtTargetQuality.setText("5");
        fTxtTargetQuality.setEditable(false);
        fSclTargetBitRate = new Scale(fTargetBitRateContainer, SWT.HORIZONTAL);
        fSclTargetBitRate.setIncrement(16);
        fSclTargetBitRate.setMinimum(64);
        fSclTargetBitRate.setMaximum(320);
        fSclTargetBitRate.setPageIncrement(64);
        fSclTargetBitRate.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        fSclTargetBitRate.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                int selection = fSclTargetBitRate.getSelection();
                if (selection % 2 == 1) {
                    ++selection;
                    fSclTargetBitRate.setSelection(selection);
                }
                fTxtTargetBitRate.setText(Integer.toString(selection) + " kbps");
                fTxtTargetBitRate.pack();
            }
        });
        fSclTargetQuality = new Scale(fTargetQualityContainer, SWT.HORIZONTAL);
        fSclTargetQuality.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        fSclTargetQuality.setIncrement(1);
        fSclTargetQuality.setMaximum(10);
        fSclTargetQuality.setMinimum(1);
        fSclTargetQuality.setPageIncrement(2);
        fSclTargetQuality.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                fTxtTargetQuality.setText(Integer.toString(fSclTargetQuality.getSelection()));
                fTxtTargetQuality.pack();
            }
        });
        fBtnCBR = new Button(fTargetBitRateContainer, SWT.CHECK);
        fBtnCBR.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 2, 1));
        fBtnCBR.setText("Use constant bitrate");
        fLblVBRMode = new Label(fTargetQualityContainer, SWT.NONE);
        fLblVBRMode.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 1, 1));
        fLblVBRMode.setText("Variable bitrate mode:");
        fCboVBRMode = new Combo(fTargetQualityContainer, SWT.READ_ONLY);
        fCboVBRMode.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 1, 1));
        fCboVBRMode.setItems(new String[] { VariableBitrateMode.STANDARD.getDisplay(), VariableBitrateMode.FAST.getDisplay() });
        fCboVBRMode.setData(new VariableBitrateMode[] { VariableBitrateMode.STANDARD, VariableBitrateMode.FAST });
        fBtnSetQualityManually = new Button(fGrpCustomQuality, SWT.CHECK);
        fBtnSetQualityManually.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 4, 1));
        fBtnSetQualityManually.setText("Set quality using custom parameters below");
        fBtnSetQualityManually.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                flexCustomQualityControls();
            }
        });
        Composite customParametersContainer = new Composite(fContainer, SWT.NULL);
        customParametersContainer.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
        customParametersContainer.setLayout(new GridLayout(2, false));
        Label lblCustomParameters = new Label(customParametersContainer, SWT.NONE);
        lblCustomParameters.setText("Custom Parameters:");
        fTxtCustomParameters = new Text(customParametersContainer, SWT.BORDER);
        fTxtCustomParameters.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 1, 1));
        Label lblEncodedUsingLAME = new Label(fContainer, SWT.NONE);
        lblEncodedUsingLAME.setText("Powered by L.A.M.E. encoding engine");
        GridData data = new GridData(SWT.RIGHT, SWT.DEFAULT, false, false);
        data.verticalIndent = 20;
        lblEncodedUsingLAME.setLayoutData(data);
        initControls();
        return fContainer;
    }

    private void flexCustomQualityControls() {
        if (fCboEncodingQuality.getSelectionIndex() == fCboEncodingQuality.getItemCount() - 1) {
            if (fBtnSetQualityManually.getSelection()) {
                setChildControlsEnabled(fGrpCustomQuality, false);
            } else {
                setChildControlsEnabled(fGrpCustomQuality, true);
                setChildControlsEnabled(fTargetBitRateContainer, fBtnTargetBitRate.getSelection());
                setChildControlsEnabled(fTargetQualityContainer, fBtnTargetQuality.getSelection());
                fTargetBitRateContainer.setEnabled(true);
                fTargetQualityContainer.setEnabled(true);
                fBtnTargetBitRate.setEnabled(true);
                fBtnTargetQuality.setEnabled(true);
            }
            fBtnSetQualityManually.setEnabled(true);
        } else {
            setChildControlsEnabled(fGrpCustomQuality, false);
        }
    }

    private String getLAMEParamters() {
        String lameParameters = "";
        EncodingQuality[] encodingQuality = (EncodingQuality[]) fCboEncodingQuality.getData();
        switch(encodingQuality[fCboEncodingQuality.getSelectionIndex()]) {
            case NORMAL:
                lameParameters = getPreferenceStore().getDefaultString(IPreferenceIds.LAME_ENCODING_PARAMTERS);
                break;
            case EXTREME:
                lameParameters = "--preset fast extreme";
                break;
            case INSANE:
                lameParameters = "--preset insane";
                break;
            case CUSTOM:
                if (!fBtnSetQualityManually.getSelection()) {
                    if (fBtnTargetBitRate.getSelection()) {
                        lameParameters = (fBtnCBR.getSelection() ? "-b " : "--preset ") + Integer.toString(fSclTargetBitRate.getSelection());
                    } else if (fBtnTargetQuality.getSelection()) {
                        lameParameters = "-V " + Integer.toString(fSclTargetQuality.getMaximum() - fSclTargetQuality.getSelection());
                        VariableBitrateMode[] vbrMode = (VariableBitrateMode[]) fCboVBRMode.getData();
                        switch(vbrMode[fCboVBRMode.getSelectionIndex()]) {
                            case FAST:
                                lameParameters = lameParameters + " -f";
                                break;
                        }
                    }
                }
                break;
        }
        if (fTxtCustomParameters.getText().length() > 0) {
            lameParameters = lameParameters + (lameParameters.equals("") ? "" : " ") + fTxtCustomParameters.getText();
        }
        lameParameters.trim();
        return lameParameters;
    }

    @Override
    protected String getPageID() {
        return ID;
    }

    @Override
    public void init(IWorkbench workbench) {
        setPreferenceStore(PlatformUI.getPreferenceStore());
    }

    private void initControls() {
        fFilePath = getPreferenceStore().getString(IPreferenceIds.EXTERNAL_PROGRAMS_LAME_PATH);
        if (fFilePath == null || fFilePath.equals("")) {
            fTxtLamePath.setText("<Select LAME Program>");
        } else {
            fTxtLamePath.setText(fFilePath);
        }
        fTxtCustomParameters.setText(getPreferenceStore().getString(IPreferenceIds.LAME_CUSTOM_PARAMETERS));
        fCboEncodingQuality.select(getPreferenceStore().getInt(IPreferenceIds.LAME_ENCODING_QUALITY));
        fBtnSetQualityManually.setSelection(getPreferenceStore().getBoolean(IPreferenceIds.LAME_SET_QUALITY_MANUALLY));
        fBtnTargetBitRate.setSelection(getPreferenceStore().getBoolean(IPreferenceIds.LAME_TARGET_BITRATE_IND));
        fSclTargetBitRate.setSelection(getPreferenceStore().getInt(IPreferenceIds.LAME_TARGET_BITRATE_VALUE));
        fTxtTargetBitRate.setText(Integer.toString(fSclTargetBitRate.getSelection()));
        fBtnTargetQuality.setSelection(getPreferenceStore().getBoolean(IPreferenceIds.LAME_TARGET_QUALITY_IND));
        fSclTargetQuality.setSelection(getPreferenceStore().getInt(IPreferenceIds.LAME_TARGET_QUALITY_VALUE));
        fTxtTargetQuality.setText(Integer.toString(fSclTargetQuality.getSelection()));
        fBtnCBR.setSelection(getPreferenceStore().getBoolean(IPreferenceIds.LAME_USE_CONSTANT_BITRATE));
        fCboVBRMode.select(getPreferenceStore().getInt(IPreferenceIds.LAME_VARIABLE_BITRATE_MODE));
        flexCustomQualityControls();
        fContainer.pack();
    }

    /**
	 * Performs special processing when this page's Restore Defaults button has
	 * been pressed.
	 */
    @Override
    protected void performDefaults() {
        if (MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Restore Defaults", "Are you sure you want to overwrite existing preferences?")) {
            getPreferenceStore().setToDefault(IPreferenceIds.EXTERNAL_PROGRAMS_LAME_PATH);
            getPreferenceStore().setToDefault(IPreferenceIds.LAME_CUSTOM_PARAMETERS);
            getPreferenceStore().setToDefault(IPreferenceIds.LAME_ENCODING_QUALITY);
            getPreferenceStore().setToDefault(IPreferenceIds.LAME_SET_QUALITY_MANUALLY);
            getPreferenceStore().setToDefault(IPreferenceIds.LAME_TARGET_BITRATE_IND);
            getPreferenceStore().setToDefault(IPreferenceIds.LAME_TARGET_BITRATE_VALUE);
            getPreferenceStore().setToDefault(IPreferenceIds.LAME_TARGET_QUALITY_IND);
            getPreferenceStore().setToDefault(IPreferenceIds.LAME_TARGET_QUALITY_VALUE);
            getPreferenceStore().setToDefault(IPreferenceIds.LAME_USE_CONSTANT_BITRATE);
            getPreferenceStore().setToDefault(IPreferenceIds.LAME_VARIABLE_BITRATE_MODE);
            initControls();
        }
    }

    @Override
    public boolean performOk() {
        savePrefence(IPreferenceIds.EXTERNAL_PROGRAMS_LAME_PATH, fFilePath);
        savePrefence(IPreferenceIds.LAME_CUSTOM_PARAMETERS, fTxtCustomParameters.getText());
        savePrefence(IPreferenceIds.LAME_ENCODING_QUALITY, fCboEncodingQuality.getSelectionIndex());
        savePrefence(IPreferenceIds.LAME_SET_QUALITY_MANUALLY, fBtnSetQualityManually.getSelection());
        savePrefence(IPreferenceIds.LAME_TARGET_BITRATE_IND, fBtnTargetBitRate.getSelection());
        savePrefence(IPreferenceIds.LAME_TARGET_BITRATE_VALUE, fSclTargetBitRate.getSelection());
        savePrefence(IPreferenceIds.LAME_TARGET_QUALITY_IND, fBtnTargetQuality.getSelection());
        savePrefence(IPreferenceIds.LAME_TARGET_QUALITY_VALUE, fSclTargetQuality.getSelection());
        savePrefence(IPreferenceIds.LAME_USE_CONSTANT_BITRATE, fBtnCBR.getSelection());
        savePrefence(IPreferenceIds.LAME_VARIABLE_BITRATE_MODE, fCboVBRMode.getSelectionIndex());
        savePrefence(IPreferenceIds.LAME_ENCODING_PARAMTERS, getLAMEParamters());
        return super.performOk();
    }

    private void setChildControlsEnabled(Composite composite, boolean enabled) {
        for (Control control : composite.getChildren()) {
            control.setEnabled(enabled);
            if (control instanceof Composite) {
                setChildControlsEnabled(((Composite) control), enabled);
            }
        }
    }
}
