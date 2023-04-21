package org.processmining.framework.models.activitygraph;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.processmining.framework.models.ModelGraph;
import org.processmining.framework.models.ModelGraphVertex;

/**
 * An activity graph is a graph with only activities in it. So, there doesn't
 * exists any connections between the activities.
 * 
 * @author rmans
 */
public class ActivityGraph extends ModelGraph {

    /** the list of activities */
    protected ArrayList<ModelGraphVertex> activities = new ArrayList<ModelGraphVertex>();

    /**
	 * basic constructor
	 */
    public ActivityGraph() {
        super("Activity Graph");
    }

    /**
	 * adds an activity vertex to the activity graph
	 * 
	 * @param av
	 *            ActivityVertex the activity vertex to be added to the graph
	 * @return ActivityVertex the added activity vertex
	 */
    public ModelGraphVertex addActivityVertex(ModelGraphVertex av) {
        addVertex(av);
        activities.add(av);
        return av;
    }

    /**
	 * Retrieves the activity vertices that are present in this activity graph
	 * 
	 * @return List
	 */
    public List<ModelGraphVertex> getActivityVertices() {
        return activities;
    }

    /**
	 * Removes an activity vertex from the activity graph
	 * 
	 * @param av
	 *            ActivityVertex the activity vertex to be removed from the
	 *            activity graph
	 */
    public void delActivityVertex(ModelGraphVertex av) {
        removeVertex(av);
        activities.remove(av);
    }

    /**
	 * When a activity graph is asked for its visualization, a temporary dot
	 * file is written and afterwards read by grappa to convert it into a java
	 * frame. For the activity graph, only the activities will be displayed in
	 * the picture.
	 * 
	 * @param bw
	 *            Writer
	 * @throws IOException
	 */
    public void writeToDot(Writer bw) throws IOException {
        nodeMapping.clear();
        bw.write("digraph G {ranksep=\".3\"; fontsize=\"8\"; remincross=true; margin=\"0.0,0.0\"; ");
        bw.write("fontname=\"Arial\";rankdir=\"TB\";compound=\"true\" \n");
        bw.write("edge [arrowsize=\"0.5\"];\n");
        bw.write("node [height=\".2\",width=\"1\",fontname=\"Arial\",fontsize=\"8\"];\n");
        for (int i = 0; i < activities.size(); i++) {
            ModelGraphVertex av = (ModelGraphVertex) activities.get(i);
            bw.write("node" + av.getId() + " [shape=\"box\", style=\"filled\",fillcolor=\"lavenderblush1\",label=\"" + av.getIdentifier() + "\",fontsize=6];\n");
            nodeMapping.put(new String("node" + av.getId()), av);
        }
        bw.write("}\n");
    }
}
