package com.google.code.ibear.meta;

import com.google.code.ibear.meta.IPeer;

public class TPeer implements IPeer {

    public String[] getAllFields() {
        return new String[] { "f1", "f2" };
    }
}
