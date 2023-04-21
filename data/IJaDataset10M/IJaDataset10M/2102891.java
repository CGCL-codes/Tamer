package com.amidasoft.lincat.session;

import com.amidasoft.lincat.entity.Comarques;
import com.amidasoft.lincat.entity.Contactes;
import com.amidasoft.lincat.entity.Empreses;
import com.amidasoft.lincat.entity.EmpresesContacte;
import com.amidasoft.lincat.entity.Poblacions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import org.jboss.seam.log.Log;
import org.jboss.seam.faces.Renderer;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Delete;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Credentials;

/**
 *
 * @author ricard
 */
@Name("altaEmpresaContacteBean")
@Scope(ScopeType.SESSION)
@Stateful
public class AltaEmpresaContacteBean implements AltaEmpresaContacte {

    private Contactes unContacte;

    @DataModel
    private List<Contactes> llistaContactes;

    @DataModelSelection
    private Contactes contacteSeleccionat;

    private Empreses empresaFeta;

    @In(create = true)
    private Renderer renderer;

    @In(create = true)
    @Out
    private FacesMessages facesMessages;

    @Logger
    private Log log;

    @PersistenceContext
    private EntityManager em;

    @In
    Credentials credentials;

    private String prova;

    private Poblacions poblacioNova;

    @In(required = false)
    @Out(required = false)
    private LlistaPoblacionsPerCP llistaPoblacionsPerCP;

    public AltaEmpresaContacteBean() {
        super();
    }

    @Create
    public void crea() {
        carregaContacteActual();
        if (poblacioNova == null) {
            this.poblacioNova = new Poblacions();
        }
    }

    @Destroy
    @Delete
    @Remove
    public void destrueix() {
    }

    public void enviaMail() {
        log.info("Faré l'enviament del mail....");
        try {
            renderer.render("/mails/MailAltaEmpresa.xhtml");
            log.info("Sembla que ha anat bé");
        } catch (Exception e) {
            facesMessages.add("Email sending failed: " + e.getMessage());
            log.error("Error en fer l'enviament del mail....:");
            log.error(e);
            e.printStackTrace();
        }
    }

    public void carregaEmpresa() {
        Query q = this.em.createQuery("select e.id from Empreses e where e.usuaris.login = ?");
        q.setParameter(1, this.credentials.getUsername());
        Integer empresaId = (Integer) q.getSingleResult();
        this.empresaFeta = this.em.find(Empreses.class, empresaId);
    }

    public void carregaContacteActual() {
        this.unContacte = new Contactes();
    }

    @Factory("llistaContactes")
    public void ompleLlista() {
        carregaEmpresa();
        Query q = this.em.createQuery("select ec.contactes from EmpresesContacte ec where ec.empreses.id = ?");
        q.setParameter(1, empresaFeta.getId());
        this.llistaContactes = (List<Contactes>) q.getResultList();
    }

    private boolean dadesCorrectes() {
        log.info("nom: " + this.unContacte.getNom());
        boolean nom = this.unContacte.getNom().length() > 0;
        boolean addr = this.unContacte.getAddr().length() > 0;
        boolean telf = this.unContacte.getTelefon().length() > 0;
        boolean email = this.unContacte.getEmail().length() > 0 && ((Pattern.compile("(\\w+)@(\\w+\\.)(\\w+)(\\.\\w+)*")).matcher(this.unContacte.getEmail())).matches();
        boolean url = this.unContacte.getUrl().length() > 0;
        boolean poblacions = this.unContacte.getPoblacions() != null;
        if (!nom) {
            facesMessages.add("Si us plau, entra un nom pel contacte.");
        }
        if (!addr) {
            facesMessages.add("Si us plau, entra una adreça pel contacte");
        }
        if (!poblacions) {
            facesMessages.add("Si us plau, entra una població pel contacte");
        }
        if (!telf) {
            facesMessages.add("Si us plau, entra un telèfon pel contacte");
        }
        if (!email) {
            facesMessages.add("Si us plau, entra un email pel contacte");
        }
        if (!url) {
            facesMessages.add("Si us plau, entra una url pel contacte");
        }
        return nom && addr && telf && email && url && poblacions;
    }

