package be.vanvlerken.bert.packetdistributor.server;

/**
 * This module parses the argument list for the PacketDistributorServer On creation it
 * analyzes the argument list and sets all appropriate values. When no argument
 * is found for a certain setting, a default is used.
 */
public class ArgumentInterpreter {

    private String configXml;

    /**
     */
    public ArgumentInterpreter(String defaultConfigXml) {
        this.configXml = defaultConfigXml;
    }

    /**
     * @param args
     * @return
     */
    public boolean parse(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-config") && i + 1 < args.length) {
                configXml = args[i + 1];
                break;
            }
        }
        return true;
    }

    public String getConfigXml() {
        return configXml;
    }

    /**
     * 
     */
    public void printUsage() {
        System.out.println("[-config <configXml>]");
    }
}
