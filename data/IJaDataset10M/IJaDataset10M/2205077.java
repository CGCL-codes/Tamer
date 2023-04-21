package abono;

import java.util.Date;
import utilidad.clasesBase.BaseParametros;

public class AbonoParametros extends BaseParametros {

    public AbonoParametros() {
        inicializarComponentes();
    }

    public Integer idAbono;

    public Integer numero;

    public Integer serie;

    public Integer idUsuario;

    public Integer idGrupo;

    public Integer idActividad;

    public String temporadaActividad;

    public String estado1Actividad;

    public String estado2Actividad;

    public String estado3Actividad;

    public Integer idTarifa;

    public Integer idDescuento;

    public Date fechaInicioDesde;

    public Date fechaInicioHasta;

    public Date fechaFinDesde;

    public Date fechaFinHasta;

    public String tipo;

    public String busquedaRapida;

    public void inicializarComponentes() {
    }

    public void asignarValoresFijos(AbonoParametros paramFijos) {
        if (paramFijos != null) {
            if (paramFijos.estado != null) {
                this.estado = paramFijos.estado;
            }
            if (paramFijos.numero != null) {
                this.numero = paramFijos.numero;
            }
            if (paramFijos.serie != null) {
                this.serie = paramFijos.serie;
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
            if (paramFijos.estado1Actividad != null) {
                this.estado1Actividad = paramFijos.estado1Actividad;
            }
            if (paramFijos.estado2Actividad != null) {
                this.estado2Actividad = paramFijos.estado2Actividad;
            }
            if (paramFijos.estado3Actividad != null) {
                this.estado3Actividad = paramFijos.estado3Actividad;
            }
            if (paramFijos.temporadaActividad != null) {
                this.temporadaActividad = paramFijos.temporadaActividad;
            }
            if (paramFijos.idTarifa != null) {
                this.idTarifa = paramFijos.idTarifa;
            }
            if (paramFijos.idDescuento != null) {
                this.idDescuento = paramFijos.idDescuento;
            }
        }
    }
}
