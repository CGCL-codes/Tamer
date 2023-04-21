package org.fudaa.dodico.ef;

/**
 * @author Fred Deniger
 * @version $Id: EfSegmentMutable.java,v 1.6 2006-09-19 14:42:23 deniger Exp $
 */
public class EfSegmentMutable extends EfSegment {

    /**
   * @param _pt1
   * @param _pt2
   */
    public EfSegmentMutable(final int _pt1, final int _pt2) {
        super(_pt1, _pt2);
    }

    /**
   * @param _s
   */
    public EfSegmentMutable(final EfSegment _s) {
        super(_s);
    }

    /**
   * redefinie pour augmenter la visibilit�.
   */
    public void setPt1Idx(final int _i) {
        super.setPt1Idx(_i);
    }

    /**
   * redefinie pour augmenter la visibilit�.
   */
    public void setPt2Idx(final int _i) {
        super.setPt2Idx(_i);
    }

    public void setMinMaxIdx(final int _i1, final int _i2) {
        super.setMinMaxIdx(_i1, _i2);
    }
}
