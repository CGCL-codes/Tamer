package com.turnengine.client.local.error.command.set;

import com.javabi.codebuilder.generated.IGeneratedCommandSet;
import com.turnengine.client.local.error.bean.ILocalTrace;
import java.util.List;

/**
 * The I Local Error Command Set.
 */
public interface ILocalErrorCommandSet extends IGeneratedCommandSet {

    ILocalTrace getLocalTrace(long loginId, int instanceId, String id);

    List<ILocalTrace> getLocalTraceList(long loginId, int instanceId, long from, long to);
}
