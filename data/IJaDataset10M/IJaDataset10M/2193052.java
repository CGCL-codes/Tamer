package org.wwweeeportal.portal.channelplugins;

import java.util.*;
import java.util.regex.*;
import org.w3c.dom.*;
import org.wwweeeportal.util.xml.dom.*;
import org.wwweeeportal.util.ws.rs.*;
import org.wwweeeportal.portal.*;

/**
 * <p>
 * A {@link org.wwweeeportal.portal.Channel.Plugin Plugin} providing a channel {@linkplain AbstractChannelAugmentation
 * augmentation} for {@linkplain MarkupManager#createMetaPropsPublishElement(Node, Map) publishing} the channel
 * &quot;meta-props&quot;.
 * </p>
 * 
 * <p>
 * The published &quot;meta-props&quot; may include {@linkplain #PUBLISH_ALL_ENABLE_PROP all} or
 * {@linkplain #PUBLISH_BY_NUM_PROP select} values from the
 * {@link org.wwweeeportal.portal.Page.Request#getMetaProps(Map) request},
 * {@linkplain WWWeeePortal#getMetaProps(Map, SecurityContext, HttpHeaders) portal},
 * {@linkplain Page#getMetaProps(Page.Request, Map) page}, {@linkplain Channel#getMetaProps(Page.Request, Map) channel},
 * and {@linkplain org.wwweeeportal.portal.Channel.Plugin#getMetaProps(Page.Request, Map) plugin}.
 * </p>
 * 
 * <h3 id="configuration">Configuration</h3>
 * <p>
 * In addition to those inherited from the {@link AbstractChannelAugmentation} class, the following
 * {@linkplain ConfigManager configuration properties} are supported by this class:
 * </p>
 * <ul>
 * <li>{@link #PUBLISH_ALL_ENABLE_PROP}</li>
 * <li>{@link #PUBLISH_BY_NUM_PROP}</li>
 * </ul>
 * 
 * @see MarkupManager#createMetaPropsPublishElement(Node, Map)
 */
public class ChannelMetaProps extends AbstractChannelAugmentation {

    /**
   * The key to a {@link RSProperties#RESULT_BOOLEAN_CONVERTER Boolean} property enabling the publishing of <em>all</em>
   * available &quot;meta-props&quot;. You need to be <em>very</em> conscious of security when enabling this property,
   * as the values could contain very sensitive data. The inclusion of this option is primarily intended for cases where
   * the published values will be utilized by some process (such as a {@link ChannelTransformer transformation}), and
   * not made generally available to clients.
   * 
   * @see MarkupManager#createMetaPropsPublishElement(Node, Map)
   * @category WWWEEE_PORTAL_CONFIG_PROP
   */
    public static final String PUBLISH_ALL_ENABLE_PROP = "Channel.MetaProps.Publish.All.Enable";

    /**
   * The prefix defining an ordered set of numbered keys to {@link RSProperties#RESULT_STRING_CONVERTER String} property
   * values, each in turn specifying the key to a &quot;meta-prop&quot; which should be published.
   * 
   * @see MarkupManager#createMetaPropsPublishElement(Node, Map)
   * @category WWWEEE_PORTAL_CONFIG_PROP
   */
    public static final String PUBLISH_BY_NUM_PROP = "Channel.MetaProps.Publish.Num.";

    /**
   * @see #PUBLISH_BY_NUM_PROP
   */
    protected static final Pattern PUBLISH_BY_NUM_PATTERN = Pattern.compile("^" + Pattern.quote(PUBLISH_BY_NUM_PROP) + ".*");

    /**
   * Construct a new <code>ChannelMetaProps</code> instance.
   */
    public ChannelMetaProps(final Channel channel, final ContentManager.ChannelPluginDefinition<?> definition) throws WWWeeePortal.Exception {
        super(channel, definition);
        return;
    }

    @Override
    protected void createAugmentationContent(final Page.Request pageRequest, final Channel.ViewResponse viewResponse, final Element augmentationElement) throws WWWeeePortal.Exception {
        final Map<String, Object> metaProps = new HashMap<String, Object>();
        pageRequest.getMetaProps(metaProps);
        getPortal().getMetaProps(metaProps, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders());
        pageRequest.getPage().getMetaProps(pageRequest, metaProps);
        getChannel().getMetaProps(pageRequest, metaProps);
        getMetaProps(pageRequest, metaProps);
        final boolean isPublishAllEnabled = getConfigProp(PUBLISH_ALL_ENABLE_PROP, pageRequest, RSProperties.RESULT_BOOLEAN_CONVERTER, Boolean.FALSE, false, false).booleanValue();
        final SortedMap<String, String> publishedByNum = (!isPublishAllEnabled) ? ConfigManager.getConfigProps(definition.getProperties(), PUBLISH_BY_NUM_PATTERN, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders(), RSProperties.RESULT_STRING_CONVERTER, true, true) : null;
        Map<String, Object> publishMetaProps = null;
        if (isPublishAllEnabled) {
            publishMetaProps = metaProps;
        } else {
            publishMetaProps = new HashMap<String, Object>();
            for (String publishMetaProp : publishedByNum.values()) {
                final Object value = metaProps.get(publishMetaProp);
                if (value != null) publishMetaProps.put(publishMetaProp, value);
            }
        }
        final Element propertiesElement = MarkupManager.createMetaPropsPublishElement(augmentationElement, publishMetaProps);
        getChannel().setIDAndClassAttrs(propertiesElement, Arrays.asList("properties"), new String[][] { new String[] { getPortal().getPortalID(), "internal" } }, null);
        DOMUtil.createAttribute(null, null, "title", "Channel Meta-Properties", propertiesElement);
        return;
    }
}
