package org.personalsmartspace.cm.reasoning.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.ComponentContext;
import org.personalsmartspace.cm.api.pss3p.ContextException;
import org.personalsmartspace.cm.broker.api.platform.ICtxBroker;
import org.personalsmartspace.cm.model.api.pss3p.CtxModelType;
import org.personalsmartspace.cm.model.api.pss3p.CtxOriginType;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAttribute;
import org.personalsmartspace.cm.model.api.pss3p.ICtxEntity;
import org.personalsmartspace.cm.model.api.pss3p.ICtxEntityIdentifier;
import org.personalsmartspace.cm.model.api.pss3p.ICtxIdentifier;
import org.personalsmartspace.cm.model.api.pss3p.ICtxModelObject;
import org.personalsmartspace.cm.model.api.pss3p.ICtxQuality;
import org.personalsmartspace.cm.reasoning.api.platform.ICtxRefiner;
import org.personalsmartspace.cm.reasoning.api.platform.IReasoningManager;
import org.personalsmartspace.cm.reasoning.api.platform.NotInferredException;
import org.personalsmartspace.lm.api.ILearner;
import org.personalsmartspace.lm.api.IRule;
import org.personalsmartspace.lm.bayesian.rule.BayesianRule;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;
import org.personalsmartspace.sre.api.pss3p.PssConstants;
import org.personalsmartspace.sre.ems.api.pss3p.EMSException;
import org.personalsmartspace.sre.ems.api.pss3p.EventListener;
import org.personalsmartspace.sre.ems.api.pss3p.IEventMgr;
import org.personalsmartspace.sre.ems.api.pss3p.PSSEvent;
import org.personalsmartspace.sre.ems.api.pss3p.PeerEvent;
import org.personalsmartspace.sre.ems.api.pss3p.PeerEventTypes;

/**
 * Implementation of the IReasoningManager interface.
 * TODO Persist the mapping between attributes and rules 
 * and the mapping between contextRefinement types and refiners.
 * 
 * @see IReasoningManager
 */
@Component(name = "Reasoning Manager", immediate = true)
@Service(IReasoningManager.class)
@Properties({ @Property(name = PssConstants._FW_COMPONENT, value = "true"), @Property(name = PssConstants._FW_SERVICE_ID, value = "ReasoningManager"), @Property(name = PssConstants._FW_ONTOLOGY_URI, value = "urn:i:am:a:framework:service") })
public class ReasoningManager implements IReasoningManager {

    private Log logger = new PSSLog(this);

    private static final String CtxAttrSeparator = ":";

    private ICtxEntity ruleRepo = null;

    protected RmMapper mapper = new RmMapper();

