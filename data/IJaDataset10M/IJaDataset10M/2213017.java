package org.springframework.web.servlet.view.velocity;

import java.io.StringWriter;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.context.ApplicationContextException;

/**
 * VelocityLayoutView emulates the functionality offered by Velocity's
 * VelocityLayoutServlet to ease page composition from different templates.
 *
 * <p>The <code>url</code> property should be set to the content template
 * for the view, and the layout template location should be specified as
 * <code>layoutUrl</code> property. A view can override the configured
 * layout template location by setting the appropriate key (the default
 * is "layout") in the content template.
 *
 * <p>When the view is rendered, the VelocityContext is first merged with
 * the content template (specified by the <code>url</code> property) and
 * then merged with the layout template to produce the final output.
 *
 * <p>The layout template can include the screen content through a
 * VelocityContext variable (the default is "screen_content").
 * At runtime, this variable will contain the rendered content template.
 *
 * @author Darren Davison
 * @author Juergen Hoeller
 * @since 1.2
 * @see #setLayoutUrl
 * @see #setLayoutKey
 * @see #setScreenContentKey
 */
public class VelocityLayoutView extends VelocityToolboxView {

    public static final String DEFAULT_LAYOUT_URL = "layout.vm";

    public static final String DEFAULT_LAYOUT_KEY = "layout";

    public static final String DEFAULT_SCREEN_CONTENT_KEY = "screen_content";

    private String layoutUrl = DEFAULT_LAYOUT_URL;

    private String layoutKey = DEFAULT_LAYOUT_KEY;

    private String screenContentKey = DEFAULT_SCREEN_CONTENT_KEY;

    /**
	 * Set the layout template to use. Default is "layout.vm".
	 * @param layoutUrl the template location (relative to the template
	 * root directory)
	 */
    public void setLayoutUrl(String layoutUrl) {
        this.layoutUrl = layoutUrl;
    }

    /**
	 * Set the context key used to specify an alternate layout to be used instead
	 * of the default layout. Screen content templates can override the layout
	 * template that they wish to be wrapped with by setting this value in the
	 * template, for example:<br>
	 * <code>#set( $layout = "MyLayout.vm" )</code>
	 * <p>The default key is "layout", as illustrated above.
	 * @param layoutKey the name of the key you wish to use in your
	 * screen content templates to override the layout template
	 */
    public void setLayoutKey(String layoutKey) {
        this.layoutKey = layoutKey;
    }

    /**
	 * Set the name of the context key that will hold the content of
	 * the screen within the layout template. This key must be present
	 * in the layout template for the current screen to be rendered.
	 * <p>Default is "screen_content": accessed in VTL as
	 * <code>$screen_content</code>.
	 * @param screenContentKey the name of the screen content key to use
	 */
    public void setScreenContentKey(String screenContentKey) {
        this.screenContentKey = screenContentKey;
    }

    /**
	 * Overrides <code>VelocityView.checkTemplate()</code> to additionally check
	 * that both the layout template and the screen content template can be loaded.
	 * Note that during rendering of the screen content, the layout template
	 * can be changed which may invalidate any early checking done here.
	 */
    protected void checkTemplate() throws ApplicationContextException {
        super.checkTemplate();
        try {
            getTemplate(this.layoutUrl);
        } catch (ResourceNotFoundException ex) {
            throw new ApplicationContextException("Cannot find Velocity template for URL [" + this.layoutUrl + "]: Did you specify the correct resource loader path?", ex);
        } catch (Exception ex) {
            throw new ApplicationContextException("Could not load Velocity template for URL [" + this.layoutUrl + "]", ex);
        }
    }

    /**
	 * Overrides the normal rendering process in order to pre-process the Context,
	 * merging it with the screen template into a single value (identified by the
	 * value of screenContentKey). The layout template is then merged with the
	 * modified Context in the super class.
	 */
    protected void doRender(Context context, HttpServletResponse response) throws Exception {
        renderScreenContent(context);
        String layoutUrlToUse = (String) context.get(this.layoutKey);
        if (layoutUrlToUse != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Screen content template has requested layout [" + layoutUrlToUse + "]");
            }
        } else {
            layoutUrlToUse = this.layoutUrl;
        }
        mergeTemplate(getTemplate(layoutUrlToUse), context, response);
    }

    /**
	 * The resulting context contains any mappings from render, plus screen content.
	 */
    private void renderScreenContent(Context velocityContext) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Rendering screen content template [" + getUrl() + "]");
        }
        StringWriter sw = new StringWriter();
        Template screenContentTemplate = getTemplate(getUrl());
        screenContentTemplate.merge(velocityContext, sw);
        velocityContext.put(this.screenContentKey, sw.toString());
    }
}
