package toxtree.plugins.func.rules;

import ambit2.smarts.query.SMARTSException;

public class FG79_3 extends FG {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4522164238365215591L;

    public FG79_3() {
        super();
        try {
            setID("FG79_3");
            setTitle("sulfenic acid halide");
            addSubstructure("FG79_3", "[SX2]([#6])([I,Br,Cl,F])");
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
