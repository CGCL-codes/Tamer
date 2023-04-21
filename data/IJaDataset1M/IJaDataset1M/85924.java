package JessTab;

import jess.*;
import java.util.Iterator;
import edu.stanford.smi.protege.model.Instance;

/**
 *
 *  Panel (tab) for displaying Jess rules.
 *
 *
 * @author Henrik Eriksson
 * @version 1.0
 * @see JessTab
 * @see JConsolePanel
 * @see FactsPanel
 */
public class RulesPanel extends AbstractConstructPanel {

    /**
     * Get a list of the Jess constructs.
     * @return an iterator that enumerates the constructs
     */
    protected Iterator listConstructs() {
        return getEngine().listDefrules();
    }

    /**
     * Get the name of a Jess construct.
     * o the construct object
     * @return the name
     */
    protected String getName(Object o) {
        return ((HasLHS) o).getName();
    }

    /**
     * Get the Protege instance for a Jess construct.
     * @param val the construct
     * @return the corresponding instance
     */
    protected Instance getInstance(Object val) {
        return JessTabSystemClasses.getInstance((HasLHS) val);
    }

    /**
     * Constructor for rules panel.
     * @param engine the Jess engine
     */
    public RulesPanel(Rete engine) {
        super(engine, "Rule");
    }
}
