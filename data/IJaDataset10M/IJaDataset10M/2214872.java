package org.springframework.web.servlet.view.json.writer.sojo;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import net.sf.sojo.common.WalkerInterceptor;
import org.junit.Test;
import org.springframework.web.servlet.view.json.JsonStringWriter;
import org.springframework.web.servlet.view.json.mock.writer.sojo.MockWalkerInterceptor;

public class SojoConfigTest {

    @Test
    public void testSojoConfig() {
        SojoConfig conf = new SojoConfig();
        assertEquals(0, conf.getInterceptors().size());
        conf.addInterceptor(new MockWalkerInterceptor());
        assertEquals(1, conf.getInterceptors().size());
        List<WalkerInterceptor> interceptorList = new ArrayList<WalkerInterceptor>();
        interceptorList.add(new MockWalkerInterceptor());
        conf.setInterceptors(interceptorList);
        assertEquals(conf.removeInterceptorByNumber(0).getClass(), MockWalkerInterceptor.class);
        assertEquals(0, conf.getInterceptors().size());
        conf.setKeepValueTypesMode(JsonStringWriter.MODE_KEEP_VALUETYPES_ALL);
        assertEquals(JsonStringWriter.MODE_KEEP_VALUETYPES_ALL, conf.getKeepValueTypesMode());
    }
}
