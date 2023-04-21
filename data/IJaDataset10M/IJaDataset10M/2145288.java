package org.eyrene.javaj.sync;

import java.util.Vector;

/**
 * <p>Title: NodoThread con Priorita'</p>
 * <p>Description: descrittore di un elemento della coda</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo
 */
class NodoThread_P {

    Thread id;

    int priority;

    boolean sveglia;

    /**Costruttore del NodoThread con Priorita'
 * @param id identificativo del thread
 * @param priority priorita' del thread (numeri piccoli -> priorita' alta)
 * @param sveglia vale 'true' se il thread va svegliato*/
    NodoThread_P(Thread id, int priority, boolean sveglia) {
        this.id = id;
        this.priority = priority;
        this.sveglia = sveglia;
    }
}

/**
 * <p>Title: Semaforo Binario con Priorita'</p>
 * <p>Description: semaforo con priorita'</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo
 */
public class SemaforoB_P {

    /**Valore del semaforo*/
    private int contatore;

    /**Vettore contenente i thread in attesa*/
    private Vector codaAPriorita = new Vector();

    /**Costruttore del Semaforo Binario con Priorita'
 * @param contatore valore iniziale del semaforo*/
    public SemaforoB_P(int contatore) {
        if (contatore < 0 || contatore > 1) throw new RuntimeException("Bad Initialization Exception!");
        this.contatore = contatore;
    }

    /**Esame del semaforo e attesa se e' rosso
 * @param priority thread con priorita' + bassa verranno messi in attesa*/
    public synchronized void P(int priority) {
        if (contatore == 0) {
            if (codaAPriorita.size() > 0) {
                NodoThread_P nodo = (NodoThread_P) codaAPriorita.firstElement();
                if (nodo.sveglia && priority < nodo.priority) {
                    nodo.sveglia = false;
                }
            } else {
                int pos = 0;
                while (pos < codaAPriorita.size() && ((NodoThread_P) codaAPriorita.elementAt(pos)).priority <= priority) pos++;
                codaAPriorita.insertElementAt(new NodoThread_P(Thread.currentThread(), priority, false), pos);
                while (true) {
                    try {
                        wait();
                    } catch (InterruptedException ignored) {
                    }
                    ;
                    NodoThread_P nodo = (NodoThread_P) codaAPriorita.firstElement();
                    if (nodo.id == Thread.currentThread() && nodo.sveglia) {
                        codaAPriorita.removeElementAt(0);
                        break;
                    }
                }
            }
        }
        contatore = 0;
    }

    /**Il semaforo viene messo a verde*/
    public synchronized void V() {
        if (contatore == 0) {
            if (codaAPriorita.size() == 0) contatore = 1; else {
                ((NodoThread_P) codaAPriorita.firstElement()).sveglia = true;
                notifyAll();
            }
        }
    }
}
