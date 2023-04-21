package InferenceGraphs;

import BayesianNetworks.*;
import BayesianInferences.*;
import QuasiBayesianNetworks.*;
import QuasiBayesianInferences.*;
import InterchangeFormat.*;
import java.io.*;
import java.awt.*;
import java.util.*;
import java.net.URL;

public class InferenceGraph {

    QuasiBayesNet qbn;

    QBInference qbi;

    QBExpectation qbe;

    Vector nodes = new Vector();

    private final String defaultBayesNetName = "InternalNetwork";

    public static final int MARGINAL_POSTERIOR = 1;

    public static final int EXPECTATION = 2;

    public static final int EXPLANATION = 3;

    public static final int FULL_EXPLANATION = 4;

    public static final int SENSITIVITY_ANALYSIS = 5;

    public static final int NO_CREDAL_SET = QuasiBayesNet.NO_CREDAL_SET;

    public static final int CONSTANT_DENSITY_RATIO = QuasiBayesNet.CONSTANT_DENSITY_RATIO;

    public static final int EPSILON_CONTAMINATED = QuasiBayesNet.EPSILON_CONTAMINATED;

    public static final int CONSTANT_DENSITY_BOUNDED = QuasiBayesNet.CONSTANT_DENSITY_BOUNDED;

    public static final int TOTAL_VARIATION = QuasiBayesNet.TOTAL_VARIATION;

    public InferenceGraph() {
        qbn = new QuasiBayesNet(defaultBayesNetName, 0, 0);
    }

    public InferenceGraph(BayesNet b_n) {
        qbn = new QuasiBayesNet(b_n);
        convert_bayes_net();
    }

    public InferenceGraph(String filename) throws IOException, IFException {
        qbn = new QuasiBayesNet(new java.io.DataInputStream(new java.io.FileInputStream(filename)));
        convert_bayes_net();
    }

    public InferenceGraph(URL url) throws IOException, IFException {
        qbn = new QuasiBayesNet(url);
        convert_bayes_net();
    }

    public QuasiBayesNet get_bayes_net() {
        return (convert_graph());
    }

    boolean convert_bayes_net() {
        ProbabilityVariable pv = null;
        ProbabilityFunction pf = null;
        for (int i = 0; i < qbn.number_variables(); i++) {
            pv = qbn.get_probability_variable(i);
            pf = null;
            for (int j = 0; j < qbn.number_probability_functions(); j++) {
                pf = qbn.get_probability_function(j);
                if (pf.get_variable(0) == pv) break;
            }
            if (pf == null) return (false);
            nodes.addElement(new InferenceGraphNode(this, pv, pf));
        }
        generate_parents_and_children();
        return (true);
    }

    private void generate_parents_and_children() {
        int i, j;
        DiscreteVariable variables[];
        ProbabilityFunction pf;
        InferenceGraphNode base_node, node;
        Enumeration e;
        for (e = nodes.elements(); e.hasMoreElements(); ) {
            base_node = (InferenceGraphNode) (e.nextElement());
            pf = base_node.pf;
            variables = pf.get_variables();
            for (i = 1; i < variables.length; i++) {
                node = get_node(variables[i]);
                if (node == null) continue;
                base_node.parents.addElement(node);
                node.children.addElement(base_node);
            }
        }
    }

    private InferenceGraphNode get_node(DiscreteVariable dv) {
        InferenceGraphNode node;
        for (Enumeration e = nodes.elements(); e.hasMoreElements(); ) {
            node = (InferenceGraphNode) e.nextElement();
            if (node.pv == dv) return (node);
        }
        return (null);
    }

    QuasiBayesNet convert_graph() {
        int i;
        Enumeration e;
        InferenceGraphNode node;
        ProbabilityVariable pvs[] = new ProbabilityVariable[nodes.size()];
        ProbabilityFunction pfs[] = new ProbabilityFunction[nodes.size()];
        qbn.set_probability_variables(pvs);
        qbn.set_probability_functions(pfs);
        for (i = 0, e = nodes.elements(); e.hasMoreElements(); i++) {
            node = (InferenceGraphNode) (e.nextElement());
            node.update_position();
            qbn.set_probability_variable(i, node.pv);
            qbn.set_probability_function(i, node.pf);
        }
        return (qbn);
    }

    private String generate_name(int i) {
        InferenceGraphNode no;
        char namec = (char) ((int) 'a' + i % 26);
        int suffix = i / 26;
        String name;
        if (suffix > 0) name = new String("" + namec + suffix); else name = new String("" + namec);
        for (Enumeration e = nodes.elements(); e.hasMoreElements(); ) {
            no = (InferenceGraphNode) (e.nextElement());
            if (no.get_name().equals(name)) return (generate_name(i + 1));
        }
        return (name);
    }

    /**
   * Get the name of the network.
   */
    public String get_name() {
        return (qbn.get_name());
    }

