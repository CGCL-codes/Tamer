package daemon;

import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.Scanner;
import org.apache.axis2.AxisFault;
import daemon.ServerSendXMLStub;
import daemon.ServerSendXMLStub.SendXML;
import daemon.ServerSendXMLStub.SendXMLResponse;

public class WSClient {

    public int sendXML(String fileName) {
        try {
            Scanner in = new Scanner(new FileReader(fileName));
            String conteudoArquivo = "";
            do {
                if (in.hasNextLine() == false) break;
                conteudoArquivo += (in.nextLine() + "\n");
            } while (true);
            ServerSendXMLStub stub = new ServerSendXMLStub();
            SendXML sxml = new ServerSendXMLStub.SendXML();
            sxml.setXML(conteudoArquivo);
            sxml.setUsuario("luizgrs");
            sxml.setSenha("luiz");
            SendXMLResponse res = stub.SendXML(sxml);
            String result = res.get_return();
            if (result.compareTo("OK") == 0) return 1;
        } catch (AxisFault e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 4;
    }
}
