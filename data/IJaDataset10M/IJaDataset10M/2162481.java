package servidor.log.impl.estructuras;

import java.util.Date;
import java.util.SortedSet;
import servidor.log.LSN;
import servidor.transaccion.Aislamiento;
import servidor.transaccion.Estado;
import servidor.transaccion.Transaccion;

public final class DatoTransaccion2Transaccion implements Transaccion {

    private DatoTransaccion datoTransaccion;

    public DatoTransaccion2Transaccion(DatoTransaccion datoTransaccion) {
        this.datoTransaccion = datoTransaccion;
    }

    public Aislamiento aislamiento() {
        throw new UnsupportedOperationException("DatoTransaccion2Transaccion.aislamiento()");
    }

    public void establecerUltimoLSN(LSN lsn) {
        this.datoTransaccion.lastLSN = lsn;
    }

    public Estado estado() {
        return this.datoTransaccion.estado;
    }

    public Date fechaInicio() {
        throw new UnsupportedOperationException("DatoTransaccion2Transaccion.fechaInicio()");
    }

    public ID id() {
        return this.datoTransaccion.idTransaccion;
    }

    public Transaccion padre() {
        throw new UnsupportedOperationException("DatoTransaccion2Transaccion.padre()");
    }

    public Thread threadPropietario() {
        throw new UnsupportedOperationException("DatoTransaccion2Transaccion.propietario()");
    }

    public LSN ultimoLSN() {
        return this.datoTransaccion.lastLSN;
    }

    public SortedSet<LSN> undoNextLSN() {
        return this.datoTransaccion.undoNextLSN;
    }

    public LSN dameSavepoint(String nombreSavepoint) {
        throw new UnsupportedOperationException("DatoTransaccion2Transaccion.dameSavepoint()");
    }

    public void establecerSavepoint(String nombreSavepoint) {
        throw new UnsupportedOperationException("DatoTransaccion2Transaccion.establecerSavepoint()");
    }

    public void establecerUndoNextLSN(LSN lsn) {
        throw new UnsupportedOperationException("DatoTransaccion2Transaccion.establecerUndoNextLSN()");
    }
}
