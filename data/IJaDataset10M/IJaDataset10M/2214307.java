package com.abiquo.test.intensive.command;

import com.abiquo.Grid;
import com.abiquo.api.resources.CommandResource;
import com.abiquo.test.intensive.AbstractMaster;

public class CommandMaster extends AbstractMaster {

    /**
     * Instantiates the class variables, provisioning on each node the ping resource
     * @param gr
     *             the grid reference from SystemTest
     * */
    public CommandMaster(Grid gr, int iters) {
        super(gr, CommandResource.ID, false, iters);
    }

    public CommandNodeSequence createNodeSequenceAt(String nodeName, int iters) {
        return new CommandNodeSequence(CommandResource.cast(htResources.get(nodeName)), iters);
    }
}
