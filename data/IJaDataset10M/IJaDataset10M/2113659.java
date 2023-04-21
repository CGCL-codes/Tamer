package net.sf.jguard.ext.authentication.manager;

import javax.inject.Inject;
import com.google.inject.Provider;
import net.sf.jguard.core.ApplicationName;
import net.sf.jguard.core.authentication.manager.AuthenticationManager;
import net.sf.jguard.core.authentication.manager.AuthenticationXmlStoreFileLocation;
import org.hibernate.Session;
import java.net.URL;

/**
 * @author <a href="mailto:diabolo512@users.sourceforge.net">Charles Gay</a>
 */
public class HibernateAuthenticationManagerProvider implements Provider<AuthenticationManager> {

    private String applicationName;

    private URL fileLocation;

    private Provider<Session> sessionProvider;

    @Inject
    public HibernateAuthenticationManagerProvider(@ApplicationName String applicationName, @AuthenticationXmlStoreFileLocation URL fileLocation, Provider<Session> sessionProvider) {
        this.applicationName = applicationName;
        this.fileLocation = fileLocation;
        this.sessionProvider = sessionProvider;
    }

    public AuthenticationManager get() {
        return new HibernateAuthenticationManager(applicationName, fileLocation, sessionProvider);
    }
}
