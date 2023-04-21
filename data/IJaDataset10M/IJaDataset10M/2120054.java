package ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 *
 * @author mariano
 */
@WebService(name = "P2Pws", serviceName = "P2Pws", portName = "P2PPort", targetNamespace = "http://p2p/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class P2PServer {

    private MachineList machineList;

    /** Creates a new instance of P2PServer */
    public P2PServer(MachineList machineList) {
        this.machineList = machineList;
    }

    @WebMethod(operationName = "ping")
    public String ping() {
        return "pong";
    }

    @WebMethod(operationName = "getNodeList")
    public String getMachineList() {
        return machineList.toXmlString();
    }
}
