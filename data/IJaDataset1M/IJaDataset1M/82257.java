package model;

import java.util.HashMap;
import model.vos.ErrorUserException;

/**
* Clase que gestiona la seguridad para el ingreso de los usuarios al sistema
* 
* @author Manuel
*/
public class SecurityUsers {

    private static HashMap<String, String> userList = new HashMap<String, String>();

    static {
        userList.put("manuel", "manuel");
        userList.put("hugo", "hugo");
        userList.put("zuri", "zuri");
        userList.put("jose", "jose");
        userList.put("anzures", "anzures");
    }

    private static HashMap<String, String> userType = new HashMap<String, String>();

    static {
        userType.put("manuel", "admin");
        userType.put("hugo", "admin");
        userType.put("zuri", "sale");
        userType.put("jose", "sale");
        userType.put("anzures", "sale");
    }

    /**
	 * Metodo que valida si una cuenta de usuario es correcta
	 * @return true si el usuario es valido, false en caso contrario
	 */
    public static String type(String userName) {
        return userType.get(userName);
    }

    /**
	 * Metodo que valida si una cuenta de usuario es correcta
	 * @return true si el usuario es valido, false en caso contrario
	 */
    public static boolean isValidUser(String userName, String password) throws ErrorUserException {
        boolean isValidUser = false;
        String pwd = "";
        if (userList.containsValue(userName)) {
            pwd = userList.get(userName);
            if (pwd.compareTo(password) == 0) {
                isValidUser = true;
            }
        } else {
            throw new ErrorUserException();
        }
        if (!isValidUser) {
            throw new ErrorUserException();
        }
        return isValidUser;
    }
}
