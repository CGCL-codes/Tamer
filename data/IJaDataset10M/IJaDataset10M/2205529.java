package net.sf.gridarta.model.gameobject;

import net.sf.gridarta.model.archetypetype.ArchetypeType;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for {@link GameObject} related functions.
 * @author Andreas Kirschbaum
 */
public class GameObjectUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private GameObjectUtils() {
    }

    /**
     * This method checks the objectText for syntax errors. More precisely: It
     * reads every line in the objectText and looks if it matches the
     * type-definitions (-&gt; see {@link net.sf.gridarta.model.archetypetype.ArchetypeTypeSet})
     * for this base object. If there is no match, the line is considered wrong.
     * Of course the type-definitions will never be perfect, this should be kept
     * in mind. <p/> Note that the archetype is ignored in the check. The
     * default arches should be correct, and even if not - it isn't the map
     * maker to blame.
     * @param gameObject the game object to check
     * @param type the type structure belonging to this base object
     * @return a String with all lines which don't match the
     *         type-definitions.<br> If no such "errors" encountered, null is
     *         returned
     */
    @Nullable
    public static String getSyntaxErrors(@NotNull final BaseObject<?, ?, ?, ?> gameObject, @NotNull final ArchetypeType type) {
        final StringBuilder errors = new StringBuilder();
        for (final String line : StringUtils.PATTERN_END_OF_LINE.split(gameObject.getObjectText())) {
            final int spaceIndex = line.indexOf(' ');
            final Comparable<String> attrKey = spaceIndex == -1 ? line : line.substring(0, spaceIndex);
            if (!attrKey.equals(BaseObject.DIRECTION) && !attrKey.equals(BaseObject.TYPE) && !attrKey.equals(BaseObject.NAME) && !type.hasAttributeKey(attrKey)) {
                errors.append(line.trim()).append('\n');
            }
        }
        final String retErrors = errors.toString();
        if (retErrors.trim().length() == 0) {
            return null;
        }
        return retErrors;
    }
}
