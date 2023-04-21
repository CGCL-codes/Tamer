package com.cbsgmbh.xi.af.as2.http;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.cbsgmbh.xi.af.as2.helpers.RequestDataServletImpl;
import com.cbsgmbh.xi.af.as2.helpers.ResponseDataServletImpl;
import com.cbsgmbh.xi.af.as2.util.Transformer;
import com.cbsgmbh.xi.af.trace.helpers.BaseTracer;
import com.cbsgmbh.xi.af.trace.helpers.BaseTracerSapImpl;
import com.cbsgmbh.xi.af.trace.helpers.Tracer;
import com.cbsgmbh.xi.af.trace.helpers.TracerCategories;

/**
 * Servlet which is called by the external party for posting a AS2 message
 * to XI
 * 
 * @author Andreas Schmidsberger
 */
@SuppressWarnings({ "deprecation", "serial" })
public class AdapterFacadeServlet extends HttpServlet implements SingleThreadModel {

    private static final BaseTracer baseTracer = new BaseTracerSapImpl(AdapterFacadeServlet.class.getName(), TracerCategories.APP_LISTENER);

    /**
     * This method processes the request and creates a XI message by: <p/> 1.
     * Parsing of multipart message<p/> 2. Validation of multipart parameters<p/>
     * 3. Validation against communication channel configuration<p/> 4. Parsing
     * of input data according to input format and crypt settings (channel)<p/>
     * 5. Creation and sending of XI message<p/> 7. Creation of AS2-MDN
     * response message<p/> <p/>
     * 
     * @param request
     *            Multipart message within AS2 format
     * @param response
     *            AS2-MDN response message
     */
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final Tracer tracer = baseTracer.entering("doPost(final HttpServletRequest request, final HttpServletResponse response)");
        try {
            final byte[] messageDataBytes = Transformer.convertInputStreamToByteArray(request.getInputStream());
            tracer.info("messageData: {0}", new String(messageDataBytes));
            tracer.info("messageData.length: {0}", new Integer(messageDataBytes.length));
            final RequestDataServletImpl requestData = new RequestDataServletImpl(request, messageDataBytes);
            requestData.traceData();
            final ResponseDataServletImpl responseData = new ResponseDataServletImpl(response);
            final ClassPathXmlApplicationContext factory = new ClassPathXmlApplicationContext("beansAdapterFacade.xml");
            tracer.debug("Created ClassPathXmlApplicationContext object.");
            final AdapterFacadeWorker adapterFacadeWorker = (AdapterFacadeWorker) factory.getBean("adapterFacadeWorker");
            tracer.debug("Created AdapterFacadeWorker object.");
            adapterFacadeWorker.process(requestData, responseData);
            final ServletOutputStream outStream = response.getOutputStream();
            if (responseData.getResponseMessage() != null) outStream.write(responseData.getResponseMessage());
            outStream.flush();
            outStream.close();
        } catch (Throwable t) {
            tracer.catched(t);
            tracer.error(t.getMessage());
        }
        tracer.leaving();
    }

    /**
     * Generates a AS2 error message: post-method must be used
     * 
     * @param request
     *            Request via doGet method which will be rejected
     * @param response
     *            AS2 compliant rejection message
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String HTTP_METHOD_ERROR = "Get method is not supported - use post method instead";
        final Tracer tracer = baseTracer.entering("doGet(HttpServletRequest request, HttpServletResponse response)");
        tracer.error("Invalid request using get method from remoteHost: {0}", request.getRemoteHost());
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        final PrintWriter out = response.getWriter();
        out.println(HTTP_METHOD_ERROR);
        tracer.leaving();
    }
}
