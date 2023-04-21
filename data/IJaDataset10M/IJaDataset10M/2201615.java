package Simulation.Response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import Simulation.Request.DeleteCityRequest;

/**
 * R�ponse de suppression d'une ville pour synchroniser le client
 */
@XmlRootElement(name = "DeleteCityResponse")
public class DeleteCityResponse extends DeleteCityRequest implements IResponse {

    /**
	 * Nom de la ville � supprimer.
	 */
    @SuppressWarnings("unused")
    @XmlElement(name = "Name")
    private String sName;

    public DeleteCityResponse(String cityName) {
        this.sName = cityName;
    }

    public DeleteCityResponse() {
    }
}
