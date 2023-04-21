package com.intel.gpe.client2.common.panels.targetsystemchooser;

import com.intel.gpe.client2.common.clientwrapper.ClientWrapper;
import com.intel.gpe.client2.common.panels.tree.Node;
import com.intel.gpe.clients.api.TargetSystemClient;

/**
 * The interface to a target system or a storage or a remote file node. 
 * 
 * @author Alexander Lukichev
 * @version $Id: TargetSystemOrChildNode.java,v 1.1 2006/09/15 10:49:08 vashorin Exp $
 */
public interface TargetSystemOrChildNode extends Node {

    /**
     * Get the target system client associated either with the target system node or
     * the storage node or the file location.
     * @return The target system.
     */
    public ClientWrapper<TargetSystemClient, String> getTargetSystem();
}
