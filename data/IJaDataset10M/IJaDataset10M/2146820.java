package shake.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.ELResolver;
import shake.annotation.Component;
import shake.context.NameResolver;
import com.google.inject.Inject;
import com.google.inject.Injector;

@Component
public class ComponentElResolver extends ELResolver {

    @Inject
    NameResolver nameResolver;

    @Inject
    Injector injector;

    @Override
    public Class<?> getCommonPropertyType(ELContext arg0, Object arg1) {
        return null;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext arg0, Object arg1) {
        return null;
    }

    @Override
    public Class<?> getType(ELContext arg0, Object arg1, Object arg2) {
        if (arg1 != null) {
            return null;
        }
        if (nameResolver.get((String) arg2) != null) {
            arg0.setPropertyResolved(true);
            return nameResolver.get((String) arg2);
        }
        return null;
    }

    @Override
    public Object getValue(ELContext arg0, Object arg1, Object arg2) {
        if (arg1 != null) {
            return null;
        }
        if (nameResolver.get((String) arg2) != null) {
            arg0.setPropertyResolved(true);
            return injector.getInstance(nameResolver.get((String) arg2));
        }
        return null;
    }

    @Override
    public boolean isReadOnly(ELContext arg0, Object arg1, Object arg2) {
        return true;
    }

    @Override
    public void setValue(ELContext arg0, Object arg1, Object arg2, Object arg3) {
    }
}
