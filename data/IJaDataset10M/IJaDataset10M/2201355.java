package com.gcapmedia.dab.epg.binary;

import java.util.Arrays;
import org.testng.annotations.Test;
import com.gcapmedia.dab.epg.Crid;
import com.gcapmedia.dab.epg.binary.types.CridType;

/**
 * 
 */
public class CridTypeTest {

    @Test(groups = "type")
    public void Serialize() {
        CridType crid = new CridType(new Crid("gcapmedia.com", "123456"));
        System.out.println(BitBuilder.printByteArray(crid.getBytes()));
        assert Arrays.equals(crid.getBytes(), BitParser.parseByteArray("63 72 69 64 3a 2f 2f 67 63 61 70 6d 65 64 69 61 2e 63 6f 6d 2f 31 32 33 34 35 36"));
    }

    @Test(groups = "type")
    public void Deserialize() {
        CridType crid = new CridType(new Crid("gcapmedia.com", "123456"));
        assert crid.equals(CridType.fromBytes(BitParser.parseByteArray("63 72 69 64 3a 2f 2f 67 63 61 70 6d 65 64 69 61 2e 63 6f 6d 2f 31 32 33 34 35 36")));
    }
}
