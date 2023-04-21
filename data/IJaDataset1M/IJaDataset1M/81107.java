package de.uni_trier.st.nevada.layout.hierarchic;

import java.util.List;

public interface LOrder {

    public void setLayer(int pLayer, List<HNode> pComplete);

    /**
	 * F�gt den angegebenen Knoten der Schicht mit Index pLayer hinzu. Die
	 * Schichten werden (anders als bei Vektoren) von 1 an gez�hlt, um mit
	 * Implementierungen von LayerDistributor zurechtzukommen. Die Reihenfolge
	 * des Einf�gens �ndert das Endergebnis!
	 *
	 * @param pLayer -
	 *            Der Index der Schicht, zu der zugewiesen werden soll
	 * @param pNode -
	 *            Der Knoten der zugewiesen wird.
	 * @param pGraph -
	 *            Der zugrundeliegende Graph
	 */
    public void addToLayer(int pLayer, HNode pNode, HGraph pGraph);

    public void setToLayer(int pLayer, HNode pNode, HGraph pGraph, int position);

    /**
	 * Liefert die Ordnungszahl des angegeben Knotens auf seiner Schicht zur�ck.
	 * Ein Knoten der beispielsweise in der Mitte seiner Schicht vorhanden ist
	 * w�rde unter dieser Methode den Wert 0 erhalten.
	 *
	 * @param pNode -
	 *            Der Knoten, dessen Ordnung berechnet werden soll
	 * @return - Die Ordnungszahl des Knotens
	 */
    public int getPositionOnLayer(HNode pNode);

    /**
	 * Liefert die Nummer der Schicht zur�ck, auf der der angegebene Knoten
	 * positioniert wurde
	 *
	 * @param pNode -
	 *            Der Knoten des Schichtindex berechnet werden soll
	 * @return - Die Nummer der Schicht, die pNode enth�lt.
	 */
    public int getNodeLayer(HNode pNode);

    /**
	 * Testet, ob der angegebene Knoten in der aktuellen Ordnung enthalten ist.
	 *
	 * @param pNode -
	 *            Der zu testende Knoten.
	 * @return - true, wenn pNode in der aktuellen Ordnung enthalten ist, sonst
	 *         false
	 */
    public boolean containsNode(HNode pNode);

    /**
	 * Gibt die Anzahl der Schichten der aktuellen Ordnung zur�ck
	 *
	 * @return - Die Anzahl der Schichten
	 */
    public int countLayer();

    /**
	 * Gibt die Gr��e der Schicht mit Nummer pLayer zur�ck. Hinweis: Der Index
	 * pLayer wird nicht von 0, sondern von 1 an gez�hlt.
	 *
	 * @param pLayer -
	 *            Die zu testende Schicht.
	 * @return - Die Gr��e (Anzahl der Knoten auf der Schicht) der Schicht
	 */
    public int countLayerSize(int pLayer);

    /**
	 * Sortiert eine Schicht neu.
	 *
	 * @param pLayer -
	 *            Die Nummer der zusortierenden Schicht
	 * @param pUpwards -
	 *            true, wenn die Nachfolgerknoten betrachtet werden sollen (von
	 *            unten nach oben), false bei anderer Richtung
	 * @param pSortStrength -
	 *            Die St�rke der Sortierung (0= keine Sortierung, 1= volle
	 *            Sortierung).
	 * @param pGraph -
	 *            Der zugrundeliegende Graph
	 */
    public void sortLayer(int pLayer, boolean pUpwards, double pSortStrength, HGraph pGraph);

    /**
	 * Speichert die aktuelle Konfiguration in einem Stack.
	 *
	 */
    public void backup();

    /**
	 * Setzt die aktuelle Konfiguration auf die zuletzt gespeicherte zur�ck und
	 * l�scht deren Eintrag.
	 *
	 */
    public void restore();

    public List<HNode> getLayer(int i);

    /**
	 * Liefert den Knoten an der gegebenen Matrixposition zur�ck.
	 *
	 * @param pLayerIndex -
	 *            Der entsprechende Rang
	 * @param pLayerOrderIndex -
	 *            Die Position innerhalb des Ranges
	 * @return - Den Knoten an der betreffenden Position
	 */
    public HNode getNode(int pLayerIndex, int pLayerOrderIndex);
}
