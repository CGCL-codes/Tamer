package ramon.ext.upload;

import org.w3c.dom.Element;
import ramon.ReferenciaArchivo;
import ramon.xml.XmlUtil;

public class ReferenciaMemoria implements ReferenciaArchivo {

    private String nombre;

    private long tamanio;

    private byte[] contenido;

    public ReferenciaMemoria(String nombre, byte[] contenido) {
        this.nombre = nombre;
        this.contenido = contenido;
        tamanio = contenido != null ? contenido.length : 0;
    }

    public String getId() {
        return null;
    }

    public String getNombre() {
        return nombre;
    }

    public byte[] getContenido() {
        return contenido;
    }

    public long getTamanio() {
        return tamanio;
    }

    public String toString() {
        return "Nombre: " + nombre + " Tamaño: " + tamanio;
    }

    public void toXml(Element padre, String nombre) {
        Element root = XmlUtil.crearElemento(padre, nombre);
        XmlUtil.add(root, "nombre", nombre);
        XmlUtil.add(root, "id", "na");
        XmlUtil.add(root, "tamanio", tamanio);
    }
}
