package ru.susu.algebra.centralunits.alternating;

import ru.susu.algebra.methods.IMathMethodPS;
import ru.susu.algebra.properties.IPropertySource;

/**
 * @author akargapolov
 * @since: 26.08.2010
 */
public abstract class MathMethodWithInitializers<R> implements IMathMethodPS<R> {

    @SuppressWarnings("unchecked")
    @Override
    public R run(IPropertySource ps) throws Exception {
        for (Class<? extends IMathMethodPS> dep : getDependentInitializers()) {
            ((IMathMethodPS) dep.newInstance()).run(ps);
        }
        return directRun(ps);
    }

    protected abstract R directRun(IPropertySource ps) throws Exception;

    protected abstract Class[] getDependentInitializers();
}