    /**
   * Set the name of the network.
   */
    public void set_name(String n) {
        qbn.set_name(n);
    }

    /**
   * Get the properties of the network.
   */
    public Vector get_network_properties() {
        return (qbn.get_properties());
    }

    /**
   * Set the properties of the network.
   */
    public void set_network_properties(Vector prop) {
        qbn.set_properties(prop);
    }

    /**
   * Get the type of global neighborhood modeled by the network.
   */
    public int get_global_neighborhood_type() {
        return (qbn.get_global_neighborhood_type());
    }

    /**
   * Set the global neighborhood type.
   */
    public void set_global_neighborhood(int type) {
        qbn.set_global_neighborhood_type(type);
    }

    /**
   * Get the parameter for the global neighborhood modeled by the network.
   */
    public double get_global_neighborhood_parameter() {
        return (qbn.get_global_neighborhood_parameter());
    }

    /**
   * Set the parameter for the global neighborhood modeled by the network.
   */
    public void set_global_neighborhood_parameter(double parameter) {
        qbn.set_global_neighborhood_parameter(parameter);
    }

    /**
   * Remove a property from the network.
   */
    public void remove_network_property(int index) {
        qbn.remove_property(index);
    }

    /**
   * Add a property to the network.
   */
    public void add_network_property(String prop) {
        qbn.add_property(prop);
    }

    /**
   * Determine whether or not a name is valid and/or repeated.
   */
    public String check_name(String n) {
        InferenceGraphNode no;
        String nn = validate_value(n);
        for (Enumeration e = nodes.elements(); e.hasMoreElements(); ) {
            no = (InferenceGraphNode) (e.nextElement());
            if (no.get_name().equals(nn)) return (null);
        }
        return (nn);
    }

