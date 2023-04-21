package fido.linguistic.components;

import java.util.*;
import fido.db.ArticleTable;

/**
 * Represents a noun, with all associated components such as an
 * optional article, all of the adjectives, and all prepositional
 * phrases.
 */
public class NounPhrase implements Noun {

    /** Constant used by getAction() and setAction() */
    public static final int EXISTING_INSTANCE = 1;

    /** Constant used by getAction() and setAction() */
    public static final int CREATE_INSTANCE = 2;

    /** Constant used by getAction() and setAction() */
    public static final int EXISTING_CLASS = 3;

    private int action;

    private int articleType;

    private NounClass noun;

    private Vector adjectives;

    private Vector prepositionalPhrases;

    /**
	 * Creates a new NounPhrase object with no adjectives or prepositional
	 * phrases.  Sets the article to none.
	 */
    public NounPhrase() {
        articleType = ArticleTable.NO_ARTICLE;
        adjectives = new Vector();
        prepositionalPhrases = new Vector();
    }

    /**
	 * Returns the action the system should perform when resolving this
	 * noun phrase.  For a detail description on the value returned, see
	 * setAction().
	 * 
	 * @return The action for the system to perform for this noun phrase.
	 * 
	 * @see #setAction(int)
	 */
    public int getAction() {
        return action;
    }

    /**
	 * When resolving nouns in the Resolver module, this method is used to
	 * store the action the system should take when resolving the noun.  The
	 * system could do the following:
	 * 
	 * <UL>
	 * <LI> <B>Create an instance</B> - For a noun with an indefinite article,
	 *      the system will create a new instance for the noun to reference.
	 *      An example sentence: <I>John bought a dog</I>.  Since the speaker 
	 *      used an indefinte article for the noun phrase <I>a dog</I>, the
	 *      listener assumes this is a dog that speaker and listener has not
	 *      discussed before.  Therefore, the system needs to create a new
	 *      instance of dog.
	 *
	 * <LI> <B>Use an existing instance</B> - For a noun with a definite article,
	 *      the system will search for an existing instance for the noun to
	 *      reference.  An example sentence <I>The dog chased the ball</I>.
	 *      Since the speaker used the definite article for both <I>the dog</I>
	 *      and <I>the ball</I>, the listener can assume both objects are already
	 *      known.  Therefore, the system needs to search for an instance
	 *      in the Discourse and the Object Tree.
	 *      
	 * <LI> <B>Use an existing class</B> - Certain times a noun carries an
	 *      indefinite article, the noun phrase will refer to a class of objects.
	 *      An example sentence: <I>The dog is a Poodle.</I>  Here the noun
	 *      phrase <I>a Poodle</I> refers to a class.  The eventual action
	 *      of the sentence will be to create an instance of the Poodle class.
	 * </UL>
	 * 
	 * See the resolve() method in the NounResolver module for a detain description
	 * on Sentence Patterns and how Nouns are resolved.
	 * 
	 * @see fido.linguistic.Discourse
	 * @see fido.db.ObjectTable
	 * @see fido.linguistic.NounResolver#resolve(NounPhrase)
	 */
    public void setAction(int action) {
        this.action = action;
    }

    /**
	 * Returns the type of the article.  Constant values are defined in
	 * ArticleTable.
	 * 
	 * @return The type of article for this noun phrase.
	 * 
	 * @see fido.db.ArticleTable
	 */
    public int getArticleType() {
        return articleType;
    }

    /**
	 * Sets the type for the article.  Constant values are defined in
	 * ArticleTable.
	 * 
	 * @see fido.db.ArticleTable
	 */
    public void setArticleType(int type) {
        articleType = type;
    }

    /**
	 * Returns the NounClass for this noun phrase.  This is the actual
	 * noun string and any associated Morphology tags.
	 * 
	 * @return The NounClass for this noun phrase.
	 */
    public NounClass getNounClass() {
        return noun;
    }

    /**
	 * Sets the NounClass for this noun phrase.  This is the actual
	 * noun string and any associated Morphology tags.
	 * 
	 * @param noun The NounClass for this noun phrase.
	 */
    public void setNounClass(NounClass noun) {
        this.noun = noun;
    }

    /**
	 * Returns an Iterator over all of the AdjectiveClass modifying
	 * this noun phrase.
	 * 
	 * @return An Iterator of AdjectiveClass classes.
	 */
    public Iterator getAdjectives() {
        return adjectives.iterator();
    }

    /**
	 * Adds an AdjectiveClass to this noun phrase.  The AdjectiveClass
	 * represents an adjective modifying the noun.
	 * 
	 * @param adj Adjective to add.
	 */
    public void addAdjective(AdjectiveClass adj) {
        adjectives.add(adj);
    }

    /**
	 * Returns an Iterator over all of the PrepositionalPhrases modifying
	 * this noun phrase.
	 * 
	 * @return An Iterator of PrepositionalPhrase classes.
	 */
    public Iterator getPrepositionalPhrases() {
        return prepositionalPhrases.iterator();
    }

    /**
	 * Adds an PrepositionalPhrase to this noun phrase.  The PrepositionalPhrase
	 * represents an adjectivial prepositional phrase modifying the noun.
	 * 
	 * @param phrase PrepositionalPhrase to add.
	 */
    public void addPrepositionalPhrase(PrepositionalPhrase phrase) {
        prepositionalPhrases.add(phrase);
    }
}
