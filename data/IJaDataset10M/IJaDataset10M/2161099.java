package org.blojsom.plugin.moderation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.blojsom.blog.Blog;
import org.blojsom.blog.Entry;
import org.blojsom.event.Event;
import org.blojsom.event.EventBroadcaster;
import org.blojsom.event.Listener;
import org.blojsom.plugin.Plugin;
import org.blojsom.plugin.PluginException;
import org.blojsom.plugin.pingback.event.PingbackResponseSubmissionEvent;
import org.blojsom.plugin.pingback.PingbackPlugin;
import org.blojsom.plugin.comment.CommentModerationPlugin;
import org.blojsom.plugin.comment.CommentPlugin;
import org.blojsom.plugin.comment.event.CommentResponseSubmissionEvent;
import org.blojsom.plugin.response.event.ResponseSubmissionEvent;
import org.blojsom.plugin.trackback.TrackbackModerationPlugin;
import org.blojsom.plugin.trackback.TrackbackPlugin;
import org.blojsom.plugin.trackback.event.TrackbackResponseSubmissionEvent;
import org.blojsom.util.BlojsomUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Link spam moderation plugin
 *
 * @author David Czarnecki
 * @version $Id: LinkSpamModerationPlugin.java,v 1.2 2006/03/26 21:46:57 czarneckid Exp $
 * @since blojsom 3.0
 */
public class LinkSpamModerationPlugin implements Plugin, Listener {

    private Log _logger = LogFactory.getLog(LinkSpamModerationPlugin.class);

    private static final Pattern LINK_PATTERN = Pattern.compile("<a.*?href=.*?>", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    private static final String LINKSPAM_COMMENT_THRESHOLD = "linkspam-comment-threshold";

    private static final String LINKSPAM_TRACKBACK_THRESHOLD = "linkspam-trackback-threshold";

    private static final String DELETE_LINKSPAM = "delete-linkspam";

    private static final int DEFAULT_LINK_THRESHOLD = 3;

    private EventBroadcaster _eventBroadcaster;

    /**
     * Create a new instance of the link spam moderation plugin
     */
    public LinkSpamModerationPlugin() {
    }

    /**
     * Set the {@link EventBroadcaster} event broadcaster
     *
     * @param eventBroadcaster {@link EventBroadcaster}
     */
    public void setEventBroadcaster(EventBroadcaster eventBroadcaster) {
        _eventBroadcaster = eventBroadcaster;
    }

    /**
     * Initialize this plugin. This method only called when the plugin is instantiated.
     *
     * @throws org.blojsom.plugin.PluginException
     *          If there is an error initializing the plugin
     */
    public void init() throws PluginException {
        _eventBroadcaster.addListener(this);
    }

    /**
     * Process the blog entries
     *
     * @param httpServletRequest  Request
     * @param httpServletResponse Response
     * @param blog                {@link Blog} instance
     * @param context             Context
     * @param entries             Blog entries retrieved for the particular request
     * @return Modified set of blog entries
     * @throws PluginException If there is an error processing the blog entries
     */
    public Entry[] process(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Blog blog, Map context, Entry[] entries) throws PluginException {
        return entries;
    }

    /**
     * Perform any cleanup for the plugin. Called after {@link #process}.
     *
     * @throws org.blojsom.plugin.PluginException
     *          If there is an error performing cleanup for this plugin
     */
    public void cleanup() throws PluginException {
    }

    /**
     * Called when BlojsomServlet is taken out of service
     *
     * @throws org.blojsom.plugin.PluginException
     *          If there is an error in finalizing this plugin
     */
    public void destroy() throws PluginException {
    }

    /**
     * Handle an event broadcast from another component
     *
     * @param event {@link org.blojsom.event.Event} to be handled
     */
    public void handleEvent(Event event) {
    }

    /**
     * Process an event from another component
     *
     * @param event {@link org.blojsom.event.Event} to be handled
     */
    public void processEvent(Event event) {
        if (event instanceof ResponseSubmissionEvent) {
            ResponseSubmissionEvent responseSubmissionEvent = (ResponseSubmissionEvent) event;
            String text = responseSubmissionEvent.getContent();
            Map metaData = responseSubmissionEvent.getMetaData();
            if (!BlojsomUtils.checkNullOrBlank(text)) {
                Matcher linkMatcher = LINK_PATTERN.matcher(text);
                int linkCount = 0;
                while (linkMatcher.find()) {
                    linkCount++;
                }
                int linkThreshold;
                String thresholdProperty = "";
                if (responseSubmissionEvent instanceof CommentResponseSubmissionEvent) {
                    thresholdProperty = LINKSPAM_COMMENT_THRESHOLD;
                } else if (responseSubmissionEvent instanceof TrackbackResponseSubmissionEvent) {
                    thresholdProperty = LINKSPAM_TRACKBACK_THRESHOLD;
                }
                String thresholdPropertyValue = responseSubmissionEvent.getBlog().getProperty(thresholdProperty);
                if (BlojsomUtils.checkNullOrBlank(thresholdPropertyValue)) {
                    thresholdPropertyValue = Integer.toString(DEFAULT_LINK_THRESHOLD);
                }
                String deleteLinkSpamPropertyValue = responseSubmissionEvent.getBlog().getProperty(DELETE_LINKSPAM);
                boolean deleteLinkSpam;
                try {
                    linkThreshold = Integer.parseInt(thresholdPropertyValue);
                } catch (NumberFormatException e) {
                    linkThreshold = DEFAULT_LINK_THRESHOLD;
                }
                deleteLinkSpam = Boolean.valueOf(deleteLinkSpamPropertyValue).booleanValue();
                if (linkCount >= linkThreshold) {
                    _logger.debug("Exceeded threshold for links in response: " + linkCount + " > " + linkThreshold);
                    if (responseSubmissionEvent instanceof CommentResponseSubmissionEvent) {
                        if (!deleteLinkSpam) {
                            metaData.put(CommentModerationPlugin.BLOJSOM_COMMENT_MODERATION_PLUGIN_APPROVED, Boolean.FALSE.toString());
                        } else {
                            metaData.put(CommentPlugin.BLOJSOM_PLUGIN_COMMENT_METADATA_DESTROY, Boolean.TRUE);
                        }
                    } else if (responseSubmissionEvent instanceof TrackbackResponseSubmissionEvent) {
                        if (!deleteLinkSpam) {
                            metaData.put(TrackbackModerationPlugin.BLOJSOM_TRACKBACK_MODERATION_PLUGIN_APPROVED, Boolean.FALSE.toString());
                        } else {
                            metaData.put(TrackbackPlugin.BLOJSOM_PLUGIN_TRACKBACK_METADATA_DESTROY, Boolean.TRUE);
                        }
                    } else if (responseSubmissionEvent instanceof PingbackResponseSubmissionEvent) {
                        if (deleteLinkSpam) {
                            metaData.put(PingbackPlugin.BLOJSOM_PLUGIN_PINGBACK_METADATA_DESTROY, Boolean.TRUE);
                        }
                    }
                }
            }
        }
    }
}
