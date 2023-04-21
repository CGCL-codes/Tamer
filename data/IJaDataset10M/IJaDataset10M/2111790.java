package eu.funcnet.gwt_client.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import org.apache.log4j.Logger;
import eu.funcnet.clients.jaxb.impl.FrontEndProxyImpl;
import eu.funcnet.gwt_client.client.FuncNetException;

/**
 * A servlet which renders the {@link ScoreSet}  for a FuncNet job as an XML file, and returns it.
 * The format of the file is essentially the same as that which us returned by FuncNet's
 * RetrieveSetwiseScores operation, but it is reconstituted here from the ScoreSet as the
 * {@link FrontEndProxyImpl} object doesn't currently provide any way to get at the underlying XML of
 * the messages. This could be changed in the future though.
 */
public class XmlDownloadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger __log = Logger.getLogger(XmlDownloadServlet.class);

    private final XMLOutputFactory _xof = XMLOutputFactory.newInstance();

    {
        _xof.setProperty("javax.xml.stream.isRepairingNamespaces", true);
    }

    private final FrontEndProxyImpl _proxy;

    /**
     * Create a instance of the servlet using a specific FuncNet client proxy object.
     * Used in testing.
     *
     * @param proxy the FuncNet proxy
     * @throws FuncNetException
     */
    public XmlDownloadServlet(final FrontEndProxyImpl proxy) throws FuncNetException {
        try {
            _proxy = proxy;
            __log.info("Created servlet with FuncNet proxy supplied");
        } catch (final Exception e) {
            throw new FuncNetException(e);
        }
    }

    /**
     * Create a instance of the servlet using a FuncNet client proxy object supplied by
     * the {@link ProxyFactory}'s static method (i.e. the default proxy).
     *
     * @param proxy the FuncNet proxy
     * @throws FuncNetException
     */
    public XmlDownloadServlet() throws FuncNetException {
        try {
            _proxy = ProxyFactory.getProxy();
            __log.info("Created servlet with default FuncNet proxy from ProxyFactory");
        } catch (final Exception e) {
            throw new FuncNetException(e);
        }
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String emailAddress = request.getParameter("email");
        final String jobID = request.getParameter("jobID");
        final String mode = request.getParameter("mode");
        final String xmlData = mode.equals("pairwise") ? _proxy.retrieveCompleteScoresXML(jobID, emailAddress) : _proxy.retrieveSetwiseScoresXML(jobID, emailAddress);
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", "attachment; filename=results.xml");
        final BufferedWriter bw = new BufferedWriter(new PrintWriter(response.getOutputStream()));
        bw.write(xmlData);
        bw.flush();
        bw.close();
    }
}
