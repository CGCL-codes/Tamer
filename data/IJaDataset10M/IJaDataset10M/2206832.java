package org.gecko.jee.community.mobidick.business;

import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

public class AccessorSpringHttp<EXPOSE extends Object> extends HttpInvokerProxyFactoryBean implements Accessor<EXPOSE> {

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.gecko.jee.community.mobidick.business.Accessor#getObject()
	 */
    @Override
    public EXPOSE getObject() {
        return (EXPOSE) super.getObject();
    }
}
