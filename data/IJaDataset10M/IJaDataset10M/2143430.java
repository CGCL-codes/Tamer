package com.googlecode.pondskum.gui.swing.notifyer;

import com.googlecode.pinthura.annotation.SuppressionReason;

@SuppressWarnings({ "MethodReturnOfConcreteClass" })
@SuppressionReason(SuppressionReason.Reason.BUILDER_PATTERN)
public final class FontedTextDimensionBuilder {

    private float width;

    private float height;

    private float ascent;

    private float descent;

    public FontedTextDimensionBuilder withWidth(final float width) {
        this.width = width;
        return this;
    }

    public FontedTextDimensionBuilder withHeight(final float height) {
        this.height = height;
        return this;
    }

    public FontedTextDimensionBuilder withAscent(final float ascent) {
        this.ascent = ascent;
        return this;
    }

    public FontedTextDimensionBuilder withDescent(final float descent) {
        this.descent = descent;
        return this;
    }

    public FontedTextDimension build() {
        FontedTextDimension fontedTextDimension = new FontedTextDimension();
        fontedTextDimension.setHeight(height);
        fontedTextDimension.setWidth(width);
        fontedTextDimension.setAscent(ascent);
        fontedTextDimension.setDescent(descent);
        return fontedTextDimension;
    }
}
