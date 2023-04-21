package net.ar.guia.ui;

import java.util.*;
import net.ar.guia.managers.persistence.*;
import net.ar.guia.own.interfaces.*;
import net.ar.guia.own.layouters.*;
import net.ar.guia.render.markup.*;
import net.ar.guia.render.templates.*;
import net.ar.guia.render.templates.html.*;
import net.ar.guia.visitor.*;

public class PanelUIContributor extends DefaultComponentUIContributor {

    public void doRenderingContribution(final RenderingContributionContainer theRenderingContribManager) {
        PanelComponent panel = (PanelComponent) component;
        boolean useTemplate = panel.getLayouter() instanceof TemplateLayouter;
        Template theTemplate = useTemplate ? ((TemplateLayouter) panel.getLayouter()).getClonedTemplate() : new HtmlDivsTemplate();
        StringBuffer theInitScript = new StringBuffer();
        DefaultRenderingContributionContainer defaultRenderingContributionContainer = (DefaultRenderingContributionContainer) theRenderingContribManager;
        for (Iterator i = panel.getChilds().iterator(); i.hasNext(); ) {
            VisualComponent child = (VisualComponent) i.next();
            if (child.getStyle().isVisible() || true) {
                Visitable theRendering = theRenderingContribManager.getComponentRenderer().render(child);
                Tag theIdTag = defaultRenderingContributionContainer.getComponentIdTag(child);
                Object clientProperty = child.getClientProperty(TemplateLayouter.PROPERTY_NAME);
                theTemplate.addElement(new IdTagTemplateElement(useTemplate && clientProperty != null ? clientProperty : child, theRendering, theIdTag));
                theInitScript.append(defaultRenderingContributionContainer.getComponentInitScripts(child)[0]);
            }
        }
        defaultRenderingContributionContainer.doContribution(component, theTemplate, null, theInitScript.toString());
    }

    public void doPersistenceContribution(PersistenceContributionContainer aPersistenceManager) {
        aPersistenceManager.persistValue(getComponent(), getPanelString());
    }

    public boolean isPersistedValueEqualToModel(PersistenceContributionContainer aPersistenceManager) {
        return getPanelString().equals(aPersistenceManager.restoreValue(getComponent()));
    }

    protected String getPanelString() {
        StringBuffer result = new StringBuffer();
        for (Iterator i = getPanel().getChilds().iterator(); i.hasNext(); ) result.append(((VisualComponent) i.next()).toString());
        return result.toString();
    }

    protected PanelComponent getPanel() {
        return (PanelComponent) getComponent();
    }
}
