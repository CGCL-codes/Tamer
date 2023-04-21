package ch.trackedbean.data.validator;

import java.util.*;
import ch.trackedbean.data.*;
import ch.trackedbean.utils.*;
import ch.trackedbean.validator.*;
import ch.trackedbean.validator.businessValidator.*;
import ch.trackedbean.validator.internal.*;

/**
 * Business Validator for {@link DomC}.
 * 
 * @author M. Hautle
 */
public class DomCValidator extends AbstractBusinessValidator {

    /** The error message returned by this validator. */
    public static final IErrorDescription ERROR = new ErrorDescription("deepvalidation.error");

    /** The error message set by this validator. */
    public static final IErrorDescription ELEMENT_ERROR = new ErrorDescription("wrong.mail");

    /**
     * {@inheritDoc}
     */
    @Override
    public IErrorDescription validate(Object bean, IValidationContext context) {
        final List list = BeanTestUtil.asList(get(DomC.ATTR_LIST, bean, null));
        if (list == null) return null;
        boolean err = false;
        final String base = get(DomA.ATTR_TEXT_A, list.get(0), null);
        final String basePath = getExpression(DomC.ATTR_LIST, bean.getClass());
        for (int i = 1, cnt = list.size(); i < cnt; i++) {
            final Object o = list.get(i);
            if (!base.equals(get(DomA.ATTR_TEXT_A, o, null))) {
                err = true;
                context.addErrors(basePath + "[" + i + "]." + ValidationResult.GENERAL, ELEMENT_ERROR);
            }
        }
        return err ? ERROR : null;
    }
}
