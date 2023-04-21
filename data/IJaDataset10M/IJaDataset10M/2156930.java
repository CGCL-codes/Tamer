package javax.media.ding3d.loaders.lw3d;

import javax.media.ding3d.vecmath.*;
import java.util.BitSet;
import java.util.Enumeration;
import javax.media.ding3d.Alpha;
import javax.media.ding3d.Node;
import javax.media.ding3d.NodeReferenceTable;
import javax.media.ding3d.SceneGraphObject;
import javax.media.ding3d.Switch;
import javax.media.ding3d.internal.Ding3dUtilsI18N;

/**
 * This class was used in conjunction with SequenceReader to create
 * Tloop functionality inside of Lightwave files.  This behavior handles
 * the switching between objects defined in separate lines of a
 * sequence file.  That is, each line in a sequence file has the name
 * of an object (or an object sequence, if the name ends in "000")
 * and details the start and end frames that that object should be active.
 * This class determines which object/s defined in the file should be active
 * at any given time during the animation.
 */
class SwitchPathInterpolator extends FloatValueInterpolator {

    Switch target;

    int firstSwitchIndex;

    int lastSwitchIndex;

    int currentChild;

    int childCount;

    /**
      * Constructs a new SwitchPathInterpolator object.
      * @param alpha the alpha object for this interpolator
      * @param knots an array of knot values that specify a spline
     */
    SwitchPathInterpolator(Alpha alpha, float knots[], Switch target) {
        super(alpha, knots, new float[knots.length]);
        if (knots.length != (target.numChildren() + 1)) throw new IllegalArgumentException(Ding3dUtilsI18N.getString("SwitchPathInterpolator0"));
        this.target = target;
        firstSwitchIndex = 0;
        lastSwitchIndex = target.numChildren() - 1;
        childCount = lastSwitchIndex + 1;
    }

    /**
     * This method sets the correct child for the Switch node according
     * to alpha
     * @param criteria enumeration of criteria that have triggered this wakeup
     */
    public void processStimulus(Enumeration criteria) {
        int child;
        if (this.getAlpha() != null) {
            computePathInterpolation();
            if (currentKnotIndex > 0) child = currentKnotIndex - 1; else child = 0;
            if (target.getWhichChild() != child) {
                target.setWhichChild(child);
            }
            if ((this.getAlpha()).finished()) return;
        }
        wakeupOn(defaultWakeupCriterion);
    }
}
