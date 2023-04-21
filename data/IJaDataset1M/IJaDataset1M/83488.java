package shellkk.qiq.jdm.engine.regression;

import javax.datamining.MiningFunction;
import org.hibernate.Session;
import shellkk.qiq.jdm.engine.persist.IApplySettingsAccessObject;
import shellkk.qiq.jdm.task.apply.ApplySettingsImpl;

public class RegressionApplySettingsAO implements IApplySettingsAccessObject {

    public void delete(ApplySettingsImpl as, Session session) throws Exception {
        session.delete(as);
    }

    public void load(ApplySettingsImpl as, Session session) throws Exception {
        as.getSourceDestinationMap().size();
    }

    public void saveOrUpdate(ApplySettingsImpl as, Session session) throws Exception {
        session.saveOrUpdate(as);
    }

    public MiningFunction getMiningFunction() {
        return MiningFunction.regression;
    }
}
