package org.nexopenframework.ide.eclipse.jst.wizard.component;

import java.net.URL;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectTemplate;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.web.ui.internal.wizards.NewProjectDataModelFacetWizard;
import org.nexopenframework.ide.eclipse.jst.JstActivator;
import org.nexopenframework.ide.eclipse.jst.Messages;
import org.nexopenframework.ide.eclipse.jst.datamodel.component.NexOpenFacetProjectCreationDataModelProvider;
import org.osgi.framework.Bundle;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p></p>
 * 
 * @see org.eclipse.wst.web.ui.internal.wizards.NewProjectDataModelFacetWizard
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class NexOpenComponentProjectWizard extends NewProjectDataModelFacetWizard {

    public NexOpenComponentProjectWizard(IDataModel model) {
        super(model);
        setWindowTitle(Messages.getString("component.project.title"));
    }

    public NexOpenComponentProjectWizard() {
        super();
        setWindowTitle(Messages.getString("component.project.title"));
    }

    protected IDataModel createDataModel() {
        IDataModel model = DataModelFactory.createDataModel(new NexOpenFacetProjectCreationDataModelProvider());
        return model;
    }

    protected IWizardPage createFirstPage() {
        return new NexOpenComponentProjectFirstPage(model, "first.page");
    }

    protected ImageDescriptor getDefaultPageImageDescriptor() {
        final Bundle bundle = Platform.getBundle(JstActivator.PLUGIN_ID);
        final URL url = bundle.getEntry("icons/nexopen-component.gif");
        return ImageDescriptor.createFromURL(url);
    }

    /**
	 * <p>The eclipse template to load</p>
	 */
    protected IFacetedProjectTemplate getTemplate() {
        return ProjectFacetsManager.getTemplate("template.jst.nexopen.component");
    }

    protected String getFinalPerspectiveID() {
        return JstActivator.NEXOPEN_PERSPECTIVE_ID;
    }
}
