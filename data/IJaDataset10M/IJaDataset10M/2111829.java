package org.argouml.ui.cmd;

import org.argouml.i18n.Translator;
import org.tigris.gef.base.AdjustPageBreaksAction;

/**
 * This Action toggles the display of the page breaks lines.
 *
 * @author Michiel
 */
public class ActionAdjustPageBreaks extends AdjustPageBreaksAction {

    /**
     * The Constructor.
     */
    public ActionAdjustPageBreaks() {
        this(Translator.localize("menu.adjust-pagebreaks"));
    }

    /**
     * The Constructor.
     * 
     * @param name the name of the action
     */
    public ActionAdjustPageBreaks(String name) {
        super(name);
    }
}
