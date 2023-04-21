package org.gestionabierta.dominio.modelo.presupuesto.causado;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.gestionabierta.dominio.modelo.presupuesto.Asiento;

/**
 *
 * @author Franky Villadiego
 * @author Valdemar Sotillo
 * 
 */
@Entity
@DiscriminatorValue(value = "ORDEN DE PAGO")
public class OrdenPago extends Asiento {

    private String tipo = "CA";

    public OrdenPago() {
    }

    public String getTipo() {
        return tipo;
    }
}
