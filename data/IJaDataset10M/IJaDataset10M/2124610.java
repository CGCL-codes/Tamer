package org.freelords.xmlmanager;

import javax.xml.bind.annotation.XmlRootElement;

/** List of songs to play (usually as background music).
 *
 * This class is just a very elementary wrapper around an array to enable loading from
 * an xml file. Its purpose is to allow loading of playlists from either a
 * standard list or from, e.g., a scenario definition. We do not allow outside
 * manipulation of the list; for access, an iterator, and a get method are provided.
 *
 * @author Ulf Lorenz
 */
@XmlRootElement(name = "backgroundmusic")
public class BackgroundPlayList extends PlayList1 {

    private static final long serialVersionUID = 4457523119026009391L;
}
