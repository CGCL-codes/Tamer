package com.google.code.donkirkby;

import java.util.HashMap;
import java.util.Map;

public class InMemoryStrokeDataDao implements IStrokeDataDao {

    private Map<String, String> strokeDataMap = new HashMap<String, String>();

    public void saveStrokeData(String character, String strokeData) {
        strokeDataMap.put(character, strokeData);
    }

    public String getStrokeDataForCharacter(String character) {
        return strokeDataMap.get(character);
    }
}
