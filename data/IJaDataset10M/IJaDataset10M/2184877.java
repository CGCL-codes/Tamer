package net.sf.rcpforms.dnd.handlers;

import net.sf.rcpforms.dnd.IRCPDnDDropHandler;
import net.sf.rcpforms.dnd.RCPDnDEvent;

/**
 * For Dev tasks. Simply echoes 
 * added data to <code>System.out</code>
 * 
 * <p>
 * 
 * @author spicherc Apr 19, 2010
 * @version 0.1
 */
public class DummyEchoAddDropHandler implements IRCPDnDDropHandler {

    /**
	 * Gibt die Singleton-Instanz zurueck.
	 * @return die Singleton-Instanz 
	 */
    public static DummyEchoAddDropHandler getInstance() {
        if (s_instance == null) {
            s_instance = new DummyEchoAddDropHandler();
        }
        return s_instance;
    }

    /** Die Singletoninstanz. */
    private static DummyEchoAddDropHandler s_instance = null;

    protected DummyEchoAddDropHandler() {
        super();
    }

    @Override
    public void handleDnDrop(final RCPDnDEvent dnd) {
        final StringBuilder result = new StringBuilder();
        result.append(" dropped: ").append("  +-- rcpSource = ").append(dnd.rcpSource).append('\n').append("  +-- dragSource = ").append(dnd.dragSource).append('\n').append("  +-- transferArray = [\n");
        for (int i = 0; i < dnd.transferArray.length; i++) {
            result.append("     +--[" + i + "] ").append(dnd.transferArray[i]).append("]\n");
        }
        result.append("  +-- rcpTarget = ").append(dnd.rcpTarget).append('\n').append("  +-- targetViewer = ").append(dnd.targetViewer).append('\n').append("  +-- targetInput = ").append(dnd.targetInput).append('\n').append("  +-- dropIndex = ").append(dnd.dropIndex).append('\n');
        System.out.println(result.toString());
    }
}
