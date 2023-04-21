package org.personalsmartspace.demoservice.confmgrclient.impl;

import org.personalsmartspace.demoservice.confserv.onm_msg_service.MessageFromConfService;
import org.personalsmartspace.onm.api.pss3p.CallbackListener;
import org.personalsmartspace.onm.api.pss3p.XMLConverter;

public class TestServiceCallbackImpl implements CallbackListener {

    private ConfMgrServlet confmgrservlet;

    public TestServiceCallbackImpl(ConfMgrServlet confMgrServlet) {
        this.confmgrservlet = confMgrServlet;
    }

    @Override
    public void handleCallback(String returnJsonMsgXML) {
        MessageFromConfService returnJsonMsg = (MessageFromConfService) XMLConverter.xmlToObject(returnJsonMsgXML, MessageFromConfService.class);
        System.out.println("before response writer");
        confmgrservlet.ReplyToHttpRequest(returnJsonMsg.getMessgeString());
    }

    @Override
    public void handleErrorMessage(String arg0) {
        confmgrservlet.ReplyToHttpRequest("ONM_SM_Error Msg :" + arg0);
    }
}
