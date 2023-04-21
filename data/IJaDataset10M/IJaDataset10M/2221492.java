package net.grinder.synchronisation.messages;

import net.grinder.communication.Message;

/**
 * Barrier group message sent to agents when a barrier is opened.
 *
 * @author Philip Aston
 * @version $Revision:$
 */
public class OpenBarrierMessage implements Message {

    private static final long serialVersionUID = 1L;

    private final String m_name;

    /**
   * Constructor.
   *
   * @param name
   *          Barrier name.
   */
    public OpenBarrierMessage(String name) {
        m_name = name;
    }

    /**
   * Barrier name.
   *
   * @return The barrier name.
   */
    public String getName() {
        return m_name;
    }
}
