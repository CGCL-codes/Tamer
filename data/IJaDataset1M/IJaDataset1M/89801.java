package ajpf.product;

import java.util.HashSet;

public class ProbabilisticModel extends MCAPLmodel {

    /**
	 * Add an edge to the model.  
	 * @param e
	 */
    public void addEdge(ModelState s) {
        if (!current_path.isEmpty()) {
            ModelState from = current_path.get(current_path.size() - 1);
            ModelEdge e = new ProbabilisticModelEdge(from, s);
            if (model_edges.containsKey(from)) {
                model_edges.get(from).add(e);
            } else {
                HashSet<ModelEdge> edges = new HashSet<ModelEdge>();
                edges.add(e);
                model_edges.put(from, edges);
            }
        }
    }

    public double pathProb() {
        double prob = 1;
        for (ModelEdge e : getEdgePath()) {
            double edge_prob = ((ProbabilisticModelEdge) e).getProbability();
            if (edge_prob != 0) {
                prob = prob * edge_prob;
            } else {
                prob = prob / equivalent_edges(e);
            }
        }
        return prob;
    }

    public int equivalent_edges(ModelEdge e) {
        ModelState from = e.getFromState();
        return model_edges.get(from).size();
    }
}
