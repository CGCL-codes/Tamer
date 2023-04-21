package org.personalsmartspace.psm.dynmm.defaultEvaluators;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.ComponentFactory;
import org.personalsmartspace.psm.groupmgmt.api.pss3p.IPssGroupMembershipEvaluator;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;

/**
 * True evaluator
 * 
 * This evaluator class will always be satisfied.     
  */
@Component(label = "True Evaluator", factory = TrueEvaluator.cfPropertyValue, enabled = true)
@Service(value = IPssGroupMembershipEvaluator.class)
@Property(name = ComponentConstants.COMPONENT_FACTORY, value = TrueEvaluator.cfPropertyValue)
public class TrueEvaluator extends AbstractEvaluator implements IPssGroupMembershipEvaluator {

    public TrueEvaluator() {
        super();
        this.merDescription = "True";
    }

    public static final String cfPropertyValue = "factory.mer.atomic.true";

    /**
     * Component Factory Service
     */
    @Reference(cardinality = ReferenceCardinality.OPTIONAL_UNARY, policy = ReferencePolicy.DYNAMIC, target = "(" + ComponentConstants.COMPONENT_FACTORY + "=" + TrueEvaluator.cfPropertyValue + ")")
    protected ComponentFactory componentFactory = null;

    @Override
    public ComponentFactory getMerFactory() {
        return this.componentFactory;
    }

    @Override
    public boolean evaluate(IDigitalPersonalIdentifier dpi) {
        return true;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String getFactoryId() {
        return TrueEvaluator.cfPropertyValue;
    }
}
