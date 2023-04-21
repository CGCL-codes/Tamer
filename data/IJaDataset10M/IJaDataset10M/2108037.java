package client.game.ui.course.cartelera;

import client.game.hud.HudManager;
import client.game.hud.HudStateType;
import client.game.ui.HudPopUp;

public class InternalFrameCartelera extends HudPopUp {

    private static String hudid = "InternalFrameCartelera";

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public InternalFrameCartelera() {
        super(hudid, HudStateType.WALKING_HUD, HudManager.getInstance());
        this.add(new PanelCartelera());
        this.setVisible(true);
        this.setSize(350, 350);
        this.setLocation(50, 50);
    }
}
