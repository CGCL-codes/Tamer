package com.intel.gpe.gridbeans.povray.web;

import com.intel.gpe.gridbeans.plugins.DataSetException;
import com.intel.gpe.gridbeans.web.PortletPlugin;

public class POVRayWebPlugin extends PortletPlugin {

    public POVRayWebPlugin() throws DataSetException {
        super("POVRay");
        setInputPanel(new POVRayWebInputPanel());
        setOutputPanel(new POVRayWebOutputPanel());
    }
}
