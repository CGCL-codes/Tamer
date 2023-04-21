package es.gavab.team.svnadmin.model;

/**
 * Interface of the class User.
 * 
 * @author Juan Carlos Hern�ndez Guti�rrez
 * @version 1.0
 * @since JDK1.5
 *
 */
public interface IUser {

    public boolean isPassEncrypted();

    public String getLogin();

    public void setLogin(String login);

    public String getPass();

    public void setPass(String pass);
}
