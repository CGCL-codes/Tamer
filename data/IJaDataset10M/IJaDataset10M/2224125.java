package com.potix.zk.au.impl;

import com.potix.zk.ui.Component;
import com.potix.zk.ui.UiException;
import com.potix.zk.au.AuRequest;

/**
 * Used only by {@link AuRequest} to implement the remove command.
 * 
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public class RemoveCommand extends AuRequest.Command {

    public RemoveCommand(String evtnm, boolean skipIfEverError) {
        super(evtnm, skipIfEverError);
    }

    protected void process(AuRequest request) {
        final Component comp = request.getComponent();
        if (comp != null) comp.detach();
    }
}
