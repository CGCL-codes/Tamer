package com.ncs.crm.client;

import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.util.KeyCallback;
import com.smartgwt.client.util.SC;

public class MydebugKey extends KeyIdentifier {

    public MydebugKey() {
        setCtrlKey(true);
        setShiftKey(true);
        setAltKey(true);
        setKeyName("s");
    }

    public KeyCallback getCb() {
        KeyCallback kc = new KeyCallback() {

            public void execute(String keyName) {
                SC.showConsole();
            }
        };
        return kc;
    }
}
