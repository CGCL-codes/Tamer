package org.gamegineer.game.ui.system;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import net.jcip.annotations.ThreadSafe;
import org.gamegineer.game.internal.ui.system.NullGameSystemUi;
import org.gamegineer.game.internal.ui.system.NullRoleUi;

/**
 * A collection of useful methods for working with game system user interfaces.
 * 
 * <p>
 * This class is thread-safe.
 * </p>
 */
@ThreadSafe
public final class GameSystemUiUtils {

    /**
     * Initializes a new instance of the {@code GameSystemUiUtils} class.
     */
    private GameSystemUiUtils() {
        super();
    }

    public static IGameSystemUi getGameSystemUi(final IGameSystemUiFactory gameSystemUiFactory, final String id) {
        assertArgumentNotNull(gameSystemUiFactory, "gameSystemUiFactory");
        assertArgumentNotNull(id, "id");
        final IGameSystemUi gameSystemUi = gameSystemUiFactory.getGameSystemUi(id);
        if (gameSystemUi != null) {
            return gameSystemUi;
        }
        return new NullGameSystemUi(id);
    }

    public static IRoleUi getRoleUi(final IGameSystemUi gameSystemUi, final String id) {
        assertArgumentNotNull(gameSystemUi, "gameSystemUi");
        assertArgumentNotNull(id, "id");
        for (final IRoleUi roleUi : gameSystemUi.getRoles()) {
            if (id.equals(roleUi.getId())) {
                return roleUi;
            }
        }
        return new NullRoleUi(id);
    }

    /**
     * A factory for obtaining a game system user interface with a specific
     * identifier.
     */
    public interface IGameSystemUiFactory {

        public IGameSystemUi getGameSystemUi(final String id);
    }
}
