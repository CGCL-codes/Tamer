package ndsapp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import ndsapp.domain.Case;
import ndsapp.domain.DiagnosisOption;
import ndsapp.repository.CaseDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HybridExpertSystem {

    private CaseDao cd;

    private RuleEngine re;

    private CaseEngine rc;

    private ArrayList<Case> similarCases = new ArrayList<Case>();

    private Map<String, Object> essolution;

    protected final Log logger = LogFactory.getLog(getClass());

    /**
	 * Processes a case to compute the expert system solution. 
	 * @param Case object
	 * @return nothing
	 */
    public void processCaseRequest(Case c, boolean findsimilarcases, boolean savenewcase) {
        cd.polishCase(c);
        if (savenewcase == true && findsimilarcases == true) {
            essolution = null;
            essolution = new HashMap<String, Object>();
            similarCases = rc.getCases(c);
            essolution.put("similarCases", similarCases);
            int caseid = cd.putCase(c);
            c.setCaseId(caseid);
            logger.info("A case inserted with id as " + caseid);
        } else if (savenewcase == true && findsimilarcases == false) {
            essolution = null;
            essolution = new HashMap<String, Object>();
            essolution.put("similarCases", null);
            int caseid = cd.putCase(c);
            c.setCaseId(caseid);
        } else if (savenewcase == false && findsimilarcases == true) {
            essolution = null;
            essolution = new HashMap<String, Object>();
            similarCases = rc.getCases(c);
            essolution.put("similarCases", similarCases);
        }
        ArrayList<Case> submittedCases = new ArrayList<Case>();
        submittedCases.add(c);
        essolution.put("submittedCases", submittedCases);
    }

    public void processRuleRequest(DiagnosisOption diagoption) {
        re.processSelectedSignAndSymptom(diagoption);
        essolution = null;
        essolution = new HashMap<String, Object>();
        essolution.put("diagnosisOptions", re.getDiagnosisOptions());
        essolution.put("diagnosisInstruction", re.getDiagnosisInstruction());
    }

    public ArrayList<DiagnosisOption> getDecisionTreePath() {
        return re.getDecisionTreePath();
    }

    /**
	 * Returns the expert system solution
	 * @param nothing
	 * @return solution in the form of a map
	 */
    public Map<String, Object> getESSolution() {
        return essolution;
    }

    public void setRc(CaseEngine rc) {
        this.rc = rc;
    }

    public void setRe(RuleEngine re) {
        this.re = re;
    }

    public CaseDao getCd() {
        return cd;
    }

    public void setCd(CaseDao cd) {
        this.cd = cd;
    }
}
