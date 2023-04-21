package ua.orion.tapestry.menu.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import ua.orion.tapestry.menu.lib.IMenuLink;

/**
 *
 * @author Gennadiy Dobrovolsky
 */
public class MenuLink extends AbstractLink {

    /**
     * The exact name of the page class to link to.
     */
    @Parameter(required = false, allowNull = false, defaultPrefix = BindingConstants.PROP)
    private IMenuLink menuLink;

    @Inject
    private PageRenderLinkSource LinkSource;

    void beginRender(MarkupWriter writer) {
        if (isDisabled()) {
            return;
        }
        if (menuLink == null) {
            return;
        }
        Link linkObject;
        Object[] context = menuLink.getContext();
        if (context == null || context.length == 0) {
            linkObject = LinkSource.createPageRenderLink(menuLink.getPage());
        } else {
            linkObject = LinkSource.createPageRenderLinkWithContext(menuLink.getPage(), context);
        }
        if (menuLink.getParameters() != null) {
            for (String pname : menuLink.getParameters().keySet()) {
                linkObject.addParameter(pname, menuLink.getParameter(pname));
            }
        }
        writeLink(writer, linkObject);
    }

    void afterRender(MarkupWriter writer) {
        if (isDisabled()) {
            return;
        }
        if (menuLink == null) {
            return;
        }
        writer.end();
    }
}
