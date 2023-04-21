package com.icesoft.faces.util.event.servlet;

/**
 * The <code>ViewNumberRetrievedEvent</code> class represents an event that
 * should be fired whenever a view number is retrieved. A view number is always
 * associated with an ICEfaces ID. </p>
 */
public class ViewNumberRetrievedEvent extends AbstractSessionEvent implements ContextEvent {

    private int viewNumber;

    /**
     * Constructs a <code>ViewNumberRetrievedEvent</code> with the specified
     * <code>source</code> and <code>viewNumber</code>. </p>
     *
     * @param source     the HTTP session.
     * @param iceFacesId the ICEfaces ID identifying the session.
     * @param viewNumber the view number that has been retrieved.
     * @throws IllegalArgumentException if the one of the following happens:
     *                                  <ul> <li> the specified
     *                                  <code>source</code> is
     *                                  <code>null</code>. </li> <li> the
     *                                  specified <code>iceFacesId</code> is
     *                                  either <code>null</code> or empty. </li>
     *                                  </ul>
     */
    public ViewNumberRetrievedEvent(final Object source, final String iceFacesId, final int viewNumber) throws IllegalArgumentException {
        super(source, iceFacesId);
        this.viewNumber = viewNumber;
    }

    /**
     * Gets the view number of this <code>ViewNumberRetrievedEvent</code>. </p>
     *
     * @return the view number.
     * @see #getICEfacesID()
     */
    public int getViewNumber() {
        return viewNumber;
    }
}
