package mipt.crec.lab.gui.startup.mdi.data;

import javax.swing.Icon;
import mipt.aaf.mdi.DocModel;
import mipt.aaf.mdi.data.DataDocModel;
import mipt.aaf.swing.ApplicationResource;
import mipt.aaf.swing.RootView;
import mipt.aaf.swing.mdi.MDISwingApplicationView;
import mipt.crec.lab.AbstractModule;
import mipt.crec.lab.data.DataTypes;
import mipt.data.Data;

/**
 *  
 * @author Evdokimov
 */
public class LabsApplicationView extends MDISwingApplicationView {

    /**
	 * @param rootView
	 * @param resource
	 */
    public LabsApplicationView(RootView rootView, ApplicationResource resource) {
        super(rootView, resource);
    }

    /**
	 * @see mipt.aaf.swing.mdi.MDISwingApplicationView#getDocIcon(mipt.aaf.mdi.DocModel)
	 */
    public Icon getDocIcon(DocModel docModel) {
        boolean defaultVar = ((DataDocModel) docModel).getDocData().getString(DataTypes.VARIANT_fileName).equals(AbstractModule.DEFAULT_VARIANT);
        return getResource().getIcon(defaultVar ? DataTypes.LAB : DataTypes.VARIANT + "_");
    }

    /**
	 * @see mipt.aaf.swing.mdi.MDISwingApplicationView#getDocToolTip(mipt.aaf.mdi.DocModel)
	 */
    public String getDocToolTip(DocModel docModel) {
        return ((DataDocModel) docModel).getDocParentName();
    }

    /**
	 * @see mipt.aaf.swing.mdi.MDISwingApplicationView#getDocShortName(mipt.aaf.mdi.DocModel)
	 */
    public String getDocShortName(DocModel docModel) {
        String title = ((DataDocModel) docModel).getDocParent().getString(DataTypes.LAB_title);
        if (title == null) title = getResource().getString(docModel.getDocType() + "Title");
        return title;
    }

    /**
	 * @see mipt.aaf.swing.mdi.data.DataMDISwingApplicationView#getDocTitle(mipt.aaf.mdi.DocModel)
	 */
    public String getDocTitle(DocModel docModel) {
        Data data = ((DataDocModel) docModel).getDocData();
        String title = getDocShortName(docModel);
        if (!data.getString(DataTypes.VARIANT_fileName).equals(AbstractModule.DEFAULT_VARIANT)) title = data.getString(DataTypes.DATA_name) + " - " + title;
        return title;
    }
}
