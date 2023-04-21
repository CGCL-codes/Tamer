package net.sf.mytoolbox.test;

import org.easymock.EasyMock;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Allows to create mocked beans within Spring. <br/>
 * @author ggrussenmeyer
 */
@SuppressWarnings("unchecked")
public class MockFactoryBean extends AbstractFactoryBean {

    private Class targetClass;

    public MockFactoryBean() {
    }

    /**
     * Sets the class of the interface to mock. <br/>
     * @param targetClass
     */
    public void setInterface(Class targetClass) {
        this.targetClass = targetClass;
    }

    protected Object createInstance() throws Exception {
        return EasyMock.createMock(this.targetClass);
    }

    public Class getObjectType() {
        return this.targetClass;
    }
}
