package org.tm4j.topicmap;

import java.beans.PropertyVetoException;
import java.util.Collection;

/**
 * Represents a member of an association. Members may
 * be typed by a single topic (the roleSpec property) and
 * are defined by zero or more topics.
 *
 * Note that in this definition, members are
 * <em>exclusively</em> defined by {@link Topic}s &mdash;
 * implementations must ensure that when processing
 * <a href="http://www.topicmaps.org/xtm/1.0/" target="_blank">XTM</a>
 * documents, the following rules are obeyed while parsing a
 * <a href="http://www.topicmaps.org/xtm/1.0/#elt-member" target="_blank">
 * <code>&lt;member&gt;</code></a> element:
 *
 * <ul>
 * <li>If the <code>&lt;member&gt;</code> element has one or more
 *     child <code>&lt;topicRef&gt;</code> elements,
 *     the topic(s) referenced by that topic reference shall be added
 *     to the member's set of players.</li>
 * <li>If the <code>&lt;member&gt;</code> element has one or more
 *     child <code>&lt;subjectIndicatorRef&gt;</code> elements,
 *     the topic(s) reifying the referenced subject(s)
 *     shall be added to the member's set of players; if no such topics are yet
 *     available in the topic map, they must be created first.</li>
 * <li>If the <code>&lt;member&gt;</code> element has one or more
 *     child <code>&lt;resourceRef&gt;</code> elements,
 *     the topic(s) reifying the referenced resource(s)
 *     shall be added to the member's set of players; if no such topics are yet
 *     available in the topic map, they must be created first.</li>
 * </ul>
 *
 * @author  <a href="mailto:kal@techquila.com">Kal Ahmed</a>
 * @see     Topic#addSubjectIndicator(org.tm4j.net.Locator)
 * @see     Topic#setSubjectIndicators(org.tm4j.net.Locator[])
 * @see     Topic#setSubject(org.tm4j.net.Locator)
 */
public interface Member extends TopicMapObject {

    /**
     * Returns the association to which the Member belongs.
     *
     * @return  The member's parent association, or <code>null</code>
     *          if the member is currently not part of any association.
     */
    public Association getParent();

    /**
     * Returns the topic which defines the type of this
     * Member.
     *
     * @return    The role-defining topic of this member.
     */
    public Topic getRoleSpec();

    /**
     * Sets the topic which defines the type of this Member.
     * If the Member already has a type defined, then the
     * existing type is replaced by the new one.
     *
     * @param    roleSpec    the new role-defining topic of this member.
     */
    public void setRoleSpec(Topic roleSpec) throws PropertyVetoException;

    /**
     * Adds a topic as a player, meaning one of the topics
     * that define this member.
     * A topic may be a player in multiple Members of
     * multiple Associations.
     *
     * @param    player the topic to be added to the set of topics
     *                  that define this member.
     */
    public void addPlayer(Topic player) throws PropertyVetoException;

    /**
     * Removes the specified topic from the
     * list of players of this Member object.
     * If the specified Topic is not in the list of
     * players for this Member object, then this method has
     * no effect.
     *
     * @param    player the topic to be removed from the set of topics
     *                  that define this member.
     */
    public void removePlayer(Topic player) throws PropertyVetoException;

    /**
     * Returns an unmodifiable collection of the Topics
     * which are players of this Member.
     *
     * @return  The collection of {@link Topic}s that define
     *          this member.
     *          If no players are currently defined for this
     *          member, the returned collection is empty;
     *          it is never <code>null</code>.
     */
    public Collection getPlayers();

    /**
     * Sets the collection of Topics which are players of
     * this Member. The specified array replaces all the
     * existing players of this Member.
     *
     * @param    players  the new set of topics to define
     *                    this member. If this is <code>null</code>,
     *                    all the existing players of this member
     *                    are removed.
     */
    public void setPlayers(Topic[] players) throws PropertyVetoException;
}
