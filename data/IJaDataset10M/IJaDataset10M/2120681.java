package com.turnengine.client.global.game.command;

import com.javabi.codebuilder.generated.IGeneratedCommand;
import com.turnengine.client.global.command.IGlobalExecutableCommand;
import com.turnengine.client.global.game.bean.IGameDefinition;

/**
 * The I Get Game Definition.
 */
public interface IGetGameDefinition extends IGeneratedCommand, IGlobalExecutableCommand<IGameDefinition>, Comparable<IGetGameDefinition> {

    /**
	 * Returns the instance id.
	 * @return the instance id.
	 */
    int getInstanceId();

    /**
	 * Sets the instance id.
	 * @param instanceId the instance id to set.
	 */
    void setInstanceId(int instanceId);
}
