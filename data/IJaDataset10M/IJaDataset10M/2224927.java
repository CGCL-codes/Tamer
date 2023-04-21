package servidor.log.impl.eventos;

import java.io.DataInput;
import java.io.IOException;
import java.util.SortedSet;
import servidor.catalog.Catalogo;
import servidor.log.LSN;
import servidor.log.Log;
import servidor.log.impl.LogHelper;
import servidor.log.impl.estructuras.DatoTransaccion;
import servidor.log.impl.estructuras.OutputAnalisis;
import servidor.transaccion.Estado;
import servidor.transaccion.Transaccion;

public class EventoTransaccion extends Evento {

    protected LSN prevLSN;

    protected Transaccion.ID idTransaccion;

    /**
	 * @see servidor.log.impl.eventos.Evento#realizarAnalisis(servidor.log.impl.estructuras.OutputAnalisis, servidor.log.LSN)
	 */
    @Override
    public void realizarAnalisis(OutputAnalisis outputAnalisis, LSN lsnActual) {
        if (!outputAnalisis.transTable.containsKey(this.idTransaccion)) {
            DatoTransaccion datoTransaccion = new DatoTransaccion();
            datoTransaccion.idTransaccion = this.idTransaccion;
            datoTransaccion.estado = Estado.EN_CURSO;
            datoTransaccion.lastLSN = lsnActual;
            datoTransaccion.undoNextLSN.add(this.prevLSN);
            outputAnalisis.transTable.put(datoTransaccion.idTransaccion, datoTransaccion);
        }
        switch(this.operacion) {
            case ROLLBACK:
                {
                    DatoTransaccion datoTransaccion = outputAnalisis.transTable.get(this.idTransaccion);
                    datoTransaccion.estado = Estado.EN_CURSO;
                    datoTransaccion.lastLSN = lsnActual;
                    break;
                }
            case END:
                {
                    outputAnalisis.transTable.remove(this.idTransaccion);
                    break;
                }
        }
    }

    /**
	 * @see servidor.log.impl.eventos.Evento#realizarUndo(servidor.log.LSN, servidor.log.impl.estructuras.DatoTransaccion, servidor.log.Log)
	 */
    @Override
    public void realizarUndo(LSN undoLSNActual, DatoTransaccion datoTransaccion, Log log) {
        datoTransaccion.undoNextLSN.remove(undoLSNActual);
        datoTransaccion.undoNextLSN.add(this.prevLSN);
    }

    /**
	 * @see servidor.log.impl.eventos.Evento#realizarRollback(servidor.transaccion.Transaccion, java.util.SortedSet, servidor.log.Log)
	 */
    @Override
    public void realizarRollback(Transaccion transaccion, SortedSet<LSN> undoNextLSNs, Log log) {
        undoNextLSNs.add(this.prevLSN);
    }

    @Override
    public void leerEvento(DataInput lector) throws IOException {
        byte[] longitudEventoBytes = new byte[Catalogo.LONGITUD_INT];
        lector.readFully(longitudEventoBytes);
        this.longitud = LogHelper.byteArrayAEntero(longitudEventoBytes) + Catalogo.LONGITUD_LONG;
        byte[] idTxBytes = new byte[Catalogo.LONGITUD_INT];
        lector.readFully(idTxBytes);
        this.idTransaccion = LogHelper.byteArrayAIdTransaccion(idTxBytes);
        byte[] prevLSNBytes = new byte[Catalogo.LONGITUD_LONG];
        lector.readFully(prevLSNBytes);
        this.prevLSN = LogHelper.byteArrayALSN(prevLSNBytes);
    }

    @Override
    public String toString() {
        return super.toString() + "|TX:" + this.idTransaccion + "|PrevLSN:" + this.prevLSN;
    }
}
