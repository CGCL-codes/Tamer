package utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import exceptions.InvalidDataException;
import exceptions.DatasetNotFoundException;
import persistence.dataAccessObjects.UserDAO;
import persistence.transferPOJOs.User;

/**
 *	Wrapper-Klasse mit diversen Methoden zur �berpr�fung des Anmeldezustands. 
 */
public class LoginInfo {

    private User user;

    private UserDAO userDAO;

    private LoginState loginState;

    private HttpSession session;

    /**
	 * Neue Instanz von Login-Info.
	 * @param userID
	 * @param email
	 * @param passwordMD5Hash
	 * @param userDAO
	 * @param session
	 * @throws InvalidDataException
	 */
    public LoginInfo(UserDAO userDAO, HttpSession session, HttpServletRequest request) throws InvalidDataException {
        super();
        loginState = LoginState.NOT_INITIALIZED;
        if (session == null) throw new InvalidDataException("�bergebene Session wurde nicht initialisiert.", request.getQueryString());
        if (userDAO == null) throw new InvalidDataException("�bergebene User-DAO wurde nicht initialisiert.", request.getQueryString());
        this.session = session;
        this.userDAO = userDAO;
        user = (User) session.getAttribute("user");
        String email = (String) request.getParameter("email");
        String passwort = (String) request.getParameter("passwort");
        if (user != null && user.getEmail() != null && user.getPasswort() != null) authentificate(user.getEmail(), user.getPasswort()); else if (email != null && passwort != null) authentificate(email, generateMD5Hash(passwort));
    }

    /***
	 * �berpr�ft �ber Cookies, ob User angemeldet ist und Anmeldedaten korrekt sind. Dabei wird User-Instanz
	 * mit dieser Mail aus Datenbank in User-Instanz geladen, sofern Authentifizierung erfolgreich verl�uft. 
	 * @param login
	 * @param pwd
	 * @see ServletUtilities#isLoggedIn(UserDAO, HttpSession)
	 * @return
	 */
    private boolean authentificate(String login, String pwd) {
        if (session == null) {
            this.loginState = LoginState.NOT_INITIALIZED;
            return false;
        } else {
            if (login == null || pwd == null) {
                this.loginState = LoginState.NOT_INITIALIZED;
                return false;
            }
            try {
                if (userDAO.checkCredentials(login, pwd)) {
                    loginState = LoginState.AUTHENTIFICATED;
                    user = userDAO.getUserByEMail(login);
                    session.setAttribute("user", user);
                    return true;
                } else {
                    if (userDAO.checkOnNewUserMail(login)) loginState = LoginState.NON_EXISTING_EMAIL; else loginState = LoginState.WRONG_PASSWORD;
                    return false;
                }
            } catch (DatasetNotFoundException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
        loginState = LoginState.NOT_INITIALIZED;
        return false;
    }

    /**
	 * @return the userID
	 */
    public User getUser() {
        return user;
    }

    /**
	 * @param user
	 */
    public void setUser(User user) {
        this.user = user;
    }

    /**
	 * Gibt aktuellen LoginState zur�ck.
	 * @return Aktueller LoginState.
	 */
    public LoginState getLoginState() {
        return loginState;
    }

    /**
	 * Gibt zur�ck, ob User aktuell angemeldet ist.
	 * 
	 * @return Ob User aktuell angemeldet ist..
	 */
    public boolean isLoggedIn() {
        return (loginState == LoginState.AUTHENTIFICATED) ? true : false;
    }

    /**
	 * Liefert MD5-Hash eines �bergebenen Strings zur�ck.
	 * Quelle: http://snippets.dzone.com/posts/show/3686
	 * @param source
	 * @return 
	 */
    public static String generateMD5Hash(String source) {
        String result = "";
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            byte[] data = source.getBytes();
            m.update(data, 0, data.length);
            BigInteger i = new BigInteger(1, m.digest());
            result = String.format("%1$032X", i);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
	 * F�hrt Loginoperation durch.
	 * @param request
	 * @param response
	 * @return LoginState - Zustand, der nach versuchtem Login erreicht wurde.
	 * @throws ServletException
	 */
    public LoginState login(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String email = request.getParameter("email");
        String password = request.getParameter("passwort");
        if (email != null && password != null) {
            try {
                String passwordMD5Hash = generateMD5Hash(password);
                if (userDAO.checkCredentials(email, passwordMD5Hash) == true) {
                    session.setAttribute("email", email);
                    session.setAttribute("passwort", passwordMD5Hash);
                    return LoginState.AUTHENTIFICATED;
                } else {
                    return LoginState.WRONG_PASSWORD;
                }
            } catch (NumberFormatException e) {
                return LoginState.INVALID_DATA;
            } catch (DatasetNotFoundException e) {
                return LoginState.NON_EXISTING_EMAIL;
            }
        } else {
            return LoginState.NOT_INITIALIZED;
        }
    }
}
