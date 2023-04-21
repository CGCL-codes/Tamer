package javazoom.spi.mpeg.sampled.file.tag;

/** Indicates that the value of a tag is a string, and
    provides a getValueAsString() method to get it.
    Appropriate for tags like artist, title, etc.
 */
public interface StringableTag {

    /** Return the value of this tag as a string.
	 */
    public String getValueAsString();
}
