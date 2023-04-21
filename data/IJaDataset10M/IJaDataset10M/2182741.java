package editor.model.xml.datasheet;

import editor.model.xml.TagMapper;
import org.jdom.Element;

/**
 * model of the right connection track
 */
public final class RightConnectionTrackElement extends AbstractConnectionTrackElement {

    /**
     * builds the right connection track from scratch
     */
    public RightConnectionTrackElement() {
        super(TagMapper.getTagName("right_connection_track"));
    }

    /**
     * builds the right connection track from the given root element
     *
     * @param root the root element
     */
    public RightConnectionTrackElement(final Element root) {
        super(root);
    }
}
