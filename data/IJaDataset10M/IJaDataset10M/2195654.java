package toxtree.plugins.func.rules;

import ambit2.smarts.query.SMARTSException;

public class FG51_1 extends FG {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4522164238365215591L;

    public FG51_1() {
        super();
        try {
            setID("FG51_1");
            setTitle("thiocarbonic acid monoester");
            addSubstructure("FG51_1", "[C;$(C[O][#6])](=[SX1])[OH]");
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
