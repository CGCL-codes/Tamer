package org.zkoss.zul.api;

/**
 * A column of the footer of a list box ({@link Listbox}). Its parent must be
 * {@link Listfoot}.
 * 
 * <p>
 * Unlike {@link Listheader}, you could place any child in a list footer.
 * <p>
 * Note: {@link Listcell} also accepts children.
 * <p>
 * Default {@link #getZclass}: z-list-footer.(since 3.5.0)
 * 
 * @author tomyeh
 * @since 3.5.2
 */
public interface Listfooter extends org.zkoss.zul.impl.api.LabelImageElement {

    /**
	 * Returns the listbox that this belongs to.
	 */
    public org.zkoss.zul.api.Listbox getListboxApi();

    /**
	 * Returns the column index, starting from 0.
	 */
    public int getColumnIndex();

    /**
	 * Returns the list header that is in the same column as this footer, or
	 * null if not available.
	 */
    public org.zkoss.zul.api.Listheader getListheaderApi();

    /**
	 * Returns number of columns to span this footer. Default: 1.
	 */
    public int getSpan();

    /**
	 * Sets the number of columns to span this footer.
	 * <p>
	 * It is the same as the colspan attribute of HTML TD tag.
	 */
    public void setSpan(int span);
}
