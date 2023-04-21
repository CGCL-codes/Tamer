package fr.unice.produit;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.unice.produit package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.unice.produit
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Jeu }
     * 
     */
    public Jeu createJeu() {
        return new Jeu();
    }

    /**
     * Create an instance of {@link Livre }
     * 
     */
    public Livre createLivre() {
        return new Livre();
    }

    /**
     * Create an instance of {@link Video }
     * 
     */
    public Video createVideo() {
        return new Video();
    }

    /**
     * Create an instance of {@link Bibliotheque }
     * 
     */
    public Bibliotheque createBibliotheque() {
        return new Bibliotheque();
    }

    /**
     * Create an instance of {@link Audio }
     * 
     */
    public Audio createAudio() {
        return new Audio();
    }

    /**
     * Create an instance of {@link Produit }
     * 
     */
    public Produit createProduit() {
        return new Produit();
    }
}
