package org.opennms.web.map;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Category;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.web.map.MapsConstants;
import org.opennms.web.map.view.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * @author mmigliore
 * 
 * this class provides to create, manage and delete 
 * proper session objects to use when working with maps
 * 
 */
public class LoadMapsController implements Controller {

    Category log;

    private Manager manager;

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ThreadCategory.setPrefix(MapsConstants.LOG4J_CATEGORY);
        log = ThreadCategory.getInstance(this.getClass());
        log.debug("Loading Maps");
        String user = request.getRemoteUser();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()));
        try {
            List<VMapInfo> maps = manager.getVisibleMapsMenu(user);
            bw.write(ResponseAssembler.getMapsResponse(MapsConstants.LOADMAPS_ACTION, maps));
        } catch (Exception e) {
            log.error("Error while loading visible maps for user:" + user, e);
            bw.write(ResponseAssembler.getMapErrorResponse(MapsConstants.LOADMAPS_ACTION));
        } finally {
            bw.close();
        }
        return null;
    }
}
