package net.sf.gridarta.model.match;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Maintains {@link GameObjectMatcher} instances.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class GameObjectMatchers implements Iterable<NamedGameObjectMatcher>, Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Map with game object object matchers and their IDs.
     * @serial
     */
    @NotNull
    private final Map<String, NamedGameObjectMatcher> gameObjectMatchersByIds = new HashMap<String, NamedGameObjectMatcher>();

    /**
     * List with game object matchers.
     * @serial
     */
    @NotNull
    private final Collection<NamedGameObjectMatcher> gameObjectMatchers = new ArrayList<NamedGameObjectMatcher>();

    /**
     * Returns a matcher by id; returns <code>null</code> if the matcher does
     * not exist.
     * @param ids the ids to find
     * @return the matcher, or <code>null</code> if no such matcher exists
     */
    @Nullable
    public GameObjectMatcher getMatcher(@NotNull final String... ids) {
        for (final String id : ids) {
            final GameObjectMatcher matcher = gameObjectMatchersByIds.get(id);
            if (matcher != null) {
                return matcher;
            }
        }
        return null;
    }

    /**
     * Returns a matcher by id; prints a warning if the matcher does not exist.
     * @param errorViewCollector the error view collector to use
     * @param ids the ids to find
     * @return the matcher, or <code>null</code> if no such matcher exists
     */
    @Nullable
    public GameObjectMatcher getMatcherWarn(@NotNull final ErrorViewCollector errorViewCollector, @NotNull final String... ids) {
        final GameObjectMatcher matcher = getMatcher(ids);
        if (matcher == null) {
            errorViewCollector.addWarning(ErrorViewCategory.GAMEOBJECTMATCHERS_ENTRY_INVALID, "GameObjectMatcher '" + ids[0] + "' does not exist");
        }
        return matcher;
    }

    /**
     * Return all known game object matchers that should be used as filters.
     * @return the filter game object matches
     */
    @NotNull
    public Collection<NamedGameObjectMatcher> getFilters() {
        final Collection<NamedGameObjectMatcher> result = new HashSet<NamedGameObjectMatcher>(gameObjectMatchersByIds.size());
        for (final NamedGameObjectMatcher matcher : gameObjectMatchersByIds.values()) {
            if (!matcher.isSystemMatcher()) {
                result.add(matcher);
            }
        }
        return result;
    }

    /**
     * Adds a new {@link GameObjectMatcher}.
     * @param gameObjectMatcher the game object matcher to add
     */
    public void addGameObjectMatcher(@NotNull final NamedGameObjectMatcher gameObjectMatcher) {
        gameObjectMatchers.add(gameObjectMatcher);
        gameObjectMatchersByIds.put(gameObjectMatcher.getID(), gameObjectMatcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<NamedGameObjectMatcher> iterator() {
        return Collections.unmodifiableCollection(gameObjectMatchers).iterator();
    }
}
