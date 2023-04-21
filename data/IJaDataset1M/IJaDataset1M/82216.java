package net.sf.gridarta.model.baseobject;

import java.awt.Point;
import java.io.Serializable;
import javax.swing.ImageIcon;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.FaceSource;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.gameobject.MultiArchData;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BaseObject<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>, T extends BaseObject<G, A, R, T>> extends Cloneable, Iterable<G>, Serializable {

    int EDIT_TYPE_NONE = 0x10000;

    /**
     * The attribute name of the object's animation.
     */
    @NotNull
    String ANIMATION = "animation";

    /**
     * The attribute name of the object's direction.
     */
    @NotNull
    String DIRECTION = "direction";

    /**
     * The attribute name of the object's face.
     */
    @NotNull
    String FACE = "face";

    /**
     * The attribute name of the object's name.
     */
    @NotNull
    String NAME = "name";

    /**
     * The attribute name of the object's type.
     */
    @NotNull
    String TYPE = "type";

    /**
     * The attribute name of the object's title.
     */
    @NotNull
    String TITLE = "title";

    /**
     * The attribute name of the "is_animated" flag.
     */
    @NotNull
    String IS_ANIMATED = "is_animated";

    /**
     * The name of the "is_turnable" attribute.
     */
    @NotNull
    String IS_TURNABLE = "is_turnable";

    /**
     * The attribute name of the "hp" attribute.
     */
    @NotNull
    String HP = "hp";

    /**
     * The attribute name of the "sp" attribute.
     */
    @NotNull
    String SP = "sp";

    /**
     * The name of the "no_pass" attribute.
     */
    @NotNull
    String NO_PASS = "no_pass";

    /**
     * The name of the "no_pick" attribute.
     */
    @NotNull
    String NO_PICK = "no_pick";

    /**
     * The name of the "level" attribute.
     */
    @NotNull
    String LEVEL = "level";

    /**
     * The name of the "slaying" attribute.
     */
    @NotNull
    String SLAYING = "slaying";

    /**
     * The name of the "last_heal" attribute.
     */
    @NotNull
    String LAST_HEAL = "last_heal";

    /**
     * The name of the "blocksview" attribute.
     */
    @NotNull
    String BLOCKSVIEW = "blocksview";

    /**
     * The name of the "anim_speed" attribute.
     */
    @NotNull
    String ANIM_SPEED = "anim_speed";

    /**
     * Returns the type number of this Archetype.
     * @return the type number of this archetype
     */
    int getTypeNo();

    /**
     * Returns the name of the face of this Archetype or GameObject.
     * @return the name of the face of this archetype or game object
     */
    @Nullable
    String getFaceName();

    /**
     * Counts the number of all inventory items (recursively). The returned
     * value does not include this GameObject.
     * @return the number of objects in the inventory (recursively)
     */
    int countInvObjects();

    /**
     * Returns whether an attribute name exists.
     * @param attributeName the attribute name
     * @param queryArchetype whether to query the Archetype of this GameObject:
     * <code>true</code> if the Archetype should be queried as well,
     * <code>false</code> to ignore it.
     * @return whether the attribute name exists
     */
    boolean hasAttribute(@NotNull String attributeName, boolean queryArchetype);

    /**
     * Returns whether an attribute name exists.
     * @param attributeName the attribute name
     * @return whether the attribute name exists
     */
    boolean hasAttribute(@NotNull String attributeName);

    /**
     * Returns the requested attribute value of this GameObject as {@link
     * String}. The attribute value is first searched in this GameObject. If
     * <code>queryArchetype == true</code>, additional search is done: if the
     * attribute value is not found and this GameObject is not an Archetype
     * itself, the attribute value is searched in this GameObject's Archetype as
     * well.
     * @param attributeName the name of the attribute to search; this will be
     * <code>"<var>attributeName</var> value"</code> in the {@link
     * #getObjectText() object text}
     * @param queryArchetype whether to query the Archetype of this GameObject:
     * <code>true</code> if the Archetype should be queried as well,
     * <code>false</code> to ignore it
     * @return the attribute value or an empty String ("") if no such attribute
     * @see #getAttributeString(String)
     */
    @NotNull
    String getAttributeString(@NotNull String attributeName, boolean queryArchetype);

    /**
     * Returns the requested attribute value of this GameObject as {@link
     * String}. The attribute value is first searched in this GameObject. If the
     * attribute value is not found and this GameObject is not an Archetype
     * itself, the attribute value is searched in this GameObject's Archetype as
     * well. <p/> This methods corresponds to {@link #getAttributeString(String,
     * boolean)} with <code>useArchetype == true</code>.
     * @param attributeName the name of the attribute to search
     * @return the attribute value or an empty String ("") if no such attribute
     * @see #getAttributeString(String, boolean)
     */
    @NotNull
    String getAttributeString(@NotNull String attributeName);

    /**
     * Returns the requested attribute value of this GameObject as
     * <code>int</code>.
     * @param attributeName the name of the attribute to search
     * @param queryArchetype whether to query the archetype of this game object
     * @return attribute the value or zero if not found
     * @see #getAttributeInt(String)
     * @see #getAttributeString(String, boolean)
     */
    int getAttributeInt(@NotNull String attributeName, boolean queryArchetype);

    /**
     * Returns an attribute value of this Archetype as int.
     * @param attributeName the name of the attribute value to return
     * @return the attribute value or <code>0</code> (zero) if not found
     * @see #getAttributeString(String)
     */
    int getAttributeInt(@NotNull String attributeName);

    /**
     * Returns the requested attribute value of this GameObject as
     * <code>long</code>.
     * @param attributeName the name of the attribute to search
     * @param queryArchetype whether to query the archetype of this game object
     * @return the attribute value or zero if not found
     * @see #getAttributeLong(String)
     * @see #getAttributeString(String, boolean)
     */
    long getAttributeLong(@NotNull String attributeName, boolean queryArchetype);

    /**
     * Returns an attribute value of this Archetype as long.
     * @param attributeName the name of the attribute value to return
     * @return the attribute value or <code>0</code> (zero) if not found
     * @see #getAttributeString(String)
     */
    long getAttributeLong(@NotNull String attributeName);

    /**
     * Returns the requested attribute value of this GameObject as
     * <code>double</code>.
     * @param attributeName the name of the attribute to search
     * @param queryArchetype whether to query the archetype of this game object
     * @return the attribute value or zero if not found
     * @see #getAttributeDouble(String)
     * @see #getAttributeString(String, boolean)
     */
    double getAttributeDouble(@NotNull String attributeName, boolean queryArchetype);

    /**
     * Returns an attribute value of this Archetype as double.
     * @param attributeName the name of the attribute value to return
     * @return the attribute value or <code>0.0</code> (zero) if not found
     * @see #getAttributeString(String)
     */
    double getAttributeDouble(@NotNull String attributeName);

    /**
     * Sets the String of an archetype attribute in the objectText.
     * @param attributeName search for "attributeName &lt;string&gt;"
     * @param value the value to set
     */
    void setAttributeString(@NotNull String attributeName, @NotNull String value);

    /**
     * Sets an int value attribute.
     * @param attributeName the name of the attribute to update
     * @param value the new value
     */
    void setAttributeInt(@NotNull String attributeName, int value);

    /**
     * Removes the String of an archetype attribute from the objectText.
     * @param attributeName search for "attributeName &lt;string&gt;"
     */
    void removeAttribute(@NotNull String attributeName);

    /**
     * Notifies the map model that this container is about to change.
     */
    void notifyBeginChange();

    /**
     * Notifies the map model that this container has changed.
     */
    void notifyEndChange();

    /**
     * Notifies the map model that this container has changed but need not be
     * restored by undo/redo.
     */
    void notifyTransientChange();

    /**
     * Returns the Archetype this GameObject is based on.
     * @return the archetype this game object is based on
     * @note For Archetypes, this method returns <code>this</code>.
     */
    @NotNull
    R getArchetype();

    /**
     * Returns the edit type.
     * @return the edit type
     */
    int getEditType();

    /**
     * Sets the edit type.
     * @param editType the edit type
     */
    void setEditType(int editType);

    /**
     * Adds a line of message text. This is used when creating the GameObject
     * from the ArchetypeParser.
     * @param text the text to append to message text
     */
    void addMsgTextLine(@NotNull String text);

    /**
     * Returns the message bound to this object. If the message is empty, the
     * empty String is returned. If the object has no message, this method
     * returns <code>null</code>.
     * @return the message bound to this object
     */
    @Nullable
    String getMsgText();

    /**
     * Sets the message text.
     * @param msgText the message text
     */
    void setMsgText(@Nullable String msgText);

    /**
     * Returns the X coordinate of this GameObject on its map. This method only
     * guarantees a reasonable value for GameObjects that are directly bound to
     * a map. Implementations may also return reasonable values for GameObjects
     * inside containers, but they are not required to do so.
     * @return the x coordinate on map
     * @see #getMapY()
     */
    int getMapX();

    /**
     * Returns the Y coordinate of this GameObject on its map. This method only
     * guarantees a reasonable value for GameObjects that are directly bound to
     * a map. Implementations may also return reasonable values for GameObjects
     * inside containers, but they are not required to do so.
     * @return the y coordinate on map
     * @see #getMapX()
     */
    int getMapY();

    /**
     * Returns the coordinate of this GameObject on its map. This method only
     * guarantees a reasonable value for GameObjects that are directly bound to
     * a map. Implementations may also return reasonable values for GameObjects
     * inside containers, but they are not required to do so.
     * @return the coordinate on map
     */
    Point getMapLocation();

    /**
     * Sets the X coordinate of this GameObject on its map.
     * @param mapX the x coordinate
     * @warning Only use this method during the load process.
     */
    void setMapX(int mapX);

    /**
     * Sets the Y coordinate of this GameObject on its map.
     * @param mapY the y coordinate
     * @warning Only use this method during the load process.
     */
    void setMapY(int mapY);

    /**
     * Returns whether this object is a single-part object or the head of the
     * multi-part object.
     * @return <code>true</code> if single-part or head of multi-part, otherwise
     *         <code>false</code>
     */
    boolean isHead();

    /**
     * Returns the next of this multi-part object.
     * @return the next of this multi-part object or <code>null</code> if this
     *         isn't a multi-part object or this is the last part of a
     *         multi-part object
     */
    @Nullable
    T getMultiNext();

    /**
     * Returns the number of parts for multi-part heads. (*.getMultiRefCount() >
     * 0) is often used as way to find multi-parts.
     * @return the number of parts
     * @todo rename me, "Ref" is a term in Java short for "Reference" and this
     * is not about "References" but about multi-square arches.
     */
    int getMultiRefCount();

    /**
     * Appends a tail to this GameObject.
     * @param tail the tail to append
     */
    void addTailPart(@NotNull T tail);

    /**
     * Removes all tail parts of this game object. Afterwards {@link #isMulti()}
     * will return <code>false</code>.
     */
    void removeTailParts();

    /**
     * Return the head part of a multi-part object. For single-part objects it
     * is the object itself.
     * @return the head of the object
     */
    T getHead();

    /**
     * Determines if this part is a tail part. For single-part objects this is
     * never true.
     * @return <code>true</code> iff this part if a tail part
     */
    boolean isTail();

    /**
     * Determines the horizontal extent in squares. For single-part objects 1 is
     * returned.
     * @return the horizontal extent
     */
    int getSizeX();

    /**
     * Determines the vertical extent in squares. For single-part objects 1 is
     * returned.
     * @return the vertical extent
     */
    int getSizeY();

    /**
     * Determines the maximum x-coordinate of any part relative to the head
     * part. For single-part objects 0 is returned.
     * @return the maximum x-coordinate
     */
    int getMaxX();

    /**
     * Determines the maximum y-coordinate of any part relative to the head
     * part. For single-part objects 0 is returned.
     * @return the maximum y-coordinate
     */
    int getMaxY();

    /**
     * Determines the minimum x-coordinate of any part relative to the head
     * part. For single-part objects 0 is returned.
     * @return the minimum x-coordinate
     */
    int getMinX();

    /**
     * Determines the minimum y-coordinate of any part relative to the head
     * part. For single-part objects 0 is returned.
     * @return the minimum y-coordinate
     */
    int getMinY();

    /**
     * Returns the name of the object as shown to the player.
     * @return the name of the object as shown to the player
     */
    @NotNull
    String getObjName();

    /**
     * Returns the name which is best appropriate to describe this GameObject.
     * (This can be Archetype name or GameObject name.)
     * @return the best suitable descriptive name
     */
    @NotNull
    String getBestName();

    /**
     * Returns the object text of this GameObject as String.
     * @return the object text
     */
    @NotNull
    String getObjectText();

    /**
     * Appends <var>text</var> to the object text of this GameObject.
     * @param line a line of text to append, may contain '\n' for appending
     * multiple lines
     */
    void addObjectText(@NotNull String line);

    /**
     * Sets <var>objectText</var> as object text of this GameObject.
     * @param objectText the text to set as object text
     */
    void setObjectText(@NotNull String objectText);

    /**
     * Returns whether this game object is unmodified from its underlying
     * archetype.
     * @return whether this game object is unmodified
     */
    boolean isDefaultGameObject();

    /**
     * Compares this object to another game object.
     * @param gameObject the other game object
     * @return <code>true</code> if this object equals the other object
     */
    boolean isEqual(@NotNull BaseObject<?, ?, ?, ?> gameObject);

    /**
     * Returns the direction of this Archetype or GameObject.
     * @return the direction of this archetype or game object
     */
    int getDirection();

    /**
     * Returns the {@link FaceSource} of this base object.
     * @return the face source
     */
    @NotNull
    FaceSource getFaceObjSource();

    /**
     * DaiEditor only: Returns the name of the animation.
     * @return the name of the animation
     */
    @Nullable
    String getAnimName();

    /**
     * Returns whether this Archetype is a multi-part object.
     * @return <code>true</code> if this Archetype is a multi-part object,
     *         otherwise <code>false</code>
     */
    boolean isMulti();

    void setMulti(@NotNull MultiArchData<G, A, R, T> multi);

    /**
     * Returns the face name, can be from animation or face.
     * @return the face name
     */
    @Nullable
    String getFaceObjName();

    /**
     * We set here the real face of the objects, depending on the set face and
     * the set animation. The rule is, that a active animation will overrule the
     * default face. We will catch it here. This method should always be invoked
     * after the face or animation has changed and no other change is expected
     * right now, so the changes to animation / face should be applied.
     */
    void setObjectFace();

    /**
     * Returns the normal face for this GameObject.
     * @return the normal face for this GameObject
     */
    @NotNull
    ImageIcon getNormalImage();

    /**
     * Creates a clone of this base object.
     */
    @NotNull
    T clone();

    /**
     * Returns the map lore.
     * @return the map lore
     */
    @NotNull
    String getLoreText();

    /**
     * Sets the map lore.
     * @param loreText the map lore
     */
    void setLoreText(@NotNull CharSequence loreText);

    /**
     * Creates a new {@link GameObject} instance: an {@link Archetype} is
     * instantiated, a {@link GameObject} is cloned.
     * @param gameObjectFactory the game object factory for creating new game
     * objects
     * @return the new game object instance
     */
    @NotNull
    G newInstance(@NotNull GameObjectFactory<G, A, R> gameObjectFactory);

    /**
     * Calls the appropriate <code>visit()</code> function of a {@link
     * BaseObjectVisitor}.
     * @param baseObjectVisitor the base object visitor
     */
    void visit(@NotNull BaseObjectVisitor<G, A, R> baseObjectVisitor);

    /**
     * Return whether this base object uses the "direction" attribute.
     * @return whether this base object uses the "direction" attribute
     */
    boolean usesDirection();

    /**
     * Will be called whenever the archetype faces have been reloaded.
     */
    void facesReloaded();

    /**
     * Returns a string representation of this game object. The string
     * representation is the given <code>format</code> string which supports the
     * following replacements:
     * <p/>
     * <ul> <li>$$ - a string $ character <li>${NAME} - the in-game object name
     * <li>${name} - the value of attribute "name" <li>${name:text} - text if
     * the attribute name exists; empty otherwise. replacements within "text"
     * are processed </ul>
     * @param format the format to use
     * @return the string representation
     */
    @NotNull
    String toString(@NotNull String format);
}
