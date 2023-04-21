package estrategia;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import monitor.ManagerObjetos;

public final class ParametrosGlobales {

    public static final String NOMBRE_ARCHIVO_PROPIEDADES_GLOBALES = "Parametros/Controlador.properties";

    public static final String NOMBRE_ARCHIVO_CLASIFICACION_OBJETOS = "Parametros/ClasificacionObjetos.properties";

    public static int CANTIDAD_OBJETOS;

    public static int MI_ROBOT;

    public static int TIEMPO_JUEGO;

    public static double TOLERANCIA_PARED;

    public static int TIEMPO_ENTRE_CONTROLES;

    public static double VELOCIDAD_ESCAPE;

    public static double ANGULO_ESCAPE;

    public static int TIEMPO_DE_RETROCESO;

    public static double TOLERANCIA_ENEMIGOS;

    public static double TOLERANCIA_ANGULOS_OPUESTOS_COS;

    public static double TOLERANCIA_ANGULOS_OPUESTOS_SIN;

    public static double TOLERANCIA_FUERTES = 0.08;

    public static double TOLERANCIA_DISTANCIA_MUERTE = 0.17;

    public static double PUNTO_DE_SALIDA_X = 0.2;

    public static double PUNTO_DE_SALIDA_Y = 0.2;

    public static double DISTANCIA_ENTRE_RUEDAS;

    public static String comandosIP = "192.168.32.29";

    public static int comandosPuerto = 1234;

    public static String macRobot = "00:16:53:08:6A:F4";

    public static String simIP = "127.0.0.1";

    public static int simPuerto = 34465;

