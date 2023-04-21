package de.schlund.pfixcore.editor2.frontend.util;

import de.schlund.pfixcore.editor2.frontend.resources.DynIncludesResource;
import de.schlund.pfixcore.editor2.frontend.resources.ImagesResource;
import de.schlund.pfixcore.editor2.frontend.resources.IncludesResource;
import de.schlund.pfixcore.editor2.frontend.resources.PagesResource;
import de.schlund.pfixcore.editor2.frontend.resources.ProjectsResource;
import de.schlund.pfixcore.editor2.frontend.resources.SessionResource;
import de.schlund.pfixcore.editor2.frontend.resources.TargetsResource;
import de.schlund.pfixcore.editor2.frontend.resources.UsersResource;
import de.schlund.pfixcore.lucefix.ContextSearch;
import de.schlund.pfixcore.workflow.Context;

/**
 * Provides helper methods to get a ContextResource
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public abstract class EditorResourceLocator {

    /**
     * Returns SessionResource implementation
     * 
     * @param context
     *            Context to use
     * @return SessionResource for context
     */
    public static final SessionResource getSessionResource(Context context) {
        return (SessionResource) context.getContextResourceManager().getResource(SessionResource.class.getName());
    }

    /**
     * Returns ProjectsResource Implementation
     * 
     * @param context
     *            Context to use
     * @return ProjectsResource for context
     */
    public static final ProjectsResource getProjectsResource(Context context) {
        return (ProjectsResource) context.getContextResourceManager().getResource(ProjectsResource.class.getName());
    }

    /**
     * Returns PagesResource Implementation
     * 
     * @param context
     *            Context to use
     * @return PagesResource for context
     */
    public static final PagesResource getPagesResource(Context context) {
        return (PagesResource) context.getContextResourceManager().getResource(PagesResource.class.getName());
    }

    /**
     * Returns TargetsResource Implementation
     * 
     * @param context
     *            Context to use
     * @return TargetsResource for context
     */
    public static final TargetsResource getTargetsResource(Context context) {
        return (TargetsResource) context.getContextResourceManager().getResource(TargetsResource.class.getName());
    }

    /**
     * Returns ImagesResource Implementation
     * 
     * @param context
     *            Context to use
     * @return ImagesResource for context
     */
    public static final ImagesResource getImagesResource(Context context) {
        return (ImagesResource) context.getContextResourceManager().getResource(ImagesResource.class.getName());
    }

    /**
     * Returns IncludesResource Implementation
     * 
     * @param context
     *            Context to use
     * @return IncludesResource for context
     */
    public static final IncludesResource getIncludesResource(Context context) {
        return (IncludesResource) context.getContextResourceManager().getResource(IncludesResource.class.getName());
    }

    /**
     * Returns DynIncludesResource Implementation
     * 
     * @param context
     *            Context to use
     * @return DynIncludesResource for context
     */
    public static final DynIncludesResource getDynIncludesResource(Context context) {
        return (DynIncludesResource) context.getContextResourceManager().getResource(DynIncludesResource.class.getName());
    }

    /**
     * Returns UsersResource Implementation
     * 
     * @param context
     *            Context to use
     * @return UsersResource for context
     */
    public static final UsersResource getUsersResource(Context context) {
        return (UsersResource) context.getContextResourceManager().getResource(UsersResource.class.getName());
    }

    public static final ContextSearch getContextSearch(Context context) {
        return (ContextSearch) context.getContextResourceManager().getResource(ContextSearch.class.getName());
    }
}
