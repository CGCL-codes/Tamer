package org.wct.plaf.www.basic;

import org.wct.*;
import org.apache.ecs.GenericElement;
import org.apache.ecs.html.Span;
import org.apache.ecs.html.A;
import org.apache.ecs.ElementAttributes;

/**
 * This class implements the label UI on the basic WWW Look and Feel
 * @author  juliano
 * @version 1.0
 */
public class BasicWWWLabelUI extends org.wct.plaf.www.basic.BasicWWWComponentUI {

    /**
     * Singletron instance
     */
    private static BasicWWWLabelUI ui = new BasicWWWLabelUI();

    public static BasicWWWLabelUI createUI(Object l) {
        return ui;
    }

    /** Creates new BasicWWWLabelUI */
    public BasicWWWLabelUI() {
    }

    /**
     * Returns the representation fo this component as a GenericElement (see Apache's ECS documentation)
     * It is assumed that this representation is intended to be inserted in a html document
 */
    public GenericElement render(Component c) {
        try {
            org.wct.Label l = (org.wct.Label) c;
            Span sp = new Span();
            applyBasicComponentProperties(c, sp);
            if (l.hasListeners()) {
                String id = getFormID(c);
                A link = new A("javascript:postit('" + id + "')", l.getText());
                sp.addElement(link);
            } else sp.addElement(l.getText());
            return sp;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
    }

    /**
     * Process an event on the specified component.
     * @param comp the event's target component.
     * @param var the name of the form variable
     * @param val the value of the variable
 */
    public void processEvent(Component c, String var, String[] val) {
        try {
            org.wct.Label l = (org.wct.Label) c;
            l.clicked();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.toString());
        }
    }
}
