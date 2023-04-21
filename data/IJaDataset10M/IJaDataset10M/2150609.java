package org.lcelb.accounts.manager.ui.extensions.data.adapters;

import org.eclipse.jface.resource.ImageDescriptor;
import org.lcelb.accounts.manager.data.AbstractOwner;
import org.lcelb.accounts.manager.ui.data.adapters.ModelContainerAdapter;
import org.lcelb.accounts.manager.ui.extensions.AccountsManagerUIExtensionsActivator;
import org.lcelb.accounts.manager.ui.extensions.IImageKeys;
import org.lcelb.accounts.manager.ui.extensions.data.Banks;
import org.lcelb.accounts.manager.ui.extensions.data.Categories;
import org.lcelb.accounts.manager.ui.extensions.data.Payments;
import org.lcelb.accounts.manager.ui.extensions.data.Validities;

/**
 * Created on 20 nov. 06
 * 
 * @author fournier
 * 
 */
public class AbstractOwnerAdapter extends ModelContainerAdapter {

    private Banks _banks;

    private Categories _categories;

    private Payments _payments;

    private Validities _validities;

    /**
   * @see org.lcelb.accounts.manager.ui.data.adapters.AbstractViewerAdapter#getElements(java.lang.Object)
   */
    @Override
    public Object[] getElements(Object object_p) {
        AbstractOwner person = (AbstractOwner) object_p;
        Object banks = getBanks(person);
        Object categories = getCategories(person);
        Object payments = getPayments(person);
        Object validities = getValidities(person);
        Object[] result = { banks, categories, payments, validities };
        return result;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.data.adapters.AbstractViewerAdapter#getChildren(java.lang.Object)
   */
    public Object[] getChildren(Object object_p) {
        Object[] result = getElements(object_p);
        forwardNotifiersFor(object_p, result);
        return result;
    }

    /**
   * Return the UI Validities object for this owner.
   * 
   * @param person_p
   * @return
   */
    private Object getValidities(AbstractOwner person_p) {
        if (null == _validities) {
            _validities = new Validities(person_p);
        }
        return _validities;
    }

    /**
   * Return the UI banks object for this owner.
   * 
   * @param person_p
   * @return
   */
    private Object getBanks(AbstractOwner person_p) {
        if (null == _banks) {
            _banks = new Banks(person_p);
        }
        return _banks;
    }

    /**
   * Return the UI categories object for this owner.
   * 
   * @param person_p
   * @return
   */
    private Categories getCategories(AbstractOwner person_p) {
        if (null == _categories) {
            _categories = new Categories(person_p);
        }
        return _categories;
    }

    /**
   * Return the UI categories object for this owner.
   * 
   * @param person_p
   * @return
   */
    private Payments getPayments(AbstractOwner person_p) {
        if (null == _payments) {
            _payments = new Payments(person_p);
        }
        return _payments;
    }

    /**
   * @see org.eclipse.ui.model.IWorkbenchAdapter#getImageDescriptor(java.lang.Object)
   */
    @Override
    public ImageDescriptor getImageDescriptor(Object object_p) {
        return AccountsManagerUIExtensionsActivator.getDefault().getImageDescriptor(IImageKeys.IMG_OWNER);
    }

    /**
   * @see org.eclipse.ui.model.IWorkbenchAdapter#getLabel(java.lang.Object)
   */
    @Override
    public String getLabel(Object o_p) {
        AbstractOwner person = (AbstractOwner) o_p;
        return person.getName();
    }

    /**
   * @see org.lcelb.accounts.manager.ui.data.IViewerAdapter#hasChildren(java.lang.Object)
   */
    public boolean hasChildren(Object element_p) {
        return true;
    }
}
