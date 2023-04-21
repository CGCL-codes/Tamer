package eu.planets_project.ifr.core.servreg.gui;

import java.net.URL;
import java.util.List;
import java.util.logging.Logger;
import eu.planets_project.ifr.core.servreg.api.Response;
import eu.planets_project.ifr.core.servreg.utils.PlanetsServiceExplorer;
import eu.planets_project.services.datatypes.ServiceDescription;

/**
 * Class encapsulating details of Planets Service Endpoints.  Used in the service registry
 * GUI to hold details of endpoints on the server and in the Service Registry
 * 
 * @author <a href="mailto:carl.wilson@bl.uk">Carl Wilson</a>
 *
 */
public class PlanetsServiceEndpoint {

    /** The logger for this class */
    private static Logger log = Logger.getLogger(PlanetsServiceEndpoint.class.getName());

    private static final String notRegGraphic = "/images/exclamation.png";

    private static final String isRegGraphic = "/images/accept.png";

    private String regGraphic = notRegGraphic;

    /**
	 * Enumeration for the service status
	 * 
	 * @author <a href="mailto:carl.wilson@bl.uk">Carl Wilson</a>
	 */
    public enum Status {

        /** Service is OK and instantiable */
        OK, /** Service is a new style interface but couldn't be instantiated */
        FAILED, /** Service is of a deprecated type */
        DEPRECATED
    }

    /**
	 * Enumeration of the current status of the service description.
	 */
    public enum DescriptionStatus {

        /** Service is live and the description is up to date. */
        OK, /** Service is live but the description is out of date. */
        OUTDATED, /** Service description could not be found. */
        UNKNOWN, /** Service description has just been updated. */
        UPDATED
    }

    /** The URL for the service endpoint location */
    private URL _location = null;

    /** The class of the service */
    private String _class = null;

    /** The status of the service */
    private Status _status;

    /** Boolean flag for registered services */
    private boolean _registered = false;

    /** Copy of the service description */
    private ServiceDescription _serviceDescription = null;

    /** Boolean flag for whether the service description is up to date */
    private DescriptionStatus _descriptionStatus = DescriptionStatus.UNKNOWN;

    /**
	 * @param pse
	 * 		A non null PlanetsServiceExplorer object for the Planets Service Endpoint
	 * @throws IllegalArgumentException
	 */
    public PlanetsServiceEndpoint(PlanetsServiceExplorer pse) throws IllegalArgumentException {
        if (pse == null) {
            String message = "The PlanetsServiceExplorer object cannot be null";
            log.severe(message);
            throw new IllegalArgumentException(message);
        } else if (pse.getServiceClass() == null) {
            String message = "The PlanetsServiceExplorer contained a null service class";
            log.severe(message);
            throw new IllegalArgumentException(message);
        }
        this._location = pse.getWsdlLocation();
        this._class = pse.getServiceClass().getCanonicalName();
        try {
            if (pse.isServiceInstanciable()) {
                this._status = Status.OK;
                try {
                    this._serviceDescription = pse.getServiceDescription();
                } catch (Exception e) {
                    log.severe("Failed to find service description for endpoint: " + pse.getWsdlLocation() + " : " + e);
                }
            } else {
                this._status = Status.FAILED;
            }
        } catch (RuntimeException e) {
            this._status = Status.DEPRECATED;
        }
    }

    /**
	 * @param desc
	 * 		A non null ServiceDescription object for the service
	 * @throws IllegalArgumentException 
	 */
    public PlanetsServiceEndpoint(ServiceDescription desc) throws IllegalArgumentException {
        this._location = desc.getEndpoint();
        this._class = desc.getType();
        this._status = Status.OK;
        this.setRegistered(true);
        this._serviceDescription = desc;
    }

    /**
	 * @return
	 * 		The java.net.URL location for the service endpoint
	 */
    public URL getLocation() {
        return this._location;
    }

    /**
	 * @return
	 * 		The name of the service
	 */
    public String getName() {
        if (this._location.getPath().lastIndexOf('/') >= 0) {
            return this._location.getPath().substring(this._location.getPath().lastIndexOf('/') + 1);
        } else return this._location.getPath();
    }

    /**
	 * @return
	 * 		The category name of the service derived from the class name
	 */
    public String getCategory() {
        if (this._class != null && this._class.indexOf('.') >= 0) {
            return (this._class.substring(this._class.lastIndexOf('.') + 1));
        } else {
            return (this._class);
        }
    }

    /**
	 * @return
	 * 		The type of the service
	 */
    public String getType() {
        return this._class;
    }

    /**
	 * @return
	 * 		The status of the service
	 */
    public Status getStatus() {
        return this._status;
    }

    /**
	 * @return
	 * 		True is this service implements a deprecated interface
	 */
    public boolean isDeprecated() {
        return (this._status == Status.DEPRECATED);
    }

