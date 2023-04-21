package org.jrcaf.flow.ui.datasourceView.ui;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.jrcaf.core.registry.IPluginXMLAndParameterNameStrings;
import org.jrcaf.flow.ui.UIFlowPlugin;
import org.jrcaf.flow.ui.internal.ui.datasourceView.DatasourceViewDefinition;
import org.jrcaf.flow.ui.internal.ui.datasourceView.ViewedDatasourceDefinition;
import org.jrcaf.flow.ui.internal.ui.datasourceView.ui.DatasourceComposite;

/**
 * View to show datasources declared by extensions and handle search, object 
 * creation, filtering, displaying a hierarchical model-tree and the start of
 * activities associated to the model-object-types.   
 */
public class DatasourceView extends ViewPart {

    private DatasourceComposite composite;

    /**
	 * Creates a new datasource View
	 */
    public DatasourceView() {
        super();
    }

    /**
	 * This is a callback that will allow us
	 * to create the part and initialize it.
	 * @param aParent The parent of the created part. 
	 */
    @Override
    public void createPartControl(Composite aParent) {
        DatasourceComposite.PresentationStyle style = DatasourceComposite.PresentationStyle.TREE;
        IConfigurationElement configurationElement = getConfigurationElement();
        String id = configurationElement.getAttribute(IPluginXMLAndParameterNameStrings.ID_ATTRIBUTE);
        ViewedDatasourceDefinition[] datasourceDefinitions = null;
        ViewedDatasourceDefinition[] defaultDatasourceDefinitions = null;
        DatasourceViewDefinition datasourceView = UIFlowPlugin.getDatasourceViewRegistry().getDatasourceViewFor(id);
        datasourceDefinitions = datasourceView.getViewedDatasourceDefinitions();
        defaultDatasourceDefinitions = datasourceView.getDefaultViewedDatasourceDefinitions();
        try {
            style = DatasourceComposite.PresentationStyle.valueOf(datasourceView.getPresentationStyle());
        } catch (IllegalArgumentException ex) {
            UIFlowPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, UIFlowPlugin.ID_PLUGIN, IStatus.OK, "Unknown presentation style: " + datasourceView.getPresentationStyle() + " of: " + id, ex));
        }
        composite = new DatasourceComposite(aParent, datasourceDefinitions, defaultDatasourceDefinitions, style, getViewSite(), id);
    }

    /**
	 * Passing the focus request to the part's control.
	 */
    @Override
    public void setFocus() {
        composite.setFocus();
    }
}
