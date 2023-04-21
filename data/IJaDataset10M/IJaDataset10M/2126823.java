package stub;

import java.io.Serializable;

/**
 *
 * @author nacho
 */
public class Mensaje implements Serializable {

    int codOp;

    String nombreArchivo;

    boolean write;

    int archivo;

    byte[] data;

    long longCargaUtil;

    int status;

    public Mensaje() {
        codOp = 0;
        nombreArchivo = new String();
        data = new byte[Protocolo.TAMANIO_BLOQUE];
        status = 0;
        write = false;
    }

    public int getArchivo() {
        return archivo;
    }

    public void setArchivo(int archivo) {
        this.archivo = archivo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int codError) {
        this.status = codError;
    }

    public int getCodOp() {
        return codOp;
    }

    public void setCodOp(int codOp) {
        this.codOp = codOp;
    }

    public byte[] getData() {
        return data;
    }

    public long getLongitudCargaUtil() {
        return longCargaUtil;
    }

    public void setData(byte[] data, long longitud) {
        this.data = data;
        longCargaUtil = longitud;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }
}
