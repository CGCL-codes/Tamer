package com.controltier.ctl.tasks.controller.node;

import com.controltier.ctl.common.IObject;
import com.controltier.ctl.common.ICommand;
import com.controltier.ctl.common.ICommand;
import java.io.IOException;
import java.util.Collection;

/**
 * Interface for looking up where the given resource resides on the network.
 */
public interface INodeLookup {

    /**
     * Lookup the nodes where the object resides
     *
     * @param object Object deployment
     * @return Collection of {@link com.controltier.ctl.common.INodeDesc} instances where object resides
     */
    Collection lookup(IObject object) throws IOException;

    /**
     * Lookup the nodes where the command module resides
     *
     * @param command module
     * @return Collection of {@link com.controltier.ctl.common.INodeDesc} instances where command module resides
     */
    Collection lookup(ICommand command) throws IOException;
}
