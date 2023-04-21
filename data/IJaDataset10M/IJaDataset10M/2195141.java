package com.nexirius.framework.swing;

import com.nexirius.util.assertion.Assert;
import com.nexirius.util.resource.ClientResource;
import com.nexirius.util.resource.ResourceChangeEvent;
import javax.swing.*;

/**
 * A JComponent which responds to changes in client resources.
 * Changes can include tool tip text.
 * <p/>
 * Property name           Component Property
 * -------------           ------------------
 * resourceKey.tip         tooltiptext
 * <p/>
 * <p/>
 *
 * @author Marcel Baumann
 *         Date        Author           Changes/Enhancements
 *         1999.03.01  MB              Created
 */
public class CFJComponent extends JComponent implements CFJItem {

    private String resourceKey;

    /**
     * Construct a CFJComponent (Client Framework JComponent)
     * Need a null constructor so we can use this class in a GUI builder
     */
    public CFJComponent() {
        super();
    }

    /**
     * Construct a CFJComponent   (Client Framework JComponent)
     *
     * @param resource    The resource to be used to establish basic attributes
     *                    of the JComponent
     * @param resourceKey The key to be used in establishing resources
     */
    public CFJComponent(ClientResource resource, String resourceKey) {
        super();
        Assert.pre(resource != null, "Parameter resource is not null");
        Assert.pre(resourceKey != null, "Parameter resourceKey is not null");
        this.resourceKey = resourceKey;
        update(resource);
        resource.addResourceChangeListener(this);
    }

    /**
     * Get the resource key
     *
     * @return the resource key
     */
    public String getResourceKey() {
        return this.resourceKey;
    }

    /**
     * Set the resource key
     *
     * @param resourceKey the resource key
     */
    public void setResourceKey(String resourceKey) {
        Assert.pre(resourceKey != null, "Parameter resourceKey is not null");
        this.resourceKey = resourceKey;
    }

    /**
     * @see import com.nexirius.util.resource.ResourceChangeListener;
     */
    public void resourceChange(ResourceChangeEvent event) {
        update(event.getClientResource());
    }

    /**
     * Update properties based on client resource
     *
     * @param clientResource the resource to be used to establish properties
     */
    public void update(ClientResource clientResource) {
        if (resourceKey == null) {
            return;
        }
        final String tip = clientResource.getToolTipText(resourceKey);
        if (tip != null) {
            setToolTipText(tip);
        }
    }
}
