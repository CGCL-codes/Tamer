package com.organizadordeeventos.dto.servicio;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>Contiene los la informacion que todos los servicios necesitan conocer para
 * restringir una busqueda del cliente. Ejemplo: rango de precios.<br/>
 * Encapsula los requerimientos de busqueda que provee el cliente.</p>
 * 
 * @author Martin
 * @date Jul 15, 2009
 */
public class CriterioDeBusquedaDTO {

    private BigDecimal precioMinimo;

    private BigDecimal precioMaximo;

    private Date fechaEvento;

    public BigDecimal getPrecioMinimo() {
        return this.precioMinimo;
    }

    public void setPrecioMinimo(final BigDecimal precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public BigDecimal getPrecioMaximo() {
        return this.precioMaximo;
    }

    public void setPrecioMaximo(final BigDecimal precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public Date getFechaEvento() {
        return this.fechaEvento;
    }

    public void setFechaEvento(final Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }
}
