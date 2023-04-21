package ee.webAppToolkit.rendering.freemarker.utils.expert.impl;

import com.google.common.base.Predicate;
import ee.webAppToolkit.forms.Display;
import ee.webAppToolkit.rendering.freemarker.utils.expert.FreemarkerPropertyMetadata;

public class DisplayOnly implements Predicate<FreemarkerPropertyMetadata> {

    @Override
    public boolean apply(FreemarkerPropertyMetadata propertyMetadata) {
        return propertyMetadata.getAnnotations().containsKey(Display.class.getSimpleName());
    }
}
