package unidadGestion;

import utilidad.clasesBase.*;

public class UnidadGestionParametros extends BaseParametros {

    public UnidadGestionParametros() {
        inicializarComponentes();
    }

    public Integer idUnidadGestion;

    public Integer idCentroGestion;

    public String nombre;

    public String busquedaRapida;

    public void inicializarComponentes() {
    }

    public void asignarValoresFijos(UnidadGestionParametros paramFijos) {
        if (paramFijos != null) {
            if (paramFijos.estado != null) {
                this.estado = paramFijos.estado;
            }
            if (paramFijos.idCentroGestion != null) {
                this.idCentroGestion = paramFijos.idCentroGestion;
            }
        }
    }
}
