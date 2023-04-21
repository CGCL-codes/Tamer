package net.sourceforge.strategema.games;

import net.sourceforge.strategema.games.actions.AcceptStakes;
import net.sourceforge.strategema.games.actions.AcceptTradeAction;
import net.sourceforge.strategema.games.actions.AlliedAction;
import net.sourceforge.strategema.games.actions.AnswerAction;
import net.sourceforge.strategema.games.actions.AttackAction;
import net.sourceforge.strategema.games.actions.BidAction;
import net.sourceforge.strategema.games.actions.BuzzAction;
import net.sourceforge.strategema.games.actions.CaptureAction;
import net.sourceforge.strategema.games.actions.ChoiceAction;
import net.sourceforge.strategema.games.actions.DeclareCheatAction;
import net.sourceforge.strategema.games.actions.DiscardItemAction;
import net.sourceforge.strategema.games.actions.DrawCardAction;
import net.sourceforge.strategema.games.actions.GameEngineAction;
import net.sourceforge.strategema.games.actions.JoinGameAction;
import net.sourceforge.strategema.games.actions.MoveAction;
import net.sourceforge.strategema.games.actions.OpenAuctionAction;
import net.sourceforge.strategema.games.actions.PlacePieceAction;
import net.sourceforge.strategema.games.actions.PlaceTilesAction;
import net.sourceforge.strategema.games.actions.PromotePieceAction;
import net.sourceforge.strategema.games.actions.QuestionAction;
import net.sourceforge.strategema.games.actions.RemovePieceAction;
import net.sourceforge.strategema.games.actions.RemoveTilesAction;
import net.sourceforge.strategema.games.actions.RequestAllianceAction;
import java.util.Set;

public interface GameActionHandler {

    public boolean acceptStakes(AcceptStakes stakes, VolatileGameState state);

    public boolean acceptTrade(AcceptTradeAction offer, VolatileGameState state);

    public boolean allied(AlliedAction alliance, VolatileGameState state);

    public boolean answer(AnswerAction answer, VolatileGameState state);

    public boolean attack(AttackAction attack, VolatileGameState state);

    /** The set of actions that might possibly succeed. */
    public Set<GameAction.ActionType> availableActions(VolatileGameState state);

    public boolean bid(BidAction bid, VolatileGameState state);

    public boolean buzz(BuzzAction buzz, VolatileGameState state);

    public boolean capture(CaptureAction capture, VolatileGameState state);

    public boolean choice(ChoiceAction choice, VolatileGameState state);

    public boolean declareCheat(DeclareCheatAction cheating, VolatileGameState state);

    public boolean discardItem(DiscardItemAction item, VolatileGameState state);

    public boolean drawCard(DrawCardAction draw, VolatileGameState state);

    public boolean endTurn(VolatileGameState state);

    public boolean gameEngineAction(GameEngineAction action, VolatileGameState state);

    public void initialize(GameEquipment<?> equipment, VolatileGameState state);

    public boolean joinGame(JoinGameAction newPlayer, VolatileGameState state);

    public boolean move(MoveAction move, VolatileGameState state);

    public boolean openAuction(OpenAuctionAction auction, VolatileGameState state);

    public boolean placePiece(PlacePieceAction placement, VolatileGameState state);

    public boolean placeTile(PlaceTilesAction placement, VolatileGameState state);

    public boolean promotePiece(PromotePieceAction promotion, VolatileGameState state);

    public boolean question(QuestionAction question, VolatileGameState state);

    public boolean requestAlliance(RequestAllianceAction negotiation, VolatileGameState state);

    public boolean removePiece(RemovePieceAction removal, VolatileGameState state);

    public boolean removeTiles(RemoveTilesAction removal, VolatileGameState state);

    public void beginTurn(VolatileGameState state);
}
