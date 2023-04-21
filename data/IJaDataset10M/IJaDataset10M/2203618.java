package jpatch.control.edit;

import jpatch.entity.*;

/**
 *  Thois edit removes a ControlPoint and all appended ControlPoints.
 *  It will create a new CompoundEdit with one RemoveControlPointFromCurveEdit
 *  for each attached ControlPoint
 *
 * @author     aledinsk
 * @created    27. Dezember 2003
 */
public class CompoundDeleteControlPoint extends JPatchCompoundEdit {

    /**
	 * Constructor for RemoveControlPointEdit
	 *
	 * @param  cp  ControlPoint to be removed
	 */
    public CompoundDeleteControlPoint(OLDControlPoint cp) {
        if (DEBUG) System.out.println(getClass().getName() + "(" + cp + ")");
        OLDControlPoint start = cp.getStart();
        if (start.getLoop()) {
            if (start.getLength() < 3) {
                addEdit(new CompoundDropCurve(start));
                return;
            }
            addEdit(new AtomicChangeControlPoint.Loop(start));
            addEdit(new AtomicRemoveCurve(start));
            addEdit(new AtomicAddCurve(cp.getNext()));
            addEdit(new AtomicDeleteControlPointFromCurve(cp));
            addEdit(new CompoundDropControlPoint(cp));
            return;
        }
        if (start.getLength() == 2 || (start.getLength() == 3 && cp.getNext() != null && cp.getPrev() != null)) {
            addEdit(new CompoundDropCurve(start));
            return;
        }
        addEdit(new CompoundDropControlPoint(cp));
        OLDControlPoint cpNext = cp.getNext();
        addEdit(new AtomicDeleteControlPointFromCurve(cp));
        if (cp.getPrev() == null || cp.getPrev().getPrev() == null) {
            if (cp.getPrev() != null) addEdit(new CompoundDropControlPoint(cp.getPrev()));
            addEdit(new AtomicRemoveCurve(start));
            addEdit(new AtomicAddCurve(cpNext));
            return;
        }
        if (cp.getNext() == null || cp.getNext().getNext() == null) {
            if (cp.getNext() != null) addEdit(new CompoundDropControlPoint(cpNext));
            return;
        }
        addEdit(new AtomicAddCurve(cp.getNext()));
    }
}
