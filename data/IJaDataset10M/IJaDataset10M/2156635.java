package scamsoft.squadleader.rules.orders;

import java.io.Serializable;
import scamsoft.squadleader.rules.GameState;
import scamsoft.squadleader.rules.Location;
import scamsoft.squadleader.rules.MistakeInOrderException;
import scamsoft.squadleader.rules.OrderResult;
import scamsoft.squadleader.rules.Phase;
import scamsoft.squadleader.rules.Radio;

/**
 * User: Andreas Mross
 * Date: May 17, 2003
 * Time: 3:39:29 PM
 */
public class CorrectArtillery extends AbstractOrder implements Serializable {

    private Radio radio;

    private Location target;

    /**
	 * Create an order to change the artillery's target.
	 * @param radio The radio to retarget
	 * @param target The Location to target
	 */
    public CorrectArtillery(Radio radio, Location target) {
        super(radio.getOwner());
        if (target == null) throw new NullPointerException("Target");
        this.radio = radio;
        this.target = target;
    }

    public String getDescription() {
        return "Correct artillery to " + target;
    }

    public void execute(GameState gameState) throws MistakeInOrderException {
        checkLegal(gameState);
        Radio actualRadio = (Radio) gameState.getUnits().getByID(radio);
        Location actualTarget = gameState.getLocation(target);
        actualRadio.setTarget(actualTarget);
    }

    public OrderResult checkResult(GameState gameState) {
        if (gameState.getGametime().getPhase() != Phase.RALLY_PHASE) {
            return new OrderResult("Artillery can only be corrected during the Rally Phase");
        }
        return OrderResult.OK();
    }

    static final long serialVersionUID = -5230153443929350508L;
}
