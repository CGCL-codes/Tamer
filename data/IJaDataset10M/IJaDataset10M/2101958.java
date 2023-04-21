package Persistencia.Entidades;

import Persistencia.ExpertosPersistencia.FachadaInterna;

/**
 *
 * @author diego
 */
public class DenuncianteAgente extends ObjetoPersistente implements Denunciante {

    private DenuncianteImplementacion implementacion;

    private String oidPersonaPadron;

    private boolean personaPadronBuscado;

    public String getcelular() {
        return getImplementacion().getcelular();
    }

    public String getdomicilio() {
        return getImplementacion().getdomicilio();
    }

    public String getemail() {
        return getImplementacion().getemail();
    }

    public String gettelefonofijo() {
        return getImplementacion().gettelefonofijo();
    }

    public void setcelular(String newVal) {
        getImplementacion().setcelular(newVal);
    }

    public void setdomicilio(String newVal) {
        getImplementacion().setdomicilio(newVal);
    }

    public void setemail(String newVal) {
        getImplementacion().setemail(newVal);
    }

    public void settelefonofijo(String newVal) {
        getImplementacion().settelefonofijo(newVal);
    }

    public PersonaPadron getPersonaPadron() {
        if (isPersonaPadronBuscado() == false) {
            implementacion.setPersonaPadron((PersonaPadron) FachadaInterna.getInstancia().buscar("PersonaPadron", oidPersonaPadron));
            setPersonaPadronBuscado(true);
        }
        return getImplementacion().getPersonaPadron();
    }

    public void setPersonaPadron(PersonaPadron personaPadron) {
        getImplementacion().setPersonaPadron(personaPadron);
        personaPadronBuscado = true;
        oidPersonaPadron = ((PersonaPadronAgente) personaPadron).getOid();
    }

    /**
     * @return the oidPersonaPadron
     */
    public String getOidPersonaPadron() {
        return oidPersonaPadron;
    }

    /**
     * @param oidPersonaPadron the oidPersonaPadron to set
     */
    public void setOidPersonaPadron(String oidPersonaPadron) {
        this.oidPersonaPadron = oidPersonaPadron;
    }

    /**
     * @return the implementacion
     */
    public DenuncianteImplementacion getImplementacion() {
        return implementacion;
    }

    /**
     * @param implementacion the implementacion to set
     */
    public void setImplementacion(DenuncianteImplementacion implementacion) {
        this.implementacion = implementacion;
    }

    /**
     * @return the personaPadronBuscado
     */
    public boolean isPersonaPadronBuscado() {
        return personaPadronBuscado;
    }

    /**
     * @param personaPadronBuscado the personaPadronBuscado to set
     */
    public void setPersonaPadronBuscado(boolean personaPadronBuscado) {
        this.personaPadronBuscado = personaPadronBuscado;
    }

    public boolean isEstadoBaja() {
        return implementacion.isEstadoBaja();
    }

    public void setEstadoBaja(boolean estadoBaja) {
        implementacion.setEstadoBaja(estadoBaja);
    }
}
