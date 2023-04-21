package uk.ac.leeds.comp.ui.base;

/**
 * Interface defining some basic methods to display a Java {@link Object}.
 * 
 * Because sometimes it's useful to give extra information about a presented
 * object, this view also provides method {@link #setLongDescription(String)}
 * which accepts a {@link String} with longer information that can be presented
 * to the user to further identify the presented data.
 * 
 * @param <PresentedDataType>
 *            the type of the {@link Object} presented by this view.
 * 
 * @author rdenaux
 * 
 */
public interface UIView<PresentedDataType extends Object> extends UIComponent {

    /**
	 * Returns an instance of the DataType used to identify this {@link UIView}.
	 * 
	 * @return
	 */
    PresentedDataType getPresentedData();

    /**
	 * Attempts to set aCandidate {@link Object} as the presented data for this
	 * {@link UIView}. If the candidate {@link Object} is not supported by this
	 * {@link UIView}, then an {@link IllegalArgumentException} will be thrown.
	 * 
	 * @param aCandidate
	 *            {@link Object} to be used as the data to present in this view.
	 */
    void setAsPresentedData(Object aCandidate) throws IllegalArgumentException;

    /**
	 * Default way of setting the data to be shown by this {@link UIView}.
	 * Usually this data will come from a {@link UIModel}.
	 * 
	 * @param aDataToPresent
	 *            must never be <code>null</code> because a view always needs
	 *            some data to be able to present something to the user.
	 */
    void setPresentedData(PresentedDataType aDataToPresent);

    /**
	 * Use this method to set the tooltip text for this view. There is no
	 * guarantee that this text will actually be shown, each implementation will
	 * provide its own implementation for this. However, because it's difficult
	 * to predict how tooltips will behave for a whole item view, the tooltip
	 * implementation may differ. For instance, a view which shows multiple
	 * subcomponents may define different tooltips for each subcomponent, in
	 * which case there will be no single tooltip for the whole item view. In
	 * such a case the implementation will need to define what the effect will
	 * be of invoking this method.
	 */
    void setLongDescription(String aTooltipText);
}
