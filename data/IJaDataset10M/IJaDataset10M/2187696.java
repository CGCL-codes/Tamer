package com.javector;

import com.javector.adaptive.framework.interfaces.XMLMapping;
import com.javector.adaptive.util.dto.*;
import com.javector.soaj.SoajException;
import com.javector.soaj.SoajRuntimeException;
import com.javector.soaj.config.UserDefinedSchema;
import com.javector.soaj.logging.LoggingFactory;
import com.javector.soaj.logging.SoajLogger;
import com.javector.soaj.util.ResourceFinder;
import com.javector.soaj.util.XmlUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import javax.xml.namespace.QName;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WsdlGenerator {

    private static SoajLogger log = LoggingFactory.getLogger(WsdlGenerator.class.getName());

    /**
   * Generate the WSDL specified by the SOAJ configuration to be deployed at a particular path.
   * 
   * @param requestedWsdlPath the path where the WSDL is defined in the SOAJ configuration
   * @param soajConfigDto the SOAJ configuration
   * @return the generated WSDL
   * @throws SoajException
   */
    public String generateWsdl(String requestedWsdlPath, SOAConfigDTO soajConfigDto) throws SoajException {
        log.info("generating WSDL for " + requestedWsdlPath);
        ArrayList<WsdlDTO> wsdlDTOs = soajConfigDto.getWsdlDTOs();
        WsdlDTO requestedWsdlDto = null;
        search: for (WsdlDTO wsdlDTO : wsdlDTOs) {
            for (SoajServiceDTO soajServiceDTO : wsdlDTO.getSoajServiceDTOs()) {
                for (SoajPortDTO soajPortDTO : soajServiceDTO.getSoajPortDTOs()) {
                    String endpointString = soajPortDTO.getEndpoint();
                    URL endpointUrl;
                    try {
                        endpointUrl = new URL(endpointString);
                    } catch (MalformedURLException e) {
                        throw new SoajRuntimeException("SOAJ Configuration contains malformed URL for port endpoint: " + soajPortDTO.getPortName() + " where endpoint is defined as: " + endpointString, e);
                    }
                    if (requestedWsdlPath.equals(endpointUrl.getPath())) {
                        requestedWsdlDto = wsdlDTO;
                        break search;
                    }
                }
            }
        }
        if (requestedWsdlDto == null) {
            throw new SoajException("SOAJ Configuration contains no WSDL definition deployed at: " + requestedWsdlPath);
        }
        return generateWsdlForWsdlDTO(requestedWsdlDto, soajConfigDto);
    }

    /**
   * @param wsdlDTO a WSDL definition contained in a SOAJ configuration
   * @param soajConfigDto the SOAJ configuration
   * @return the generated WSDL
   */
    public String generateWsdlForWsdlDTO(WsdlDTO wsdlDTO, SOAConfigDTO soajConfigDto) {
        return replaceInVelocitytemplate(wsdlDTO, soajConfigDto.getUserDefinedSchemas());
    }

    private String replaceInVelocitytemplate(WsdlDTO wsdlDTO, List<UserDefinedSchema> userDefinedSchemas) {
        String targetNamespace = wsdlDTO.getWsdlNamespace();
        List<SoajServiceDTO> serviceDTOList = wsdlDTO.getSoajServiceDTOs();
        StringWriter writer = new StringWriter();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (SoajServiceDTO soajServiceDTO : serviceDTOList) {
            List<SoajPortDTO> soajPortDTOs = soajServiceDTO.getSoajPortDTOs();
            for (SoajPortDTO soajPortDTO : soajPortDTOs) {
                List<SOAJOperationDTO> soajOperationDTOs = soajPortDTO.getSoajOperationDTOs();
                for (SOAJOperationDTO aSoajOperationDTOList : soajOperationDTOs) {
                    SOAJOperationDTO soajOperationDTO = (SOAJOperationDTO) aSoajOperationDTOList;
                    List<XMLMapping> paramMapping = soajOperationDTO.getParamMapping();
                    for (XMLMapping mapping : paramMapping) {
                        QName typeOrElementQName = mapping.getTypeOrElementQName();
                        String namespaceURI = typeOrElementQName.getNamespaceURI();
                        if (!hashMap.containsKey(namespaceURI)) {
                            hashMap.put(namespaceURI, "soaj" + hashMap.size());
                        }
                        QName returnType = soajOperationDTO.getReturnType();
                        namespaceURI = returnType.getNamespaceURI();
                        if (!hashMap.containsKey(namespaceURI)) {
                            hashMap.put(namespaceURI, "soaj" + hashMap.size());
                        }
                    }
                }
            }
        }
        try {
            Velocity.init();
            VelocityContext context = new VelocityContext();
            context.put("serviceDTOList", serviceDTOList);
            context.put("ContextRoot", "contextroot");
            context.put("WsdlName", wsdlDTO.getWsdlName());
            context.put("targetNamespace", targetNamespace);
            context.put("hashMap", hashMap);
            context.put("userDefinedSchemas", userDefinedSchemas);
            Velocity.evaluate(context, writer, "string", new BufferedReader(new InputStreamReader(ResourceFinder.getResourceAsStream("wsdlTemplate.vm", WsdlGenerator.class))));
        } catch (Exception e) {
            throw new RuntimeException("Exception @ replaceInVelocitytemplate", e);
        }
        return XmlUtil.formatXml(writer.toString());
    }
}
