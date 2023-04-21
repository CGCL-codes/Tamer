package com.csaba.account.overview;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

public class LastTransactionsLabelProvider implements ITableLabelProvider {

    @Override
    public Image getColumnImage(Object arg0, int arg1) {
        return null;
    }

    @Override
    public String getColumnText(Object arg0, int colIndex) {
        Transaction t = (Transaction) arg0;
        switch(colIndex) {
            case 0:
                return t.date;
            case 1:
                return t.targettAccountNo;
            case 2:
                return t.details;
            case 3:
                return t.amount;
            default:
                return t.currency;
        }
    }

    @Override
    public void addListener(ILabelProviderListener arg0) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean isLabelProperty(Object arg0, String arg1) {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener arg0) {
    }
}
