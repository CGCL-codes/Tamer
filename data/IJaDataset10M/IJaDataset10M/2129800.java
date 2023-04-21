package scamsoft.squadleader.rules.persistence;

import nu.xom.Element;
import scamsoft.squadleader.rules.Game;
import scamsoft.squadleader.rules.Location;
import scamsoft.squadleader.rules.Player;
import scamsoft.squadleader.rules.UnitGroup;
import scamsoft.squadleader.rules.orders.Move;

/**
 * User: Andreas Mross
 * Date: 04-Jul-2007
 * Time: 08:02:07
 */
public class MoveBuilder extends AbstractOrderBuilder {

    static final String ORIGIN = "origin";

    static final String MOVE_TARGET = "moveTarget";

    static final String UNITS = "units";

    static final String MOVE = "move";

    public MoveBuilder(PersistenceRegistry registry) {
        super(registry);
    }

    public Element toXML(Move move) {
        Element result = new Element(MOVE);
        addHeader(result, move.getPlayer());
        Element startLocation = new Element(ORIGIN);
        startLocation.appendChild(LocationBuilder.getCoordinateNode((Location) Util.getPrivateField(move, ORIGIN, Move.class)));
        result.appendChild(startLocation);
        Element moveTarget = new Element(MOVE_TARGET);
        moveTarget.appendChild(LocationBuilder.getCoordinateNode(move.getTarget()));
        result.appendChild(moveTarget);
        Element units = new Element(UNITS);
        units.appendChild(getUnitGroupNode((UnitGroup) Util.getPrivateField(move, UNITS, Move.class)));
        result.appendChild(units);
        return result;
    }

    public Move fromXML(Element element, Game game) {
        Player player = super.getPlayer(element, game);
        Location origin = LocationBuilder.getLocation(game.getMap(), element.getFirstChildElement(ORIGIN));
        Location target = LocationBuilder.getLocation(game.getMap(), element.getFirstChildElement(MOVE_TARGET));
        UnitSource unitSource = new CounterGroupUnitSource(game.getUnits());
        UnitGroup units = super.createUnitGroup(element.getFirstChildElement(UNITS), unitSource);
        Move move = new Move(player, origin, target, units);
        return move;
    }
}
