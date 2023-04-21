package org.apache.isis.extensions.wicket.ui.components.welcome;

import org.apache.isis.extensions.wicket.model.models.WelcomeModel;
import org.apache.isis.extensions.wicket.ui.ComponentFactory;
import org.apache.isis.extensions.wicket.ui.ComponentFactoryAbstract;
import org.apache.isis.extensions.wicket.ui.ComponentType;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

/**
 * {@link ComponentFactory} for {@link WelcomePanel}.
 */
public class WelcomePanelFactory extends ComponentFactoryAbstract {

    private static final long serialVersionUID = 1L;

    public WelcomePanelFactory() {
        super(ComponentType.WELCOME);
    }

    @Override
    public ApplicationAdvice appliesTo(IModel<?> model) {
        return appliesIf(model instanceof WelcomeModel);
    }

    public Component createComponent(String id, IModel<?> model) {
        return new WelcomePanel(id, (WelcomeModel) model);
    }
}
