package org.gamegineer.game.internal.core.config;

import java.util.ArrayList;
import java.util.List;
import org.gamegineer.game.core.config.Configurations;
import org.gamegineer.game.core.config.IGameConfiguration;
import org.gamegineer.game.core.config.IPlayerConfiguration;
import org.gamegineer.game.core.system.GameSystems;
import org.gamegineer.game.core.system.IGameSystem;
import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.game.internal.core.config.ConfigurationUtils} class.
 */
public final class ConfigurationUtilsTest {

    /**
     * Initializes a new instance of the {@code ConfigurationUtilsTest} class.
     */
    public ConfigurationUtilsTest() {
        super();
    }

    private static IGameConfiguration createGameConfiguration(final String id, final String name, final IGameSystem gameSystem, final List<IPlayerConfiguration> playerConfigs) {
        assert id != null;
        assert name != null;
        assert gameSystem != null;
        assert playerConfigs != null;
        return new IGameConfiguration() {

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public List<IPlayerConfiguration> getPlayers() {
                return new ArrayList<IPlayerConfiguration>(playerConfigs);
            }

            public IGameSystem getSystem() {
                return gameSystem;
            }
        };
    }

    private static IPlayerConfiguration createPlayerConfiguration(final String roleId, final String userId) {
        assert roleId != null;
        assert userId != null;
        return new IPlayerConfiguration() {

            public String getRoleId() {
                return roleId;
            }

            public String getUserId() {
                return userId;
            }
        };
    }

    /**
     * Ensures the {@code assertGameConfigurationLegal} method throws an
     * exception when passed an illegal game configuration due to a player
     * configuration list that contains a duplicate role.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAssertGameConfigurationLegal_GameConfig_Illegal_PlayerConfigs_DuplicateRole() {
        final String id = "id";
        final String name = "name";
        final IGameSystem gameSystem = GameSystems.createUniqueGameSystem();
        final List<IPlayerConfiguration> playerConfigs = Configurations.createPlayerConfigurationList(gameSystem);
        playerConfigs.set(0, Configurations.createPlayerConfiguration(gameSystem.getRoles().get(1).getId()));
        ConfigurationUtils.assertGameConfigurationLegal(createGameConfiguration(id, name, gameSystem, playerConfigs));
    }

    /**
     * Ensures the {@code assertGameConfigurationLegal} method throws an
     * exception when passed an illegal game configuration due to a player
     * configuration list that is missing at least one role.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAssertGameConfigurationLegal_GameConfig_Illegal_PlayerConfigs_MissingRole() {
        final String id = "id";
        final String name = "name";
        final IGameSystem gameSystem = GameSystems.createUniqueGameSystem();
        final List<IPlayerConfiguration> playerConfigs = Configurations.createPlayerConfigurationList(gameSystem);
        playerConfigs.remove(0);
        ConfigurationUtils.assertGameConfigurationLegal(createGameConfiguration(id, name, gameSystem, playerConfigs));
    }

    /**
     * Ensures the {@code assertGameConfigurationLegal} method throws an
     * exception when passed an illegal game configuration due to a player
     * configuration list that contains at least one unknown role.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAssertGameConfigurationLegal_GameConfig_Illegal_PlayerConfigs_UnknownRole() {
        final String id = "id";
        final String name = "name";
        final IGameSystem gameSystem = GameSystems.createUniqueGameSystem();
        final List<IPlayerConfiguration> playerConfigs = Configurations.createPlayerConfigurationList(gameSystem);
        playerConfigs.set(0, Configurations.createUniquePlayerConfiguration());
        ConfigurationUtils.assertGameConfigurationLegal(createGameConfiguration(id, name, gameSystem, playerConfigs));
    }

    /**
     * Ensures the {@code assertGameConfigurationLegal} method does nothing when
     * passed a legal game configuration.
     */
    @Test
    public void testAssertGameConfigurationLegal_GameConfig_Legal() {
        final String id = "id";
        final String name = "name";
        final IGameSystem gameSystem = GameSystems.createUniqueGameSystem();
        final List<IPlayerConfiguration> playerConfigs = Configurations.createPlayerConfigurationList(gameSystem);
        ConfigurationUtils.assertGameConfigurationLegal(createGameConfiguration(id, name, gameSystem, playerConfigs));
    }

    /**
     * Ensures the {@code assertGameConfigurationLegal} method throws an
     * exception when passed a {@code null} game configuration.
     */
    @Test(expected = NullPointerException.class)
    public void testAssertGameConfigurationLegal_GameConfig_Null() {
        ConfigurationUtils.assertGameConfigurationLegal(null);
    }

    /**
     * Ensures the {@code assertPlayerConfigurationLegal} method does nothing
     * when passed a legal player configuration.
     */
    @Test
    public void testAssertPlayerConfigurationLegal_PlayerConfig_Legal() {
        final String roleId = "role-id";
        final String userId = "user-id";
        ConfigurationUtils.assertPlayerConfigurationLegal(createPlayerConfiguration(roleId, userId));
    }

    /**
     * Ensures the {@code assertPlayerConfigurationLegal} method throws an
     * exception when passed a {@code null} player configuration.
     */
    @Test(expected = NullPointerException.class)
    public void testAssertPlayerConfigurationLegal_PlayerConfig_Null() {
        ConfigurationUtils.assertPlayerConfigurationLegal(null);
    }
}
