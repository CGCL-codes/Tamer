package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import com.volantis.mcs.eclipse.builder.editors.common.EditorPropertyParser;
import com.volantis.mcs.eclipse.common.Convertors;
import com.volantis.mcs.eclipse.common.NamedColor;
import com.volantis.mcs.eclipse.common.ResourceUnits;
import com.volantis.mcs.eclipse.controls.ColorButton;
import com.volantis.mcs.eclipse.controls.UnitsCombo;
import com.volantis.mcs.themes.CustomStyleValue;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleAngle;
import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorPercentages;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleFraction;
import com.volantis.mcs.themes.StyleFrequency;
import com.volantis.mcs.themes.StyleFunctionCall;
import com.volantis.mcs.themes.StyleIdentifier;
import com.volantis.mcs.themes.StyleInherit;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleInvalid;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StyleNumber;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleTime;
import com.volantis.mcs.themes.StyleURI;
import com.volantis.mcs.themes.StyleUserAgentDependent;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueVisitor;
import com.volantis.mcs.themes.StyleTranscodableURI;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.properties.AllowableKeywordsAccessor;
import com.volantis.mcs.themes.types.StyleAngleType;
import com.volantis.mcs.themes.types.StyleChoiceType;
import com.volantis.mcs.themes.types.StyleColorType;
import com.volantis.mcs.themes.types.StyleComponentURIType;
import com.volantis.mcs.themes.types.StyleFractionType;
import com.volantis.mcs.themes.types.StyleFrequencyType;
import com.volantis.mcs.themes.types.StyleFunctionCallType;
import com.volantis.mcs.themes.types.StyleIdentifierType;
import com.volantis.mcs.themes.types.StyleInheritType;
import com.volantis.mcs.themes.types.StyleIntegerType;
import com.volantis.mcs.themes.types.StyleKeywordsType;
import com.volantis.mcs.themes.types.StyleLengthType;
import com.volantis.mcs.themes.types.StyleListType;
import com.volantis.mcs.themes.types.StyleNumberType;
import com.volantis.mcs.themes.types.StyleOrderedSetType;
import com.volantis.mcs.themes.types.StylePairType;
import com.volantis.mcs.themes.types.StylePercentageType;
import com.volantis.mcs.themes.types.StyleStringType;
import com.volantis.mcs.themes.types.StyleTimeType;
import com.volantis.mcs.themes.types.StyleType;
import com.volantis.mcs.themes.types.StyleTypeVisitor;
import com.volantis.mcs.themes.types.StyleURIType;
import com.volantis.mcs.themes.types.StyleTranscodableURIType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.styling.properties.StyleProperty;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Editor for single style values.
 */
public class StyleValueEditor extends Composite {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER = LocalizationFactory.createExceptionLocalizer(StyleValueEditor.class);

    private static final String RESOURCE_PREFIX = "StyleValueEditor.";

    /**
     * The string for the inherit entry in the editable combo. This does not
     * come from a resource bundle as it should never be localised as it is
     * a W3C keyword
     */
    private static final String INHERIT_KEYWORD = "inherit";

    private int controlCount;

    private boolean colorButtonRequired;

    private boolean browseButtonRequired;

    private boolean unitsComboRequired;

    private boolean importantRequired;

    private ColorButton colorButton;

    private Button browseButton;

    private UnitsCombo unitsCombo;

    private Combo editableCombo;

    private Button importantCheckbox;

    private StylePropertyBrowseAction browseAction;

    private StyleProperty property;

    /**
     * This may be different to {@link property#getName} in some cases - e.g.
     * when used in a pair editor.
     */
    private String propertyName;

    private List valueTypes;

    private EditorContext context;

    private static final AllowableKeywordsAccessor ALLOWABLE_KEYWORDS_ACCESSOR = new AllowableKeywordsAccessor();

    private ModifyListener editableComboListener;

    private ListenerList listeners = new ListenerList();

    private boolean transmittingEvents = true;

