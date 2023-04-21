package es.cim.alfrescoClient;

public class Fichero {

    private String nombreFichero;

    private byte[] contenidoFichero;

    public byte[] getContenidoFichero() {
        return contenidoFichero;
    }

    public void setContenidoFichero(byte[] contenidoFichero) {
        this.contenidoFichero = contenidoFichero;
    }

    public String getNombreFichero() {
        return nombreFichero;
    }

    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }
}
