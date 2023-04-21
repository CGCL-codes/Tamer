package org.mbari.vars.annotation.ui;

import javax.swing.ImageIcon;
import org.mbari.vars.annotation.ui.actions.AddPopulationPropAction;

/**
 * <p>
 * Adds a 'population of' association to the currently selected observations
 * </p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: PopulationPropButton.java 314 2006-07-10 02:38:46Z hohonuuli $
 * @see org.mbari.vars.annotation.ui.actions.AddPopulationPropAction
 */
public class PopulationPropButton extends PropButton {

    /**
     *
     */
    private static final long serialVersionUID = -4737835322654961290L;

    /**
     *      Constructor
     */
    public PopulationPropButton() {
        super();
        setAction(new AddPopulationPropAction());
        setIcon(new ImageIcon(getClass().getResource("/images/vars/annotation/nbutton.png")));
        setToolTipText("population, 2 or more");
        setEnabled(false);
    }
}
