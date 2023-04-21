package net.sourceforge.cruisecontrol.sourcecontrols.accurev;

/**
 * Implements a "numeric" time-spec (n or n.m) format.
 *
 * @author <a href="mailto:Nicola_Orru@scee.net">Nicola Orru'</a>
 */
public final class TransactionNumberTimespec extends Timespec {

    /**
     * Convenience constant containing the KewordTimespec "highest"
     */
    public static final KeywordTimespec HIGHEST = new KeywordTimespec("highest");

    private int transaction;

    /**
     * Creates a new TransactionNumberTimespec with no count (n form)
     *
     * @param transaction the transaction number
     */
    public TransactionNumberTimespec(int transaction) {
        this.transaction = transaction;
    }

    /**
     * Creates a new TransactionNumberTimespec with the given count (n.m form)
     *
     * @param transaction the transaction number
     * @param count       the count
     */
    public TransactionNumberTimespec(int transaction, int count) {
        super(count);
        this.transaction = transaction;
    }

    /**
     * formats the transaction (main) part onto a String
     *
     * @return the transaction number as a string
     */
    public String format() {
        return Integer.toString(transaction);
    }
}
