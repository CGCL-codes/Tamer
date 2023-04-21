package fondefitco.Vista.Controlador;

import fondefitco.Controlador.ValidarorVistas;
import javax.swing.JTextField;

/**
 *
 * @author a103
 */
public class GestorVista_Consultarenbd {

    ValidarorVistas validador = new ValidarorVistas();

    public boolean verificampo(JTextField... campos) {
        return validador.CamposVacios(campos);
    }

    /**
     * Metodo que me permite borrar toda la interfaz, y coloca el focus en la posicion inicial.
     * este metodo tiene en cuenta cual es el primer campo que se envia para poder entregar
     * el focus.
     */
    public void RetornaColor(JTextField... campos) {
        validador.RetornaColor(campos);
        validador.VaciarCampos(campos);
        campos[0].requestFocus();
    }
}
