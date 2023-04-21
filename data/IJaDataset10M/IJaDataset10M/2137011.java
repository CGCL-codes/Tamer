package navigators.smart.tom.demo.counter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import navigators.smart.tom.ServiceProxy;
import navigators.smart.tom.util.Logger;

/**
 * Example client that updates a BFT replicated service (a counter).
 *
 */
public class CounterClient {

    @SuppressWarnings("static-access")
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java ...CounterClient <process id> <increment> [<number of operations>]");
            System.out.println("       if <increment> equals 0 the request will be read-only");
            System.out.println("       default <number of operations> equals 1000");
            System.exit(-1);
        }
        ServiceProxy counterProxy = new ServiceProxy(Integer.parseInt(args[0]));
        int result = 0;
        try {
            int inc = Integer.parseInt(args[1]);
            int numberOfOps = (args.length > 2) ? Integer.parseInt(args[2]) : 1000;
            boolean wait = false;
            if ((args.length > 2 && args[2].equals("wait")) || (args.length > 3 && args[3].equals("wait"))) {
                wait = true;
            }
            Logger.debug = false;
            BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
            for (int i = 0; i < numberOfOps; i++) {
                if (wait) {
                    System.out.println("Iteration " + i);
                    System.out.println("Press Enter for next iteration, type 'exit' to exit or type 'go' to run all remaining iterations");
                    String lido = inReader.readLine();
                    if (lido.equals("exit")) {
                        break;
                    } else if (lido.equals("go")) {
                        wait = false;
                    }
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream(4);
                new DataOutputStream(out).writeInt(inc);
                System.out.println("Counter sending: " + i);
                byte[] reply;
                if (inc == 0) reply = counterProxy.invokeUnordered(out.toByteArray()); else reply = counterProxy.invokeOrdered(out.toByteArray());
                if (reply != null) {
                    int newValue = new DataInputStream(new ByteArrayInputStream(reply)).readInt();
                    System.out.println("Counter value: " + newValue);
                    result = 0;
                } else {
                    result = 1;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = 1;
        } finally {
            counterProxy.close();
            System.exit(result);
        }
    }
}
