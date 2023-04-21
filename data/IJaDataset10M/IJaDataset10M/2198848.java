package echo;

import org.gearman.Gearman;
import org.gearman.GearmanFunction;
import org.gearman.GearmanFunctionCallback;
import org.gearman.GearmanServer;
import org.gearman.GearmanWorker;

/**
 * The echo worker polls jobs from a job server and execute the echo function.
 * 
 * The echo worker illustrates how to setup a basic worker
 */
public class EchoWorker implements GearmanFunction {

    /** The echo function name */
    public static final String ECHO_FUNCTION_NAME = "echo";

    /** The host address of the job server */
    public static final String ECHO_HOST = "localhost";

    /** The port number the job server is listening on */
    public static final int ECHO_PORT = 4730;

    public static void main(String... args) {
        Gearman gearman = Gearman.createGearman();
        GearmanServer server = gearman.createGearmanServer(EchoWorker.ECHO_HOST, EchoWorker.ECHO_PORT);
        GearmanWorker worker = gearman.createGearmanWorker();
        worker.addFunction(EchoWorker.ECHO_FUNCTION_NAME, new EchoWorker());
        worker.addServer(server);
    }

    @Override
    public byte[] work(String function, byte[] data, GearmanFunctionCallback callback) throws Exception {
        return data;
    }
}
