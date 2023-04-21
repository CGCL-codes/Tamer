package de.jformular.config;

import de.jformular.context.AttributeContext;
import de.jformular.event.FormularEvent;
import de.jformular.event.ForwardEvent;
import de.jformular.event.ForwardEventHandler;
import de.jformular.exception.FormularRuntimeException;
import de.jformular.factory.FormularIdentityFactory;
import de.jformular.interfaces.FormularIdentity;
import de.jformular.util.ClassBuilder;
import de.jformular.util.log.Log;
import de.jformular.xmlbinding.formularconfiguration.Forward;
import de.jformular.xmlbinding.formularconfiguration.GlobalForwards;
import de.jformular.xmlbinding.formularconfiguration.SegmentDefinition;

/**
 * Class declaration
 * @author Frank Dolibois, fdolibois@itzone.de, http://www.itzone.de
 * @version $Id: ForwardHelper.java,v 1.6 2002/10/14 14:02:38 fdolibois Exp $
 */
public class ForwardHelper {

    private FormularDefinitionHelper formularDefinitionHelper = new FormularDefinitionHelper();

    private ConditionHelper conditionHelper = new ConditionHelper();

    private Forward forward;

    private AttributeContext context;

    /**
     * Constructor declaration
     */
    public ForwardHelper() {
    }

    /**
     */
    public Forward getReferdForward(Forward forward, FormularEvent event) {
        GlobalForwards globalForwards = formularDefinitionHelper.getFormularDefinition(event.getFormularIdentity()).getGlobalForwards();
        if (globalForwards != null) {
            for (int i = 0; i < globalForwards.getForwardCount(); i++) {
                Forward refer = globalForwards.getForward(i);
                if (forward.getRefid().equals(refer.getId())) {
                    return refer;
                }
            }
        }
        throw new FormularRuntimeException("refered forward " + forward.getRefid() + " not found");
    }

    /**
     */
    public Forward getForward(FormularEvent event) {
        SegmentDefinition segmentDefinition = formularDefinitionHelper.getSegmentDefinition(event.getFormularIdentity());
        AttributeContext context = event.getFormularContainer().getContext();
        for (int i = 0; i < segmentDefinition.getForwardCount(); i++) {
            Forward forward = segmentDefinition.getForward(i);
            if (conditionHelper.isValid(forward.getCondition(), context)) {
                if (forward.getRefid() != null) {
                    forward = getReferdForward(forward, event);
                }
                String command = FormularEvent.COMMAND_PREFIX + forward.getCommand();
                if (command.equalsIgnoreCase(event.getCommand()) && conditionHelper.isValid(forward.getCondition(), context)) {
                    return forward;
                }
            }
        }
        return null;
    }

    /**
     */
    public ForwardEvent getForwardEvent(FormularIdentity identity) {
        String uri = formularDefinitionHelper.getSegmentDefinition(identity).getUri();
        return getForwardEvent(uri, identity);
    }

    /**
     */
    public ForwardEvent getForwardEvent(String uri, FormularIdentity identity) {
        ForwardEvent forwardEvent = new ForwardEvent();
        if (Log.getLogger().isDebugEnabled()) {
            Log.getLogger().debug("Create ForwardEvent for FormularIdentity " + identity + " and URI " + uri);
        }
        forwardEvent.setUri(uri);
        forwardEvent.setFormularIdentity(identity);
        return forwardEvent;
    }

    /**
     * get the forward information for the given event
     */
    public ForwardEvent getForwardEvent(FormularEvent event) {
        Forward forward = getForward(event);
        if (forward != null) {
            if (forward.hasIdentity() && (forward.getUri() != null)) {
                throw new FormularRuntimeException("Can not mix url " + forward.getUri() + " and identity " + forward.getIdentity() + " in same forward statement");
            }
            if (forward.getForwardHandler() != null) {
                try {
                    ForwardEventHandler forwardEventHandler = (ForwardEventHandler) ClassBuilder.build(forward.getForwardHandler().getType());
                    ForwardEvent forwardEvent = forwardEventHandler.getForwardEvent(event, forward.getForwardHandler().getParam());
                    if (forwardEvent != null) {
                        return forwardEvent;
                    }
                } catch (Exception ex) {
                    new FormularRuntimeException(ex);
                }
            }
            if (forward.hasIdentity()) {
                FormularIdentity identity = FormularIdentityFactory.getInstance().createIdentity(event.getFormularIdentity().getName(), forward.getIdentity());
                return getForwardEvent(identity);
            } else {
                return getForwardEvent(forward.getUri(), null);
            }
        }
        return null;
    }
}
