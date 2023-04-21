package de.fu.tracebook.core.data;

/**
 * This object represents a medium. It can be an audio recording, photo or text.
 * The medium itself is stored on the devices SD-card. This object just stores
 * the path.
 */
public interface IDataMedia {

    /**
     * Media type constants. Type audio.
     */
    int TYPE_AUDIO = 2;

    /**
     * Media type constants. Type picture.
     */
    int TYPE_PICTURE = 1;

    /**
     * Media type constants. Type text.
     */
    int TYPE_TEXT = 0;

    /**
     * Deletes a medium on the devices memory. Note: Make sure that there is no
     * reference to this medium anymore.
     */
    void delete();

    /**
     * Getter-method.
     * 
     * @return The unique id of the medium.
     */
    int getId();

    /**
     * Getter-method.
     * 
     * @return The name of the medium as it is displayed. (Should generally be
     *         not null, except some idiot misused methods)
     */
    String getName();

    /**
     * Getter-method. Returns path to the directory the medium is in.
     * 
     * @return The path to the medium on the devices medium. (Should generally
     *         be not null, except some idiot misused methods)
     */
    String getPath();

    /**
     * Getter-method.
     * 
     * @return The type of the medium.
     */
    int getType();

    /**
     * Setter-method. Changing the name may have no impact on serialization. On
     * next deserialization the old name may appear again.
     * 
     * @param newname
     *            The new name for the medium.
     */
    void setName(String newname);

    /**
     * Setter-method.
     * 
     * @param type
     *            The new type of this medium.
     */
    void setType(int type);
}
