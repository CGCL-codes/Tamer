package items;

/** */
public interface TermIF {

    /** */
    public double evaluate(double x);

    /** */
    public TermIF differentiate();

    /** */
    public String toString();

    /** */
    public void addTerm(TermIF term);

    public TermIF[] getTerms();

    public double getCoefficient();

    public double getAttribute();
}
