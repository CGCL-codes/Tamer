package org.personalsmartspace.ipojo;

import java.util.Dictionary;
import org.osgi.framework.ServiceRegistration;

public class PSSSuperServiceRegistration {

    public PSSSuperServiceRegistration(ServiceRegistration mSr) {
        m_sr = mSr;
    }

    public ServiceRegistration m_sr;

    public Object describe() {
        StringBuffer description = new StringBuffer("\nSuperServiceRegistration");
        return description.toString();
    }

    public void unregister() {
        if (m_sr != null) {
            m_sr.unregister();
            m_sr = null;
        }
    }

    public void setProperties(Dictionary propertiesToPublish) {
        if (this.m_sr != null) {
            m_sr.setProperties(propertiesToPublish);
        }
    }
}
