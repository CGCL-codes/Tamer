package openfarmmanager.manager.web.controller.job;

import java.net.MalformedURLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import openfarmmanager.manager.web.controller.SpringSubManager;
import openfarmmanager.manager.web.controller.controlcentre.ControlCentreController;
import openfarmmanager.util.logging.LogManagerUtil;
import openfarmtools.interpreter.exceptions.OpenFarmException;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class AbortJobsController extends SpringSubManager {

    private static final Logger log = LogManagerUtil.getLogger(ControlCentreController.class);

    @Override
    public ModelAndView doStuff(HttpServletRequest request, HttpServletResponse response) throws OpenFarmException {
        ModelAndView mv = new ModelAndView("canceled");
        String ac = http.get("ac");
        if ("true".equals(http.get("stopSingleJob")) && http.existsInRequest("jobId") && http.existsInRequest("jobGroupId")) {
            String jobId = http.get("jobId");
            String jobGroupId = http.get("jobGroupId");
            try {
                managerControl.stopSingleJob(jobGroupId, jobId);
                mv.setView(new RedirectView("interpreterstate.htm", true));
            } catch (Exception e) {
                log.warn(e.getMessage());
                throw new OpenFarmException(e.getMessage());
            }
        } else if ("true".equals(http.get("stopJobGroup")) && http.existsInRequest("jobGroupId")) {
            String jobGroupId = http.get("jobGroupId");
            try {
                managerControl.stopJobGroup(jobGroupId);
                mv.setView(new RedirectView("interpreterstate.htm", true));
            } catch (Exception e) {
                log.warn(e.getMessage());
                throw new OpenFarmException(e.getMessage());
            }
        }
        if ("true".equals(http.get("abortAll"))) {
            try {
                managerControl.abortAll(ac);
                mv.addObject("abort", true);
            } catch (MalformedURLException e) {
                log.warn(e.getMessage());
                throw new OpenFarmException(e.getMessage());
            }
        }
        return mv;
    }
}
