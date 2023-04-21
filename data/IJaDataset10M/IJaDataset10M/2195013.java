package com.turnengine.client.local.unit.command;

import com.javabi.codebuilder.generated.IGeneratedCommand;
import com.turnengine.client.local.command.ILocalExecutableCommand;
import com.turnengine.client.local.unit.bean.IStorageItem;
import java.util.List;

/**
 * The I Get Storage Items.
 */
public interface IGetStorageItems extends IGeneratedCommand, ILocalExecutableCommand<List<IStorageItem>>, Comparable<IGetStorageItems> {

    /**
	 * Returns the login id.
	 * @return the login id.
	 */
    long getLoginId();

    /**
	 * Returns the instance id.
	 * @return the instance id.
	 */
    int getInstanceId();

    /**
	 * Sets the login id.
	 * @param loginId the login id to set.
	 */
    void setLoginId(long loginId);

    /**
	 * Sets the instance id.
	 * @param instanceId the instance id to set.
	 */
    void setInstanceId(int instanceId);
}
