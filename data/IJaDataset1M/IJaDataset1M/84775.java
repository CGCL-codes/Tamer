package net.sf.carmaker.assemblyline;

import net.sf.carmaker.orders.IOrder;

/**
 * Interface f�r eine Fertigungsstation
 * 
 * @author mhoefel, mmann
 *
 */
public interface IAssemblyStation {

    /**
	 * Simuliert einen Zeitschritt, in dem ein Auftrag abgearbeitet wird. Ist die AssemblyStation 
	 * mit der Bearbeitung des aktuellen {@linkplain IOrder Auftrags} fertig, wird <code>true</code>
	 * zur�ckgegeben.
	 *  
	 * @return boolean der aussagt ob Station fertig.
	 */
    public boolean tick();

    /**
	 * Entfernt aktuellen Auftrag aus der Station und gibt ihn zur�ck. Um den Auftrag entfernen zu 
	 * k�nnen muss er abgearbeitet sein und es muss in der Station ein Auftrag gesetzt sein.
	 * 
	 * @return Aktueller Auftrag der Station
	 * @throws IllegalStateException wenn der Auftrag noch nicht abgearbeitet wurde, oder kein Auftrag enthalten ist.
	 */
    public IOrder removeOrder();

    /**
	 * Setzt aktuellen Auftrag f�r die Station. Um den aktuellen Auftrag setzen zu k�nnen, muss die 
	 * Station leer sein.
	 * @param order Zu setzender Auftrag
	 * @throws IllegalStateException wenn die Station noch nicht leer ist.
	 */
    public void setOrder(IOrder order);

    /**
	 * Gibt <code>true</code> zur�ck, wenn die Station leer ist, sprich wenn kein {@linkplain IOrder Auftrag} gesetzt 
	 * ist.
	 * @return boolean der Aussagt, ob Station leer ist. 
	 */
    public boolean isEmpty();

    /**
     * Vergleich zweier AssemblyStations
     * @param otherObject
     * @return true/false Objekt gleich ja/nein
     */
    public boolean equals(Object otherObject);
}
