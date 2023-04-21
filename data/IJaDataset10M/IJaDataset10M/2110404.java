package edu.xtec.jclic.bags;

import edu.xtec.util.JDomUtility;

/**
 * This special case of {@link edu.xtec.jclic.bags.JumpInfo} is used in
 * {@link edu.xtec.jclic.bags.ActivitySequenceJump} objects to decide the jump to be
 * taked (or the action to be performed) based on the results obtained by the user when
 * playing JClic activities. In addition to the standard JumpInfo fields and methods, this
 * class have two public members where to store a score and time thresholds. The exact
 * meaning of this members will depend on the type of this <CODE>ConditionalJumpInfo</CODE>
 * ({@link edu.xtec.jclic.bags.ActivitySequenceJump.upperJump} or {@link edu.xtec.jclic.bags.ActivitySequenceJump.lowerJump}).
 * @author Francesc Busquets (fbusquets@xtec.net)
 * @version 1.0
 */
public class ConditionalJumpInfo extends JumpInfo implements Cloneable {

    public int threshold;

    public int time;

    /** Creates new ConditionalJumpInfo */
    public ConditionalJumpInfo(int action, String sequence, int threshold) {
        this(action, sequence, threshold, -1);
    }

    public ConditionalJumpInfo(int action, String sequence, int threshold, int time) {
        super(action, sequence);
        this.threshold = threshold;
        this.time = time;
    }

    private ConditionalJumpInfo() {
        super(STOP);
        threshold = -1;
        time = -1;
    }

    public static final String TIME = "time", THRESHOLD = "threshold";

    public org.jdom.Element getJDomElement() {
        org.jdom.Element e = super.getJDomElement();
        e.setAttribute(THRESHOLD, Integer.toString(threshold));
        if (time >= 0) e.setAttribute(TIME, Integer.toString(time));
        return e;
    }

    public static ConditionalJumpInfo getConditionalJumpInfo(org.jdom.Element e) throws Exception {
        ConditionalJumpInfo cji = new ConditionalJumpInfo();
        cji.setProperties(e, null);
        return cji;
    }

    public void setProperties(org.jdom.Element e, Object aux) throws Exception {
        super.setProperties(e, aux);
        threshold = JDomUtility.getIntAttr(e, THRESHOLD, threshold);
        time = JDomUtility.getIntAttr(e, TIME, time);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
