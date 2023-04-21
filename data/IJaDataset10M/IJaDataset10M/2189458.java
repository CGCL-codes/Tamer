package info.mp3lib.core;

import java.io.IOException;
import org.jdom.Element;

/**
 * Denotes all objects that holds an XML node containing music file
 * data.
 * @author Gab
 */
public interface IXMLMusicElement {

    /**
	 * Retrieves the XML node holding the IMusicFile parent data
	 * @return the XML node
	 */
    public Element getElement();

    /**
	 * Retrieves the id attribute of the XML node
	 * @return the id
	 */
    public int getId();

    /**
	 * Sets the given id as XML element attribute
	 * @param id the id to set
	 */
    public void setId(final int id);

    /**
	 * Retrieves the code attribute of the XML node
	 * @return the code
	 */
    public int getCode();

    /**
	 * Sets the given code as XML element attribute
	 * @param code the code to set
	 */
    public void setCode(final int code);

    /**
	 * Retrieves the name attribute of the XML node
	 * @return the name
	 */
    public String getName();

    /**
	 * Sets the given name as XML element attribute
	 * @param name the name to set
	 */
    public void setName(final String name);

    /**
	 * Saves the XML node in the zicfile.xml file on the file system.
	 * @throws IOException when an IO error occurs
	 */
    public void save() throws IOException;

    /** name attribute : used by artist, album, track */
    public static final String ATTR_NAME = "name";

    /** id attribute : used by artist, album, track */
    public static final String ATTR_ID = "id";

    /** code attribute : used by artist, album, track */
    public static final String ATTR_CODE = "code";

    /** size attribute : used by album, track */
    public static final String ATTR_SIZE = "size";

    /** style attribute : used by artist */
    public static final String ATTR_STYLE = "style";

    /** path attribute : used by album */
    public static final String ATTR_PATH = "path";

    /** file name attribute : used by track */
    public static final String ATTR_FILENAME = "filename";

    /** year attribute : used by album */
    public static final String ATTR_YEAR = "year";

    /** length attribute : used by track */
    public static final String ATTR_LENGTH = "length";

    /** XML artist element */
    public static final String ELT_ARTIST = "artist";

    /** XML album element */
    public static final String ELT_ALBUM = "album";

    /** XML track element */
    public static final String ELT_TRACK = "track";
}
