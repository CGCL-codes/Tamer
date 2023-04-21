package org.apache.axis2.jaxws.module;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.modules.Module;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;

public class JAXWSModule implements Module {

    public void engageNotify(AxisDescription axisDescription) throws AxisFault {
    }

    public void init(ConfigurationContext configurationContext, AxisModule module) throws AxisFault {
        AxisConfiguration axisConfiguration = configurationContext.getAxisConfiguration();
        registerGlobalModule(axisConfiguration, "jaxws");
    }

    public void shutdown(ConfigurationContext configurationContext) throws AxisFault {
    }

    public void applyPolicy(Policy policy, AxisDescription axisDescription) throws AxisFault {
    }

    public boolean canSupportAssertion(Assertion assertion) {
        return false;
    }

    private void registerGlobalModule(AxisConfiguration axisConfiguration, String moduleName) {
        if (!axisConfiguration.getGlobalModules().contains(moduleName)) {
            axisConfiguration.addGlobalModuleRef(moduleName);
        }
    }
}
