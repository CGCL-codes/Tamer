package test;

import basys.eib.EIBAddress;
import basys.eib.EIBConnection;
import basys.eib.EIBFrame;
import basys.eib.EIBFrameListener;
import basys.eib.EIBGrpaddress;
import basys.eib.EIBPhaddress;
import basys.eib.KNXnetIPConnection;
import basys.eib.event.EIBFrameEvent;
import basys.eib.exceptions.EIBAddressFormatException;
import basys.eib.exceptions.EIBConnectionNotPossibleException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import tuwien.auto.eibxlator.PDUXlatorList;
import tuwien.auto.eibxlator.PointPDUXlator;
import tuwien.auto.eibxlator.PointPDUXlator_1;
import tuwien.auto.eibxlator.PointPDUXlator_2ByteFloat;
import tuwien.auto.eibxlator.PointPDUXlator_Boolean;
import tuwien.auto.eicl.util.EICLException;

/**
 *
 * @author ELR
 */
public class Prueba {

    public static void main(String args[]) {
        EIBConnection connection = KNXnetIPConnection.getEIBConnection();
        try {
            connection.connect("192.168.100.37");
            int[] data = { 0 };
            EIBFrame eibframe = new EIBFrame(false, 0, new EIBPhaddress(1, 1, 1), new EIBGrpaddress("2/0/0"), 6, 0, 0x80, data);
            eibframe.setAPCI(0);
            eibframe.setPriority(3);
            connection.addEIBFrameListener(new handler());
            Thread.sleep(20000);
            connection.disconnect();
            PointPDUXlator dimVal = PDUXlatorList.getPointPDUXlator(PDUXlatorList.TYPE_BOOLEAN[0], PointPDUXlator_Boolean.DPT_OPENCLOSE[0]);
        } catch (InterruptedException ex) {
            Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EICLException ex) {
            Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EIBAddressFormatException ex) {
            connection.disconnect();
            Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EIBConnectionNotPossibleException ex) {
            connection.disconnect();
            Logger.getLogger(Prueba.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class handler implements EIBFrameListener {

    public void frameReceived(EIBFrameEvent fe) {
        try {
            PointPDUXlator dimVal = PDUXlatorList.getPointPDUXlator(PDUXlatorList.TYPE_BOOLEAN[0], PointPDUXlator_Boolean.DPT_OPENCLOSE[0]);
            EIBFrame ei = fe.getEIBFrame();
            int[] aux = fe.getEIBFrame().getApdata();
            byte[] aux2 = new byte[aux.length + 1];
            aux2[0] = 0;
            aux2[1] = (byte) (aux[0] - 128);
            int j = 2;
            for (int i = 1; i < aux.length; i++) {
                aux2[j] = (byte) aux[i];
                j++;
            }
            dimVal.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
            dimVal.setAPDUByteArray(aux2);
            System.out.println(dimVal.getASDUasString());
        } catch (EICLException ex) {
            Logger.getLogger(handler.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}
