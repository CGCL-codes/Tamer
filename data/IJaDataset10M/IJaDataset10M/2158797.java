package mx.ipn;

public class Constantes {

    public static final int FABRICA_DAOS_MYSQL = 1;

    public static final int FABRICA_DAOS_ORACLE = 2;

    private static String urlServidorPersistencia = "192.168.5.102";

    private static String urlServidorNegocios = "192.168.5.101";

    private static String puertoServidorPersistencia = "8080";

    private static String puertoServidorNegocios = "8080";

    public static String URL_SERVICIOS_PERSISTENCIA = "http://" + urlServidorPersistencia + ":" + puertoServidorPersistencia + "/axis2/services/SAISTServiciosPersistencia";

    public static String URL_SERVICIOS_NEGOCIOS = "http://" + urlServidorNegocios + ":" + puertoServidorNegocios + "/axis2/services/SAISTServiciosNegocios";

    public static final String URI_SERVICIOS = "http://servicios.ipn.mx";

    public static final String DIRECTORIO_DE_LOGS = System.getProperty("user.home") + System.getProperty("file.separator") + "saist_logs";

    public static void setUrlServidorPersistencia(String urlServidorPersistencia) {
        Constantes.urlServidorPersistencia = urlServidorPersistencia;
    }

    public static String getUrlServidorPersistencia() {
        return urlServidorPersistencia;
    }

    public static void setUrlServidorNegocios(String urlServidorNegocios) {
        Constantes.urlServidorNegocios = urlServidorNegocios;
    }

    public static String getUrlServidorNegocios() {
        return urlServidorNegocios;
    }

    public static void setPuertoServidorPersistencia(String puertoServidorPersistencia) {
        Constantes.puertoServidorPersistencia = puertoServidorPersistencia;
        URL_SERVICIOS_PERSISTENCIA = "http://" + urlServidorPersistencia + ":" + puertoServidorPersistencia + "/axis2/services/SAISTServiciosPersistencia";
    }

    public static String getPuertoServidorPersistencia() {
        return puertoServidorPersistencia;
    }

    public static void setPuertoServidorNegocios(String puertoServidorNegocios) {
        Constantes.puertoServidorNegocios = puertoServidorNegocios;
        URL_SERVICIOS_NEGOCIOS = "http://" + urlServidorNegocios + ":" + puertoServidorNegocios + "/axis2/services/SAISTServiciosNegocios";
    }

    public static String getPuertoServidorNegocios() {
        return puertoServidorNegocios;
    }
}
