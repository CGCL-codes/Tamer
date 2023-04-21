package org.jcryptool.visual.zeroknowledge.algorithm.feigefiatshamir;

import org.jcryptool.visual.zeroknowledge.algorithm.Funcs;
import org.jcryptool.visual.zeroknowledge.algorithm.Modell;

/**
 * Klasse, die die Funktionalität bereit stellt, das Fiat-Shamir-Protokoll mehrmals am Stück ohne
 * Parameterüberwachung durchzuführen.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class FFSFuncs implements Funcs {

    private FFSAlice alice;

    private FFSBob bob;

    private FFSCarol carol;

    private boolean secretKnown;

    /**
     * Konstruktor, der ein Alice-Objekt und ein Modell erhält
     * 
     * @param alice2 Alice-Objekt, dessen Paramter genommen werden sollen
     * @param m Modell-Objekt mit den Werten der beiden Primzahlen p und q
     */
    public FFSFuncs(FFSAlice alice2, Modell m) {
        secretKnown = true;
        int length = alice2.getSecret().length;
        alice = new FFSAlice(length);
        alice.setN(m.getP(), m.getQ());
        alice.generateSecret();
        carol = new FFSCarol(length);
        carol.setN(m.getP(), m.getQ());
        carol.generateSecret();
        bob = new FFSBob(length);
        bob.setN(m.getP(), m.getQ());
    }

    /**
     * Führt value-mal das Feige-Fiat-Shamir-Protokoll durch. Liefert als Ergebnis die Anzahl der
     * geglückten Antworten
     * 
     * @param value Anzahl, wie oft das Protokoll ausgeführt werden soll
     * @return Anzahl, wie oft erfolgreich verifiziert wurde
     * @see Funcs#protokoll(int)
     */
    public int protokoll(int value) {
        FFSPerson p;
        if (secretKnown) p = alice; else p = carol;
        int correct = 0;
        bob.setV(p.getV());
        for (int i = 1; i <= value; i++) {
            p.generateR();
            bob.receiveX(p.getX());
            bob.generateB();
            p.calculateY(bob.getB());
            bob.setToVerify(p.getY());
            if (bob.verify()) {
                correct++;
            }
        }
        return correct;
    }

    /**
     * Setzt, ob das Geheimnis bekannt ist oder nicht
     * 
     * @param isAlice true, wenn das Geheimnis als bekannt eingestuft werden soll
     * @see Funcs#setSecretKnown(boolean)
     */
    public void setSecretKnown(boolean isAlice) {
        this.secretKnown = isAlice;
    }
}
