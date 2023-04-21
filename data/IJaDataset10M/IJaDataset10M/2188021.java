package net.sf.metaprint2d.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sf.metaprint2d.Fingerprint;
import net.sf.metaprint2d.FingerprintData;
import net.sf.metaprint2d.data.BinFile;
import org.junit.Assert;
import org.junit.Test;

public class BinFileTest {

    @Test
    public void testWriteRead() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        List<FingerprintData> list = new ArrayList<FingerprintData>();
        list.add(new FingerprintData(0, Fingerprint.parseCompressedString("611_01_01_01_01_01"), 0, 6));
        list.add(new FingerprintData(0, Fingerprint.parseCompressedString("601_11_01_01_01_01"), 5, 92));
        list.add(new FingerprintData(0, Fingerprint.parseCompressedString("601_01_21_01_01_01"), 6, 12));
        list.add(new FingerprintData(0, Fingerprint.parseCompressedString("601_01_11_01_01_01"), 3, 7));
        list.add(new FingerprintData(0, Fingerprint.parseCompressedString("601_01_01_01_01_01"), 1, 2));
        BinFile.write(out, list);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        List<FingerprintData> data = BinFile.read(in);
        Assert.assertEquals(list, data);
    }
}
