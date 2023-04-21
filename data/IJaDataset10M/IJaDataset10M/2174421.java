package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.mcs.dom2theme.AssetResolver;
import com.volantis.mcs.dom2theme.ExtractorContext;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.dom2theme.impl.normalizer.BackgroundNormalizer;
import com.volantis.mcs.dom2theme.impl.normalizer.BackgroundResolver;
import com.volantis.mcs.dom2theme.impl.normalizer.BorderNormalizer;
import com.volantis.mcs.dom2theme.impl.normalizer.ListStyleResolver;
import com.volantis.mcs.dom2theme.impl.normalizer.MarqueeNormalizer;
import com.volantis.mcs.dom2theme.impl.normalizer.PropertiesNormalizer;
import com.volantis.mcs.dom2theme.impl.normalizer.TextAlignNormalizer;
import com.volantis.mcs.dom2theme.impl.optimizer.border.BorderOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.font.FontOptimizer;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.ImmutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Optimise the properties.
 */
public class PropertiesOptimizer implements InputPropertiesOptimizer {

    /**
     * The object responsible for checking the status of individual properties.
     */
    private final PropertyClearerCheckerImpl propertyClearerChecker;

    /**
     * The set of objects that are responsible for resolving any policy
     * references within the properties to URLs.
     */
    private final PropertiesNormalizer[] resolvers;

    /**
     * The set of objects that are responsible for normalizing those properties
     * that are dependent upon one another.
     */
    private final PropertiesNormalizer[] normalizers;

    /**
     * The set of objects that are responsible for optimizing groups of
     * properties by making use of shorthands where possible.
     */
    private final ShorthandOptimizer[] optimizers;

    /**
     * The optimizer for all properties that are handled individually.
     */
    private final PropertyGroupOptimizer individualOptimizer;

    /**
     * The set of properties supported by the target device.
     *
     * <p>Any properties not in this set are ignored.</p>
     */
    private final ImmutableStylePropertySet supportedProperties;

    /**
     * The output values that have been created but not used yet.
     *
     * <p>If this is not already set this is initialised before optimizing the
     * properties. If afterwards if it is empty then it will be used again next
     * time, otherwise this field is cleared and the values returned.
     */
    private MutableStyleProperties savedOutputValues;

    /**
     * Initialise.
     *
     * @param detailsSet          Details for all the properties to be output.
     * @param context             The contextual information needed to optimize.
     * @param supportedShorthands The set of supported shorthands.
     */
    public PropertiesOptimizer(PropertyDetailsSet detailsSet, ExtractorContext context, ShorthandSet supportedShorthands) {
        propertyClearerChecker = new PropertyClearerCheckerImpl(detailsSet);
        AssetResolver assetResolver = context.getAssetResolver();
        supportedProperties = getSupportedProperties(detailsSet);
        resolvers = new PropertiesNormalizer[] { new BackgroundResolver(supportedProperties, assetResolver), new ListStyleResolver(supportedProperties, assetResolver) };
        normalizers = new PropertiesNormalizer[] { new BackgroundNormalizer(supportedProperties), new BorderNormalizer(supportedProperties), new TextAlignNormalizer(supportedProperties), new MarqueeNormalizer(supportedProperties) };
        optimizers = new ShorthandOptimizer[] { new HeterogeneousShorthandOptimizer(StyleShorthands.BACKGROUND, propertyClearerChecker, supportedShorthands), new BorderOptimizer(propertyClearerChecker, supportedShorthands), new EdgeShorthandOptimizer(StyleShorthands.MARGIN, propertyClearerChecker, supportedShorthands), new EdgeShorthandOptimizer(StyleShorthands.PADDING, propertyClearerChecker, supportedShorthands), new CornerShorthandOptimizer(StyleShorthands.MCS_BORDER_RADIUS, propertyClearerChecker, supportedShorthands), new HeterogeneousShorthandOptimizer(StyleShorthands.MARQUEE, propertyClearerChecker, supportedShorthands), new FontOptimizer(propertyClearerChecker, supportedShorthands) };
        MutableStylePropertySet individualProperties = supportedProperties.createMutableStylePropertySet();
        for (int i = 0; i < optimizers.length; i++) {
            ShorthandOptimizer optimizer = optimizers[i];
            optimizer.removeProperties(individualProperties);
        }
        StyleProperty[] orderedPropertyArray = detailsSet.getOrderedPropertyArray(individualProperties);
        individualOptimizer = new IndividualPropertyOptimizer(propertyClearerChecker, orderedPropertyArray);
    }

    /**
     * Get the set of properties supported by the target device.
     *
     * @param detailsSet The details for the target device.
     * @return The set of supported properties.
     */
    private ImmutableStylePropertySet getSupportedProperties(PropertyDetailsSet detailsSet) {
        ImmutableStylePropertySet supportedProperties = detailsSet.getSupportedProperties();
        MutableStylePropertySet propertySet = supportedProperties.createMutableStylePropertySet();
        propertySet.remove(StylePropertyDetails.CONTENT);
        supportedProperties = propertySet.createImmutableStylePropertySet();
        return supportedProperties;
    }

    public MutableStyleProperties calculateOutputProperties(String elementName, PseudoStylePath pseudoPath, final MutablePropertyValues inputValues, StyleValues parentValues, DeviceValues deviceValues) {
        if (elementName == null) {
            throw new IllegalArgumentException("elementName cannot be null");
        }
        if (pseudoPath == null) {
            throw new IllegalArgumentException("pseudoPath cannot be null");
        }
        if (inputValues == null) {
            throw new IllegalArgumentException("inputValues cannot be null");
        }
        if (parentValues == null) {
            throw new IllegalArgumentException("parentValues cannot be null");
        }
        TargetEntity target;
        if (pseudoPath.containsPseudoClass()) {
            target = TargetEntity.PSEUDO_CLASS;
        } else {
            target = TargetEntity.ELEMENT;
        }
        for (int i = 0; i < resolvers.length; i++) {
            PropertiesNormalizer resolver = resolvers[i];
            resolver.normalize(inputValues);
        }
        if (!pseudoPath.containsPseudoClass()) {
            for (int i = 0; i < normalizers.length; i++) {
                PropertiesNormalizer normalizer = normalizers[i];
                normalizer.normalize(inputValues);
            }
        }
        propertyClearerChecker.prepare(parentValues, target);
        if (savedOutputValues == null) {
            savedOutputValues = ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        }
        MutableStyleProperties outputValues = savedOutputValues;
        for (int i = 0; i < optimizers.length; i++) {
            ShorthandOptimizer optimizer = optimizers[i];
            optimizer.optimize(target, inputValues, outputValues, deviceValues);
        }
        individualOptimizer.optimize(target, inputValues, outputValues, deviceValues);
        if (outputValues.isEmpty()) {
            outputValues = null;
        } else {
            savedOutputValues = null;
        }
        return outputValues;
    }
}
