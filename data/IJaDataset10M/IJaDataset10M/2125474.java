package net.ontopia.topicmaps.nav2.taglibs.output;

import java.io.IOException;
import java.util.Iterator;
import java.util.Collection;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import net.ontopia.utils.CollectionUtils;
import net.ontopia.utils.StringifierIF;
import net.ontopia.utils.StringUtils;
import net.ontopia.topicmaps.core.TopicNameIF;
import net.ontopia.topicmaps.core.DataTypes;
import net.ontopia.topicmaps.core.VariantNameIF;
import net.ontopia.topicmaps.core.OccurrenceIF;
import net.ontopia.topicmaps.nav2.core.NavigatorConfigurationIF;
import net.ontopia.topicmaps.nav2.core.ContextManagerIF;
import net.ontopia.topicmaps.nav2.core.NavigatorRuntimeException;
import net.ontopia.utils.ObjectUtils;

/**
 * INTERNAL: Output Producing Tag for writing out the content of an
 * occurrence or a string.
 * <p>Note: Only outputs first element of collection.
 */
public class ContentTag extends BaseOutputProducingTag implements StringifierIF {

    protected String strifyCN;

    public final void generateOutput(JspWriter out, Iterator iter) throws JspTagException, IOException {
        StringifierIF strify = this;
        if (strifyCN != null) strify = (StringifierIF) contextTag.getNavigatorApplication().getInstanceOf(strifyCN);
        Object elem = null;
        if (iter.hasNext()) elem = iter.next();
        if (elem == null) {
            ContextManagerIF ctxtMgr = contextTag.getContextManager();
            Collection coll = ctxtMgr.getValue(variableName);
            if (coll != null) elem = CollectionUtils.getFirstElement(coll);
        }
        out.print(strify.toString(elem));
    }

    /**
   * Tag attribute for setting the stringifier to be used when
   * producing the string from the selected object.
   * @since 2.0
   */
    public final void setStringifier(String strifyCN) {
        this.strifyCN = strifyCN;
    }

    /**
   * PRIVATE: Included to implement StringifierIF interface.
   */
    public String toString(Object object) {
        String content = null;
        String NULL_VALUE = null;
        String NULL_VALUE_ALT = null;
        String EMPTY_VALUE = null;
        String EMPTY_VALUE_ALT = null;
        if (object instanceof OccurrenceIF) {
            OccurrenceIF occ = (OccurrenceIF) object;
            if (ObjectUtils.equals(DataTypes.TYPE_STRING, occ.getDataType())) content = occ.getValue();
            NULL_VALUE = NavigatorConfigurationIF.OCCURRENCE_NULLVALUE;
            NULL_VALUE_ALT = NavigatorConfigurationIF.DEFVAL_OCC_NULLVALUE;
            EMPTY_VALUE = NavigatorConfigurationIF.OCCURRENCE_EMPTYVALUE;
            EMPTY_VALUE_ALT = NavigatorConfigurationIF.DEFVAL_OCC_EMPTYVALUE;
        } else if (object instanceof TopicNameIF || object instanceof VariantNameIF) {
            if (object instanceof TopicNameIF) content = ((TopicNameIF) object).getValue(); else content = ((VariantNameIF) object).getValue();
            NULL_VALUE = NavigatorConfigurationIF.NAMESTRING_NULLVALUE;
            EMPTY_VALUE = NavigatorConfigurationIF.NAMESTRING_EMPTYVALUE;
        } else content = (object == null ? NULL_VALUE : object.toString());
        if (content == null || content.equals("")) {
            NavigatorConfigurationIF navConf = contextTag.getNavigatorConfiguration();
            if (navConf != null) {
                if (content == null && NULL_VALUE != null) content = navConf.getProperty(NULL_VALUE, NULL_VALUE_ALT); else if (EMPTY_VALUE != null) content = navConf.getProperty(EMPTY_VALUE, EMPTY_VALUE_ALT);
            }
        }
        return StringUtils.escapeHTMLEntities(content);
    }
}
