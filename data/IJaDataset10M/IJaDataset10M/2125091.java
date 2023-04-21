package net.customware.confluence.plugin.scaffolding;

import java.util.Stack;
import com.atlassian.confluence.core.SpaceContentEntityObject;
import com.atlassian.confluence.core.ContentEntityObject;
import com.atlassian.confluence.pages.PageManager;
import com.atlassian.confluence.pages.templates.PageTemplate;
import com.atlassian.confluence.renderer.PageContext;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.spaces.SpaceManager;
import com.atlassian.renderer.v2.RenderMode;
import com.atlassian.renderer.v2.SubRenderer;
import com.atlassian.renderer.v2.macro.MacroException;
import org.randombits.confluence.support.ConfluenceMacro;
import org.randombits.confluence.support.ContextAssistant;
import org.randombits.confluence.support.MacroInfo;
import org.randombits.storage.IndexedStorage;
import org.randombits.storage.Storage;

/**
 * The 'live-template' macro basically imports the current version of whatever
 * template has been specified. This is different to creating a page with a
 * template, because it updates when the template changes.
 * 
 * @author David Peterson
 */
public class LiveTemplateMacro extends ConfluenceMacro {

    private static final String TEMPLATE_PARAM = "template";

    private static final String CONTEXT_ATTR = "net.customware.confluence.scaffold.live-template";

    private SubRenderer subRenderer;

    @Override
    protected String execute(MacroInfo info) throws MacroException {
        IndexedStorage params = info.getMacroParams();
        String template = params.getString(TEMPLATE_PARAM, params.getString(0, null));
        PageContext context = info.getPageContext();
        Space space = null;
        ContentEntityObject content = context.getEntity();
        if (content instanceof SpaceContentEntityObject) {
            SpaceContentEntityObject sceo = (SpaceContentEntityObject) content;
            if (!sceo.isLatestVersion()) sceo = (SpaceContentEntityObject) content.getLatestVersion();
            space = sceo.getSpace();
        }
        PageTemplate pageTemplate = ContextAssistant.getInstance().getPageTemplate(template, space);
        if (pageTemplate != null) {
            Storage reqAttrs = info.getRequestAttributes();
            Stack<String> stack = null;
            if (reqAttrs != null) {
                stack = (Stack<String>) reqAttrs.getObject(CONTEXT_ATTR, null);
                if (stack == null) {
                    stack = new Stack<String>();
                    reqAttrs.setObject(CONTEXT_ATTR, stack);
                }
                stack.push(pageTemplate.isGlobalPageTemplate() ? "global" : "space");
            }
            try {
                String body = pageTemplate.getContent();
                return subRenderer.render(body, context, RenderMode.ALL);
            } finally {
                if (reqAttrs != null) {
                    stack.pop();
                    if (stack.size() == 0) reqAttrs.setObject(CONTEXT_ATTR, null);
                }
            }
        } else {
            throw new MacroException("No template named '" + template + "' is accessible.");
        }
    }

    /**
     * Supplies the name of the macro.
     * 
     * @return the name of the macro.
     */
    public String getName() {
        return "include-template";
    }

    /**
     * This provides information about Pages.
     * 
     * @param pageManager
     *            The PageManager instance.
     */
    public void setPageManager(PageManager pageManager) {
    }

    /**
     * This provides information about Spaces.
     * 
     * @param spaceManager
     *            The SpaceManager instance.
     */
    public void setSpaceManager(SpaceManager spaceManager) {
    }

    public boolean isInline() {
        return false;
    }

    public boolean hasBody() {
        return false;
    }

    public RenderMode getBodyRenderMode() {
        return RenderMode.NO_RENDER;
    }

    /**
     * @param subRenderer
     *            The subRenderer to set.
     */
    public void setSubRenderer(SubRenderer subRenderer) {
        this.subRenderer = subRenderer;
    }
}
