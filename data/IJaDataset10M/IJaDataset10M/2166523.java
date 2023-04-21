package mipt.aaf.swing.mdi.data;

import mipt.aaf.mdi.DocModel;
import mipt.aaf.mdi.MDIView;
import mipt.aaf.mdi.data.DataDocModel;
import mipt.aaf.swing.ApplicationResource;
import mipt.aaf.swing.RootView;
import mipt.aaf.swing.ViewModelAgent;
import mipt.aaf.swing.mdi.MDISwingApplicationView;

/**
 * Changes doc strings, depends on DataDocModel
 * @author Evdokimov
 */
public class DataMDISwingApplicationView extends MDISwingApplicationView {

    public DataMDISwingApplicationView() {
        super();
    }

    public DataMDISwingApplicationView(RootView rootView, ApplicationResource resource) {
        super(rootView, resource);
    }

    public DataMDISwingApplicationView(RootView rootView, ApplicationResource resource, ViewModelAgent modelAgent) {
        super(rootView, resource, modelAgent);
    }

    public DataMDISwingApplicationView(MDIView realView, RootView rootView, ApplicationResource resource) {
        super(realView, rootView, resource);
    }

    public DataMDISwingApplicationView(MDIView realView, RootView rootView, ApplicationResource resource, ViewModelAgent modelAgent) {
        super(realView, rootView, resource, modelAgent);
    }

    public DataMDISwingApplicationView(MDIView realView) {
        super(realView);
    }

    /**
	 * @see mipt.aaf.swing.mdi.MDISwingApplicationView#getDocShortName(mipt.aaf.mdi.DocModel)
	 */
    public String getDocShortName(DocModel docModel) {
        return ((DataDocModel) docModel).getDocParentName();
    }

    /**
	 * See createOpenAgainItem()
	 * @see mipt.aaf.swing.mdi.MDISwingApplicationView#getDocTitle(mipt.aaf.mdi.DocModel)
	 */
    public String getDocTitle(DocModel docModel) {
        String title = super.getDocTitle(docModel);
        String parentName = getDocShortName(docModel);
        if (parentName != null) title = title + " : " + parentName;
        return title;
    }
}
