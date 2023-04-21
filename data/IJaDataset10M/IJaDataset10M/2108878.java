package org.nkumar.immortal.web;

import junit.framework.TestCase;
import org.easymock.EasyMock;
import javax.servlet.http.HttpServletRequest;

public final class ImmortalResourceURLProviderTest extends TestCase {

    public void testUrlMapping() {
        urlMappingTest("app");
        urlMappingTest(null);
        try {
            ImmortalResourceURLProvider.getVersionedUrlFromRequest(createRequestMock("/app"), "relativepath/image.png");
            fail("IllegalArgumentException must be thrown in this case");
        } catch (IllegalArgumentException ignore) {
        } catch (Exception e) {
            fail("IllegalArgumentException must be thrown in this case");
        }
    }

    private static void urlMappingTest(final String contextName) {
        final HttpServletRequest mock = createRequestMock(contextName);
        final String contextPath = mock.getContextPath();
        assertEquals(contextPath + "/i_m_m_o_r_t_a_l/resources/js/date_ED802C30EF3F2989FEABADFC126C5E07.js", ImmortalResourceURLProvider.getVersionedUrlFromRequest(mock, "/resources/js/date.js"));
        assertEquals(contextPath + "/i_m_m_o_r_t_a_l/resources/css/main_13AA78395B360E1BE3E8FCF50372303F.css", ImmortalResourceURLProvider.getVersionedUrlFromRequest(mock, "/resources/css/main.css"));
        assertEquals(contextPath + "/i_m_m_o_r_t_a_l/resources/image/immortal_29D81C8095693E2227EFF46A886E2E9E.png", ImmortalResourceURLProvider.getVersionedUrlFromRequest(mock, "/resources/image/immortal.png"));
        assertEquals(contextPath + "/resources/image/unmapped.png", ImmortalResourceURLProvider.getVersionedUrlFromRequest(mock, "/resources/image/unmapped.png"));
    }

    private static HttpServletRequest createRequestMock(final String contextName) {
        final String contextPath = contextName == null ? "" : "/" + contextName;
        final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getContextPath()).andReturn(contextPath).anyTimes();
        EasyMock.replay(request);
        return request;
    }
}
