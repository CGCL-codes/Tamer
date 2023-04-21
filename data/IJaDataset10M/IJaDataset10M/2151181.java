package andre.grids.filesystem.common.exceptions;

/**
 *
 * @author andre
 */
public class DirectoryNotEmpty extends java.rmi.RemoteException implements java.io.Serializable {

    public DirectoryNotEmpty(String dirName, String operation) {
        super("O diretório: " + dirName + " não está vazio e não pode receber a operação: " + operation);
    }
}
