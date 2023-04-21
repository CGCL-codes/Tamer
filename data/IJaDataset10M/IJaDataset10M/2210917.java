package org.rascalli.mbe;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Abstract convenience implementation of the Tool interface.
 * </p>
 * 
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * 
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * </p>
 * 
 * <p>
 * <b>last modified:</b><br/> $Author: christian $<br/> $Date: 2007-12-11
 * 17:07:13 +0100 (Di, 11 Dez 2007) $<br/> $Revision: 2447 $
 * </p>
 * 
 * @author Christian Schollum
 */
public abstract class AbstractTool implements Tool, Effector, Sensor {

    protected Log log = LogFactory.getLog(getClass());

    private final String id;

    private Mind mind = null;

    protected AbstractTool(String id) {
        this.id = id;
    }

    /**
     * <p>
     * Set this tool's reference to the mind.
     * </p>
     * 
     * @throws NullPointerException
     *             if {@code mind} is {@code null}.
     */
    public final void setMind(Mind mind) {
        if (mind == null) {
            throw new NullPointerException("mind is null");
        }
        this.mind = mind;
    }

    /**
     * <p>
     * Send data to the mind.
     * </p>
     * 
     * @param data
     *            the RDF data to be sent to the mind.
     * @throws IllegalStateException
     *             if the mind reference has not previously been set via
     *             {@link #setMind(Mind)}.
     */
    protected final void sendToMind(RdfData data) {
        if (mind == null) {
            throw new IllegalStateException("reference to mind not set");
        }
        mind.processInput(data);
    }

    /**
     * <p>
     * A default implementation of this method that throws an
     * UnsupportedOperationException.
     * </p>
     * 
     * <p>
     * A sub-class that does not override this method is a pure sensor tool (it
     * provides only input to the mind, but cannot execute an action).
     * </p>
     */
    public RdfData executeAction(RdfData data) throws Exception {
        throw new UnsupportedOperationException("no action implemented by this tool: toolId=" + getId());
    }

    public final String getId() {
        return id;
    }

    /**
     * Returns a string representation of this tool.
     * 
     * @return a string representation of this tool.
     */
    @Override
    public String toString() {
        return "Tool (" + getId() + ")";
    }

    /**
     * @param string
     */
    protected RdfData createError(final String errorText) {
        try {
            return new ToolError(ToolID.forString(getId()), errorText);
        } catch (RdfException e) {
            if (log.isErrorEnabled()) {
                log.error("cannot create tool error", e);
            }
        }
        return null;
    }
}
