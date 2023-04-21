package ramon.campos;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import ramon.campos.anot.AntesDe;
import ramon.campos.anot.DelPasado;
import ramon.campos.anot.DespuesDe;
import ramon.campos.anot.Validador;
import ramon.impl.exc.ConfigException;

public class GeneradorFecha extends GeneradorPrimitivo<Date> {

    private static final String DD_MM_YYYY = "dd/MM/yyyy";

    public GeneradorFecha() {
    }

    public GeneradorFecha(Date fecha) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(fecha);
        this.valor = gc.get(GregorianCalendar.DAY_OF_MONTH) + "/" + (gc.get(GregorianCalendar.MONTH) + 1) + "/" + gc.get(GregorianCalendar.YEAR);
    }

    @Override
    protected Date generate() {
        if (valor != null && valor.length() > 0) {
            DateFormat df = new SimpleDateFormat(DD_MM_YYYY);
            try {
                return df.parse(valor);
            } catch (ParseException e) {
                addError(valor + ": la fecha debe tener el formato \"d�a/mes/a�o\"");
            }
        }
        return null;
    }

    @Validador(DelPasado.class)
    public String validar(DelPasado delPasado) {
        if (campo.after(new Date())) return delPasado.error(); else return null;
    }

    @Validador(AntesDe.class)
    public String validar(AntesDe antesDe) {
        if (compararFecha(antesDe.fecha()) >= 0) return String.format(antesDe.error(), antesDe.fecha()); else return null;
    }

    @Validador(DespuesDe.class)
    public String validar(DespuesDe despuesDe) {
        if (compararFecha(despuesDe.fecha()) <= 0) return String.format(despuesDe.error(), despuesDe.fecha()); else return null;
    }

    private int compararFecha(String fechaString) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return campo.compareTo(df.parse(fechaString));
        } catch (ParseException e) {
            throw new ConfigException(String.format("La fecha puesta en la anotaci�n de validaci�n (%s), " + "no tiene un formato v�lido", fechaString));
        }
    }
}
