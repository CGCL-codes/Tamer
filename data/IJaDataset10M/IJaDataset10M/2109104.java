package ch.sahits.ant;

import java.util.List;
import org.jdom.Element;

/**
 * Represents a basic fileset.
 * This interface is not inteded to be implemented directly
 * @author Andi Hotz, Sahits GmbH
 */
public interface IFileset {

    /**
	 * List of included paths
	 * @return List of paths
	 */
    public List<String> includs();

    /**
	 * List of excluded paths
	 * @return List of paths
	 */
    public List<String> exclude();

    /**
	 * Convert to an XML representation of the element
	 * @return XML fileset
	 */
    public Element toElement();
}
