package hu.csq.dyneta.networkstatistics;

import edu.uci.ics.jung.algorithms.scoring.VertexScorer;
import edu.uci.ics.jung.graph.Graph;
import hu.csq.dyneta.DocumentedPlugin;
import hu.csq.dyneta.InteroperatorPlugin;
import hu.csq.dyneta.lib.plugins.NetworkStatistics;
import hu.csq.dyneta.misc.AggregatorDouble;
import java.util.HashMap;
import org.apache.commons.lang.mutable.MutableDouble;

/**
 *
 * @author Tamás Cséri
 */
public abstract class AggregateVertexScores<V, E> implements NetworkStatistics<V, E>, DocumentedPlugin, InteroperatorPlugin {

    public MutableDouble getVertexScore(V v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Override this method to provide the custom vertex scorer.
     *
     * @param g
     * @return
     */
    public abstract VertexScorer<V, Double> getVertexScorer(Graph<V, E> g);

    public abstract String getStatNamePrefix();

    /**
     * Init is implemented as empty, although it can be overridden.
     */
    public void init() {
    }

    public int getNumberOfStats() {
        return 5;
    }

    HashMap<V, MutableDouble> vertexscores;

    protected String getInteroperableObjectName() {
        return getClass().getName() + "_vertexscores";
    }

    @SuppressWarnings("unchecked")
    private void loadInteroperableObjects(int vertexcount) {
        Object o = interoperatorHashMap.get(getInteroperableObjectName());
        if (o instanceof HashMap) {
            vertexscores = (HashMap<V, MutableDouble>) o;
        } else {
            vertexscores = new HashMap<V, MutableDouble>(vertexcount);
            interoperatorHashMap.put(getInteroperableObjectName(), vertexscores);
        }
    }

    public void getStats(Graph<V, E> g, double[] rv, int rvbegin) {
        VertexScorer<V, Double> vs = getVertexScorer(g);
        AggregatorDouble ad = new AggregatorDouble();
        loadInteroperableObjects(g.getVertexCount());
        for (V v : g.getVertices()) {
            Double vertexscore = vs.getVertexScore(v);
            ad.data(vertexscore);
            if (vertexscores != null) {
                MutableDouble vertexscoreholder = vertexscores.get(v);
                if (vertexscoreholder != null) {
                    vertexscoreholder.setValue(vertexscore);
                } else {
                    vertexscores.put(v, new MutableDouble(vertexscore));
                }
            }
        }
        rv[rvbegin + 0] = ad.getMin();
        rv[rvbegin + 1] = ad.getMax();
        rv[rvbegin + 2] = ad.getSum();
        rv[rvbegin + 3] = ad.getAvg();
        rv[rvbegin + 4] = ad.getVar();
    }

    public String[] getStatNames() {
        String prefix = getStatNamePrefix();
        String[] rv = new String[getNumberOfStats()];
        rv[0] = prefix + "Min";
        rv[1] = prefix + "Max";
        rv[2] = prefix + "Sum";
        rv[3] = prefix + "Avg";
        rv[4] = prefix + "Var";
        return rv;
    }

    /**
     * <p>Override this method to provide additional description in HTML format.</p>
     *
     * <p>Should look like:</p>
     * <p><tt>
     * &lt;p&gt;Additional paragraph for the desctiption section&lt;/p&gt;
     * &lt;p&gt;Another one&lt;/p&gt;
     * &lt;h2&gt;New section&lt;/h2&gt;
     * &lt;p&gt;Paragraph for new section&lt;/p&gt;
     * </tt></p>
     * @return
     */
    public String getAdditionalDescription() {
        return "";
    }

    public String getHTMLDesciption() {
        StringBuilder sb = new StringBuilder("<h2>Description</h2><p>");
        sb.append(getShortDesciption());
        sb.append("</p>");
        sb.append(getAdditionalDescription());
        sb.append("<h2>Output columns</h2><table><thead><tr><td>Name</td><td>Value</td></tr></thead><tbody>");
        String[] colNames = getStatNames();
        for (int i = 0; i < getNumberOfStats(); i++) {
            sb.append("<tr><td>");
            sb.append(colNames[i]);
            sb.append("</td><td>");
            switch(i) {
                case 0:
                    sb.append("Minium");
                    break;
                case 1:
                    sb.append("Maxium");
                    break;
                case 2:
                    sb.append("Sum");
                    break;
                case 3:
                    sb.append("Average");
                    break;
                case 4:
                    sb.append("Variance");
                    break;
            }
            sb.append("</td></tr>");
        }
        sb.append("</tbody></table>");
        return sb.toString();
    }

    public abstract String getNameDescription();

    public String getShortDesciption() {
        return "Calculates the aggreate functions of " + getNameDescription() + " of vertices in the graph.";
    }

    HashMap<String, Object> interoperatorHashMap;

    public void seeInteroperatorHashMap(HashMap<String, Object> interoperatorHashMap) {
        this.interoperatorHashMap = interoperatorHashMap;
        interoperatorHashMap.put(getInteroperableObjectName(), "This will be a vertex scorer hashmap");
    }
}
