package org.pustefixframework.editor.webui.handlers;

import org.pustefixframework.container.annotations.Inject;
import org.pustefixframework.editor.webui.resources.CommonIncludesResource;
import org.pustefixframework.editor.webui.resources.IncludesResource;
import de.schlund.pfixcore.workflow.Context;

/**
 * Handles include part restore from backup
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public class RestoreIncludePartHandler extends CommonRestoreIncludePartHandler {

    private IncludesResource includesResource;

    @Override
    protected CommonIncludesResource getResource(Context context) {
        return includesResource;
    }

    @Inject
    public void setIncludesResource(IncludesResource includesResource) {
        this.includesResource = includesResource;
    }
}
