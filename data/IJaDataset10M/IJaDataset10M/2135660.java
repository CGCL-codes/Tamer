package org.pustefixframework.editor.webui.handlers;

import org.pustefixframework.container.annotations.Inject;
import org.pustefixframework.editor.generated.EditorStatusCodes;
import org.pustefixframework.editor.webui.resources.CommonIncludesResource;
import org.pustefixframework.editor.webui.resources.ProjectsResource;
import org.pustefixframework.editor.webui.wrappers.CommonSelectIncludePart;
import de.schlund.pfixcore.generator.IHandler;
import de.schlund.pfixcore.generator.IWrapper;
import de.schlund.pfixcore.workflow.Context;

/**
 * Handles common include part selection
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public abstract class CommonSelectIncludePartHandler implements IHandler {

    private ProjectsResource projectsResource;

    protected abstract CommonIncludesResource getResource(Context context);

    public void handleSubmittedData(Context context, IWrapper wrapper) throws Exception {
        CommonSelectIncludePart input = (CommonSelectIncludePart) wrapper;
        if (!this.getResource(context).selectIncludePart(input.getPath(), input.getPart(), input.getTheme())) {
            input.addSCodePath(EditorStatusCodes.INCLUDES_INCLUDE_UNDEF);
        }
    }

    public void retrieveCurrentStatus(Context context, IWrapper wrapper) throws Exception {
    }

    public boolean prerequisitesMet(Context context) throws Exception {
        return (projectsResource.getSelectedProject() != null);
    }

    public boolean isActive(Context context) throws Exception {
        return true;
    }

    public boolean needsData(Context context) throws Exception {
        return true;
    }

    @Inject
    public void setProjectsResource(ProjectsResource projectsResource) {
        this.projectsResource = projectsResource;
    }
}
