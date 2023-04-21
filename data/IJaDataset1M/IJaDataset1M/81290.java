package trstudio.blueboxalife.graphic.part;

import java.awt.Color;
import java.awt.Graphics2D;
import trstudio.blueboxalife.boa.BOA;
import trstudio.classlibrary.io.event.InputAction;
import trstudio.blueboxalife.io.InputManager;
import trstudio.blueboxalife.state.ALifeException;
import trstudio.classlibrary.drivers.Main;
import trstudio.blueboxalife.state.GameProcess;
import trstudio.classlibrary.drivers.Resource;

/**
 * Représente un composant graphique.
 *
 * @author Sebastien Villemain
 */
public abstract class CompoundPart implements GameProcess {

    /**
	 * Repère de position en X.
	 */
    protected int offsetX = 0;

    /**
	 * Repère de position en Y.
	 */
    protected int offsetY = 0;

    /**
	 * Position X.
	 */
    protected int x = 0;

    /**
	 * Position Y.
	 */
    protected int y = 0;

    /**
	 * Identifiant.
	 */
    protected int id = -1;

    /**
	 * Agent parent attaché.
	 */
    protected BOA parent = null;

    /**
	 * Actionner un objet, une interaction.
	 */
    private InputAction clickInteraction = null;

    /**
	 * Saisir un objet dans la main pour le transporter.
	 * Un second clique pour le lacher dans le monde.
	 */
    private InputAction clickTake = null;

    public CompoundPart(BOA parent, int id, int x, int y) {
        this.parent = parent;
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
	 * Vérifie si l'agent est visible par une caméra distante.
	 *
	 * @return
	 */
    private boolean showOnRemoteCameras() {
        return parent.physics.isCameraShy();
    }

    private boolean canClick() {
        return parent.physics.isActivateable();
    }

    /**
	 * Largeur du composant graphique.
	 *
	 * @return
	 */
    protected abstract int getWidth();

    /**
	 * Hauteur du composant graphique.
	 *
	 * @return
	 */
    protected abstract int getHeight();

    protected void gainFocus() {
        throw new ALifeException("Unable to gain focus.");
    }

    protected void loseFocus() {
        throw new ALifeException("Unable to lose focus.");
    }

    public void loadResources(Resource resourceManager) {
        clickInteraction = new InputAction("clickInteraction", InputAction.Detection.INITAL_PRESS);
        clickTake = new InputAction("clickTake", InputAction.Detection.INITAL_PRESS);
    }

    public void start(InputManager inputManager) {
    }

    public void stop() {
    }

    public void update(long elapsedTime) {
        if (parent.physics.isActivateable()) {
            boolean interaction = clickInteraction.isPressed();
            boolean take = clickTake.isPressed();
            if (interaction || take) {
            }
        }
    }

    public void draw(Graphics2D g, int screenWidth, int screenHeight) {
        if (parent.physics.visible) {
            if (Main.cmdDebugMode) {
                int xoff = (int) (offsetX + parent.physics.x + x);
                int yoff = (int) (offsetY + parent.physics.y + y);
                Color color = g.getColor();
                g.setColor(Color.RED);
                g.drawLine(xoff + (getWidth() / 2), yoff, xoff + getWidth(), yoff + (getHeight() / 2));
                g.drawLine(xoff + getWidth(), yoff + (getHeight() / 2), xoff + (getWidth() / 2), yoff + getHeight());
                g.drawLine(xoff + (getWidth() / 2), yoff + getHeight(), xoff, yoff + (getHeight() / 2));
                g.drawLine(xoff, yoff + (getHeight() / 2), xoff + (getWidth() / 2), yoff);
                g.setColor(color);
            }
        }
    }
}
