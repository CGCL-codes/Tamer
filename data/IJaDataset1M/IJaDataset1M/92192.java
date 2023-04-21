package proyecto2;

/**
 *
 * @author Tono
 */
public class AppsInternet extends Cuenta {

    private String link;

    public AppsInternet(String titulo, String usuario, String pass, String link) {
        super(titulo, usuario, pass);
    }

    public String getLink() {
        return link;
    }
}
