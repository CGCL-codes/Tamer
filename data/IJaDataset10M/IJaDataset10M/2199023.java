package net.sf.beanform.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.beanform.prop.BeanProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.event.ReportStatusEvent;
import org.apache.tapestry.form.validator.Validator;
import org.apache.tapestry.form.validator.ValidatorFactory;

/**
 * Default implementation of the {@link CachingValidatorFactory} interface.
 *
 * @author Daniel Gredler
 */
public class CachingValidatorFactoryImpl implements CachingValidatorFactory {

    private static final Log LOG = LogFactory.getLog(CachingValidatorFactoryImpl.class);

    private String serviceId;

    private ValidatorFactory validatorFactory;

    private Map<BeanProperty, List<Validator>> cache = new HashMap<BeanProperty, List<Validator>>();

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setValidatorFactory(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    @SuppressWarnings("unchecked")
    public List<Validator> constructValidatorList(IComponent component, BeanProperty property) {
        List<Validator> validators;
        synchronized (this.cache) {
            validators = this.cache.get(property);
            if (validators == null) {
                List<Validator> inherent = this.getInherentValidators(component, property);
                List<Validator> userDefined = this.validatorFactory.constructValidatorList(component, property.getValidators());
                validators = new ArrayList<Validator>();
                validators.addAll(inherent);
                validators.addAll(userDefined);
                this.cache.put(property, validators);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Bean property '" + property.getName() + "' " + "has " + validators.size() + " validators: " + validators);
                }
            }
        }
        return validators;
    }

    public void resetEventDidOccur() {
        synchronized (this.cache) {
            this.cache.clear();
        }
    }

    public void reportStatus(ReportStatusEvent event) {
        event.title(this.serviceId);
        synchronized (this.cache) {
            event.property("cached validator count", this.cache.size());
            event.collection("cached validators", this.cache.keySet());
        }
    }

    /**
     * Returns validators for the specified property that are inherent to the property type (ie, "this
     * property is a short, so its minimum value must be {@link Short#MIN_VALUE}"). It's important that
     * the number validation come before the min/max value validations.
     */
    @SuppressWarnings("unchecked")
    private List<Validator> getInherentValidators(IComponent component, BeanProperty property) {
        String number = null;
        if (property.isNumber()) {
            if (property.isFloat() || property.isDouble()) number = NumberValidator.NAME; else number = WholeNumberValidator.NAME;
        }
        String minValue;
        if (property.isShort()) minValue = String.valueOf(Short.MIN_VALUE); else if (property.isInteger()) minValue = String.valueOf(Integer.MIN_VALUE); else if (property.isLong()) minValue = String.valueOf(Long.MIN_VALUE); else if (property.isFloat()) minValue = String.valueOf(Float.MIN_VALUE); else if (property.isDouble()) minValue = String.valueOf(Double.MIN_VALUE); else minValue = null;
        String maxValue;
        if (property.isShort()) maxValue = String.valueOf(Short.MAX_VALUE); else if (property.isInteger()) maxValue = String.valueOf(Integer.MAX_VALUE); else if (property.isLong()) maxValue = String.valueOf(Long.MAX_VALUE); else if (property.isFloat()) maxValue = String.valueOf(Float.MAX_VALUE); else if (property.isDouble()) maxValue = String.valueOf(Double.MAX_VALUE); else maxValue = null;
        StringBuilder sb = new StringBuilder();
        if (number != null) sb.append(number);
        if (minValue != null) sb.append(sb.length() > 0 ? "," : "").append(StringMin.NAME).append("=").append(minValue);
        if (maxValue != null) sb.append(sb.length() > 0 ? "," : "").append(StringMax.NAME).append("=").append(maxValue);
        String expression = sb.toString();
        return this.validatorFactory.constructValidatorList(component, expression);
    }
}
