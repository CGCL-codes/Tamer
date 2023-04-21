package edu.psu.gv.eadvisor.web.administrative;

import edu.psu.gv.eadvisor.dao.ElectiveModuleDao;
import edu.psu.gv.eadvisor.domain.ElectiveModule;
import edu.psu.gv.eadvisor.domain.ElectiveRequirement;
import edu.psu.gv.eadvisor.facade.ElectiveRequirementFacade;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.apache.commons.lang.text.StrTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author eadvisor Team
 */
public class ElectiveRequirementForm extends SimpleFormController {

    private ElectiveRequirementFacade electiveRequirementFacade;

    private ElectiveModuleDao electiveModuleDao;

    public void setElectiveRequirementFacade(ElectiveRequirementFacade electiveRequirementFacade) {
        this.electiveRequirementFacade = electiveRequirementFacade;
    }

    public void setElectiveModuleDao(ElectiveModuleDao electiveModuleDao) {
        this.electiveModuleDao = electiveModuleDao;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long electiveRequirementId = ServletRequestUtils.getLongParameter(request, "requirementId");
        ElectiveRequirement electiveRequirement;
        if (electiveRequirementId == null) {
            electiveRequirement = new ElectiveRequirement();
        } else {
            electiveRequirement = electiveRequirementFacade.getElectiveRequirement(electiveRequirementId);
        }
        request.setAttribute("electiveModules", electiveRequirementFacade.getElectiveModulesNotInRequirement(electiveRequirement));
        return electiveRequirement;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object o, BindException e) throws Exception {
        String electiveModuleList = request.getParameter("electiveModuleList");
        ElectiveRequirement electiveRequirement = (ElectiveRequirement) o;
        Set<ElectiveModule> electiveModules = processElectiveModuleList(electiveModuleList);
        electiveRequirement.setElectiveModules(electiveModules);
        electiveRequirementFacade.save(electiveRequirement);
        request.setAttribute("message", getMessageSourceAccessor().getMessage("requirement.elective.save.success", new Object[] { electiveRequirement.getName() }));
        request.setAttribute("electiveModules", electiveRequirementFacade.getElectiveModulesNotInRequirement(electiveRequirement));
        return new ModelAndView("electiveRequirementForm", "electiveRequirement", electiveRequirement);
    }

    private Set<ElectiveModule> processElectiveModuleList(String electiveModuleList) {
        if (electiveModuleList == null) {
            return new HashSet<ElectiveModule>();
        }
        StrTokenizer toker = new StrTokenizer(electiveModuleList);
        toker.setIgnoreEmptyTokens(true);
        Set<ElectiveModule> electiveModules = new HashSet<ElectiveModule>();
        while (toker.hasNext()) {
            electiveModules.add(electiveModuleDao.get(Long.parseLong((String) toker.next())));
        }
        return electiveModules;
    }
}
