package bluffinmuffin.protocol.observer.game;

import bluffinmuffin.protocol.commands.game.BetTurnEndedCommand;
import bluffinmuffin.protocol.commands.game.BetTurnStartedCommand;
import bluffinmuffin.protocol.commands.game.GameEndedCommand;
import bluffinmuffin.protocol.commands.game.GameStartedCommand;
import bluffinmuffin.protocol.commands.game.PlayerHoleCardsChangedCommand;
import bluffinmuffin.protocol.commands.game.PlayerJoinedCommand;
import bluffinmuffin.protocol.commands.game.PlayerLeftCommand;
import bluffinmuffin.protocol.commands.game.PlayerMoneyChangedCommand;
import bluffinmuffin.protocol.commands.game.PlayerTurnBeganCommand;
import bluffinmuffin.protocol.commands.game.PlayerTurnEndedCommand;
import bluffinmuffin.protocol.commands.game.PlayerWonPotCommand;
import bluffinmuffin.protocol.commands.game.TableClosedCommand;
import bluffinmuffin.protocol.commands.game.TableInfoCommand;
import bluffinmuffin.protocol.observer.ICommandListener;

public interface IGameClientListener extends ICommandListener {

    void betTurnEndedCommandReceived(BetTurnEndedCommand command);

    void betTurnStartedCommandReceived(BetTurnStartedCommand command);

    void gameEndedCommandReceived(GameEndedCommand command);

    void gameStartedCommandReceived(GameStartedCommand command);

    void holeCardsChangedCommandReceived(PlayerHoleCardsChangedCommand command);

    void playerJoinedCommandReceived(PlayerJoinedCommand command);

    void playerLeftCommandReceived(PlayerLeftCommand command);

    void playerMoneyChangedCommandReceived(PlayerMoneyChangedCommand command);

    void playerTurnBeganCommandReceived(PlayerTurnBeganCommand command);

    void playerTurnEndedCommandReceived(PlayerTurnEndedCommand command);

    void playerWonPotCommandReceived(PlayerWonPotCommand command);

    void tableClosedCommandReceived(TableClosedCommand command);

    void tableInfoCommandReceived(TableInfoCommand command);
}
