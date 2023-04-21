package server.messaging;

import java.util.Observable;
import common.protocol.IMessageDispatcher;
import common.protocol.Message;
import common.protocol.SendMessage;
import server.pd.map.Entrance;
import server.pd.map.Hotel;
import server.pd.map.StreetField;

public class EntranceMessager implements IMessager {

    private int iStreetFieldSuffix;

    private int iHotelSuffix;

    private int iGameNr;

    private Entrance objEntrance;

    public EntranceMessager(Entrance _objEntrance, int _iGameNr) {
        iGameNr = _iGameNr;
        Hotel objHotel = _objEntrance.getHotel();
        if (objHotel == null) {
            System.out.println("EntranceNr: " + _objEntrance.getSuffix() + "\nFieldID: " + _objEntrance.getStreetField().getSuffix());
        } else {
            iHotelSuffix = objHotel.getSuffix();
        }
        StreetField objStreetField = _objEntrance.getStreetField();
        if (objStreetField == null) {
            System.out.println("EntranceNr: " + _objEntrance.getSuffix() + "\nHotelID: " + _objEntrance.getHotel().getSuffix());
        } else {
            iStreetFieldSuffix = objStreetField.getSuffix();
        }
        objEntrance = _objEntrance;
        _objEntrance.addObserver(this);
    }

    public void send(int _iReceiver, IMessageDispatcher _objMsgDisp) {
        Message objMsg = new SendMessage(_iReceiver, MSG_ENTRANCE_STATUS);
        objMsg.addField(FLD_ENTRANCE_STATUS_FIELD_ID, String.valueOf(iStreetFieldSuffix));
        objMsg.addField(FLD_ENTRANCE_STATUS_HOTEL_ID, String.valueOf(iHotelSuffix));
        objMsg.addField(FLD_ENTRANCE_STATUS_BUILT, String.valueOf(objEntrance.isBuilt()));
        _objMsgDisp.dispatchOut(objMsg);
    }

    public void update(Observable _objObserv, Object _obj) {
        SendMessageList.getInstance().add(this, iGameNr);
    }
}
