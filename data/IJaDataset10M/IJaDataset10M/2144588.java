package net.sf.jregression.reglas;

/**
 * @author mblasi
 * mailto: matias.blasi@gmail.com
 *
 * Interfaz que define los nombres de los tags y sus propiedades 
 */
public interface Tarea {

    public static final String SEPARADOR = "/";

    public static final String JREGRESSION = "jregression";

    public static final String LOTES = "lotes";

    public static final String LOTE = "lote";

    public static final String LOTE_ID = "id";

    public static final String PARAM = "param";

    public static final String PARAM_VALOR = "valor";

    public static final String PARAM_NOMBRE = "nombre";

    public static final String PARAM_TIPO = "tipo";

    public static final String PARAM_TIPO_DEPENDIENTE = "dependiente";

    public static final String PARAM_TIPO_VARIABLE = "variable";

    public static final String PARAM_TIPO_RANDOM = "rnd";

    public static final String PARAM_TIPO_INTEGER = "java.lang.Integer";

    public static final String PARAM_TIPO_STRING = "java.lang.String";

    public static final String PARAM_TIPO_BIGDECIMAL = "java.math.BigDecimal";

    public static final String PARAM_TIPO_DATE = "java.util.Date";

    public static final String PARAM_TIPO_TIMESTAMP = "java.sql.Timestamp";

    public static final String PARAM_TIPO_BOOLEAN = "java.lang.Boolean";

    public static final String PARAM_VO = "vo";

    public static final String PARAM_VO_CLASE = "clase";

    public static final String PARAM_COLECCION = "coleccion";

    public static final String PARAM_PROPIEDAD = "propiedad";

    public static final String PRUEBAS = "pruebas";

    public static final String PRUEBA = "prueba";

    public static final String PRUEBA_TIPO = "tipo";

    public static final String PRUEBA_TIPO_FLUJO_BASICO = "flujo-basico";

    public static final String PRUEBA_TIPO_FLUJO_EXCEPCION = "flujo-excepcion";

    public static final String PRUEBA_ID = "id";

    public static final String PRUEBA_METODO = "metodo";

    public static final String PRUEBA_SOAP = "soap";

    public static final String PRUEBA_LOGUEARME = "loguearme";

    public static final String PRUEBA_LOGUEARME_SI = "si";

    public static final String PRUEBA_LOGUEARME_NO = "no";

    public static final String PRUEBA_ENTRADA = "entrada";

    public static final String PRUEBA_ESTRATEGIA_ENTRADA = "input-strategy";

    public static final String PRUEBA_ESTRATEGIA_SALIDA = "output-strategy";

    public static final String PRUEBA_EXCEPCION = "excepcion";

    public static final String SALIDA = "salida";

    public static final String SALIDA_ID = "id";

    public static final String SALIDA_STRATEGY = "assertion-strategy";

    public static final String BATERIAS = "baterias";

    public static final String BATERIA = "bateria";

    public static final String BATERIA_LOGIN_PARTICULAR = "particular";

    public static final String BATERIA_LOGIN_GLOBAL = "global";

    public static final String BATERIA_EXCLUIR = "excluir";

    public static final String BATERIA_EXCLUIR_SI = "si";

    public static final String BATERIA_EXCLUIR_NO = "no";
}
