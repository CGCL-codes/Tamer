package org.fxplayer.rest.representations;

import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class PlayListsRepresentation.
 */
@XmlRootElement(name = "playLists")
public class PlayListsRepresentation extends JAXBElementRepresentation {

    /** The tracks. */
    private final Collection<PlayListRepresentation> playLists;

    /**
	 * Instantiates a new play lists representation.
	 */
    public PlayListsRepresentation() {
        this(null);
    }

    /**
	 * Instantiates a new play lists representation.
	 * @param playLists
	 *          the play lists
	 */
    public PlayListsRepresentation(final Collection<PlayListRepresentation> playLists) {
        this.playLists = playLists;
    }

    /**
	 * Gets the play lists.
	 * @return the play lists
	 */
    @XmlElement(name = "playList")
    public Collection<PlayListRepresentation> getPlayLists() {
        return playLists;
    }
}
