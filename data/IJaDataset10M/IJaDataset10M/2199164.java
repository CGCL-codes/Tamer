package org.kaintoch.gps.gpx.filter;

import org.kaintoch.gps.gpx.GpxTrack;

/**
 * http://de.wikipedia.org/wiki/Douglas-Peucker-Algorithmus
 * http://www.softsurfer.com/Archive/algorithm_0205/algorithm_0205.htm
 * http://forums.oracle.com/forums/thread.jspa?threadID=166874
 * http://www.cs.sunysb.edu/~algorith/implement/DPsimp/implement.shtml
 * http://www.codeproject.com/KB/recipes/dphull.aspx
 * @author stefan
 *
 */
public class GpxDouglasPeucker implements IGpxTrackFilter {

    private static final String[][] argDescriptor = new String[][] { { "orthoDist", "java.lang.Double", "maximum allowed orthogonal distance between original and filtered track." } };

    public GpxDouglasPeucker() {
        throw new RuntimeException("YNI");
    }

    /**
	 * @see org.kaintoch.gps.gpx.filter.IGpxTrackFilter#filter(org.kaintoch.gps.gpx.GpxTrack)
	 */
    public GpxTrack filter(GpxTrack in) throws Exception {
        orthoDistance();
        doDouglasPeucker();
        return null;
    }

    /**
	 * @see org.kaintoch.gps.gpx.filter.IGpxFileFilter#getInfo()
	 */
    public String getName() {
        return null;
    }

    private void doDouglasPeucker() {
    }

    private void orthoDistance() {
    }

    public String[][] getArgs() {
        return argDescriptor;
    }

    public String getInfo() {
        return null;
    }
}
