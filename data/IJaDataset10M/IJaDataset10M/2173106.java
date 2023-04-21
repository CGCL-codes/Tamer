package com.googlecode.jazure.examples.rate.inner;

import java.util.HashMap;
import java.util.Map;
import com.googlecode.jazure.sdk.task.Result;

public class HiltonResult implements Result {

    private static final long serialVersionUID = -6527953834092322963L;

    public static final String HAS_ROOM_RATE = "hasRoomRate";

    private final String passport;

    private final RoomRate roomRate;

    public HiltonResult(String passport, RoomRate roomRate) {
        this.passport = passport;
        this.roomRate = roomRate;
    }

    @Override
    public boolean successful() {
        return true;
    }

    @Override
    public Map<String, String> keyValues() {
        Map<String, String> keyValues = new HashMap<String, String>();
        keyValues.put(HAS_ROOM_RATE, Boolean.TRUE.toString());
        return keyValues;
    }
}
