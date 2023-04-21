package BayesianNetworks;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/**
 * ***************************************************************
 */
public class ProbabilityFunction extends DiscreteFunction implements Serializable {

    protected Vector properties;

    protected BayesNet bn;

    /**
     * Default constructor for a ProbabilityFunction.
     */
    public ProbabilityFunction() {
    }

    /**
     * Constructor for ProbabilityFunction.
     */
    public ProbabilityFunction(BayesNet b_n, int n_vb, int n_vl, Vector prop) {
        super(n_vb, n_vl);
        properties = prop;
        bn = b_n;
    }

    /**
     * Constructor for ProbabilityFunction.
     */
    public ProbabilityFunction(BayesNet b_n, DiscreteVariable pvs[], double v[], Vector prop) {
        super(pvs, v);
        properties = prop;
        bn = b_n;
    }

    /**
     * Constructor for ProbabilityFunction.
     */
    public ProbabilityFunction(DiscreteFunction df, double[] new_values) {
        super(df.variables, new_values);
        if (df instanceof ProbabilityFunction) {
            bn = ((ProbabilityFunction) df).bn;
            properties = ((ProbabilityFunction) df).properties;
        }
    }

    /**
     * Constructor for ProbabilityFunction.
     */
    public ProbabilityFunction(DiscreteFunction df, BayesNet b_n) {
        super(df.variables, df.values);
        bn = b_n;
        if (df instanceof ProbabilityFunction) properties = ((ProbabilityFunction) df).properties;
    }

    void process_properties() {
    }

    /**
     * Set a single value of the probability function.
     */
    public void set_value(String variable_value_pairs[][], double val) {
        int index;
        ProbabilityVariable pv;
        int value_indexes[] = new int[bn.probability_variables.length];
        for (int i = 0; i < variable_value_pairs.length; i++) {
            index = bn.index_of_variable(variable_value_pairs[i][0]);
            pv = bn.probability_variables[index];
            value_indexes[index] = pv.index_of_value(variable_value_pairs[i][1]);
        }
        int pos = get_position_from_indexes(bn.probability_variables, value_indexes);
        values[pos] = val;
    }

    /**
     * Evaluate a function given a list of pairs (Variable Value) which specifies a value of the function.
     */
    public double evaluate(String variable_value_pairs[][]) {
        int index;
        ProbabilityVariable pv;
        int value_indexes[] = new int[bn.probability_variables.length];
        for (int i = 0; i < variable_value_pairs.length; i++) {
            index = bn.index_of_variable(variable_value_pairs[i][0]);
            pv = bn.probability_variables[index];
            value_indexes[index] = pv.index_of_value(variable_value_pairs[i][1]);
        }
        return (evaluate(value_indexes));
    }

    /**
     * Evaluate a function given a (possibly partial) instantiation of variables through the markers. The markers
     * indicate which variables are present in the function to be evaluated.
     */
    public double evaluate(int value_indexes[]) {
        return (super.evaluate(bn.probability_variables, value_indexes));
    }

    /**
     * Get position in a function from a (possibly partial) instantiation of variables through the indexes.
     */
    public int get_position_from_indexes(int variable_indexes[]) {
        return (super.get_position_from_indexes(bn.probability_variables, variable_indexes));
    }

    /**
     * Obtain expected value of a DiscreteFunction The current implementation is very limited; it assumes that both the
     * ProbabilityFunction object and the DiscreteFunctions object has a single variable, and the variable must be the
     * same for both functions.
     */
    public double expected_value(DiscreteFunction df) {
        double ev = 0.0;
        for (int i = 0; i < df.values.length; i++) ev += values[i] * df.values[i];
        return (ev);
    }

    /**
     * Obtain posterior expected value of a DiscreteFunction This assumes that the probability values are unnormalized,
     * equal to p(x, e) where e is the evidence. The current implementation is very limited; it assumes that both the
     * ProbabilityFunction object and the DiscreteFunctions object has a single variable, and the variable must be the
     * same for both functions.
     */
    public double posterior_expected_value(DiscreteFunction df) {
        double ev = 0.0;
        double p = 0.0;
        for (int i = 0; i < df.values.length; i++) {
            p += values[i];
            ev += values[i] * df.values[i];
        }
        return (ev / p);
    }

    /**
     * Calculate the variance of a DiscreteFunction. The current implementation is very limited; it assumes that both
     * the ProbabilityFunction object and the DiscreteFunctions object has a single variable, and the variable must be
     * the same for both functions.
     */
    public double variance(DiscreteFunction df) {
        double aux, ev = 0.0, evv = 0.0;
        for (int i = 0; i < df.values.length; i++) {
            aux = values[i] * df.values[i];
            ev += aux;
            evv = df.values[i] * aux;
        }
        return (evv - ev * ev);
    }

