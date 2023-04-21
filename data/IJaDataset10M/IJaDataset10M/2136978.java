package it.could.confluence.autoexport.engine;

import it.could.confluence.autoexport.ConfigurationManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import com.atlassian.confluence.pages.AbstractPage;
import com.atlassian.confluence.pages.BlogPost;
import com.atlassian.confluence.pages.Page;
import com.atlassian.renderer.WikiStyleRenderer;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.util.GeneralUtil;
import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginAccessor;
import com.atlassian.renderer.RenderContext;
import com.opensymphony.util.TextUtils;

/**
 * <p>An extremely simple utilty class that can be used by templates for
 * dumping different strings.</p>
 */
public final class ExportUtils {

    private static final Logger LOGGER = Logger.getLogger(ExportBeautifier.class);

    /** <p>The {@link ConfigurationManager} accessing settings.</p> */
    private final ConfigurationManager configurationManager;

    /** <p>The {@link WikiStyleRenderer} rendering content.</p> */
    private final WikiStyleRenderer wikiStyleRenderer;

    /** <p>The {@link PluginAccessor} gathering plugin details.</p> */
    private final PluginAccessor pluginAccessor;

    /** <p>Constructor to give instances to Velocity.</p> */
    public ExportUtils(ConfigurationManager config, WikiStyleRenderer renderer, PluginAccessor accessor) {
        this.configurationManager = config;
        this.wikiStyleRenderer = renderer;
        this.pluginAccessor = accessor;
    }

    /**
     * <p>Render a simple page and return its HTML representation as a
     * {@link String}.</p>
     */
    public String render(AbstractPage page) {
        if (page == null) return null;
        final RenderContext context = page.toPageContext();
        final String content = page.getContent();
        return this.wikiStyleRenderer.convertWikiToXHtml(context, content);
    }

    /**
     * <p>Return a simple link to the specified {@link AbstractPage}.</p>
     */
    public String link(AbstractPage page) {
        final String title = TextUtils.htmlEncode(page.getTitle());
        return new StringBuffer("<a href=\"").append(this.configurationManager.getConfluenceUrl()).append(page.getUrlPath()).append("\" title=\"").append(title).append("\">").append(title).append("</a>").toString();
    }

    /**
     * <p>Return a simple link for the specified {@link Space}.</p>
     */
    public String link(Space space) {
        final Page home = space.getHomePage();
        final String name = TextUtils.htmlEncode(space.getName());
        if (home == null) return name;
        return new StringBuffer("<a href=\"").append(this.configurationManager.getConfluenceUrl()).append(home.getUrlPath()).append("\" title=\"").append(name).append("\">").append(name).append("</a>").toString();
    }

    /**
     * <p>Return all the breadcrumbs for the specified {@link Page}.</p>
     * 
     * <p>The default separator used is "<code>&gt;</code>" (greater than).</p>
     * 
     * @see #breadcrumbs(Page, String)
     */
    public String breadcrumbs(Page page) {
        return breadcrumbs(page, ">");
    }

    /**
     * <p>Return all the breadcrumbs for the specified {@link Page} separated
     * by the specified {@link String}.</p>
     */
    public String breadcrumbs(Page page, String separator) {
        try {
            final String s = "&nbsp;" + TextUtils.htmlEncode(separator) + "&nbsp;";
            final StringBuffer buffer = new StringBuffer();
            final Page parent = page.getParent();
            if (parent != null) buffer.append(breadcrumbs(parent, separator)); else buffer.append(link(page.getSpace()));
            return buffer.append(s).append(link(page)).toString();
        } catch (Throwable throwable) {
            LOGGER.error("Error processing breadcrumbs for post", throwable);
            return "";
        }
    }

    /**
     * <p>Return all the breadcrumbs for the specified {@link BlogPost}.</p>
     * 
     * <p>The default separator used is "<code>&gt;</code>" (greater than).</p>
     * 
     * @see #breadcrumbs(BlogPost, String)
     */
    public String breadcrumbs(BlogPost post) {
        return breadcrumbs(post, ">");
    }

    /**
     * <p>Return all the breadcrumbs for the specified {@link BlogPost}
     * separated by the specified {@link String}.</p>
     */
    public String breadcrumbs(BlogPost post, String separator) {
        try {
            final String s = "&nbsp;" + TextUtils.htmlEncode(separator) + "&nbsp;";
            final Date date = post.getCreationDate();
            return new StringBuffer(link(post.getSpace())).append(s).append(new SimpleDateFormat("yyyy").format(date)).append(s).append(new SimpleDateFormat("MM").format(date)).append(s).append(new SimpleDateFormat("dd").format(date)).append(s).append(link(post)).toString();
        } catch (Throwable throwable) {
            LOGGER.error("Error processing breadcrumbs for post", throwable);
            return "";
        }
    }

    /**
     * <p>Return a link to Atlassian Confluence's home page and its
     * version details.</p>
     */
    public String getConfluenceInfo() {
        return new StringBuffer().append("<a href=\"http://www.atlassian.com/confluence/\">").append("Atlassian Confluence</a> (Version: ").append(GeneralUtil.getVersionNumber()).append(" Build: ").append(GeneralUtil.getBuildNumber()).append(" ").append(GeneralUtil.getBuildDateString()).append(")").toString();
    }

    /**
     * <p>Return a link to the AutoExport plugin's home page and its
     * version details.</p>
     */
    public String getAutoexportInfo() {
        String pluginName = "AutoExport Plugin";
        String pluginVersion = "(unknown)";
        String pluginUrl = "http://could.it/autoexport/";
        final Plugin plugin = this.pluginAccessor.getPlugin("confluence.extra.autoexport");
        if (plugin != null) {
            pluginName = plugin.getName();
            pluginVersion = plugin.getPluginInformation().getVersion();
            pluginUrl = plugin.getPluginInformation().getVendorUrl();
        }
        return new StringBuffer("<a href=\"").append(pluginUrl).append("\">").append(pluginName).append("</a> (Version: ").append(pluginVersion).append(")").toString();
    }
}
