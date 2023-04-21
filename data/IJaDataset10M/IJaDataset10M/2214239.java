package com.turnengine.client.local.upkeep.command.gwt;

import com.google.common.annotations.GwtCompatible;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.javabi.codebuilder.generated.gwt.IGeneratedRemoteServiceAsync;
import com.turnengine.client.local.upkeep.bean.IUpkeepCondition;
import com.turnengine.client.local.upkeep.enums.UpkeepConditionType;

/**
 * The Add Upkeep Condition Service Async.
 */
@GwtCompatible
public interface AddUpkeepConditionServiceAsync extends IGeneratedRemoteServiceAsync {

    void addUpkeepCondition(long loginId, int instanceId, int upkeepId, UpkeepConditionType type, int conditionId, long conditionAmount, AsyncCallback<IUpkeepCondition> calback);
}
