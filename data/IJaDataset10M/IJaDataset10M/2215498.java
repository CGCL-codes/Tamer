package org.easyrec.plugin.slopeone.model;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * DOCUMENT ME!
 * <p><b>Company:&nbsp;</b> SAT, Research Studios Austria</p>
 * <p><b>Copyright:&nbsp;</b> (c) 2007</p>
 * <p><b>last modified:</b><br/> $Author$<br/> $Date$<br/> $Revision$</p>
 *
 * @author Patrick Marschik
 */
public class SlopeOneConfigurationValidator implements Validator {

    public boolean supports(final Class clazz) {
        return clazz.equals(SlopeOneConfiguration.class);
    }

    public void validate(final Object target, final Errors errors) {
        SlopeOneConfiguration configuration = (SlopeOneConfiguration) target;
        Integer maxRecsPerItem = configuration.getMaxRecsPerItem();
        if (maxRecsPerItem != null && maxRecsPerItem < 1) errors.rejectValue("maxRecsPerItem", "error.outOfRange", "maxRecsPerItem is not null it has to be greater than or equal to 1.");
        Long minRatedCount = configuration.getMinRatedCount();
        if (minRatedCount != null && minRatedCount < 0) errors.rejectValue("minRatedCount", "error.outOfRange", "minRatedCount is not null it has to be greater than or equal to 0");
    }
}
