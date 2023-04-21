package org.ps.seishin;

import org.apache.log4j.PropertyConfigurator;
import org.ps.seishin.configuration.ConfigurationManager;
import org.ps.seishin.services.requestserver.RequestServer;

public class Server {

    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");
        ParseArgs(args);
        Initialize();
        ServiceManager.addAndStartService(RequestServer.getInstance());
        ServiceManager.waitEndOfServices();
    }

    /**
     * Parseador de argumentos de la consola
     * @param args Argumentos de la consola
     */
    private static void ParseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch(args[i]) {
                case "-v":
                    break;
                case "-s":
                    break;
                case "-c":
                    i++;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Rutina de inicializacion del servidor
     */
    private static void Initialize() {
        System.out.println("Starting...");
        System.out.print("Loading configuration... ");
        ConfigurationManager.Load();
        System.out.println("done.");
    }
}
