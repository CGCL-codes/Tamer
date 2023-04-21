package org.xaware.server.engine.channel;

import org.jdom.Element;
import org.xaware.server.engine.IBizViewContext;
import org.xaware.server.engine.IChannelPoolingSpecification;
import org.xaware.server.engine.exceptions.XAwareConfigMissingException;
import org.xaware.server.engine.exceptions.XAwareConfigurationException;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.XAwareSubstitutionException;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This class provides parsing of the common pooling parameters
 * 
 * 
 * 
 * @author jweaver
 * 
 */
public abstract class BaseChannelPoolingSpecification extends AbstractChannelSpecification implements IChannelPoolingSpecification {

    private static final XAwareLogger lf = XAwareLogger.getXAwareLogger(BaseChannelPoolingSpecification.class.getName());

    @Override
    public void transformSpecInfo(IBizViewContext p_bizViewContext) throws XAwareConfigMissingException, XAwareConfigurationException, XAwareSubstitutionException, XAwareException {
        parsePoolingDefinition(p_bizViewContext);
    }

    protected boolean isSet(final String key) {
        Object obj = m_props.get(key);
        if (obj == null) {
            return false;
        }
        return true;
    }

    protected void parsePoolingDefinition(IBizViewContext bizViewContext) throws XAwareException {
        final String methodName = "parsePoolingDefinition";
        lf.entering(getBizDriverIdentifier() + ":ChannelPoolingSpec", methodName);
        Element poolingParams = m_bizDriverRootElement.getChild(XAwareConstants.BIZDRIVER_POOLINGPARAMS, XAwareConstants.xaNamespace);
        if (poolingParams == null) {
            throw new XAwareConfigMissingException("No " + XAwareConstants.BIZDRIVER_CONNECTION + " element found");
        }
        String s = this.getChildElementValue(poolingParams, XAwareConstants.BIZDRIVER_POOLING_MAXACTIVE, bizViewContext, false);
        if (null != s && s.length() > 0) {
            m_props.put(XAwareConstants.BIZDRIVER_POOLING_MAXACTIVE, s);
        }
        s = this.getChildElementValue(poolingParams, XAwareConstants.BIZDRIVER_POOLING_MAXIDLE, bizViewContext, false);
        if (null != s && s.length() > 0) {
            m_props.put(XAwareConstants.BIZDRIVER_POOLING_MAXIDLE, s);
        }
        s = this.getChildElementValue(poolingParams, XAwareConstants.BIZDRIVER_POOLING_MAXWAIT, bizViewContext, false);
        if (null != s && s.length() > 0) {
            m_props.put(XAwareConstants.BIZDRIVER_POOLING_MAXWAIT, s);
        }
        s = this.getChildElementValue(poolingParams, XAwareConstants.BIZDRIVER_POOLING_MINEVICTABLETIME, bizViewContext, false);
        if (null != s && s.length() > 0) {
            m_props.put(XAwareConstants.BIZDRIVER_POOLING_MINEVICTABLETIME, s);
        }
        s = this.getChildElementValue(poolingParams, XAwareConstants.BIZDRIVER_POOLING_NUMTESTPEREVICTRUN, bizViewContext, false);
        if (null != s && s.length() > 0) {
            m_props.put(XAwareConstants.BIZDRIVER_POOLING_NUMTESTPEREVICTRUN, s);
        }
        s = this.getChildElementValue(poolingParams, XAwareConstants.BIZDRIVER_POOLING_TESTONBORROW, bizViewContext, false);
        if (null != s && s.length() > 0) {
            m_props.put(XAwareConstants.BIZDRIVER_POOLING_TESTONBORROW, s);
        }
        s = this.getChildElementValue(poolingParams, XAwareConstants.BIZDRIVER_POOLING_TESTONRETURN, bizViewContext, false);
        if (null != s && s.length() > 0) {
            m_props.put(XAwareConstants.BIZDRIVER_POOLING_TESTONRETURN, s);
        }
        s = this.getChildElementValue(poolingParams, XAwareConstants.BIZDRIVER_POOLING_TESTWHILEIDLE, bizViewContext, false);
        if (null != s && s.length() > 0) {
            m_props.put(XAwareConstants.BIZDRIVER_POOLING_TESTWHILEIDLE, s);
        }
        s = this.getChildElementValue(poolingParams, XAwareConstants.BIZDRIVER_POOLING_TIMEBTWNEVICTRUNS, bizViewContext, false);
        if (null != s && s.length() > 0) {
            m_props.put(XAwareConstants.BIZDRIVER_POOLING_TIMEBTWNEVICTRUNS, s);
        }
        s = this.getChildElementValue(poolingParams, XAwareConstants.BIZDRIVER_POOLING_INITIALSIZE, bizViewContext, false);
        if (null != s && s.length() > 0) {
            m_props.put(XAwareConstants.BIZDRIVER_POOLING_INITIALSIZE, s);
        }
    }
}
