package edu.bsu.monopoly.game.player;

import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import com.sun.corba.se.pept.transport.Acceptor;
import edu.bsu.monopoly.game.GameCommands;
import edu.bsu.monopoly.game.GameStatus;
import edu.bsu.monopoly.game.MonopdGame;
import edu.bsu.monopoly.game.actions.AuctionManager;
import edu.bsu.monopoly.game.actions.Command;
import edu.bsu.monopoly.game.actions.PropertyAction;
import edu.bsu.monopoly.scripts.MonopdAI;
import edu.bsu.monopoly.trade.AcceptTrade;
import edu.bsu.monopoly.trade.CardTrade;
import edu.bsu.monopoly.trade.MoneyTrade;
import edu.bsu.monopoly.trade.Trade;
import edu.bsu.monopoly.trade.TradePlayerList;
import edu.bsu.monopoly.trade.TradeTerm;
import edu.bsu.monopoly.util.Console;

public class ArtificialPlayer extends Player {

    private MonopdAI ai;

    private MonopdGame game;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public ArtificialPlayer(Attributes atts) {
        super(atts);
    }

    public ArtificialPlayer(Player p) {
        super(p);
    }

    public ArtificialPlayer(int playerID, String name, int gameID) {
        super(playerID, name, gameID);
    }

    public void handleDebt(GameStatus status, MonopdGame game, int amount) {
        ai.inDebt(status, amount, this);
    }

    public void trade(GameStatus status, MonopdGame game, Trade trade) {
        TradePlayerList others = new TradePlayerList();
        for (TradePlayer p : trade.tradePlayers()) {
            if (!trade.getTradePlayerMe().equals(p)) {
                others.add(p);
            }
        }
        ai.trade(status, trade, trade.getTradePlayerMe(), others);
    }

    private boolean preRollCompleted = false;

    protected void update(GameStatus status, MonopdGame game) {
        if (!inDebt()) {
            this.game = game;
            assert ai != null;
            if (AuctionManager.instance().currentAuction() != null) {
                log.info(AuctionManager.instance().currentAuction().highestBidder() + " " + id());
                if (!AuctionManager.instance().currentAuction().highestBidder().equals(this)) {
                    game.action(ai.auctionHouse(AuctionManager.instance().currentAuction(), status, this));
                }
            }
            if (AuctionManager.instance().currentAuction() == null) {
                if (hasTurn() && canRoll() && !preRollCompleted) {
                    Command command = ai.preRollTurn(status, this);
                    if (command != null) {
                        game.action(command);
                        preRollCompleted = true;
                    }
                }
                if (location().taxAmount() != 0 && location().taxPercentage() > 0) {
                    game.action(ai.payTax(status));
                }
                if (inJail() && hasTurn()) {
                    game.action(ai.inTheClink(status));
                }
                if (canRoll() && hasTurn()) {
                    log.info("rolling now");
                    game.action(GameCommands.ROLL_DICE);
                    if (!canRoll()) preRollCompleted = false;
                }
                if (canBuyEstate() || canAuction()) {
                    game.action(ai.buyOrAuctionProperty(status, this));
                }
            }
        }
    }

    public void addAI(MonopdAI l) {
        assert l != null;
        this.ai = l;
    }
}
