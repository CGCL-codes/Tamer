package jelb.messaging;

import jelb.netio.Message;
import jelb.netio.Uint8;

public class GetTradeReject implements IMessage {

    private Uint8 who;

    public Uint8 getWho() {
        return who;
    }

    public byte getType() {
        return jelb.netio.Protocol.GET_TRADE_REJECT;
    }

    public void init(Message rawMessage) {
        this.who = rawMessage.getUint8(0);
    }
}
