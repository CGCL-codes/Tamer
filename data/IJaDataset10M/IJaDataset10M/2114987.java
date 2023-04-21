package org.eclipse.jface.viewers;

/**
 * The ILazyContentProvider is the content provider
 * for table viewers created using the SWT.VIRTUAL flag that
 * only wish to return thier contents as they are queried.
 */
public interface ILazyContentProvider extends IContentProvider {

    /**
	 * Called when a previously-blank item becomes visible in the
	 * TableViewer. If the content provider knows the element
	 * at this row, it should respond by calling 
	 * TableViewer#replace(Object, int)
	 * 
	 * @param index The index that is being updateed in the
	 * table.
	 */
    public void updateElement(int index);
}
