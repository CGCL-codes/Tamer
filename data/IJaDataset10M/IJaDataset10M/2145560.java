package rbe;

public class EBTestFactory extends EBFactory {

    private static final EBTransition homeTrans = new EBSimpleTrans("http://www.cs.wisc.edu/~heil/");

    private static final EBTransition resTrans = new EBSimpleTrans("http://www.cae.wisc.edu/~ecearch/strata/");

    private static final EBTransition famTrans = new EBSimpleTrans("http://www.cs.wisc.edu/~heil/family.html");

    private int count = 1;

    private static final int[][] transProb = { { 3000, 6000, 9999 }, { 3000, 6000, 9999 }, { 3000, 6000, 9999 } };

    private static final EBTransition[][] trans = { { homeTrans, resTrans, famTrans }, { homeTrans, resTrans, famTrans }, { homeTrans, resTrans, famTrans } };

    public EB getEB(RBE rbe) {
        return (new EB(rbe, transProb, trans, 3, "Test EB #" + (count++)));
    }
}
