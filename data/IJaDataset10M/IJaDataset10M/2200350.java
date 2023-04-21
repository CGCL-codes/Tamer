package server.messaging.handler;

import java.util.LinkedList;
import server.pd.id.IIDTypeConstants;
import server.pd.id.MainIDDestributor;
import server.pd.map.Hotel;
import server.pd.player.ActionNotAllowedException;
import server.pd.player.AllBuildingsBuiltException;
import server.pd.player.HotelNotAvailableException;
import server.pd.player.Player;
import common.protocol.Message;
import common.protocol.IMessageDispatcher;
import common.protocol.MsgField;

public class FreeBuildPhaseHandler extends ServerMessageHandler {

    public void handle(Player _objSource, IMessageDispatcher _objDisp, Message _objMessage) {
        MsgField objMsgField;
        String szMsgFldName;
        Hotel objHotel = null;
        int iHotelID = -1;
        LinkedList objMsgFields = _objMessage.getFields();
        MainIDDestributor objMainIDDest = MainIDDestributor.getInstance();
        while (objMsgFields.isEmpty() == false) {
            objMsgField = (MsgField) objMsgFields.removeFirst();
            szMsgFldName = objMsgField.getFieldName();
            if (szMsgFldName.equals(FLD_FREE_BUILDPHASE_HOTEL_ID)) {
                iHotelID = Integer.parseInt(objMsgField.getFieldData());
            }
        }
        if (iHotelID == -1) {
            reject(_objDisp, _objMessage, DATA_REJECT_REASON_MISSING_FIELD, FLD_FREE_BUILDPHASE_HOTEL_ID);
            return;
        }
        objHotel = (Hotel) objMainIDDest.getIDObject(iHotelID, IIDTypeConstants.HOTEL, _objSource.getGameNr());
        if (objHotel == null) {
            reject(_objDisp, _objMessage, DATA_REJECT_REASON_HOTEL_NOT_EXIST, HOTEL_NOT_EXIST);
            return;
        }
        try {
            _objSource.buildFreeBuilding(objHotel);
        } catch (ActionNotAllowedException e) {
            reject(_objDisp, _objMessage, DATA_REJECT_ACTION_NOT_ALLOWED, e.getMessage());
            return;
        } catch (HotelNotAvailableException e) {
            reject(_objDisp, _objMessage, DATA_REJECT_REASON_HOTEL_NOT_EXIST, HOTEL_NOT_EXIST);
            return;
        } catch (AllBuildingsBuiltException e) {
            reject(_objDisp, _objMessage, DATA_REJECT_REASON_ALL_BUILDINGS_BUILT, ALL_BUILDINGS_BUILT);
            return;
        }
    }
}
