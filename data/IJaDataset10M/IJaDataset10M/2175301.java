package net.sourceforge.coffea.tools.capacities;

import java.util.List;

/** Capacity of handling a types container */
public interface ITypesContainerHandling extends ITypesOwnerContainableHandling, IGroupHandling {

    /**
	 * Returns the list of handlers handling the types belonging to the handled
	 * group
	 * @return List of handlers handling the types belonging to the handled
	 * group
	 */
    public abstract List<ITypeHandling<?, ?>> getTypesHandlers();

    /**
	 * Adds an handler to the list of handlers handling the types belonging to
	 * the handled group
	 * @param clH
	 * New handler to add to the list
	 */
    public abstract void addTypeHandler(ITypeHandling<?, ?> clH);

    /**
	 * Resolves a {@link ITypeHandling handler} for a type given its full name
	 * @param n
	 * {@link ITypeHandling} full name
	 * @return {@link ITypeHandling handler} for the given full name
	 */
    public ITypeHandling<?, ?> resolveTypeHandler(String n);
}
