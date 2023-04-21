package com.remote.control;

import android.os.Handler;

public class RepeatHandler extends Handler {

    private final Integer _keycode;

    public RepeatHandler(Integer keycode) {
        _keycode = keycode;
    }

    public Integer getKeycode() {
        return _keycode;
    }
}
