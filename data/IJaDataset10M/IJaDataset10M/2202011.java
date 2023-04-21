package com.xenoage.zong.musiclayout.stampings;

import static com.xenoage.util.math.Point2f.p;
import com.xenoage.util.math.Point2f;
import com.xenoage.util.math.Shape;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.text.FormattedText;

/**
 * Class for a text stamping belonging to a staff, e.g. for lyric
 * and directions.
 *
 * @author Andreas Wenger
 */
public final class StaffTextStamping extends TextStamping {

    /** The position, relative to the left border of the score. */
    public final SP position;

    public StaffTextStamping(StaffStamping parentStaff, MusicElement musicElement, FormattedText text, SP position) {
        super(text, parentStaff, musicElement, computeBoundingShape(text, parentStaff, position));
        this.position = position;
    }

    /**
   * Gets the position within the frame in mm.
   */
    @Override
    public Point2f getPositionInFrame() {
        return p(position.xMm, parentStaff.computeYMm(position.lp));
    }

    /**
   * Returns the bounding shape of this text.
   */
    private static Shape computeBoundingShape(FormattedText text, StaffStamping parentStaff, SP position) {
        float x = position.xMm;
        float y = parentStaff.computeYMm(position.lp);
        return text.getBoundingRect().move(x, y);
    }
}
