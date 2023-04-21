package toxtree.plugins.func.rules;

import ambit2.smarts.query.SMARTSException;

public class FG18 extends FG {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4522164238365215591L;

    public FG18() {
        super();
        try {
            setID("FG18");
            setTitle("thiohemiaminal");
            addSubstructure("FG18", "[$([CX4]([#6])[#6]),$([CX4H][#6]),$([CX4H2])]([$([SX2H1]),$([SX2][#6])])[$([NX3]([#6])[#6]),$([NX3H][#6]),$([NX3H2])]");
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
