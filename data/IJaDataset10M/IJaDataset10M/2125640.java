package net.hypotenubel.jaicwain.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import net.hypotenubel.jaicwain.App;
import net.hypotenubel.util.swing.DesktopLayout;

/**
 * When invoked, this action makes a side dominant in a
 * {@code DesktopLayout}.
 * 
 * @author Christoph Daniel Schulze
 * @version $Id: MakeDominantAction.java 112 2006-09-20 16:49:31Z captainnuss $
 */
public class MakeDominantAction extends AbstractAction {

    /**
     * {@code Container} that uses the layout manager.
     */
    private JComponent component;

    /**
     * {@code DesktopLayout} being the target of everything we do.
     */
    private DesktopLayout target;

    /**
     * {@code String} specifying the side which is to be made dominant.
     */
    private String side;

    /**
     * Creates a new {@code MakeDominantAction} object and initializes it.
     * 
     * @param component {@code JComponent} that uses the layout manager.
     * @param target {@code DesktopLayout} that shall be the target for our
     *               actions.
     * @param side {@code String} containing the name of the side which
     *             shall be made dominant.
     */
    public MakeDominantAction(JComponent component, DesktopLayout target, String side) {
        if (component == null) {
            throw new NullPointerException("component can't be null");
        }
        if (target == null) {
            throw new NullPointerException("target can't be null");
        }
        if (side == null) {
            throw new NullPointerException("side can't be null");
        }
        this.component = component;
        this.target = target;
        this.side = side;
        putValue(NAME, App.localization.localize("app", "makedominantaction" + ".name", "Fill whole side"));
        putValue(SHORT_DESCRIPTION, App.localization.localize("app", "makedominantaction.description.short", "Makes this container window fill the" + "whole side."));
        setEnabled(!target.isDominant(side));
    }

    public void actionPerformed(ActionEvent e) {
        try {
            target.setDominant(side);
            component.revalidate();
        } catch (IllegalArgumentException f) {
        }
    }
}
