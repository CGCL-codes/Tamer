package net.sf.gridarta.model.match;

import java.util.ArrayList;
import java.util.Collection;
import net.sf.gridarta.model.gameobject.GameObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link GameObjectMatcher} that And-combines other <code>GameObjectMatcher</code>s.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class AndGameObjectMatcher implements GameObjectMatcher {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The list of {@link GameObjectMatcher GameObjectMatchers} to AND.
     * @serial
     */
    @NotNull
    private final Collection<GameObjectMatcher> matchers = new ArrayList<GameObjectMatcher>();

    /**
     * Creates an <code>AndGameObjectMatcher</code>.
     * @param matchers the matchers to And
     */
    public AndGameObjectMatcher(@NotNull final Collection<GameObjectMatcher> matchers) {
        this.matchers.addAll(matchers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMatching(@NotNull final GameObject<?, ?, ?> gameObject) {
        for (final GameObjectMatcher gameObjectMatcher : matchers) {
            if (!gameObjectMatcher.isMatching(gameObject)) {
                return false;
            }
        }
        return true;
    }
}
