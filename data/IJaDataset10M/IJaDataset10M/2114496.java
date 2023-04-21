package org.lcelb.accounts.manager.ui.extensions.data.adapters;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.resource.ImageDescriptor;
import org.lcelb.accounts.manager.common.ICommonConstants;
import org.lcelb.accounts.manager.common.helper.MiscConverter;
import org.lcelb.accounts.manager.data.AbstractOwner;
import org.lcelb.accounts.manager.data.ModelElement;
import org.lcelb.accounts.manager.data.extensions.transaction.DatedTransaction;
import org.lcelb.accounts.manager.data.extensions.transaction.MonthTransactions;
import org.lcelb.accounts.manager.data.helper.DataGetter;
import org.lcelb.accounts.manager.data.helper.DataHelper;
import org.lcelb.accounts.manager.data.helper.YearMonitor;
import org.lcelb.accounts.manager.data.impl.ModelElementWithIdImpl;
import org.lcelb.accounts.manager.data.transaction.category.AbstractCategory;
import org.lcelb.accounts.manager.data.transaction.payment.AbstractPayment;
import org.lcelb.accounts.manager.data.transaction.validity.AbstractValidity;
import org.lcelb.accounts.manager.ui.data.adapters.ModelElementAdapter;
import org.lcelb.accounts.manager.ui.extensions.AccountsManagerUIExtensionsActivator;
import org.lcelb.accounts.manager.ui.extensions.IImageKeys;
import org.lcelb.accounts.manager.ui.extensions.data.UIData;
import org.lcelb.accounts.manager.ui.extensions.viewers.transactions.TransactionsViewerProvider;

/**
 * @author gbrocard
 * 
 * 6 nov. 06
 */
public class DatedTransactionAdapter extends ModelElementAdapter {

    /**
   * @see org.lcelb.accounts.manager.ui.data.adapters.AbstractViewerAdapter#compare(java.lang.Object,
   *      java.lang.Object)
   */
    @Override
    public int compare(Object adaptableObject_p, Object toCompare_p) {
        int result = super.compare(adaptableObject_p, toCompare_p);
        if (!(toCompare_p instanceof DatedTransaction)) {
            return result;
        }
        DatedTransaction transaction = (DatedTransaction) adaptableObject_p;
        DatedTransaction transactionToCompare = (DatedTransaction) toCompare_p;
        checkTransactionEmptiness(transaction);
        checkTransactionEmptiness(transactionToCompare);
        boolean isTransactionEmpty = (ICommonConstants.NO_ELEMENTS == transaction.getData());
        boolean isTransactionToCompareEmpty = (ICommonConstants.NO_ELEMENTS == transactionToCompare.getData());
        if (isTransactionEmpty && !isTransactionToCompareEmpty) {
            result = ICommonConstants.COMPARISON_GREATER;
        } else if (!isTransactionEmpty && isTransactionToCompareEmpty) {
            result = ICommonConstants.COMPARISON_LESS;
        }
        if (ICommonConstants.COMPARISON_EQUAL != result) {
            return result;
        }
        Date transactionDate = transaction.getDate();
        Date transactionToCompareDate = transactionToCompare.getDate();
        if ((null == transactionDate) && (null != transactionToCompareDate)) {
            result = ICommonConstants.COMPARISON_GREATER;
        } else if ((null != transactionDate) && (null == transactionToCompareDate)) {
            result = ICommonConstants.COMPARISON_LESS;
        }
        if ((ICommonConstants.COMPARISON_EQUAL == result) && (null != transactionDate) && (null != transactionToCompareDate)) {
            result = transactionDate.equals(transactionToCompareDate) || transactionDate.before(transactionToCompareDate) ? ICommonConstants.COMPARISON_LESS : ICommonConstants.COMPARISON_GREATER;
        }
        return result;
    }

    /**
   * Test emptiness for a given transaction. If so, set
   * ICommonConstants.NO_ELEMENTS flag.
   * 
   * @param transaction_p
   *          void
   */
    protected void checkTransactionEmptiness(DatedTransaction transaction_p) {
        if (ICommonConstants.NO_ELEMENTS == transaction_p.getData()) {
            return;
        }
        Collection<EStructuralFeature> featuresCollection = TransactionsViewerProvider.COLUMN_INDEX_TO_ECORE_ATTRIBUTE_ID.values();
        boolean isEmpty = true;
        for (Iterator<EStructuralFeature> features = featuresCollection.iterator(); features.hasNext() && isEmpty; ) {
            EStructuralFeature feature = features.next();
            EObject container = getContainer(transaction_p, feature);
            if (null != container) {
                isEmpty &= !container.eIsSet(feature);
            }
        }
        if (isEmpty) {
            transaction_p.setData(ICommonConstants.NO_ELEMENTS);
        }
    }

