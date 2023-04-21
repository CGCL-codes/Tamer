package utilidad;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

public class ComparadorBigDecimal implements Comparator {

    public ComparadorBigDecimal(String clase, String metodo) {
        this.clase = clase;
        this.metodo = metodo;
        this.tipoComparacion = "ASC";
    }

    public ComparadorBigDecimal(String clase, String metodo, String tipoComparacion) {
        this.clase = clase;
        this.metodo = metodo;
        this.tipoComparacion = tipoComparacion;
    }

    private String clase;

    private String metodo;

    private String tipoComparacion;

    public int compare(Object o1, Object o2) {
        int comparacion = 0;
        try {
            BigDecimal numero1 = (BigDecimal) Util.invocaMetodo(o1, clase, metodo);
            BigDecimal numero2 = (BigDecimal) Util.invocaMetodo(o2, clase, metodo);
            if ("DESC".equalsIgnoreCase(this.tipoComparacion)) {
                comparacion = numero2.compareTo(numero1);
            } else {
                comparacion = numero1.compareTo(numero2);
            }
        } catch (Exception ex) {
            System.out.println("Excepcion en Comparador de BigDecimal: " + ex.toString());
        }
        return comparacion;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }
}
