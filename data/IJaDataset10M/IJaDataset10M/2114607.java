package uk.ac.shef.wit.textractor.model;

public interface Corpus extends Iterable<Document> {

    boolean add(Document d);

    boolean remove(Document d);

    boolean contains(Document d);

    int size();
}
