package net.taylor.identity.jmx;

import java.util.Calendar;
import net.taylor.inject.Locator;
import net.taylor.jmx.MBean;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;

/**
 * This defines the MBean interface for various identity configuration settings.
 * 
 * @author jgilbert01
 */
@Name("identityConfiguration")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Startup
@MBean(objectName = Configuration.OBJECT_NAME)
@Install(precedence = Install.BUILT_IN)
public class Configuration {

    public static final String OBJECT_NAME = "net.taylor.identity:service=Configuration,application=#{applicationName}";

    private Boolean lockingEnabled = true;

    public Boolean getLockingEnabled() {
        return lockingEnabled;
    }

    public void setLockingEnabled(Boolean lockingEnabled) {
        this.lockingEnabled = lockingEnabled;
    }

    private Integer lockDuration = 30;

    public Integer getLockDuration() {
        return lockDuration;
    }

    public void setLockDuration(Integer lockDuration) {
        this.lockDuration = lockDuration;
    }

    private Integer maxFailedLoginAttempts = 3;

    public Integer getMaxFailedLoginAttempts() {
        return maxFailedLoginAttempts;
    }

    public void setMaxFailedLoginAttempts(Integer maxFailedLoginAttempts) {
        this.maxFailedLoginAttempts = maxFailedLoginAttempts;
    }

    public static Configuration instance() {
        return (Configuration) Locator.getInstance(Configuration.class);
    }
}
