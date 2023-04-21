package org.nakedobjects.plugins.dndviewer;

import java.util.Vector;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.plugins.dndviewer.viewer.AwtColorsAndFonts;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Color;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Location;

public class UserActionSet implements UserAction {

    private Color backgroundColor = Toolkit.getColor(AwtColorsAndFonts.COLOR_DEBUG_BASELINE);

    private final String groupName;

    private final boolean includeDebug;

    private final boolean includeExploration;

    private final Vector options = new Vector();

    private final NakedObjectActionType type;

    public UserActionSet(final boolean includeExploration, final boolean includeDebug, final NakedObjectActionType type) {
        this.type = type;
        this.groupName = "";
        this.includeExploration = includeExploration;
        this.includeDebug = includeDebug;
    }

    public UserActionSet(final String groupName, final UserActionSet parent) {
        this.groupName = groupName;
        this.includeExploration = parent.includeExploration;
        this.includeDebug = parent.includeDebug;
        this.type = parent.type;
        this.backgroundColor = parent.getColor();
    }

    public UserActionSet(final String groupName, final UserActionSet parent, final NakedObjectActionType type) {
        this.groupName = groupName;
        this.includeExploration = parent.includeExploration;
        this.includeDebug = parent.includeDebug;
        this.type = type;
        this.backgroundColor = parent.getColor();
    }

    /**
     * Add the specified option if it is of the right type for this menu.
     */
    public void add(final UserAction option) {
        final NakedObjectActionType section = option.getType();
        if (section == USER || (includeExploration && section == EXPLORATION) || (includeDebug && section == DEBUG)) {
            options.addElement(option);
        }
    }

    public Consent disabled(final View view) {
        return Allow.DEFAULT;
    }

    public void execute(final Workspace workspace, final View view, final Location at) {
    }

    /**
     * Returns the background colour for the menu
     */
    public Color getColor() {
        return backgroundColor;
    }

    public String getDescription(final View view) {
        return "";
    }

    public String getHelp(final View view) {
        return "";
    }

    public UserAction[] getMenuOptions() {
        final UserAction[] v = new UserAction[options.size()];
        for (int i = 0; i < v.length; i++) {
            v[i] = (UserAction) options.elementAt(i);
        }
        return v;
    }

    public String getName(final View view) {
        return groupName;
    }

    public NakedObjectActionType getType() {
        return type;
    }

    /**
     * Specifies the background colour for the menu
     */
    public void setColor(final Color color) {
        backgroundColor = color;
    }

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("type", type);
        for (int i = 0, size = options.size(); i < size; i++) {
            str.append(((UserAction) options.elementAt(i)).getClass() + " ,");
        }
        return str.toString();
    }
}
