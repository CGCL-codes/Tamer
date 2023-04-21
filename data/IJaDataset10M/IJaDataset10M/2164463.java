package ar.edu.unicen.exa.server.communication.processors;

import ar.edu.unicen.exa.server.player.Player;
import com.sun.sgs.app.AppContext;
import common.elearning.event.ElearningResponseEvent;
import common.messages.IMessage;
import common.messages.responses.MsgElearningResponse;
import common.processors.IProcessor;

/**
 * Esta clase procesa los mensajes que llegan de un cliente darkstar que
 * funciona como servidor de aplicaciones
 */
public class PELearningResponseMessage extends ServerMsgProcessor {

    /**
	 * Instantiates a new pE learning response message.
	 */
    public PELearningResponseMessage() {
        super();
    }

    /**
	 * (non-Javadoc)
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
    @Override
    public IProcessor factoryMethod() {
        return new PELearningResponseMessage();
    }

    /**
	 * (non-Javadoc)
	 * 
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 * 
	 *      Envia el mensaje al cliente que tiene que procesarlo
	 */
    @Override
    public void process(IMessage msg) {
        ElearningResponseEvent eventResponse = ((MsgElearningResponse) msg).getResponse();
        Player player = getClientPlayer(eventResponse.getSession());
        try {
            player.send(msg);
        } catch (Exception e) {
            System.out.println("No SE PUDO MANDAR EL MENSAJE A " + eventResponse.getSession());
            e.printStackTrace();
        }
    }

    private Player getClientPlayer(String session) {
        return (Player) AppContext.getDataManager().getBinding(session);
    }
}
