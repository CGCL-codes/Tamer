package no.ntnu.xqft.tree.operator;

import java.util.ArrayList;
import no.ntnu.xqft.tree.param.Param;

/**
 * @author andreas
 *
 */
public class Union extends Operator {

    public Union(ArrayList<Param> params, ArrayList<Operator> operators) {
        super(params, operators);
        this.name = "union";
    }

    public Union(ArrayList<Operator> operators) {
        super(null, operators);
        this.name = "union";
    }
}
