package arqueologia.model;

import arqueologia.controller.Director;
import arqueologia.view.juego.Juego;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Diana
 */
public class PersistenciaArchivo implements Serializable {

    public static void guardarArchivo(Juego principal, String archivo) throws FileNotFoundException, IOException {
        ObjectOutputStream Escritor = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(archivo)));
        Escritor.writeObject(principal);
        Escritor.close();
    }
}
