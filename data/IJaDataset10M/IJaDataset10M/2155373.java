package hu.sztaki.lpds.pgportal.servlet.ajaxactions.grids;

import hu.sztaki.lpds.pgportal.servlet.ajaxactions.inf.JobConfigUI;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import dci.data.Middleware;

/**
 *
 * @author csig
 */
public class JobConfigUI_local implements JobConfigUI {

    public String getJsp() {
        return "/jsp/workflow/zen/middleware_glite.jsp";
    }

    public Hashtable getJobParameters(String user, HashMap exeParams, List<Middleware> sessionConfig) {
        Hashtable conf = new Hashtable(new JobConfigUtils().getGridParams("local", exeParams, sessionConfig));
        conf.putAll(new JobConfigUtils().getMyJobParams(exeParams));
        return conf;
    }
}
