package net.ar.guia.managers.contributors;

import net.ar.guia.*;
import net.ar.guia.helpers.*;
import net.ar.guia.own.implementation.*;
import net.ar.guia.own.interfaces.*;

/**
 * Visita cada componente de una ventana, busca por cada uno el contribuidor que
 * le corresponda y lo asigna a una propiedad del JComponent
 * 
 * @author Fernando Damian Petrola
 */
public class ComponentContributorAssigner extends ComponentVisitorAdapter {

    public static final String CONTRIBUTOR_NAME_PROPERTY = "theContributor";

    protected String theWindowName;

    protected ContributorManager theContributionStateManager = new ContributorManager();

    public ComponentContributorAssigner(VisualComponent aTopComponent) {
        theWindowName = aTopComponent.getTypeId();
    }

    public void assignContributorsFrom(VisualComponent aComponent) {
        aComponent.accept(this);
    }

    public void visitComponentBegin(VisualComponent aComponent) {
        ComponentContributor theContributor = getContributor(aComponent);
        aComponent.setContributor(theContributor);
        theContributor.setComponent(aComponent.getOwner());
    }

    protected ComponentContributor getContributor(VisualComponent aComponent) {
        ComponentContributor result = aComponent.getContributor();
        if (result == null) {
            String theContributorClassName = (String) aComponent.getClientProperty(CONTRIBUTOR_NAME_PROPERTY);
            if (theContributorClassName == null || theContributorClassName.equals("")) {
                theContributorClassName = theContributionStateManager.getContributorClassNameFromTreeState(theWindowName, GuiaFramework.getInstance().getComponentNameManager().getUniqueId(aComponent));
                if (theContributorClassName == null || theContributorClassName.equals("")) theContributorClassName = GuiaFramework.getInstance().getContributorManager().getDefaultComponentContributorClassName(aComponent.getUIClass());
            }
            try {
                result = (ComponentContributor) Class.forName(theContributorClassName).newInstance();
            } catch (Exception e) {
                throw new GuiaException(e);
            }
        }
        return result;
    }
}
