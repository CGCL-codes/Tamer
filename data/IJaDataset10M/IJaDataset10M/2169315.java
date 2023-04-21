package domain.simplex.control;

import domain.model.Variable;
import domain.simplex.SimplexRestriction;
import domain.model.numbers.Number;
import domain.model.numbers.NumberModel;
import java.util.ArrayList;
import java.util.Iterator;
import util.Common;

/**
 *
 * @author Vitucho
 */
public class OptimalMatrixAnalizator {

    ResolverControl rc;

    Number[][] matrix;

    public OptimalMatrixAnalizator(ResolverControl rc) {
        this.rc = rc;
    }

    public void calc() {
        int i, j;
        int n = rc.getRestrictionsCount();
        Number m;
        matrix = new Number[n][n];
        ArrayList<SimplexRestriction> simplexRests = rc.getRestrictions();
        i = 0;
        for (SimplexRestriction srest : simplexRests) {
            Variable v = srest.getBaseTerm().getVar();
            m = (rc.getVariableRepository().isFictional(v)) ? NumberModel.getMinusOne() : NumberModel.getOne();
            VarInformation vi = rc.getColVarInformation(v);
            Iterator<VarRelation> iter = vi.getRelations().iterator();
            j = 0;
            while (iter.hasNext()) {
                Number number = iter.next().getCofactor();
                matrix[j][i] = number.multiply(m);
                j++;
            }
            i++;
        }
    }

    public Number[][] getMatrix() {
        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int k = 0; k < matrix.length; k++) {
            Number[] numbers = matrix[k];
            for (int l = 0; l < numbers.length; l++) {
                Number number = numbers[l];
                res.append(number).append(",");
            }
            res.append(Common.LINE_SEP);
        }
        return res.toString();
    }
}
