package org.jbfilter.test.junit;

import org.jbfilter.bean.factory.FilterComponentBeans;
import org.jbfilter.core.FilterComponent;
import org.jbfilter.core.fcomps.single.ContainsStringFilterComponent;
import org.jbfilter.test.ContainsStringFilterComponentTest;
import org.jbfilter.test.beans.Composer;

public class ContainsStringFilterComponentBeanRefl2Test extends ContainsStringFilterComponentTest {

    @Override
    protected <T> boolean pass(FilterComponent<T> fcomp, T bean, Class<T> beanClass) {
        return JbFilterBeanTestUtils.pass(fcomp, bean, beanClass);
    }

    @Override
    protected ContainsStringFilterComponent<Composer> createComposerLastNameFilterComponent() {
        return FilterComponentBeans.newContainsStringFilterComponent("idLn", "lastName");
    }

    @Override
    protected ContainsStringFilterComponent<Composer> createComposerParticleFilterComponent() {
        return FilterComponentBeans.newContainsStringFilterComponent("idP", "particle");
    }
}
