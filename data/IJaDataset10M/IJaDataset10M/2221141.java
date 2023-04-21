package org.amse.bomberman.server.net.tcpimpl.sessions.control;

import java.util.List;
import org.amse.bomberman.protocol.InvalidDataException;

/**
 *
 * @author Kirilchuk V.E
 */
public interface RequestExecutor {

    void sendGames() throws InvalidDataException;

    void tryCreateGame(List<String> args) throws InvalidDataException;

    void tryJoinGame(List<String> args) throws InvalidDataException;

    void tryDoMove(List<String> args) throws InvalidDataException;

    void sendGameMapInfo() throws InvalidDataException;

    void tryStartGame() throws InvalidDataException;

    void tryLeave() throws InvalidDataException;

    void tryPlaceBomb() throws InvalidDataException;

    void sendDownloadingGameMap(List<String> args) throws InvalidDataException;

    void sendGameStatus() throws InvalidDataException;

    void sendGameMapsList() throws InvalidDataException;

    void tryAddBot(List<String> args) throws InvalidDataException;

    void sendGameInfo() throws InvalidDataException;

    void addMessageToChat(List<String> args) throws InvalidDataException;

    void sendNewMessagesFromChat() throws InvalidDataException;

    void tryKickPlayer(List<String> args) throws InvalidDataException;

    void sendGamePlayersStats() throws InvalidDataException;

    void setClientNickName(List<String> args) throws InvalidDataException;
}