    static {
        try {
            loadProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadProperties() throws IOException {
        Properties properties = new Properties();
        InputStream is = new FileInputStream(NOMBRE_ARCHIVO_PROPIEDADES_GLOBALES);
        properties.load(is);
        if (properties.containsKey("TOLERANCIA_PARED")) {
            TOLERANCIA_PARED = Double.parseDouble(properties.getProperty("TOLERANCIA_PARED"));
        } else {
            avisarError("TOLERANCIA_PARED");
        }
        if (properties.containsKey("TIEMPO_ENTRE_CONTROLES")) {
            TIEMPO_ENTRE_CONTROLES = Integer.parseInt(properties.getProperty("TIEMPO_ENTRE_CONTROLES"));
        } else {
            avisarError("TIEMPO_ENTRE_CONTROLES");
        }
        if (properties.containsKey("VELOCIDAD_ESCAPE")) {
            VELOCIDAD_ESCAPE = Double.parseDouble(properties.getProperty("VELOCIDAD_ESCAPE"));
        } else {
            avisarError("VELOCIDAD_ESCAPE");
        }
        if (properties.containsKey("ANGULO_ESCAPE")) {
            ANGULO_ESCAPE = Double.parseDouble(properties.getProperty("ANGULO_ESCAPE"));
        } else {
            avisarError("ANGULO_ESCAPE");
        }
        if (properties.containsKey("TIEMPO_DE_RETROCESO")) {
            TIEMPO_DE_RETROCESO = Integer.parseInt(properties.getProperty("TIEMPO_DE_RETROCESO"));
        } else {
            avisarError("TIEMPO_DE_RETROCESO");
        }
        if (properties.containsKey("TOLERANCIA_ENEMIGOS")) {
            TOLERANCIA_ENEMIGOS = Double.parseDouble(properties.getProperty("TOLERANCIA_ENEMIGOS"));
        } else {
            avisarError("TOLERANCIA_ENEMIGOS");
        }
        if (properties.containsKey("TOLERANCIA_ANGULOS_OPUESTOS_COS")) {
            TOLERANCIA_ANGULOS_OPUESTOS_COS = Double.parseDouble(properties.getProperty("TOLERANCIA_ANGULOS_OPUESTOS_COS"));
        } else {
            avisarError("TOLERANCIA_ANGULOS_OPUESTOS_COS");
        }
        if (properties.containsKey("TOLERANCIA_ANGULOS_OPUESTOS_SIN")) {
            TOLERANCIA_ANGULOS_OPUESTOS_SIN = Double.parseDouble(properties.getProperty("TOLERANCIA_ANGULOS_OPUESTOS_SIN"));
        } else {
            avisarError("TOLERANCIA_ANGULOS_OPUESTOS_SIN");
        }
        if (properties.containsKey("TOLERANCIA_FUERTES")) {
            TOLERANCIA_FUERTES = Double.parseDouble(properties.getProperty("TOLERANCIA_FUERTES"));
        } else {
            avisarError("TOLERANCIA_FUERTES");
        }
        if (properties.containsKey("DISTANCIA_ENTRE_RUEDAS")) {
            DISTANCIA_ENTRE_RUEDAS = Double.parseDouble(properties.getProperty("DISTANCIA_ENTRE_RUEDAS"));
        } else {
            avisarError("DISTANCIA_ENTRE_RUEDAS");
        }
        if (properties.containsKey("comandosIP")) {
            comandosIP = properties.getProperty("comandosIP").toString();
        } else {
            avisarError("comandosIP");
        }
        if (properties.containsKey("comandosPuerto")) {
            comandosPuerto = Integer.parseInt(properties.getProperty("comandosPuerto"));
        } else {
            avisarError("comandosPuerto");
        }
        if (properties.containsKey("macRobot")) {
            macRobot = properties.getProperty("macRobot").toString();
        } else {
            avisarError("macRobot");
        }
        if (properties.containsKey("simIP")) {
            simIP = properties.getProperty("simIP").toString();
        } else {
            avisarError("simIP");
        }
        if (properties.containsKey("simPuerto")) {
            simPuerto = Integer.parseInt(properties.getProperty("simPuerto"));
        } else {
            avisarError("simPuerto");
        }
        if (properties.containsKey("PUNTO_DE_SALIDA_X")) {
            PUNTO_DE_SALIDA_X = Double.parseDouble(properties.getProperty("PUNTO_DE_SALIDA_X"));
        } else {
            avisarError("PUNTO_DE_SALIDA_X");
        }
        if (properties.containsKey("PUNTO_DE_SALIDA_Y")) {
            PUNTO_DE_SALIDA_Y = Double.parseDouble(properties.getProperty("PUNTO_DE_SALIDA_Y"));
        } else {
            avisarError("PUNTO_DE_SALIDA_Y");
        }
        Properties propiedadesObjetos = new Properties();
        InputStream is2 = new FileInputStream(NOMBRE_ARCHIVO_CLASIFICACION_OBJETOS);
        propiedadesObjetos.load(is2);
        if (propiedadesObjetos.containsKey("CANTIDAD_OBJETOS")) {
            CANTIDAD_OBJETOS = Integer.parseInt(propiedadesObjetos.getProperty("CANTIDAD_OBJETOS"));
        } else {
            avisarError("CANTIDAD_OBJETOS");
        }
        if (propiedadesObjetos.containsKey("MI_ROBOT")) {
            MI_ROBOT = Integer.parseInt(propiedadesObjetos.getProperty("MI_ROBOT"));
        } else {
            avisarError("MI_ROBOT");
        }
        if (propiedadesObjetos.containsKey("TIEMPO_JUEGO")) {
            TIEMPO_JUEGO = Integer.parseInt(propiedadesObjetos.getProperty("TIEMPO_JUEGO"));
        } else {
            avisarError("TIEMPO_JUEGO");
        }
    }

    private static void avisarError(String claveInexistente) throws IOException {
        throw new IOException("Falta propiedad " + claveInexistente + "en el archivo de propiedades " + NOMBRE_ARCHIVO_PROPIEDADES_GLOBALES + " o " + NOMBRE_ARCHIVO_CLASIFICACION_OBJETOS);
    }
}
