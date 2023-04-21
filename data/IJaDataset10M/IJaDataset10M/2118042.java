package com.nokia.ats4.appmodel.model.domain.usecase;

import com.nokia.ats4.appmodel.model.domain.Transition;
import java.util.List;

/**
 * UseCase represents a sub set of States and Transitions of a KendoModel that
 * form a logical whole, i.e. a use case. A single use case may contain several
 * {@link com.nokia.ats4.appmodel.model.domain.usecase.UseCasePath use case paths} or
 * just one path.
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public interface UseCase {

    /**
     * Get the unique ID of the use case.
     */
    public long getId();

    /**
     * Set the name of this use case. Name must not be <code>null</code> or
     * empty String.
     *
     * @param name Use case name String
     * @throws IllegalArgumentException If name is null or empty String
     */
    public void setName(String name);

    /**
     * sets the unique ID of the use case.
     */
    public void setId(long id);

    /**
     * Get the name of the UseCase.
     *
     * @return Name String
     */
    public String getName();

    /**
     * Set use case description. If <code>null</code> is passed as the parameter
     * the description is reset to empty String.
     *
     * @param description String to set or <code>null</code>
     */
    public void setDescription(String description);

    /**
     * Get the use case description.
     *
     * @return Description String
     */
    public String getDescription();

    /**
     * Add the specified use case path this use case.
     *
     * @param path UseCasePath to add
     * @throws IllegalArgumentException If parameter does not define a path
     *         between the same start and end States than all the other paths in
     *         the use case.
     */
    public void addPath(UseCasePath path);

    /**
     * Creates use case path from list of transitions and adds the use case path to this use case.
     * This method assumes that data added here is done when saved project is opened and there is no
     * need to notify any observers about adding the created path to this use case.
     * 
     * @param path UseCasePath to add
     * @param id the unique identifier
     * @param name for the paths
     * @param description for the paths
     */
    public void loadPath(List<Transition> path, long id, String name, String description);

    /**
     * Creates use case path from list of transitions and adds
     * the  use case path to this use case.
     *
     * @param path list of transitions that make up the path
     * @param id the unique identifier
     * @throws IllegalArgumentException If parameter does not define a path
     *         between the same start and end States than all the other paths in
     *         the use case.
     */
    public UseCasePath createPath(List<Transition> path, long id);

    /**
     * Creates use case path from list of transitions and adds
     * the  use case path to this use case. The id of creted path is created by Kendo
     *
     * @param path list of transitions that make up the path
     * @throws IllegalArgumentException If parameter does not define a path
     *         between the same start and end States than all the other paths in
     *         the use case.
     */
    public UseCasePath createPath(List<Transition> path);

    /**
     * Remove the specified use case path from this use case
     *
     * @param path UseCasePath to remove
     * @return true if succeeded, otherwise false
     */
    public boolean removePath(UseCasePath path);

    /**
     * Get the list of use case paths that belong in to this use case.
     *
     * @return List of UseCasePath objects
     */
    public List<UseCasePath> getUseCasePaths();

    /**
     * Get a UseCasePath by specified index.
     * @param index Index of the use case path to gets
     */
    public UseCasePath getUseCasePath(int index);

    /**
     * Tells the number of individual use case paths in this use case.
     *
     * @return Number of paths in this use case
     */
    public int getPathCount();
}