    private SelectionListener changeSelectionListener = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent event) {
            valueChanged();
        }
    };

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param composite         parent composite
     * @param i                 style - see superclass explanation
     * @param property          which is being edited by this editor
     * @param propertyType      type which this editor can contain
     * @param acceptsImportant  indicates whether this property supports css
     *                          important
     * @param browseAction      if non null, defines what the browse button of
     *                          this editor should do.
     * @param context           in which this editor should operate
     */
    public StyleValueEditor(Composite composite, int i, StyleProperty property, StyleType propertyType, boolean acceptsImportant, StylePropertyBrowseAction browseAction, EditorContext context) {
        this(composite, i, property, property.getName(), propertyType, acceptsImportant, browseAction, context);
    }

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param composite         parent composite
     * @param i                 style - see superclass explanation
     * @param property          which is being edited by this editor
     * @param propertyName      in most cases this will be property#getName,
     *                          however for editors which are part of a
     *                          {@link StylePairEditor} it may be
     *                          property#getName +".first" (or ".second")
     * @param propertyType      type which this editor can contain
     * @param acceptsImportant  indicates whether this property supports css
     *                          important
     * @param browseAction      if non null, defines what the browse button of
     *                          this editor should do.
     * @param context           in which this editor should operate
     */
    public StyleValueEditor(Composite composite, int i, StyleProperty property, String propertyName, StyleType propertyType, boolean acceptsImportant, StylePropertyBrowseAction browseAction, EditorContext context) {
        super(composite, i);
        this.context = context;
        controlCount = 1;
        this.importantRequired = acceptsImportant;
        this.property = property;
        this.propertyName = propertyName;
        if (browseAction != null) {
            browseButtonRequired = true;
            this.browseAction = browseAction;
        }
        valueTypes = new ArrayList();
        propertyType.accept(new StyleTypeVisitor() {

            public void visitStyleInheritType(StyleInheritType type) {
                valueTypes.add(StyleValueType.INHERIT);
            }

            public void visitStyleLengthType(StyleLengthType type) {
                valueTypes.add(StyleValueType.LENGTH);
                unitsComboRequired = true;
            }

            public void visitStyleURIType(StyleURIType type) {
                valueTypes.add(StyleValueType.URI);
            }

            public void visitStyleKeywordsType(StyleKeywordsType type) {
                valueTypes.add(StyleValueType.KEYWORD);
            }

            public void visitStylePercentageType(StylePercentageType type) {
                valueTypes.add(StyleValueType.PERCENTAGE);
                unitsComboRequired = true;
            }

            public void visitStyleTimeType(StyleTimeType type) {
                valueTypes.add(StyleValueType.TIME);
                unitsComboRequired = true;
            }

            public void visitStyleAngleType(StyleAngleType type) {
                valueTypes.add(StyleValueType.ANGLE);
                unitsComboRequired = true;
            }

            public void visitStylePairType(StylePairType type) {
                valueTypes.add(StyleValueType.PAIR);
            }

            public void visitStyleColorType(StyleColorType type) {
                valueTypes.add(StyleValueType.COLOR);
                colorButtonRequired = true;
            }

            public void visitStyleFunctionCallType(StyleFunctionCallType type) {
                valueTypes.add(StyleValueType.FUNCTION_CALL);
            }

            public void visitStyleListType(StyleListType type) {
                valueTypes.add(StyleValueType.LIST);
            }

            public void visitStyleIdentifierType(StyleIdentifierType type) {
                valueTypes.add(StyleValueType.IDENTIFIER);
            }

            public void visitStyleNumberType(StyleNumberType type) {
                valueTypes.add(StyleValueType.NUMBER);
            }

            public void visitStyleIntegerType(StyleIntegerType type) {
                valueTypes.add(StyleValueType.INTEGER);
            }

            public void visitStyleComponentURIType(StyleComponentURIType type) {
                valueTypes.add(StyleValueType.COMPONENT_URI);
            }

            public void visitStyleTranscodableURIType(final StyleTranscodableURIType type) {
                valueTypes.add(StyleValueType.TRANSCODABLE_URI);
            }

            public void visitStyleChoiceType(StyleChoiceType type) {
                List choices = type.getTypes();
                Iterator it = choices.iterator();
                while (it.hasNext()) {
                    StyleType optionType = (StyleType) it.next();
                    optionType.accept(this);
                }
            }

            public void visitStyleOrderedSetType(StyleOrderedSetType type) {
                valueTypes.add(StyleValueType.LIST);
            }

            public void visitStyleStringType(StyleStringType type) {
                valueTypes.add(StyleValueType.STRING);
            }

            public void visitStyleFractionType(StyleFractionType type) {
                type.getNumeratorType().accept(this);
                type.getDenominatorType().accept(this);
            }

            public void visitStyleFrequencyType(StyleFrequencyType type) {
                valueTypes.add(StyleValueType.FREQUENCY);
                unitsComboRequired = true;
            }
        });
        if (browseButtonRequired) {
            controlCount += 1;
        }
        if (colorButtonRequired) {
            controlCount += 1;
        }
        if (unitsComboRequired) {
            controlCount += 1;
        }
        if (importantRequired) {
            controlCount += 1;
        }
        createComponents();
    }

    public void addModifyListener(ModifyListener listener) {
        listeners.add(listener);
    }

    public void removeModifyListener(ModifyListener listener) {
        listeners.remove(listener);
    }

    private void createComponents() {
        GridLayout layout = new GridLayout(controlCount, false);
        this.setLayout(layout);
        if (colorButtonRequired) {
            colorButton = new ColorButton(this, SWT.PUSH);
            ModifyListener colorButtonListener = new ModifyListener() {

                public void modifyText(ModifyEvent event) {
                    handleColorButtonSelection();
                }
            };
            colorButton.addModifyListener(colorButtonListener);
        }
        addEditableCombo();
        if (unitsComboRequired) {
            addUnitsCombo();
        }
        if (browseButtonRequired) {
            browseButton = new Button(this, SWT.PUSH);
            browseButton.setText(EditorMessages.getString(RESOURCE_PREFIX + "browse.label"));
            browseButton.addSelectionListener(new SelectionListener() {

                public void widgetSelected(SelectionEvent event) {
                    doBrowseAction();
                }

                public void widgetDefaultSelected(SelectionEvent event) {
                    doBrowseAction();
                }
            });
        }
        if (importantRequired) {
            importantCheckbox = new Button(this, SWT.CHECK);
            importantCheckbox.setText(EditorMessages.getString(RESOURCE_PREFIX + "important.label"));
            importantCheckbox.addSelectionListener(changeSelectionListener);
            importantCheckbox.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        }
        pack();
        if (colorButtonRequired) {
            int comboHeight = editableCombo.getBounds().height;
            colorButton.setSize(comboHeight, comboHeight);
            colorButton.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
            pack();
        }
    }

    private void doBrowseAction() {
        if (browseAction != null) {
            String initialValue = getValue();
            String browsed = browseAction.doBrowse(initialValue, this, context);
            if (browsed != null && !browsed.equals(initialValue)) {
                editableCombo.setText(browsed);
            }
        }
    }

    /**
     * Method that will be invoked whenever the ColorButton control is clicked
     */
    private void handleColorButtonSelection() {
        RGB rgb = colorButton.getColor();
        editableCombo.setText(Convertors.RGBToHex(rgb));
        if (unitsCombo != null) {
            unitsCombo.setEnabled(false);
        }
        valueChanged();
    }

    private AllowableKeywords getAllowableKeywords() {
        AllowableKeywords allowableKeywords = null;
        if (propertyName != null) {
            allowableKeywords = ALLOWABLE_KEYWORDS_ACCESSOR.getAllowableKeywords(propertyName);
        }
        return allowableKeywords;
    }

    /**
     * Adds an editable combo to this Composite
     */
    private void addEditableCombo() {
        editableCombo = new Combo(this, SWT.NONE);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        editableCombo.setLayoutData(data);
        if (valueTypes.contains(StyleValueType.INHERIT)) {
            editableCombo.add(INHERIT_KEYWORD, 0);
        }
        if (valueTypes.contains(StyleValueType.KEYWORD)) {
            AllowableKeywords allowableKeywords = getAllowableKeywords();
            if (allowableKeywords != null) {
                List keywords = allowableKeywords.getKeywords();
                for (int i = 0; i < keywords.size(); i++) {
                    StyleKeyword keyword = (StyleKeyword) keywords.get(i);
                    if (!keyword.isInternal()) {
                        editableCombo.add(keyword.getName());
                    }
                }
            }
        }
        if (colorButtonRequired) {
            NamedColor colors[] = NamedColor.getAllColors();
            for (int i = 0; i < colors.length; i++) {
                editableCombo.add(colors[i].getName());
            }
        }
        editableComboListener = new ModifyListener() {

            public void modifyText(ModifyEvent event) {
                handleEditableComboTextInput();
            }
        };
        editableCombo.addModifyListener(editableComboListener);
    }

    /**
     * Method that will be invoked whenever the editable combo receives input
     */
    private void handleEditableComboTextInput() {
        String text = editableCombo.getText();
        if (colorButton != null) {
            String hexRGBCandidate = NamedColor.getHex(text);
            if (hexRGBCandidate == null) {
                hexRGBCandidate = text;
            }
            RGB rgb = null;
            try {
                rgb = (hexRGBCandidate == null) ? null : Convertors.hexToRGB(hexRGBCandidate);
            } catch (NumberFormatException nfe) {
            }
            RGB oldRGB = colorButton.getColor();
            if (rgb == null ? oldRGB != null : !rgb.equals(oldRGB)) {
                colorButton.setColor(rgb);
            }
        }
        if (unitsCombo != null) {
            boolean enableUnitsCombo = true;
            try {
                Double.parseDouble(text);
            } catch (NumberFormatException e) {
                enableUnitsCombo = false;
            }
            unitsCombo.removeSelectionListener(changeSelectionListener);
            unitsCombo.setEnabled(enableUnitsCombo);
            unitsCombo.addSelectionListener(changeSelectionListener);
        }
        if (importantCheckbox != null) {
            boolean enabled = true;
            if (text.equals("")) {
                enabled = false;
            }
            importantCheckbox.removeSelectionListener(changeSelectionListener);
            importantCheckbox.setEnabled(enabled);
            importantCheckbox.addSelectionListener(changeSelectionListener);
        }
        valueChanged();
    }

    /**
     * Adds a UnitsCombo to this composite.
     */
    private void addUnitsCombo() {
        List units = new ArrayList();
        if (valueTypes.contains(StyleValueType.ANGLE)) {
            units.add(ResourceUnits.DEGREE);
            units.add(ResourceUnits.GRAD);
            units.add(ResourceUnits.RADIAN);
        }
        if (valueTypes.contains(StyleValueType.LENGTH)) {
            units.add(ResourceUnits.PIXEL);
            units.add(ResourceUnits.EM);
            units.add(ResourceUnits.EX);
            units.add(ResourceUnits.PICA);
            units.add(ResourceUnits.POINT);
            units.add(ResourceUnits.INCH);
            units.add(ResourceUnits.CENTIMETRE);
            units.add(ResourceUnits.MILLIMETRE);
        }
        if (valueTypes.contains(StyleValueType.PERCENTAGE)) {
            units.add(ResourceUnits.PERCENT);
        }
        if (valueTypes.contains(StyleValueType.TIME)) {
            units.add(ResourceUnits.MILLISECOND);
            units.add(ResourceUnits.SECOND);
        }
        if (valueTypes.contains(StyleValueType.FREQUENCY)) {
            units.add(ResourceUnits.HERTZ);
            units.add(ResourceUnits.KILOHERTZ);
        }
        boolean requireBlankUnitsEntry = (valueTypes.contains(StyleValueType.INTEGER) || valueTypes.contains(StyleValueType.NUMBER));
        unitsCombo = new UnitsCombo(this, units, requireBlankUnitsEntry);
        unitsCombo.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        unitsCombo.addSelectionListener(changeSelectionListener);
    }

    /**
     * Returns a string representation of the style value that the controls
     * currently represent
     */
    public String getValue() {
        boolean unitsEnabled = unitsCombo != null && unitsCombo.isEnabled();
        final String text = editableCombo.getText();
        if (text != null && !text.equals("") && unitsEnabled && unitsCombo.getSelectedUnit() != null) {
            return text + unitsCombo.getSelectedUnit();
        } else {
            return text;
        }
    }

    public boolean isImportant() {
        return importantCheckbox != null && importantCheckbox.getSelection();
    }

    private String styleValueToString(StyleValue value) {
        return value.getStandardCSS();
    }

    public void setPropertyValue(PropertyValue newPropertyValue) {
        StyleValue newValue;
        boolean important;
        if (newPropertyValue == null) {
            newValue = null;
            important = false;
        } else {
            newValue = newPropertyValue.getValue();
            important = newPropertyValue.getPriority() == Priority.IMPORTANT;
        }
        updateEditor(newValue, important);
    }

    void updateEditor(StyleValue newValue, boolean important) {
        try {
            transmittingEvents = false;
            if (newValue == null) {
                editableCombo.setText("");
                if (colorButton != null) {
                    colorButton.setColor(null);
                }
                if (importantCheckbox != null) {
                    importantCheckbox.setSelection(false);
                }
                if (unitsCombo != null) {
                    unitsCombo.setSelectedUnit(null);
                }
            } else {
                newValue.visit(new EditorUpdater(), null);
                if (importantCheckbox != null) {
                    importantCheckbox.setSelection(important);
                }
            }
        } finally {
            transmittingEvents = true;
        }
    }

    private void valueChanged() {
        if (transmittingEvents) {
            Event event = new Event();
            event.widget = this;
            String value = getValue();
            event.data = value;
            event.text = value;
            ModifyEvent modifyEvent = new ModifyEvent(event);
            Object interested[] = listeners.getListeners();
            for (int i = 0; i < interested.length; i++) if (interested[i] != null) {
                ((ModifyListener) interested[i]).modifyText(modifyEvent);
            }
        }
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);
        if (editableCombo != null) {
            editableCombo.setEnabled(b);
        }
        if (importantCheckbox != null) {
            importantCheckbox.setEnabled(b);
        }
        if (unitsCombo != null) {
            if (!b) {
                unitsCombo.setEnabled(b);
            } else if (editableCombo != null) {
                String text = editableCombo.getText();
                if (!"".equals(text)) {
                    EditorPropertyParser parser = new EditorPropertyParser();
                    PropertyValue propertyValue = parser.parsePropertyValue(property, text, isImportant());
                    StyleValue value = propertyValue.getValue();
                    if (!(value instanceof StyleKeyword || value instanceof StyleInherit)) {
                        unitsCombo.setEnabled(b);
                    }
                }
            }
        }
        if (browseButton != null) {
            browseButton.setEnabled(b);
        }
        if (colorButton != null) {
            colorButton.setEnabled(b);
        }
    }

    private class EditorUpdater implements StyleValueVisitor {

        public void visit(StyleAngle value, Object object) {
            editableCombo.setText(String.valueOf(value.getNumber()));
            unitsCombo.setSelectedUnit(value.getUnit().toString());
        }

        public void visit(StyleColorName value, Object object) {
            editableCombo.setText(value.getName());
        }

        public void visit(StyleColorPercentages value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StyleColorRGB value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StyleComponentURI value, Object object) {
            editableCombo.setText(value.getExpressionAsString());
        }

        public void visit(StyleTranscodableURI value, Object object) {
            editableCombo.setText(value.getUri());
        }

        public void visit(StyleFrequency value, Object object) {
            editableCombo.setText(String.valueOf(value.getNumber()));
            unitsCombo.setSelectedUnit(value.getUnit().toString());
        }

        public void visit(StyleFunctionCall value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StyleIdentifier value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StyleInherit value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StyleInteger value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StyleInvalid value, Object object) {
            editableCombo.setText(value.getValue());
        }

        public void visit(StyleKeyword value, Object object) {
            String identifier = value.getName();
            if (identifier != null) {
                editableCombo.setText(identifier);
            }
        }

        public void visit(StyleLength value, Object object) {
            editableCombo.setText(String.valueOf(value.getNumber()));
            unitsCombo.setSelectedUnit(value.getUnit().toString());
        }

        public void visit(StyleList value, Object object) {
            editableCombo.setText(value.getStandardCSS(StylePropertyMetadata.getListSeperator(propertyName)));
        }

        public void visit(StyleNumber value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StylePair value, Object object) {
            editableCombo.setText(styleValueToString(value));
        }

        public void visit(StylePercentage value, Object object) {
            editableCombo.setText(String.valueOf(value.getPercentage()));
            unitsCombo.setSelectedUnit(ResourceUnits.PERCENT.getUnit());
        }

        public void visit(StyleString value, Object object) {
            editableCombo.setText(value.getString());
        }

        public void visit(StyleURI value, Object object) {
            editableCombo.setText(value.getURI());
        }

        public void visit(StyleUserAgentDependent value, Object object) {
            throw new UnsupportedOperationException(EXCEPTION_LOCALIZER.format("style-value-not-updateable", new String[] { "StyleUserAgentDependent", "User agent dependent values not " + "supported by GUI" }));
        }

        public void visit(StyleTime value, Object object) {
            editableCombo.setText(String.valueOf(value.getNumber()));
            unitsCombo.setSelectedUnit(value.getUnit().toString());
        }

        public void visit(StyleFraction value, Object object) {
            throw new UnsupportedOperationException(EXCEPTION_LOCALIZER.format("style-value-not-updateable", new Object[] { "StyleFractions", "Fractions should be updated using " + "the TwoStyleEditor, which will update the fraction's " + "components individually" }));
        }

        public void visit(CustomStyleValue value, Object object) {
            throw new UnsupportedOperationException();
        }
    }
}
