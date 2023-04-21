package test.ucapc;

import java.util.Map;
import net.sf.beenuts.apc.BeenutsAgentProcessCluster;
import net.sf.beenuts.apc.util.APCConfiguration;

public class LocalMaster {

    public static void main(String args[]) {
        Map<String, APCConfiguration> cfg = APCConfiguration.loadConfig("apc_config_1.xml");
        System.out.println("agentProcessCluster configurations: " + cfg.keySet());
        BeenutsAgentProcessCluster apc = new BeenutsAgentProcessCluster();
        apc.initialize(cfg.get("master_cfg"), "apc1");
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }
        apc.shutdown();
    }
}
