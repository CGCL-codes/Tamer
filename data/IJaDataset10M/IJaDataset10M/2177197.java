package imtek.optsuite.psi.phaseshifter.iow.gui;

import imtek.optsuite.acquisition.mtools.MeasurementToolPreferences;
import imtek.optsuite.acquisition.mtools.MeasurementToolRef;
import imtek.optsuite.acquisition.mtools.composites.MeasurementToolRefDetailComposite;
import imtek.optsuite.psi.phaseshifter.iow.IOWPhaseShifterPrefs;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.nightlabs.base.composite.LabeledText;
import org.nightlabs.base.composite.XComposite;

/**
 * @author Alexander Bieber <fleque@users.sourceforge.net>
 * 
 */
public class PhaseShifterDetailComposite extends MeasurementToolRefDetailComposite {

    private ScrolledComposite scrolledComposite;

    private XComposite wrapper;

    private LabeledText transAtMaxVoltageText;

    private LabeledText converterResolutionText;

    private LabeledText waveLengthText;

    private LabeledText mountAngleText;

    private LabeledText nominalShiftText;

    private LabeledText modulationThresholdText;

    private ModifyListener modifyListener;

    /**
	 * Calls super constructor
	 */
    public PhaseShifterDetailComposite(Composite parent, int style, MeasurementToolRef toolRef) {
        super(parent, style, toolRef);
    }

    /**
	 * Calls super constructor
	 */
    public PhaseShifterDetailComposite(Composite parent, int style, MeasurementToolRef toolRef, boolean showButtonsOnChange) {
        super(parent, style, toolRef, showButtonsOnChange);
    }

    protected Composite createContents(Composite parent) {
        scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        modifyListener = new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                setChanged(true);
            }
        };
        wrapper = new XComposite(scrolledComposite, SWT.NONE, XComposite.LayoutMode.TIGHT_WRAPPER);
        scrolledComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        transAtMaxVoltageText = new LabeledText(wrapper, "Translation at maximal voltage (e.g. 3E-6D): ");
        transAtMaxVoltageText.getTextControl().addModifyListener(modifyListener);
        converterResolutionText = new LabeledText(wrapper, "DA-Converter resolution: ");
        converterResolutionText.getTextControl().addModifyListener(modifyListener);
        waveLengthText = new LabeledText(wrapper, "Measurement wavelength (in nm): ");
        waveLengthText.getTextControl().addModifyListener(modifyListener);
        mountAngleText = new LabeledText(wrapper, "Mount angle (in deg.): ");
        mountAngleText.getTextControl().addModifyListener(modifyListener);
        nominalShiftText = new LabeledText(wrapper, "Nominal phase shift in deg.: ");
        nominalShiftText.getTextControl().addModifyListener(modifyListener);
        modulationThresholdText = new LabeledText(wrapper, "Modulation threshold: ");
        modulationThresholdText.getTextControl().addModifyListener(modifyListener);
        scrolledComposite.setContent(wrapper);
        scrolledComposite.setMinSize(wrapper.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        wrapper.layout();
        return scrolledComposite;
    }

    public void showPreferences(MeasurementToolPreferences prefs) {
        if (!(prefs instanceof IOWPhaseShifterPrefs)) return;
        IOWPhaseShifterPrefs iowPrefs = (IOWPhaseShifterPrefs) prefs;
        transAtMaxVoltageText.getTextControl().setText("" + iowPrefs.getTransAtMaxVoltage());
        converterResolutionText.getTextControl().setText("" + iowPrefs.getConverterResolution());
        waveLengthText.getTextControl().setText("" + iowPrefs.getWaveLengthNM());
        mountAngleText.getTextControl().setText("" + iowPrefs.getMountAngleDegrees());
        nominalShiftText.getTextControl().setText("" + iowPrefs.getNominalShift());
        modulationThresholdText.getTextControl().setText("" + iowPrefs.getModulationThreshold());
        okCancelComposite.setVisible(false);
    }

    public void updatePreferences(MeasurementToolPreferences prefs) {
        IOWPhaseShifterPrefs iowPrefs = (IOWPhaseShifterPrefs) prefs;
        try {
            iowPrefs.setTransAtMaxVoltage(Double.parseDouble(transAtMaxVoltageText.getTextControl().getText()));
            iowPrefs.setConverterResolution(Integer.parseInt(converterResolutionText.getTextControl().getText()));
            iowPrefs.setWaveLengthNM(Double.parseDouble(waveLengthText.getTextControl().getText()));
            iowPrefs.setMountAngleDegrees(Double.parseDouble(mountAngleText.getTextControl().getText()));
            iowPrefs.setNominalShift(Double.parseDouble(nominalShiftText.getTextControl().getText()));
            iowPrefs.setModulationThreshold(Double.parseDouble(modulationThresholdText.getTextControl().getText()));
            iowPrefs.validate();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
	 * @return Returns the converterResolutionText.
	 */
    public LabeledText getConverterResolutionText() {
        return converterResolutionText;
    }

    /**
	 * @return Returns the modulationThresholdText.
	 */
    public LabeledText getModulationThresholdText() {
        return modulationThresholdText;
    }

    /**
	 * @return Returns the mountAngleText.
	 */
    public LabeledText getMountAngleText() {
        return mountAngleText;
    }

    /**
	 * @return Returns the nominalShiftText.
	 */
    public LabeledText getNominalShiftText() {
        return nominalShiftText;
    }

    /**
	 * @return Returns the transAtMaxVoltageText.
	 */
    public LabeledText getTransAtMaxVoltageText() {
        return transAtMaxVoltageText;
    }

    /**
	 * @return Returns the waveLengthText.
	 */
    public LabeledText getWaveLengthText() {
        return waveLengthText;
    }

    protected String getOKCancelText() {
        return null;
    }
}
