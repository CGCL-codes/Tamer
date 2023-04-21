package ar.edu.unicen.exa.server.communication.processors.social;

import java.io.Serializable;
import ar.edu.unicen.exa.server.communication.processors.PDataManipulation;
import ar.edu.unicen.exa.server.serverLogic.ModelAccess;
import common.datatypes.social.SearchResponse;
import common.messages.requests.MsgGetData;
import common.processors.IProcessor;

/**
 * 
 * @author Gaston Manoli
 *
 */
public class PGetPersistentNotifications extends PDataManipulation {

    @Override
    public Serializable getReturnData(MsgGetData msgGetData) {
        SearchResponse searchResponse = (SearchResponse) this.getData(msgGetData);
        SearchResponse infoToReturn = ModelAccess.getInstance().getSocialEvent(searchResponse);
        return infoToReturn;
    }

    @Override
    public IProcessor factoryMethod() {
        return new PGetPersistentNotifications();
    }
}
