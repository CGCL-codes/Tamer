package edu.mit.lcs.haystack.rdf;

/**
 * @author Dennis Quan
 */
public interface IURIGenerator {

    public Resource generateNewResource();

    public Resource generateUnknownResource();

    public Resource generateAnonymousResource();
}
