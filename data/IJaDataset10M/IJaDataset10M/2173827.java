package demo.gwt.portlet.client;

import com.google.gwt.user.client.Element;

/**
 * Each GWT Portlet should implements this interface
 * 
 * Portlet can be exchange as Javascript like: <code>
 * 	{
 * 		getTitle:	function(){ ... }
 * 		getMode:	function(){ ... }
 * 		setMode:	function(){ ... }
 * 		renderFrame: function(){ ... }
 * 		renderBody:	function(){ ... }
 * 	}
 * </code>
 */
public interface Portlet {

    String MODE_NORMAL = "normal";

    String MODE_MAXIMUM = "maximum";

    String MODE_MINIMUM = "minimum";

    PortletContainer getContainer();

    String getTitle();

    String getMode();

    void setMode(String mode);

    /**
	 * enable a portlet to custom frame render, but generally, a portlet only
	 * render its body parts and delegate the frame to PortletFrame
	 */
    void renderFrame(Element container);

    /**
	 * must be implemented by portlet
	 */
    void renderBody(Element container);
}
