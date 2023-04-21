package org.nexopenframework.samples.simple.propertyeditors;

import java.beans.PropertyEditorSupport;
import org.nexopenframework.samples.simple.model.Country;
import org.springframework.util.NumberUtils;

/**
 * <p>simple application using NexOpen Framework</p>
 * 
 * <pCustom {@link PropertyEditorSupport} for delaing with {@link Country} entity</p>
 * 
 * @author <a href="mailto:mercefrancesc@yahoo.es">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class CountryPropertyEditor extends PropertyEditorSupport {

    /**
	 * <p>Editor for {@link Country} object model</p>
	 * 
	 * @throws IllegalArgumentException if some error appears during parsing
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
    @Override
    public void setAsText(final String value) {
        final Long id_obj = (Long) NumberUtils.parseNumber(value, Long.class);
        final Country country = new Country();
        country.setId(id_obj.longValue());
        super.setValue(country);
    }
}
