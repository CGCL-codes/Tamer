package edu.eps.ceu.consultaplus.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author miguel
 */
public class Validador {

    private static final String DNI = "((\\d){1,8}-?[A-Z])?";

    private static final String NOMBRE = "([a-zA-Z[ñÑáéíóúàèìòùäëïöüÁÉÍÓÚÀÈÌÒÙÄËÏÖÜ]][a-zA-Z[ñÑáéíóúàèìòùäëïöüÁÉÍÓÚÀÈÌÒÙÄËÏÖÜ -.']]*)?";

    private static final String TELEFONO = "((\\d){9,9})?";

    private static final String NUMERO = "((\\d){1,3})?";

    private static final String PISO = "((\\d){1,2}º? ?[a-zA-Z[ñÑ]])?";

    private static final String CP = "((\\d){5,5})?";

    private static final String USUARIO = "";

    private static final String PASSWORD = "";

    private static final String EMAIL = ".+@.+\\.[a-z]+";

    private static final String CABECERA = "Formato incorrecto: ";

    private static final String DNI_ERROR = CABECERA + "El formato debe ser 12345678A ó 12345678-A";

    private static final String NOMBRE_ERROR = CABECERA + "No debe contener ni números ni caracteres como #$%&'()*+,./:;<=>?@[\\]^_`{|}~";

    private static final String TELEFONO_ERROR = CABECERA + "El teléfono debe tener 9 dígitos";

    private static final String NUMERO_ERROR = CABECERA + "Solo debe contener un número comprendido entre 1 y 999";

    private static final String PISO_ERROR = CABECERA + "El formato debe ser 1ºE, 1 E, 1º E, ...";

    private static final String CP_ERROR = CABECERA + "Sólo debe contener cinco digitos";

    private static final String USUARIO_ERROR = CABECERA + "";

    private static final String PASSWORD_ERROR = CABECERA + "";

    private static final String EMAIL_ERROR = CABECERA + "la dirección email debe contener el carácter @ y un dominio";

    String error;

    String campo;

    public Validador() {
    }

    private void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public boolean validarCampo(String texto, String campo) {
        boolean flag = false;
        if (campo.equals("DNI")) {
            flag = validar(texto, DNI, DNI_ERROR);
        }
        if (campo.equals("Nombre")) {
            flag = validar(texto, NOMBRE, NOMBRE_ERROR);
        }
        if (campo.equals("Telefono")) {
            flag = validar(texto, TELEFONO, TELEFONO_ERROR);
        }
        if (campo.equals("Numero")) {
            flag = validar(texto, NUMERO, NUMERO_ERROR);
        }
        if (campo.equals("Piso")) {
            flag = validar(texto, PISO, PISO_ERROR);
        }
        if (campo.equals("CP")) {
            flag = validar(texto, CP, CP_ERROR);
        }
        if (campo.equals("Email")) {
            flag = validar(texto, EMAIL, EMAIL_ERROR);
        }
        return flag;
    }

    private boolean validar(String texto, String pattern, String error) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(texto);
        boolean b = m.matches();
        if (b == false) {
            setError(error);
            return false;
        }
        return b;
    }
}
