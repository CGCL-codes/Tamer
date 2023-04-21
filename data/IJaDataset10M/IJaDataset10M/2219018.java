package edu.upc.lsi.kemlg.aws.communication;

import java.util.Map;
import java.util.TreeMap;
import edu.upc.lsi.kemlg.aws.AgentEndpoint;
import edu.upc.lsi.kemlg.aws.utils.AgentLogger;

public class AgentRegistry {

    private static Map<String, AgentEndpoint> map;

    public static void addAgent(String name, AgentEndpoint ae) {
        if (map == null) {
            map = new TreeMap<String, AgentEndpoint>();
        }
        AgentLogger.log("Putting " + name + " into registry.");
        map.put(name, ae);
    }

    public static AgentEndpoint getAgent(String name) {
        return map.get(name);
    }
}
