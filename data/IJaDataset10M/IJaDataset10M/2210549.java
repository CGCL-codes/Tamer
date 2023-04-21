package per.test.samples;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Hello extends UnicastRemoteObject implements HelloInterface {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7689493773186823295L;

    private String message;

    public Hello(String msg) throws RemoteException {
        message = msg;
    }

    /**  
	 * 远程接口方法的实现  
	 */
    public String say() throws RemoteException {
        System.out.println("Called by HelloClient");
        return message;
    }
}
