package org.gamegineer.server.core.system;

import net.jcip.annotations.ThreadSafe;
import org.gamegineer.server.internal.core.system.RegistryGameSystemSource;

/**
 * A factory for creating game system sources.
 * 
 * <p>
 * This class is thread-safe.
 * </p>
 * 
 * <p>
 * This class is not intended to be extended by clients.
 * </p>
 */
@ThreadSafe
public final class GameSystemSourceFactory {

    /**
     * Initializes a new instance of the {@code GameSystemSourceFactory} class.
     */
    private GameSystemSourceFactory() {
        super();
    }

    public static IGameSystemSource createRegistryGameSystemSource() {
        return new RegistryGameSystemSource();
    }
}
