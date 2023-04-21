package com.blogspot.dmottab.server.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.blogspot.dmottab.server.bo.BOPerfil;

@Path("/Perfil")
public class WSPerfil {

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public BOPerfil getXML() {
        BOPerfil perfil = new BOPerfil();
        perfil.setNombre("David Alcides");
        perfil.setApellido("Motta Baldarrago");
        perfil.setLogin("dmotta");
        perfil.setPassword("1234");
        return perfil;
    }
}
