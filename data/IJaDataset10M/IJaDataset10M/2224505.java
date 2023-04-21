package business;

import stub.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author nacho
 */
public class Servidor extends Thread {

    File archivo;

    public File open(String filename, boolean modo) throws IOException {
        System.out.println("Archivo que pide abrir: " + filename);
        archivo = new File(filename);
        if (modo == Protocolo.WRITE) {
            if (!archivo.exists()) {
                return archivo;
            } else {
                return null;
            }
        } else {
            if (archivo.exists()) {
                return archivo;
            } else {
                return null;
            }
        }
    }

    public OpenFile read(OpenFile archivo, byte[] data) throws FileNotFoundException, IOException {
        DataInputStream stream = new DataInputStream(new FileInputStream(archivo.getArchivo()));
        stream.skip(archivo.getSeek());
        int leidos = stream.read(data);
        archivo.setSeek(archivo.getSeek() + leidos);
        return archivo;
    }

    public synchronized void write(OpenFile archivo, byte[] data, int cargautil) throws IOException {
        FileOutputStream stream = new FileOutputStream(archivo.getArchivo(), true);
        stream.write(data, 0, cargautil);
    }
}
