package net.zarubsys.unianalyzer.dataminers.lpt.gui.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.zarubsys.unianalyzer.dataminers.lpt.gui.parameterObject.StartStopData;
import net.zarubsys.unianalyzer.dataminers.lpt.pin.CustomizedPin;
import net.zarubsys.unianalyzer.dataminers.lpt.pin.PinHelper;
import net.zarubsys.unianalyzer.dataminers.lpt.wizard.utils.LPTConsts;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class LPTMeasuringStartNStop extends AbstractPageWithContext {

    private final UseTriggerListener useTriggerlistener = new UseTriggerListener();

    private final RisingFallingSelectionListener risingFallingListener = new RisingFallingSelectionListener();

    private final StopRadioListener stopRadioListener = new StopRadioListener();

    private final PinModifyListener pinModifyListener = new PinModifyListener();

    private Spinner maxWaitingIntSpinner;

    private Combo maxWaitingIntCombo;

    private Button automatic;

    private Button falling;

    private Button rising;

    private Button timeStop;

    private Combo measureTimeUnits;

    private Button samplesStop;

    private Combo pinCombo;

    private Composite startTriggerCompostite;

    private Text measureTime;

    private Text sampleCountText;

    private List<Control> timeStopDetail = new ArrayList<Control>(2);

    private List<Control> sampleStopDetail = new ArrayList<Control>(2);

    private Set<Control> controlsToEnableDisable = null;

    private boolean initSequence = false;

    public LPTMeasuringStartNStop(String pageName) {
        super(pageName);
    }

    public void createControl(Composite parent) {
        GridData gd;
        Label label;
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout gl = new GridLayout(1, false);
        gl.verticalSpacing = 12;
        container.setLayout(gl);
        Group startGroup = new Group(container, SWT.NONE);
        startGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        startGroup.setLayout(new GridLayout(1, false));
        startGroup.setText(Messages.getString("LPTMeasuringStartNStop.startGroup"));
        Button manual = new Button(startGroup, SWT.RADIO);
        manual.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        manual.setText(Messages.getString("LPTMeasuringStartNStop.startManually"));
        manual.setSelection(true);
        manual.addSelectionListener(useTriggerlistener);
        automatic = new Button(startGroup, SWT.RADIO);
        automatic.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        automatic.setText(Messages.getString("LPTMeasuringStartNStop.useTrigger"));
        automatic.addSelectionListener(useTriggerlistener);
        startTriggerCompostite = new Composite(startGroup, SWT.BORDER);
        startTriggerCompostite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        startTriggerCompostite.setLayout(new GridLayout(2, false));
        Composite leftTrigger = new Composite(startTriggerCompostite, SWT.NONE);
        leftTrigger.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        leftTrigger.setLayout(new GridLayout(2, false));
        label = new Label(leftTrigger, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText(Messages.getString("LPTMeasuringStartNStop.pinSelection"));
        pinCombo = new Combo(leftTrigger, SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        gd.widthHint = 140;
        pinCombo.setLayoutData(gd);
        pinCombo.addModifyListener(pinModifyListener);
        label = new Label(leftTrigger, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText(Messages.getString("LPTMeasuringStartNStop.waitingInt"));
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
                getMeasurignSettings().getStartStopData().setMaxWaitingInterval(interval);
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
                getMeasurignSettings().getStartStopData().setMaxWaitingIntervalUnits(units);
                validate();
            }
        });
        Composite rightTrigger = new Composite(startTriggerCompostite, SWT.NONE);
        rightTrigger.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        rightTrigger.setLayout(new GridLayout(2, false));
        label = new Label(rightTrigger, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));
        label.setText(Messages.getString("LPTMeasuringStartNStop.triggerType"));
        Composite edgeComposite = new Composite(rightTrigger, SWT.NONE);
        edgeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        edgeComposite.setLayout(new GridLayout(1, false));
        rising = new Button(edgeComposite, SWT.RADIO);
        rising.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));
        rising.setText(Messages.getString("LPTMeasuringStartNStop.risingEdge"));
        rising.addSelectionListener(risingFallingListener);
        falling = new Button(edgeComposite, SWT.RADIO);
        falling.setLayoutData(new GridData(SWT.BEGINNING, SWT.TOP, false, false));
        falling.setText(Messages.getString("LPTMeasuringStartNStop.fallingEdge"));
        falling.addSelectionListener(risingFallingListener);
        Group stopGroup = new Group(container, SWT.NONE);
        stopGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        stopGroup.setLayout(new GridLayout(1, false));
        stopGroup.setText(Messages.getString("LPTMeasuringStartNStop.stop"));
        Composite composite = new Composite(stopGroup, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        composite.setLayout(new GridLayout(1, false));
        timeStop = new Button(composite, SWT.RADIO);
        timeStop.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        timeStop.addSelectionListener(stopRadioListener);
        timeStop.setText(Messages.getString("LPTMeasuringStartNStop.time"));
        samplesStop = new Button(composite, SWT.RADIO);
        samplesStop.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        samplesStop.addSelectionListener(stopRadioListener);
        samplesStop.setText(Messages.getString("LPTMeasuringStartNStop.sampleCount"));
        Composite composite2 = new Composite(composite, SWT.BORDER);
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
                    getMeasurignSettings().getStartStopData().setMeasureTime(Integer.parseInt(text));
                } catch (NumberFormatException error) {
                    getMeasurignSettings().getStartStopData().setMeasureTime(-1);
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
                getMeasurignSettings().getStartStopData().setMeasureTimeUnits(units);
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
                getMeasurignSettings().getStartStopData().setSampleCount(sampleCount);
                validate();
            }
        });
        label = new Label(composite2, SWT.NONE);
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        label.setText(Messages.getString("LPTMeasuringStartNStop.samples"));
        sampleStopDetail.add((Control) label);
        setDefaultValues();
        setControl(container);
    }

    private void validate() {
        StartStopData ssd = getMeasurignSettings().getStartStopData();
        if (ssd.isUseStartTrigger()) {
            int waitingInterval = ssd.getMaxWaitingInterval() * ssd.getMaxWaitingIntervalUnits();
            if (waitingInterval < 100 || waitingInterval > 60000000) {
                setUserOutput(Messages.getString("LPTMeasuringStartNStop.errorMaxWaitingInt"));
                return;
            }
        }
        if (ssd.isStopByTime()) {
            int time = ssd.getMeasureTime() * ssd.getMeasureTimeUnits();
            if (time < 100 || time > 60000000) {
                setUserOutput(Messages.getString("LPTMeasuringStartNStop.errorMeasuringTime"));
                return;
            }
        } else {
            int sampleCount = ssd.getSamplesCount();
            if (sampleCount < 100 || sampleCount > 100000000) {
                setUserOutput(Messages.getString("LPTMeasuringStartNStop.errorSampleCount"));
                return;
            }
        }
        setUserOutput(null);
    }

    private void updatePins(List<CustomizedPin> pins) {
        PinHelper pinHelper = PinHelper.getInstance();
        List<CustomizedPin> filteredPins = pinHelper.getCheckedPins(pins);
        pinCombo.removeModifyListener(pinModifyListener);
        pinCombo.setItems(pinHelper.convertCustomizedPinsToStrings(filteredPins));
        pinCombo.addModifyListener(pinModifyListener);
        pinCombo.select(0);
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

    private void setDefaultValues() {
        initSequence = true;
        StartStopData ssd = getMeasurignSettings().getStartStopData();
        maxWaitingIntSpinner.setMaximum(999);
        maxWaitingIntSpinner.setMinimum(1);
        maxWaitingIntSpinner.setSelection(ssd.getMaxWaitingInterval());
        maxWaitingIntCombo.select(LPTConsts.findValueIndex(LPTConsts.getTimeUnitsValues(), ssd.getMaxWaitingIntervalUnits()));
        rising.setSelection(ssd.isRisingEdge());
        boolean stopByTime = ssd.isStopByTime();
        timeStop.setSelection(stopByTime);
        measureTime.setText(String.valueOf(ssd.getMeasureTime()));
        measureTimeUnits.select(LPTConsts.findValueIndex(LPTConsts.getTimeUnitsValues(), ssd.getMeasureTimeUnits()));
        sampleCountText.setText(String.valueOf(ssd.getSamplesCount()));
        PinHelper pinHelper = PinHelper.getInstance();
        List<CustomizedPin> filteredPins = pinHelper.getCheckedPins(getMeasurignSettings().getCustomizedPins());
        pinCombo.select(filteredPins.indexOf(ssd.getStartTriggerPin()));
        stopDetailEnable(stopByTime);
        setStartTriggerEnabled(ssd.isUseStartTrigger());
        initSequence = false;
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

    /**
	 * Enables and disables time stop and sample stop details.
	 * @enable if true, time stop is enabled and sample stop is disabled and otherwise.
	 */
    private void stopDetailEnable(boolean enable) {
        for (Control control : timeStopDetail) {
            control.setEnabled(enable);
        }
        for (Control control : sampleStopDetail) {
            control.setEnabled(!enable);
        }
    }

    private class UseTriggerListener implements SelectionListener {

        public void widgetDefaultSelected(SelectionEvent e) {
        }

        public void widgetSelected(SelectionEvent e) {
            if (initSequence) return;
            boolean enable = (e.widget == automatic && ((Button) e.widget).getSelection() == true);
            getMeasurignSettings().getStartStopData().setUseStartTrigger(enable);
            setStartTriggerEnabled(enable);
            validate();
        }
    }

    private final class StopRadioListener implements SelectionListener {

        public void widgetDefaultSelected(SelectionEvent e) {
        }

        public void widgetSelected(SelectionEvent e) {
            if (initSequence) return;
            boolean enable = (e.widget == timeStop && ((Button) e.widget).getSelection() == true);
            getMeasurignSettings().getStartStopData().setStopByTime(enable);
            stopDetailEnable(enable);
            validate();
        }
    }

    private final class RisingFallingSelectionListener implements SelectionListener {

        public void widgetDefaultSelected(SelectionEvent e) {
        }

        public void widgetSelected(SelectionEvent e) {
            if (initSequence) return;
            boolean isRisignSelected = (e.widget == rising && ((Button) e.widget).getSelection() == true);
            getMeasurignSettings().getStartStopData().setRisingEdge(isRisignSelected);
        }
    }

    private class PinModifyListener implements ModifyListener {

        public void modifyText(ModifyEvent e) {
            if (initSequence) return;
            int index = ((Combo) e.widget).getSelectionIndex();
            if (index < 0) return;
            PinHelper pinHelper = PinHelper.getInstance();
            List<CustomizedPin> filteredPins = pinHelper.getCheckedPins(getMeasurignSettings().getCustomizedPins());
            getMeasurignSettings().getStartStopData().setStartTriggerPin(filteredPins.get(index));
        }
    }

    public void setVisible(boolean visible) {
        if (visible) {
            updatePins(getMeasurignSettings().getCustomizedPins());
            validate();
        }
        super.setVisible(visible);
    }
}
