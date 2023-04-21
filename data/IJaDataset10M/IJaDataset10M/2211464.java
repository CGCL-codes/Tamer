package server.messaging.handler;

import server.pd.player.Player;
import common.protocol.Message;
import common.protocol.IMessageDispatcher;

public class GameListHandler extends ServerMessageHandler {

    public void handle(Player _objSource, IMessageDispatcher _objDisp, Message _objMessage) {
        reject(_objDisp, _objMessage, DATA_REJECT_REASON_SERVICE_NOT_IMPLEMENTED, NOT_IMPLEMENTED);
    }
}
