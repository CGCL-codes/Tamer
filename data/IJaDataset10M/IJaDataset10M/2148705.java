package beanface.el.functor.testing;

import java.util.Vector;
import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;
import com.sun.el.lang.FunctionMapperImpl;
import com.sun.faces.el.ELContextImpl;

public class ELContextFactory {

    private ELContext elContext;

    private ELResolver elResolver;

    private Vector<ELResolver> extraELResolvers;

    public void addELResolver(ELResolver r) {
        if (extraELResolvers == null) extraELResolvers = new Vector<ELResolver>();
        extraELResolvers.add(r);
        if (elResolver != null && elResolver instanceof CompositeELResolver) ((CompositeELResolver) elResolver).add(r);
    }

    public ELResolver getELResolver() {
        if (elResolver == null) {
            CompositeELResolver resolver = new CompositeELResolver();
            if (extraELResolvers != null) {
                for (ELResolver r : extraELResolvers) resolver.add(r);
            }
            resolver.add(new MapELResolver());
            resolver.add(new ResourceBundleELResolver());
            resolver.add(new ListELResolver());
            resolver.add(new ArrayELResolver());
            resolver.add(new BeanELResolver());
            elResolver = resolver;
        }
        return elResolver;
    }

    public ELContext getELContext() {
        if (elContext == null) {
            ELContextImpl elContextImpl = new ELContextImpl(getELResolver());
            elContextImpl.setVariableMapper(new VariableMapperImpl());
            elContextImpl.setFunctionMapper(new FunctionMapperImpl());
            elContext = elContextImpl;
        }
        return elContext;
    }
}
