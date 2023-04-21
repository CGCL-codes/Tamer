package test.it.hotel.controller.sms;

import java.io.IOException;
import it.hotel.controller.sms.ISmsController;
import it.hotel.controller.user.IUserContainer;
import it.hotel.model.role.Role;
import it.hotel.model.user.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import test.it.hotel.model.abstrakt.BaseHotelTestCase;

public class TestSmsController extends BaseHotelTestCase {

    protected ISmsController smsReceiver;

    protected IUserContainer userContainer;

    public MockHttpServletRequest req = new MockHttpServletRequest();

    public MockHttpServletResponse resp = new MockHttpServletResponse();

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        User user = new User();
        Role role = new Role();
        role.setDescription("test");
        role.setName("test");
        user.setRole(role);
        user.setStructureId(1);
        userContainer.setUser(user);
    }

    public void testReceiveMessage() throws Exception {
        req.setMethod("POST");
        req.setRequestURI("http://localhost:8080/hotel/sms/create.htm");
        String receipt = "2#12345:source:079456345:200:xxx:1234567809:userfred:";
        String message = "11796 book owner2 password 238 12.09.2008 3 testname surname";
        req.addParameter("TextMessage", (message));
        ModelAndView mav = smsReceiver.create(req, resp);
        assertEquals("sms.ok", mav.getViewName());
        assertEquals(bookingRawManager.getAll().size(), 7);
    }

    public void testReceiveMessageWithHttpPost() throws ClientProtocolException, IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://192.167.131.126/hotel/sms/create.htm");
        String receipt = "2#12345:source:079456345:200:xxx:1234567809:userfred:";
        String message = "11796 book owner2 password 238 12.09.2008 3 testname surname";
        HttpParams params = new BasicHttpParams();
        params.setParameter("TextMessage", message);
        httpPost.setParams(params);
        HttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();
    }
}
