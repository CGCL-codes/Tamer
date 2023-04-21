package com.nexirius.framework.swing;

import com.nexirius.util.assertion.Assert;
import com.nexirius.util.resource.ClientResource;
import com.nexirius.util.resource.ResourceChangeEvent;
import com.nexirius.util.resource.ResourceChangeListener;
import javax.swing.*;

/**
 * A JButton which responds to changes in client resources.
 * Changes can include text, icon, mnemonic etc.
 *
 * @author Marcel Baumann
 *         Date        Author           Changes/Enhancements
 *         1999.01.16  MB              Created
 */
public class ClientResourceJButton extends JButton implements ResourceChangeListener {

    private String resourceKey;

    /**
     * Construct a ClientResourceJButton
     * Need a null constructor so we can use this class in a GUI builder
     */
    public ClientResourceJButton() {
        super();
    }

    /**
     * Construct a ClientResourceJButton
     *
     * @param resource    The resource to be used to establish basic attributes
     *                    of the JButton
     * @param resourceKey The key to be used in establishing resources
     */
    public ClientResourceJButton(ClientResource resource, String resourceKey) {
        super(resourceKey);
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
    protected void update(ClientResource clientResource) {
        if (resourceKey == null) {
            return;
        }
        final String text = clientResource.getButtonText(resourceKey);
        if (text != null) {
            setText(text);
        } else {
            setText(resourceKey);
        }
        final int mnemo = clientResource.getMnemonic(resourceKey, ".buttonText");
        if (text != null && mnemo != -1) {
            setMnemonic(text.charAt(mnemo));
        }
        final Icon icon = clientResource.getIcon(resourceKey);
        if (icon != null) {
            setIcon(icon);
        }
        final String tip = clientResource.getToolTipText(resourceKey);
        if (tip != null) {
            setToolTipText(tip);
        }
        invalidate();
        validate();
        repaint();
    }
}
