package client.application.handlers;

import java.util.LinkedList;
import client.domain.game.PDBankCredit;
import client.domain.id.MainIDDestributor;
import common.protocol.IMessageConstants;
import common.protocol.IMessageDispatcher;
import common.protocol.IMessageHandler;
import common.protocol.Message;
import common.protocol.MsgField;

public class BankInfoHandler implements IMessageHandler, IMessageConstants {

    public void newConnection(IMessageDispatcher _objDisp, int _iID) {
    }

    public void lostConnection(IMessageDispatcher _objDisp, int _iID) {
    }

    public void handle(IMessageDispatcher _objDisp, Message _objMsg) {
        LinkedList objMsgFields = _objMsg.getFields();
        MsgField objMsgField;
        MainIDDestributor objIDDest = MainIDDestributor.getInstance();
        int iMaxAvailableCredit = -1;
        float fInterest = -1;
        while (objMsgFields.isEmpty() == false) {
            objMsgField = (MsgField) objMsgFields.removeFirst();
            try {
                if (objMsgField.getFieldName().equals(FLD_BANK_INFO_INTEREST)) fInterest = Float.parseFloat(objMsgField.getFieldData());
                if (objMsgField.getFieldName().equals(FLD_BANK_INFO_MAX_AVAILABLE_CREDIT)) iMaxAvailableCredit = Integer.parseInt(objMsgField.getFieldData());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        PDBankCredit.getInstance().setInterest(fInterest);
        PDBankCredit.getInstance().setMaxCredit(iMaxAvailableCredit);
    }
}
