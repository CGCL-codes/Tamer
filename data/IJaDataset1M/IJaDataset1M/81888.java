package com.xmog.web.example;

import com.google.inject.Singleton;
import com.xmog.web.persistence.Transactional;

/**
 * @author <a href="mailto:maa@xmog.com">Mark Allen</a>
 */
@Singleton
public class BackendService {

    @Transactional
    public String createMessage(String value) {
        return "Here's the value: " + value;
    }
}
