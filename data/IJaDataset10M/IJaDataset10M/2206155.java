package com.oki.sample.b2bua.flow.step10;

import javax.servlet.sip.SipServletResponse;
import org.mobicents.ssf.flow.annotation.Evaluate;
import org.mobicents.ssf.flow.configuration.TransitionSet;

@TransitionSet(values = { "action.forward1xx", "action.forwardResInv", "action.forwardRes" })
public class SipResponse {

    @Evaluate
    public String evaluate(SipServletResponse res) {
        int status = res.getStatus();
        String method = res.getMethod();
        if ("INVITE".equals(method)) {
            if (status < 200) {
                return "action.forward1xx";
            } else {
                return "action.forwardResInv";
            }
        } else {
            return "action.forwardRes";
        }
    }
}
