package org.jpublish.component;

import org.jpublish.Repository;
import org.jpublish.JPublishContext;

/** JPublish component which renders content.

    @author Anthony Eden
    @since 2.0
*/
public class ContentComponent extends AbstractComponent {

    private static final String NAME = "Content";

    private static final String DESCRIPTION = "Display content.";

    protected Repository repository;

    /** Construct a new ContentComponent. */
    public ContentComponent() {
        this.name = NAME;
        this.description = DESCRIPTION;
    }

    /** Render the component for the specified path and return the result.
    
        @param path The request path
        @param context The context
        @return The rendered component
        @throws Exception
    */
    public String render(String path, JPublishContext context) throws Exception {
        return renderView(getViewRepository().get(path), path, context);
    }

    /**
     * Process the text parameter and Render the component
     *
     * @param text    the string to process
     * @param context The context
     * @return The rendered component
     * @throws Exception any Exception
     */
    public String renderText(String text, JPublishContext context) throws Exception {
        return null;
    }

    /**
     * Process the View from at the given path and Render the component
     *
     * @param path    the View to process
     * @param context The context
     * @return The rendered component
     * @throws Exception any Exception
     */
    public String renderPath(String path, JPublishContext context) throws Exception {
        return null;
    }
}
