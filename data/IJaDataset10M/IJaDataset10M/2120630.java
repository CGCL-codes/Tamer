package CED;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class Node {

    String word;

    HashMap<String, Node> nexts;

    Vector<Integer> documents;

    double impactValue;

    int documentCount, nextCount;

    boolean isActive;

    public Node(String _word, double _impactValue) {
        word = _word;
        nexts = new HashMap<String, Node>();
        documents = new Vector<Integer>();
        impactValue = _impactValue;
        isActive = true;
    }

    public boolean hasNext(String word) {
        return nexts.containsKey(word);
    }

    public double getImpactValue() {
        return impactValue;
    }

    public Node getNext(String word) {
        return nexts.get(word);
    }

    public int getDocumentCount() {
        return this.documentCount;
    }

    public int getNextCount() {
        return this.nextCount;
    }

    public void addNext(String word, double impactValue) {
        nexts.put(word, new Node(word, impactValue));
        this.nextCount++;
    }

    public void removeNext(String word) {
        nexts.remove(word);
        this.nextCount--;
    }

    public void addDocument(int documentNo) {
        documents.add(new Integer(documentNo));
        this.documentCount++;
    }

    public void removeDocument(int documentNo) {
        documents.remove(new Integer(documentNo));
        this.documentCount--;
    }

    public Iterator<Integer> getDocumentIterator() {
        return documents.iterator();
    }
}
