package org.zkoss.zk.au.in;

import org.zkoss.lang.Objects;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectionEvent;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.Command;

/**
 * Used only by {@link AuRequest} to implement the {@link SelectionEvent}
 * relevant command.
 * 
 * @author jumperchen
 * @since 3.0.0
 */
public class SelectionCommand extends Command {

    public SelectionCommand(String evtnm, int flags) {
        super(evtnm, flags);
    }

    protected void process(AuRequest request) {
        final Component comp = request.getComponent();
        if (comp == null) throw new UiException(MZk.ILLEGAL_REQUEST_COMPONENT_REQUIRED, this);
        final String[] data = request.getData();
        if (data == null || data.length != 3) throw new UiException(MZk.ILLEGAL_REQUEST_WRONG_DATA, new Object[] { Objects.toString(data), this });
        final int start = Integer.parseInt(data[0]);
        final int end = Integer.parseInt(data[1]);
        final String txt = data[2];
        Events.postEvent(new SelectionEvent(getId(), comp, start, end, txt));
    }
}
