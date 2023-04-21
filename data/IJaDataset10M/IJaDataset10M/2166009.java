package net.disy.ogc.wps.v_1_0_0.proxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.JAXBElement;
import net.disy.ogc.ows.v_1_1_0.OwsException;
import net.disy.ogc.wps.v_1_0_0.WpsProcess;
import net.disy.ogc.wps.v_1_0_0.util.WpsUtils;
import net.opengis.ows.v_1_1_0.CodeType;
import net.opengis.ows.v_1_1_0.Operation;
import net.opengis.ows.v_1_1_0.RequestMethodType;
import net.opengis.wps.v_1_0_0.ProcessBriefType;
import net.opengis.wps.v_1_0_0.ProcessDescriptionType;
import net.opengis.wps.v_1_0_0.ProcessDescriptions;
import net.opengis.wps.v_1_0_0.WPSCapabilitiesType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultProxyWpsProcessFactory implements ProxyWpsProcessFactory {

    private final Log log = LogFactory.getLog(DefaultProxyWpsProcessFactory.class);

    private static final String ID_SEPARATOR = "-";

    private final GenericMarshallingHttpClient httpClient;

    private final PrefixStrategy prefixStrategy;

    public DefaultProxyWpsProcessFactory(GenericMarshallingHttpClient httpClient, PrefixStrategy prefixStrategy) {
        this.httpClient = httpClient;
        this.prefixStrategy = prefixStrategy;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<WpsProcess> createProxyWpsProcesses(String capabilitiesUrl) throws OwsException, WPSConnectionException, WPSMarshallingException {
        List<WpsProcess> result = new ArrayList<WpsProcess>();
        JAXBElement<WPSCapabilitiesType> capabilities = ExceptionHandlingUtils.handleExceptionReport(httpClient.executeFullyParametrizedGetRequest(capabilitiesUrl), JAXBElement.class);
        String describeProcessUrl = null;
        String executeUrl = null;
        for (Operation op : capabilities.getValue().getOperationsMetadata().getOperation()) {
            List<JAXBElement<RequestMethodType>> requestMethods = op.getDCP().get(0).getHTTP().getGetOrPost();
            if ("DescribeProcess".equals(op.getName())) {
                JAXBElement<RequestMethodType> describeProcessRequestMethod = RequestMethodUtils.getGet(requestMethods);
                if (describeProcessRequestMethod != null) {
                    String describeProcessBaseUrl = describeProcessRequestMethod.getValue().getHref();
                    log.debug("Extracted describe process URL: [" + describeProcessBaseUrl + "]");
                    describeProcessUrl = describeProcessBaseUrl + (describeProcessBaseUrl.contains("?") ? "" : "?") + "service=WPS&request=DescribeProcess&version=1.0.0&identifier=";
                } else {
                    throw new UnsupportedOperationException("Currently only GET DescribeProcess operation is supported");
                }
            } else if ("Execute".equals(op.getName())) {
                JAXBElement<RequestMethodType> describeProcessRequestMethod = RequestMethodUtils.getPost(requestMethods);
                if (describeProcessRequestMethod != null) {
                    String executeBaseUrl = describeProcessRequestMethod.getValue().getHref();
                    log.debug("Extracted execute URL: [" + executeBaseUrl + "]");
                    executeUrl = executeBaseUrl + (executeBaseUrl.contains("?") ? "" : "?") + "service=WPS&request=Execute&version=1.0.0&identifier=";
                } else {
                    throw new UnsupportedOperationException("Only POST is supported for Execute");
                }
            }
        }
        String proxyPrefix = prefixStrategy.createPrefixFromURL(capabilitiesUrl);
        log.debug("Created prefix [" + proxyPrefix + "] for the capabilities URL [" + capabilitiesUrl + "]");
        for (ProcessBriefType process : capabilities.getValue().getProcessOfferings().getProcess()) {
            CodeType remoteId = process.getIdentifier();
            String describeProcessUrlWithId = describeProcessUrl + remoteId.getValue();
            String executeUrlWithId = executeUrl + remoteId.getValue();
            ProcessDescriptions processes = ExceptionHandlingUtils.handleExceptionReport(httpClient.executeFullyParametrizedGetRequest(describeProcessUrlWithId), ProcessDescriptions.class);
            ProcessDescriptionType remoteDescription = processes.getProcessDescription().get(0);
            CodeType proxyId = WpsUtils.createCodeType(proxyPrefix + ID_SEPARATOR + remoteId.getValue());
            log.debug("Created proxy id [" + proxyId.getValue() + "]");
            WpsProcess proxyProcess = new ProxyProcess(proxyId, executeUrlWithId, remoteDescription, httpClient);
            log.debug("Created proxy process: " + proxyProcess);
            result.add(proxyProcess);
        }
        return result;
    }
}
