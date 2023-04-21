package eventos;

import mensaje_objetos.banco.Cheque;

/**
 *
 * @author VMonsantoA
 */
public class ChequeEvent {

    public static final int NUEVO = 0;

    public static final int EDITAR = 1;

    public static final int VISUALIZAR = 2;

    public static final int SELECCIONAR_CHEQUE = 3;

    private Cheque cheque;

    private int id;

    /** Creates a new instance of BancoEvent */
    public ChequeEvent() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cheque getCheque() {
        return cheque;
    }

    public void setCheque(Cheque cheque) {
        this.cheque = cheque;
    }
}
