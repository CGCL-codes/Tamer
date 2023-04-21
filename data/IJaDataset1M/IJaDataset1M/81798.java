package com.zara.store.ws;

import com.zara.store.common.EnvtJmsClient;

public class EnvtWs {

    public int createEnvt(String xml) {
        new EnvtJmsClient().sendEnvt(xml);
        return 1;
    }

    private static final EnvtWs instance = new EnvtWs();

    public static EnvtWs getInstance() {
        return instance;
    }
}
