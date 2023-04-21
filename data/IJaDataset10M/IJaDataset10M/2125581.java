package org.carabiner.state;

import java.util.Date;
import org.carabiner.util.CarabinerException;
import org.carabiner.util.EnvironmentProperties;

public class StubStateOfGrace extends AbstractStateOfGrace {

    private boolean blessed = false;

    private Date date = new Date();

    public StubStateOfGrace() {
        super(StubStateOfGrace.class);
    }

    public void bless() throws CarabinerException {
        blessed = true;
        date = new Date();
        fireStateChangeEvent();
    }

    public void curse() throws CarabinerException {
        blessed = false;
        date = new Date();
        fireStateChangeEvent();
    }

    public Date getDateChanged() {
        return (Date) date.clone();
    }

    public boolean isBlessed() throws CarabinerException {
        return blessed;
    }

    public String getLastUser() {
        return System.getProperty("user.name");
    }

    public String getOperatingSystem() {
        return EnvironmentProperties.getOSProperty();
    }

    public String getJavaVersion() {
        return EnvironmentProperties.getJVMProperty();
    }
}
