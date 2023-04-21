package com.volantis.mcs.policies.impl.variants.metadata;

import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.variants.metadata.PixelDimensionsMetaDataBuilder;
import com.volantis.mcs.policies.variants.metadata.PixelDimensionsMetaData;

public abstract class AbstractMetaDataPixelsDimensionBuilder extends AbstractMetaDataSingleEncodingBuilder implements PixelDimensionsMetaDataBuilder {

    private int width;

    private int height;

    protected AbstractMetaDataPixelsDimensionBuilder(PixelDimensionsMetaData pixelsDimension) {
        if (pixelsDimension != null) {
            width = pixelsDimension.getWidth();
            height = pixelsDimension.getHeight();
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (!equals(this.width, width)) {
            stateChanged();
        }
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (!equals(this.height, height)) {
            stateChanged();
        }
        this.height = height;
    }

    protected final void validateSingleEncodingImpl(ValidationContext context) {
        validateDimension(context, width, PolicyModel.WIDTH);
        validateDimension(context, height, PolicyModel.HEIGHT);
        validatePixelsDimensionImpl(context);
    }

    protected abstract void validatePixelsDimensionImpl(ValidationContext context);

    /**
     * Make sure that the dimension is valid.
     *
     * @param context   The validation context.
     * @param dimension The dimension to check.
     * @param property  The dimension property for reporting errors.
     */
    private void validateDimension(ValidationContext context, int dimension, PropertyIdentifier property) {
        if (dimension <= 0) {
            Step step = context.pushPropertyStep(property);
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR, context.createMessage(PolicyMessages.MINIMUM_EXCLUSIVE, new Object[] { property.getDescription(), new Integer(0), new Integer(dimension) }));
            context.popStep(step);
        }
    }

    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof AbstractMetaDataPixelsDimensionBuilder) ? equalsSpecific((AbstractMetaDataPixelsDimensionBuilder) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(AbstractMetaDataPixelsDimensionBuilder other) {
        return super.equalsSpecific(other) && equals(width, other.width) && equals(height, other.height);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, width);
        result = hashCode(result, height);
        return result;
    }
}
