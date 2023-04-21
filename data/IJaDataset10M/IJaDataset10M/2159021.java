package net.ontopia.topicmaps.nav2.taglibs.TMvalue;

import java.util.HashSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import javax.servlet.jsp.JspTagException;
import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.AssociationIF;
import net.ontopia.topicmaps.core.AssociationRoleIF;
import net.ontopia.utils.DeciderIF;
import net.ontopia.topicmaps.nav2.utils.NavigatorUtils;
import net.ontopia.topicmaps.nav2.core.NavigatorRuntimeException;
import net.ontopia.topicmaps.nav2.taglibs.value.BaseScopedTag;

/**
 * INTERNAL: Value Producing Tag for finding all the association
 * roles of all the topics and associations in a collection.
 */
public class RolesTag extends BaseScopedTag {

    public static final String BINARY = "binary";

    public static final String NARY = "nary";

    private String cardinality = null;

    private String varRemoveColl = null;

    public Collection process(Collection tmObjects) throws JspTagException {
        if (tmObjects == null || tmObjects.isEmpty()) return Collections.EMPTY_SET; else {
            HashSet assocRoles = new HashSet();
            Collection excludedTopics = (varRemoveColl == null ? null : contextTag.getContextManager().getValue(varRemoveColl));
            DeciderIF scopeDecider = null;
            if (useUserContextFilter) scopeDecider = getScopeDecider(SCOPE_ASSOCIATIONS);
            Iterator iter = tmObjects.iterator();
            Object obj = null;
            while (iter.hasNext()) {
                obj = iter.next();
                if (obj instanceof TopicIF) {
                    TopicIF topic = (TopicIF) obj;
                    Collection _roles = topic.getRoles();
                    if (checkCardinality(_roles.size())) {
                        Iterator itRoles = _roles.iterator();
                        while (itRoles.hasNext()) {
                            AssociationRoleIF assocRole = (AssociationRoleIF) itRoles.next();
                            if (scopeDecider != null && !scopeDecider.ok(assocRole.getAssociation())) continue;
                            if (excludedTopics == null || !excludedTopics.contains(assocRole.getPlayer())) assocRoles.add(assocRole);
                        }
                    }
                } else if (obj instanceof AssociationIF) {
                    AssociationIF assoc = (AssociationIF) obj;
                    Collection _roles = assoc.getRoles();
                    if (checkCardinality(_roles.size())) {
                        Iterator itRoles = _roles.iterator();
                        while (itRoles.hasNext()) {
                            AssociationRoleIF assocRole = (AssociationRoleIF) itRoles.next();
                            if (scopeDecider != null && !scopeDecider.ok(assocRole.getAssociation())) continue;
                            if (excludedTopics == null || !excludedTopics.contains(assocRole.getPlayer())) assocRoles.add(assocRole);
                        }
                    }
                } else {
                    String msg = "RolesTag: only support instances of TopicIF or " + "AssociationIF, but got " + obj.getClass().getName();
                    throw new NavigatorRuntimeException(msg);
                }
            }
            return assocRoles;
        }
    }

    /**
   * INTERNAL: returns true if claimed cardinality is kept, otherwise
   * false.
   */
    private final boolean checkCardinality(int nrOfPlayers) {
        if (cardinality == null) return true;
        if (cardinality.equals(BINARY)) return (nrOfPlayers == 2);
        if (cardinality.equals(NARY)) return (nrOfPlayers > 2);
        return false;
    }

    /**
   * Sets the variable name of an input collection consisting of one
   * or more topics that will removed from the retrieved list of
   * association role players.
   */
    public void setRemove(String varRemoveTopicColl) {
        this.varRemoveColl = varRemoveTopicColl;
    }

    /**
   * Specify in which cardinality you are interested. This filters out
   * the association roles retrieved for the input collection
   * (consisting of TopicIF and AssociationIF objects). If not
   * specified any kind of cardinality is returned. Allowed values
   * are "binary", "nary" (means at least trenary).
   */
    public void setCardinality(String cardinality) {
        if (cardinality.equals(BINARY) || cardinality.equals(NARY)) this.cardinality = cardinality; else throw new IllegalArgumentException("RolesTag: not allowed value specified" + " for attribute 'cardinality'.");
    }
}
