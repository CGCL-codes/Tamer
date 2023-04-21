package dialog;

/**
 *  Class for displaying the Link&ouml;ping University logotype. The LiULogotype
 *  can be used as a regular component in windows/frames.
 *  <p><img src="doc-files/LiU.gif"><p>
 *  If you think the logotype is ugly on the farmyard, there are ways to remove it.
 *  You just have to figure out how.
 *
 *  @author Henrik Eriksson
 *  @version 0.01
 *
 *  @see <a href="http://www.liu.se">Link&ouml;ping University</a>
 */
public class LiULogotype extends ScaledImageCanvas {

    /** Constructs a Link&ouml;ping University logotype. */
    public LiULogotype() {
        super("../resources/LiU.gif", 1.0);
    }
}
