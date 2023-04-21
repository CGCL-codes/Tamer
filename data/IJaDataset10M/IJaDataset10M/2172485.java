package toxtree.plugins.func.rules;

import toxTree.tree.rules.StructureAlert;
import ambit2.smarts.query.SMARTSException;

public class FG extends StructureAlert {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4522164238365215591L;

    public FG() {
        super();
    }

    @Override
    public void setTitle(String name) {
        super.setTitle(name);
        setExplanation(title);
    }

    @Override
    public void addSubstructure(String title, String smarts) throws SMARTSException {
        super.addSubstructure(title, smarts);
        setExplanation(String.format("%s<p>SMARTS: %s", getExplanation(), smarts));
        setExamples(new String[] { null, smarts });
    }
}
