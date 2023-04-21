package it.polimi.miaria.socialemis.wsnservice;

import it.polimi.miaria.perla2gis.db.perla.Cluster;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Administrator
 */
@Stateless
@Path("it.polimi.miaria.perla2gis.db.perla.cluster")
public class ClusterFacadeREST extends AbstractFacade<Cluster> {

    @PersistenceContext(unitName = "SocialEmis-WSNServicePU")
    private EntityManager em;

    public ClusterFacadeREST() {
        super(Cluster.class);
    }

    @POST
    @Override
    @Consumes({ "application/xml", "application/json" })
    public void create(Cluster entity) {
        super.create(entity);
    }

    @PUT
    @Override
    @Consumes({ "application/xml", "application/json" })
    public void edit(Cluster entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@Context UriInfo info) {
        super.remove(super.find(info));
    }

    @GET
    @Path("{id}")
    @Produces({ "application/xml", "application/json" })
    public Cluster find(@Context UriInfo info) {
        return super.find(info);
    }

    @GET
    @Override
    @Produces({ "application/xml", "application/json" })
    public List<Cluster> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({ "application/xml", "application/json" })
    public List<Cluster> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[] { from, to });
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @java.lang.Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
