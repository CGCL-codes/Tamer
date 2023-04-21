package client.communication;

import java.net.PasswordAuthentication;
import java.util.Properties;

/**
 * La clase mantiene referencias a las instanicas unicas de objetos
 * reelevantes para toda la aplicacion, los cuales son unicos durante la
 * ejecucion de la misma, por ejemplo la instancia de
 * {@link ClientCommunication}. Centralizar el acceso a dichas clases desde
 * un puinto comun, evita tener que referenciarlas mediante variables
 * internas que se deben setear en todas las clases que deben usar estos
 * objetos.
 *
 * @author lito
 */
public class GameContext {

    /** El cliente a utilizar en el contexto de la aplicacion. */
    private static ClientCommunication clientCommunication;

    /**
	 * Se deben guardar si o si los siguientes datos:<BR/>
	 * host, port, userName, password.
	 */
    private static Properties properties;

    /**
	 * Constructor default por defecto, se lo implemneta privado y
	 * vacio, puesto que todos los metodos son estaticos. 
	 */
    private GameContext() {
    }

    /**
	 * @return the clientCommunication
	 */
    public static ClientCommunication getClientCommunication() {
        return clientCommunication;
    }

    /**
	 * @param newClientCommunication the clientCommunication to set
	 */
    public static void setClientCommunication(final ClientCommunication newClientCommunication) {
        GameContext.clientCommunication = newClientCommunication;
    }

    /**
	 * @return the properties
	 */
    public static Properties getProperties() {
        return properties;
    }

    /**
	 * @param newProperties the properties to set
	 */
    public static void setProperties(final Properties newProperties) {
        GameContext.properties = newProperties;
    }

    /**
	 * @return El nombre del usuario que esta utilizando el GameContext.
	 */
    public static String getUserName() {
        return properties.getProperty("userName");
    }

    /**
	 * @param userName El nombre del usuario que esta
	 * utilizando el GameContext.
	 */
    public static void setUserName(final String userName) {
        properties.setProperty("userName", userName);
    }

    /**
	 * @return El password del usuario que esta utilizando el
	 * GameContext.
	 */
    public static String getPassword() {
        return properties.getProperty("password");
    }

    /**
	 * @param password El password del usuario que esta utilizando
	 * el GameContext.
	 */
    public static void setPassword(final String password) {
        properties.setProperty("password", password);
    }

    /**
	 * @return El host del usuario que esta utilizando el
	 * GameContext.
	 */
    public static String getHost() {
        return properties.getProperty("host");
    }

    /**
	 * @param host El host del servidor que esta utilizando
	 * el GameContext.
	 */
    public static void setHost(final String host) {
        properties.setProperty("host", host);
    }

    /**
	 * @return El port del servidor que esta utilizando el
	 * GameContext.
	 */
    public static String getPort() {
        return properties.getProperty("port");
    }

    /**
	 * @param port El port del servidor que esta utilizando
	 * el GameContext.
	 */
    public static void setPort(final String port) {
        properties.setProperty("port", port);
    }

    /**
	 * Crea y retorna una autentificacion con el {@link #userName} y
	 * el {@link #password} que tenga seteado el GameContext.
	 * 
	 *  @return una autentificacion con el {@link #userName} y
	 * el {@link #password} que tenga seteado el GameContext.
	 */
    public static PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(getUserName(), getPassword().toCharArray());
    }
}
