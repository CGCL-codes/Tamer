package net.ontopia.topicmaps.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.ontopia.topicmaps.core.AssociationIF;
import net.ontopia.topicmaps.core.AssociationRoleIF;
import net.ontopia.topicmaps.core.TMObjectIF;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TypedIF;
import net.ontopia.utils.DeciderIF;

/**
 * INTERNAL: This class provides utility functions for traversing class
 * hierarchies which are expressed using the Published Subject
 * Indicators (PSIs) defined by the XTM 1.0 Specification.  Operations
 * provided by this class are not thread-safe.</p>
 */
public class TypeHierarchyUtils {

    private AssociationWalker supertypesWalker;

    private AssociationWalker subtypesWalker;

    /**
   * INTERNAL: Creates and initialises a new instance of the utility class.
   */
    public TypeHierarchyUtils() {
        DeciderIF assocDecider = new SubjectIdentityDecider(PSI.getXTMSuperclassSubclass());
        DeciderIF subclassDecider = new SubjectIdentityDecider(PSI.getXTMSubclass());
        DeciderIF superclassDecider = new SubjectIdentityDecider(PSI.getXTMSuperclass());
        supertypesWalker = new AssociationWalker(assocDecider, subclassDecider, superclassDecider);
        subtypesWalker = new AssociationWalker(assocDecider, superclassDecider, subclassDecider);
    }

    /**
   * INTERNAL: Determines if the singly-typed object <code>typed</code>
   * is an instance of the type <code>klass</code>.  This function
   * returns true if <code>typed</code> is a direct instance of
   * <code>klass</code> or if it is an instance of some subtype of
   * <code>klass</code>.
   *
   * @param typed the given typedIF object
   * @param klass a topicIF object; the given type
   * @return boolean; true iff the given typedIF is an instance of the
   * given type or an instance of a subtype of the given type
   */
    public boolean isInstanceOf(TypedIF typed, TopicIF klass) {
        TopicIF _type = typed.getType();
        if (klass != null && _type != null && (klass.equals(_type) || supertypesWalker.isAssociated(_type, klass))) {
            return true;
        }
        return false;
    }

    /**
   * INTERNAL: Determines if the <code>typed</code>
   * is an instance of the type <code>klass</code>.  This function
   * returns true if <code>typed</code> is a direct instance of
   * <code>klass</code> or if it is an instance of some subclass of
   * <code>klass</code>.
   *
   * @param typed the given typedIF object
   * @param klass a topicIF object; the given type
   * @return boolean; true if the given object is an instance of the given type or an instance of a 
   *            subtype of the given type
   */
    public boolean isInstanceOf(TopicIF typed, TopicIF klass) {
        Collection<TopicIF> _types = typed.getTypes();
        if (_types.contains(klass)) return true;
        Iterator<TopicIF> it = _types.iterator();
        while (it.hasNext()) {
            if (supertypesWalker.isAssociated(it.next(), klass)) {
                return true;
            }
        }
        return false;
    }

    /**
   * INTERNAL: Returns true if the two topics are directly or indirectly
   * associated under the association type and rolespec definitions
   * provided in the constructor for this walker.  The calculation is
   * performed using a depth-first traversal of the tree formed by the
   * associations concerned, which aborts as soon as the associated
   * topic is found.
   *
   * @param start The topic to begin computation from; an object
   * implementing TopicIF.
   * @param associated The topic to be found in the association; an
   * object implementing TopicIF.
   * @return Boolean: true if the given topics are directly or
   * indirectly associated
   */
    public boolean isAssociatedWith(TopicIF start, TopicIF associated) {
        Collection<AssociationRoleIF> roles = start.getRoles();
        Iterator<AssociationRoleIF> itRoles = roles.iterator();
        AssociationRoleIF assocRole;
        AssociationIF assoc;
        Collection<AssociationRoleIF> assocRoles;
        Iterator<AssociationRoleIF> itAssocRoles;
        AssociationRoleIF otherAssocRole;
        while (itRoles.hasNext()) {
            assocRole = itRoles.next();
            assoc = assocRole.getAssociation();
            assocRoles = assoc.getRoles();
            itAssocRoles = assocRoles.iterator();
            while (itAssocRoles.hasNext()) {
                otherAssocRole = itAssocRoles.next();
                TopicIF player = otherAssocRole.getPlayer();
                if (player != null && player.equals(associated)) return true;
            }
        }
        return false;
    }

