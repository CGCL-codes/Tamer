package edu.bonn.cs.wmp.views;

import edu.bonn.cs.wmp.awarenesswidgets.WMPAwarenessWidget;
import edu.bonn.cs.wmp.awarenesswidgets.ContentChange;

/**
 * This interface has to be implemented by all WMPViews.
 * 
 * The implementing class needs to have access to some kind of data structure to
 * store all {@link WMPAwarenessWidget}s it manages. Normally an ArrayList is more
 * than sufficient.
 * 
 * @author Sven Bendel
 */
public interface WMPView {

    /**
	 * Adds the given widget to the list of widgets being informed by this
	 * view about {@link ContentChange}s.
	 * @param w {@link WMPAwarenessWidget}
	 */
    public void addWidget(WMPAwarenessWidget w);

    /**
	 * Adds the given widget to the list of widgets being informed by this
	 * view about {@link ContentChange}s.
	 * @param w {@link WMPAwarenessWidget}
	 */
    public boolean removeWidget(WMPAwarenessWidget w);

    /**
	 * This method notifies all Awareness Widgets who have registered
	 * themselves at this View of new {@link ContentChange}s.
	 * @param c ContentChange to be propagated to the Awareness Widgets
	 */
    public void notifyExternalWMPWidgetsOfContentChange(ContentChange c);

    /**
	 * This method is the link to the other collaborators. The method has to
	 * take care of propagating the {@link ContentChange} to the Awareness widgets
	 * of all session takers.
	 * @param c ContentChange to be propagated to the other session takers
	 */
    public void notifyCollaboratorsOfContentChange(ContentChange c);

    /**
	 * @return the WMP ID of this view 
	 */
    public String getWMPName();
}
