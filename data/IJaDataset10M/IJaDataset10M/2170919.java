package de.walware.statet.r.internal.rserve.launchconfigs;

import de.walware.statet.r.rserve.RServePlugin;

public interface IRServeConstants {

    String ID_RSERVE_LAUNCHCONFIG = "de.walware.statet.r.launchConfigurationTypes.RServeClient";

    String CONFIG_CONNECTION_SERVERADDRESS = RServePlugin.PLUGIN_ID + "/connection/server_address";

    String CONFIG_CONNECTION_SERVERPORT = RServePlugin.PLUGIN_ID + "/connection/server_port";

    String CONFIG_CONNECTION_SOCKETTIMEOUT = RServePlugin.PLUGIN_ID + "/connection/socket_timeout";
}
