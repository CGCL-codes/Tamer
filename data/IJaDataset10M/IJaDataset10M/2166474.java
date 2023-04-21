package sistema.clases;

import de.uni_paderborn.tools.fca.*;
import java.util.*;
import de.uni_paderborn.fujaba.sdm.*;

/**
 * UMLClass: 'DetalleRemito'
 */
public class DetalleRemito {

    private int detalleRemito_id;

    public void setDetalleRemito_id(int id) {
        detalleRemito_id = id;
    }

    public int getDetalleRemito_id() {
        return detalleRemito_id;
    }

    /**
    * UMLAttribute: ' cantidad : Integer'
    */
    private int cantidad;

    /**
    * <pre>
    *                 1             venta             n 
    * EdicionImpresa ----------------------------------- DetalleRemito
    *                 edicionImpresa      detalleRemito 
    * </pre>
    */
    private EdicionImpresa edicionImpresa;

    /**
    * <pre>
    *           1          venta           n 
    * Ejemplar ------------------------------ DetalleRemito
    *           ejemplar      detalleFactura 
    * </pre>
    */
    private Ejemplar ejemplar;

    /**
    * UMLAttribute: 'iSBN : ISBN'
    */
    private ISBN iSBN;

    /**
    * UMLAttribute: ' precioUnitario : Float'
    */
    private float precioUnitario;

    /**
    * <pre>
    *         /\  1         tiene         n 
    * Remito <  >--------------------------- DetalleRemito
    *         \/  remito      detalleRemito 
    * </pre>
    */
    private Remito remito;

    /**
    * UMLAttribute: ' subTotal : Float'
    */
    private float subTotal;

    /**
    * UMLMethod: 'Read access method for attribute cantidad : Integer'
    */
    public int getCantidad() {
        return cantidad;
    }

    /**
    * UMLMethod: '+ getEdicionImpresa () : EdicionImpresa'
    */
    public EdicionImpresa getEdicionImpresa() {
        return this.edicionImpresa;
    }

    /**
    * UMLMethod: '+ getEjemplar () : Ejemplar'
    */
    public Ejemplar getEjemplar() {
        return this.ejemplar;
    }

    /**
    * UMLMethod: 'Read access method for attribute precioUnitario : Float'
    */
    public float getPrecioUnitario() {
        return precioUnitario;
    }

    /**
    * UMLMethod: '+ getRemito () : Remito'
    */
    public Remito getRemito() {
        return this.remito;
    }

    /**
    * UMLMethod: 'Read access method for attribute subTotal : Float'
    */
    public float getSubTotal() {
        return subTotal;
    }

    /**
    * UMLMethod: '+ removeYou () : Void'
    */
    public void removeYou() {
        Remito tmpRemito = getRemito();
        if (tmpRemito != null) {
            setRemito(null);
        }
        EdicionImpresa tmpEdicionImpresa = getEdicionImpresa();
        if (tmpEdicionImpresa != null) {
            setEdicionImpresa(null);
        }
        Ejemplar tmpEjemplar = getEjemplar();
        if (tmpEjemplar != null) {
            setEjemplar(null);
        }
    }

    /**
    * UMLMethod: 'Write access method for attribute cantidad : Integer'
    */
    public int setCantidad(int cantidad) {
        if (this.cantidad != cantidad) {
            this.cantidad = cantidad;
        }
        return this.cantidad;
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
                oldValue.removeFromDetalleRemito(this);
            }
            this.edicionImpresa = value;
            if (value != null) {
                value.addToDetalleRemito(this);
            }
            changed = true;
        }
        return changed;
    }

    /**
    * UMLMethod: '+ setEjemplar (value : Ejemplar) : Boolean'
    */
    public boolean setEjemplar(Ejemplar value) {
        boolean changed = false;
        if (this.ejemplar != value) {
            if (this.ejemplar != null) {
                Ejemplar oldValue = this.ejemplar;
                this.ejemplar = null;
                oldValue.removeFromDetalleFactura(this);
            }
            this.ejemplar = value;
            if (value != null) {
                value.addToDetalleFactura(this);
            }
            changed = true;
        }
        return changed;
    }

    /**
    * UMLMethod: 'Write access method for attribute precioUnitario : Float'
    */
    public float setPrecioUnitario(float precioUnitario) {
        if (this.precioUnitario != precioUnitario) {
            this.precioUnitario = precioUnitario;
        }
        return this.precioUnitario;
    }

    /**
    * UMLMethod: '+ setRemito (value : Remito) : Boolean'
    */
    public boolean setRemito(Remito value) {
        boolean changed = false;
        if (this.remito != value) {
            if (this.remito != null) {
                Remito oldValue = this.remito;
                this.remito = null;
                oldValue.removeFromDetalleRemito(this);
            }
            this.remito = value;
            if (value != null) {
                value.addToDetalleRemito(this);
            }
            changed = true;
        }
        return changed;
    }

    /**
    * UMLMethod: 'Write access method for attribute subTotal : Float'
    */
    public float setSubTotal(float subTotal) {
        if (this.subTotal != subTotal) {
            this.subTotal = subTotal;
        }
        return this.subTotal;
    }

    public void calcularSubTotal() {
        setSubTotal(getCantidad() * getPrecioUnitario());
    }

    public String toString() {
        String resultado = "";
        calcularSubTotal();
        if (!(getEdicionImpresa() == null)) {
            resultado = getCantidad() + "  " + getEdicionImpresa().toString() + "  " + getSubTotal();
        } else if (!(getEjemplar() == null)) {
            resultado = getCantidad() + "  " + getEjemplar().toString() + "  " + getSubTotal();
        }
        return resultado;
    }

    public void ponerEnStock() {
        if (!(getEdicionImpresa() == null)) {
            getEdicionImpresa().setStock(getEdicionImpresa().getStock() + getCantidad());
        } else if (!(getEjemplar() == null)) {
            getEjemplar().setStock(getEjemplar().getStock() + getCantidad());
        }
    }

    public boolean sacarDeStock() {
        if (!(getEdicionImpresa() == null)) {
            if (getEdicionImpresa().getStock() < getCantidad()) {
                return false;
            } else {
                getEdicionImpresa().setStock(getEdicionImpresa().getStock() - getCantidad());
                return true;
            }
        } else if (!(getEjemplar() == null)) {
            if (getEjemplar().getStock() < getCantidad()) {
                return false;
            } else {
                getEjemplar().setStock(getEjemplar().getStock() - getCantidad());
                return true;
            }
        } else {
            return false;
        }
    }
}
