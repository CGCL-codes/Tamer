package eu.planets_project.tb.impl.services.wrappers;

import java.net.URL;
import java.util.List;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import eu.planets_project.services.characterise.CharacteriseResult;
import eu.planets_project.services.compare.CompareProperties;
import eu.planets_project.services.compare.CompareResult;
import eu.planets_project.services.datatypes.DigitalObject;
import eu.planets_project.services.datatypes.Parameter;
import eu.planets_project.services.datatypes.ServiceDescription;
import eu.planets_project.tb.impl.services.util.PlanetsServiceExplorer;

/**
 * This is a wrapper class that upgrades all supported Identify service
 * interfaces to the same level.
 * 
 * @author <a href="mailto:Andrew.Jackson@bl.uk">Andy Jackson</a>
 *
 */
public class ComparePropertiesWrapper implements CompareProperties {

    /** */
    private static final Log log = LogFactory.getLog(ComparePropertiesWrapper.class);

    PlanetsServiceExplorer pse = null;

    Service service = null;

    CompareProperties c = null;

    /**
     * @param wsdl The WSDL to wrap as a service.
     */
    public ComparePropertiesWrapper(URL wsdl) {
        this.pse = new PlanetsServiceExplorer(wsdl);
        this.init();
    }

    /**
     * @param pse Construct based on a service explorer.
     */
    public ComparePropertiesWrapper(PlanetsServiceExplorer pse) {
        this.pse = pse;
        this.init();
    }

    /**
     * 
     */
    private void init() {
        service = Service.create(pse.getWsdlLocation(), pse.getQName());
        try {
            c = (CompareProperties) service.getPort(pse.getServiceClass());
        } catch (Exception e) {
            log.error("Failed to instanciate service " + pse.getQName() + " at " + pse.getWsdlLocation() + " : Exception - " + e);
            e.printStackTrace();
            c = null;
        }
    }

    public ServiceDescription describe() {
        return c.describe();
    }

    public CompareResult compare(CharacteriseResult first, CharacteriseResult second, List<Parameter> config) {
        return c.compare(first, second, config);
    }

    public List<Parameter> convertConfig(DigitalObject configFile) {
        return c.convertConfig(configFile);
    }

    public CharacteriseResult convertInput(DigitalObject inputFile) {
        return c.convertInput(inputFile);
    }
}
