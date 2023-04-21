package net.disy.ogc.wps.v_1_0_0;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import net.disy.ogc.ows.v_1_1_0.OwsException;
import net.opengis.wps.v_1_0_0.DescribeProcess;
import net.opengis.wps.v_1_0_0.Execute;
import net.opengis.wps.v_1_0_0.ExecuteResponse;
import net.opengis.wps.v_1_0_0.GetCapabilities;
import net.opengis.wps.v_1_0_0.ProcessDescriptions;
import net.opengis.wps.v_1_0_0.WPSCapabilitiesType;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggingWpsOperationdDecorator implements WpsOperations {

    private static Log logger = LogFactory.getLog(WpsOperations.class);

    private final WpsOperations wpsOperations;

    private final JAXBContext context;

    public LoggingWpsOperationdDecorator(final WpsOperations wpsOperations, final JAXBContext context) {
        Validate.notNull(wpsOperations);
        Validate.notNull(context);
        this.wpsOperations = wpsOperations;
        this.context = context;
    }

    public WpsOperations getWpsOperations() {
        return wpsOperations;
    }

    public JAXBContext getContext() {
        return context;
    }

    @Override
    public WPSCapabilitiesType getCapabilities(GetCapabilities input) throws IOException, OwsException {
        if (logger.isTraceEnabled()) {
            logger.trace("Incoming wps:GetCapabilities request:\n" + marshal(input));
        }
        final WPSCapabilitiesType capabilities = getWpsOperations().getCapabilities(input);
        final JAXBElement<WPSCapabilitiesType> output = new JAXBElement<WPSCapabilitiesType>(new QName("http://www.opengis.net/wps/1.0.0", "Capabilities"), WPSCapabilitiesType.class, capabilities);
        if (logger.isTraceEnabled()) {
            logger.trace("Outgoing wps:Capabilities response:\n" + marshal(output));
        }
        return output.getValue();
    }

    @Override
    public ProcessDescriptions describeProcess(DescribeProcess input) throws IOException, OwsException {
        if (logger.isTraceEnabled()) {
            logger.trace("Incoming wps:DescribeProcess request:\n" + marshal(input));
        }
        final ProcessDescriptions output = getWpsOperations().describeProcess(input);
        if (logger.isTraceEnabled()) {
            logger.trace("Outgoing wps:ProcessDescriptions response:\n" + marshal(output));
        }
        return output;
    }

    @Override
    public ExecuteResponse execute(Execute input) throws IOException, OwsException {
        if (logger.isTraceEnabled()) {
            logger.trace("Incoming wps:Execute request:\n" + marshal(input));
        }
        final ExecuteResponse output = getWpsOperations().execute(input);
        if (logger.isTraceEnabled()) {
            logger.trace("Outgoing wps:ExecuteResponse response:\n" + marshal(output));
        }
        return output;
    }

    private String marshal(final Object object) {
        final Writer writer = new StringWriter();
        try {
            final Marshaller marshaller = getContext().createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", WpsConstants.NAMESPACE_PREFIX_MAPPER);
            marshaller.marshal(object, writer);
            return writer.toString();
        } catch (Exception ex) {
            logger.warn("Could not marshall the object [" + object + "].", ex);
            return "Could not marshall the object.";
        }
    }
}
