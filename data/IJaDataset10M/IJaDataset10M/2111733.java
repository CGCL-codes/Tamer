package net.sphene.gwt.widgets.slider;

import com.google.gwt.user.client.ui.Widget;

/**
 * a simple interface which can be used by implementors to verify that a given value
 * can be used for the given slider.
 * 
 * @author kahless
 */
public interface ValueChangeVerifier {

    /**
	 * receives the old value and the new value and should verify that the user is allowed
	 * to make the change. should return the new value.
	 * 
	 * @param sender sender of the event.
	 * @param oldValue the old value currently set.
	 * @param newValue the new value which should be set..
	 * @return the verified new value.
	 */
    public double preValueChange(Widget sender, double oldValue, double newValue);
}
