package net.ontopia.topicmaps.webed.impl.actions.tmobject;

import net.ontopia.topicmaps.core.ScopedIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.webed.core.ActionIF;
import net.ontopia.topicmaps.webed.core.ActionParametersIF;
import net.ontopia.topicmaps.webed.core.ActionResponseIF;
import net.ontopia.topicmaps.webed.core.ActionRuntimeException;
import net.ontopia.topicmaps.webed.impl.utils.ActionSignature;

/**
 * PUBLIC: Action for adding a theme to a scoped object.
 */
public class AddTheme implements ActionIF {

    public void perform(ActionParametersIF params, ActionResponseIF response) {
        ActionSignature paramsType = ActionSignature.getSignature("baov");
        paramsType.validateArguments(params, this);
        ScopedIF scoped = (ScopedIF) params.get(0);
        TopicIF theme = (TopicIF) params.getTMObjectValue();
        if (theme == null) throw new ActionRuntimeException("No topic ID given to AddTheme action");
        scoped.addTheme(theme);
    }
}
