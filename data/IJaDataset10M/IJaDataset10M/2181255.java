package org.wings.frames;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.SComponent;
import org.wings.SContainer;
import org.wings.SFrame;
import org.wings.SLayoutManager;
import org.wings.io.Device;
import org.wings.resource.DynamicCodeResource;
import org.wings.resource.DynamicResource;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * An invisible frame, that executes a javascript function <code>onload</code>,
 * that reloads all dirty frames.
 *
 * @author <a href="mailto:engels@mercatis.de">Holger Engels</a>
 */
public class SReloadFrame extends SFrame {

    private static final Log logger = LogFactory.getLog("org.wings");

    private Set dirtyResources;

    public SReloadFrame() {
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
     *
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
        if (!(p == null || p instanceof SFrameSet)) throw new IllegalArgumentException("The SReloadFrame can only be added to SFrameSets.");
        super.setParent(p);
    }

    /**
     * There is no parent frame.
     */
    protected void setParentFrame(SFrame f) {
    }

    /**
     * There is no parent frame.
     */
    public SFrame getParentFrame() {
        return null;
    }

    /**
     * No LayoutManager allowed.
     */
    public void setLayout(SLayoutManager l) {
    }

    public void setDirtyResources(Set dirtyResources) {
        this.dirtyResources = dirtyResources;
    }

    /**
     * Generate a minimal document with a javascript function, that reloads
     * all dirty frames. The list of dirty frames is obtained from the ReloadManager.
     * After the code has been generated, the dirty components list is cleared.
     * ** create a PLAF for this ***
     */
    public void write(Device d) throws IOException {
        d.print("<head><title>Reloading frameset</title>\n");
        d.print("<script language=\"javascript\">\n");
        d.print("function reload() {\n");
        if (dirtyResources != null) {
            boolean all = false;
            DynamicResource toplevel = null;
            {
                Iterator it = dirtyResources.iterator();
                while (it.hasNext()) {
                    DynamicResource resource = (DynamicResource) it.next();
                    if (!(resource.getFrame() instanceof SReloadFrame) && resource.getFrame().getParent() == null) {
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
                    if (!(resource instanceof DynamicCodeResource)) continue;
                    d.print("parent.frame");
                    d.print(resource.getFrame().getName());
                    d.print(".location.href='");
                    d.print(resource.getURL());
                    d.print("';\n");
                    if (logger.isTraceEnabled()) logger.debug("parent.frame" + resource.getFrame().getName() + ".location.href='" + resource.getURL() + "';\n");
                }
            }
        }
        d.print("}\n");
        d.print("</script>\n");
        d.print("</head>\n");
        d.print("<body onload=\"reload()\"></body>");
    }
}
