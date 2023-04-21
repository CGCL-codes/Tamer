package de.juwimm.cms.cocoon.acting;

import java.util.*;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.SingleThreaded;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.*;
import org.apache.log4j.Logger;
import de.juwimm.cms.cocoon.helper.CocoonSpringHelper;
import de.juwimm.cms.remote.WebServiceSpring;

/**
 * An <code>Action</code> that matches a string from within the host parameter of the HTTP request.</br>
 * This action searches for the host parameter in the database and returns the startpage-url for this host to the sitemap.</br>
 * <p><h5>Configuration:</h5>
 * <pre>
 * &lt;map:action name="cms.actions.hoststartpage" src="de.juwimm.cms.cocoon.acting.HostStartPageAction"/&gt;
 * </pre>
 * </p>
 * <p><h5>Usage:</h5>
 * <pre>
 * &lt;map:match pattern="whatever"&gt;
 *     &lt;map:act type="cms.actions.hoststartpage"&gt;
 *         &lt;map:select type="parameter"&gt;
 *             &lt;map:parameter name="parameter-selector-test" value="{startpageURL}"/&gt;
 *             &lt;map:when test="0"&gt;
 *                 &lt;!-- host does not have a startpage configured --&gt;
 *             &lt;/map:when&gt;
 *             &lt;map:otherwise&gt;
 *                 &lt;!-- host points to {startpageURL} --&gt;
 *             &lt;/map:otherwise&gt;
 *         &lt;/map:select&gt;
 *     &lt;/map:act&gt;
 * &lt;/map:match&gt;
 * </pre>
 * </p>
 * <p><h5>Result:</h5>The action returns only one value as parameters to the sitemap:</br>
 * <ul>
 * <li>startpageURL - the startpage-url for this host the main-sitemap would normally redirect to if the request-path is empty. If no url is found this value equals &quot;0&quot;.</li> 
 * </ul>
 * This action returns &quot;null&quot; if the request does not contain a host value, the host is empty, the host isn't found in the database or the host is 127.0.0.1 or &quot;localhost&quot;.
 * </p>
 * 
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 * company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id: HostStartPageAction.java 8 2009-02-15 08:54:54Z skulawik $
 * @since ConQuest 2.4.12
 */
public class HostStartPageAction extends AbstractAction implements SingleThreaded {

    private Logger log = Logger.getLogger(HostStartPageAction.class);

    private WebServiceSpring webSpringBean = null;

    private static final String STARTPAGE_URL = "startpageURL";

    public HostStartPageAction() {
    }

    public Map act(Redirector redirector, SourceResolver resolver, Map objectModel, String source, Parameters parameters) throws Exception {
        if (log.isDebugEnabled()) log.debug("start acting");
        try {
            webSpringBean = (WebServiceSpring) CocoonSpringHelper.getBean(objectModel, CocoonSpringHelper.WEB_SERVICE_SPRING);
        } catch (Exception exf) {
            log.error("Could not load WebServiceSpring!", exf);
        }
        Map<String, String> sitemapParams = new HashMap<String, String>();
        Request request = ObjectModelHelper.getRequest(objectModel);
        String host = request.getHeader("Host");
        int portPosition = host.lastIndexOf(":");
        if (portPosition > 0) {
            host = host.substring(0, portPosition);
        }
        if (host == null) {
            if (log.isDebugEnabled()) log.debug("No Host header -- failing.");
            return null;
        } else if ("".equalsIgnoreCase(host)) {
            if (log.isDebugEnabled()) log.debug("Host header empty -- failing.");
            return null;
        } else if ("127.0.0.1".equalsIgnoreCase(host) || "localhost".equalsIgnoreCase(host)) {
            if (log.isDebugEnabled()) log.debug("Host \"localhost\" -- skipping...");
            return null;
        } else {
            if (log.isDebugEnabled()) log.debug("found host: " + host);
            String startPageUrl = this.webSpringBean.getStartPage(host);
            if (log.isDebugEnabled()) {
                log.debug("found " + HostStartPageAction.STARTPAGE_URL + ": " + startPageUrl);
            }
            if ("".equalsIgnoreCase(startPageUrl)) {
                startPageUrl = "0";
            }
            sitemapParams.put(HostStartPageAction.STARTPAGE_URL, startPageUrl);
        }
        if (log.isDebugEnabled()) log.debug("finished acting");
        return sitemapParams;
    }
}
