package org.lcelb.accounts.manager.ui.extensions.data.adapters;

import java.text.SimpleDateFormat;
import org.eclipse.jface.resource.ImageDescriptor;
import org.lcelb.accounts.manager.common.ICommonConstants;
import org.lcelb.accounts.manager.data.extensions.transaction.MonthTransactions;
import org.lcelb.accounts.manager.ui.data.adapters.ModelContainerAdapter;
import org.lcelb.accounts.manager.ui.extensions.AccountsManagerUIExtensionsActivator;
import org.lcelb.accounts.manager.ui.extensions.IImageKeys;

/**
 * @author gbrocard
 * 
 * 6 nov. 06
 */
public class MonthTransactionsAdapter extends ModelContainerAdapter {

    private static final String DATE_FORMAT = "MMMMMMMMM";

    /**
   * @see org.lcelb.accounts.manager.ui.data.IViewerAdapter#getElements(java.lang.Object)
   */
    public Object[] getElements(Object object_p) {
        MonthTransactions month = (MonthTransactions) object_p;
        return month.getTransactions().toArray();
    }

    /**
   * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(java.lang.Object)
   */
    @Override
    public Object[] getChildren(Object o_p) {
        return ICommonConstants.NO_ELEMENTS;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.data.IViewerAdapter#hasChildren(java.lang.Object)
   */
    public boolean hasChildren(Object element_p) {
        return false;
    }

    /**
   * @see org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(java.lang.Object)
   */
    @Override
    public ImageDescriptor getImageDescriptor(Object object_p) {
        return AccountsManagerUIExtensionsActivator.getDefault().getImageDescriptor(IImageKeys.IMG_MONTH_CALENDAR);
    }

    /**
   * @see org.eclipse.ui.model.IWorkbenchAdapter#getLabel(java.lang.Object)
   */
    @Override
    public String getLabel(Object o_p) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(((MonthTransactions) o_p).getDate());
    }

    /**
   * @see org.lcelb.accounts.manager.ui.data.adapters.AbstractContainerAdapter#forwardOnAddModelNotifier()
   */
    @Override
    protected boolean forwardOnAddModelNotifier() {
        return true;
    }
}
