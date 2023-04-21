package tr.view.contexts.screen;

import java.util.Collection;

/**
 * A provider of context nodes.
 *
 * @author <a href="mailto:jimoore@netspace.net.au">Jeremy Moore</a>
 */
public interface ContextNodeProvider {

    /**
     * Provide the given context nodes.
     * @param contextNodes the context nodes.
     */
    public void provide(Collection<ContextNode> contextNodes);
}
