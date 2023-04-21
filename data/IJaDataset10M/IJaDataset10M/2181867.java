package ramon.impl;

import org.w3c.dom.Element;
import ramon.util.StackTrace;
import ramon.xml.XmlTranslatable;
import ramon.xml.XmlUtil;
import ramon.ReferenciaArchivo;

/**
 * Para tratar las condiciones de excepción en el procesamiento de subida o <br/>
 * bajada de archivos.
 */
public class FileUploadException extends RuntimeException implements XmlTranslatable {

    private ReferenciaArchivo ref;

    public FileUploadException(String motivo, ReferenciaArchivo ref) {
        super(motivo);
        this.ref = ref;
    }

    public FileUploadException(String motivo, ReferenciaArchivo ref, Throwable causante) {
        super(motivo, causante);
        this.ref = ref;
    }

    public void toXml(Element padre, String nombre) {
        Element root = XmlUtil.crearElemento(padre, nombre);
        XmlUtil.add(root, "archivo", ref);
        XmlUtil.add(root, "motivo", getMessage());
        XmlUtil.add(root, "error", new StackTrace(getCause()));
    }

    public ReferenciaArchivo getReferenciaArchivo() {
        return ref;
    }
}
