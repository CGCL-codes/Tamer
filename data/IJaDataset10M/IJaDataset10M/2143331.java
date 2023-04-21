package org.jrest4guice.jndi;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
@SuppressWarnings("unchecked")
public class JndiServiceInfo {

    private Class serviceClass;

    private String jndiName;

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public JndiServiceInfo(Class serviceClass, String jndiName) {
        super();
        this.serviceClass = serviceClass;
        this.jndiName = jndiName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((jndiName == null) ? 0 : jndiName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final JndiServiceInfo other = (JndiServiceInfo) obj;
        if (jndiName == null) {
            if (other.jndiName != null) return false;
        } else if (!jndiName.equals(other.jndiName)) return false;
        return true;
    }
}
