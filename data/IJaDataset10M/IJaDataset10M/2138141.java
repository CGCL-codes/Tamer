package com.minfo.services.module;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.modules.Module;
import org.apache.log4j.Logger;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;

public class TransformModule implements Module {

    private static transient Logger log = Logger.getLogger(TransformModule.class);

    public void applyPolicy(Policy arg0, AxisDescription arg1) throws AxisFault {
    }

    public boolean canSupportAssertion(Assertion arg0) {
        return false;
    }

    public void engageNotify(AxisDescription arg0) throws AxisFault {
    }

    public void init(ConfigurationContext arg0, AxisModule arg1) throws AxisFault {
        log.debug("init(...)");
    }

    public void shutdown(ConfigurationContext arg0) throws AxisFault {
    }
}
