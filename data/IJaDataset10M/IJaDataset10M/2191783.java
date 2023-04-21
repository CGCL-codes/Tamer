package edu.princeton.wordnet.pojos.app;

/**
 * @author bbou
 */
public interface Processor<T> {

    void process(T o);
}
