package org.eclipse.emf.edit.provider;

/**
 * This is the interface needed to provide labels for items in a TableViewer.
 * This interface is similar to {@link IItemLabelProvider}, but this will pass additional information, 
 * namely the column index.
 */
public interface ITableItemLabelProvider {

    /**
   * This does the same thing as ITableLabelProvider.getColumnText.
   */
    public String getColumnText(Object object, int columnIndex);

    /**
   * This does the same thing as ITableLabelProvider.getColumnImage.
   */
    public Object getColumnImage(Object object, int columnIndex);
}
