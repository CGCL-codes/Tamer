package org.springframework.webflow.registry;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.webflow.Flow;
import org.springframework.webflow.builder.FlowAssembler;
import org.springframework.webflow.builder.FlowBuilder;
import org.springframework.webflow.builder.ResourceHolder;

/**
 * A flow definition holder that can detect changes on an underlying flow
 * definition resource and refresh that resource automatically.
 * 
 * @author Keith Donald
 */
public class RefreshableFlowHolder implements FlowHolder {

    /**
	 * The flow definition assembled by this assembler.
	 */
    private Flow flow;

    /**
	 * The flow assembler.
	 */
    private FlowAssembler assembler;

    /**
	 * A last modified date for the backing flow resource, used to support
	 * automatic reassembly on resource change.
	 */
    private long lastModified;

    /**
	 * A flag indicating whether or not this assembler is in the middle of the
	 * assembly process.
	 */
    private boolean assembling;

    /**
	 * Creates a new refreshable flow holder that uses the configured assembler (GOF director) to
	 * drive flow assembly, on initial use and on any resource change.
	 * @param assembler the flow assembler (director)
	 */
    public RefreshableFlowHolder(FlowAssembler assembler) {
        this.assembler = assembler;
    }

    public String getId() {
        return assembler.getFlowParameters().getId();
    }

    public Flow getFlow() {
        if (assembling) {
            return getFlowBuilder().getResult();
        }
        if (!isAssembled()) {
            lastModified = calculateLastModified();
            assembleFlow();
        } else {
            refreshIfChanged();
        }
        return flow;
    }

    /**
	 * Returns the flow builder that actually builds the Flow definition.
	 */
    public FlowBuilder getFlowBuilder() {
        return assembler.getFlowBuilder();
    }

    /**
	 * Returns a flag indicating if this holder has performed and completed
	 * Flow assembly.
	 */
    protected boolean isAssembled() {
        return flow != null;
    }

    /**
	 * Returns a flag indicating if this holder is performing assembly.
	 */
    protected boolean isAssembling() {
        return assembling;
    }

    /**
	 * Returns the last modifed date of the backed builder resource.
	 * @return the last modified date
	 */
    protected long getLastModified() {
        return lastModified;
    }

    /**
	 * Assemble the held flow definition, delegating to the configured
	 * FlowAssembler (director).
	 */
    protected void assembleFlow() {
        try {
            assembling = true;
            assembler.assembleFlow();
            flow = getFlowBuilder().getResult();
        } finally {
            assembling = false;
        }
    }

    public void refresh() {
        assembleFlow();
    }

    /**
	 * Reassemble the flow if its underlying resource has changed.
	 */
    protected void refreshIfChanged() {
        if (this.lastModified == -1) {
            return;
        }
        if (this.lastModified < calculateLastModified()) {
            refresh();
        }
    }

    /**
	 * Helper that retrieves the last modified date by querying the backing flow
	 * resource.
	 * @return the last modified date, or -1 if it could not be retrieved
	 */
    protected long calculateLastModified() {
        if (getFlowBuilder() instanceof ResourceHolder) {
            Resource resource = ((ResourceHolder) getFlowBuilder()).getResource();
            try {
                return resource.getFile().lastModified();
            } catch (IOException e) {
            }
        }
        return -1;
    }
}
