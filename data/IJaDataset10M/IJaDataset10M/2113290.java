package eventos;

import mensaje_objetos.cliente.GrupoCliente;
import mensaje_objetos.recibocaja.ReciboCaja;

/**
 *
 * @author carlos
 */
public class ReciboCajaEvent {

    public static final int NUEVO = 0;

    public static final int EDITAR = 1;

    public static final int VISUALIZAR = 2;

    public static final int SELECCIONAR_RECIBO_CAJA = 3;

    private ReciboCaja reciboCaja;

    private int Id;

    public ReciboCaja getReciboCaja() {
        return reciboCaja;
    }

    public void setReciboCaja(ReciboCaja reciboCaja) {
        this.reciboCaja = reciboCaja;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }
}
