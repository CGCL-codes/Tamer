package com.j2xtreme.xwidget.xwt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author rob
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FlowContainer extends Container {

    private static Log log = LogFactory.getLog(FlowContainer.class);

    public FlowContainer() {
        super();
    }

    List children = new ArrayList();

    public void doRemove(Widget w) {
        children.remove(w);
    }

    public List getChildWidgets() {
        return Collections.unmodifiableList(children);
    }

    public void doAdd(Widget w, Constraints constraints) {
        log.debug("Adding: " + w);
        children.add(w);
    }
}
