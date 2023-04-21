package edu.arsc.fullmetal.commons;

import java.util.Collections;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import static edu.arsc.fullmetal.commons.XmlUtilities.*;

/**
 * Represents a protocol stanza describing robots available for use.
 * 
 * @todo   Add routines to construct from XML.
 * @author cgranade
 */
public class RobotsList extends ProtocolStanza {

    private final List<RobotElement> robots;

    private static final String ELEM_ROBOTS = "robots";

    static {
        ProtocolStanza.addToDispatch(ELEM_ROBOTS, RobotsList.class);
    }

    public RobotsList(Document d) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Constructs a new stanza given a list of robot element objects.
     * @param robots
     *     Robots to be added to the new stanza.
     */
    public RobotsList(List<RobotElement> robots) {
        this.robots = Collections.unmodifiableList(robots);
    }

    public List<RobotElement> getRobots() {
        return robots;
    }

    @Override
    public Document getXML() {
        Document d = new Document();
        Element root = new Element(ELEM_ROBOTS, FMP_NS);
        d.setRootElement(root);
        for (RobotElement r : robots) {
            d.addContent(r.getXML());
        }
        return d;
    }
}
