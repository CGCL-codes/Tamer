package com.gcapmedia.dab.epg.binary;

import java.util.Arrays;
import org.testng.annotations.Test;
import com.gcapmedia.dab.epg.binary.types.NumericType;

/**
 * 
 */
public class NumericTypeTest {

    @Test(groups = "type")
    public void Serialize() {
        NumericType type = new NumericType(44000, 24);
        System.out.println(BitBuilder.printByteArray(type.getBytes()));
        assert Arrays.equals((type.getBytes()), BitParser.parseByteArray("00 ab e0"));
    }

    @Test(groups = "type")
    public void Deserialize() {
        NumericType type = new NumericType(44000, 24);
        assert type.equals(NumericType.fromBytes(BitParser.parseByteArray("00 ab e0"), 24));
    }
}
