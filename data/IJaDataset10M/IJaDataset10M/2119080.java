package org.hmaciel.descop.Action;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hmaciel.descop.ejb.ActRelationship.ActRelBean;
import org.hmaciel.descop.ejb.act.ActBean;
import org.hmaciel.descop.ejb.controladores.IAltaDesc;
import org.hmaciel.descop.ejb.datatypes.ENBean;
import org.hmaciel.descop.ejb.datatypes.ENXPBean;
import org.hmaciel.descop.ejb.datatypes.ENXP_Entity;
import org.hmaciel.descop.ejb.datatypes.EN_Entity;
import org.hmaciel.descop.ejb.entity.EntityBean;
import org.hmaciel.descop.ejb.entity.OrganizationBean;
import org.hmaciel.descop.ejb.entity.PersonBean;
import org.hmaciel.descop.ejb.participation.ParticipationBean;
import org.hmaciel.descop.ejb.participation.PerformerBean;
import org.hmaciel.descop.ejb.role.AssignedBean;
import org.hmaciel.descop.ejb.role.RoleBean;
import org.hmaciel.descop.ejb.vocabularios.ActClassVoc;
import org.hmaciel.descop.ejb.vocabularios.ParticipationTypeVoc;
import org.hmaciel.descop.otros.PlantillaBean;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Stateful
@Name("register")
@Scope(ScopeType.SESSION)
public class RegisterAction implements Register {

    @PersistenceContext
    private EntityManager em;

    List<PlantillaBean> plants;

    PlantillaBean plan;

    PersonBean medico;

    @Logger
    private Log log;

    @EJB
    IAltaDesc logica;

    String text;

    String nombreClin;

    Date fecha;

    OrganizationBean org;

    @Create
    public String nuevo() {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        return null;
    }

    public String carga() {
        RoleBean r = em.find(RoleBean.class, 1);
        text = r.getName().toString();
        return "ok";
    }

    public String getNombreClin() {
        return nombreClin;
    }

    public void setNombreClin(String nombreClin) {
        this.nombreClin = nombreClin;
    }

    public String accion() {
        ParticipationBean p = new PerformerBean();
        @SuppressWarnings("unused") ParticipationBean clonPart = (ParticipationBean) p.clone();
        ActBean a = logica.nuevaDesc(em);
        ActBean b = (ActBean) a.clone();
        System.out.print("----------ORIGINAL-----------");
        System.out.print(a.getIdent());
        System.out.print("----------CLON-----------");
        System.out.print(b.getIdent());
        return "ok";
    }

    @SuppressWarnings("unchecked")
    public String levantar() {
        int a = 0;
        a++;
        @SuppressWarnings("unused") List<PersonBean> as = em.createQuery("from DocClinBean").getResultList();
        RoleBean roleCust = new AssignedBean();
        EntityBean maciel = new OrganizationBean();
        ENXPBean nombrePila = new ENXP_Entity();
        nombrePila.setNombre("Hospital Maciel");
        List<ENXPBean> nombreCompleto = new LinkedList<ENXPBean>();
        nombreCompleto.add(nombrePila);
        EN_Entity nombre = new EN_Entity();
        nombre.setNombres(nombreCompleto);
        List<ENBean> namelist = new LinkedList<ENBean>();
        namelist.add(nombre);
        maciel.setName(namelist);
        roleCust.setPlayer(maciel);
        maciel.getPlayedRole().add(roleCust);
        em.persist(roleCust);
        return "ok";
    }

    public String register() {
        log.info("Se ha realizado la Operacion correctamente");
        return "/home.xhtml";
    }

    @SuppressWarnings("unused")
    private ActBean DarServEvent(ActBean a2) {
        ActBean salida = null;
        for (Iterator iterator = a2.getOutboundRelationship().iterator(); iterator.hasNext(); ) {
            ActRelBean rel = (ActRelBean) iterator.next();
            ActBean a = rel.getTarget();
            ActClassVoc actVoc = rel.getTarget().getClassCode();
            if (actVoc == ActClassVoc.Act) {
                salida = a;
            }
        }
        return salida;
    }

    @SuppressWarnings("unused")
    private RoleBean DarCustodian(ActBean a2) {
        ParticipationBean cust = null;
        for (ParticipationBean p : a2.getParticipations()) {
            ParticipationTypeVoc part = p.getTypeCode();
            if (part == ParticipationTypeVoc.Custodian) {
                cust = p;
            }
        }
        return cust.getRol();
    }

    @SuppressWarnings("unused")
    private List<ENBean> armoNombre(String strNombrePila, String strNapellido) {
        ENXPBean nombrePila = new ENXP_Entity();
        nombrePila.setNombre(strNombrePila);
        nombrePila.setTipo("given");
        ENXPBean apellido = new ENXP_Entity();
        apellido.setNombre(strNapellido);
        apellido.setTipo("lastname");
        List<ENXPBean> nombreCompleto = new LinkedList<ENXPBean>();
        nombreCompleto.add(nombrePila);
        nombreCompleto.add(apellido);
        EN_Entity nombre = new EN_Entity();
        nombre.setNombres(nombreCompleto);
        List<ENBean> namelist = new LinkedList<ENBean>();
        namelist.add(nombre);
        return namelist;
    }

    public PersonBean getMedico() {
        return medico;
    }

    public void setMedico(PersonBean medico) {
        this.medico = medico;
    }

    public String register1() {
        @SuppressWarnings("unused") List auts = em.createQuery("select u from Author u where u.id=555").getResultList();
        log.info("Se ha realizado la Operacion con exito");
        return "/registered.xhtml";
    }

    public String register2() {
        return "/registered.xhtml";
    }

    @Destroy
    @Remove
    public void destroy() {
    }

    public String cargoMedicos() {
        return null;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PlantillaBean getPlan() {
        return plan;
    }

    public void setPlan(PlantillaBean plan) {
        this.plan = plan;
    }

    public List<PlantillaBean> getPlants() {
        return plants;
    }

    public void setPlants(List<PlantillaBean> plants) {
        this.plants = plants;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public OrganizationBean getOrg() {
        return org;
    }

    public void setOrg(OrganizationBean org) {
        this.org = org;
    }
}
