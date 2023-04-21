package cloudspace.ui.applet;

public abstract class JSFactory {

    private JSFactory() {
    }

    public static String generateJSFunction(String funcName, String eventName, String... parameters) {
        String function = functionGenHelper(funcName, parameters);
        function += "zAu.send(new zk.Event(null, '" + eventName + "',data, {toServer:true,clt:true}));}";
        return function;
    }

    private static String functionGenHelper(String funcName, String... parameters) {
        String function = "function ";
        function += funcName;
        function += "(reqId";
        if (parameters.length != 0) function += ", ";
        String dataSetup = "var data = {};";
        dataSetup += "data['reqId'] = reqId;";
        for (int i = 0; i < parameters.length; i++) {
            String param = parameters[i];
            function += param;
            dataSetup += "data['" + param + "'] = " + param + ";";
            if (i + 1 < parameters.length) function += ", ";
        }
        function += "){";
        function += dataSetup;
        return function;
    }

    public static String generateGenericJSFunction(String genericJsFunc, String[] genericParam) {
        String function = functionGenHelper(genericJsFunc, genericParam);
        function += "zAu.send(new zk.Event(null, eventName,data, {toServer:true,clt:true}));}";
        return function;
    }
}
