package ndsapp.web;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ndsapp.domain.DiagnosisOption;
import ndsapp.repository.RuleDao;
import ndsapp.service.HybridExpertSystem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class RuleBasedDiagnosisController implements Controller {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    private HybridExpertSystem es;

    private RuleDao rd;

    private int rulebeingserved = 0;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ruleId = request.getParameter("ruleid");
        Map<String, Object> model = new LinkedHashMap<String, Object>();
        if (rd.getDbconn().connection == null) {
            model.put("MessageToAdmin", "Database connection could not be established.");
            return new ModelAndView("home", "model", model);
        } else if (ruleId != null) {
            int ruleid = Integer.parseInt(ruleId);
            es.processRuleRequest(new DiagnosisOption(ruleid, " "));
            if (es.getESSolution().get("diagnosisOptions") != null) {
                model.put("decisionTreePath", es.getDecisionTreePath());
                model.put("diagnosisOptions", es.getESSolution().get("diagnosisOptions"));
                model.put("diagnosisInstruction", es.getESSolution().get("diagnosisInstruction"));
                rulebeingserved = ruleid;
            } else if (es.getESSolution().get("diagnosisOptions") == null && ruleid < 800) {
                es.processRuleRequest(new DiagnosisOption(rulebeingserved, " "));
                model.put("decisionTreePath", es.getDecisionTreePath());
                model.put("diagnosisOptions", es.getESSolution().get("diagnosisOptions"));
                model.put("diagnosisInstruction", es.getESSolution().get("diagnosisInstruction"));
            } else {
                es.processRuleRequest(new DiagnosisOption(ruleid, " "));
                model.put("decisionTreePath", es.getDecisionTreePath());
                model.put("diagnosisOptions", es.getESSolution().get("diagnosisOptions"));
                model.put("diagnosisInstruction", es.getESSolution().get("diagnosisInstruction"));
            }
        }
        logger.info("returning from rules controller.");
        return new ModelAndView("rulediagnosis", "model", model);
    }

    public void setEs(HybridExpertSystem es) {
        this.es = es;
    }

    public RuleDao getRd() {
        return rd;
    }

    public void setRd(RuleDao rd) {
        this.rd = rd;
    }
}
