package net.ontopia.topicmaps.utils;

import java.util.*;
import net.ontopia.topicmaps.core.*;
import net.ontopia.utils.*;

/**
 * INTERNAL: Characteristic processing utilities.
 */
public class CharacteristicUtils {

    /**
   * INTERNAL: Finds first characteristic of a specified type.
   *
   * @param objects A collection of TypedIF objects.
   * @return The first object that has the specified type.
   *
   * @since 2.0
   */
    public static TypedIF getByType(Collection objects, TopicIF type) {
        Iterator it = objects.iterator();
        while (it.hasNext()) {
            TypedIF typed = (TypedIF) it.next();
            if (type.equals(typed.getType())) return typed;
        }
        return null;
    }

    /**
   * INTERNAL: Gets the basenames of the given collection of topics.
   *
   * @param topics A scope; a collection of TopicIF objects.
   * @return An ArrayList of TopicNameIF objects; the base names of 
   *             all the topics in the given collection.
   */
    public static Collection getTopicNames(Collection topics) {
        List result = new ArrayList();
        Iterator iter = topics.iterator();
        while (iter.hasNext()) {
            TopicIF topic = (TopicIF) iter.next();
            result.addAll(topic.getTopicNames());
        }
        return result;
    }

    /**
   * Gets all the variant names of the given collecton of basenames,
   * including nested ones.   
   *
   * @param names A collection of TopicNameIF objects.
   * @return The variant names of all the basenames in the given collection; 
   *                an ArrayList of VariantNameIF objects.
   */
    public static Collection getVariants(Collection names) {
        List result = new ArrayList();
        Iterator iter = names.iterator();
        while (iter.hasNext()) {
            TopicNameIF name = (TopicNameIF) iter.next();
            result.addAll(name.getVariants());
        }
        return result;
    }

    /**
   * Gets the occurrences of all the topics in the given collection.
   *
   * @param topics A collection of TopicIF objects; a scope.
   * @return An ArrayList of OccurrenceIF objects; all the occurrences of
   *              the topics in the given collection.
   */
    public static Collection getOccurrences(Collection topics) {
        List result = new ArrayList();
        Iterator iter = topics.iterator();
        while (iter.hasNext()) {
            TopicIF topic = (TopicIF) iter.next();
            result.addAll(topic.getOccurrences());
        }
        return result;
    }

    /**
   * Gets the association roles of the topics in the given collection.
   *
   * @param topics A collection of TopicIF objects; a scope.

   * @return An ArrayList of AssociationRoleIF objects; all the 
   *         association roles of the topics in the given collection.
   */
    public static Collection getRoles(Collection topics) {
        List result = new ArrayList();
        Iterator iter = topics.iterator();
        while (iter.hasNext()) {
            TopicIF topic = (TopicIF) iter.next();
            result.addAll(topic.getRoles());
        }
        return result;
    }

    /**
   * INTERNAL: Gets the topics of a collection of basenames. 
   *
   * @param basenames A collection of TopicNameIFs.
   * @return A collection of TopicIF objects; the topics to which the
   * basenames belong.
   */
    public static Collection getTopicsOfTopicNames(Collection basenames) {
        Collection topics = new HashSet();
        Iterator iter = basenames.iterator();
        while (iter.hasNext()) {
            TopicIF topic = ((TopicNameIF) iter.next()).getTopic();
            if (topic != null) topics.add(topic);
        }
        return topics;
    }

    /**
   * INTERNAL: Returns all the directly associated topics of the given
   * topic.
   *
   * @since 3.4
   */
    public static Collection getAssociatedTopics(TopicIF topic) {
        Collection result = new HashSet();
        Collection roles = topic.getRoles();
        if (roles.isEmpty()) return Collections.EMPTY_SET;
        Iterator iter = roles.iterator();
        while (iter.hasNext()) {
            AssociationRoleIF role1 = (AssociationRoleIF) iter.next();
            AssociationIF assoc = role1.getAssociation();
            Iterator riter = assoc.getRoles().iterator();
            while (riter.hasNext()) {
                AssociationRoleIF role2 = (AssociationRoleIF) riter.next();
                if (ObjectUtils.equals(role1, role2)) continue;
                TopicIF other = role2.getPlayer();
                if (other != null) result.add(other);
            }
        }
        return result;
    }
}
