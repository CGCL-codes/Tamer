package ar.com.mludeiro.hermes.bo.mercaderia;

import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import javax.swing.JOptionPane;
import ar.com.mludeiro.hermes.db.mercaderia.Articulo;
import ar.com.mludeiro.hermes.db.mercaderia.Elemento;
import com.certesystems.swingforms.Action;
import com.certesystems.swingforms.Enlace;
import com.certesystems.swingforms.exceptions.WarningException;
import com.certesystems.swingforms.forms.states.FormStateSelect;
import com.certesystems.swingforms.grid.Grid;
import com.certesystems.swingforms.grid.actions.GridAction;

public class AccionCambiarCostoElemento extends GridAction {

    public AccionCambiarCostoElemento() {
        super(Action.LEVEL_UPDATE, "Modificar Costo", "/ar/com/mludeiro/hermes/bo/images/price.gif", KeyEvent.VK_F6);
    }

    public void doGridAction(Grid grid) throws Exception {
        String value = JOptionPane.showInputDialog(null, "Cambio de Costo", "Ingrese el nuevo costo del elemento", JOptionPane.QUESTION_MESSAGE);
        if (value != null) {
            try {
                BigDecimal precio = new BigDecimal(value).setScale(2);
                Elemento elem = (Elemento) grid.getRegister();
                if (elem.getCostoActual() != null && precio.equals(elem.getCostoActual().getPrecioActual())) {
                    throw new WarningException("El elemento ya tenía ese costo");
                }
                elem.changePrice(precio);
                elem.getObjectContext().commitChanges();
                if (grid.getForm() != null) {
                    grid.getForm().setState(new FormStateSelect());
                }
                for (Enlace enlace : grid.getEnlaces()) {
                    enlace.asyncRefresh();
                }
            } catch (NumberFormatException nfe) {
                throw new WarningException("Costo ingresado no válido");
            }
        }
    }

    public int getActionGroup() {
        return Action.GROUP_CUSTOM;
    }

    public boolean isAllowed(Object source) {
        return true;
    }
}
