package net.martinimix.beans.bind;

import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import net.martinimix.beans.BasePairPropertyEditorRegistry;
import net.martinimix.beans.MartiniMixBasePairPropertyEditorRegistry;
import net.martinimix.beans.bind.support.DNABasePairPropertyEditor;
import net.martinimix.domain.account.PhoneNumber;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import com.bluemartini.dna.BMContext;
import com.bluemartini.dna.BusinessObject;
import com.bluemartini.dna.BusinessObjectDef;
import com.bluemartini.dna.Currency;
import com.bluemartini.dna.DNAList;

/**
 * Provides an abstract business object binder capable of binding fields on Java beans
 * to Blue Martini <code>BusinesObject</code>s
 * 
 * @author Scott Rossillo
 *
 */
public abstract class AbstractBusinessObjectBinder implements BusinessObjectBinder {

    private static final Log log = LogFactory.getLog(BusinessObjectBinder.class);

    private BasePairPropertyEditorRegistry basePairPropertyEditorRegistry = new MartiniMixBasePairPropertyEditorRegistry();

    /**
	 * Constructs a new business object binder without providing a 
	 * property map.
	 */
    public AbstractBusinessObjectBinder() {
    }

    public final BusinessObject bind(Object source, String businessObjectType) throws BusinessObjectBindException {
        BusinessObjectDef businessObjectDef = BMContext.getBusinessObjectDef(businessObjectType);
        if (businessObjectDef == null || businessObjectDef.isEmpty()) {
            throw new IllegalArgumentException("BusinessObject type [" + businessObjectType + "] " + "does not appear to be defined.");
        }
        return businessObjectDef.createBusinessObject(this.doBind(source));
    }

    /**
	 * Binds the fields from the given source to a DNA list.
	 * 
	 * @param bean the <code>Object</code> whose fields will be bound to
	 * the returned <code>DNAList</code> properties
	 * 
	 * @return a <code>DNAList</code> populated with properties based on the
	 * given <code>object</code>'s fields
	 * 
	 * @throws BusinessObjectBindException if a critical error occurs while binding
	 * fields to properties
	 */
    protected final DNAList doBind(Object bean) throws BusinessObjectBindException {
        DNAList target = new DNAList();
        final Map fieldToPropertyMap = this.fieldToPropertyMap(bean);
        final BeanWrapper wrapper = new BeanWrapperImpl(bean);
        final PropertyDescriptor fields[] = wrapper.getPropertyDescriptors();
        if (log.isDebugEnabled()) {
            log.debug("doing bind on [" + bean + "] with [" + fields.length + "] fields.");
        }
        String fieldName;
        Object value;
        for (int i = 0; i < fields.length; i++) {
            fieldName = fields[i].getName();
            value = wrapper.getPropertyValue(fieldName);
            if (log.isDebugEnabled()) {
                log.debug("\tBinding [" + fieldName + "] -> [" + formatValue(value) + "]");
            }
            bindField(target, (String) fieldToPropertyMap.get(fieldName), value);
        }
        return target;
    }

    protected abstract Map fieldToPropertyMap(Object bean);

    private void bindField(DNAList target, String key, Object value) {
        if (value == null || key == null) {
            return;
        }
        DNABasePairPropertyEditor editor = basePairPropertyEditorRegistry.findCustomEditor(value.getClass());
        if (editor != null) {
            target.setBasePair(key, editor.getAsBasePair(value));
        } else {
            if (value instanceof Long) {
                target.setLong(key, (Long) value);
            } else if (value instanceof String) {
                target.setString(key, (String) value);
            } else if (value instanceof Currency) {
                target.setCurrency(key, (Currency) value);
            } else if (value instanceof Boolean) {
                target.setBoolean(key, (Boolean) value);
            } else if (value instanceof Calendar) {
                target.setDateTime(key, (Calendar) value);
            } else if (value instanceof Double) {
                target.setDouble(key, (Double) value);
            } else if (value instanceof PhoneNumber) {
                target.setString(key, value.toString());
            }
        }
    }

    /**
	 * Returns the given value formatted for print (debugging).
	 */
    private String formatValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Calendar) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            return fmt.format(((Calendar) value).getTime());
        }
        return value.toString();
    }
}
