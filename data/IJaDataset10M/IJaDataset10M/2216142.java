package net.sf.gridarta.model.validation.checks;

import java.awt.Point;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.match.GameObjectMatcher;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.model.validation.ValidatorPreferences;
import net.sf.gridarta.model.validation.errors.ShopSquare2Error;
import net.sf.gridarta.model.validation.errors.ShopSquareError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link net.sf.gridarta.model.validation.MapValidator} to detect shop
 * squares which allow magic or prayers.
 * @author Andreas Kirschbaum
 */
public class ShopSquareChecker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends AbstractShopSquareChecker<G, A, R> {

    /**
     * The {@link GameObjectMatcher} for finding shop squares.
     */
    @NotNull
    private final GameObjectMatcher shopMatcher;

    /**
     * The {@link GameObjectMatcher} for finding no-spell squares.
     */
    @NotNull
    private final GameObjectMatcher noSpellsMatcher;

    /**
     * The {@link GameObjectMatcher} for finding blocked squares.
     */
    @Nullable
    private final GameObjectMatcher blockedMatcher;

    /**
     * Creates a new instance.
     * @param validatorPreferences the validator preferences to use
     * @param shopMatcher the game object matcher for finding shop squares
     * @param noSpellsMatcher the game object matcher for finding no-spell
     * squares
     * @param blockedMatcher the game object matcher for finding blocked
     * squares
     */
    public ShopSquareChecker(@NotNull final ValidatorPreferences validatorPreferences, @NotNull final GameObjectMatcher shopMatcher, @NotNull final GameObjectMatcher noSpellsMatcher, @Nullable final GameObjectMatcher blockedMatcher) {
        super(validatorPreferences);
        this.shopMatcher = shopMatcher;
        this.noSpellsMatcher = noSpellsMatcher;
        this.blockedMatcher = blockedMatcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMap(@NotNull final MapModel<G, A, R> mapModel, @NotNull final ErrorCollector<G, A, R> errorCollector) {
        final boolean[][] shopSquares = findMatchingSquares(mapModel, shopMatcher);
        final boolean[][] noSpellsSquares = findMatchingSquares(mapModel, noSpellsMatcher);
        final boolean[][] blockedSquares = blockedMatcher == null ? null : findMatchingSquares(mapModel, blockedMatcher);
        final Point point = new Point();
        for (point.x = 0; point.x < shopSquares.length && point.x < noSpellsSquares.length; point.x++) {
            for (point.y = 0; point.y < shopSquares[point.x].length && point.y < noSpellsSquares[point.x].length; point.y++) {
                if (shopSquares[point.x][point.y]) {
                    if (!noSpellsSquares[point.x][point.y]) {
                        errorCollector.collect(new ShopSquareError<G, A, R>(mapModel.getMapSquare(point)));
                    } else if (blockedSquares != null && hasAdjacentNonBlockedSpellsAllowedSquare(noSpellsSquares, blockedSquares, point)) {
                        errorCollector.collect(new ShopSquare2Error<G, A, R>(mapModel.getMapSquare(point)));
                    }
                }
            }
        }
    }

    /**
     * Returns whether a given square is not blocked and has a non-blocked
     * adjacent square that allows spells.
     * @param noSpellsSquares the map squares disallowing spells
     * @param blockedSquares the blocked map squares
     * @param point the map square to check
     * @return whether a match was found
     */
    private static boolean hasAdjacentNonBlockedSpellsAllowedSquare(@NotNull final boolean[][] noSpellsSquares, @NotNull final boolean[][] blockedSquares, @NotNull final Point point) {
        if (blockedSquares[point.x][point.y]) {
            return false;
        }
        for (int dx = -1; dx <= +1; dx++) {
            for (int dy = -1; dy <= +1; dy++) {
                final int x = point.x + dx;
                final int y = point.y + dy;
                try {
                    if (!noSpellsSquares[x][y] && !blockedSquares[x][y]) {
                        return true;
                    }
                } catch (final ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
        return false;
    }
}
