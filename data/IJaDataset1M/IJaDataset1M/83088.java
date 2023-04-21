package secd.utils;

public class InfoCircuito {

    String url = null;

    String nombre = null;

    boolean esPrincipal = false;

    public InfoCircuito(String url, String nombre, boolean esPrincipal) {
        this.url = url;
        this.nombre = nombre;
        this.esPrincipal = esPrincipal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean esPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }
}
