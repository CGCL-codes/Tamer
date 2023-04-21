package LCCSat;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Classe per realizzare un nodo di una struttura dati a grafo che supporti
 * anche la suddivisione dei nodi in classi di equivalenza.
 *
 * @author Marco Tamassia
 */
public class Node {

    /**
	 * Un nome per il nodo.
	 */
    final String name;

    /**
	 * La lista dei successori di questo nodo.
	 */
    List<Node> successors;

    /**
	 * Il rappresentante di questo nodo.
	 */
    Node find;

    /**
	 * Una lista dei predecessori dei nodi della classe di equivalenza in cui si
	 * trova questo nodo. È mantenuta solo dal rappresentante della classe.
	 */
    Set<Node> ccpar;

    /**
	 * Un idendificatore numerico del nodo. L'univocità è garantita dalla classe Graph.
	 * viene aggiornato se non da una chiamata a toString().
	 */
    int id;

    /**
	 * Usato unicamente per migliorare le prestazioni del metodo toString. Non
	 * viene ricalcolato, se non da una chiamata a toString(). Il campo va
	 * settato a null se vi sono modifiche ai successori.
	 */
    private String toStringCache;

    /**
	 * Crea un nuovo nodo, appartenente ad una classe di equivalenza a sé.
	 *
	 * @param name Un nome per questo nodo.
	 */
    public Node(String name) {
        this.name = name;
        this.successors = new ArrayList<Node>();
        this.find = null;
        this.ccpar = new HashSet<Node>();
        this.id = -1;
        this.toStringCache = null;
    }

    /**
	 * Trova il rappresentante dell'insieme a cui il nodo appartiene.
	 *
	 * @return Il rappresentante di questo nodo.
	 */
    public Node findRepresentative() {
        Node current = this;
        Node representative, temp;
        while (current.find != null) {
            current = current.find;
        }
        representative = current;
        current = this;
        while (current.find != null) {
            temp = current.find;
            current.find = representative;
            current = temp;
        }
        return representative;
    }

    /**
	 * Aggiunge un successore a questo nodo.
	 *
	 * @param x Il nodo che si vuole aggiungere tra i successori di questo.
	 */
    public void addSuccessor(Node x) {
        this.successors.add(x);
        x.ccpar.add(this);
        this.toStringCache = null;
    }

    /**
	 * Resetta il campo ccpar di questo nodo.
	 *
	 * @param x Il nodo che si vuole aggiungere tra i successori di questo.
	 */
    public void resetCCParents() {
        this.ccpar = new HashSet<Node>();
    }

    /**
	 * Unisce l'insieme di cui fa parte questo nodo all'insieme di un altro
	 * nodo.
	 *
	 * @param x Il nodo di cui si vuole unire l'insieme.
	 */
    public void unionClasses(Node y) {
        Node xRepresentative = this.findRepresentative();
        Node yRepresentative = y.findRepresentative();
        if (xRepresentative != yRepresentative) {
            yRepresentative.find = xRepresentative;
            xRepresentative.ccpar.addAll(yRepresentative.ccpar);
            yRepresentative.ccpar.clear();
        }
    }

    /**
	 * Verifica se due nodi sono uguali.
	 *
	 * @param x Il nodo con cui si vuole confrontare questo nodo.
	 * @return <pre>true</pre> se i nodi sono uguali, <pre>false</pre> altrimenti.
	 */
    public boolean equals(Node x) {
        return this.hashCode() == x.hashCode();
    }

    /**
	 * Restituisce il numero di successori di questo nodo.
	 *
	 * @return il numero di successori di questo nodo.
	 */
    public int calcOutDegree() {
        return this.successors.size();
    }

    /**
	 * Svuota la lista dei successori di questo nodo.
	 *
	 */
    public void clearSuccessors() {
        this.successors = new ArrayList<Node>();
    }

    /**
	 * Ottiene la lista dei predecessori di questo nodo, tenendo conto della
	 * classe di equivalenza (quindi restituisce i padri di tutti i nodi nella
	 * stessa classe di equivalenza di questo nodo).
	 *
	 */
    public Set<Node> getCCParents() {
        return this.findRepresentative().ccpar;
    }

    /**
	 * Restituisce una rappresentazione del nodo sotto forma di stringa.
	 *
	 * @return una rappresentazione del nodo sotto forma di stringa.
	 */
    public String toString() {
        Node n;
        StringBuilder str = new StringBuilder(this.successors.size() * 20);
        str.append(this.name);
        if (this.toStringCache == null) {
            if (this.successors.size() > 0) {
                str.append("(");
                n = this.successors.get(0);
                str.append(n.toString());
                for (int i = 1; i < this.successors.size(); i++) {
                    n = this.successors.get(i);
                    str.append("," + n.toString());
                }
                str.append(")");
            }
            this.toStringCache = str.toString();
        }
        return this.toStringCache;
    }

    /**
	 * Calcola un'identificatore del nodo che corrisponda a quello di qualunque
	 * altro nodo con successori equivalenti.
	 * 
	 *
	 * @return un'identificatore del nodo che corrisponda a quello di qualunque
	 * altro nodo con successori equivalenti e nomi uguali.
	 */
    public String calcSignature() {
        Node n;
        String s;
        s = this.name;
        if (this.successors.size() > 0) {
            s += "(";
            n = this.successors.get(0);
            s += n.findRepresentative().id;
            for (int i = 1; i < this.successors.size(); i++) {
                n = this.successors.get(i);
                s += "," + n.findRepresentative().id;
            }
            s += ")";
        }
        return s;
    }
}
