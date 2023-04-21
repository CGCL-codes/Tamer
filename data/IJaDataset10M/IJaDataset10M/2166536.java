package edu.berkeley.guir.quill;

import java.util.*;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import edu.berkeley.guir.quill.gesturelib.*;

/** 
 * <P>
 * This software is distributed under the 
 * <A HREF="http://guir.cs.berkeley.edu/projects/COPYRIGHT.txt">
 * Berkeley Software License</A>.
 
*/
public class DuplicateNameNotice extends AbstractInfoNotice {

    /** the name that > 1 container has */
    private String name;

    /** all the GestureContainers with the same name */
    private Set containers;

    private boolean displayMe = true;

    public DuplicateNameNotice(GestureContainer gc1, GestureContainer gc2) {
        containers = new HashSet();
        containers.add(gc1);
        containers.add(gc2);
        name = gc1.getName();
    }

    public DuplicateNameNotice(Collection gestureCategories) {
        containers = new HashSet(gestureCategories);
        name = ((GestureCategory) gestureCategories.iterator().next()).getName();
    }

    /** if either container name has changed, expire this */
    public boolean hasExpired() {
        for (Iterator iter = containers.iterator(); iter.hasNext(); ) {
            GestureContainer gc = (GestureContainer) iter.next();
            if (!gc.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    protected void displayImpl(SummaryLog log) {
        if (displayMe) {
            log.append("Warning: two gestures share the name '" + name + "'. You should rename one.");
            displayMe = false;
        }
    }

    protected void displaySummaryImpl(SummaryLog log) {
    }

    public String getName() {
        return "Duplicate name: '" + name + "'";
    }

    /** returns a Set */
    public Collection getGestureObjects() {
        return containers;
    }

    public HowToPanel getHowToPanel() {
        HowToPanel htPanel = new HowToPanel();
        htPanel.add(new JTextArea("To rename an object, select it in the tree and execute the 'Gesture/Rename' menu item."), BorderLayout.CENTER);
        return htPanel;
    }

    public String getReferenceTag() {
        return "Duplicate Names";
    }

    public String toString() {
        StringBuffer result = new StringBuffer("DuplicateNameNotice(" + super.hashCode() + ")[");
        for (Iterator iter = containers.iterator(); iter.hasNext(); ) {
            GestureContainer gc = (GestureContainer) iter.next();
            result.append(gc.getName());
            if (iter.hasNext()) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }
}
