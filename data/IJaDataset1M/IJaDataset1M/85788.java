package com.siegre.action;

import java.util.List;
import javax.persistence.EntityManager;
import com.siegre.model.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

/**
 * Clase idiomapersonaHome, session bean que implementa la logica de negocio para la entity idiomapersona
 * inserta, actualiza, consulta y elimina regsitro de la tabla
 * además contiene métodos con comportamientos específicos
 *
 * @author Rafael Ortega
 * @author Mayer Monsalve
 * @version 1.0 Build 10 Diciembre 15, 2011
 */
@Name("idiomapersonaHome")
@Scope(ScopeType.CONVERSATION)
public class IdiomapersonaHome extends EntityHome<Idiomapersona> {

    private static final long serialVersionUID = 9126410779163156573L;

    @In(create = true)
    IdiomaHome idiomaHome;

    @In(create = true)
    PersonanaturalHome personanaturalHome;

    @DataModel
    private List<Idiomapersona> listaIP;

    @DataModelSelection
    private Idiomapersona idiomapersonaSel;

    @In
    EntityManager entityManager;

    @In
    private FacesMessages facesMessages;

    /** Metodo sobrescrito de la clase EntityHome - persist()
     * prepara el entity
     * inserta un registro en la tabla
     * coloca un facesMessage si la insercion fue exitosa
     *
     * @return String contiene "persisted" si fue exitoso
     */
    @Override
    public String persist() {
        getInstance().preparar(personanaturalHome.getInstance());
        String exit = super.persist();
        limpiar();
        facesMessages.clear();
        facesMessages.add("Agregación Exitosa");
        return exit;
    }

    /** Metodo sobrescrito de la clase EntityHome - update()
     * prepara el entity
     * actualiza un registro en la tabla
     * coloca un facesMessage si la actualizacion fue exitosa
     *
     * @return String contiene "updated" si fue exitoso
     */
    @Override
    public String update() {
        getInstance().preparar(personanaturalHome.getInstance());
        String exit = super.update();
        limpiar();
        facesMessages.clear();
        facesMessages.add("Edición Exitosa");
        return exit;
    }

    /** Metodo sobrescrito de la clase EntityHome - remove()
     * borra un registro en la tabla
     * coloca un facesMessage si el borrado fue exitoso
     *
     * @return String contiene "removed" si fue exitoso
     */
    @Override
    public String remove() {
        String exit = super.remove();
        facesMessages.clear();
        facesMessages.add("Eliminación Exitosa");
        return exit;
    }

    public void editar() {
        setInstance(idiomapersonaSel);
    }

    public void borrar() {
        setInstance(idiomapersonaSel);
        remove();
        limpiar();
    }

    public void setIdiomapersonaIdpeId(Long id) {
        setId(id);
    }

    public Long getIdiomapersonaIdpeId() {
        return (Long) getId();
    }

    @Override
    protected Idiomapersona createInstance() {
        Idiomapersona idiomapersona = new Idiomapersona();
        return idiomapersona;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();
        Idioma idioma = idiomaHome.getDefinedInstance();
        if (idioma != null) {
            getInstance().setIdioma(idioma);
        }
        Personanatural personanatural = personanaturalHome.getDefinedInstance();
        if (personanatural != null) {
            getInstance().setPersonanatural(personanatural);
        }
    }

    public boolean isWired() {
        if (getInstance().getIdioma() == null) return false;
        if (getInstance().getPersonanatural() == null) return false;
        return true;
    }

    public Idiomapersona getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    @Factory("listaIP")
    @SuppressWarnings("unchecked")
    public List<Idiomapersona> getListaIP() {
        listaIP = entityManager.createQuery("select idiomapersona from Idiomapersona idiomapersona where idiomapersona.personanatural.penaId=:id").setParameter("id", personanaturalHome.getInstance().getPenaId()).getResultList();
        return listaIP;
    }

    public void limpiar() {
        setInstance(new Idiomapersona());
    }

    public boolean isEmptyListIP() {
        return getListaIP().isEmpty();
    }
}
