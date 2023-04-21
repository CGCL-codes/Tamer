package com.volantis.mcs.protocols.widgets.renderers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.styles.EffectRule;
import com.volantis.mcs.themes.StyleFraction;
import com.volantis.mcs.themes.StyleFrequency;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleTime;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.FrequencyUnit;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.mcs.themes.values.TimeUnit;
import com.volantis.mcs.themes.values.converters.FrequencyUnitConverter;
import com.volantis.mcs.themes.values.converters.TimeUnitConverter;
import com.volantis.styling.StatefulPseudoClass;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Extracts style values in the way they are almost ready to be rendered in the
 * JavaScript.
 * <p>
 * 
 * This class is a state machine. It holds the styles to retrieve values from,
 * and the pseudo-class indicating the state of an element.
 * <p>
 * 
 * Usage of this class can be described in to basic steps: <br>
 * 1. Set the styles and the pseudo-class <br>
 * 2. Extract style values
 */
public final class StylesExtractor {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(StylesExtractor.class);

    /**
     * Converter between values from different time units
     */
    private static final TimeUnitConverter timeConverter = TimeUnitConverter.getInstance();

    /**
     * Converter between values from different frequency units.
     */
    private static final FrequencyUnitConverter frequencyConverter = FrequencyUnitConverter.getInstance();

    /**
     * Widget element styles to retrieve values from.
     */
    private Styles styles;

    /**
     * Pseudo-class to retrieve properties from.
     */
    private StatefulPseudoClass pseudoClass;

    /**
     * The target protocol.
     */
    private VolantisProtocol protocol;

    private static final StyleValueFactory STYLE_VALUE_FACTORY = StyleValueFactory.getDefaultInstance();

    /**
     * default value of mcs-effect-style style property.
     */
    private static final StyleKeyword DEFAULT_MCS_EFFECT_STYLE = (StyleKeyword) StylePropertyDetails.MCS_EFFECT_STYLE.getStandardDetails().getInitialValue();

    /**
     * default value of mcs-effect-duration style property.
     */
    private static final StyleTime DEFAULT_MCS_EFFECT_DURATION = (StyleTime) StylePropertyDetails.MCS_EFFECT_DURATION.getStandardDetails().getInitialValue();

    /**
     * default value of mcs-frame-rate style property.
     */
    private static final StyleFrequency DEFAULT_MCS_FRAME_RATE = (StyleFrequency) StylePropertyDetails.MCS_FRAME_RATE.getStandardDetails().getInitialValue();

    /**
     * default list of mcs-effect-style values.
     */
    private static final StyleList MCS_EFFECT_STYLE_DEFAULT_VALUE;

    static {
        List list = new ArrayList();
        list.add(DEFAULT_MCS_EFFECT_STYLE);
        MCS_EFFECT_STYLE_DEFAULT_VALUE = STYLE_VALUE_FACTORY.getList(list);
    }

    /**
     * default list of mcs-frame-rate values. 
     */
    private static final StyleList MCS_FRAME_RATE_DEFAULT_VALUE;

    static {
        List list = new ArrayList();
        list.add(DEFAULT_MCS_FRAME_RATE);
        MCS_FRAME_RATE_DEFAULT_VALUE = STYLE_VALUE_FACTORY.getList(list);
    }

    /**
     * default list of mcs-effect-duration values. 
     */
    private static final StyleList MCS_EFFECT_DURATION_DEFAULT_VALUE;

    static {
        List list = new ArrayList();
        list.add(DEFAULT_MCS_EFFECT_DURATION);
        MCS_EFFECT_DURATION_DEFAULT_VALUE = STYLE_VALUE_FACTORY.getList(list);
    }

    /**
     * Creates new instance of this class.
     */
    protected StylesExtractor() {
        this(null);
    }

    /**
     * Creates new instance of this class, initialised with given styles.
     * 
     * @param styles styles to extract values from
     */
    protected StylesExtractor(Styles styles) {
        this(styles, null);
    }

    /**
     * Creates new instance of this class, initialised with given styles and
     * pseudo-class.
     * 
     * @param styles styles to extract values from
     * @param pseudoClass pseudo-class to retrieve values from
     */
    public StylesExtractor(Styles styles, StatefulPseudoClass pseudoClass) {
        this.styles = styles;
        this.pseudoClass = pseudoClass;
    }

