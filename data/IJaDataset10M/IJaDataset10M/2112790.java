package org.oobench.rmi;

import java.rmi.*;
import java.net.*;
import org.oobench.common.*;

public class RMIBenchmark extends AbstractBenchmark {

    private static int minorNumber;

    public ClientInterface lookup() throws RemoteException, MalformedURLException, NotBoundException {
        return (ClientInterface) Naming.lookup("/Server");
    }

    public int getMajorNumber() {
        return 12;
    }

    public int getMinorNumber() {
        return minorNumber;
    }

    public void simple(ClientInterface remoteObject, int count) throws RemoteException {
        int counter = 0;
        reset();
        for (int i = 0; i < count; ++i) {
            counter = remoteObject.incrementCounter(counter);
            proceed();
        }
        reset();
    }

    public void test(int count, String description) {
        count = getCountWithDefault(count);
        System.out.println("*** Testing RMI (" + description + ")");
        for (int c = 0; c < 10; c++) {
            if (c == 9) {
                beginAction(1, "lookup", 1);
            }
            ClientInterface remoteObject = null;
            try {
                remoteObject = lookup();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (c == 9) {
                endAction();
                beginAction(2, "simple", count);
            }
            try {
                simple(remoteObject, count);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            if (c == 9) {
                endAction();
            }
        }
    }

    public static void testRMI(int count, String description, String[] args, int theMinorNunber) {
        minorNumber = theMinorNunber;
        RMIBenchmark bench = new RMIBenchmark();
        bench.setArguments(args);
        bench.test(count, description);
    }
}
