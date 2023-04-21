package scamsoft.squadleader.rules.persistence;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Elements;
import scamsoft.squadleader.rules.Game;
import scamsoft.squadleader.rules.Player;
import scamsoft.squadleader.rules.PlayerState;

/**
 * User: Andreas Mross
 * Date: 06-Jul-2007
 * Time: 18:26:54
 */
public class PlayerStateBuilder extends AbstractBuilder {

    static final String PLAYER = "player";

    private static final String READY_FOR_NEXT_PHASE = "readyForNextPhase";

    private static final String SMOKE_USED = "smokeUsed";

    static final String PLAYER_STATE = "playerState";

    private static final String CURRENT_STACK = "currentStack";

    public PlayerStateBuilder(PersistenceRegistry registry) {
        super(registry);
    }

    public Element toXML(PlayerState playerState) {
        Element result = new Element(PLAYER_STATE);
        result.addAttribute(new Attribute(PLAYER, Integer.toString(playerState.getPlayer().getID())));
        Util.addIntegerValueNode(result, SMOKE_USED, playerState.getSmokeUsed());
        Util.addBooleanNode(result, READY_FOR_NEXT_PHASE, playerState.isReadyForNextPhase());
        Util.addIntegerValueNode(result, CURRENT_STACK, playerState.getCurrentStack() + 1);
        return result;
    }

    public PlayerState fromXML(Element element, Game game) {
        Player player = game.getPlayer(Integer.parseInt(element.getAttributeValue(PLAYER)));
        PlayerState result = new PlayerState(player);
        Elements elements = element.getChildElements();
        for (int i = 0; i < elements.size(); i++) {
            Element nextElement = elements.get(i);
            String name = nextElement.getLocalName();
            if (name.equals(INTEGER_VALUE)) {
                super.setIntegerValue(result, nextElement, PlayerState.class);
            } else if (name.equals(TRUE_BOOLEAN)) {
                setBooleanValueTrue(result, nextElement, PlayerState.class);
            }
        }
        return result;
    }
}
