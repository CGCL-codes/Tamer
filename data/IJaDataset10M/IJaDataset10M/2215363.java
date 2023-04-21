package es.realtimesystems.simplemulticast;

/**
 * La interfaz PTMFCipher se utiliza para proporcionar seguridad al Canal Multicast.
 * Su �nica misi�n es proporcionar dos objetos javax.crypto.Cipher para codificar
 * y descodificar, adem�s de un m�todo de iniciaci�n del codificador y del descodificador.
 * En este m�todo se deber� llamar al m�todo init() del los objetos javax.crypto.Cipher.
 */
public interface PTMFCipher {

    public javax.crypto.Cipher getCipher();

    public javax.crypto.Cipher getUncipher();

    public void init();
}
