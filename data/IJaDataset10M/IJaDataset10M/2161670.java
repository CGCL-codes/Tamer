package no.ugland.utransprod.gui.buttons;

import javax.swing.JButton;
import no.ugland.utransprod.gui.IconEnum;
import no.ugland.utransprod.gui.Updateable;
import no.ugland.utransprod.gui.WindowInterface;

/**
 * Generell lagreknapp
 * @author atle.brekka
 *
 */
public class SaveButton extends JButton {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * @param updateable
	 * @param window
	 */
    public SaveButton(Updateable updateable, WindowInterface window) {
        super(new SaveAction(updateable, window));
        setIcon(IconEnum.ICON_SAVE.getIcon());
    }
}
