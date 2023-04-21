package cuota.devolucion;

import java.util.Date;
import utilidad.clasesBase.*;

public class DevolucionCuotaParametros extends BaseParametros {

    public DevolucionCuotaParametros() {
        inicializarComponentes();
    }

    public Integer idDevolucionCuota;

    public Integer idCuota;

    public Integer idUsuario;

    public Integer idGrupo;

    public Integer idActividad;

    public Date fechaDesde;

    public Date fechaHasta;

    public String busquedaRapida;

    public void inicializarComponentes() {
    }

    public void asignarValoresFijos(DevolucionCuotaParametros paramFijos) {
        if (paramFijos != null) {
            if (paramFijos.estado != null) {
                this.estado = paramFijos.estado;
            }
            if (paramFijos.idUsuario != null) {
                this.idUsuario = paramFijos.idUsuario;
            }
            if (paramFijos.idGrupo != null) {
                this.idGrupo = paramFijos.idGrupo;
            }
            if (paramFijos.idActividad != null) {
                this.idActividad = paramFijos.idActividad;
            }
            if (paramFijos.ordenSQL != null) {
                this.ordenSQL = paramFijos.ordenSQL;
            }
        }
    }
}
