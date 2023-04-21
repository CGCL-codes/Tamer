package org.tm4j.topicmap.tmdm.tm4j1;

import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.net.URL;
import java.beans.PropertyVetoException;
import org.tm4j.net.LocatorFactory;
import org.tm4j.topicmap.*;
import org.tm4j.topicmap.utils.IDGenerator;
import org.tm4j.topicmap.utils.IDGeneratorFactory;

/**
	@author <a href="mailto:xuan--2007.05--org.tm4j.topicmap.tmdm--tm4j.org@public.software.baldauf.org">Xuân Baldauf</a>
*/
public class TopicMapUtilsImpl implements org.tm4j.topicmap.TopicMapUtils {

    protected IDGenerator idGenerator = IDGeneratorFactory.newIDGenerator();

    /**
	 * Retrieves the type of all specified associations
	 * @param associations The associations to be searched. If this parameter is null, all associations in the topic map are searched.
	 * @return An unmodifiable collection of Topics defining the type of the searched associations.
	 */
    public Collection getAssociationTypes(Collection associations) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns a collection of the associations in a given scope.
	 * @param scope The single-theme scope to be searched.
	 * @param associations The associations to be searched. If this is null then all associations in the topic map are searched.
	 * @return An unmodifiable collection of all the associations found.
	 */
    public Collection getAssociationsInScope(Topic scope, Collection associations) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns an unmodifiable collection of associations in a given scope.
	 * @param scope The set of themes to be searched.
	 * @param associations The associations to be searched. If this is null then all associations in the topic map are searched.
	 * @return An unmodifiable collection of all the associations found.
	 */
    public Collection getAssociationsInScope(Topic[] scope, Collection associations) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns an unmodifiable collection of associations which are of the specified type.
	 * @param type The topic defining the type to be searched.
	 * @param associations The associations to be searched. If this is null, then all associations in the topic map are searched.
	 */
    public Collection getAssociationsOfType(Topic type, Collection associations) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns an iterator over all of the Associations in the topic map.
	 * @return an Iterator object which will iterate over the topic map's associations
	 */
    public Iterator getAssociationsIterator() {
        throw new UnsupportedOperationException();
    }

    /**
	 * getTopicTypes
	 * Returns all of the Topics which describe the type of another topic.
	 * @param topics A Collection  of the topics to return the types of. If
	 * this is null, then the types of all topics in the map will be returned.
	 */
    public Collection getTopicTypes(Collection topics) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns an iterator over all of the topics in the topic map.
	 */
    public Iterator getTopicsIterator() {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns an iterator over all of the base names of topics in the topic map.
	 * @return an Iterator over BaseName objects
	 */
    public Iterator getNamesIterator() {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns an iterator over all of the occurrences of topics in the topic map
	 * @return an Iterator over Occurrence objects
	 */
    public Iterator getOccurrencesIterator() {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns only those objects in the specified collection which are of the specifed type.
	 * @param type The topic defining the type to filter on
	 * @param objects The objects to be filtered
	 */
    public Collection getObjectsOfType(Topic type, Collection objects) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Gets the all topic map objects in the specified scope
	 * @param scope The scope to be searched.
	 * @param objects The objects to be processed. If null, all objects in the topic map are processed.
	 * @return The set of all objects in the given scope.
	 */
    public Collection getObjectsInScope(Collection scope, Collection objects) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Get the scopes of all specified topic map objects.
	 * @param objects A List of the objects to be processed. If this is null then all objects in the topic map are processed.
	 * @return The collection of all themes defined by the collection of objects
	 */
    public Collection getObjectScopes(Collection objects) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Gets the types of the specified objects
	 * @param objects List of the objects to be processed
	 * @return Collection of all types found.
	 */
    public Collection getObjectTypes(Collection objects) {
        throw new UnsupportedOperationException();
    }

    /**
	 * getTopicsByIdentity
	 * Gets the Topic objects which have the specified identity.
	 * Note that a Topic Map implementation may return only one Topic or a number
	 * of Topics, depending on whether Topics are early-merged or late-merged.
	 * @param identity The topic identity to be searched for
	 * @return The collection of all Topics which have the specified identity.
	 */
    public Collection getTopicsByIdentity(String identity) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns the set of Topics in this topic map which are used as a theme in the
	 * scope of one or more other topic map objects.
	 * @deprecated from 0.9.0 use the {@link org.tm4j.topicmap.index.basic.ThemesIndex}
	 */
    public Collection getThemes() {
        throw new UnsupportedOperationException();
    }

    /**
	* Returns the set of Members of the specified Topic for which the
	* subject or subject indicator of the roleSpec matches the specified locator.
	*/
    public Collection getMembersOfType(Topic topic, org.tm4j.net.Locator subjectOrIndicator) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns the set of Members of the specified Association for which the
	 * subject or subject indicator of the roleSpec matches the specified locator
	 */
    public Collection getMembersOfType(Association assoc, org.tm4j.net.Locator subjectOrIndicator) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns the set of topics which are players in Members of the specified association
	 * where the Member plays role defined by roleSpec.
	 */
    public Collection getPlayersOfRole(Association assoc, Topic roleSpec) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns a string which may be used as a display name in the specified scope.
	 * If a variant name with a parameter of 'display' exists in the specified scope, then that
	 * variant's name string is returned. Otherwise a base name in the specified scope
	 * is sought and if found, then the base name string is returned. Finally a base name in
	 * the unconstrained scope is sought and that name string returned.
	 * If all of these searches fail, null is returned.
	 */
    public String getDisplayName(Topic topic, Topic[] inScope) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns a string which may be used as a sort name in the specified scope.
	 * If a variant name with a parameter of 'sort' exists in the specified scope, then that
	 * variant's name string is returned. Otherwise a base name in the specified scope
	 * is sought and if found, then the base name string is returned. Finally a base name in
	 * the unconstrained scope is sought and that name string returned.
	 * If all of these searches fail, null is returned.
	 */
    public String getSortName(Topic topic, Topic[] inScope) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Returns a collection of all associations in which the specified topic
	 * plays a role.
	 */
    public Collection getAssociations(Topic topic) {
        throw new UnsupportedOperationException();
    }

    public String generateId() {
        return idGenerator.getID();
    }

    public String generateId(URL baseURL) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Retrieves the superclasses of the class defined by the
	 * Topic <code>cls</code>. This method makes use of the XTM-defined
	 * subclass/superclass association and role types
	 * @param cls The topic defining the class to retrieve the superclasses for.
	 * @return An unmodifiable Collection of the Topic objects which define the superclasses of <code>cls</code>
	 * @since 0.7.0
	 */
    public Collection getSuperClasses(Topic cls) {
        throw new UnsupportedOperationException();
    }

    /**
	 * Retrieves the subclasses of the class defined by the Topic
	 * <code>cls</code>. This method makes use of the XTM-defined
	 * subclass/superclass association and role types.
	 * @param cls The topic defining the class to retrieve the subclasses for.
	 * @return An unmodifiable Collection of the Topic objects which define the subclasses of <code>cls</code>
	 * @since 0.7.0
	 */
    public Collection getSubClasses(Topic cls) {
        throw new UnsupportedOperationException();
    }
}