    /**
   * INTERNAL: Returns the topics which are supertypes of the given typing topic
   *
   * @param klass a topicIF; the given typing topic 
   * @return an unmodifiable collection of topicIF objects; the
   * supertypes of the given topic
   */
    public Collection<TopicIF> getSuperclasses(TopicIF klass) {
        return supertypesWalker.walkTopics(klass);
    }

    /**
   * INTERNAL: Returns the topics which are supertypes of the given
   * typing topic up to a given level. If the level is 1 only the
   * immediate superclasses are returned, at level 2 immediate
   * superclasses and their immediate superclasses are returned, and
   * so on. To get all superclasses, regardless of level, use the
   * variant of this method which has no numeric parameter.
   *
   * @param klass a topicIF; the given typing topic
   * @param level the level to which superclasses are to be found
   * @return an unmodifiable collection of topicIF objects; the
   * supertypes of the given topic
   * @since 1.2.5
   */
    public Collection<TopicIF> getSuperclasses(TopicIF klass, int level) {
        Collection<TopicIF> supers = new ArrayList<TopicIF>();
        Iterator<List<TMObjectIF>> it = supertypesWalker.walkPaths(klass).iterator();
        while (it.hasNext()) {
            List<TMObjectIF> path = it.next();
            for (int ix = 2; ix < path.size() && ix <= level * 2; ix += 2) supers.add((TopicIF) path.get(ix));
        }
        return supers;
    }

    /**
   * INTERNAL: Returns the topics which are subtypes of the topic klass.
   *
   * @param klass a topicIF; the given typing topic 
   * @return a collection of topicIF objects; the subtypes of the given topic
   */
    public Collection<TopicIF> getSubclasses(TopicIF klass) {
        return subtypesWalker.walkTopics(klass);
    }

    /**
   * INTERNAL: Returns the topics which are subtypes of the given typing
   * topic down to a given level. If the level is 1 only the immediate
   * subclasses are returned, at level 2 immediate subclasses and
   * their immediate subclasses are returned, and so on. To get all
   * subclasses, regardless of level, use the variant of this method
   * which has no numeric parameter.
   *
   * @param klass a topicIF; the given typing topic
   * @param level the level to which subclasses are to be found
   * @return a collection of topicIF objects; the subtypes of the given topic
   * @since 1.2.5
   */
    public Collection<TopicIF> getSubclasses(TopicIF klass, int level) {
        Collection<TopicIF> subs = new ArrayList<TopicIF>();
        Iterator<List<TMObjectIF>> it = subtypesWalker.walkPaths(klass).iterator();
        while (it.hasNext()) {
            List<TMObjectIF> path = it.next();
            for (int ix = 2; ix < path.size() && ix <= level * 2; ix += 2) subs.add((TopicIF) path.get(ix));
        }
        return subs;
    }

    /**
   * INTERNAL: Returns the topic which types the singly typed object
   * <code>typed</code> and all of its supertypes.
   *
   * @param typed a typedIF object 
   * @return a collection of topicIF objects; the type and all
   * supertypes of the given typedIF object.   
   */
    public Collection<TopicIF> getSupertypes(TypedIF typed) {
        if (typed.getType() == null) return new ArrayList<TopicIF>();
        return supertypesWalker.walkTopics(typed.getType());
    }

    /**
   * INTERNAL: Returns the topics which types of the object
   * <code>typed</code> and all their supertypes.
   *
   * @param typed a topic
   * @return a collection of topicIF objects; the type and all supertypes
   *         of the given object
   */
    public Collection<TopicIF> getSupertypes(TopicIF typed) {
        return getSupertypes(typed, false);
    }

    /**
   * INTERNAL: Returns the topics which types the object
   * <code>typed</code> (if not <code>excludeTypes</code> is set to
   * true) and all their supertypes.
   *
   * @param typed a topic
   * @param excludeTypes types of specified topic are not in returned
   * collection
   * @return a collection of topicIF objects; the type and all
   * supertypes of the given object
   * @since 1.1
   */
    public Collection<TopicIF> getSupertypes(TopicIF typed, boolean excludeTypes) {
        Set<TopicIF> ret = new HashSet<TopicIF>();
        Iterator<TopicIF> it = typed.getTypes().iterator();
        while (it.hasNext()) {
            TopicIF type = it.next();
            if (!excludeTypes) ret.add(type);
            ret.addAll(supertypesWalker.walkTopics(type));
        }
        return ret;
    }
}
