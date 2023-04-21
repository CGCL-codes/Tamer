package org.swxjava;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.swxjava.core.DefaultRemoter;
import org.swxjava.core.SwxRpcMarshaller;
import org.swxjava.util.Log;
import org.swxjava.util.LogFactory;
import org.swxjava.util.RequestUtils;

public class MarshallerTest {

    private static final Log LOG = LogFactory.getLog(Marshaller.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMarshallInbound() throws Exception {
        SwxRpcMarshaller m = new SwxRpcMarshaller();
        HttpServletRequest request = new MockHttpServletRequest();
        Map<String, String> params = request.getParameterMap();
        params.put(RequestUtils.PARAM_SERVICE_CLASS, "org.swxjava.examples.Simple");
        params.put(RequestUtils.PARAM_METHOD, "echoData");
        params.put(RequestUtils.PARAM_ARGS, "[{\"id\":10, \"name\":\"SWX Javas\"}]");
        HttpServletResponse response = new MockHttpServletResponse();
        Call call = m.marshallInbound(request, response);
        LOG.info(call);
        Remoter remoter = new DefaultRemoter();
        Reply reply = remoter.execute(call);
        LOG.info(reply);
    }
}
