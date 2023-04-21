package org.dynalang.debug.serialized;

import java.util.Map;

public class Ref extends Serialized {

    public Ref(int handle) {
        super(handle);
    }

    @Override
    public void toJson(Map<String, Object> json) {
        json.put("ref", Integer.valueOf(getHandle()));
    }
}
