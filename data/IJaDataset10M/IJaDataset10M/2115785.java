package com.extjs.djn.spring.test.autowired.action;

import org.springframework.stereotype.Component;
import com.extjs.djn.spring.test.autowired.IAutoWiredDirectAction;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

@Component
public class FullAutowiredTest implements IAutoWiredDirectAction {

    @DirectMethod
    public boolean test_methodFAT() {
        return true;
    }
}
