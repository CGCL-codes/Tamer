package issrg.pba;

public class DefaultRiskAssessment implements RiskAssessment {

    public DefaultRiskAssessment() {
    }

    public boolean assessRisk(Subject user, Target target, Action action) {
        return false;
    }

    public void service(Subject user, Target target, Action action) throws PbaException {
    }

    public void service(Subject user) throws PbaException {
    }
}
