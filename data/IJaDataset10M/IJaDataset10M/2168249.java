package com.go.teaservlet;

import javax.servlet.ServletContext;
import com.go.tea.runtime.OutputReceiver;
import com.go.tea.engine.DynamicContextSource;
import com.go.trove.io.CharToByteBuffer;
import com.go.trove.log.Log;

/******************************************************************************
 * 
 * @author Jonathan Colwell
 * @version
 * <!--$$Revision: 3 $-->, <!--$$JustDate:-->  1/30/02 <!-- $-->
 */
public class HttpContextSource implements DynamicContextSource {

    private ServletContext mServletContext;

    private Log mLog;

    HttpContextSource(ServletContext servletContext, Log log) {
        mLog = log;
        mServletContext = servletContext;
    }

    public Class getContextType() {
        return HttpContext.class;
    }

    public Object createContext(Class expected, Object obj) throws ClassNotFoundException {
        if (expected.isAssignableFrom(HttpContextImpl.class)) {
            return createContext(obj);
        } else {
            throw new ClassNotFoundException(expected + " is not available from this context source.");
        }
    }

    public Object createContext(Object obj) {
        ApplicationRequest req = null;
        ApplicationResponse resp = null;
        CharToByteBuffer buf = null;
        OutputReceiver outRec = null;
        if (obj instanceof TeaServletTransaction) {
            TeaServletTransaction trans = (TeaServletTransaction) obj;
            req = trans.getRequest();
            resp = trans.getResponse();
            buf = resp.getResponseBuffer();
            outRec = trans.getOutputReceiver();
        } else if (obj instanceof OutputReceiver) {
            outRec = (OutputReceiver) obj;
        }
        return new HttpContextImpl(mServletContext, mLog, req, resp, buf, outRec);
    }
}