    /**
     * Creates new instance of this class, initialised with given styles and
     * concelaed state. If <code>concealed</code> flag is set, it will
     * initialise pseudo-class to 'mcs-concealed'. If the flag is not set, it
     * will reset pseudo-class.
     * 
     * @param styles styles to extract values from
     * @param concealed normal or concealed state
     */
    protected StylesExtractor(Styles styles, boolean concealed) {
        this(styles, concealed ? StatefulPseudoClasses.MCS_CONCEALED : null);
    }

    /**
     * @return the styles to retrieve values from
     */
    public Styles getStyles() {
        return styles;
    }

    /**
     * @param styles the styles to set.
     */
    public void setStyles(Styles styles) {
        this.styles = styles;
    }

    /**
     * @return the pseudo-class to retrieve properties from, or null if
     *         properties are retrieved from base style.
     */
    public StatefulPseudoClass getPseudoClass() {
        return pseudoClass;
    }

    /**
     * @param pseudoClass The pseudo-class to set.
     */
    public void setPseudoClass(StatefulPseudoClass pseudoClass) {
        this.pseudoClass = pseudoClass;
    }

    /**
     * @return Returns the protocol.
     */
    protected VolantisProtocol getProtocol() {
        return protocol;
    }

    /**
     * @param protocol The protocol to set.
     */
    protected void setProtocol(VolantisProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * Returns string including values of all valid CSS specified style
     * properties. All non-standard CSS styles will not be included.
     * 
     * The string will be formatted in the way to be passed directly to the
     * Element.setStyles() JavaScript function defined in scriptaculous library.
     */
    public String getJavaScriptStyles() {
        StringBuffer buffer = new StringBuffer("{");
        Styles actualStyles = getActualStyles();
        if (actualStyles != null) {
            PropertyValues values = actualStyles.getPropertyValues();
            StylePropertyDefinitions definitions = values.getStylePropertyDefinitions();
            boolean firstProperty = true;
            for (int index = 0; index < definitions.count(); index++) {
                StyleProperty property = definitions.getStyleProperty(index);
                String propertyName = property.getName();
                if (propertyName.startsWith("mcs-")) continue;
                StyleValue value = values.getSpecifiedValue(property);
                if (value != null) {
                    if (firstProperty) {
                        firstProperty = false;
                    } else {
                        buffer.append(", ");
                    }
                    buffer.append("'");
                    buffer.append(property.getName());
                    buffer.append("': '");
                    buffer.append(value.getStandardCSS());
                    buffer.append("'");
                }
            }
        }
        buffer.append('}');
        return buffer.toString();
    }

    /**
     * Returns computed value for given style property, or null if not found.
     * <p>
     * Style value will be retrieved using following rules:
     * <li> If styles are null, this will always return null.
     * <li> If styles are set and pseudo-class is null, this will return
     * computed style value.
     * <li> If styles are set and pseudo-class is set, this will return value
     * specified for the pseudo-class, and in case it's missing, the computed
     * style value.
     * 
     * @param property style property to retrieve value for
     * @return retrieved style value
     */
    private StyleValue getStyleValue(StyleProperty property) {
        StyleValue value = null;
        if (styles != null) {
            if (pseudoClass != null) {
                Styles nestedStyles = styles.findNestedStyles(pseudoClass);
                if (nestedStyles != null) {
                    value = nestedStyles.getPropertyValues().getComputedValue(property);
                }
            }
            if (value == null) {
                value = styles.getPropertyValues().getComputedValue(property);
            }
        }
        return value;
    }

    public List getEffectRules() {
        List effectsDescList = getEffectStyleList();
        List durationList = getEffectDurationList();
        List frameList = getFrameRateList();
        int listSize = effectsDescList.size() < durationList.size() ? effectsDescList.size() : durationList.size();
        listSize = listSize < frameList.size() ? listSize : frameList.size();
        if (!((effectsDescList.size() == durationList.size()) && (effectsDescList.size() == frameList.size()))) {
            logger.warn("repeater-parameters-list-not-equals", new Object[] { new Integer(effectsDescList.size()), new Integer(durationList.size()), new Integer(frameList.size()) });
        }
        durationList = fillListWith(durationList, effectsDescList.size(), DEFAULT_MCS_EFFECT_DURATION);
        frameList = fillListWith(frameList, effectsDescList.size(), DEFAULT_MCS_FRAME_RATE);
        List resultList = new ArrayList();
        for (int i = 0; i < effectsDescList.size(); i++) {
            StyleValue effectValue = (StyleValue) effectsDescList.get(i);
            StyleValue durationValue = (StyleValue) durationList.get(i);
            StyleValue fpsValue = (StyleValue) frameList.get(i);
            String effectStyle = getEffectStyle(effectValue);
            double duration = getEffectDuration(durationValue);
            double fps = getFrameRate(fpsValue);
            EffectRule rule = EffectRule.parse(effectStyle);
            rule.setDuration(duration);
            rule.setFps(fps);
            resultList.add(rule);
        }
        return resultList;
    }

    /**
     * Returns effect list. If mcs-effect-style has only single value it returns one element set.
     * @return
     */
    public Set getEffectsList() {
        List effectsDescList = getEffectStyleList();
        Set effects = new HashSet();
        for (int i = 0; i < effectsDescList.size(); i++) {
            StyleValue effectValue = (StyleValue) effectsDescList.get(i);
            String effectStyle = getEffectStyle(effectValue);
            if (effectStyle.indexOf(" ") != -1) {
                effectStyle = effectStyle.substring(0, effectStyle.indexOf(" "));
            }
            effects.add(effectStyle);
        }
        return effects;
    }

    /**
     * Returns effect duration in seconds retrieved from the styles. If not
     * found, <code>1.0</code> is returned by default. If there is more then 
     * one duration specified, returns the first one. 
     * 
     * @return effect duration in seconds
     * 
     * @see #getEffectDurationList
     */
    public double getEffectDuration() {
        List durationList = getEffectDurationList();
        return getEffectDuration((StyleValue) durationList.get(0));
    }

    /**
     * Returns frame rate in hertz retrieved from the styles. If not found,
     * <code>10.0</code> is returned by default.  If there is more then 
     * one frame rate specified, returns the first one. 
     *  
     * @return frame rate in hertz
     * 
     * @see #getFrameRateList
     */
    public double getFrameRate() {
        List frameRateList = getFrameRateList();
        return getFrameRate((StyleValue) frameRateList.get(0));
    }

    /**
     * 
     */
    public String getEffectStyle() {
        List effectStyleList = getEffectStyleList();
        return getEffectStyle((StyleValue) effectStyleList.get(0));
    }

    /**
     * Returns lag time in seconds retrieved from the styles mcs-delay. If not
     * found, <code>1.0</code> is returned by default.
     * 
     * @return delay in seconds
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public double getDelay() {
        return getTime(StylePropertyDetails.MCS_DELAY, TimeUnit.S, 1.0);
    }

    /**
     * Returns transition interval in seconds retrieved from the styles. If not
     * found, <code>1.0</code> is returned by default.
     * 
     * @return transition interval in seconds
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public double getTransitionInterval() {
        return getTime(StylePropertyDetails.MCS_TRANSITION_INTERVAL, TimeUnit.S, 1.0);
    }

    /**
     * Returns duration in seconds retrieved from the styles, 
     * or <code>Double.NaN</code> if not specified.
     * 
     * @return duration in seconds
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public double getDuration() {
        double duration = Double.NaN;
        StyleValue styleValue = getStyleValue(StylePropertyDetails.MCS_DURATION);
        if (styleValue != null) {
            if (styleValue instanceof StyleTime) {
                StyleTime styleTime = (StyleTime) styleValue;
                duration = styleTime.getNumber();
            }
        }
        return duration;
    }

    public String getDateTimeFormat() {
        String format = null;
        StyleValue styleValue = getStyleValue(StylePropertyDetails.MCS_DATETIME_FORMAT);
        if (styleValue instanceof StyleKeyword) {
            StyleKeyword styleKeyword = (StyleKeyword) styleValue;
            if ("normal".equals(styleKeyword.getName())) {
                format = new String("%D %d %m %Y %h:%i:%s");
            }
        } else if (styleValue != null) {
            if (styleValue instanceof StyleString) {
                StyleString styleString = (StyleString) styleValue;
                format = styleString.getString();
            }
        }
        return format;
    }

    /**
     * Returns items order retrieved from the styles.
     * 
     * @return marquee style name
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public String getItemsOrder() {
        return getKeyword(StylePropertyDetails.MCS_ITEMS_ORDER, "normal");
    }

    /**
     * Returns JavaScript marquee style name retrieved from the styles. If not
     * found, <code>"none"</code> is returned by default.
     * 
     * @return marquee style name
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public String getMarqueeStyle() {
        return getKeyword(StylePropertyDetails.MCS_MARQUEE_STYLE, "none");
    }

    /**
     * Returns JavaScript focus style name retrieved from the styles. If not
     * found, <code>"accept"</code> is returned by default.
     * 
     * @return marquee style name
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public String getFocusStyle() {
        return getKeyword(StylePropertyDetails.MCS_FOCUS, "accept");
    }

    /**
     * Returns JavaScript marquee direction name retrieved from the styles. If
     * not found, <code>"left"</code> is returned by default.
     * 
     * @return marquee direction
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public String getMarqueeDirection() {
        return getKeyword(StylePropertyDetails.MCS_MARQUEE_DIRECTION, "left");
    }

    /**
     * Returns number of marquee effect repetitions. Value Integer.MAX_VALUE
     * means infinite number of repetitions. If not found, <code>1</code> is
     * returned by default.
     * 
     * @return number of marquee effect repetitions
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public int getMarqueeRepetitions() {
        int repetitions = 1;
        StyleValue styleValue = getStyleValue(StylePropertyDetails.MCS_MARQUEE_REPETITION);
        if (styleValue != null) {
            if (styleValue instanceof StyleKeyword) {
                StyleKeyword styleKeyword = (StyleKeyword) styleValue;
                if ("infinite".equals(styleKeyword.getName())) {
                    repetitions = Integer.MAX_VALUE;
                }
            } else if (styleValue instanceof StyleInteger) {
                StyleInteger styleInteger = (StyleInteger) styleValue;
                repetitions = styleInteger.getInteger();
            }
        }
        return repetitions;
    }

    /**
     * 
     * @param defaultWidth value returned if width is not specified or it is
     *        not in px units.
     *      see comment to MapDefaultRenderer.MAX_DISPLAY_SIZE
     * @return min(value of width property, defaultWidth) 
     */
    public int getWidthIfSpecifiedInPixels(int defaultWidth) {
        int width = defaultWidth;
        StyleValue styleValue = getStyleValue(StylePropertyDetails.WIDTH);
        if (styleValue != null) {
            if (styleValue instanceof StyleLength) {
                StyleLength styleInteger = (StyleLength) styleValue;
                if (styleInteger.getUnit() == LengthUnit.PX) {
                    width = (int) styleInteger.getNumber();
                }
            }
        }
        return width;
    }

    /**
     * Returns number of repetitions. Value Integer.MAX_VALUE
     * means infinite number of repetitions.
     * 
     * @return number of repetitions
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public int getRepetitions() {
        int repetitions = 1;
        StyleValue styleValue = getStyleValue(StylePropertyDetails.MCS_REPETITIONS);
        if (styleValue != null) {
            if (styleValue instanceof StyleKeyword) {
                StyleKeyword styleKeyword = (StyleKeyword) styleValue;
                if ("infinite".equals(styleKeyword.getName())) {
                    repetitions = Integer.MAX_VALUE;
                }
            } else if (styleValue instanceof StyleInteger) {
                StyleInteger styleInteger = (StyleInteger) styleValue;
                repetitions = styleInteger.getInteger();
            }
        }
        return repetitions;
    }

    /**
     * Returns number of items displayed in widget e.g. autocomplete. Value Integer.MAX_VALUE
     * means infinite number of itemLimit. If not found, <code>1</code> is
     * returned by default.
     * 
     * @return number of marquee effect repetitions
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public int getItemLimit() {
        int limits = 5;
        StyleValue styleValue = getStyleValue(StylePropertyDetails.MCS_ITEM_LIMIT);
        if (styleValue != null) {
            if (styleValue instanceof StyleKeyword) {
                StyleKeyword styleKeyword = (StyleKeyword) styleValue;
                if ("infinite".equals(styleKeyword.getName())) {
                    limits = Integer.MAX_VALUE;
                }
            } else if (styleValue instanceof StyleInteger) {
                StyleInteger styleInteger = (StyleInteger) styleValue;
                limits = styleInteger.getInteger();
            }
        }
        return limits;
    }

    /**
     * Returns delay after which the slideshow widget is launched in seconds.
     * Value Double.POSITIVE_INFINITY means it's never launched.
     * 
     * @return delay after which slideshow is launched.
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public double getSlideshowLaunchDelay() {
        double delay = Double.POSITIVE_INFINITY;
        StyleValue styleValue = getStyleValue(StylePropertyDetails.MCS_SLIDESHOW_LAUNCH_DELAY);
        if (styleValue != null) {
            if (styleValue instanceof StyleTime) {
                StyleTime styleTime = (StyleTime) styleValue;
                delay = styleTime.getNumber();
            }
        }
        return delay;
    }

    /**
     * Returns marquee speed retrieved from the styles in characters per second.
     * If not found, <code>6.0</code> is returned by default.
     * 
     * @return marquee speed in characters per second [em/s]
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public double getMarqueeSpeed() {
        double speed = 6.0;
        StyleValue styleValue = getStyleValue(StylePropertyDetails.MCS_MARQUEE_SPEED);
        if (styleValue instanceof StyleLength) {
            StyleLength styleLength = (StyleLength) styleValue;
            if (styleLength.getUnit() == LengthUnit.EM) {
                speed = styleLength.getNumber();
            }
        } else if (styleValue instanceof StyleFraction) {
            StyleFraction styleFraction = (StyleFraction) styleValue;
            StyleValue styleNumerator = styleFraction.getNumerator();
            StyleValue styleDenominator = styleFraction.getDenominator();
            if ((styleNumerator instanceof StyleLength) && (styleDenominator instanceof StyleTime)) {
                StyleLength styleLength = (StyleLength) styleNumerator;
                StyleTime styleTime = (StyleTime) styleDenominator;
                double timeInSeconds = timeConverter.convert(styleTime.getNumber(), styleTime.getUnit(), TimeUnit.S);
                if (!Double.isNaN(timeInSeconds)) {
                    if (styleLength.getUnit() == LengthUnit.EM) {
                        speed = styleLength.getNumber() / timeInSeconds;
                    }
                }
            }
        }
        return speed;
    }

    /**
     * Returns input format. If not found, null is returned by default.
     * 
     * @return input format
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public String getInputFormat() {
        return getString(StylePropertyDetails.MCS_INPUT_FORMAT, null);
    }

    /**
     * Returns true, if the styles contains 'message' as one of the possible
     * validation error actions.
     * 
     * @return true, if the styles contains 'message' as one of the possible
     *         validation error actions.
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public boolean containsMessageValidationErrorAction() {
        return isOrIncludesKeyword(StylePropertyDetails.MCS_VALIDATION_ERROR_ACTION, StyleKeywords.MESSAGE);
    }

    /**
     * Returns true, if the styles contains 'focus' as one of the possible
     * validation error actions.
     * 
     * @return true, if the styles contains 'focus' as one of the possible
     *         validation error actions.
     * 
     * @see #getStyleValue - explains the way style values are retrieved
     */
    public boolean containsFocusValidationErrorAction() {
        return isOrIncludesKeyword(StylePropertyDetails.MCS_VALIDATION_ERROR_ACTION, StyleKeywords.FOCUS);
    }

    private String getEffectStyle(StyleValue effectValue) {
        String effectStyle = null;
        if (effectValue instanceof StyleString) {
            effectStyle = ((StyleString) effectValue).getString();
        } else if (effectValue instanceof StyleKeyword) {
            effectStyle = ((StyleKeyword) effectValue).getName();
        } else {
            logger.warn("unexpected-mcs-effect-style-type", effectValue.getStyleValueType().getType());
            effectStyle = DEFAULT_MCS_EFFECT_STYLE.getName();
        }
        return effectStyle;
    }

    private double getEffectDuration(StyleValue styleValue) {
        StyleTime styleTime = null;
        if (styleValue instanceof StyleTime) {
            styleTime = (StyleTime) styleValue;
        } else {
            logger.warn("unexpected-mcs-effect-duration-type", styleValue.getStyleValueType().getType());
            styleTime = DEFAULT_MCS_EFFECT_DURATION;
        }
        return timeConverter.convert(styleTime.getNumber(), styleTime.getUnit(), TimeUnit.S);
    }

    private double getFrameRate(StyleValue styleValue) {
        StyleFrequency styleFrequency = null;
        if (styleValue instanceof StyleFrequency) {
            styleFrequency = (StyleFrequency) styleValue;
        } else {
            logger.warn("unexpected-mcs-frame-rate-type", styleValue.getStyleValueType().getType());
            styleFrequency = DEFAULT_MCS_FRAME_RATE;
        }
        double frameRate = frequencyConverter.convert(styleFrequency.getNumber(), styleFrequency.getUnit(), FrequencyUnit.HZ);
        if (protocol != null) {
            ProtocolConfiguration protocolConfiguration = protocol.getProtocolConfiguration();
            if (protocolConfiguration != null) {
                frameRate = Math.min(frameRate, protocolConfiguration.getMaxClientFrameRate());
            }
        }
        return frameRate;
    }

    /**
     * Extract effects description list from list containing data about effect name and time, 
     * effect duration and effect frame rate.  
     * 
     * @return list of collected effect descriptions
     */
    private List getEffectStyleList() {
        StyleValue styleValue = getStyleValue(StylePropertyDetails.MCS_EFFECT_STYLE);
        List effectStyleList = null;
        if (styleValue instanceof StyleList) {
            effectStyleList = ((StyleList) styleValue).getList();
        } else if (styleValue instanceof StyleKeyword) {
            effectStyleList = new ArrayList();
            effectStyleList.add(styleValue);
        }
        if (null == effectStyleList || effectStyleList.isEmpty()) {
            effectStyleList = MCS_EFFECT_STYLE_DEFAULT_VALUE.getList();
        }
        return effectStyleList;
    }

    private List getEffectDurationList() {
        StyleValue styleValue = getStyleValue(StylePropertyDetails.MCS_EFFECT_DURATION);
        List durationList = null;
        if (styleValue instanceof StyleList) {
            durationList = ((StyleList) styleValue).getList();
        } else if (styleValue instanceof StyleTime) {
            durationList = new ArrayList();
            durationList.add(styleValue);
        }
        if (null == durationList || durationList.isEmpty()) {
            durationList = MCS_EFFECT_DURATION_DEFAULT_VALUE.getList();
        }
        return durationList;
    }

    private List getFrameRateList() {
        StyleValue styleValue = getStyleValue(StylePropertyDetails.MCS_FRAME_RATE);
        List frameRateList = null;
        if (styleValue instanceof StyleList) {
            frameRateList = ((StyleList) styleValue).getList();
        } else if (styleValue instanceof StyleFrequency) {
            frameRateList = new ArrayList();
            frameRateList.add(styleValue);
        }
        if (null == frameRateList || frameRateList.isEmpty()) {
            frameRateList = MCS_FRAME_RATE_DEFAULT_VALUE.getList();
        }
        return frameRateList;
    }

    private Styles getActualStyles() {
        Styles actualStyles = null;
        if (styles != null) {
            if (pseudoClass == null) {
                actualStyles = styles;
            } else {
                actualStyles = styles.findNestedStyles(pseudoClass);
            }
        }
        return actualStyles;
    }

    private double getTime(StyleProperty property, TimeUnit unit, double defaultTime) {
        double value = defaultTime;
        StyleValue styleValue = getStyleValue(property);
        if ((styleValue != null) && (styleValue instanceof StyleTime)) {
            StyleTime styleTime = (StyleTime) styleValue;
            value = timeConverter.convert(styleTime.getNumber(), styleTime.getUnit(), unit);
        }
        return value;
    }

    private String getKeyword(StyleProperty property, String defaultKeyword) {
        String keyword = defaultKeyword;
        StyleValue styleValue = getStyleValue(property);
        if ((styleValue != null) && (styleValue instanceof StyleKeyword)) {
            keyword = ((StyleKeyword) styleValue).getName();
        }
        return keyword;
    }

    private boolean isOrIncludesKeyword(StyleProperty property, StyleKeyword keyword) {
        boolean result = false;
        StyleValue styleValue = getStyleValue(property);
        if (styleValue != null) {
            if (styleValue instanceof StyleKeyword) {
                StyleKeyword styleKeyword = (StyleKeyword) styleValue;
                result = (styleKeyword == keyword);
            } else if (styleValue instanceof StyleList) {
                StyleList styleList = (StyleList) styleValue;
                result = styleList.getList().contains(keyword);
            }
        }
        return result;
    }

    private String getString(StyleProperty property, String defaultString) {
        String string = defaultString;
        StyleValue styleValue = getStyleValue(property);
        if (styleValue != null) {
            if (styleValue instanceof StyleString) {
                StyleString styleString = (StyleString) styleValue;
                string = styleString.getString();
            }
        }
        return string;
    }

    /**
     * Fill list with default values up to provided size. 
     * 
     * @param list
     * @param refSize
     * @param defaultValue
     * @return
     */
    private List fillListWith(List list, int refSize, StyleValue defaultValue) {
        List defaultList = new ArrayList();
        defaultList.addAll(list);
        for (int i = 0; i < refSize; i++) {
            defaultList.add(defaultValue);
        }
        return defaultList;
    }
}
