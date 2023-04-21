package Persistencia.Entidades;

import Persistencia.ExpertosPersistencia.Criterio;
import Persistencia.ExpertosPersistencia.FachadaInterna;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author diego
 */
public class DenunciaAgente extends CasoAgente implements Denuncia {

    private String oidReclamo;

    private List<String> listaOidFallaTecnica;

    private boolean reclamoBuscado;

    private boolean denunciaEstadoBuscado;

    private boolean fallaTecnicaBuscado;

    public int getcodigoDenuncia() {
        return getImplementacion().getcodigoDenuncia();
    }

    public float getprioridad() {
        return getImplementacion().getprioridad();
    }

    public void setcodigoDenuncia(int newVal) {
        getImplementacion().setcodigoDenuncia(newVal);
    }

    public void setprioridad(float newVal) {
        getImplementacion().setprioridad(newVal);
    }

    public List<Reclamo> getReclamo() {
        if (isReclamoBuscado() == false) {
            List<Criterio> listaDeCriterio = new ArrayList<Criterio>();
            listaDeCriterio.add(FachadaInterna.getInstancia().crearCriterio("oidDenuncia", "=", super.getOid()));
            List<SuperDruperInterfaz> listaDeInterfaces = FachadaInterna.getInstancia().buscar("Reclamo", listaDeCriterio);
            List<Reclamo> listaReclamo = new ArrayList<Reclamo>();
            for (SuperDruperInterfaz aux : listaDeInterfaces) {
                listaReclamo.add((Reclamo) aux);
            }
            getImplementacion().setReclamo(listaReclamo);
            setReclamoBuscado(true);
        }
        return getImplementacion().getReclamo();
    }

    public void setReclamo(List<Reclamo> reclamo) {
        getImplementacion().setReclamo(reclamo);
        reclamoBuscado = true;
    }

    public List<DenunciaEstado> getDenunciaEstado() {
        if (isDenunciaEstadoBuscado() == false) {
            List<Criterio> listaDeCriterio = new ArrayList<Criterio>();
            listaDeCriterio.add(FachadaInterna.getInstancia().crearCriterio("OIDDenuncia", "=", super.getOid()));
            List<SuperDruperInterfaz> listaDencunaEstados = FachadaInterna.getInstancia().buscar("DenunciaEstado", listaDeCriterio);
            for (SuperDruperInterfaz denunciaEstado : listaDencunaEstados) {
                getImplementacion().addDenunciaEstado((DenunciaEstado) denunciaEstado);
            }
        }
        return getImplementacion().getDenunciaEstado();
    }

    public void setDenunciaEstado(List<DenunciaEstado> denunciaEstado) {
        getImplementacion().setDenunciaEstado(denunciaEstado);
        denunciaEstadoBuscado = true;
    }

    public List<FallaTecnica> getFallasTecnica() {
        if (isFallaTecnicaBuscado() == false) {
            List<Criterio> listacCriterios = new ArrayList<Criterio>();
            listacCriterios.add(FachadaInterna.getInstancia().crearCriterio("Denuncia", "=", this));
            List<FallaTecnica> listaFallas = new ArrayList<FallaTecnica>();
            for (SuperDruperInterfaz falla : FachadaInterna.getInstancia().buscar("FallaTecnica", listacCriterios)) {
                listaFallas.add((FallaTecnica) falla);
            }
            getImplementacion().setFallasTecnica(listaFallas);
            fallaTecnicaBuscado = true;
        }
        return getImplementacion().getFallasTecnica();
    }

    public void setFallasTecnica(List<FallaTecnica> fallaTecnica) {
        getImplementacion().setFallasTecnica(fallaTecnica);
    }

    /**
     * @return the implementacion
     */
    @Override
    public DenunciaImplementacion getImplementacion() {
        return (DenunciaImplementacion) super.getImplementacion();
    }

    /**
     * @param implementacion the implementacion to set
     */
    public void setImplementacion(DenunciaImplementacion implementacion) {
        super.setImplementacion(implementacion);
    }

    /**
     * @return the oidReclamo
     */
    public String getOidReclamo() {
        return oidReclamo;
    }

    /**
     * @param oidReclamo the oidReclamo to set
     */
    public void setOidReclamo(String oidReclamo) {
        this.oidReclamo = oidReclamo;
    }

    /**
     * @return the reclamoBuscado
     */
    public boolean isReclamoBuscado() {
        return reclamoBuscado;
    }

    public void addOidFallaTecnica(String oidFallaTecnica) {
        if (listaOidFallaTecnica == null) {
            listaOidFallaTecnica = new ArrayList<String>();
        }
        listaOidFallaTecnica.add(oidFallaTecnica);
    }

    /**
     * @param reclamoBuscado the reclamoBuscado to set
     */
    public void setReclamoBuscado(boolean reclamoBuscado) {
        this.reclamoBuscado = reclamoBuscado;
    }

    /**
     * @return the denunciaEstadoBuscado
     */
    public boolean isDenunciaEstadoBuscado() {
        return denunciaEstadoBuscado;
    }

    /**
     * @param denunciaEstadoBuscado the denunciaEstadoBuscado to set
     */
    public void setDenunciaEstadoBuscado(boolean denunciaEstadoBuscado) {
        this.denunciaEstadoBuscado = denunciaEstadoBuscado;
    }

    /**
     * @return the fallaTecnicaBuscado
     */
    public boolean isFallaTecnicaBuscado() {
        return fallaTecnicaBuscado;
    }

    /**
     * @param fallaTecnicaBuscado the fallaTecnicaBuscado to set
     */
    public void setFallaTecnicaBuscado(boolean fallaTecnicaBuscado) {
        this.fallaTecnicaBuscado = fallaTecnicaBuscado;
    }

    public void agregarDenunciaEstado(DenunciaEstado denEstado) {
        ((DenunciaEstadoAgente) denEstado).setOidDenuncia(getOid());
        getImplementacion().agregarDenunciaEstado(denEstado);
    }
}
