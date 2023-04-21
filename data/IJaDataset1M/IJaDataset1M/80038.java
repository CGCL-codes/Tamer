package Galaxy.Visitor.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import Galaxy.Tree.Workflow.ToolState.Primitive;
import LONI.tree.GraphObject.GraphObject;
import LONI.tree.workflow.Connection;

/***
 * The loni environment class keeps track of the LONI workflow 
 * environment when nodes inside the galaxy environment are being
 * visited. The step environment allows the converter to handle non
 * 1 to 1 instances where a galaxy module corresponds to a modulegroup
 * of loni modules. 
 * @author viper
 *
 */
public class LoniEnvironment implements Environment {

    private List<GraphObject> dataModules = new LinkedList();

    private List<Connection> connections = new LinkedList();

    private Map<String, String> externalInputs = new HashMap<String, String>();

    private Map<String, String> externalOutputs = new HashMap<String, String>();

    public void addEnvironment(LoniEnvironment e) {
        dataModules.addAll(e.dataModules);
        connections.addAll(e.connections);
        externalInputs.putAll(e.getInputAliases());
        externalOutputs.putAll(e.getOutputAliases());
    }

    public void addConnection(Connection conn) {
        connections.add(conn);
    }

    public void addModule(GraphObject dm) {
        dataModules.add(dm);
    }

    public void addExternalInput(String alias, String id) {
        externalInputs.put(alias, id);
    }

    public void addExternalOutput(String alias, String id) {
        externalOutputs.put(alias, id);
    }

    public List<GraphObject> getModules() {
        return dataModules;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public Map<String, String> getInputAliases() {
        return externalInputs;
    }

    public Map<String, String> getOutputAliases() {
        return externalOutputs;
    }
}

;
