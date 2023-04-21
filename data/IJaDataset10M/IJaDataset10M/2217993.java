package persister.factory;

import applicationWorkbench.Activator;

public class PersisterConnectionInfoAP extends PersisterConnectionInfo {

    public static String getPluginID() {
        return Activator.PLUGIN_ID;
    }
}
