package ch.trackedbean.binding.util.validation;

import java.util.Map.Entry;
import ch.simpleel.util.*;
import ch.trackedbean.binding.mapping.*;
import ch.trackedbean.tracking.*;
import ch.trackedbean.validator.*;

/**
 * {@link IBeanValidator} delegating the validation to {@link ValidatorManager#validateBusinessPart(Object)}.
 * 
 * @author M. Hautle
 */
public class DefaultBusinessValidator implements IBeanValidator {

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object bean, final IBeanStatus status, String dst) {
        final ValidationResult res = ValidatorManager.validateBusinessPart(bean);
        if (res.isBeanValid()) return;
        for (Entry<String, IErrorDescription[]> entry : res.getAllErrors().entrySet()) {
            final String property = entry.getKey();
            if (ELHelper.isSimpleProperty(property)) {
                status.setStatus(property, BeanStatusFlag.ERROR, ValidatorManager.buildErrorString(entry.getValue()));
                continue;
            }
            final IBeanStatus propStaus = TrackedBeanUtils.getBeanStatus(bean, ELHelper.getParentPath(property));
            propStaus.setStatus(ELHelper.getLastSegment(property), BeanStatusFlag.ERROR, ValidatorManager.buildErrorString(entry.getValue()));
        }
    }
}
