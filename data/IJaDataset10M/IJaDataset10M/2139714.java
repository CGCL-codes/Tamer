package org.xaware.server.engine.instruction.bizcomps.sqlconfig;

import org.jdom.Element;
import org.xaware.server.engine.IBizViewContext;
import org.xaware.server.engine.context.AbstractConfigTranslator;
import org.xaware.server.engine.exceptions.XAwareConfigMissingException;
import org.xaware.server.engine.exceptions.XAwareConfigurationException;
import org.xaware.server.engine.instruction.bizcomps.SqlBizCompInst;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.XAwareSubstitutionException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This element (xa:row_convert) defines the XML structure of the element that was converted to a BizComponent
 * reference, including the element's child elements. This element is ignored by {@link SqlBizCompInst} it is used to
 * document the original structure of an element that was converted to a BizComponent reference. Do not modify this
 * element. You can use this element to revert a converted element to its original XML format by replacing it with a
 * copy of the xa:row_convert element's child elements.
 * 
 * @author jweaver
 * 
 */
public class SqlRowConvertConfig extends AbstractConfigTranslator {

    private static final String classLocation = "SqlRowConvertConfig";

    public static final String ROW_CONVERT_ELEMENT_NAME = XAwareConstants.BIZCOMPONENT_ATTR_ROWCONVERT;

    /**
     * @throws XAwareConfigMissingException
     *             If the row convert element is not a child of the parent element passed in.
     * @throws XAwareConfigurationException
     * @throws XAwareSubstitutionException
     *             when the attributes associated with the found element can not be substituted
     */
    public SqlRowConvertConfig(final IBizViewContext p_context, final XAwareLogger p_logger, Element p_configElement) throws XAwareException {
        super(p_context, p_logger, classLocation, p_configElement);
        transferConfigInfo();
    }

    /**
     * @return XAwareConstants.BIZCOMPONENT_ATTR_ROWCONVERT
     * @see org.xaware.server.engine.context.BaseConfigTranslator#getElementNameConstant()
     */
    @Override
    public String getElementNameConstant() {
        return ROW_CONVERT_ELEMENT_NAME;
    }

    /**
     * 
     */
    private void transferConfigInfo() {
    }
}
