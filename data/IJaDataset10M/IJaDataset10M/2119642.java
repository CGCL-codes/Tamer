package org.zkoss.zul.api;

import org.zkoss.zk.ui.WrongValueException;

/**
 * A slider with both a min and max handle.
 * <p>
 * Default {@link #getZclass} as follows: (since 3.5.0)
 * <ol>
 * <li>Case 1: If {@link #getOrient()} is vertical, "z-slider-ver" is assumed</li>
 * <li>Case 2: If {@link #getOrient()} is horizontal, "z-slider-hor" is assumed</li>
 * </ol>
 */
public interface Rangeslider extends org.zkoss.zul.impl.api.XulElement {

    /**
	 * Returns the orient.
	 * <p>
	 * Default: "horizontal".
	 */
    public String getOrient();

    /**
	 * Sets the orient.
	 * <p>
	 * Default : "horizontal"
	 * 
	 * @param orient
	 *            either "horizontal" or "vertical".
	 */
    public void setOrient(String orient) throws WrongValueException;

    /**
	 * Returns the sliding text.
	 * <p>
	 * Default : "{0}"
	 * 
	 */
    public String getSlidingtext();

    /**
	 * Sets the sliding text. The syntax "{0}" will be replaced with the
	 * position at client side.
	 * 
	 */
    public void setSlidingtext(String slidingtext);

    /**
	 * Returns whether it is a vertical slider.
	 * 
	 */
    public boolean isVertical();

    /**
	 * Returns the name of this component.
	 * <p>
	 * Default: null.
	 * <p>
	 * The name is used only to work with "legacy" Web application that handles
	 * user's request by servlets. It works only with HTTP/HTML-based browsers.
	 * It doesn't work with other kind of clients.
	 * <p>
	 * Don't use this method if your application is purely based on ZK's
	 * event-driven model.
	 * 
	 */
    public String getName();

    /**
	 * Sets the name of this component.
	 * <p>
	 * The name is used only to work with "legacy" Web application that handles
	 * user's request by servlets. It works only with HTTP/HTML-based browsers.
	 * It doesn't work with other kind of clients.
	 * <p>
	 * Don't use this method if your application is purely based on ZK's
	 * event-driven model.
	 * 
	 * @param name
	 *            the name of this component.
	 */
    public void setName(String name);
}
