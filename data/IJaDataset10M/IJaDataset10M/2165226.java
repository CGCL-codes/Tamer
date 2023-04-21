package com.levigo.jbig2.segments;

import java.io.IOException;
import java.io.InputStream;
import javax.imageio.stream.ImageInputStream;
import junit.framework.Assert;
import org.junit.Test;
import com.levigo.jbig2.io.DefaultInputStreamFactory;
import com.levigo.jbig2.io.SubInputStream;
import com.levigo.jbig2.util.CombinationOperator;
import com.levigo.jbig2.util.InvalidHeaderValueException;

public class HalftoneRegionTest {

    @Test
    public void parseHeaderTest() throws IOException, InvalidHeaderValueException {
        InputStream is = getClass().getResourceAsStream("/images/sampledata.jb2");
        DefaultInputStreamFactory disf = new DefaultInputStreamFactory();
        ImageInputStream iis = disf.getInputStream(is);
        SubInputStream sis = new SubInputStream(iis, 302, 87);
        HalftoneRegion hr = new HalftoneRegion(sis);
        hr.init(null, sis);
        Assert.assertEquals(true, hr.isMMREncoded());
        Assert.assertEquals(0, hr.getHTemplate());
        Assert.assertEquals(false, hr.isHSkipEnabled());
        Assert.assertEquals(CombinationOperator.OR, hr.getCombinationOperator());
        Assert.assertEquals(0, hr.getHDefaultPixel());
        Assert.assertEquals(8, hr.getHGridWidth());
        Assert.assertEquals(9, hr.getHGridHeight());
        Assert.assertEquals(0, hr.getHGridX());
        Assert.assertEquals(0, hr.getHGridY());
        Assert.assertEquals(1024, hr.getHRegionX());
        Assert.assertEquals(0, hr.getHRegionY());
    }
}
