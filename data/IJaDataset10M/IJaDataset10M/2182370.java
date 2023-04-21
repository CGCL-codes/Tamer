package org.opennms.web.map;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.web.map.MapsConstants;
import org.opennms.web.map.view.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * <p>LoadLabelMapController class.</p>
 *
 * @author mmigliore
 * @author <a href="mailto:antonio@opennms.it">Antonio Russo</a>
 * @version $Id: $
 * @since 1.8.1
 */
@SuppressWarnings("deprecation")
public class LoadLabelMapController extends SimpleFormController {

    ThreadCategory log;

    private Manager manager;

    /**
	 * <p>Getter for the field <code>manager</code>.</p>
	 *
	 * @return a {@link org.opennms.web.map.view.Manager} object.
	 */
    public Manager getManager() {
        return manager;
    }

    /**
	 * <p>Setter for the field <code>manager</code>.</p>
	 *
	 * @param manager a {@link org.opennms.web.map.view.Manager} object.
	 */
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    /** {@inheritDoc} */
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
        ThreadCategory.setPrefix(MapsConstants.LOG4J_CATEGORY);
        log = ThreadCategory.getInstance(this.getClass());
        try {
            String user = request.getRemoteUser();
            log.debug("Loading Label Map for user:" + user);
            bw.write(ResponseAssembler.getLoadLabelMapResponse(manager.getNodeLabelToMaps(user)));
        } catch (Throwable e) {
            log.error("Error in map's startup", e);
            bw.write(ResponseAssembler.getMapErrorResponse(MapsConstants.LOADLABELMAP_ACTION));
        } finally {
            bw.close();
        }
        return null;
    }
}
