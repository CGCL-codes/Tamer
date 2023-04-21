package org.plazmaforge.studio.reportdesigner.dialogs.crosstab.providers;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.plazmaforge.studio.reportdesigner.ReportDesignerResources;
import org.plazmaforge.studio.reportdesigner.dialogs.CommonFolderProvider;
import org.plazmaforge.studio.reportdesigner.dialogs.providers.IObjectSelector;
import org.plazmaforge.studio.reportdesigner.model.crosstab.CrosstabColumnGroup;

/** 
 * @author Oleh Hapon
 * $Id: CrosstabColumnGroupProvider.java,v 1.4 2010/11/18 12:29:36 ohapon Exp $
 */
public class CrosstabColumnGroupProvider extends AbstractCrosstabGroupProvider {

    public CrosstabColumnGroupProvider(IObjectSelector parentSelector) {
        super(parentSelector);
        setName(ReportDesignerResources.Crosstab_Column_Group);
    }

    public CrosstabColumnGroupProvider(IObjectSelector parentSelector, IContentProvider contentProvider, ILabelProvider labelProvider, CommonFolderProvider commonFolderProvider) {
        super(parentSelector, contentProvider, labelProvider, commonFolderProvider);
        setName(ReportDesignerResources.Crosstab_Column_Group);
        setColumnNames(new String[] { ReportDesignerResources.Crosstab_Column_Group });
        setColumnSizes(new int[] { 150 });
        setColumnStyles(new int[] { SWT.NONE });
    }

    public Object createElement() {
        return new CrosstabColumnGroup();
    }

    public boolean deleteElement(Object element) {
        if (getSelectedCrosstab().hasSingleColumnGroup()) {
            openWarningDialog("Can not delete single Column Group");
            return false;
        }
        getSelectedCrosstab().removeColumnGroup((CrosstabColumnGroup) element);
        return true;
    }
}
