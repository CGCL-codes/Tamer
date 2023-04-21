package net.ontopia.topicmaps.nav2.taglibs.TMvalue;

import java.util.Collection;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import net.ontopia.topicmaps.nav2.core.NavigatorRuntimeException;
import net.ontopia.topicmaps.nav2.core.ValueProducingTagIF;
import net.ontopia.topicmaps.nav2.core.ValueAcceptingTagIF;
import net.ontopia.topicmaps.nav2.taglibs.logic.ContextTag;
import net.ontopia.topicmaps.nav2.utils.FrameworkUtils;

/**
 * INTERNAL: Value Producing Tag for generating a collection of
 * TopicMapReferenceIF objects containing information about topicmaps
 * available to this application.
 * <p>
 * (Note: this is somewhat special because this Tag does not need to
 *  manipulate an input collection and so it is not implementing
 * the interface ValueProducingTagIF).
 */
public class TopicMapReferencesTag extends TagSupport {

    /**
   * Process the start tag for this instance.
   */
    public int doStartTag() throws JspTagException {
        ValueAcceptingTagIF acceptingTag = (ValueAcceptingTagIF) findAncestorWithClass(this, ValueAcceptingTagIF.class);
        ContextTag contextTag = FrameworkUtils.getContextTag(pageContext);
        Collection refs = contextTag.getNavigatorApplication().getTopicMapRepository().getReferences();
        acceptingTag.accept(refs);
        return SKIP_BODY;
    }
}
