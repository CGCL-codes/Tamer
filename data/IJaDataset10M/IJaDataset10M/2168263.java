package org.apache.jetspeed.portal;

import org.apache.jetspeed.util.MimeType;
import org.apache.turbine.util.RunData;
import org.apache.ecs.ConcreteElement;
import java.io.Serializable;

/**
A portlet is an implementation of a small control (usually rendered in HTML)
that is available to a client application.  Portlets were designed to be
extensible so that 3rd parties implement their own Portlets.

@author <a href="mailto:burton@apache.org">Kevin A. Burton</a>
@version $Id: Portlet.java,v 1.48 2004/03/29 21:38:42 taylor Exp $
*/
public interface Portlet extends Serializable {

    public int PORTLET_NORMAL = 0;

    public int PORTLET_MINIMIZED = 1;

    public int PORTLET_MAXIMIZED = 2;

    /**
    Returns a name for this portlet.  This is used by PSML to identify a Portlet
    within the PortletRegistry
    */
    public String getName();

    /**
    Sets the name on this Portlet.

    @see #getName()
    */
    public void setName(String name);

    /**
    <p>
    Allows a Portlet to define its title.  This can be used by a PortletControl
    for rendering its content.
    </p>

    <p>
    In order to define a default title you should not override this but should
    call setTitle() within your init() method
    </p>

    <p>
    This should return null if not specified.
    </p>
    */
    public String getTitle();

    /**
     * Get a title for this instance of the portlet.  This method is called
     * from the context variable portlet_instance and from PortletInstance
     *
     * If you wish to append to the title, then you code should look like
     *    getTitle( String instanceTitle)
     *    {
     *      return super.getTitle( instanceTitle) + " - Appened title text";
     *    }
     *
     * @param instanceTitle Title from PSML
     */
    public String getTitle(String instanceTitle);

    /**
    Set the title for this Portlet
    */
    public void setTitle(String title);

    /**
    <p>
    Returns a description of this portlet.  This should describe what the
    capabilities of the portlet and how it can help the user.
    </p>

    <p>
    In order to define a default title you should not override (in the
    AbstractPortlet implementation) this but should call setDescription()
    within your init() method
    </p>

    <p>
    This should return null if not specified.
    </p>
    */
    public String getDescription();

    /**
     * Get a Description for this instance of the portlet.  This method is called
     * from the context variable portlet_instance and from PortletInstance
     *
     * If you wish to append to the Description, then you code should look like
     *    getDescription( String instanceTitle)
     *    {
     *      return super.getDescription( instanceDescription) + " - Appened Description text";
     *    }
     *
     * @param instanceDescription Description from PSML
     */
    public String getDescription(String instanceDescription);

    /**
    Set the description for this Portlet
    */
    public void setDescription(String description);

    /**
     * Getter for property image. 
     * @return Name of portlet image, icon.  The name is expected to be in the form of a URL.
     */
    public String getImage(String instanceImage);

    /**
     * Setter for property image. 
     */
    public void setImage(String instanceImage);

    /**
    Returns an HTML representation of this portlet.  Usually a Portlet would
    initialized itself within init() and then when getContent is called it
    would return its presentation.
    */
    public ConcreteElement getContent(RunData rundata);

    /**
    All initialization should be performed here.  If your Portlet wants to
    do any work it should be done here.  You are not guaranteed that any
    particular order of method call will happen just that init() will happen
    first. Therefore if you have to calculate things like a title, a
    description, etc it should happen here.
    */
    public void init() throws PortletException;

    /**
    Set's the configuration of this servlet.
    */
    public void setPortletConfig(PortletConfig pc);

    /**
    Get the config of this servlet.
    */
    public PortletConfig getPortletConfig();

    /**
    <p>Return true if this portlet is allowed to be edited in the rundata's context .</p>

    <p>Note:  PortletControl implementations should pay attention to this so
    that they don't allow this option if it returns false.</p>
    */
    public boolean getAllowEdit(RunData rundata);

    /**
    <p>Return true if this portlet is allowed to be viewed in the rundata's context .</p>

    <p>Note:  PortletControl implementations should pay attention to this so
    that they don't allow this option if it returns false.</p>
    */
    public boolean getAllowView(RunData rundata);

    /**
    <p>Return true if this portlets is allowed to be maximized.</p>

    <p>Note:  PortletControl implementations should pay attention to this so
    that they don't allow this option if it returns false.</p>
    */
    public boolean getAllowMaximize(RunData rundata);

    /**
    Get the creation time for this Portlet
    */
    public long getCreationTime();

    /**
     * Returns TRUE if the title bar in should be displayed. The title bar includes
     * the portlet title and action buttons.  This
     * 
     * @param rundata The RunData object for the current request
     */
    public boolean isShowTitleBar(RunData rundata);

    /**
    Set the creation time for this Portlet
    */
    public void setCreationTime(long creationTime);

    /**
    Returns true portlet is able to output content for given mimetype
    */
    public boolean supportsType(MimeType mimeType);

    /**
     * Retrieve a portlet attribute from persistent storage
     *
     * @param attrName The attribute to retrieve
     * @param attrDefValue The value if the attr doesn't exists
     * @param rundata The RunData object for the current request
     * @return The attribute value
     */
    public String getAttribute(String attrName, String attrDefValue, RunData rundata);

    /**
     * Stores a portlet attribute in persistent storage
     *
     * @param attrName The attribute to retrieve
     * @paarm attrValue The value to store
     * @param rundata The RunData object for the current request
     */
    public void setAttribute(String attrName, String attrValue, RunData rundata);

    /**
     * Gets the portlet instance associated with this portlet.
     *
     * @return PortletInstance
     */
    public PortletInstance getInstance(RunData rundata);

    /**
    Retrieve a unique portlet id 
    */
    public String getID();

    public void setID(String id);

    /**
    * @return true if the portlet does its own customization
    */
    public boolean providesCustomization();
}
