package org.nakedobjects.viewer.web.request;

import org.nakedobjects.object.NakedObject;
import org.nakedobjects.viewer.web.component.Block;
import org.nakedobjects.viewer.web.component.Component;
import org.nakedobjects.viewer.web.component.DebugPane;
import java.util.Vector;

public class ObjectHistory {

    private final Vector history = new Vector();

    public void listObjects(Context context, Block navigation) {
        Block taskBar = context.getFactory().createBlock("history");
        taskBar.add(context.getFactory().createHeading("History"));
        for (int i = history.size() - 1; i >= 0; i--) {
            NakedObject object = ((NakedObject) history.elementAt(i));
            String id = context.registerNakedObject(object);
            Component icon = context.getFactory().createObjectIcon(object, id, "history");
            taskBar.add(icon);
        }
        navigation.add(taskBar);
    }

    public void add(NakedObject object) {
        history.removeElement(object);
        history.addElement(object);
    }

    public void debug(DebugPane debugPane) {
        for (int i = history.size() - 1; i >= 0; i--) {
            NakedObject object = ((NakedObject) history.elementAt(i));
            debugPane.appendln(object.toString());
        }
    }
}
