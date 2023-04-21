package server.messaging;

import java.util.Observable;
import common.protocol.IMessageDispatcher;
import common.protocol.Message;
import common.protocol.SendMessage;
import server.pd.map.Building;

public class BuildingMessager implements IMessager {

    private Building objBuilding;

    private int iGameNr;

    public BuildingMessager(Building _objBuilding, int _iGameNr) {
        iGameNr = _iGameNr;
        objBuilding = _objBuilding;
        objBuilding.addObserver(this);
    }

    public void send(int _iReceiver, IMessageDispatcher _objMsgDisp) {
        Message objMsg = new SendMessage(_iReceiver, MSG_BUILDING_STATUS);
        objMsg.addField(FLD_BUILDING_STATUS_ID, String.valueOf(objBuilding.getSuffix()));
        objMsg.addField(FLD_BUILDING_STATUS_BUILT, String.valueOf(objBuilding.isBuild()));
        _objMsgDisp.dispatchOut(objMsg);
    }

    public void update(Observable _objObserv, Object _obj) {
        SendMessageList.getInstance().add(this, iGameNr);
    }
}
