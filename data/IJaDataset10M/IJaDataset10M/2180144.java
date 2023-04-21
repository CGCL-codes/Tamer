package org.matsim.network;

import org.matsim.basic.network.BasicLaneDefinitions;
import org.matsim.utils.io.MatsimJaxbXmlWriter;

/**
 * Writes the lane definitions according to
 * the http://www.matsim.org/files/dtd/laneDefinitions_v*.xsd
 * grammar.
 * @author dgrether
 *
 */
public class MatsimLaneDefinitionsWriter {

    private MatsimJaxbXmlWriter writerDelegate;

    /**
	 * Writes the file with the default format for 
	 * LaneDefinitions within MATSim.
	 * @param lanedefs
	 */
    public MatsimLaneDefinitionsWriter(BasicLaneDefinitions lanedefs) {
        this.writerDelegate = new LaneDefinitionsWriter11(lanedefs);
    }

    public void writeFile(String filename) {
        this.writerDelegate.writeFile(filename);
    }
}
