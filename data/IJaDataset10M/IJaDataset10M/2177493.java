package org.mobicents.ssf.flow.engine.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.commons.beanutils.ConvertUtils;
import org.mobicents.ssf.flow.config.FlowElementAttribute;
import org.mobicents.ssf.flow.config.spring.SipFlowDefinitionResource;
import org.mobicents.ssf.flow.definition.FlowDefinition;
import org.mobicents.ssf.flow.definition.StateDefinition;
import org.mobicents.ssf.flow.definition.registry.FlowDefinitionHolder;
import org.mobicents.ssf.flow.engine.AbstractState;
import org.mobicents.ssf.flow.engine.ActionList;
import org.mobicents.ssf.flow.engine.ActionState;
import org.mobicents.ssf.flow.engine.AnnotatedObject;
import org.mobicents.ssf.flow.engine.EndState;
import org.mobicents.ssf.flow.engine.Evaluate;
import org.mobicents.ssf.flow.engine.EvaluateList;
import org.mobicents.ssf.flow.engine.EvaluateState;
import org.mobicents.ssf.flow.engine.Flow;
import org.mobicents.ssf.flow.engine.Transition;
import org.mobicents.ssf.flow.engine.TransitionableState;
import org.mobicents.ssf.flow.engine.builder.template.AbstractAnnotatedTemplate;
import org.mobicents.ssf.flow.engine.builder.template.AbstractStateTemplate;
import org.mobicents.ssf.flow.engine.builder.template.ActionListTemplate;
import org.mobicents.ssf.flow.engine.builder.template.ActionStateTemplate;
import org.mobicents.ssf.flow.engine.builder.template.ActionTemplate;
import org.mobicents.ssf.flow.engine.builder.template.AttributeTemplate;
import org.mobicents.ssf.flow.engine.builder.template.EndStateTemplate;
import org.mobicents.ssf.flow.engine.builder.template.EvaluateListTemplate;
import org.mobicents.ssf.flow.engine.builder.template.EvaluateStateTemplate;
import org.mobicents.ssf.flow.engine.builder.template.EvaluateTemplate;
import org.mobicents.ssf.flow.engine.builder.template.FlowTemplate;
import org.mobicents.ssf.flow.engine.builder.template.PropertyTemplate;
import org.mobicents.ssf.flow.engine.builder.template.TransitionTemplate;
import org.mobicents.ssf.flow.engine.builder.template.TransitionableStateTemplate;
import org.mobicents.ssf.flow.engine.builder.xml.TemplateBuilder;
import org.mobicents.ssf.flow.engine.support.AbstractAction;
import org.mobicents.ssf.flow.internal.SipFlowResourceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class DefaultFlowDefinitionHolder implements FlowDefinitionHolder {

    private Logger logger = LoggerFactory.getLogger(DefaultFlowDefinitionHolder.class);

    private TemplateBuilder builder;

    private SipFlowDefinitionResource resource;

    private FlowDefinition flow;

    private long lastModified;

    private ResourceBundle evaluateBundle = ResourceBundle.getBundle("org.mobicents.ssf.flow.resources.evaluate-type");

    private ResourceBundle actionBundle = ResourceBundle.getBundle("org.mobicents.ssf.flow.resources.action-type");

    public DefaultFlowDefinitionHolder(SipFlowDefinitionResource resource, TemplateBuilder builder) {
        this.resource = resource;
        this.builder = builder;
        refresh();
    }

    public FlowTemplate getFlowTemplate() {
        return (FlowTemplate) this.builder.getTemplate();
    }

    public FlowDefinition getFlowDefinition() {
        return getFlowDefinition(false);
    }

    private FlowDefinition getFlowDefinition(boolean force) {
        if (this.flow == null || force) {
            FlowTemplate template = (FlowTemplate) this.builder.getTemplate();
            this.flow = createFlow(template);
        }
        return this.flow;
    }

    public String getFlowDefinitionId() {
        return this.flow.getId();
    }

    public SipFlowDefinitionResource getResource() {
        return this.resource;
    }

    public void refresh() {
        try {
            long resourceTime = this.resource.getPath().lastModified();
            if (this.lastModified != resourceTime) {
                this.builder.init();
                this.lastModified = resourceTime;
                getFlowDefinition(true);
            }
        } catch (IOException e) {
            logger.error("Cannot find resource:" + resource.getPath(), e);
            throw new BuildException(SipFlowResourceMessage.getMessage(112, resource.getPath()), e);
        }
    }

    protected FlowDefinition createFlow(FlowTemplate template) {
        Flow flow = new Flow();
        List<AbstractStateTemplate> states = template.getStateTemplateList();
        String startId = template.getStartStateId();
        setupAnnotatedObject(template, flow);
        Map map = flow.getAttributeMap();
        if (this.resource != null) {
            Set<FlowElementAttribute> attributes = this.resource.getAttributes();
            if (attributes != null) {
                for (FlowElementAttribute attribute : attributes) {
                    String name = attribute.getName();
                    String value = attribute.getValue();
                    String type = attribute.getType();
                    try {
                        if (type == null || type.trim().length() == 0) {
                            map.put(name, value);
                        } else {
                            Class clazz = getType(type);
                            map.put(name, ConvertUtils.convert(value, clazz));
                        }
                    } catch (Exception e) {
                        throw new BuildException(SipFlowResourceMessage.getMessage(113, attribute), e);
                    }
                }
            }
        }
        flow.setAttributeMap(map);
        flow.setStartState(startId);
        for (AbstractStateTemplate state : states) {
            createState(state, flow);
        }
        return flow;
    }

    private StateDefinition createState(AbstractStateTemplate template, Flow flow) {
        AbstractState state = null;
        if (template instanceof ActionStateTemplate) {
            state = new ActionState();
            setupActionState((ActionStateTemplate) template, (ActionState) state);
        } else if (template instanceof EvaluateStateTemplate) {
            state = new EvaluateState();
            setupEvaluateState((EvaluateStateTemplate) template, (EvaluateState) state);
        } else if (template instanceof EndStateTemplate) {
            state = new EndState();
            setupEndState((EndStateTemplate) template, (EndState) state);
        } else {
            throw new BuildException(SipFlowResourceMessage.getMessage(114, template));
        }
        setupAnnotatedObject(template, state);
        state.setOwner(flow);
        flow.addStateDefinition(state);
        return state;
    }

    private void setupEndState(EndStateTemplate template, EndState state) {
        ActionList actionList = new ActionList();
        this.setupActionList(template.getActionList(), actionList);
        state.setEndActionList(actionList);
        ActionList entryActionList = new ActionList();
        this.setupActionList(template.getEntryActionList(), entryActionList);
        state.setEntryActionList(entryActionList);
        List<ActionList> eventActionList = new ArrayList<ActionList>();
        this.setupActionLists(template.getEventActionLists(), eventActionList);
        state.setEventActionLists(eventActionList);
    }

    private void setupEvaluateState(EvaluateStateTemplate template, EvaluateState state) {
        setupTransitionableState(template, state);
        EvaluateListTemplate evaluateList = template.getEvaluateList();
        setupEvaluateList(evaluateList, state);
        ActionList entryActionList = new ActionList();
        setupActionList(template.getEntryActionList(), entryActionList);
        state.setEntryActionList(entryActionList);
        List<ActionList> eventActionList = new ArrayList<ActionList>();
        this.setupActionLists(template.getEventActionLists(), eventActionList);
        state.setEventActionLists(eventActionList);
        List<ActionList> exitActionList = new ArrayList<ActionList>();
        this.setupActionLists(template.getExitActionLists(), exitActionList);
        state.setExitActionLists(exitActionList);
    }

    private void setupEvaluateList(EvaluateListTemplate list, EvaluateState state) {
        List<EvaluateTemplate> evaluateTemplateList = list.getEvaluateList();
        EvaluateList evaluateList = new EvaluateList();
        for (EvaluateTemplate evaluateTemplate : evaluateTemplateList) {
            String type = evaluateTemplate.getType();
            if (type == null || type.trim().length() == 0) {
                type = "annotated";
            }
            if (logger.isTraceEnabled()) {
                logger.trace("seuptEvaluateList:type=" + type);
            }
            Evaluate evaluate = null;
            String className = evaluateBundle.getString(type);
            if (className != null) {
                try {
                    evaluate = (Evaluate) Class.forName(className).newInstance();
                } catch (Exception e) {
                    throw new BuildException(SipFlowResourceMessage.getMessage(116, type, className), e);
                }
            } else {
                throw new BuildException(SipFlowResourceMessage.getMessage(114, evaluateTemplate));
            }
            this.setupAnnotatedObject(evaluateTemplate, evaluate);
            this.setupProperty(evaluateTemplate, evaluate);
            evaluate.setParentEvaluateStateId(state.getId());
            evaluateList.addEvaluate(evaluate);
        }
        state.setEvaluateList(evaluateList);
    }

    private void setupProperty(EvaluateTemplate template, Evaluate evaluate) {
        List<PropertyTemplate> properties = template.getProperties();
        for (PropertyTemplate propertyTemplate : properties) {
            String name = propertyTemplate.getName();
            String value = propertyTemplate.getValue();
            evaluate.addProperty(name, value);
        }
    }

    private void setupActionState(ActionStateTemplate template, ActionState state) {
        setupTransitionableState(template, state);
        ActionList actionList = new ActionList();
        setupActionList(template.getActionList(), actionList);
        state.setActionList(actionList);
        ActionList entryActionList = new ActionList();
        setupActionList(template.getEntryActionList(), entryActionList);
        state.setEntryActionList(entryActionList);
        List<ActionList> eventActionList = new ArrayList<ActionList>();
        this.setupActionLists(template.getEventActionLists(), eventActionList);
        state.setEventActionLists(eventActionList);
        List<ActionList> exitActionList = new ArrayList<ActionList>();
        this.setupActionLists(template.getExitActionLists(), exitActionList);
        state.setExitActionLists(exitActionList);
    }

    private void setupActionLists(List<ActionListTemplate> actionListTemplates, List<ActionList> list) {
        if (logger.isTraceEnabled()) {
            logger.trace("setupActionLists:template=" + actionListTemplates);
        }
        if (actionListTemplates == null || actionListTemplates.isEmpty()) {
            return;
        }
        for (ActionListTemplate template : actionListTemplates) {
            ActionList actionList = new ActionList();
            setupActionList(template, actionList);
            list.add(actionList);
        }
        if (logger.isTraceEnabled()) {
            logger.trace("setupActionLists:list=" + list);
        }
    }

    private void setupActionList(ActionListTemplate actionListTemplate, ActionList list) {
        if (actionListTemplate == null) {
            return;
        }
        List<ActionTemplate> actionTemplateList = actionListTemplate.getActions();
        if (actionTemplateList == null) {
            return;
        }
        this.setupAnnotatedObject(actionListTemplate, list);
        for (ActionTemplate actionTemplate : actionTemplateList) {
            AbstractAction action = null;
            String type = actionTemplate.getType();
            if (type == null || type.trim().length() == 0) {
                type = "annotated";
            }
            String className = actionBundle.getString(type);
            if (className != null) {
                try {
                    action = (AbstractAction) Class.forName(className).newInstance();
                } catch (Exception e) {
                    throw new BuildException(SipFlowResourceMessage.getMessage(116, type, className), e);
                }
            } else {
                throw new BuildException(SipFlowResourceMessage.getMessage(114, actionTemplate));
            }
            this.setupAnnotatedObject(actionTemplate, action);
            List<PropertyTemplate> propertyList = actionTemplate.getProperties();
            for (PropertyTemplate propertyTemplate : propertyList) {
                String name = propertyTemplate.getName();
                String value = propertyTemplate.getValue();
                action.addProperty(name, value);
            }
            list.addAction(action);
        }
    }

    private void setupTransitionableState(TransitionableStateTemplate template, TransitionableState state) {
        List<TransitionTemplate> transitionList = template.getTransitions();
        for (TransitionTemplate transitionTemplate : transitionList) {
            Transition transition = new Transition();
            this.setupAnnotatedObject(transitionTemplate, transition);
            transition.setTo(transitionTemplate.getTo());
            transition.setOn(transitionTemplate.getOn());
            transition.setAction(transitionTemplate.getAction());
            state.addTransition(transition);
        }
    }

    private void setupAnnotatedObject(AbstractAnnotatedTemplate template, AnnotatedObject targetObject) {
        String id = template.getId();
        List<AttributeTemplate> attributes = template.getAttributes();
        String name = template.getName();
        String desc = template.getDescription();
        targetObject.setId(id);
        targetObject.setName(name);
        targetObject.setDescription(desc);
        Map map = new HashMap();
        if (attributes != null) {
            for (AttributeTemplate attribute : attributes) {
                String atype = attribute.getType();
                String aname = attribute.getName();
                String avalue = attribute.getValue();
                if (atype == null) {
                    map.put(aname, avalue);
                } else {
                    try {
                        Class clazz = getType(atype);
                        map.put(aname, ConvertUtils.convert(avalue, clazz));
                    } catch (Exception e) {
                        throw new BuildException(SipFlowResourceMessage.getMessage(115, template, attribute), e);
                    }
                }
            }
        }
        targetObject.setAttributeMap(map);
    }

    private Class getType(String type) throws ClassNotFoundException {
        type = type.trim();
        if (type.equals("boolean")) {
            return Boolean.class;
        } else if (type.equals("byte")) {
            return Byte.class;
        } else if (type.equals("char")) {
            return Character.class;
        } else if (type.equals("short")) {
            return Short.class;
        } else if (type.equals("int")) {
            return Integer.class;
        } else if (type.equals("long")) {
            return Long.class;
        } else if (type.equals("float")) {
            return Float.class;
        } else if (type.equals("double")) {
            return Double.class;
        }
        return Class.forName(type);
    }
}
