package sistema.clases;

import de.uni_paderborn.tools.fca.*;
import java.util.*;
import de.uni_paderborn.fujaba.sdm.*;

/**
 * UMLClass: 'PedidoEdicionImpresa'
 */
public class PedidoEdicionImpresa extends Pedido {

    /**
    * <pre>
    *                 /\  1                Assoc                 n 
    * EdicionImpresa <  >------------------------------------------ PedidoEdicionImpresa
    *                 \/  edicionImpresa      pedidoEdicionImpresa 
    * </pre>
    */
    private EdicionImpresa edicionImpresa;

    /**
    * UMLMethod: '+ getEdicionImpresa () : EdicionImpresa'
    */
    public EdicionImpresa getEdicionImpresa() {
        return this.edicionImpresa;
    }

    /**
    * UMLMethod: '+ removeYou () : Void'
    */
    public void removeYou() {
        super.removeYou();
        EdicionImpresa tmpEdicionImpresa = getEdicionImpresa();
        if (tmpEdicionImpresa != null) {
            setEdicionImpresa(null);
        }
    }

    /**
    * UMLMethod: '+ setEdicionImpresa (value : EdicionImpresa) : Boolean'
    */
    public boolean setEdicionImpresa(EdicionImpresa value) {
        boolean changed = false;
        if (this.edicionImpresa != value) {
            if (this.edicionImpresa != null) {
                EdicionImpresa oldValue = this.edicionImpresa;
                this.edicionImpresa = null;
                oldValue.removeFromPedidoEdicionImpresa(this);
            }
            this.edicionImpresa = value;
            if (value != null) {
                value.addToPedidoEdicionImpresa(this);
            }
            changed = true;
        }
        return changed;
    }

    public String toString() {
        return "\"" + edicionImpresa.getTitulo() + "\" - " + fechaPedido.toString() + " - Cantidad: " + getCantidad();
    }
}
