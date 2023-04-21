package org.mgkFramework.messaging;

import java.util.Collection;

public interface IGatewayProxy {

    public void sendMsgAsync(MsgEnvHlpr inMsg);

    public abstract Collection<MsgEnvHlpr> getAsynchResponses();

    public MsgEnvHlpr sendMsgWaitForResponse(MsgEnvHlpr inMsg);
}
