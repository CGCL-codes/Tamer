package com.potix.zk.au.impl;

import com.potix.lang.Objects;
import com.potix.zk.mesg.MZk;
import com.potix.zk.ui.Component;
import com.potix.zk.ui.UiException;
import com.potix.zk.ui.event.Events;
import com.potix.zk.ui.event.InputEvent;
import com.potix.zk.ui.ext.Inputable;
import com.potix.zk.au.AuRequest;

/**
 * Used only by {@link AuRequest} to implement the {@link InputEvent}
 * relevant command.
 * 
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public class InputCommand extends AuRequest.Command {

    public InputCommand(String evtnm, boolean skipIfEverError) {
        super(evtnm, skipIfEverError);
    }

    protected void process(AuRequest request) {
        final Component comp = request.getComponent();
        if (comp == null) throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, this);
        final String[] data = request.getData();
        if (data == null || data.length != 1) throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA, new Object[] { Objects.toString(data), this });
        final String newval = data[0];
        if (this == AuRequest.ON_CHANGE && (comp instanceof Inputable)) ((Inputable) comp).setTextByClient(newval);
        Events.postEvent(new InputEvent(getId(), comp, newval));
    }
}
