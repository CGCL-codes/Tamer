package org.apache.axis2.jaxws.client.config;

import org.apache.axis2.Constants;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.jaxws.ExceptionFactory;
import org.apache.axis2.jaxws.binding.SOAPBinding;
import org.apache.axis2.jaxws.core.MessageContext;
import org.apache.axis2.jaxws.feature.ClientConfigurator;
import org.apache.axis2.jaxws.i18n.Messages;
import org.apache.axis2.jaxws.message.Message;
import org.apache.axis2.jaxws.spi.Binding;
import org.apache.axis2.jaxws.spi.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.ws.soap.MTOMFeature;
import java.io.InputStream;
import java.util.List;

/**
 *
 */
public class MTOMConfigurator implements ClientConfigurator {

    private static Log log = LogFactory.getLog(MTOMConfigurator.class);

    public void configure(MessageContext messageContext, BindingProvider provider) {
        Binding bnd = (Binding) provider.getBinding();
        MTOMFeature mtomFeature = (MTOMFeature) bnd.getFeature(MTOMFeature.ID);
        Message requestMsg = messageContext.getMessage();
        requestMsg.setMTOMEnabled(false);
        if (mtomFeature == null) {
            throw ExceptionFactory.makeWebServiceException(Messages.getMessage("mtomFeatureErr"));
        }
        if (mtomFeature.isEnabled()) {
            int threshold = mtomFeature.getThreshold();
            List<String> attachmentIDs = requestMsg.getAttachmentIDs();
            requestMsg.setMTOMEnabled(true);
            if (threshold <= 0) {
                if (log.isDebugEnabled()) {
                    log.debug("Enabling MTOM with no threshold.");
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("MTOM Threshold Value =" + threshold);
                }
                messageContext.setProperty(Constants.Configuration.MTOM_THRESHOLD, new Integer(threshold));
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("The MTOMFeature was found, but not enabled.");
            }
        }
    }

    public boolean supports(Binding binding) {
        return binding instanceof SOAPBinding;
    }
}