    /**
   * @see org.lcelb.accounts.manager.ui.data.IViewerAdapter#getColumnImageDescriptor(java.lang.Object,
   *      int)
   */
    public ImageDescriptor getColumnImageDescriptor(Object object_p, int columnIndex_p) {
        ImageDescriptor descriptor = null;
        if (TransactionsViewerProvider.LABEL_INDEX == columnIndex_p) {
            DatedTransaction transaction = (DatedTransaction) object_p;
            checkTransactionEmptiness(transaction);
            if (ICommonConstants.NO_ELEMENTS == transaction.getData()) {
                descriptor = AccountsManagerUIExtensionsActivator.getDefault().getImageDescriptor(IImageKeys.IMG_EDIT);
            }
        }
        return descriptor;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.data.IViewerAdapter#getColumnLabel(java.lang.Object,
   *      int)
   */
    public String getColumnLabel(Object object_p, int columnIndex_p) {
        DatedTransaction transaction = (DatedTransaction) object_p;
        EStructuralFeature attribute = TransactionsViewerProvider.COLUMN_INDEX_TO_ECORE_ATTRIBUTE_ID.get(columnIndex_p);
        EObject container = getContainer(transaction, attribute);
        Object value = null;
        if (null != container) {
            value = container.eGet(attribute);
        }
        return convertAttributeValueToString(transaction, value);
    }

    /**
   * @see org.lcelb.accounts.manager.ui.data.IViewerAdapter#getColumnValue(java.lang.Object,
   *      int)
   */
    public Object getColumnValue(Object object_p, int columnIndex_p) {
        DatedTransaction transaction = (DatedTransaction) object_p;
        EStructuralFeature attribute = TransactionsViewerProvider.COLUMN_INDEX_TO_ECORE_ATTRIBUTE_ID.get(columnIndex_p);
        Object result = null;
        EObject container = getContainer(transaction, attribute);
        result = getSpecificValue(transaction, container, attribute, columnIndex_p);
        if ((null == result) && (null != container)) {
            Object value = container.eGet(attribute);
            result = convertAttributeValueToString(transaction, value);
        }
        if (null == result) {
            result = ICommonConstants.EMPTY_STRING;
        }
        return result;
    }

    /**
   * Get container supporting value.
   * 
   * @param transaction_p
   * @param feature_p
   * @return EObject
   */
    protected EObject getContainer(DatedTransaction transaction_p, EStructuralFeature feature_p) {
        Class<?> containerClass = feature_p.getContainerClass();
        EObject container = null;
        if (containerClass.isInstance(transaction_p)) {
            container = transaction_p;
        } else if (containerClass.isInstance(transaction_p.getCategory())) {
            container = transaction_p.getCategory();
        } else if (containerClass.isInstance(transaction_p.getPayment())) {
            container = transaction_p.getPayment();
        } else if (containerClass.isInstance(transaction_p.getValidity())) {
            container = transaction_p.getValidity();
        }
        return container;
    }

    /**
   * Convert attribute value to a displayable String.
   * 
   * @param transaction_p
   * @param value_p
   * @return String
   */
    protected String convertAttributeValueToString(DatedTransaction transaction_p, Object value_p) {
        String result = null;
        if (value_p instanceof String) {
            result = (String) value_p;
        } else if (value_p instanceof Double) {
            if (!ICommonConstants.NO_ELEMENTS.equals(transaction_p.getData())) {
                result = NumberFormat.getCurrencyInstance().format(value_p);
            } else {
                result = ICommonConstants.EMPTY_STRING;
            }
        } else if (value_p instanceof Date) {
            result = MiscConverter.convertDate((Date) value_p);
        }
        return result;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.data.IViewerAdapter#canModify(java.lang.Object,
   *      int)
   */
    public boolean canModify(Object object_p, int columnIndex_p) {
        return true;
    }

