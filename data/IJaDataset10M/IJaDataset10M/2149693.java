package net.sf.domainpersist.common;

import net.sf.domainpersist.util.MyBeanFactory;

/**
 * @author Fernando Scanavino <fscanavino@users.sourceforge.net>
 *
 */
public class DomainPersistenceTestFixture {

    public DomainPersistenceTestFixture() {
        super();
        this.beanFactory = MyBeanFactory.getInstance();
    }

    protected MyBeanFactory getTestBeanFactory() {
        return this.beanFactory;
    }

    protected <T> T getTestBean(String beanName, Class<T> beanClass) {
        return beanClass.cast(this.beanFactory.getTestBean(beanName));
    }

    private MyBeanFactory beanFactory;
}
