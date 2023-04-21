package layer;

/**
 * Objekte einer Klasse, die dieses Interface implementiert, verf�gen �ber die
 * M�glichkeit sich selbst zu serialisieren bzw. zu deserialisieren.
 * 
 * @author Yark Schroeder, Manuel Scholz
 * @version $Id: ISerializable.java,v 1.2 2006/03/30 09:39:50 yark Exp $
 * @since 1.3
 */
public interface ISerializable {

    /**
	 * Serialisiert die Nachricht.
	 * 
	 * @return serialisiertes Objekt als ByteArray
	 * @since 1.3
	 */
    public byte[] serialize();

    /**
	 * Deserialisiert das �bergebene ByteArray und f�llt das Objekt, �ber das
	 * diese Methode aufgerufen wurde, mit den wieder hergestellten
	 * Informationen.
	 * 
	 * @param abyData
	 *            serialisierte Nachricht als ByteArray
	 * @return true, wenn der Vorgang erfolgreich, sonst false
	 * @since 1.3
	 */
    public boolean unserialize(byte[] abyData);
}
