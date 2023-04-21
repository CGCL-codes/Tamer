package mipt.math.sys.num.fuzzy.ode;

import java.io.Writer;
import mipt.math.Number;
import mipt.math.array.Vector;
import mipt.math.fuzzy.lincomb.CombinationNumber;
import mipt.math.fuzzy.lincomb.ConservativeLinearCombination;
import mipt.math.fuzzy.lincomb.LinearCombination;
import mipt.math.sys.num.Inexactitude;

/**
 * WriterODESolution for FuzzyNumbers with LinearCombination inside: writes value,
 *   error; comination sum, coefficients (term.x.value after each) and free term  
 * Call setWriteRelativeError(true) to write relative error instead of combination sum
 */
public class CombinationWriterODESolution extends FuzzyWriterODESolution {

    public CombinationWriterODESolution(Writer writer) {
        super(writer);
    }

    public CombinationWriterODESolution(Writer writer, int columnWidth, String separator, boolean writeIndex) {
        super(writer, columnWidth, separator, writeIndex);
    }

    public void addPoint(Number x, Vector y, int pointIndex, Inexactitude inexact) {
        if (pointIndex == 0) return;
        super.addPoint(x, y, pointIndex, inexact);
    }

    /**
	 * 
	 */
    protected String number2string(Number x) {
        if (!(x instanceof CombinationNumber)) return super.number2string(x);
        CombinationNumber X = (CombinationNumber) x;
        StringBuffer buf = new StringBuffer(30 + 20 * X.getCombination().size());
        double value = X.doubleValue();
        buf.append(double2string(value));
        buf.append(separator);
        double error = X.doubleError();
        buf.append(double2string(error));
        buf.append(separator);
        if (shouldWriteRelativeError()) {
            value = calcRelativeError(error, value);
        } else {
            value = X.getCombinationValue();
        }
        buf.append(double2string(value));
        java.util.Iterator terms = X.getCombination().termIterator();
        while (terms.hasNext()) {
            LinearCombination.Term term = (LinearCombination.Term) terms.next();
            buf.append(separator);
            buf.append(double2string(term.c));
            buf.append(separator);
            buf.append(double2string(term.x.doubleValue()));
        }
        if (X.getCombination() instanceof ConservativeLinearCombination) {
            buf.append(separator);
            buf.append(double2string(((ConservativeLinearCombination) X.getCombination()).getX0()));
        }
        return buf.toString();
    }
}
