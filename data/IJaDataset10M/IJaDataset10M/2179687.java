package com.oki.sample.b2bua.flow.step20;

import javax.servlet.sip.SipServletRequest;
import org.mobicents.ssf.flow.annotation.Evaluate;
import org.mobicents.ssf.flow.configuration.TransitionSet;

@TransitionSet(values = { "action.forwardReq" })
public class SipRequest {

    @Evaluate
    public String evaluate(SipServletRequest req) {
        return "action.forwardReq";
    }
}