    /**
	 * @param registered
	 * 		Boolean denoting registration
	 */
    public void setRegistered(boolean registered) {
        this._registered = registered;
        if (this._registered) this.setRegGraphic(PlanetsServiceEndpoint.isRegGraphic); else this.setRegGraphic(PlanetsServiceEndpoint.notRegGraphic);
    }

    /**
	 * @return
	 * 		True if the service is registered
	 */
    public boolean isRegistered() {
        return _registered;
    }

    /**
	 * @param regGraphic the regGraphic to set
	 */
    public void setRegGraphic(String regGraphic) {
        this.regGraphic = regGraphic;
    }

    /**
	 * @return the regGraphic
	 */
    public String getRegGraphic() {
        return regGraphic;
    }

    /**
     * @return the _serviceDescription
     */
    public ServiceDescription getDescription() {
        return _serviceDescription;
    }

    /**
     * @return the _upToDate
     */
    public boolean getUpToDate() {
        if (this._descriptionStatus == DescriptionStatus.OK) return true;
        return false;
    }

    /**
     * @return the _descriptionStatus
     */
    public DescriptionStatus getDescriptionStatus() {
        return _descriptionStatus;
    }

    /**
     * @param status the _descriptionStatus to set
     */
    public void setDescriptionStatus(DescriptionStatus status) {
        _descriptionStatus = status;
    }

    /**
     * @return "success", if successful
     */
    public String updateDescription() {
        log.info("update: " + this.getLocation());
        ServiceDescription csd = getCurrentServiceDescription();
        if (csd != null) {
            if (this.getDescription().equals(csd)) {
                this.setDescriptionStatus(DescriptionStatus.OK);
            } else {
                this.removeFromServiceRegistry();
                this._serviceDescription = csd;
                this.addToServiceRegistry();
                this.setDescriptionStatus(DescriptionStatus.UPDATED);
            }
        } else {
            this.setDescriptionStatus(DescriptionStatus.UNKNOWN);
        }
        return "success";
    }

    private void addToServiceRegistry() {
        ServiceDescription toReg = new ServiceDescription.Builder(this.getDescription()).endpoint(this.getLocation()).build();
        Response response = ServiceRegistryBackingBean.registry.register(toReg);
        log.info("Got response success: " + response.success());
        log.info("Got response: " + response.getMessage());
        if (response.success()) {
            log.info("Updated. " + this.getDescription().getEndpoint());
        }
        this.setRegistered(true);
    }

    private void removeFromServiceRegistry() {
        Response response = ServiceRegistryBackingBean.registry.delete(new ServiceDescription.Builder(this.getDescription().getName(), this.getDescription().getType()).endpoint(this.getDescription().getEndpoint()).build());
        log.info("Got response: " + response.getMessage());
        if (response.success()) log.info("Deregistered: " + this.getLocation());
        this.setRegistered(false);
    }

    /**
     * @return "success", if successful
     */
    public String deregisterService() {
        this.removeFromServiceRegistry();
        ServiceRegistryBackingBean reg = (ServiceRegistryBackingBean) ServiceRegistryBackingBean.getManagedObject("RegistryBean");
        List<PlanetsServiceEndpoint> servs = reg.getRegisteredServices();
        if (servs.contains(this)) {
            servs.remove(this);
        }
        return "success";
    }

    /**
     * @return "success", if successful
     */
    public String registerService() {
        if (this.getDescription() != null) {
            if (this.getDescription().getEndpoint() == null) {
                this._serviceDescription = new ServiceDescription.Builder(this.getDescription().toXml()).endpoint(this.getLocation()).build();
            }
            this.addToServiceRegistry();
            ServiceRegistryBackingBean reg = (ServiceRegistryBackingBean) ServiceRegistryBackingBean.getManagedObject("RegistryBean");
            List<PlanetsServiceEndpoint> servs = reg.getRegisteredServices();
            if (!servs.contains(this)) {
                servs.add(this);
            }
        }
        return "success";
    }

    /**
     * 
     */
    public void checkUpToDate() {
        try {
            ServiceDescription csd = getCurrentServiceDescription();
            if (csd != null && this.getDescription().equals(csd)) {
                this.setDescriptionStatus(DescriptionStatus.OK);
            } else {
                this.setDescriptionStatus(DescriptionStatus.OUTDATED);
                log.info("Old: " + this.getDescription().toXmlFormatted());
                if (csd != null) {
                    log.info("New: " + csd.toXmlFormatted());
                } else {
                    log.info("No new service description");
                }
            }
        } catch (Exception e) {
            log.severe("Could not check service description for: " + this.getDescription().getEndpoint());
        }
    }

    private ServiceDescription getCurrentServiceDescription() {
        try {
            PlanetsServiceExplorer pse = new PlanetsServiceExplorer(this.getDescription().getEndpoint());
            return new ServiceDescription.Builder(pse.getServiceDescription().toXml()).endpoint(this.getDescription().getEndpoint()).build();
        } catch (Exception e) {
            log.severe("Could not check service description for: " + this.getDescription().getEndpoint());
            return null;
        }
    }
}
