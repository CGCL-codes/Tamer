package datos.archivo;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author luciano
 */
public interface PersistenteEnDisco {

    public boolean leer() throws FileNotFoundException;

    public boolean guardar() throws FileNotFoundException;
}
