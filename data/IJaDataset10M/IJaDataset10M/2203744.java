package be.kuleuven.cw.peno3.server;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/GebruikerHandler")
public class GebruikerDAO extends DataAccessObject {

    protected DatabaseManager manager = DatabaseManager.getInstance();

    protected static final String NO_SUCH_USER = "No such user";

    /**
	 * @return De code voor een niet gevonden gebruiker
	 */
    @GET
    @Path("/loginFoutString")
    @Produces("application/json")
    public String geenGebruiker() {
        return GebruikerDAO.NO_SUCH_USER;
    }

    /**
	 * Probeert een gebruiker op te halen via login en wachtwoord
	 * 
	 * @param login De login van de gebruiker
	 * @param wachtwoord Het wachtwoord van de gebruiker
	 * 
	 * @return Een Json representatie van de gebruiker of de code voor een niet gevonden gebruiker
	 */
    @POST
    @Path("/geefGebruiker")
    @Produces("application/json")
    public String geefGebruiker(@FormParam("login") String login, @FormParam("wachtwoord") String wachtwoord) {
        String query;
        String jsonString;
        if (login != null) {
            if (wachtwoord != null) {
                query = "SELECT gebruikerId, login, voornaam, achternaam, type FROM Gebruiker where login='" + login + "'";
                query += " AND wachtwoord ='" + wachtwoord + "'";
            } else {
                return GebruikerDAO.NO_SUCH_USER;
            }
        } else {
            return GebruikerDAO.NO_SUCH_USER;
        }
        jsonString = haalRecordVanQuery(query);
        jsonString = (jsonString == "") ? GebruikerDAO.NO_SUCH_USER : jsonString;
        return jsonString;
    }

    /**
	 * Probeert een gebruiker op te halen via enkel de gebruikersid
	 * 
	 * @param gebruikerId het id van de gebruiker
	 * 
	 * @return Een Json representatie van de gebruiker of de code voor een niet gevonden gebruiker
	 */
    @POST
    @Path("/geefGebruikerInfo")
    @Produces("application/json")
    public String geefGebruikerInfo(@FormParam("gebruikerId") String id) {
        String query;
        String jsonString;
        if (id != null) {
            query = "SELECT gebruikerId, login, voornaam, achternaam, type FROM Gebruiker where gebruikerId='" + id + "'";
        } else {
            return GebruikerDAO.NO_SUCH_USER;
        }
        jsonString = haalRecordVanQuery(query);
        jsonString = (jsonString == "") ? GebruikerDAO.NO_SUCH_USER : jsonString;
        return jsonString;
    }
}
