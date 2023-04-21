package ar.com.grupo1.ler.dominio;

import java.math.BigDecimal;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Define todo lo que tiene que ver una Ruta
 * Su cantidad total de Km, y cuantos de ellos estan en buen estado.
 * La cantidad de Km en mal estado, se consideran de calcular la diferencia 
 * entre el total de km y los km en buen estado 
 *
 */
public class Ruta {

    private BigDecimal cantTotalKm;

    private BigDecimal cantKmEnBuenEstado;

    public BigDecimal getCantTotalKm() {
        return cantTotalKm;
    }

    public void setCantTotalKm(BigDecimal cantTotalKm) {
        this.cantTotalKm = cantTotalKm;
    }

    public BigDecimal getCantKmEnBuenEstado() {
        return cantKmEnBuenEstado;
    }

    public void setCantKmEnBuenEstado(BigDecimal cantKmEnBuenEstado) {
        this.cantKmEnBuenEstado = cantKmEnBuenEstado;
    }

    public BigDecimal getCantKmEnMalEstado() {
        return cantTotalKm.subtract(cantKmEnBuenEstado);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
