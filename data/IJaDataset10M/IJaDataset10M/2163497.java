package aplicacion.fisica.documentos;

import java.io.Serializable;
import componentes.gui.imagen.figuras.Figura;

public class Anotacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Documento documento;

    private Figura contenido;

    private String usuario;

    private String rol;

    public Anotacion() {
    }

    public void setDocumento(Documento doc) {
        documento = doc;
    }

    public void setContenido(Figura cont) {
        contenido = cont;
    }

    public Documento getDocumento() {
        return documento;
    }

    public Figura getContenido() {
        return contenido;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
