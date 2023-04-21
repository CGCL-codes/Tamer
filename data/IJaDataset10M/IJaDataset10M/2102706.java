package org.ez.messageGateway;

public class EZSendMsgController extends BaseEZMessageGatewayController {

    @Override
    protected void processMessage(IEZMsgEnv inMsg, IEZMsgEnv responseEnv) {
        this.gatewayFactory.getGateway(inMsg.getConnectionToken()).sendIncoming(inMsg);
    }
}
