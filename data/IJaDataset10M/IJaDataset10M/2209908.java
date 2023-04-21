package org.wings;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.externalizer.ExternalizeManager;
import org.wings.io.Device;

/**
 * An invisible frame, that executes a javascript function <code>onload</code>,
 * that reloads all dirty frames.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 * @version $Revision: 1759 $
 */
public class ReloadManagerFrame extends SFrame {

    private static final Log logger = LogFactory.getLog("org.wings");

    public ReloadManagerFrame() {
    }

    public final SContainer getContentPane() {
        return null;
    }

    /**
     * This frame stays invisible
     */
    public SComponent addComponent(SComponent c, Object constraint, int index) {
        throw new IllegalArgumentException("Adding Components is not allowed");
    }

    /**
     * This frame stays invisible
     * @deprecated use {@link #remove(SComponent)} instead for swing conformity
     */
    public void removeComponent(SComponent c) {
        throw new IllegalArgumentException("Does not have Components");
    }

    /**
     * Sets the parent FrameSet container.
     *
     * @param p the container
     */
    public void setParent(SContainer p) {
        if (!(p == null || p instanceof SFrameSet)) throw new IllegalArgumentException("The ReloadManagerFrame can only be added to SFrameSets.");
        parent = p;
    }

    /**
     * There is no parent frame.
     *
     * @param f the frame
     */
    protected void setParentFrame(SFrame f) {
    }

    /**
     * There is no parent frame.
     *
     * @return
     */
    public SFrame getParentFrame() {
        return null;
    }

    /**
     * No LayoutManager allowed.
     */
    public void setLayout(SLayoutManager l) {
        throw new IllegalArgumentException("No LayoutManager allowed");
    }

    private Set dirtyResources;

    public void setDirtyResources(Set dirtyResources) {
        this.dirtyResources = dirtyResources;
    }

    /**
     * Generate a minimal document with a javascript function, that reloads
     * all dirty frames. The list of dirty frames is obtained from the ReloadManager.
     * After the code has been generated, the dirty components list is cleared.
     *** create a PLAF for this ***
     */
    public void write(Device d) throws IOException {
        ExternalizeManager externalizer = getSession().getExternalizeManager();
        d.print("<head><title>ReloadManager</title>\n");
        d.print("<script language=\"javascript\">\n");
        d.print("function reload() {\n");
        if (dirtyResources != null) {
            boolean all = false;
            DynamicResource toplevel = null;
            {
                Iterator it = dirtyResources.iterator();
                while (it.hasNext()) {
                    DynamicResource resource = (DynamicResource) it.next();
                    if (!(resource.getFrame() instanceof ReloadManagerFrame) && resource.getFrame().getParent() == null) {
                        toplevel = resource;
                        all = true;
                    }
                }
            }
            if (all) {
                d.print("parent.location.href='");
                d.print(toplevel.getURL());
                d.print("';\n");
                if (logger.isTraceEnabled()) logger.debug("parent.location.href='" + toplevel.getURL() + "';\n");
                Iterator it = dirtyResources.iterator();
                while (it.hasNext()) {
                    DynamicResource resource = (DynamicResource) it.next();
                    resource.invalidate();
                }
            } else {
                Iterator it = dirtyResources.iterator();
                while (it.hasNext()) {
                    DynamicResource resource = (DynamicResource) it.next();
                    resource.invalidate();
                    d.print("parent.frame");
                    d.print(resource.getFrame().getComponentId());
                    d.print(".location.href='");
                    d.print(resource.getURL());
                    d.print("';\n");
                    if (logger.isTraceEnabled()) logger.debug("parent.frame" + resource.getFrame().getComponentId() + ".location.href='" + resource.getURL() + "';\n");
                }
            }
        }
        d.print("}\n");
        d.print("</script>\n");
        d.print("</head>\n");
        d.print("<body onload=\"reload()\"></body>");
    }
}