    /**
   * Check whether a string is a valid name.
   */
    public String validate_value(String value) {
        StringBuffer str = new StringBuffer(value);
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') str.setCharAt(i, '_');
        }
        return str.toString();
    }

    /**
   * Print the QuasiBayesNet.
   */
    public void print_bayes_net(PrintStream out) {
        QuasiBayesNet qb_n = get_bayes_net();
        qb_n.print(out);
    }

    /**
   * Print information about a posterior marginal for the Bayesian
   * network into the given PrintStream.
   * @param queried_variable indicates the variable of interest.
   * @param show_bucket_tree determines whether or not to present
   *                     a description of the BucketTree.
   */
    public void print_marginal(PrintStream pstream, String queried_variable, boolean do_compute_clusters, boolean show_bucket_tree) {
        if ((do_compute_clusters == false) || (qbi == null) || (qbi.areClustersProduced() == false)) qbi = new QBInference(get_bayes_net(), do_compute_clusters);
        qbi.inference(queried_variable);
        qbi.print(pstream, show_bucket_tree);
    }

    /**
   * Reset the QBInference.
   */
    public void reset_marginal() {
        qbi = null;
    }

    /**
   * Print information about a posterior expectation for the Bayesian
   * network into the given PrintStream.
   * @param queried_variable indicates the variable of interest.
   * @param show_bucket_tree determines whether or not to present
   *                     a description of the BucketTree.
   */
    public void print_expectation(PrintStream pstream, String queried_variable, boolean do_compute_clusters, boolean show_bucket_tree) {
        if ((do_compute_clusters == false) || (qbe == null) || (qbi.areClustersProduced() == false)) qbe = new QBExpectation(get_bayes_net(), do_compute_clusters);
        qbe.expectation(queried_variable);
        qbe.print(pstream, show_bucket_tree);
    }

    /**
   * Reset the QBExpectation.
   */
    public void reset_expectation() {
        qbe = null;
    }

    /**
   * Print information about an explanation for the Bayesian
   * network into the given PrintStream.
   * @param show_bucket_tree determines whether or not to present
   *                     a description of the BucketTree.
   */
    public void print_explanation(PrintStream pstream, boolean show_bucket_tree) {
        Explanation ex = new Explanation(get_bayes_net());
        ex.explanation();
        ex.print(pstream, show_bucket_tree);
    }

    /**
   * Print information about a full explanation for the Bayesian
   * network into the given PrintStream.
   * @param show_bucket_tree determines whether or not to present
   *                     a description of the BucketTree.
   */
    public void print_full_explanation(PrintStream pstream, boolean show_bucket_tree) {
        Explanation fex = new Explanation(get_bayes_net());
        fex.full_explanation();
        fex.print(pstream, show_bucket_tree);
    }

    /**
   * Print the metrics for sensitivity analysis of the Bayesian
   * network into the given PrintStream.
   */
    public void print_sensitivity_analysis(PrintStream pstream) {
        System.out.print("HEY! Sensitivity analysis not implemented yet!");
    }

    /**
   * Save the Bayesian network into a PrintStream in the BIF InterchangeFormat.
   */
    public void save_bif(PrintStream out) {
        QuasiBayesNet qb_n = get_bayes_net();
        qb_n.save_bif(out);
    }

    /**
   * Save the Bayesian network into a PrintStream in the XML InterchangeFormat.
   */
    public void save_xml(PrintStream out) {
        QuasiBayesNet qb_n = get_bayes_net();
        qb_n.save_xml(out);
    }

    /**
   * Save the Bayesian networks in BUGS format into a PrintStream.
   */
    public void save_bugs(PrintStream out) {
        QuasiBayesNet qb_n = get_bayes_net();
        qb_n.save_bugs(out);
    }

    /**
   * Print method for an InferenceGraph
   */
    public void print() {
        print(System.out);
    }

    /**
   * Print method for an InferenceGraph
   */
    public void print(PrintStream out) {
        QuasiBayesNet qb_n = get_bayes_net();
        qb_n.print(out);
    }

    /**
   * Get the nodes in the network.
   */
    public Vector get_nodes() {
        return (nodes);
    }

    /**
   * Get the nodes in the network as an Enumeration object.
   */
    public Enumeration elements() {
        return (nodes.elements());
    }

    /**
   * Get the number of variables in the network
   */
    public int number_nodes() {
        return (nodes.size());
    }

    /**
   * Create a new node in the network.
   */
    public void create_node(int x, int y) {
        Point p = new Point(x, y);
        String n = generate_name(nodes.size());
        nodes.addElement(new InferenceGraphNode(this, n, p));
        convert_graph();
    }

    /**
   * Create an arc from parent to child.
   */
    public boolean create_arc(InferenceGraphNode parent, InferenceGraphNode child) {
        for (Enumeration e = child.parents.elements(); e.hasMoreElements(); ) {
            if (parent == ((InferenceGraphNode) (e.nextElement()))) return (false);
        }
        parent.children.addElement(child);
        child.parents.addElement(parent);
        child.init_dists();
        convert_graph();
        return (true);
    }

    /**
   * Delete a node in the network.
   */
    public void delete_node(InferenceGraphNode node) {
        Enumeration e;
        InferenceGraphNode parent, child;
        for (e = node.children.elements(); e.hasMoreElements(); ) {
            child = (InferenceGraphNode) (e.nextElement());
            child.parents.removeElement(node);
            child.init_dists();
        }
        for (e = node.parents.elements(); e.hasMoreElements(); ) {
            parent = (InferenceGraphNode) (e.nextElement());
            parent.children.removeElement(node);
        }
        nodes.removeElement(node);
        convert_graph();
    }

    /**
   * Delete the arc from parent to child.
   */
    public void delete_arc(InferenceGraphNode parent, InferenceGraphNode child) {
        parent.children.removeElement(child);
        child.parents.removeElement(parent);
        child.init_dists();
        convert_graph();
    }

    /**
   * Determines whether the connection of
   * bottom_node to head_node would cause the
   * network to have a cycle.
   */
    public boolean hasCycle(InferenceGraphNode bottom_node, InferenceGraphNode head_node) {
        Vector children;
        Enumeration e;
        InferenceGraphNode next_node, child_node;
        InferenceGraphNode listed_nodes[] = new InferenceGraphNode[nodes.size()];
        Hashtable hashed_nodes = new Hashtable();
        int last_listed_node_index = 0;
        int current_listed_node_index = 0;
        listed_nodes[0] = head_node;
        hashed_nodes.put(head_node.pv.get_name(), head_node);
        while (current_listed_node_index <= last_listed_node_index) {
            next_node = listed_nodes[current_listed_node_index];
            current_listed_node_index++;
            children = next_node.children;
            for (e = children.elements(); e.hasMoreElements(); ) {
                child_node = (InferenceGraphNode) (e.nextElement());
                if (child_node == bottom_node) return (true);
                if (!hashed_nodes.containsKey(child_node.pv.get_name())) {
                    hashed_nodes.put(child_node.pv.get_name(), child_node);
                    last_listed_node_index++;
                    listed_nodes[last_listed_node_index] = child_node;
                }
            }
        }
        return (false);
    }

    /**
   * Change the values of a variable. Note that, if the number
   * of new values is different from the number of current values,
   * this operation resets the
   * probability values of the variable and all its children.
   */
    public void change_values(InferenceGraphNode node, String values[]) {
        InferenceGraphNode cnode;
        Vector children;
        Enumeration e;
        if (node.pv.number_values() == values.length) {
            node.pv.set_values(values);
            return;
        }
        node.pv.set_values(values);
        node.init_dists();
        children = node.get_children();
        for (e = children.elements(); e.hasMoreElements(); ) {
            cnode = (InferenceGraphNode) (e.nextElement());
            cnode.init_dists();
        }
        convert_graph();
    }

    /**
   * Set a value for the position of the node.
   */
    public void set_pos(InferenceGraphNode node, Point position) {
        node.pos = position;
        convert_graph();
    }
}
