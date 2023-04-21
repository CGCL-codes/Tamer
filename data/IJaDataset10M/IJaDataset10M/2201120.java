package org.poker.prophecy.math;

import java.util.Vector;

/**
 * Baut einen Baum auf, um alle Permutationen einer Menge zu ermitteln.
 * Achtung: Da die Menge der Permutationen einer Menge m mit k Elementen k Fakult�t ist,
 * erreicht die Methode sehr schnell Speicher-(und Vectorausma�)-Grenzen
 * 
 * Beispiel: Menge M := [a,b,c]
 * Aufruf: new TreeKnot([a,b,c], new Vector(<TreeKnot>))
 * Aufbau:
 *        root
 *        / \   \
 *       a   b   c
 *      / \ / \ / \
 *      b c a c a b
 *      | | | | | |
 *      c b c a b a
 * Jeder Knoten des Baums ist dabei ein neues TreeKnotElement.
 * Die Trace eines Knoten gibt die von der Wurzel aus durchlaufenen
 * Elemente bis zum jeweiligen Knoten an.
 * S�mtliche Knoten werden in den �bergebenen allKnotsVector gestored.
 * Damit ist es m�glich, draus sp�ter alle Permutationen (=alle Knoten mit Traces der l�nge 3) zu ermitteln
 *  
 * @author bg
 */
public class TreeKnot {

    Vector<TreeKnot> sucs = new Vector<TreeKnot>();

    Vector<Object> knotVector = null;

    Vector<Object> trace = new Vector<Object>();

    Vector<Object> trace2 = new Vector<Object>();

    Object knotvalue;

    /**
     * Baut einen Baum aller Permutationen auf �ber die �bergebene Menge.
     * S�mtliche entstandenen Knoten werden im allKnotsVector gespeichert.
     * @param menge
     * @param allKnotsVector
     */
    public TreeKnot(Menge menge, Vector<TreeKnot> allKnotsVector) {
        knotVector = new Vector<Object>();
        knotvalue = null;
        for (int i = 0; i < menge.size(); i++) {
            knotVector.addElement(menge.elementAt(i));
        }
        Object aktElement = null;
        Vector<Object> aktWorkKnots = null;
        for (int i = 0; i < knotVector.size(); i++) {
            aktElement = knotVector.elementAt(i);
            aktWorkKnots = (Vector<Object>) knotVector.clone();
            aktWorkKnots.remove(i);
            this.sucs.addElement(new TreeKnot(aktElement, aktWorkKnots, trace, allKnotsVector));
        }
        allKnotsVector.addElement(this);
    }

    /**
     * Privater Konstruktor f�r den Algorithmus, der �hnlich der Tiefensuche vorgeht
     * @param knotvalue
     * @param knotVector
     * @param otrace
     * @param allKnotsVector
     */
    private TreeKnot(Object knotvalue, Vector<Object> knotVector, Vector<Object> otrace, Vector<TreeKnot> allKnotsVector) {
        for (int i = 0; i < otrace.size(); i++) {
            this.trace.addElement(otrace.elementAt(i));
        }
        this.knotvalue = knotvalue;
        this.trace.addElement(knotvalue);
        this.knotVector = knotVector;
        Object aktElement = null;
        Vector<Object> aktWorkKnots = null;
        for (int i = 0; i < knotVector.size(); i++) {
            aktElement = knotVector.elementAt(i);
            aktWorkKnots = (Vector<Object>) knotVector.clone();
            aktWorkKnots.remove(i);
            this.sucs.addElement(new TreeKnot(aktElement, aktWorkKnots, trace, allKnotsVector));
        }
        allKnotsVector.addElement(this);
    }

    public String toString() {
        return ("Knot: " + knotvalue + ", KV: " + knotVector + ", Trace: " + trace + ", TSize:" + trace.size());
    }
}
