package web.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import communication.Manager;

@Path("/getservices")
public class Services {

    /** Responde com todos os servicos oferecidos pela RSSF
	 * */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String response() {
        Manager manager = new Manager();
        return manager.NewQuery();
    }
}
