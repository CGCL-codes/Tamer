package org.jactr.core.production.six;

import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.model.IModel;
import org.jactr.core.production.CannotInstantiateException;
import org.jactr.core.production.ISubsymbolicProduction;
import org.jactr.core.production.ISymbolicProduction;
import org.jactr.core.production.VariableBindings;
import org.jactr.core.production.basic.AbstractInstantiation;
import org.jactr.core.production.basic.AbstractProduction;
import org.jactr.core.production.basic.BasicSymbolicProduction;
import org.jactr.core.production.condition.ICondition;

/**
 * Description of the Class
 * 
 * @author harrison
 * @created February 5, 2003
 */
public class DefaultInstantiation6 extends AbstractInstantiation {

    private static transient Log LOGGER = LogFactory.getLog(DefaultInstantiation6.class.getName());

    public DefaultInstantiation6(AbstractProduction parent, Collection<ICondition> boundConditions, VariableBindings variableBindings) throws CannotInstantiateException {
        super(parent, boundConditions, variableBindings);
        ISubsymbolicProduction6 pssp = (ISubsymbolicProduction6) parent.getSubsymbolicProduction();
        ISubsymbolicProduction6 ssp = (ISubsymbolicProduction6) getSubsymbolicProduction();
        ssp.setCreationTime(parent.getModel().getAge());
        ssp.setExpectedUtility(pssp.getExpectedUtility());
        ssp.setUtility(pssp.getUtility());
        ssp.setFiringTime(pssp.getFiringTime());
        ssp.setReward(pssp.getReward());
    }

    @Override
    public void dispose() {
        getSubsymbolicProduction().dispose();
        super.dispose();
    }

    /**
   * overriden since the default version returns the subsymbolic of the instantiating
   * production
   * @return
   * @see org.jactr.core.production.basic.AbstractInstantiation#getSubsymbolicProduction()
   */
    @Override
    public ISubsymbolicProduction getSubsymbolicProduction() {
        return _subsymbolicProduction;
    }

    /**
   * we use the parent productions subsymbolic, so return null
   * 
   * @see org.jactr.core.production.basic.AbstractProduction#createSubsymbolicProduction(org.jactr.core.production.basic.AbstractProduction,
   *      org.jactr.core.model.IModel)
   */
    @Override
    protected ISubsymbolicProduction createSubsymbolicProduction(AbstractProduction production, IModel model) {
        return new DefaultSubsymbolicProduction6(production, model);
    }

    @Override
    protected ISymbolicProduction createSymbolicProduction(AbstractProduction production, IModel model) {
        return new BasicSymbolicProduction(production, model);
    }
}
