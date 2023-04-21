package hu.csq.dyneta.networkevents;

import cern.jet.random.Uniform;
import edu.uci.ics.jung.graph.Graph;
import hu.csq.dyneta.DocumentedPlugin;
import hu.csq.dyneta.InteroperatorPlugin;
import hu.csq.dyneta.lib.plugins.NetworkEvent;
import hu.csq.dyneta.ParameterizablePlugin;
import hu.csq.dyneta.misc.LinkedListSelector;
import hu.csq.dyneta.parameterhelper.ParameterHelper;
import hu.csq.dyneta.parameterhelper.constraints.PHCBoolean;
import hu.csq.dyneta.parameterhelper.constraints.PHCNoConstraint;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.commons.collections15.Factory;

/**
 *
 * @author Tamás Cséri
 */
public class MaxVertexScore<V, E> implements NetworkEvent<V, E>, ParameterizablePlugin, DocumentedPlugin, InteroperatorPlugin {

    ParameterHelper ph = new ParameterHelper();

    HashMap<String, Object> interoperatorHashMap;

    Uniform r;

    LinkedListSelector<V> lls;

    public MaxVertexScore() {
        ph.add("maximum", "true", new PHCBoolean(), "If true it operates with the maximum. If false it operates with the minimum.");
        ph.add("datasourcepluginname", "", new PHCNoConstraint(), "A plugin where the vertex scores come from. You have to enter the full name.");
        ph.add("deletevertex", "false", new PHCBoolean(), "If true the plugin deletes the selected vertex. If false it removes all edges connected to the vertice.");
        ph.addSeed();
        ph.ready();
    }

    String datasourcepluginname;

    boolean maximum;

    boolean deletevertex;

    public void init() {
        datasourcepluginname = ph.get("datasourcepluginname") + "_vertexscores";
        if (!interoperatorHashMap.containsKey(datasourcepluginname)) {
            throw new RuntimeException("MaxVertexScore plugin need a data source to get vertex scores from.");
        }
        if (!interoperatorHashMap.get(datasourcepluginname).equals("This will be a vertex scorer hashmap")) {
            throw new RuntimeException("MaxVertexScore plugin need a data source to get vertex scores from.");
        }
        maximum = Boolean.parseBoolean(ph.get("maximum"));
        deletevertex = Boolean.parseBoolean(ph.get("deletevertex"));
    }

    Graph<V, E> g;

    HashMap<V, Number> vertexscores;

    public void setGraph(Graph<V, E> g, Factory<V> fV, Factory<E> fE) {
        this.g = g;
        if (g != null) {
            r = new Uniform(0.0, 1.0, (int) ph.getLong("seed"));
            lls = new LinkedListSelector<V>();
            interoperatorHashMap.put(datasourcepluginname, "Let there be a vertex scorer hashmap here");
        } else {
            vertexscores = null;
        }
    }

    @SuppressWarnings("unchecked")
    private void loadInteroperableObjects() {
        try {
            vertexscores = (HashMap<V, Number>) interoperatorHashMap.get(datasourcepluginname);
        } catch (ClassCastException ex) {
            throw new RuntimeException("MaxVertexScore plugin need a data source to get vertex scores from.");
        }
    }

    public void doEventStep() {
        loadInteroperableObjects();
        if (g.getVertexCount() > 0) {
            LinkedList<V> lmaxV = new LinkedList<V>();
            double maxdegree = vertexscores.get(g.getVertices().iterator().next()).doubleValue();
            if (!maximum) {
                maxdegree = -maxdegree;
            }
            double currentdegree;
            for (V iv : g.getVertices()) {
                currentdegree = vertexscores.get(iv).doubleValue();
                if (!maximum) {
                    currentdegree = -currentdegree;
                }
                if (currentdegree > maxdegree) {
                    lmaxV.clear();
                    maxdegree = currentdegree;
                }
                if (currentdegree == maxdegree) {
                    lmaxV.add(iv);
                }
            }
            V v = lls.getRandomElement(lmaxV, r);
            g.removeVertex(v);
            if (!deletevertex) {
                g.addVertex(v);
                if (g.degree(v) > 0) {
                    throw new RuntimeException("big-big error");
                }
            } else {
                vertexscores.remove(v);
            }
        }
    }

    public ParameterHelper getParameterHelper() {
        return ph;
    }

    public String getShortDesciption() {
        return "Removes a node that has maximum/minimum score.";
    }

    public String getHTMLDesciption() {
        return "<h2>Description</h2><p>" + getShortDesciption() + "</p>";
    }

    public void seeInteroperatorHashMap(HashMap<String, Object> interoperatorHashMap) {
        this.interoperatorHashMap = interoperatorHashMap;
    }
}
