package pl.rzarajczyk.breaktime.utils.localization.swing;

import java.lang.reflect.Field;
import org.apache.commons.logging.Log;
import pl.rzarajczyk.breaktime.utils.localization.LocalizableElement;
import pl.rzarajczyk.utils.log.LazyLogFactory;

/**
 *
 * @author rafalz
 */
public abstract class AbstractFieldLocalizableElement<E> implements LocalizableElement {

    protected Log log = LazyLogFactory.getLog(getClass());

    protected Field f;

    protected Object self;

    public AbstractFieldLocalizableElement(Field f, Object self) {
        this.f = f;
        this.self = self;
    }

    @Override
    public String getKey() {
        return LocalizationUtils.buildKey(self.getClass(), f.getName());
    }

    protected E getFieldValue() {
        return LocalizationUtils.<E>get(f, self, true);
    }
}
