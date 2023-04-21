package jbridge;

import inou.util.CommandLine;

public class BridgeBuilder {

    public static void main(String[] args) throws Exception {
        CommandLine cli = new CommandLine(args);
        IBridgeBuilder builder = getBridgeBuilder(cli.getArgument());
        if (builder == null) {
            printHelp();
        } else {
            start(builder, cli);
        }
    }

    private static void printHelp() {
        System.out.println("> java jbridge.BridgeBuilder (bridge builder classname)");
    }

    private static IBridgeBuilder getBridgeBuilder(String bridgeBuilderClassName) {
        if (bridgeBuilderClassName == null) return null;
        try {
            Class c = Class.forName(bridgeBuilderClassName);
            return (IBridgeBuilder) c.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void start(IBridgeBuilder builder, CommandLine cli) throws Exception {
        builder.setConfig(cli);
        BridgeServer server = new BridgeServer(builder.getOverrideCaller(), builder.getObjectTransformer());
        builder.start(server);
        System.out.println("OK.");
    }
}
