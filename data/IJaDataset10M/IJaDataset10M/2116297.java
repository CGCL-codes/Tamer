package test;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import org.zkoss.eclipse.rpc.Callback;
import org.zkoss.eclipse.rpc.Const;
import org.zkoss.eclipse.rpc.Instruction;
import org.zkoss.eclipse.rpc.LocalRmiServer;
import org.zkoss.eclipse.rpc.RmiReceiver;
import org.zkoss.eclipse.rpc.ChannelEventHandler;
import org.zkoss.eclipse.rpc.RmiChannel;
import org.zkoss.eclipse.rpc.RmiServices;
import org.zkoss.eclipse.rpc.impl.NonBlockingReceiverImpl;
import org.zkoss.eclipse.rpc.impl.RmiChannelImpl;
import org.zkoss.eclipse.rpc.impl.RmiRegistryServerImpl;

/**
 * @author Ian Tsai
 *
 */
public class RmiServer_TEST {

    /**
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        run_TEST();
    }

    /**
	 * 
	 */
    public static void run_TEST() {
        try {
            NonBlockingReceiverImpl nonBlocking;
            RmiChannel channel = RmiServices.initMainRmiChannelService(nonBlocking = new NonBlockingReceiverImpl(new RmiReceiver() {

                public void receive(Serializable instruction) {
                    Instruction ins = (Instruction) instruction;
                    String signature = ins.getSignature();
                    String[] args = (String[]) ins.getArguments();
                    StringBuffer sb = new StringBuffer("[");
                    for (String st : args) sb.append(st).append(",");
                    sb.append("]");
                    System.out.println("On receive instruction!!!");
                    System.out.println("Signature=" + signature);
                    System.out.println("Arguments=" + sb);
                    System.out.println("Do Callback...");
                    ins.doCallback(args, null);
                }
            }));
            System.out.println("RMI Server is ready to listen");
            Instruction ins = new Instruction(new Callback() {

                public void doCallback(Serializable rs, Throwable ex) throws Exception {
                    System.out.println("Callback on Fire!!!");
                }
            });
            ins.setArguments(new String[] { "Alpha", "Bravo", "Charli", "Delta", "Echo" });
            ins.setSignature("test test!!!");
            ins.send(RmiServices.getCurrentRmiChannel());
            while (true) Thread.sleep(3000);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