    /**
     * The Sling Dynamic ClassLoader manager.
     */
    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.DYNAMIC)
    protected DynamicClassLoaderManager dynClService = null;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.DYNAMIC, target = "(" + ComponentConstants.COMPONENT_NAME + "=Bayesian Learning)")
    protected ILearner<BayesianRule> bayesianLearning = null;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.DYNAMIC)
    protected IEventMgr evtMgr = null;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.DYNAMIC, target = "(" + ComponentConstants.COMPONENT_NAME + "=Bayesian Inference)")
    protected ICtxRefiner<BayesianRule> bayesianCtxRefiner = null;

    /**
     * The following map holds a mapping from context refiner rule types to instances of ICtxRefiner
     */
    protected Map<Class<? extends IRule>, ICtxRefiner<? extends IRule>> ruleType2ICtxRefiner = new HashMap<Class<? extends IRule>, ICtxRefiner<? extends IRule>>();

    /**
     * The set of locally running context refiners.
     */
    protected Set<ICtxRefiner<? extends IRule>> availableLocalRefiners = new HashSet<ICtxRefiner<? extends IRule>>();

    /**
     * 
     */
    public static final String RULE_REPO_TYPE = "CtxReasoningRuleRepo";

    /**
     * The context broker.
     */
    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.DYNAMIC)
    protected ICtxBroker ctxBroker = null;

    /**
     * This is the instance that is injected whenever a Ctx Refiner appears/disappears.               
     */
    @Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC, bind = "setAvailableRefiner", unbind = "unsetAvailableRefiner")
    private ICtxRefiner<? extends IRule> ref = null;

    private void doDebug(String msg) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(msg);
        }
    }

    private void doWarn(String msg) {
        if (this.logger.isWarnEnabled()) {
            this.logger.warn(msg);
        }
    }

    private void doError(String msg, Throwable t) {
        if (this.logger.isErrorEnabled()) {
            this.logger.error(msg, t);
        }
    }

    protected synchronized void setAvailableRefiner(ICtxRefiner<? extends IRule> ref) {
        this.ref = ref;
        doDebug("Adding inferrer " + ref + " of type " + ref.getRuleType().getCanonicalName());
        this.availableLocalRefiners.add(ref);
        this.ruleType2ICtxRefiner.put(ref.getRuleType(), ref);
    }

    protected synchronized void unsetAvailableRefiner(ICtxRefiner<? extends IRule> ref) {
        this.ref = null;
        doDebug("Removing inferrer " + ref + " of type " + ref.getRuleType().getCanonicalName());
        this.availableLocalRefiners.remove(ref);
        this.ruleType2ICtxRefiner.remove(ref.getRuleType());
    }

    /**
     * The set of locally available rules.
     */
    protected Set<IRule> availableRules = new HashSet<IRule>();

    /**
     * This is the instance that is injected whenever an InferenceRule appears/disappears.               
     */
    @Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC, bind = "addRule", unbind = "removeRule")
    private IRule rule = null;

    protected synchronized void addRule(IRule newRule) {
        this.rule = newRule;
        doDebug("Adding rule whose type is " + newRule.getClass().getCanonicalName());
        this.availableRules.add(newRule);
        this.addRule2Repo(newRule);
        this.mapper.mapRule(newRule);
        for (String outputType : newRule.getOutputTypes()) {
            try {
                ICtxEntity operator = this.ctxBroker.retrieveOperator();
                ICtxEntity device = this.ctxBroker.retrieveDevice();
                boolean availableInOperatorEntity = !operator.getAttributes(outputType).isEmpty();
                if (!availableInOperatorEntity) {
                    ICtxAttribute newAttr = this.ctxBroker.createAttribute(operator.getCtxIdentifier(), outputType);
                    this.ctxBroker.update(newAttr, CtxOriginType.INFERRED);
                    doDebug("\t-----------------------------------------------------------------");
                    doDebug("\tCreated Output Ctx Attribute under the Operator entity!");
                    doDebug("\tOperator ID =" + operator.getOperatorId());
                    doDebug("\tTriggering Rule =" + newRule);
                    doDebug("\tCreated Ctx Attr =" + newAttr);
                    doDebug("\t-----------------------------------------------------------------");
                }
            } catch (ContextException e) {
                this.doWarn("Unable to create CtxAttribute for output type '" + outputType + "'");
            }
        }
    }

    protected synchronized void removeRule(IRule disappearingRule) {
        this.rule = null;
        doDebug("Removing rule whose type is " + disappearingRule.getClass().getCanonicalName());
        this.availableRules.remove(disappearingRule);
        this.removeRuleFromRepo(disappearingRule);
        this.mapper.unmapRule(disappearingRule);
    }

    private Collection<ICtxAttribute> getAttributesFromIds(List<ICtxIdentifier> foundIds) {
        doDebug("Retrieving attributes " + foundIds);
        Collection<ICtxAttribute> foundModelObjects = new LinkedList<ICtxAttribute>();
        try {
            for (ICtxModelObject ctxModelObject : this.ctxBroker.retrieve(foundIds)) {
                if (ctxModelObject instanceof ICtxAttribute) {
                    foundModelObjects.add((ICtxAttribute) ctxModelObject);
                }
            }
        } catch (ContextException e) {
            doError("Could not retrieve attributes " + foundIds + ": " + e.getLocalizedMessage(), e);
        }
        return foundModelObjects;
    }

    private Collection<ICtxAttribute> getEvaluationInputs(ICtxEntity scope, String attrType) {
        doDebug("[RM] Getting evaluation inputs:\nscope = " + scope.getCtxIdentifier() + "\nattribute type = " + attrType);
        Collection<ICtxAttribute> result = new LinkedList<ICtxAttribute>();
        Set<ICtxAttribute> foundAttrs = scope.getAttributes(attrType);
        if (!foundAttrs.isEmpty()) {
            doDebug("Found evaluation inputs for type " + attrType + " in scope " + scope.getCtxIdentifier());
            for (final ICtxAttribute foundAttr : foundAttrs) {
                if (foundAttr.getValue() != null) {
                    doDebug("Input attribute value is not null, adding to the result set");
                    result.add(foundAttr);
                }
            }
        }
        if (result.isEmpty()) {
            try {
                final ICtxEntity deviceEnt = this.ctxBroker.retrieveDevice();
                foundAttrs = deviceEnt.getAttributes(attrType);
                if (!foundAttrs.isEmpty()) {
                    doDebug("Found evaluation inputs for type " + attrType + " in DEVICE entity " + deviceEnt.getCtxIdentifier());
                    for (final ICtxAttribute foundAttr : foundAttrs) {
                        if (foundAttr.getValue() != null) {
                            doDebug("Input attribute value is not null, adding to the result set");
                            result.add(foundAttr);
                        }
                    }
                }
            } catch (ContextException e) {
                doError("Could not get evaluation inputs from DEVICE entity: " + e.getLocalizedMessage(), e);
            }
        }
        return result;
    }

    @Override
    public Collection<ICtxAttribute> getEvaluationInputs(ICtxEntityIdentifier scopeId, String attrType) {
        Collection<ICtxAttribute> result = null;
        try {
            ICtxEntity ctxEnt = (ICtxEntity) this.ctxBroker.retrieve(scopeId);
            result = this.getEvaluationInputs(ctxEnt, attrType);
        } catch (ContextException e) {
            doError("Unable to retrieve CtxEntity with id '" + scopeId + "'\n" + e.getLocalizedMessage(), e);
        }
        return result;
    }

    @Override
    public Collection<ICtxAttribute> getEvaluationInputs(IDigitalPersonalIdentifier targetDpi, String attrType) {
        doDebug("[RM] Getting evaluation inputs:\nscope (DPI) = " + targetDpi + "\nattribute type = " + attrType);
        Collection<ICtxAttribute> result = new HashSet<ICtxAttribute>();
        List<ICtxIdentifier> foundIds = new LinkedList<ICtxIdentifier>();
        try {
            foundIds.addAll(this.ctxBroker.lookup(targetDpi, CtxModelType.ATTRIBUTE, attrType));
        } catch (ContextException e) {
            doError("Could not get evaluation inputs: " + e.getLocalizedMessage(), e);
        }
        result = this.getAttributesFromIds(foundIds);
        return result;
    }

    @Override
    public void inferAttributeValue(ICtxAttribute ctxAttr, Date hocOrigin, boolean isContinuoslyInferred) throws NotInferredException {
        doDebug("Inferring context attribute with ID " + ctxAttr.getCtxIdentifier() + ", isContinuouslyInferred = " + isContinuoslyInferred);
        String attrType = ctxAttr.getType();
        String filteredAttributeType = ReasoningManager.getFilteredCtxType(attrType);
        if (filteredAttributeType.length() == 0) return;
        Set<IRule> relevantRules = this.mapper.outputTypes2Rules.get(filteredAttributeType);
        if (relevantRules == null) {
            doDebug("No rules found for attribute type " + filteredAttributeType);
            return;
        }
        for (IRule rule : relevantRules) {
            ICtxRefiner<IRule> refiner = (ICtxRefiner<IRule>) this.ruleType2ICtxRefiner.get(rule.getClass());
            if (refiner != null) {
                if (!isContinuoslyInferred) {
                    try {
                        this.triggerRefiner(refiner, ctxAttr, rule, this);
                        this.ctxBroker.update(ctxAttr, CtxOriginType.INFERRED);
                    } catch (NotInferredException e) {
                        doError("Unable to infer on demand context attribute " + ctxAttr + " with Refiner " + refiner + " applied to rule " + rule, e);
                        throw e;
                    } catch (ContextException e) {
                        doError("Unable to update on-demand inferred context attribute " + ctxAttr + ": " + e.getLocalizedMessage(), e);
                    }
                } else {
                    this.mapper.addCiCtxAttribute(ctxAttr);
                    for (String currentInputType : rule.getInputTypes()) {
                        try {
                            ICtxEntityIdentifier attributeScope = ctxAttr.getScope();
                            ICtxEntityIdentifier deviceScope = this.ctxBroker.retrieveDevice().getCtxIdentifier();
                            if (attributeScope != null) {
                                Integer currentCounter = null;
                                currentCounter = this.mapper.inputTypes4ContinuouslyInferredAttributes.get(currentInputType);
                                if (currentCounter == null) currentCounter = new Integer(0);
                                this.mapper.inputTypes4ContinuouslyInferredAttributes.put(currentInputType, currentCounter + 1);
                                ReasoningManager.ContinuousInferenceHandler handlerAttribute = new ReasoningManager.ContinuousInferenceHandler(this, ctxAttr, currentInputType, rule);
                                this.ctxBroker.registerUpdateNotification(handlerAttribute, attributeScope, currentInputType, false);
                                this.ctxBroker.registerUpdateNotification(handlerAttribute, deviceScope, currentInputType, false);
                            } else {
                                doError("Unable to subscribe for changes of input context attributes" + ": Unable to retrieve scope identifiers for continuously inferred attribute " + ctxAttr.getCtxIdentifier(), null);
                            }
                        } catch (ContextException e) {
                            doError("Unable to register for updates on CtxAttribute type " + currentInputType + ". Cause:" + e.getMessage(), e);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void inferAttributeValue(ICtxAttribute ctxAttr, boolean isContinuoslyInferred) throws NotInferredException {
        this.inferAttributeValue(ctxAttr, null, isContinuoslyInferred);
    }

    public static String getFilteredCtxType(String type) {
        String[] CtxTypeParts = type.split(ReasoningManager.CtxAttrSeparator);
        if (CtxTypeParts.length == 0 || CtxTypeParts[0].length() == 0) return "";
        return CtxTypeParts[0];
    }

    @Override
    public boolean isInferrable(ICtxEntityIdentifier ctxId, String ctxType, boolean isContinuous) {
        doDebug("scope = " + ctxId + ", ctxType = " + ctxType + ", isContinuous = " + isContinuous);
        boolean result = false;
        String filteredAttributeType = ReasoningManager.getFilteredCtxType(ctxType);
        if (this.mapper.outputTypes2Rules.containsKey(filteredAttributeType)) {
            result = true;
        } else {
            Collection<? extends IRule> newRules = new LinkedList<IRule>();
            newRules = this.bayesianLearning.runLearning(ctxType, null, null);
            for (IRule rule : newRules) {
                this.addRule(rule);
                if (rule.getOutputTypes().contains(filteredAttributeType)) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * This method is called when this component is activated. It resembles the
     * start() method of BundleActivators.
     */
    protected void activate(ComponentContext context) {
        try {
            if (!this.isRuleRepoAvailable()) {
                this.ruleRepo = this.ctxBroker.createEntity(ReasoningManager.RULE_REPO_TYPE);
            } else {
                List<ICtxIdentifier> rrIdList = this.ctxBroker.lookup(CtxModelType.ENTITY, ReasoningManager.RULE_REPO_TYPE);
                this.ruleRepo = (ICtxEntity) this.ctxBroker.retrieve(rrIdList.get(0));
                Set<ICtxAttribute> ruleAttrSet = this.ruleRepo.getAttributes();
                for (ICtxAttribute ctxAttribute : ruleAttrSet) {
                    IRule deserializedRule = (IRule) ctxAttribute.getBlobValue(this.dynClService.getDynamicClassLoader());
                    context.getBundleContext().registerService(IRule.class.getCanonicalName(), deserializedRule, new Hashtable<String, String>());
                }
            }
        } catch (ContextException e) {
            doWarn("Unable to retrieve entity whose type is " + ReasoningManager.RULE_REPO_TYPE);
            e.printStackTrace();
        }
        for (IRule currentRule : this.availableRules) {
            this.addRule2Repo(currentRule);
        }
    }

    /**
     * This method is called when this component is deactivated. It resembles
     * the stop() method of BundleActivators.
     */
    protected void deactivate(ComponentContext context) {
    }

    private boolean addRule2Repo(IRule newRule) {
        if (this.ruleRepo == null) return false;
        try {
            ICtxAttribute newAttr = this.ctxBroker.createAttribute(this.ruleRepo.getCtxIdentifier(), newRule.toString(), newRule);
            this.refreshRepoRef();
            return true;
        } catch (ContextException e) {
            return false;
        }
    }

    private boolean removeRuleFromRepo(IRule disappearingRule) {
        if (this.ruleRepo == null) return false;
        try {
            this.refreshRepoRef();
            Set<ICtxAttribute> ruleAttrSet = this.ruleRepo.getAttributes(disappearingRule.toString());
            for (ICtxAttribute ctxAttribute : ruleAttrSet) {
                this.ctxBroker.remove(ctxAttribute.getCtxIdentifier());
            }
            return true;
        } catch (ContextException e) {
            return false;
        }
    }

    private boolean isRuleRepoAvailable() {
        int detectedSize = 0;
        try {
            detectedSize = this.ctxBroker.lookup(CtxModelType.ENTITY, ReasoningManager.RULE_REPO_TYPE).size();
        } catch (ContextException e) {
            doWarn("Unable to lookup entity whose type is " + ReasoningManager.RULE_REPO_TYPE);
            e.printStackTrace();
        }
        return detectedSize > 0;
    }

    private void refreshRepoRef() {
        try {
            this.ruleRepo = (ICtxEntity) ctxBroker.retrieve(this.ruleRepo.getCtxIdentifier());
        } catch (ContextException e) {
            e.printStackTrace();
        }
    }

    private void triggerRefiner(ICtxRefiner<IRule> refiner, ICtxAttribute ctxAttr, IRule rule, IReasoningManager rm) throws NotInferredException {
        doDebug("[" + System.currentTimeMillis() + "] Trigerring refiner for context attribute with ID " + ctxAttr.getCtxIdentifier() + " using rule " + rule);
        Collection<ICtxAttribute> refinedAttrs = refiner.eval(ctxAttr, rule, rm);
        if (refinedAttrs == null || refinedAttrs.isEmpty()) {
            throw new NotInferredException(ctxAttr);
        } else {
            for (ICtxAttribute currentRefinedCtxAttr : refinedAttrs) {
                if (null != currentRefinedCtxAttr && currentRefinedCtxAttr.getScope() != ctxAttr.getScope()) {
                    ctxAttr.setStringValue(currentRefinedCtxAttr.getStringValue());
                    ctxAttr.setSourceId(currentRefinedCtxAttr.getSourceId());
                    ICtxQuality targetQuality = ctxAttr.getQuality();
                    ICtxQuality sourceQuality = currentRefinedCtxAttr.getQuality();
                    CtxOriginType srcOrigin = sourceQuality.getOrigin();
                    if (srcOrigin != null) targetQuality.setOrigin(srcOrigin);
                    Double srcPrecision = sourceQuality.getPrecision();
                    if (srcPrecision != null) targetQuality.setPrecision(srcPrecision);
                    Double srcFrequency = sourceQuality.getUpdateFrequency();
                    if (srcFrequency != null) targetQuality.setUpdateFrequency(srcFrequency);
                }
            }
        }
    }

    /** This class implements a handler for triggering CtxRefiner invocations
     * for Ctx Attributes that are continuously inferred 
     */
    class ContinuousInferenceHandler extends EventListener {

        /** The "containing" Reasoning Manager*/
        private ReasoningManager container = null;

        /** The CtxAttribute that will be re-evaluated as a consequence of an input attribute changing*/
        private ICtxAttribute targetAttr = null;

        /** The type of the input attribute whose changes will trigger reevaluation 
         *  An input attribute with that type MIGHT be in the same scope of the 
         *  affected targetAttr OR in the Device Entity scope*/
        private String trackedType = null;

        /** The rule that has to be reevaluated as a consequence of a change in the input attribute */
        private IRule rule = null;

        private ICtxEntityIdentifier deviceId = null;

        public ContinuousInferenceHandler(ReasoningManager container, ICtxAttribute targetAttr, String trackedType, IRule rule) {
            super();
            this.container = container;
            this.targetAttr = targetAttr;
            this.trackedType = trackedType;
            this.rule = rule;
            try {
                this.deviceId = container.ctxBroker.retrieveDevice().getCtxIdentifier();
            } catch (ContextException e) {
                container.doError("Unable to retrieve device Id: " + e.getLocalizedMessage(), e);
            }
            container.evtMgr.registerListener(this, new String[] { PeerEventTypes.CONTEXT_REMOVE_EVENT }, null);
        }

        @Override
        public void handlePSSEvent(PSSEvent event) {
            container.logger.warn("Received unexpected PSS event " + event.geteventName());
        }

        @Override
        public void handlePeerEvent(PeerEvent event) {
            if (event.geteventType().equalsIgnoreCase(PeerEventTypes.CONTEXT_UPDATE_EVENT)) {
                this.handleCtxAttributeChangeEvt(event);
            } else if (event.geteventType().equalsIgnoreCase(PeerEventTypes.CONTEXT_REMOVE_EVENT)) {
                this.handleCtxAttributeRemovalEvt(event);
            } else container.logger.warn("Received unexpected Peer event " + event.geteventName());
        }

        private void handleCtxAttributeChangeEvt(PeerEvent event) {
            ICtxAttribute changedCtxAttribute = (ICtxAttribute) event.geteventInfo();
            String desc = "'" + changedCtxAttribute.getType() + "'='" + changedCtxAttribute.getStringValue() + "' managed by CiHandler " + this;
            if (changedCtxAttribute.getScope().equals(this.deviceId)) {
                container.doDebug("[RM] Change on input attribute [Device Scope]: " + desc);
            } else {
                container.doDebug("[RM] Change on input attribute [Attribute Scope]: " + desc);
            }
            String filteredCtxAttrType = ReasoningManager.getFilteredCtxType(changedCtxAttribute.getType());
            if (container.mapper.inputTypes4ContinuouslyInferredAttributes.containsKey(filteredCtxAttrType)) {
                ICtxRefiner<IRule> ctxRefiner = null;
                ctxRefiner = (ICtxRefiner<IRule>) container.ruleType2ICtxRefiner.get(this.rule.getClass());
                if (ctxRefiner != null) {
                    try {
                        long start = System.currentTimeMillis();
                        this.targetAttr = (ICtxAttribute) container.ctxBroker.retrieve(this.targetAttr.getCtxIdentifier(), false);
                        long afterRetrieval = System.currentTimeMillis();
                        container.triggerRefiner(ctxRefiner, this.targetAttr, this.rule, container);
                        long afterInference = System.currentTimeMillis();
                        container.ctxBroker.update(this.targetAttr, CtxOriginType.INFERRED);
                        long afterUpdate = System.currentTimeMillis();
                        container.doDebug("[RM] Time Total '" + targetAttr.getType() + "'='" + targetAttr.getStringValue() + "' - " + "Total = retrieve+infer+update --> " + (afterUpdate - start) + " = " + (afterRetrieval - start) + "+" + (afterInference - afterRetrieval) + "+" + (afterUpdate - afterInference));
                    } catch (NotInferredException e1) {
                        container.doError("Unable to continuously infer attribute: " + e1.getLocalizedMessage(), e1);
                    } catch (ContextException e) {
                        container.doError("Unable to update the CtxOriginType of continuously inferred CtxAttribute " + this.targetAttr.getCtxIdentifier() + ": " + e.getLocalizedMessage(), e);
                    }
                }
            }
        }

        private void handleCtxAttributeRemovalEvt(PeerEvent event) {
            if (!(event.geteventInfo() instanceof ICtxAttribute)) return;
            ICtxAttribute removedCtxAttribute = (ICtxAttribute) event.geteventInfo();
            if (this.trackedType.equalsIgnoreCase(removedCtxAttribute.getType())) {
                String desc = "'" + removedCtxAttribute.getType() + "'='" + removedCtxAttribute.getStringValue() + "'\nCtxId='" + removedCtxAttribute.getCtxIdentifier() + "'\nmanaged by CiHandler " + this;
                if (removedCtxAttribute.getScope().equals(this.deviceId)) {
                    container.doDebug("[RM] Removal of input attribute [Device Scope]: " + desc);
                } else {
                    container.doDebug("[RM] Removal of input attribute [Attribute Scope]: " + desc);
                }
                try {
                    container.evtMgr.unregisterListener(this, null);
                    Integer currentCounter = null;
                    currentCounter = container.mapper.inputTypes4ContinuouslyInferredAttributes.get(this.trackedType);
                    if (currentCounter == null || (currentCounter != null && currentCounter.intValue() == 0)) {
                        container.mapper.inputTypes4ContinuouslyInferredAttributes.remove(this.trackedType);
                    } else {
                        Integer updatedCounter = currentCounter - 1;
                        if (updatedCounter.intValue() == 0) {
                            container.mapper.inputTypes4ContinuouslyInferredAttributes.remove(this.trackedType);
                        } else {
                            container.mapper.inputTypes4ContinuouslyInferredAttributes.put(this.trackedType, updatedCounter);
                        }
                    }
                } catch (EMSException e) {
                    container.doError("Unable to unregister listener on Continuously Inferred Ctx Attribute", e);
                }
            }
        }
    }
}
