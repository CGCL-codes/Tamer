package util.consola;

/**
 * Un EntradaReader notifica a un ReaderListener
 * cuando le es llamado su m�todo read(char[], int, int).
 */
public interface ReaderListener {

    /**
 * Llamado con true cuando a un EntradaReader se le llama
 * su read(char[],int,int); con false cuando se
 * termina este read.
 */
    public void waitingRead(boolean t);
}
