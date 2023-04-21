package org.gamegineer.client.internal.core.config;

import org.gamegineer.client.core.config.AbstractGameClientConfigurationTestCase;
import org.gamegineer.client.core.config.IGameClientConfiguration;
import org.gamegineer.client.core.system.FakeGameSystemUiSource;

/**
 * A fixture for testing the
 * {@link org.gamegineer.client.internal.core.config.GameClientConfiguration}
 * class to ensure it does not violate the contract of the
 * {@link org.gamegineer.client.core.config.IGameClientConfiguration} interface.
 */
public final class GameClientConfigurationAsGameClientConfigurationTest extends AbstractGameClientConfigurationTestCase {

    /**
     * Initializes a new instance of the
     * {@code GameClientConfigurationAsGameClientConfigurationTest} class.
     */
    public GameClientConfigurationAsGameClientConfigurationTest() {
        super();
    }

    @Override
    protected IGameClientConfiguration createGameClientConfiguration() {
        return GameClientConfiguration.createGameClientConfiguration(new FakeGameSystemUiSource());
    }
}
