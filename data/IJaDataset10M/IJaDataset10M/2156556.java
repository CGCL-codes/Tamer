package net.zschech.gwt.comet.server.impl;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import com.google.apphosting.api.DeadlineExceededException;

public class GAEAsyncServlet extends BlockingAsyncServlet {

    @Override
    public Object suspend(CometServletResponseImpl response, CometSessionImpl session, HttpServletRequest request) throws IOException {
        try {
            super.suspend(response, session, request);
        } catch (DeadlineExceededException e) {
            response.tryTerminate();
        }
        return null;
    }
}
