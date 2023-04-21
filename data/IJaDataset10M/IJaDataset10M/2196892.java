package org.primordion.dynsys.app.leakybucket;

import org.primordion.xholon.base.IIntegration;
import org.primordion.xholon.base.XholonWithPorts;

/**
 * Leaky Bucket model.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8.1 (Created on December 18, 2011)
 */
public class Xhleakybucket extends XholonWithPorts {

    public static int timeStepMultiplier = IIntegration.M_128;

    protected static double dt = 1.0 / (double) timeStepMultiplier;

    public String roleName = null;

    public Xhleakybucket() {
        super();
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static int getTimeStepMultiplier() {
        return timeStepMultiplier;
    }

    public static void setTimeStepMultiplier(int timeStepMultiplier) {
        Xhleakybucket.timeStepMultiplier = timeStepMultiplier;
        dt = 1.0 / (double) timeStepMultiplier;
    }

    public static double getDt() {
        return dt;
    }
}
