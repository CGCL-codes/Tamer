package org.openmeetings.jai;

import java.awt.Point;
import junit.framework.TestCase;
import org.openmeetings.utils.geom.GeomPoint;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

public class TestInterpolation extends TestCase {

    private static final Logger log = Logger.getLogger(TestInterpolation.class);

    public void testInterpolate() {
        try {
        } catch (Exception er) {
            log.error("ERROR ", er);
            System.out.println("Error exporting: " + er);
            er.printStackTrace();
        }
    }
}
