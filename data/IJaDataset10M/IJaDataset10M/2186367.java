package net.sourceforge.buildprocess.autodeploy;

/**
 * Exception raised while sending e-mail
 * 
 * @author <a href="mailto:jb@nanthrax.net">Jean-Baptiste Onofr�</a>
 */
public class EmailException extends AutoDeployException {

    /**
    * Generated Serial Version UID
    */
    private static final long serialVersionUID = 7724547512446727664L;

    /**
    * Default constructor
    */
    public EmailException(String message) {
        super(message);
    }
}
