package com.itealabs.postjson;

import java.util.Map;

/**
 * 
 * @version $Id: JsonCallWaiter.java 13 2010-04-23 05:50:41Z iteahere $
 * @author itea
 * @date 2010-4-16
 */
public interface JsonCallWaiter {

    JsonReturn service(JsonCall jsonCall, Map<String, Object> ctx);
}
