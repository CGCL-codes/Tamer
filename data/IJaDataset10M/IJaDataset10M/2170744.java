package parsing.extraerDatosHDF;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class DatosFecha implements Serializable {

    private Date fecha;

    private int agua;

    private int nieve;

    private int total;

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getAgua() {
        return agua;
    }

    public void setAgua(int agua) {
        this.agua = agua;
    }

    public int getNieve() {
        return nieve;
    }

    public void setNieve(int nieve) {
        this.nieve = nieve;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