    /**
   * Get specific value of an attribute.
   * 
   * @param transaction_p
   * @param container_p
   * @param attribute_p
   * @param columnIndex_p
   * @return Object
   */
    protected Object getSpecificValue(DatedTransaction transaction_p, EObject container_p, EStructuralFeature attribute_p, int columnIndex_p) {
        Object result = null;
        switch(columnIndex_p) {
            case TransactionsViewerProvider.AMOUNT_INDEX:
                result = ICommonConstants.EMPTY_STRING;
                if (!ICommonConstants.NO_ELEMENTS.equals(transaction_p.getData())) {
                    result = ICommonConstants.EMPTY_STRING + transaction_p.getAmount();
                }
                break;
            case TransactionsViewerProvider.DATE_INDEX:
                Calendar newCalendar = Calendar.getInstance();
                Date currentDate = transaction_p.getDate();
                if (null != currentDate) {
                    newCalendar.setTime(transaction_p.getDate());
                    result = ICommonConstants.EMPTY_STRING + newCalendar.get(Calendar.DATE);
                }
                break;
            case TransactionsViewerProvider.CATEGORY_INDEX:
            case TransactionsViewerProvider.PAYMENT_INDEX:
            case TransactionsViewerProvider.VALIDITY_INDEX:
                result = (null != container_p) ? ((ModelElementWithIdImpl) container_p).getData() : null;
                if (null != result) {
                    result = ((UIData) result).getIndexInWidget();
                } else {
                    result = ICommonConstants.DEFAULT_INDEX_IN_WIDGET;
                }
                break;
            default:
                break;
        }
        return result;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.data.IViewerAdapter#modify(java.lang.Object,
   *      int, java.lang.Object)
   */
    @SuppressWarnings("unchecked")
    public void modify(Object object_p, int columnIndex_p, Object value_p) {
        DatedTransaction transaction = (DatedTransaction) object_p;
        if (null == value_p) {
            return;
        }
        if (ICommonConstants.NO_ELEMENTS == transaction.getData()) {
            if (ICommonConstants.EMPTY_STRING.equals(value_p) || ICommonConstants.DEFAULT_INDEX_IN_WIDGET.equals(value_p)) {
                return;
            }
        }
        boolean newValueSet = false;
        switch(columnIndex_p) {
            case TransactionsViewerProvider.LABEL_INDEX:
                String oldLabel = transaction.getLabel();
                if (!value_p.equals(oldLabel)) {
                    transaction.setLabel((String) value_p);
                    newValueSet = true;
                }
                break;
            case TransactionsViewerProvider.CATEGORY_INDEX:
                {
                    AbstractOwner currentUser = DataHelper.getActiveOwner();
                    Integer positionInCombo = (Integer) value_p;
                    AbstractCategory category = (AbstractCategory) getModelObjectFromIndex(positionInCombo, (List<? extends ModelElement>) currentUser.getCategories());
                    AbstractCategory oldCategory = transaction.getCategory();
                    if ((null == oldCategory) || !oldCategory.equals(category)) {
                        transaction.setCategory(category);
                        newValueSet = true;
                    }
                }
                break;
            case TransactionsViewerProvider.PAYMENT_INDEX:
                {
                    AbstractOwner currentUser = DataHelper.getActiveOwner();
                    Integer positionInCombo = (Integer) value_p;
                    AbstractPayment payment = (AbstractPayment) getModelObjectFromIndex(positionInCombo, (List<? extends ModelElement>) currentUser.getPayments());
                    AbstractPayment oldPayment = transaction.getPayment();
                    if (oldPayment != payment) {
                        transaction.setPayment(payment);
                        newValueSet = true;
                    }
                }
                break;
            case TransactionsViewerProvider.VALIDITY_INDEX:
                {
                    AbstractOwner currentUser = DataHelper.getActiveOwner();
                    Integer positionInCombo = (Integer) value_p;
                    AbstractValidity validity = (AbstractValidity) getModelObjectFromIndex(positionInCombo, (List<? extends ModelElement>) currentUser.getValidities());
                    AbstractValidity oldValidity = transaction.getValidity();
                    if ((null == oldValidity) || !oldValidity.equals(validity)) {
                        transaction.setValidity(validity);
                        newValueSet = true;
                    }
                }
                break;
            case TransactionsViewerProvider.DATE_INDEX:
                {
                    String dateText = (String) value_p;
                    if ((null != dateText) && !ICommonConstants.EMPTY_STRING.equals(dateText.trim())) {
                        MonthTransactions month = (MonthTransactions) transaction.eContainer();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(month.getDate());
                        calendar.setLenient(false);
                        int date = Integer.parseInt(dateText);
                        calendar.set(Calendar.DATE, date);
                        try {
                            Date newDate = calendar.getTime();
                            Date oldDate = transaction.getDate();
                            if (!newDate.equals(oldDate)) {
                                transaction.setDate(newDate);
                                newValueSet = true;
                            }
                        } catch (IllegalArgumentException iae_p) {
                        }
                    }
                }
                break;
            case TransactionsViewerProvider.AMOUNT_INDEX:
                {
                    String amountText = MiscConverter.convertAmountToLocaleFormat((String) value_p);
                    Number newAmount = MiscConverter.convertAmount(amountText);
                    if (null != newAmount) {
                        double oldAmount = transaction.getAmount();
                        double newAmountValue = newAmount.doubleValue();
                        if (oldAmount != newAmountValue) {
                            transaction.setAmount(newAmountValue);
                            newValueSet = true;
                        }
                    }
                }
                break;
            default:
                System.out.println("Object = " + object_p + ", index = " + columnIndex_p + " value = " + value_p);
                break;
        }
        if (newValueSet) {
            YearMonitor.getInstance().addYearToSave(DataGetter.getContainingYear(transaction));
        }
    }

    /**
   * Get a model element from its registered index (@see ModelElement.setData())
   * (among given ones).
   * 
   * @param index_p
   * @param elements_p
   * @return ModelElement
   */
    protected ModelElement getModelObjectFromIndex(Integer index_p, List<? extends ModelElement> elements_p) {
        ModelElement result = null;
        for (Iterator<? extends ModelElement> objects = elements_p.iterator(); objects.hasNext() && (null == result); ) {
            ModelElement currentElement = (ModelElement) objects.next();
            if (index_p.equals(((UIData) currentElement.getData()).getIndexInWidget())) {
                result = currentElement;
            }
        }
        return result;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.data.IViewerAdapter#isDisplayableInTableViewer(java.lang.Object)
   */
    public boolean isDisplayableInTableViewer(Object element_p) {
        return true;
    }
}
