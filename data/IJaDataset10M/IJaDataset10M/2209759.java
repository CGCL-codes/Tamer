package org.nightlabs.jdo.inheritance;

import javax.jdo.FetchPlan;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.spi.PersistenceCapable;
import org.nightlabs.inheritance.Inheritable;
import org.nightlabs.inheritance.InheritanceManager;
import org.nightlabs.jdo.FetchPlanBackup;
import org.nightlabs.jdo.NLJDOHelper;

public class JDOInheritanceManager extends InheritanceManager {

    /**
	 * Same as {@link InheritanceManager#inheritAllFields(Inheritable, Inheritable)} except that it
	 * ensures that all JDO managed fields are retrieved before the inheritance takes place.
	 */
    @Override
    public void inheritAllFields(Inheritable mother, Inheritable child) {
        provideFields(mother);
        provideFields(child);
        super.inheritAllFields(mother, child);
    }

    protected void provideFields(Inheritable obj) {
        if (obj instanceof PersistenceCapable) {
            PersistenceManager pm = JDOHelper.getPersistenceManager(obj);
            if (pm == null) throw new IllegalStateException("There is no PersistenceManager assigned to this object (it is currently not persistent): " + obj);
            FetchPlanBackup fetchPlanBackup = NLJDOHelper.backupFetchPlan(pm.getFetchPlan());
            try {
                pm.getFetchPlan().setGroup(FetchPlan.ALL);
                pm.retrieve(obj, true);
            } finally {
                NLJDOHelper.restoreFetchPlan(pm.getFetchPlan(), fetchPlanBackup);
            }
        }
    }
}
