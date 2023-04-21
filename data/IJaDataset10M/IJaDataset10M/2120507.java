package pl.org.minions.stigma.chat.client;

/**
 * Interface used for communication between chat processing
 * objects.
 */
public interface ChatTargetChangedListener {

    /**
     * Called when default chat target changes.
     * @param target
     *            new default target.
     */
    void chatTargetChoosen(ChatTarget target);
}
