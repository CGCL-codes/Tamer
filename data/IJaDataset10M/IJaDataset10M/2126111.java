package customer.core.ctrl;

import src.util.Message;
import src.util.MessageCfg;
import src.util.ServerIo;

public class LifeCheckIo extends ServerIo {

    public boolean checkServer() {
        new Message(new MessageCfg(true, false));
        message = new Message("/lifeCheck", String.class);
        sendReceive();
        return message.getStatus() == 200;
    }
}