    public void nouContacte() {
        if (dadesCorrectes()) {
            if (this.unContacte.getPoblacions() != null) {
                if (this.unContacte.getPoblacions().getCp() != null) {
                    if (this.unContacte.getPoblacions().getCp().equals("-1")) {
                        this.unContacte.setPoblacions(null);
                    }
                } else {
                    this.unContacte.setPoblacions(null);
                }
            }
            EmpresesContacte ec = new EmpresesContacte();
            ec.setEmpreses(this.empresaFeta);
            this.em.persist(this.unContacte);
            this.unContacte = em.find(Contactes.class, this.unContacte.getId());
            ec.setContactes(this.unContacte);
            this.em.persist(ec);
            this.em.flush();
            this.llistaContactes.add(this.unContacte);
            log.info("Afegit a la llista de nous contactes el contacte de nom: " + this.llistaContactes.get(this.llistaContactes.size() - 1).getNom());
            ompleLlista();
            this.unContacte = new Contactes();
        }
        log.info("Contactes actuals: " + llistaContactes.size());
    }

    public String guardaContactes() {
        enviaMail();
        facesMessages.add("Ja s'ha donat d'alta la teva empresa. Ara identifica't, si us plau.");
        return "/registrat.xhtml";
    }

    public String torna() {
        return "/AltaEmpresaSeccionsComarques.xhtml";
    }

    public void esborraContacte() {
        System.err.print("BREAKPOINT 0:");
        Query q = this.em.createQuery("select ec from EmpresesContacte ec where ec.empreses = ? and ec.contactes = ?");
        q.setParameter(1, this.empresaFeta);
        q.setParameter(2, contacteSeleccionat);
        List<EmpresesContacte> empresesContacte = q.getResultList();
        Iterator<EmpresesContacte> it = empresesContacte.iterator();
        EmpresesContacte ec;
        while (it.hasNext()) {
            ec = it.next();
            ec = this.em.find(EmpresesContacte.class, ec.getId());
            this.em.remove(ec);
            log.info("Esborrant l'empresa contacte de id " + ec.getId());
            this.em.flush();
        }
        this.contacteSeleccionat = this.em.find(Contactes.class, this.contacteSeleccionat.getId());
        this.em.remove(this.contacteSeleccionat);
        this.em.flush();
        ompleLlista();
    }

    public void afegeixPoblacio() {
        if (!"".equals(poblacioNova.getNom()) && !"".equals(poblacioNova.getCp())) {
            if (poblacioNova.getCp().matches("^\\d\\d\\d\\d\\d$")) {
                System.out.println("-------------------------tot correcte!");
                insereixPoblacio(poblacioNova);
            } else {
                System.out.println("-------------------------codi postal incorrecte!");
                facesMessages.add("Si us plau, entra un codi postal de format '[0-9][0-9][0-9][0-9][0-9]' que s'avingui amb la comarca que has escollit.");
            }
        } else {
            if ("".equals(poblacioNova.getNom())) {
                System.out.println("-------------------------nom en blanc!");
                facesMessages.add("Per a afegir una nova població necessites introduir un nom per a aquesta.");
            }
            if ("".equals(poblacioNova.getCp())) {
                System.out.println("-------------------------codi postal en blanc!");
                facesMessages.add("Per a afegir una nova població necessites introduir un codi postal per a aquesta.");
            }
        }
        this.poblacioNova = new Poblacions();
    }

    private void insereixPoblacio(Poblacions p) {
        Query qPoblacio = em.createQuery("select p from Poblacions p where p.cp = ?");
        qPoblacio.setParameter(1, p.getCp());
        if (qPoblacio.getResultList().size() == 0) {
            log.info("No existia cap població amb aquest codi postal => l'afegeixo");
            em.persist(p);
        } else {
            log.warn("Ja existia una població amb aquest codi postal => NO l'afegeixo");
            facesMessages.add(FacesMessage.SEVERITY_ERROR, "Atenció! Ja existeix una població amb aquest codi postal a la llista.");
        }
        this.unContacte.setPoblacions(em.find(Poblacions.class, p.getCp()));
        llistaPoblacionsPerCP.setLlistaPoblacions(null);
    }

    public Contactes getUnContacte() {
        return this.unContacte;
    }

    public void setUnContacte(Contactes unContacte) {
        this.unContacte = unContacte;
    }

    public Empreses getEmpresaFeta() {
        return empresaFeta;
    }

    public void setEmpresaFeta(Empreses empresaFeta) {
        this.empresaFeta = empresaFeta;
    }

    public String getProva() {
        return prova;
    }

    public void setProva(String prova) {
        this.prova = prova;
    }

    public Poblacions getPoblacioNova() {
        return poblacioNova;
    }

    public void setPoblacioNova(Poblacions poblacioNova) {
        this.poblacioNova = poblacioNova;
    }

    public LlistaPoblacionsPerCP getLlistaPoblacionsPerCP() {
        return llistaPoblacionsPerCP;
    }

    public void setLlistaPoblacionsPerCP(LlistaPoblacionsPerCP llistaPoblacionsPerCP) {
        this.llistaPoblacionsPerCP = llistaPoblacionsPerCP;
    }
}
