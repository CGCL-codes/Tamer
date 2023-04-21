package org.openscience.cdk.interfaces;

/**
 * Classes that implement this interface of a scheme. 
 * This is designed to contain a set of reactions which are linked in 
 * some way but without hard coded semantics.
 *
 * @author      miguelrojasch <miguelrojasch@yahoo.es>
 * @cdk.module  interfaces
 * @cdk.githash
 */
public interface IReactionScheme extends IReactionSet {

    /**
	 * Add a scheme of reactions.
	 * 
	 * @param reactScheme The IReactionScheme to include
	 */
    public void add(IReactionScheme reactScheme);

    /**
	 *  Returns an Iterable for looping over all IMolecularScheme
	 *   in this ReactionScheme.
	 *
	 * @return    An Iterable with the IMolecularScheme in this ReactionScheme
	 */
    public Iterable<IReactionScheme> reactionSchemes();

    /**
	 * Returns the number of ReactionScheme in this Scheme.
	 *
	 * @return     The number of ReactionScheme in this Scheme
	 */
    public int getReactionSchemeCount();

    /**
	 * Removes all IReactionScheme from this chemObject.
	 */
    public void removeAllReactionSchemes();

    /**
	 * Removes an IReactionScheme from this chemObject.
	 *
	 * @param  scheme  The IReactionScheme to be removed from this chemObject
	 */
    public void removeReactionScheme(IReactionScheme scheme);

    /**
	 * Clones this IReactionScheme object and its content.
	 *
	 * @return    The cloned object
	 */
    public Object clone() throws CloneNotSupportedException;
}
