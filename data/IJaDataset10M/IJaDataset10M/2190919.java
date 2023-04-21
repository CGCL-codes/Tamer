package net.sourceforge.fluxion.runcible.swing.utils.impl;

import org.protege.editor.core.ui.view.ViewComponent;
import org.protege.editor.core.ui.view.ViewComponentPlugin;
import org.protege.editor.core.ui.view.ViewComponentPluginAdapter;
import org.protege.editor.core.ui.workspace.Workspace;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.renderer.OWLSystemColors;
import org.protege.editor.owl.ui.view.OWLObjectPropertyHierarchyViewComponent;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLObjectProperty;
import java.awt.*;
import java.util.Set;

/**
 * Author: Rob Davey<br> Date: 18-Oct-2007<br><br>
 * <p/>
 */
public class RuncibleOWLObjectPropertySelectorPanel extends AbstractRuncibleSelectorPanel {

    private OWLObjectPropertyHierarchyViewComponent view;

    private final OWLDescription desc;

    public RuncibleOWLObjectPropertySelectorPanel(OWLEditorKit editorKit, OWLDescription desc) {
        super(editorKit);
        this.desc = desc;
        createUI();
    }

    protected ViewComponentPlugin getViewComponentPlugin() {
        return new ViewComponentPluginAdapter() {

            public String getLabel() {
                return "Object properties";
            }

            public Workspace getWorkspace() {
                return getOWLEditorKit().getOWLWorkspace();
            }

            public ViewComponent newInstance() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
                view = new OWLObjectPropertyHierarchyViewComponent();
                view.setup(this);
                return view;
            }

            public Color getBackgroundColor() {
                return OWLSystemColors.getOWLObjectPropertyColor();
            }
        };
    }

    public OWLObjectProperty getSelectedOWLObjectProperty() {
        return view.getSelectedProperty();
    }

    public Set<OWLObjectProperty> getSelectedOWLObjectProperties() {
        return view.getSelectedProperties();
    }

    public void dispose() {
        view.dispose();
    }
}
