package at.tuwien.minimee.registry;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;
import at.tuwien.minimee.model.ToolConfig;
import at.tuwien.minimee.registry.xml.PreservationActionServiceFactory;
import eu.planets_project.pp.plato.model.FormatInfo;
import eu.planets_project.pp.plato.services.PlatoServiceException;
import eu.planets_project.pp.plato.xml.StrictErrorHandler;

/**
 * This class is the external entry point for MiniMEE - it contains all {@link PreservationActionService services}
 * that are configured in this instance.
 * The services have to have a corresponding back part configured in the {@link ToolRegistry}
 * @author cbu
 *
 */
public class MiniMeeRegistry {

    private List<PreservationActionService> services = new ArrayList<PreservationActionService>();

    public MiniMeeRegistry() {
    }

    /**
     * Returns all services for the given source- and target format.
     * Currently not exposed as a web service since it has been integrated
     * with Plato.
     * @param sourceFormat
     * @param targetFormat
     * @return
     */
    public List<PreservationActionService> findServices(FormatInfo sourceFormat, FormatInfo targetFormat) {
        ArrayList<PreservationActionService> matching = new ArrayList<PreservationActionService>();
        for (PreservationActionService service : services) {
            boolean doesSourceMatch = false;
            for (FormatInfo info : service.getSourceFormats()) {
                if (doMatch(sourceFormat, info)) doesSourceMatch = true;
            }
            if (doesSourceMatch) {
                if (doMatch(targetFormat, service.getTargetFormat())) matching.add(service);
            }
        }
        return matching;
    }

    /**
     * Checks if the pattern matches the format:
     * - partially true, if info is missing in format or pattern (e.g: null, empty string)
     * - if PUIDs are equal AND 
     * - (format.name contains(!) pattern.name OR pattern.name contains(!) format.name) AND
     * - defaultExtensions are equal
     */
    private boolean doMatch(FormatInfo srcFormat, FormatInfo regInfo) {
        if (srcFormat == null || regInfo == null) {
            return true;
        }
        return (isEmptyOrNull(srcFormat.getPuid()) || isEmptyOrNull(regInfo.getPuid()) || srcFormat.getPuid().equals(regInfo.getPuid())) && (isEmptyOrNull(srcFormat.getName()) || isEmptyOrNull(regInfo.getName()) || regInfo.getName().toUpperCase().contains(srcFormat.getName().toUpperCase()) || srcFormat.getName().toUpperCase().contains(regInfo.getName().toUpperCase())) && (isEmptyOrNull(srcFormat.getDefaultExtension()) || isEmptyOrNull(regInfo.getDefaultExtension()) || srcFormat.getDefaultExtension().toUpperCase().equals(regInfo.getDefaultExtension().toUpperCase()));
    }

    private static boolean isEmptyOrNull(String value) {
        return (value == null) || ("".equals(value));
    }

    /**
     * Trashes current registry information and loads the information of the XML file, which is provided
     * by the input stream <param>config</config>.
     * 
     *  @throws PlatoServiceException if the stream can't be parsed. 
     */
    public void reloadFrom(String configFile) throws PlatoServiceException {
        Digester d = new Digester();
        d.setValidating(false);
        StrictErrorHandler errorHandler = new StrictErrorHandler();
        d.setErrorHandler(errorHandler);
        d.setClassLoader(PreservationActionServiceFactory.class.getClassLoader());
        services.clear();
        d.push(services);
        d.addFactoryCreate("*/preservationActionService", PreservationActionServiceFactory.class);
        d.addSetNext("*/preservationActionService", "add");
        d.addCallMethod("*/preservationActionService/name", "setName", 0);
        d.addCallMethod("*/preservationActionService/description", "setDescription", 0);
        d.addCallMethod("*/preservationActionService/descriptor", "setDescriptor", 0);
        d.addObjectCreate("*/preservationActionService/sourceFormats", ArrayList.class);
        d.addSetNext("*/preservationActionService/sourceFormats", "setSourceFormats");
        d.addObjectCreate("*/preservationActionService/sourceFormats/format", FormatInfo.class);
        d.addBeanPropertySetter("*/format/puid", "puid");
        d.addBeanPropertySetter("*/format/name", "name");
        d.addBeanPropertySetter("*/format/extension", "defaultExtension");
        d.addSetNext("*/preservationActionService/sourceFormats/format", "add");
        d.addObjectCreate("*/preservationActionService/targetFormat", FormatInfo.class);
        d.addBeanPropertySetter("*/targetFormat/puid", "puid");
        d.addBeanPropertySetter("*/targetFormat/name", "name");
        d.addBeanPropertySetter("*/targetFormat/extension", "defaultExtension");
        d.addSetNext("*/preservationActionService/targetFormat", "setTargetFormat");
        d.addCallMethod("*/preservationActionService/url", "setUrl", 0);
        d.addObjectCreate("*/preservationActionService/externalInfo", ArrayList.class);
        d.addSetNext("*/preservationActionService/externalInfo", "setExternalInfo");
        d.addCallMethod("*/preservationActionService/externalInfo/url", "add", 0);
        try {
            InputStream config = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile);
            d.parse(config);
        } catch (IOException e) {
            throw new PlatoServiceException("Could not read registry data.", e);
        } catch (SAXException e) {
            throw new PlatoServiceException("Could not read registry data.", e);
        }
    }

    /**
     * Reloads registry data, at the moment from an XML file
     */
    public void reload() throws PlatoServiceException {
        String configFile = "data/services/miniMEE-actions.xml";
        reloadFrom(configFile);
    }

    public String getToolIdentifier(String url) {
        ToolRegistry toolRegistry = ToolRegistry.getInstance();
        ToolConfig toolConfig = toolRegistry.getToolConfig(ToolRegistry.getToolKey(url));
        if (toolConfig == null) {
            return "";
        }
        return toolConfig.getTool().getIdentifier();
    }

    public String getToolParameters(String url) {
        ToolRegistry toolRegistry = ToolRegistry.getInstance();
        ToolConfig toolConfig = toolRegistry.getToolConfig(ToolRegistry.getToolKey(url));
        return toolConfig.getParams();
    }

    /**
     * For testing only:
     * Creates a MiniMEE registry, data is loaded from the XML file which location is passed
     * as first command line parameter.
     *  
     * @param args
     */
    public static void main(String[] args) {
        try {
            MiniMeeRegistry registry = new MiniMeeRegistry();
            registry.reloadFrom(args[0]);
            List<PreservationActionService> services = registry.findServices(null, null);
            for (PreservationActionService service : services) {
                System.out.println(service.getName());
            }
        } catch (PlatoServiceException e) {
            e.printStackTrace();
        }
    }
}
