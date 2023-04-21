package andre.grids.filesystem.common.exceptions;

import java.rmi.RemoteException;

/**
 *
 * @author andre
 */
public class DirectoryNotFound extends RemoteException implements java.io.Serializable {

    public DirectoryNotFound(String dirName) {
        super("O diretório: " + dirName + " não foi encontrado.");
    }
}
