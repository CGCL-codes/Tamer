package servidor.log.impl.estructuras;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import servidor.log.LSN;
import servidor.tabla.Registro;
import servidor.transaccion.Estado;
import servidor.transaccion.Transaccion;

public class DatoTransaccion {

    public Transaccion.ID idTransaccion;

    public Estado estado;

    public LSN lastLSN;

    public SortedSet<LSN> undoNextLSN;

    public Set<Registro.ID> registrosBloqueados;

    public DatoTransaccion() {
        this.undoNextLSN = new TreeSet<LSN>();
        this.registrosBloqueados = new HashSet<Registro.ID>();
    }

    @Override
    public String toString() {
        return "LastLSN:" + lastLSN + "-UndoNextLSN:" + undoNextLSN + "-RegLocked:" + registrosBloqueados;
    }
}
