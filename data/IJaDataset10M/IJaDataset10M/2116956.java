package com.chuidiang.editores.primitivos.conversores;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.StringTokenizer;
import com.chuidiang.editores.matematicas.angulos.GradosConDecimales;

/**
 * Convierte un <code>Double</code> que representa una latitud en grados 
 * a String y viceversa.<br>
 */
public abstract class ConversorEjeGeografico implements InterfaceConversorString<Number> {

    public static final String SIMBOLO_GRADOS = "º";

    /**
    * Se le pasa una cadena con una Latitud y devuelve un Double con dicha
    * latitud en grados.<br>
    * Si no se puede hacer la coversiOn, lanza una ParseException
    *
    * @param cadena Cadena con la latitud.<br>
    * @param valor Se ignora.<br>
    *
    * @return Un Double con la latitud en grados.<br>
    */
    public Number parseString(String cadena) throws ParseException {
        StringTokenizer tokenizator = new StringTokenizer(cadena, SIMBOLO_GRADOS + "'");
        String cadenaGrados = "0";
        String cadenaMinutos = "0";
        String cadenaSegundos = "0";
        String cadenaHemisferio = getCadenaEjePositivo();
        try {
            cadenaGrados = tokenizator.nextToken().trim();
            cadenaMinutos = tokenizator.nextToken().trim();
            cadenaSegundos = tokenizator.nextToken().trim();
            cadenaHemisferio = tokenizator.nextToken().trim();
        } catch (Exception e) {
            String nombreEje = getNombreEje();
            throw new ParseException(cadena + "no se puede convertir a " + nombreEje, 0);
        }
        int grados;
        int minutos;
        int segundos;
        int signo;
        grados = Integer.parseInt(cadenaGrados);
        minutos = Integer.parseInt(cadenaMinutos);
        segundos = Integer.parseInt(cadenaSegundos);
        if (cadenaHemisferio.compareTo(this.getCadenaEjePositivo()) == 0) {
            signo = 1;
        } else if (cadenaHemisferio.compareTo(this.getCadenaEjeNegativo()) == 0) {
            signo = -1;
        } else {
            throw new ParseException(cadenaHemisferio + " no se puede interpretar como " + this.getCadenaEjePositivo() + " o " + this.getCadenaEjePositivo(), 0);
        }
        return new Double(GradosConDecimales.getGradosConDecimales(grados, minutos, segundos, signo));
    }

    /**
    * Recibe un Double que representa una longitud en grados con decimales y lo
    * devuelve en formato String GGG�MM'SS''.<br>
    * Si el valor no es Double devuelve null. Si el valor esta fuera del rango
    * -90 a + 90,
    *
    * @param valor Double con la latitud en grados con decimales.<br>
    *
    * @return Cadena con la latitud.<br>
    */
    public String toString(Number valor) {
        DecimalFormat format = new DecimalFormat("00");
        double angulo = valor.doubleValue();
        String cadenaSignoEje = this.getCadenaEjePositivo();
        if (angulo < 0) {
            cadenaSignoEje = this.getCadenaEjeNegativo();
        }
        int segundos = GradosConDecimales.getSegundos(angulo);
        String segs = format.format(segundos);
        int minutos = GradosConDecimales.getMinutos(angulo);
        String mins = format.format(minutos);
        long grados = GradosConDecimales.getGrados(angulo);
        format.applyPattern(this.getPatternGrados());
        String grads = format.format(grados);
        return grads + "º" + mins + "'" + segs + "''" + cadenaSignoEje;
    }

    /**
    * DOCUMENT ME!
    *
    * @return
    */
    protected abstract String getCadenaEjeNegativo();

    /**
    * DOCUMENT ME!
    *
    * @return
    */
    protected abstract String getCadenaEjePositivo();

    /**
    * DOCUMENT ME!
    *
    * @return
    */
    protected abstract String getNombreEje();

    /**
    * COMENTARIO.<br>
    *
    * @return COMENTARIO.<br>
    */
    protected abstract String getPatternGrados();
}
