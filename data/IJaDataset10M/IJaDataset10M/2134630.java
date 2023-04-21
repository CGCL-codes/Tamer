package gov.lanl.xmltape.resolver.xquery;

import gov.lanl.util.xquery.XQueryProfile;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import ORG.oclc.openurl.ContextObject;
import ORG.oclc.openurl.OpenURLException;

public class OpenURLaDORe7RangeService extends XQueryResolverService {

    static Logger log = Logger.getLogger(OpenURLaDORe7RangeService.class.getName());

    private XQueryProfile profile;

    private XQueryResolver resolver;

    public OpenURLaDORe7RangeService(ContextObject contextObject) throws OpenURLException, IOException {
        super(contextObject);
    }

    public void run(String referent) {
        try {
            bytes = resolver.getResultsAsRange(referent, profile);
            contentType = "text/plain; charset=UTF-8";
            status = HttpServletResponse.SC_OK;
        } catch (ResolverException e) {
            bytes = new String("Specified resource (" + referent + ") was not found.").getBytes();
            status = HttpServletResponse.SC_NOT_FOUND;
        } catch (Exception e) {
            bytes = new String("An Internal Server Error Occurred.").getBytes();
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            log.info("from servlet:" + e.toString());
        }
    }

    public void setResolver(XQueryResolver resolver) {
        this.resolver = resolver;
    }

    public void setProfile(XQueryProfile profile) {
        this.profile = profile;
    }
}
