package org.pustefixframework.editor.webui.handlers;

import org.pustefixframework.container.annotations.Inject;
import org.pustefixframework.editor.webui.resources.CommonIncludesResource;
import org.pustefixframework.editor.webui.resources.IncludesResource;
import de.schlund.pfixcore.workflow.Context;

/**
 * Handles include part branching
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public class BranchIncludePartHandler extends CommonBranchIncludePartHandler {

    private IncludesResource includesResource;

    @Inject
    public void setIncludesResource(IncludesResource includesResource) {
        this.includesResource = includesResource;
    }

    @Override
    protected CommonIncludesResource getResource(Context context) {
        return includesResource;
    }
}
