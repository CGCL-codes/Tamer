package net.sf.freecol.common.networking;

import net.sf.freecol.common.model.Game;
import net.sf.freecol.common.model.Goods;
import net.sf.freecol.common.model.IndianSettlement;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.common.model.Settlement;
import net.sf.freecol.common.model.Unit;
import net.sf.freecol.server.FreeColServer;
import net.sf.freecol.server.model.ServerPlayer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The message sent when negotiating a sale at an IndianSettlement.
 */
public class SellPropositionMessage extends DOMMessage {

    /**
     * The ID of the unit that is selling.
     */
    private String unitId;

    /**
     * The ID of the settlement that is buying.
     */
    private String settlementId;

    /**
     * The goods to be sold.
     */
    private Goods goods;

    /**
     * The price being negotiated.
     */
    private String goldString;

    /**
     * Create a new <code>SellPropositionMessage</code>.
     *
     * @param unit The <code>Unit</code> that is trading.
     * @param goods The <code>Goods</code> to sell.
     * @param gold The price of the goods (negative if unknown).
     */
    public SellPropositionMessage(Unit unit, Settlement settlement, Goods goods, int gold) {
        this.unitId = unit.getId();
        this.settlementId = settlement.getId();
        this.goods = goods;
        this.goldString = Integer.toString(gold);
    }

    /**
     * Create a new <code>SellPropositionMessage</code> from a
     * supplied element.
     *
     * @param game The <code>Game</code> this message belongs to.
     * @param element The <code>Element</code> to use to create the message.
     */
    public SellPropositionMessage(Game game, Element element) {
        this.unitId = element.getAttribute("unit");
        this.settlementId = element.getAttribute("settlement");
        this.goldString = element.getAttribute("gold");
        this.goods = new Goods(game, DOMMessage.getChildElement(element, Goods.getXMLElementTagName()));
    }

    /**
     * What is the price currently negotiated for this transaction?
     *
     * @return The current price.
     */
    public int getGold() {
        try {
            return Integer.parseInt(goldString);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Handle a "sellProposition"-message.
     *
     * @param server The <code>FreeColServer</code> handling the message.
     * @param player The <code>Player</code> the message applies to.
     * @param connection The <code>Connection</code> message was received on.
     *
     * @return This message with updated gold value,
     *         or an error <code>Element</code> on failure.
     */
    public Element handle(FreeColServer server, Player player, Connection connection) {
        ServerPlayer serverPlayer = server.getPlayer(connection);
        Unit unit;
        IndianSettlement settlement;
        try {
            unit = server.getUnitSafely(unitId, serverPlayer);
            settlement = server.getAdjacentIndianSettlementSafely(settlementId, unit);
        } catch (Exception e) {
            return DOMMessage.clientError(e.getMessage());
        }
        if (goods.getLocation() != unit) {
            return DOMMessage.createError("server.trade.noGoods", "Goods " + goods.getId() + " are not with unit " + unitId);
        }
        int gold;
        try {
            gold = Integer.parseInt(goldString);
        } catch (NumberFormatException e) {
            return DOMMessage.clientError("Bad gold: " + goldString);
        }
        return server.getInGameController().sellProposition(serverPlayer, unit, settlement, goods, gold);
    }

    /**
     * Convert this SellPropositionMessage to XML.
     *
     * @return The XML representation of this message.
     */
    public Element toXMLElement() {
        Element result = createNewRootElement(getXMLElementTagName());
        Document doc = result.getOwnerDocument();
        result.setAttribute("unit", unitId);
        result.setAttribute("settlement", settlementId);
        result.appendChild(goods.toXMLElement(null, doc));
        result.setAttribute("gold", goldString);
        return result;
    }

    /**
     * The tag name of the root element representing this object.
     *
     * @return "sellProposition".
     */
    public static String getXMLElementTagName() {
        return "sellProposition";
    }
}
