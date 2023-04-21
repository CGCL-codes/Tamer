package scamsoft.squadleader.rules.orders;

import java.io.Serializable;
import scamsoft.squadleader.rules.GameState;
import scamsoft.squadleader.rules.MistakeInOrderException;
import scamsoft.squadleader.rules.OrderResult;
import scamsoft.squadleader.rules.Phase;
import scamsoft.squadleader.rules.Rotatable;
import scamsoft.squadleader.rules.Unit;

/**
 * User: Andreas Mross
 * Date: May 17, 2003
 * Time: 10:08:24 AM
 */
public class RotateAntiClockwise extends AbstractOrder implements Serializable {

    private Rotatable rotator;

    public RotateAntiClockwise(Rotatable rotatable) {
        super(rotatable.getOwner());
        this.rotator = rotatable;
    }

    public String getDescription() {
        return "Rotate " + rotator + " anti-clockwise";
    }

    public void execute(GameState gameState) throws MistakeInOrderException {
        Unit unit = (Unit) rotator;
        Unit actualUnit = gameState.getUnits().getByID(unit);
        Rotatable rotatable = (Rotatable) actualUnit;
        rotatable.rotateAntiClockwise();
    }

    public OrderResult checkResult(GameState gameState) {
        if (!(gameState.getPhase() == Phase.SETUP_PHASE || (gameState.getGametime().getPhasingSide() == getPlayer().getSide() && gameState.getPhase() == Phase.MOVEMENT_PHASE))) {
            return new OrderResult("You can only rotate a unit during your movement phase");
        }
        if (!rotator.isRotatable()) {
            return new OrderResult("This unit cannot rotate");
        }
        return new OrderResult(true);
    }

    static final long serialVersionUID = 5427146881671558356L;
}
