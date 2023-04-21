package utils.ifs.sheepvariations;

import utils.ifs.IFSUtil;
import utils.linear.LVect2d;

/**
 * This code is developed from the whitepaper "The Fractal Flame Algorithm" by Draves and Reckase.
 * @author Calvin Ashmore
 */
public class Tangent implements IFSUtil.Mapping_v2 {

    public LVect2d map(LVect2d v, LVect2d dest) {
        dest.setTo(Math.sin(v.x) / Math.cos(v.y), Math.tan(v.y));
        return dest;
    }
}
