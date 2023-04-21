package com.borzak.cncmill;

import java.util.*;
import org.apache.commons.logging.*;

public class MirrorCommand extends ActionListCommand {

    private static Log log = LogFactory.getLog(MirrorCommand.class);

    private MillingTransform transform = new MirrorXTransform();

    public MirrorCommand(MillingActionList actionsList, ProgressStatusBar statusBar) {
        super("Mirror X Axis", actionsList, statusBar);
    }

    protected void execute() {
        List list = actionsList.getSelectedList(0);
        List newActions = new LinkedList();
        log.debug("Mirroring around X axis");
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            MillingAction a = (MillingAction) iter.next();
            newActions.add(a.getTransformedInstance(transform));
        }
        actionsList.addAll(newActions);
        actionsList.removeAll(list);
    }
}
