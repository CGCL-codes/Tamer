package org.mgkFramework.messaging;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;

public class TestMessaging extends TestCase {

    private GatewayProxy gateway;

    private GatewayService gatewayService;

    public void testStress() {
        StressTester tester = new StressTester(this.gateway);
        tester.run(50, 30000);
    }

    public void testSendAndWait() {
        MsgEnvHlpr inMsg = MsgEnvHlpr.newInstance();
        inMsg.setName("ping");
        gateway.sendMsgWaitForResponse(inMsg);
    }

    @Override
    protected void setUp() throws Exception {
        gateway = new GatewayProxy();
        gateway.setLogin("TestUser@Test.com");
        ApplicationContext context = ContextHelper.getContext();
        gatewayService = (GatewayService) context.getBean("mes_gatewayService");
        gateway.setGateway(gatewayService);
        super.setUp();
    }
}
