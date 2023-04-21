package com.turnengine.client.global.game.command;

import com.javabi.codebuilder.generated.IGeneratedCommand;
import com.turnengine.client.global.command.IGlobalExecutableCommand;
import com.turnengine.client.global.game.bean.IGameInstance;

/**
 * The I New Game Instance.
 */
public interface INewGameInstance extends IGeneratedCommand, IGlobalExecutableCommand<IGameInstance>, Comparable<INewGameInstance> {

    /**
	 * Returns the login id.
	 * @return the login id.
	 */
    long getLoginId();

    /**
	 * Returns the game name.
	 * @return the game name.
	 */
    String getGameName();

    /**
	 * Returns the version name.
	 * @return the version name.
	 */
    String getVersionName();

    /**
	 * Returns the instance name.
	 * @return the instance name.
	 */
    String getInstanceName();

    /**
	 * Returns the host id.
	 * @return the host id.
	 */
    int getHostId();

    /**
	 * Sets the login id.
	 * @param loginId the login id to set.
	 */
    void setLoginId(long loginId);

    /**
	 * Sets the game name.
	 * @param gameName the game name to set.
	 */
    void setGameName(String gameName);

    /**
	 * Sets the version name.
	 * @param versionName the version name to set.
	 */
    void setVersionName(String versionName);

    /**
	 * Sets the instance name.
	 * @param instanceName the instance name to set.
	 */
    void setInstanceName(String instanceName);

    /**
	 * Sets the host id.
	 * @param hostId the host id to set.
	 */
    void setHostId(int hostId);
}
