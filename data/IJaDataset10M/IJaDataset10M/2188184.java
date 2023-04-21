package eventos;

import mensaje_objetos.cliente.GrupoCliente;
import mensaje_objetos.cliente.Pais;

/**
 *
 * @author carlos
 */
public class PaisEvent {

    public static final int NUEVO = 0;

    public static final int EDITAR = 1;

    public static final int VISUALIZAR = 2;

    public static final int SELECCIONAR_PAIS = 3;

    private Pais pais;

    private int Id;

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }
}
