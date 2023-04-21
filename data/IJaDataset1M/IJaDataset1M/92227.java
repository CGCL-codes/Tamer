package org.xaware.server.engine.instruction.bizcomps.sfconfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import org.xaware.server.engine.IBizViewContext;
import org.xaware.server.engine.context.AbstractConfigTranslator;
import org.xaware.server.engine.exceptions.XAwareConfigurationException;
import org.xaware.server.engine.exceptions.XAwareProcessingException;
import org.xaware.server.engine.instruction.bizcomps.sqlconfig.SqlWhereClauseParameter;
import org.xaware.server.engine.instruction.bizcomps.sqlconfig.SqlWhereFragmentConfig;
import org.xaware.server.engine.instruction.bizcomps.sqlconfig.SqlWhereSegmentConfig;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * It is the responsibility of this class to produce a set of where clause segments (without the word where in the
 * clause) that can be used in building a SQL statement. It is also the responsibility of this class to add the
 * generated where clause parameters to a list of parameters for the SQL statement. The top element in this structure is
 * xa:where. The children of the xa:where element are xa:frag_set elements. Segments are joined using an AND
 * conjunction.
 * <p>
 * <b>An example of the structure:</b>
 * <p>
 * <code>
 * &lt;xa:where&gt;<br>
 * &nbsp;&nbsp;&lt;xa:frag_set&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;xa:frag xa:left_value="LastName" xa:oper="=" xa:right_value="%LastName%" xa:datatype="12" /&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;xa:frag xa:left_value="FirstName" xa:oper="=" xa:right_value="%FirstName%" xa:datatype="12" /&gt;<br>
 * &nbsp;&nbsp;&lt;/xa:frag_set&gt;<br>
 * &lt;/xa:where&gt;<br>
 * </code>
 * <p>
 * The resulting where clause is:
 * <p>
 * (((LastName = ?)) AND ((FirstName = ?)))
 * <p>
 * 
 * @see SqlWhereFragmentConfig
 * @see SqlWhereSegmentConfig
 * @see SqlWhereClauseParameter
 * 
 * @author jweaver
 * 
 */
public class SFWhereClauseConfig extends AbstractConfigTranslator {

    public static final String WHERE_CLAUSE_ELEMENT_NAME = XAwareConstants.BIZCOMPONENT_ATTR_WHERE;

    /** */
    private static final String CLASS_LOCATION = "SFWhereClauseConfig";

    /** set of {@link SqlWhereSegmentConfig} objects build from xa:frag_set elements */
    private List<SFWhereSegmentConfig> m_segmentsList = new ArrayList<SFWhereSegmentConfig>(2);

    /**
     * Constructor
     * 
     * @see AbstractConfigTranslator
     * @param p_configElement
     * @param p_context
     * @param p_logger
     * @throws XAwareException
     */
    public SFWhereClauseConfig(Element p_configElement, IBizViewContext p_context, XAwareLogger p_logger) throws XAwareException {
        super(p_context, p_logger, CLASS_LOCATION, p_configElement);
        translateConfig();
    }

    @Override
    protected String getElementNameConstant() {
        return WHERE_CLAUSE_ELEMENT_NAME;
    }

    /**
     * Parses the xa:where element.
     * 
     * @throws XAwareProcessingException
     * @throws XAwareException
     *             if there is a problem performing substitution.
     */
    private void translateConfig() throws XAwareConfigurationException, XAwareException {
        List whereSets = m_configElement.getChildren(SFWhereSegmentConfig.WHERE_SET_ELEMENT_NAME, XAwareConstants.xaNamespace);
        if (whereSets != null && !whereSets.isEmpty()) {
            for (Iterator segmentsIter = whereSets.iterator(); segmentsIter.hasNext(); ) {
                Element segmentElement = (Element) segmentsIter.next();
                SFWhereSegmentConfig segment = new SFWhereSegmentConfig(m_context, this.m_logger, segmentElement);
                m_segmentsList.add(segment);
            }
        }
    }

    /**
     * Builds a string that represents the configured set of where clause fragments.
     * 
     * @param p_whereClauseParameters
     * @return
     */
    public String generateWhereClauseSegments() {
        StringBuffer whereBuff = new StringBuffer(250);
        if (m_segmentsList != null && !m_segmentsList.isEmpty()) {
            whereBuff.append("(");
            for (int i = 0; i < m_segmentsList.size(); i++) {
                if (i != 0) {
                    whereBuff.append(" OR ");
                }
                SFWhereSegmentConfig segment = m_segmentsList.get(i);
                whereBuff.append(segment.getWhereClauseFragments());
            }
            whereBuff.append(")");
        }
        return whereBuff.toString();
    }

    /**
     * Creates a set of Objects representing the values of the parameters based on the xa:right_value and the
     * xa:datatype setting.
     * 
     * @return
     * @throws XAwareException
     *             If there is a problem converting from a string to an Object representation of the value.
     */
    @SuppressWarnings("unchecked")
    public Object[] getWhereClauseParameterValues() throws XAwareException {
        ArrayList<Object> values = new ArrayList<Object>(3);
        for (Iterator iter = m_segmentsList.iterator(); iter.hasNext(); ) {
            SFWhereSegmentConfig segment = (SFWhereSegmentConfig) iter.next();
            values.addAll(segment.getWhereParameterValues(m_logger));
        }
        return values.toArray();
    }

    /**
     * Iterates through the list of fragments and constructs new {@link SqlWhereClauseParameter} objects.
     * 
     * @return a List of {@link SqlWhereClauseParameter} objects
     */
    public List<SFWhereClauseParameter> getWhereClauseParameters() {
        ArrayList<SFWhereClauseParameter> params = new ArrayList<SFWhereClauseParameter>();
        for (Iterator iter = m_segmentsList.iterator(); iter.hasNext(); ) {
            SFWhereSegmentConfig segment = (SFWhereSegmentConfig) iter.next();
            params.addAll(segment.getWhereClauseParameters());
        }
        return params;
    }
}
