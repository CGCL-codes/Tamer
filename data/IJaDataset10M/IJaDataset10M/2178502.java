package us.wthr.jdem846.swtui;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Text;
import us.wthr.jdem846.DemConstants;
import us.wthr.jdem846.ModelOptions;
import us.wthr.jdem846.color.ColorInstance;
import us.wthr.jdem846.color.ColorRegistry;
import us.wthr.jdem846.color.ColoringInstance;
import us.wthr.jdem846.color.ColoringRegistry;
import us.wthr.jdem846.i18n.I18N;
import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;
import us.wthr.jdem846.render.EngineInstance;
import us.wthr.jdem846.render.EngineRegistry;
import us.wthr.jdem846.swtui.ValueMonitoredScale.MonitoredValueListener;
import us.wthr.jdem846.swtui.config.LightPositionConfigPanel;

public class ModelOptionsPanel extends Composite {

    private static Log log = Logging.getLog(ModelOptionsPanel.class);

    private ModelOptions modelOptions;

    private Combo engineCombo;

    private Combo backgroundColorCombo;

    private Combo coloringCombo;

    private Combo hillshadingCombo;

    private EngineModelObserver engineModel;

    private ColoringListModel coloringModel;

    private BackgroundColorOptionsListModel backgroundColorModel;

    private HillShadingOptionsListModel hillshadingModel;

    private Text widthText;

    private Text heightText;

    private Text tileSizeText;

    private ValueMonitoredScale lightMultipleScale;

    private ValueMonitoredScale spotExponentScale;

    private ValueMonitoredScale elevationMultipleScale;

    private LightPositionConfigPanel lightPositionConfig;

    private boolean ignoreValueChanges = false;

    private List<Listener> changeListeners = new LinkedList<Listener>();

