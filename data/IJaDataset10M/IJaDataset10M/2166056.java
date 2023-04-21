package com.simontuffs.soapstone.plugins.rmi;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import com.simontuffs.soapstone.api.Commands;
import com.simontuffs.soapstone.api.Plugin;
import com.simontuffs.soapstone.api.Commands.Command;
import com.simontuffs.soapstone.api.OptionSpace.Options;

/**
 * @author simon@simontuffs.com
 */
public class RMIPlugin implements Plugin {

    public static final String REGISTRY = "-registry";

    public static final String STRING = "-string";

    protected boolean debug = Plugin.Utils.debug("rmi");

    private Options options;

    protected static Registry rmiRegistry;

    protected Client client;

    public static void main(String[] args) {
    }

    /**
     * @see com.simontuffs.soapstone.api.Plugin#getProtocol()
     */
    public String getProtocol() {
        return "rmi";
    }

    /**
     * @see com.simontuffs.soapstone.api.Plugin#getCommand(com.simontuffs.soapstone.api.OptionSpace.Options)
     */
    public Command getCommand(Options $options) {
        options = $options;
        options.setDefault(RMIPlugin.REGISTRY, options.getInt(Commands.PORT) + 2);
        options.setDefault(RMIPlugin.STRING, false);
        int registry = options.getInt(RMIPlugin.REGISTRY);
        return new RMIMode(registry, options.getInt(Commands.SIZE), options.getInt(Commands.BUFFERS), options.getBoolean(Commands.ECHO), options.getBoolean(RMIPlugin.STRING));
    }

    /**
     * @see com.simontuffs.soapstone.api.Plugin#getClient()
     */
    public Client getClient() {
        if (client == null) client = new RMIClient();
        return client;
    }

    /**
     * @see com.simontuffs.soapstone.api.Plugin#getServer()
     */
    public Server getServer() {
        return new RMIServer();
    }

    public boolean hasServer() {
        return true;
    }

    /**
     * @author simon@simontuffs.com
     */
    public class RMIServer implements Server {

        /**
         * @see com.simontuffs.soapstone.api.Plugin.Server#startServer(com.simontuffs.soapstone.api.Commands.Command)
         */
        public void startServer(Command command) throws Exception {
            RMIMode rmiMode = (RMIMode) command;
            if (debug) {
                System.out.println("RMIServer.startServer(" + command + ")");
            }
            if (rmiRegistry == null) {
                try {
                    rmiRegistry = LocateRegistry.createRegistry(rmiMode.registryPort);
                } catch (RemoteException rx) {
                    rmiRegistry = LocateRegistry.getRegistry(rmiMode.registryPort);
                }
                rmiRegistry.list();
            }
            Remote rmiServer = new RMIServiceServer();
            if (debug) System.out.println("RMIServer " + RMIService.NAME + " started at " + rmiRegistry);
            rmiRegistry.rebind(RMIService.NAME, rmiServer);
        }

        /**
         * @see com.simontuffs.soapstone.api.Plugin.Server#runServer()
         */
        public void runServer(Command command) throws Exception {
            if (debug) {
                System.out.println("RMIServer.runServer() -- noop");
            }
        }

        /**
         * @see com.simontuffs.soapstone.api.Plugin.Server#stopServer()
         */
        public void stopServer(Command command) throws Exception {
            RMIMode rmiMode = (RMIMode) command;
            if (debug) {
                System.out.println("RMIServer.stopServer()");
            }
            if (rmiRegistry != null && rmiRegistry.lookup(RMIService.NAME) != null) rmiRegistry.unbind(RMIService.NAME);
        }
    }

    /**
     * @author simon@simontuffs.com
     */
    public class RMIClient extends Plugin.BaseClient {

        /**
         * @see com.simontuffs.soapstone.api.Plugin.Client#doBenchmark(com.simontuffs.soapstone.api.Commands.Command)
         */
        public Benchmark doBenchmark(Command command) throws Exception {
            if (debug) {
                System.out.println("RMIClient.doBenchmark(" + command + ")");
            }
            RMIMode rmiMode = (RMIMode) command;
            long start = new Date().getTime();
            long bytes = 0;
            int partial = 0;
            long count = 0;
            RMIService service = null;
            String rmi = "rmi://" + command.serverAddress.getHostName() + ":" + rmiMode.registryPort + "/" + RMIService.NAME;
            try {
                service = (RMIService) Naming.lookup(rmi);
            } catch (Exception x) {
                throw new Exception("Unable to connect to: " + rmi, x);
            }
            byte[] buf = new byte[command.bufferSize];
            for (int i = 0; i < buf.length; i++) {
                buf[i] = (byte) ((i % 26) + 'a');
            }
            for (int i = 0; i < command.buffers; i++) {
                if (rmiMode.string) {
                    if (command.echo) {
                        byte[] result = service.echoString(new String(buf)).getBytes();
                        bytes += result.length;
                    } else {
                        service.sendString(new String(buf));
                    }
                } else {
                    if (command.echo) {
                        byte[] result = service.echo(buf);
                        bytes += result.length;
                    } else {
                        service.send(buf);
                    }
                }
                bytes += buf.length;
            }
            long end = new Date().getTime();
            service = null;
            return new Benchmark(end - start, bytes);
        }
    }

    /**
     * This class must be static because it must be serializable.
     * @author simon@simontuffs.com
     */
    public static class RMIMode extends Command {

        public int registryPort;

        public boolean string = false;

        public RMIMode(int $registryPort, int $bufferSize, int $buffers, boolean $echo, boolean $string) {
            super($bufferSize, $buffers, $echo);
            registryPort = $registryPort;
            string = $string;
        }
    }

    /** 
     * @see com.simontuffs.soapstone.api.Plugin#getHelp()
     */
    public String getOptions() {
        String nl = "\n";
        String help = "  -registryport {port}  Use specified port for local RMI registry" + nl + "  -string               Transport bytes inside strings" + nl;
        return help;
    }
}
