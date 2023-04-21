package de.rauchhaupt.games.poker.holdem.lib.remoteplayer.server.wsstubs;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.rauchhaupt.games.poker.holdem.lib.remoteplayer.server.wsstubs package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.rauchhaupt.games.poker.holdem.lib.remoteplayer.server.wsstubs
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ClientHoldemPlayerInfoForPlayer }
     * 
     */
    public ClientHoldemPlayerInfoForPlayer createClientHoldemPlayerInfoForPlayer() {
        return new ClientHoldemPlayerInfoForPlayer();
    }

    /**
     * Create an instance of {@link StringArray }
     * 
     */
    public StringArray createStringArray() {
        return new StringArray();
    }

    /**
     * Create an instance of {@link HoldemTableParameters }
     * 
     */
    public HoldemTableParameters createHoldemTableParameters() {
        return new HoldemTableParameters();
    }

    /**
     * Create an instance of {@link ClientHoldemPokerResult }
     * 
     */
    public ClientHoldemPokerResult createClientHoldemPokerResult() {
        return new ClientHoldemPokerResult();
    }

    /**
     * Create an instance of {@link ClientHoldemPlayerInfoForPlayerArray }
     * 
     */
    public ClientHoldemPlayerInfoForPlayerArray createClientHoldemPlayerInfoForPlayerArray() {
        return new ClientHoldemPlayerInfoForPlayerArray();
    }
}
