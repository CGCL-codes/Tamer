package com.volantis.mcs.policies.impl.variants.image;

import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.impl.variants.VariantValidator;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.content.BaseURLRelativeBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.image.GenericImageSelectionBuilder;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;

public class ImageVariantValidator implements VariantValidator {

    public void validate(ValidationContext context, SourceLocation sourceLocation, VariantBuilder variantBuilder) {
        ContentBuilder content = variantBuilder.getContentBuilder();
        if (content instanceof BaseURLRelativeBuilder) {
            BaseURLRelativeBuilder relative = (BaseURLRelativeBuilder) content;
            ImageMetaDataBuilder image = (ImageMetaDataBuilder) variantBuilder.getMetaDataBuilder();
            SelectionBuilder selection = variantBuilder.getSelectionBuilder();
            if (relative.getBaseLocation() == BaseLocation.DEVICE) {
                if (image.getConversionMode() == ImageConversionMode.ALWAYS_CONVERT) {
                    context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR, context.createMessage("convertible-image-not-on-device"));
                } else if (selection instanceof GenericImageSelectionBuilder) {
                    context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR, context.createMessage("generic-image-not-on-device"));
                }
            }
        }
    }
}
