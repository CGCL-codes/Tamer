package actividad;

import java.util.Date;
import utilidad.clasesBase.*;

public class ActividadParametros extends BaseParametros {

    public ActividadParametros() {
        inicializarComponentes();
    }

    public Integer idActividad;

    public Integer idInstalacion;

    public Integer idUnidadGestion;

    public String nombre;

    public Date fechaInicioDesde;

    public Date fechaInicioHasta;

    public Date fechaFinDesde;

    public Date fechaFinHasta;

    public String tipo;

    public String tipoCuota;

    public Boolean libre;

    public String temporada;

    public String estado2;

    public String estado3;

    public String estado4;

    public String busquedaRapida;

    public void inicializarComponentes() {
    }

    public void asignarValoresFijos(ActividadParametros paramFijos) {
        if (paramFijos != null) {
            if (paramFijos.temporada != null) {
                this.temporada = paramFijos.temporada;
            }
            if (paramFijos.estado != null) {
                this.estado = paramFijos.estado;
            }
            if (paramFijos.estado2 != null) {
                this.estado2 = paramFijos.estado2;
            }
            if (paramFijos.estado3 != null) {
                this.estado3 = paramFijos.estado3;
            }
            if (paramFijos.estado4 != null) {
                this.estado4 = paramFijos.estado4;
            }
            if (paramFijos.idInstalacion != null) {
                this.idInstalacion = paramFijos.idInstalacion;
            }
            if (paramFijos.idUnidadGestion != null) {
                this.idUnidadGestion = paramFijos.idUnidadGestion;
            }
        }
    }
}
