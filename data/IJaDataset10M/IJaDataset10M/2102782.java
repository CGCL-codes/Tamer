package scamsoft.squadleader.client.orders;

import scamsoft.squadleader.client.ClientOrder;
import scamsoft.squadleader.client.UserFeedback;
import scamsoft.squadleader.client.UIState;
import scamsoft.squadleader.client.SLCursor;
import scamsoft.squadleader.rules.GameState;
import scamsoft.squadleader.rules.Order;
import scamsoft.squadleader.rules.OrderResult;
import scamsoft.squadleader.rules.Location;
import java.awt.*;

/**
 * User: Andreas Mross
 * Date: 24/09/2007
 * Time: 12:02:00
 */
public class FireSmallArms implements ClientOrder {

    public UserFeedback getUserFeedback(UIState ui, GameState game, Location targetLocation) {
        Order order = new scamsoft.squadleader.rules.orders.FireSmallArms(ui.getPlayer(), game.getGametime(), ui.getSelectedUnits(), targetLocation, game);
        OrderResult result = order.checkResult(game);
        Cursor cursor = result.isLegal() ? SLCursor.TARGET : SLCursor.NA;
        UserFeedback feeback = new UserFeedback(cursor, order, result);
        return feeback;
    }

    public void paintOrder(Graphics2D g, UIState ui, GameState game) {
    }
}
