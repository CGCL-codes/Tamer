package neoAtlantis.utilidades.accessController.authentication.interfaces;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import neoAtlantis.utilidades.accessController.cipher.interfaces.DataCipher;
import neoAtlantis.utilidades.accessController.objects.User;

/**
 *
 * @author Hiryu (aslhiryu@gmail.com)
 */
public abstract class AuthenticationWay {

    /**
     * Versi&oacute;n de la clase.
     */
    public static String VERSION = "1.0";

    /**
     * Nombre para el parametro de las formas de html para el codigo de confirmacion
     */
    public static String CODE_PARAM = "code";

    public static String PARAM_LOGIN = "na_auth";

    /**
     * Defina la contrase�a por default
     */
    protected String passDefault = "";

    /**
     * Cifrador que se utiliza para encriptar las contrase&ntilde;as (si este esta como null las contrase&ntilde;as no seran encriptadas).
     */
    protected DataCipher cifrador;

    /**
     * Define el metodo para validar usuarios.
     * @param datos Variables que se deben de utilizar para realizar la autenticaci�n
     * @return Usuario que se encontro al realizar la autenticacion:
     * @throws java.lang.Exception
     */
    public abstract User autenticaUsuario(Map<String, Object> datos) throws Exception;

    /**
     * Define el metodo para agregar cuentas.
     * @param user Nickname del usuario
     * @param pass Contrase&ntilde;a del usuario
     * @return true, si logro agregar la cuenta
     * @throws java.lang.Exception
     */
    public abstract boolean agregaCuenta(User user, String pass) throws Exception;

    /**
     * Define el metodo para agregar cuentas temporales.
     * @param user Nickname del usuario
     * @return true, si logro agregar la cuenta
     * @throws java.lang.Exception
     */
    public abstract boolean agregaCuentaTemporal(User user) throws Exception;

    /**
     * Define el metodo para modificar una contrase&ntilde;a.
     * @param user Nickname del usuario
     * @param pass Nueva contrase&ntilde;a para el usuario contrase&ntilde;a
     * @return true, si logro modificar la cuenta
     * @throws java.lang.Exception
     */
    public abstract boolean modificaContrasena(User user, String pass) throws Exception;

    /**
     * Define el metodo para inicializar una contrase&ntilde;a.
     * @param user Nickname del usuario
     * @return true, si logro modificar la cuenta
     * @throws java.lang.Exception
     */
    public abstract boolean restauraContrasena(User user) throws Exception;

    public abstract String generaEntornoAutenticacionWeb(String action, String captchaService);

    /**
     * Asigna un Cifrador para la encriptaci&oacute;n de las contrase&ntilde;as  (si este esta como null las contrase&ntilde;as no seran encriptadas).
     * @param c Cifrador de Datos
     */
    public void setCifradorDatos(DataCipher c) {
        this.cifrador = c;
    }

    /**
     * Metodo para validar la esuctrura de una contrase�a
     * @param pass Contrasena a validar
     * @param cars Numero de caracteres minimos con los que debe de contar
     * @param nums Numero de digitos minimos con los que debe de contar
     * @param esps Numero de caracteres especiales minimos con los que debe de contar
     * @return true si es valida
     */
    public static boolean validaConstitucionContrasena(String pass, int cars, int nums, int esps) {
        if (pass == null || cars < 0 || nums < 0 || esps < 0) {
            return false;
        }
        char[] cad = pass.toCharArray();
        int c = 0, n = 0, e = 0;
        for (int i = 0; cad != null && i < cad.length; i++) {
            if (Character.isWhitespace(cad[i])) {
                return false;
            } else if (Character.isDigit(cad[i])) {
                n++;
            } else if (Character.isLetter(cad[i])) {
                c++;
            } else {
                e++;
            }
        }
        if (n >= nums && c >= cars && e >= esps) {
            return true;
        }
        return false;
    }

    /**
     * Metodo para generar una contrase�a
     * @param cars Numero de caracteres con los que debe de contar
     * @param nums Numero de digitos con los que debe de contar
     * @param esps Numero de caracteres especiales minimos con los que debe de contar
     * @return Contrase�a generada
     */
    public static String generaContrasena(int cars, int nums, int esps) {
        StringBuffer sb = new StringBuffer("");
        int c = 0, n = 0, e = 0, tmp;
        Random r = new Random(Calendar.getInstance().getTimeInMillis());
        char ch;
        while (true) {
            tmp = r.nextInt(126);
            if (tmp < 33) {
                continue;
            }
            ch = (char) tmp;
            if (Character.isWhitespace(ch)) {
                continue;
            } else if (Character.isDigit(ch) && n < nums) {
                n++;
                sb.append(ch);
            } else if (Character.isLetter(ch) && c < cars) {
                c++;
                sb.append(ch);
            } else if (e < esps) {
                e++;
                sb.append(ch);
            }
            if (c == cars && n == nums && e == esps) {
                break;
            }
        }
        return sb.toString();
    }

    /**
     * Metodo para genera una clave de confirmaci�n
     * @param tam Tama�o de la clave
     * @return Clave generada
     */
    public static String generaClaveConfirmacion(int tam) {
        StringBuffer sb = new StringBuffer("");
        int c = 0, tmp;
        Random r = new Random(Calendar.getInstance().getTimeInMillis());
        char ch;
        while (true) {
            tmp = r.nextInt(122);
            if (tmp < 48 || (tmp > 57 && tmp < 65) || (tmp > 91 && tmp < 96)) {
                continue;
            }
            ch = (char) tmp;
            if (Character.isLetterOrDigit(ch)) {
                sb.append(ch);
                c++;
            }
            if (c == tam) {
                break;
            }
        }
        return sb.toString();
    }
}
