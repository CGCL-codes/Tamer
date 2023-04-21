package com.togethersoft.modules.ocl;

import com.togethersoft.openapi.ide.*;
import com.togethersoft.openapi.ide.inspector.*;
import com.togethersoft.openapi.ide.inspector.util.*;
import com.togethersoft.openapi.ide.inspector.util.editors.*;
import com.togethersoft.openapi.ide.inspector.util.property.*;
import com.togethersoft.openapi.ide.inspector.util.table.*;
import com.togethersoft.openapi.ide.util.*;
import com.togethersoft.openapi.rwi.*;
import com.togethersoft.openapi.util.RwiElementsUtil;

/**
 * Integrates the OCL Editor into the inspector.
 * 
 * Also is the main class of the "OCL Editor" module.
 * 
 * Changes being made by Christian Nill only affect the type of module created (<code>IdeActivatable</code>).
 * @author Stefan Ocke
 * @author Christian Nill */
public class OCLInspectorBuilder implements IdeInspectorBuilder, IdeActivatable {

    /** run by Together on activation of module. */
    public void autorun() {
        IdeInspectorManager manager = IdeInspectorManagerAccess.getInspectorManager();
        manager.addInspectorBuilder(this);
    }

    /** run by Together on deactivation of module. */
    public void shutdown() {
        IdeInspectorManager manager = IdeInspectorManagerAccess.getInspectorManager();
        manager.removeInspectorBuilder(this);
    }

    /**
     * constructs the "OCL Constraints" inspector page. */
    public IdeInspector buildInspector(IdeContext context, IdeInspector inspector) {
        RwiElement[] rwiElements = RwiElementsUtil.getRwiElements(context);
        if (rwiElements.length != 1) {
            return inspector;
        }
        oclInspectorComp.setContext(rwiElements[0]);
        inspector.addComponent(oclInspectorComp, new OCLConstraintVisibilityCondition());
        System.err.println("OCLInspectorComponent created");
        return inspector;
    }

    /** @directed */
    private OCLInspectorComponent oclInspectorComp = new OCLInspectorComponent();
}
