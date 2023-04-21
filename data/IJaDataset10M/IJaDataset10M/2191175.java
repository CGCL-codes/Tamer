package mx.ipn.to;

import java.util.Date;

public class BitacoraChoferUnidadTO extends TransferObject {

    private int idOperacion;

    private java.util.Date fecha;

    private int idChofer;

    private int idUnidad;

    private Integer[] listIdOperacion;

    private java.util.Date[] listfechas;

    private ChoferTO[] listChoferTO;

    private UnidadTO[] listUnidadTO;

    public void setIdOperacion(int idOperacion) {
        this.idOperacion = idOperacion;
    }

    public int getIdOperacion() {
        return idOperacion;
    }

    public void setFecha(java.util.Date fecha) {
        this.fecha = fecha;
    }

    public java.util.Date getFecha() {
        return fecha;
    }

    public void setIdChofer(int idChofer) {
        this.idChofer = idChofer;
    }

    public int getIdChofer() {
        return idChofer;
    }

    public void setIdUnidad(int idUnidad) {
        this.idUnidad = idUnidad;
    }

    public int getIdUnidad() {
        return idUnidad;
    }

    public Integer[] getListIdOperacion() {
        return listIdOperacion;
    }

    public void setListIdOperacion(Integer[] listIdOperacion) {
        this.listIdOperacion = listIdOperacion;
    }

    public java.util.Date[] getListFecha() {
        return listfechas;
    }

    public void setListFecha(java.util.Date[] listfechas) {
        this.listfechas = listfechas;
    }

    public UnidadTO[] getListUnidadTO() {
        return listUnidadTO;
    }

    public ChoferTO[] getListChoferTO() {
        return listChoferTO;
    }

    public void setListChoferTO(ChoferTO[] listChoferTO) {
        this.listChoferTO = listChoferTO;
    }

    public void setListUnidadTO(UnidadTO[] listUnidadTO) {
        this.listUnidadTO = listUnidadTO;
    }

    public BitacoraChoferUnidadTO() {
        super();
        this.idOperacion = -1;
        this.fecha = null;
        this.idChofer = -1;
        this.idUnidad = -1;
        this.listIdOperacion = null;
        this.listfechas = null;
        this.listUnidadTO = null;
        this.listChoferTO = null;
    }

    public BitacoraChoferUnidadTO(int idOperacion, Date fecha, ChoferTO[] listChoferTO, UnidadTO[] listUnidadTO) {
        super();
        this.idOperacion = idOperacion;
        this.fecha = fecha;
        this.idChofer = -1;
        this.idUnidad = -1;
        this.listIdOperacion = null;
        this.listfechas = null;
        this.listUnidadTO = listUnidadTO;
        this.listChoferTO = listChoferTO;
    }

    public BitacoraChoferUnidadTO(Integer[] listIdOperacion, Date[] listfechas, ChoferTO[] listChoferTO, UnidadTO[] listUnidadTO) {
        super();
        this.idOperacion = -1;
        this.fecha = null;
        this.idChofer = -1;
        this.idUnidad = -1;
        this.listIdOperacion = listIdOperacion;
        this.listfechas = listfechas;
        this.listUnidadTO = listUnidadTO;
        this.listChoferTO = listChoferTO;
    }
}
