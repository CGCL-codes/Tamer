package client.presentation.map;

import javax.swing.JOptionPane;
import common.protocol.IMessageConstants;
import common.protocol.Message;
import common.protocol.SendMessage;
import client.Client;
import client.domain.game.PDBank;
import client.domain.game.PDHotel;
import client.domain.game.PDHotelRegistry;
import client.domain.game.PDWayField;
import client.presentation.main.ViewMain;

public class FreeBuildPhaseSelectionControllerStrategy implements ISelectionControllerStrategy {

    private boolean blnIsFinished = false;

    public void hotelSelected(PDHotel _objHotel, boolean _blnDoubleClicked) {
        Message objMsg = new SendMessage(Client.getDispatcher().getLastConnetionID(), IMessageConstants.MSG_FREE_BUILDPHASE);
        objMsg.addField(IMessageConstants.FLD_FREE_BUILDPHASE_HOTEL_ID, String.valueOf(_objHotel.getSuffix()));
        Client.getDispatcher().dispatchOut(objMsg);
        blnIsFinished = true;
    }

    public void wayFieldSelected(PDWayField _objWayField, boolean _blnDoubleClicked) {
        JOptionPane.showMessageDialog(ViewMain.getInstance(), "Click on a valid Hotel", "Invalid click", JOptionPane.OK_OPTION);
    }

    public void hotelRegistrySelected(PDHotelRegistry _objHotelRegistry, boolean _blnDoubleClicked) {
        JOptionPane.showMessageDialog(ViewMain.getInstance(), "Click on a valid Hotel", "Invalid click", JOptionPane.OK_OPTION);
    }

    public void bankSelected(PDBank _objBank, boolean _blnDoubleClicked) {
        JOptionPane.showMessageDialog(ViewMain.getInstance(), "Click on a valid Hotel", "Invalid click", JOptionPane.OK_OPTION);
    }

    public boolean isFinished() {
        return blnIsFinished;
    }
}
