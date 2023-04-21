package utilidad;

import java.util.Comparator;
import java.util.Date;

public class ComparadorFechas implements Comparator {

    public ComparadorFechas(String clase, String metodo) {
        this.clase = clase;
        this.metodo = metodo;
        this.tipoComparacion = "ASC";
    }

    public ComparadorFechas(String clase, String metodo, String tipoComparacion) {
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
            Date fecha1 = (Date) Util.invocaMetodo(o1, clase, metodo);
            Date fecha2 = (Date) Util.invocaMetodo(o2, clase, metodo);
            if ("DESC".equalsIgnoreCase(this.tipoComparacion)) {
                comparacion = fecha2.compareTo(fecha1);
            } else {
                comparacion = fecha1.compareTo(fecha2);
            }
        } catch (Exception ex) {
            System.out.println("Excepcion en Comparador Fechas");
        }
        return comparacion;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }
}
