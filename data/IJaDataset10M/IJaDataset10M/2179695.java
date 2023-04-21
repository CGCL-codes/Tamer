package org.apache.webdav.connector;

import javax.security.auth.Subject;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.security.PasswordCredential;
import java.security.PrivilegedAction;
import java.util.Properties;
import java.util.Iterator;

public class WebDAVSubjectAction implements PrivilegedAction {

    private Subject subject;

    private Properties props;

    private ManagedConnectionFactory mcf;

    WebDAVSubjectAction(Subject subject, Properties props, ManagedConnectionFactory mcf) {
        this.subject = subject;
        this.props = props;
        this.mcf = mcf;
    }

    public Object run() {
        Iterator i = subject.getPrivateCredentials().iterator();
        while (i.hasNext()) {
            Object o = i.next();
            if (o instanceof PasswordCredential) {
                PasswordCredential cred = (PasswordCredential) o;
                if (cred.getManagedConnectionFactory().equals(mcf)) {
                    props.setProperty("userName", (cred.getUserName() == null) ? "" : cred.getUserName());
                    props.setProperty("password", new String(cred.getPassword()));
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
}
