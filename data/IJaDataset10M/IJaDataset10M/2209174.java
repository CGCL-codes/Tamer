package bioweka.analyzers;

import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojava.bio.symbol.SimpleSymbolList;
import org.biojava.bio.symbol.SymbolList;
import org.biojava.bio.symbol.SymbolPropertyTable;
import bioweka.core.debuggers.Debugger;

/**
 * Abstract base class for simple sequence analyzers.
 * @author <a href="mailto:Martin.Szugat@GMX.net">Martin Szugat</a>
 * @version $Revision: 1.2 $
 */
public abstract class AbstractSimpleSequenceAnalyzer extends AbstractSequenceAnalyzer {

    /**
     * Initializes the simple sequence analyzers.
     */
    public AbstractSimpleSequenceAnalyzer() {
        super();
    }

    /**
     * Analyzes the symbol property values of a sequence and returns a numeric
     * value for this sequence. The list of symbol property values may contain
     * {@link Double#NaN} values to indicate that for the belonging symbol in
     * the sequence no symbol property value could be retrieved from the symbol
     * property table.
     * @param values the symbol property values for the sequence passed to the
     * {@link #analyze(SymbolPropertyTable, String)} method
     * @return the return value for the public <code>analyze</code> method
     * @throws NullPointerException if <code>values</code> is
     * <code>null</code>.
     */
    protected abstract double analyze(double[] values) throws NullPointerException;

    /**
     * {@inheritDoc}
     */
    public final double analyze(SymbolPropertyTable table, String sequence) throws Exception {
        Debugger debugger = getDebugger();
        debugger.enters("analyze", table, sequence);
        double result = Double.NaN;
        try {
            if (sequence.length() > 0) {
                SymbolTokenization tokenizer = table.getAlphabet().getTokenization(TOKENIZER);
                SymbolList symbolList = new SimpleSymbolList(tokenizer, sequence);
                double[] values = new double[symbolList.length()];
                for (int i = 1; i <= symbolList.length(); i++) {
                    try {
                        values[i - 1] = table.getDoubleValue(symbolList.symbolAt(i));
                    } catch (Exception e) {
                        values[i - 1] = Double.NaN;
                    }
                }
                result = analyze(values);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        debugger.exits("analyze", new Double(result));
        return result;
    }
}
