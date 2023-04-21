package juego;

import java.util.Locale;
import juego.genSS.DescriptorAstro;

public class Utils {

    public static final double SEGUNDOS_MINUTO = 60.0d;

    public static final double SEGUNDOS_HORA = 3600.0d;

    public static final double SEGUNDOS_DIA = 24.0d * SEGUNDOS_HORA;

    public static final double SEGUNDOS_ANIO = 365.0d * SEGUNDOS_DIA;

    public static final double SEGUNDOS_MES_DECIMAL = 10.0d * SEGUNDOS_DIA;

    public static final double SEGUNDOS_ANIO_DECIMAL = 10.0d * SEGUNDOS_MES_DECIMAL;

    public static String velocidadAString(double vel) {
        double absVel = Math.abs(vel);
        if (absVel < 1000.0d) {
            return String.format(Locale.ENGLISH, "%.0f m/s", vel);
        } else if (absVel < 100000.0d) {
            return String.format(Locale.ENGLISH, "%.1f Km/s", vel / 1000.0d);
        } else {
            return String.format(Locale.ENGLISH, "%d Km/s", (int) (vel / 1000));
        }
    }

    public static String aceleracionAString(double acel) {
        double absAcel = Math.abs(acel);
        if (absAcel < 1000.0d) {
            return String.format(Locale.ENGLISH, "%.1f m/s²", acel);
        } else if (absAcel < 100000.0d) {
            return String.format(Locale.ENGLISH, "%.1f Km/s²", acel / 1000.0d);
        } else {
            return String.format(Locale.ENGLISH, "%d Km/s²", (int) (acel / 1000));
        }
    }

    public static String distanciaAString(double dist) {
        if (dist < 1000000.0d) {
            return String.format(Locale.ENGLISH, "%.3f Km", dist / 1000.0d);
        } else if (dist < 1000000000.0d) {
            return String.format(Locale.ENGLISH, "%d Km", (int) (dist / 1000));
        } else if (dist < 100000000000.0d) {
            return String.format(Locale.ENGLISH, "%.3f M.Km", dist / 1000000000.0d);
        } else {
            return String.format(Locale.ENGLISH, "%.3f AU", dist / 150000000000.0d);
        }
    }

    public static String tiempoAString(double tiempo) {
        int dias = (int) Math.floor(tiempo / (3600.0d * 24.0d));
        tiempo -= dias * (3600.0d * 24.0d);
        double horasD = tiempo / 3600.0d;
        if (dias >= 1) {
            return String.format(Locale.ENGLISH, "%dd %.1fh", dias, horasD);
        }
        int horas = (int) Math.floor(horasD);
        tiempo -= horas * 3600.0d;
        int minutos = (int) Math.floor(tiempo / 60.0d);
        tiempo -= minutos * 60.0d;
        return String.format("%d:%02d:%02d", horas, minutos, (int) tiempo);
    }

    public static String masaAString(double masa) {
        if (masa < 10000.0d) {
            return String.format(Locale.ENGLISH, "%.1f Kg", masa);
        }
        if (masa < 1000000.0d) {
            return String.format(Locale.ENGLISH, "%.1f Tons", masa / 1000.0d);
        }
        double masaSol = masa / DescriptorAstro.MASA_SOL;
        if (masaSol > 0.1d) {
            return String.format(Locale.ENGLISH, "%.2f MSol", masaSol);
        }
        double masaJup = masa / DescriptorAstro.MASA_JUPITER;
        if (masaJup > 0.01d) {
            return String.format(Locale.ENGLISH, "%.2f MJup", masaJup);
        }
        double masaTierra = masa / DescriptorAstro.MASA_TIERRA;
        if (masaTierra > 0.01d) {
            return String.format(Locale.ENGLISH, "%.2f MEarth", masaTierra);
        }
        if (masaTierra > 0.0001d) {
            return String.format(Locale.ENGLISH, "%.4f MEarth", masaTierra);
        }
        return String.format(Locale.ENGLISH, "%.3E Tons", masa / 1000.0d);
    }

    public static String fechaAString(int anyoInicial, double fecha) {
        double t = fecha / SEGUNDOS_ANIO_DECIMAL;
        int anyo = anyoInicial + ((int) Math.floor(t));
        t = (t - Math.floor(t)) * SEGUNDOS_ANIO_DECIMAL;
        t /= SEGUNDOS_MES_DECIMAL;
        int mes = (int) Math.floor(t);
        t = (t - Math.floor(t)) * SEGUNDOS_MES_DECIMAL;
        t /= SEGUNDOS_DIA;
        int dia = (int) Math.floor(t);
        t = (t - Math.floor(t)) * SEGUNDOS_DIA;
        t /= SEGUNDOS_HORA;
        int hora = (int) Math.floor(t);
        t = (t - Math.floor(t)) * SEGUNDOS_HORA;
        t /= SEGUNDOS_MINUTO;
        int minuto = (int) Math.floor(t);
        t = (t - Math.floor(t)) * SEGUNDOS_MINUTO;
        return String.format("%02d:%02d:%02d %02d-%02d-%d", hora, minuto, (int) t, mes + 1, dia + 1, anyo);
    }

    public int anyoDecimalAAnyoTerrestre(int anyoDecimal) {
        return (int) (anyoDecimal * SEGUNDOS_ANIO / SEGUNDOS_ANIO_DECIMAL);
    }

    public int anyoTerrestreAAnyoDecimal(int anyoTerrestre) {
        return (int) (anyoTerrestre * SEGUNDOS_ANIO_DECIMAL / SEGUNDOS_ANIO);
    }
}
