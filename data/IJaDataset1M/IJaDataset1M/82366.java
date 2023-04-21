package no.ntnu.xqft.tree.operator;

import java.util.ArrayList;
import no.ntnu.xqft.tree.param.*;

/**
 * @author andreas
 *
 */
public class Lookup extends Operator {

    /**
     * Constructor
     * 
     * @param params
     * @param operators
     */
    public Lookup(ArrayList<Param> params, ArrayList<Operator> operators) {
        super(params, operators);
        this.name = "LOOKUP";
    }

    /**
     * Performs a term lookup and returns a set
     * 
     * @param term
     */
    public Lookup(String term) {
        super(null, null);
        this.name = "lookup";
        this.params = new ArrayList<Param>();
        this.operators = new ArrayList<Operator>();
        params.add(new StringLiteral(term));
    }

    /**
     * Performs a term lookup and returns a set
     * 
     * @param term
     */
    public Lookup(String term, ArrayList<Operator> operators) {
        super(null, null);
        this.params = new ArrayList<Param>();
        this.operators = operators;
        params.add(new StringLiteral(term));
    }
}
