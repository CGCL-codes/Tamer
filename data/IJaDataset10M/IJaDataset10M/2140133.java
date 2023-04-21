package org.jbfilter.test.junit;

import java.util.Date;
import org.jbfilter.bean.PropertyAccessor;
import org.jbfilter.bean.factory.FilterComponentBeans;
import org.jbfilter.core.FilterComponent;
import org.jbfilter.core.fcomps.single.DateFilterComponent;
import org.jbfilter.test.DateFilterComponentTest;
import org.jbfilter.test.beans.Composer;

public class DateFilterComponentBeanTest extends DateFilterComponentTest {

    @Override
    protected <T> boolean pass(FilterComponent<T> fcomp, T bean, Class<T> beanClass) {
        return JbFilterBeanTestUtils.pass(fcomp, bean, beanClass);
    }

    @Override
    protected DateFilterComponent<Composer> createDateFilterComponent() {
        return FilterComponentBeans.newDateFilterComponent("id0", new PropertyAccessor<Composer, Date>() {

            public Date getPropertyValue(Composer composer) {
                return composer.getDied();
            }
        });
    }
}