    public ModelOptionsPanel(Composite parent, ModelOptions modelOptions) {
        super(parent, SWT.NONE);
        this.modelOptions = modelOptions;
        GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.verticalAlignment = SWT.FILL;
        setLayout(new GridLayout(2, false));
        Composite optionsComposite = new Composite(this, SWT.NONE);
        optionsComposite.setLayoutData(gridData);
        optionsComposite.setLayout(new GridLayout(2, false));
        Label label;
        GridData controlGridData = new GridData();
        controlGridData.horizontalAlignment = SWT.FILL;
        Listener genericSelectionListener = new Listener() {

            public void handleEvent(Event arg0) {
                fireChangeListeners();
            }
        };
        SelectionListener sliderSelectionListener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                fireChangeListeners();
            }
        };
        FocusListener focusLostListener = new FocusAdapter() {

            public void focusLost(FocusEvent arg0) {
                fireChangeListeners();
            }
        };
        label = new Label(optionsComposite, SWT.FLAT);
        label.setText(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.engineCombo.label") + ":");
        label.pack();
        engineCombo = new Combo(optionsComposite, SWT.READ_ONLY);
        engineModel = new EngineModelObserver(engineCombo);
        engineModel.addSelectionListener(genericSelectionListener);
        engineCombo.setLayoutData(controlGridData);
        engineCombo.pack();
        label = new Label(optionsComposite, SWT.FLAT);
        label.setText(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.widthText.label") + ":");
        label.pack();
        widthText = TextFactory.createNumberText(optionsComposite);
        widthText.addFocusListener(focusLostListener);
        widthText.setLayoutData(controlGridData);
        label = new Label(optionsComposite, SWT.FLAT);
        label.setText(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.heightText.label") + ":");
        label.pack();
        heightText = TextFactory.createNumberText(optionsComposite);
        heightText.addFocusListener(focusLostListener);
        heightText.setLayoutData(controlGridData);
        label = new Label(optionsComposite, SWT.FLAT);
        label.setText(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.backgroundColorCombo.label") + ":");
        label.pack();
        backgroundColorCombo = new Combo(optionsComposite, SWT.READ_ONLY);
        backgroundColorModel = new BackgroundColorOptionsListModel(backgroundColorCombo);
        backgroundColorModel.addSelectionListener(genericSelectionListener);
        backgroundColorCombo.setLayoutData(controlGridData);
        backgroundColorCombo.pack();
        label = new Label(optionsComposite, SWT.FLAT);
        label.setText(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.coloringCombo.label") + ":");
        label.pack();
        coloringCombo = new Combo(optionsComposite, SWT.READ_ONLY);
        coloringModel = new ColoringListModel(coloringCombo);
        coloringModel.addSelectionListener(genericSelectionListener);
        coloringCombo.setLayoutData(controlGridData);
        coloringCombo.pack();
        label = new Label(optionsComposite, SWT.FLAT);
        label.setText(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.hillshadingCombo.label") + ":");
        label.pack();
        hillshadingCombo = new Combo(optionsComposite, SWT.READ_ONLY);
        hillshadingModel = new HillShadingOptionsListModel(hillshadingCombo);
        hillshadingModel.addSelectionListener(genericSelectionListener);
        hillshadingCombo.setLayoutData(controlGridData);
        hillshadingCombo.pack();
        label = new Label(optionsComposite, SWT.FLAT);
        label.setText(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.tileSizeText.label") + ":");
        label.pack();
        tileSizeText = TextFactory.createNumberText(optionsComposite);
        tileSizeText.addFocusListener(focusLostListener);
        tileSizeText.setLayoutData(controlGridData);
        label = new Label(optionsComposite, SWT.FLAT);
        label.setText(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.lightMultipleSlider.label") + ":");
        label.pack();
        lightMultipleScale = new ValueMonitoredScale(optionsComposite, new MonitoredValueListener() {

            NumberFormat format = NumberFormat.getInstance();

            public String getValueString(int value) {
                format.setMaximumFractionDigits(3);
                format.setMinimumFractionDigits(3);
                double adjustedValue = (double) value / 100.0;
                return format.format(adjustedValue);
            }
        });
        lightMultipleScale.addSelectionListener(sliderSelectionListener);
        lightMultipleScale.setMinimum(0);
        lightMultipleScale.setMaximum(100);
        lightMultipleScale.setIncrement(1);
        lightMultipleScale.setSelection(50);
        label = new Label(optionsComposite, SWT.FLAT);
        label.setText(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.spotExponentSlider.label") + ":");
        label.pack();
        spotExponentScale = new ValueMonitoredScale(optionsComposite, new MonitoredValueListener() {

            NumberFormat format = NumberFormat.getIntegerInstance();

            public String getValueString(int value) {
                return format.format(value);
            }
        });
        spotExponentScale.addSelectionListener(sliderSelectionListener);
        spotExponentScale.setMinimum(ModelOptions.SPOT_EXPONENT_MINIMUM);
        spotExponentScale.setMaximum(ModelOptions.SPOT_EXPONENT_MAXIMUM);
        spotExponentScale.setIncrement(1);
        spotExponentScale.setPageIncrement(1);
        spotExponentScale.setSelection(5);
        label = new Label(optionsComposite, SWT.FLAT);
        label.setText(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.elevationMultipleSlider.label") + ":");
        label.pack();
        elevationMultipleScale = new ValueMonitoredScale(optionsComposite, new MonitoredValueListener() {

            NumberFormat format = NumberFormat.getIntegerInstance();

            public String getValueString(int value) {
                return format.format(value);
            }
        });
        elevationMultipleScale.addSelectionListener(sliderSelectionListener);
        elevationMultipleScale.setMinimum(0);
        elevationMultipleScale.setMaximum(100);
        elevationMultipleScale.setIncrement(1);
        elevationMultipleScale.setSelection(50);
        lightPositionConfig = new LightPositionConfigPanel(this);
        GridData lightPosGridData = new GridData();
        lightPosGridData.horizontalAlignment = SWT.FILL;
        lightPosGridData.verticalAlignment = SWT.FILL;
        lightPositionConfig.setLayoutData(lightPosGridData);
        applyOptionsToUI();
    }

    public void resetDefaultOptions() {
        setModelOptions(new ModelOptions());
    }

    protected void applyOptionsToUI() {
        ignoreValueChanges = true;
        widthText.setText("" + modelOptions.getWidth());
        heightText.setText("" + modelOptions.getHeight());
        tileSizeText.setText("" + modelOptions.getTileSize());
        engineModel.setSelectionByValue(modelOptions.getEngine());
        coloringModel.setSelectionByValue(modelOptions.getColoringType());
        backgroundColorModel.setSelectionByValue(modelOptions.getBackgroundColor());
        hillshadingModel.setSelectionByValue(modelOptions.getHillShadeType());
        lightMultipleScale.setSelection((int) Math.round(modelOptions.getLightingMultiple() * 100));
        spotExponentScale.setSelection(modelOptions.getSpotExponent());
        elevationMultipleScale.setSelection((int) Math.round(modelOptions.getElevationMultiple()));
        lightPositionConfig.setSolarAzimuth(modelOptions.getLightingAzimuth());
        lightPositionConfig.setSolarElevation(modelOptions.getLightingElevation());
        lightPositionConfig.updatePreview(false);
        ignoreValueChanges = false;
    }

    protected void applyOptionsToModel() {
        modelOptions.setWidth(Integer.parseInt(widthText.getText()));
        modelOptions.setHeight(Integer.parseInt(heightText.getText()));
        modelOptions.setTileSize(Integer.parseInt(tileSizeText.getText()));
        modelOptions.setEngine(engineModel.getSelectedValue());
        modelOptions.setColoringType(coloringModel.getSelectedValue());
        modelOptions.setBackgroundColor(backgroundColorModel.getSelectedValue());
        modelOptions.setHillShadeType(hillshadingModel.getSelectedValue());
        modelOptions.setLightingMultiple((double) lightMultipleScale.getSelection() / 100.0);
        modelOptions.setElevationMultiple((double) elevationMultipleScale.getSelection());
        modelOptions.setSpotExponent(spotExponentScale.getSelection());
    }

    public void setModelOptions(ModelOptions modelOptions) {
        this.modelOptions = modelOptions;
        applyOptionsToUI();
    }

    class HillShadingOptionsListModel extends ComboModelController<Integer> {

        public HillShadingOptionsListModel(Combo combo) {
            super(combo);
            addItem(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.hillShadeOptions.lighten"), DemConstants.HILLSHADING_LIGHTEN);
            addItem(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.hillShadeOptions.darken"), DemConstants.HILLSHADING_DARKEN);
            addItem(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.hillShadeOptions.combined"), DemConstants.HILLSHADING_COMBINED);
            addItem(I18N.get("us.wthr.jdem846.ui.modelOptionsPanel.hillShadeOptions.none"), DemConstants.HILLSHADING_NONE);
            setSelected(0);
        }
    }

    class BackgroundColorOptionsListModel extends ComboModelController<String> {

        public BackgroundColorOptionsListModel(Combo combo) {
            super(combo);
            for (ColorInstance colorInstance : ColorRegistry.getInstances()) {
                addItem(colorInstance.getName(), colorInstance.getIdentifier());
            }
            setSelected(0);
        }
    }

    class ColoringListModel extends ComboModelController<String> {

        public ColoringListModel(Combo combo) {
            super(combo);
            List<ColoringInstance> colorings = ColoringRegistry.getInstances();
            for (ColoringInstance colorInstance : colorings) {
                addItem(colorInstance.getName(), colorInstance.getIdentifier());
            }
            setSelected(0);
        }
    }

    class EngineModelObserver extends ComboModelController<String> {

        public EngineModelObserver(Combo combo) {
            super(combo);
            List<EngineInstance> engineInstances = EngineRegistry.getInstances();
            for (EngineInstance engineInstance : engineInstances) {
                addItem(engineInstance.getName(), engineInstance.getIdentifier());
            }
            setSelected(0);
        }
    }

    public void fireChangeListeners() {
        if (ignoreValueChanges) return;
        Event event = new Event();
        for (Listener listener : changeListeners) {
            listener.handleEvent(event);
        }
    }

    public void addChangeListener(Listener listener) {
        changeListeners.add(listener);
    }

    public boolean removeChangeListener(Listener listener) {
        return changeListeners.remove(listener);
    }
}
