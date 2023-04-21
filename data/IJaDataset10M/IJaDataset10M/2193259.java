package org.trackplan.app.gui.layout_component.factories;

import java.awt.Point;
import java.util.regex.Pattern;
import org.trackplan.app.gui.NameStore;
import org.trackplan.app.gui.layout_editor.LayoutComponent;
import org.trackplan.app.gui.layout_editor.ShuntingSignal;

/**
 * Creates a shunting signal.
 * 
 * @author Aristeidis Apostolakis
 *@author James Mistry
 */
public class ShuntingSignalFactory extends ComponentFactory {

    public static final String componentTypeUid = "SHUNTINGSIGNAL";

    public LayoutComponent makeComponent() {
        NameStore name = new NameStore();
        Pattern track = Pattern.compile("^SH\\d+$");
        name.addAllowedFormat(track, "SH<number> - Where <number> is a positive number.");
        String nameWhenAdded = getNextNumberedName(ShuntingSignal.DEFAULT_NAME_PREFIX);
        name.setStoredName(nameWhenAdded);
        return new ShuntingSignal(new Point(), name, "");
    }
}
