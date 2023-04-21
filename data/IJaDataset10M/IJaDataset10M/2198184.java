package freestyleLearningGroup.independent.gui;

/**
 * <code>FLGObjectPool</code> repr&auml;sentiert einen Pool von Objekten, aus dem man sich nach
 * Bedarf bedienen kann (Objekte aus dem Pool nehmen) und in den man die nicht mehr gebrauchten
 * Objekte wieder zur&uuml;cklegt. Der Vorteil dieser Objektverwaltung als Pool ist eine mehrfache Benutzung von Objekten,
 * sprich: Ein Objekt wird von verschiedenen Stellen benutzt,aber nur einmal erzeugt und am Ende wieder vernichtet.<p> <p>
 * <b>Beispiel</b>: Ein Objektpool von Punkten beschleunigt bspw. die Grafikausgabe des <code>FLGCoordSystem</code>. <p><pre>
 *		FLGObjectPool pointPool;
 *		pointPool = new FLGObjectPool(100) {
 *			public Object newInstance() {
 *				return new FLGPoint(0,0,0);
 *			}
 *		};
 * </pre><p>
 */
public abstract class FLGObjectPool {

    private Object[] objects;

    private int noObjectsInPool = 0;

    private int maxNoObjects;

    /**
     * Erzeugt einen <code>FLGObjectPool</code> der initial leer ist und maximal die spezifizierte
     * Anzahl Objekte aufnehmen kann.
     * @param	maxNoObject	die maximale Anzahl Objekte, die in diesem Pool gespeichert werden sollen0.
     */
    public FLGObjectPool(int maxNoObjects) {
        objects = new Object[maxNoObjects];
        this.maxNoObjects = maxNoObjects;
    }

    /**
     * Da nur der Benutzer dieser Klasse weiss, was f&uuml;r Objekte er in diesem Objektppol
     * speichern m&ouml;chte, muss dieser Methode, die eine neue Instanz liefert, von ihm implementiert werden.
     * @return	ein neues Objekt.
     */
    public abstract Object newInstance();

    /**
     * Ein Objekt wird zur Benutzung aus diesem Objektpool genommen.
     * @return	ein Objekt.
     */
    public Object getNewObject() {
        Object object;
        if (noObjectsInPool > 0) {
            object = objects[noObjectsInPool - 1];
            objects[noObjectsInPool - 1] = null;
            noObjectsInPool--;
        } else {
            object = newInstance();
        }
        return object;
    }

    /**
     * Das spezifizierte Objekt wird wieder zur&uuml;ck in den Objektpool gelegt.
     * @param	object	das nicht mehr gebrauchte Objekt.
     */
    public void releaseObject(Object object) {
        if (noObjectsInPool < maxNoObjects) objects[noObjectsInPool++] = object;
    }
}
