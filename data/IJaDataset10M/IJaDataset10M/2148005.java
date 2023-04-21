package persistence;

import java.util.Date;

/**
 *
 * @author alfredo
 */
public class Movimiento {

    String referencia;

    float monto;

    Date fecha;

    int id;

    public int getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public void parse(String data) {
        int index = 0;
        String sr, sf, sm, sid;
        sr = data.substring(index, (index = data.indexOf(';', index)));
        sf = data.substring(++index, (index = data.indexOf(';', index)));
        sm = data.substring(++index, (index = data.indexOf(';', index)));
        sid = data.substring(++index, (index = data.indexOf(';', index)));
        referencia = sr;
        fecha = new Date(Long.parseLong(sf));
        monto = Float.parseFloat(sm);
        id = Integer.parseInt(sid);
    }

    public String toString() {
        return referencia + ";" + fecha.getTime() + ";" + monto + ";";
    }
}
