package de.laidback.racoon.controller;

import java.util.List;

/**
 * Bildet das Interface f�r eine zur�ckgehende Antwort. 
 * 
 * @author Thomas
 *
 */
public interface IResponse extends Cloneable {

    /**
	 * F�gt einen Parameter zum Response hinzu. Dieser
	 * wird dann als innerer Tag im Response-Tag gerendert.
	 * 
	 * @param key Der Name bzw. Schl�ssel des Parameters
	 * @param value Der Wert des Parameters.
	 */
    public void addParam(String key, Object value);

    /**
	 * Setzt den Funktionsnamen.
	 */
    public void setFunctionName(String functionName);

    /**
	 * Liefert wie toString() den Fuktionsnamen
	 * der Antwort.
	 * 
	 * @return
	 */
    public String getFunctionName();

    /**
	 * F�gt einen weiteren Empf�nger der Nachricht hinzu.
	 * Es muss darauf geachtet werden, dass ein Empf�nger
	 * nicht doppelt hinzugef�gt werden kann.
	 * 
	 * @param receiver
	 */
    public void addReceiver(IReceiver receiver);

    /**
	 * Liefert die Liste der Emfp�nger der Nachricht. Diese
	 * Methode wird vom Network-Manager benutzt, um auf die
	 * tats�chlichen NetworkClients zu kommen.
	 * 
	 * @return
	 */
    public List<IReceiver> getReceivers();

    public IResponse getClone();

    public void checkIn();

    public void setCommandHandler(CommandHandler cmdHandler);

    public void clear();
}