    /**
     * Save the contents of a ProbabilityFunction object into a PrintStream in the XMLBIF v0.3 format.
     */
    public void save_xml_0_3(PrintStream out) {
        int i, j, size_of_first = 0, size_of_others = 1;
        String property;
        out.println("<DEFINITION>");
        if (variables != null) {
            out.println("\t<FOR>" + variables[0].name + "</FOR>");
            for (j = 1; j < variables.length; j++) {
                out.println("\t<GIVEN>" + variables[j].name + "</GIVEN>");
            }
            out.print("\t<TABLE>");
            if (variables.length > 1) {
                size_of_first = variables[0].number_values();
                for (j = 1; j < variables.length; j++) size_of_others *= variables[j].number_values();
                for (i = 0; i < size_of_others; i++) for (j = 0; j < size_of_first; j++) out.print(values[j * size_of_others + i] + " ");
            } else {
                for (j = 0; j < values.length; j++) out.print(values[j] + " ");
            }
            out.println("</TABLE>");
        }
        if ((properties != null) && (properties.size() > 0)) {
            for (Enumeration e = properties.elements(); e.hasMoreElements(); ) {
                property = (String) (e.nextElement());
                out.println("\t<PROPERTY>" + property + "</PROPERTY>");
            }
        }
        out.println("</DEFINITION>\n");
    }

    /**
     * Save the contents of a ProbabilityFunction object into a PrintStream.
     */
    public void save_xml(PrintStream out) {
        int j;
        String property;
        out.println("<PROBABILITY>");
        if (variables != null) {
            out.println("\t<FOR>" + variables[0].name + "</FOR>");
            for (j = 1; j < variables.length; j++) {
                out.println("\t<GIVEN>" + variables[j].name + "</GIVEN>");
            }
            out.print("\t<TABLE>");
            for (j = 0; j < values.length; j++) out.print(values[j] + " ");
            out.println("</TABLE>");
        }
        if ((properties != null) && (properties.size() > 0)) {
            for (Enumeration e = properties.elements(); e.hasMoreElements(); ) {
                property = (String) (e.nextElement());
                out.println("\t<PROPERTY>" + property + "</PROPERTY>");
            }
        }
        out.println("</PROBABILITY>\n");
    }

    /**
     * Print method.
     */
    public void print(PrintStream out) {
        int j;
        String property;
        if (variables != null) {
            out.print("probability ( ");
            for (j = 0; j < variables.length; j++) {
                out.print(" \"" + variables[j].name + "\" ");
            }
            out.print(") {");
            out.println(" //" + variables.length + " variable(s) and " + values.length + " values");
            out.println("\ttable ");
            if (variables.length == 1) {
                for (j = 0; j < values.length; j++) {
                    out.print("\t\t" + values[j]);
                    if (j == (values.length - 1)) out.print("; ");
                    out.print("\t// p(" + variables[0].values[j] + " | evidence )");
                    if (j != (values.length - 1)) out.println();
                }
            } else {
                out.print("\t\t");
                for (j = 0; j < values.length; j++) out.print(" " + values[j]);
            }
            out.print(";");
        }
        out.println();
        if ((properties != null) && (properties.size() > 0)) {
            for (Enumeration e = properties.elements(); e.hasMoreElements(); ) {
                property = (String) (e.nextElement());
                out.println("\tproperty \"" + property + "\" ;");
            }
        }
        out.println("}");
    }

    /**
     * Get the properties of the current ProbabilityFunction.
     */
    public Vector get_properties() {
        return (properties);
    }

    /**
     * Set the properties.
     */
    public void set_properties(Vector prop) {
        properties = prop;
    }

    /**
     * Get an Enumeration with the properties of the current ProbabilityFunction.
     */
    public Enumeration get_enumerated_properties() {
        return (properties.elements());
    }

    /**
     * Add a property to the current ProbabilityFunction.
     */
    public void add_property(String prop) {
        if (properties == null) properties = new Vector();
        properties.addElement(prop);
    }

    /**
     * Remove a property in the current ProbabilityFunction.
     */
    public void remove_property(String prop) {
        if (properties == null) return;
        properties.removeElement(prop);
    }

    /**
     * Remove a property in a given position in the current ProbabilityFunction.
     *
     * @param i Position of the property.
     */
    public void remove_property(int i) {
        if (properties == null) return;
        properties.removeElementAt(i);
    }
}
