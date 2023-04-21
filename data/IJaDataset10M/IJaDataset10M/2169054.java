package com.atolsystems.singleInstance;

import com.atolsystems.atolutilities.EnumInputDialog;
import com.atolsystems.singleInstance.SingleInstanceServer.ThreadingPolicy;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author atolsystems
 */
public class SingleInstanceTestApp implements SingleInstanceApp {

    private static int port = 42533;

    SingleInstanceServer singleInstance = new SingleInstanceServer(this, port);

    public static void main(String[] args) {
        SingleInstanceTestApp app = new SingleInstanceTestApp();
        app.singleInstance.main(args);
    }

    public String processArgs(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            if (arg.equals("shutdownServer")) singleInstance.stopServerAtExit();
            sb.append(arg);
            sb.append(' ');
        }
        return sb.toString();
    }

    public int singleInstanceMain(String[] args) {
        System.out.println("singleInstanceMain starts");
        ThreadingPolicy policy = (ThreadingPolicy) EnumInputDialog.showInputDialog("Choose the threading policy", "Threading policy", ThreadingPolicy.class);
        System.out.println("policy=" + policy);
        singleInstance.setPolicy(policy);
        String msg = processArgs(args);
        String in = JOptionPane.showInputDialog("singleInstanceMain:\n" + "args: " + msg + "\n" + "Enter exit code of first invocation:");
        int retVal;
        try {
            retVal = Integer.parseInt(in);
        } catch (Throwable e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
            retVal = -1;
        }
        System.out.println("singleInstanceMain ends, returning " + retVal);
        return retVal;
    }

    private Integer newInvocationCnt = 0;

    public int newInvocation(String[] args) {
        int invocationNumber;
        synchronized (newInvocationCnt) {
            newInvocationCnt++;
            invocationNumber = newInvocationCnt;
        }
        String msg = processArgs(args);
        JOptionPane.showMessageDialog(null, msg, "Invocation #" + invocationNumber, JOptionPane.INFORMATION_MESSAGE);
        return invocationNumber;
    }
}
