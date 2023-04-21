package gov.sns.apps.orbitdisplay;

import com.cosylab.databush.utilities.Orientation;
import com.cosylab.databush.utilities.PropertyChangeSupportable;

/**
 * Insert the type's description here. Creation date: (6/26/00 20:07:44)
 *
 * @author:
 */
public abstract class AbstractManipulatorBean extends PropertyChangeSupportable {

    protected int[] elementCount = new int[2];

    private gov.sns.xal.smf.AcceleratorSeq accSeq = null;

    protected Boolean[][] selected = new Boolean[2][0];

    protected double[][] elementsPositions = new double[2][];

    /** DOCUMENT ME! */
    public static final java.lang.String PROPERTY_ALL_DATA = "ALL_DATA";

    /** DOCUMENT ME! */
    public static final java.lang.String PROPERTY_SELECTION = "SELECTION";

    /** DOCUMENT ME! */
    public static final java.lang.String PROPERTY_SELECTION_HOR = "SELECTION_HOR";

    /** DOCUMENT ME! */
    public static final java.lang.String PROPERTY_SELECTION_VER = "SELECTION_VER";

    protected java.lang.Boolean[] defaultSelectionValue = { new Boolean(false), new Boolean(false) };

    /**
	 * BeamAnalizatorBean constructor comment.
	 *
	 * @param as DOCUMENT ME!
	 */
    public AbstractManipulatorBean(gov.sns.xal.smf.AcceleratorSeq as) {
        super();
        this.accSeq = as;
    }

    /**
	 * Insert the method's description here. Creation date: (7/23/00 20:07:50)
	 *
	 * @param orientation int
	 */
    public void deselectAll(Orientation orientation) {
        for (int i = 0; i < elementCount[orientation.value()]; i++) {
            this.selected[orientation.value()][i] = new Boolean(false);
        }
        fireSelectionChanged(orientation);
    }

    /**
	 * Insert the method's description here. Creation date: (7/21/00 16:45:55)
	 *
	 * @param orientation java.beans.PropertyChangeEvent
	 */
    public void fireSelectionChanged(Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            firePropertyChange(PROPERTY_SELECTION_HOR);
        } else {
            firePropertyChange(PROPERTY_SELECTION_VER);
        }
        firePropertyChange(PROPERTY_SELECTION);
    }

    /**
	 * Insert the method's description here. Creation date: (6/26/00 20:09:33)
	 *
	 * @return com.cosylab.databush.DataBush
	 */
    public gov.sns.xal.smf.AcceleratorSeq getAcceleratorSeq() {
        return accSeq;
    }

    /**
	 * Insert the method's description here. Creation date: (6/24/00 11:32:06)
	 *
	 * @param orientation int
	 *
	 * @return int
	 */
    public int getElementCount(Orientation orientation) {
        return elementCount[orientation.value()];
    }

    /**
	 * Insert the method's description here. Creation date: (6/26/00 20:43:36)
	 *
	 * @param orientation DOCUMENT ME!
	 *
	 * @return double[][]
	 */
    public double[] getElementsPositions(Orientation orientation) {
        return elementsPositions[orientation.value()];
    }

    /**
	 * Insert the method's description here. Creation date: (6/26/00 20:14:54)
	 *
	 * @param orientation DOCUMENT ME!
	 *
	 * @return boolean[][]
	 */
    public Boolean[] getSelected(Orientation orientation) {
        return selected[orientation.value()];
    }

    /**
	 * Insert the method's description here. Creation date: (7/23/00 20:07:50)
	 *
	 * @param orientation int
	 */
    public void invertSelection(Orientation orientation) {
        for (int i = 0; i < elementCount[orientation.value()]; i++) {
            this.selected[orientation.value()][i] = new Boolean(!this.selected[orientation.value()][i].booleanValue());
        }
        fireSelectionChanged(orientation);
    }

    /**
	 * Insert the method's description here. Creation date: (6/26/00 20:50:28)
	 */
    protected void rebuild() {
        selected[Orientation._HORIZONTAL] = new Boolean[elementCount[Orientation._HORIZONTAL]];
        selected[Orientation._VERTICAL] = new Boolean[elementCount[Orientation._VERTICAL]];
        java.util.Arrays.fill(selected[Orientation._HORIZONTAL], defaultSelectionValue[Orientation._HORIZONTAL]);
        java.util.Arrays.fill(selected[Orientation._VERTICAL], defaultSelectionValue[Orientation._VERTICAL]);
        elementsPositions[Orientation._HORIZONTAL] = new double[elementCount[Orientation._HORIZONTAL]];
        elementsPositions[Orientation._VERTICAL] = new double[elementCount[Orientation._VERTICAL]];
    }

    /**
	 * Insert the method's description here. Creation date: (7/23/00 20:07:50)
	 *
	 * @param orientation int
	 */
    public void selectAll(Orientation orientation) {
        for (int i = 0; i < elementCount[orientation.value()]; i++) {
            this.selected[orientation.value()][i] = new Boolean(true);
        }
        fireSelectionChanged(orientation);
    }

    /**
	 * Insert the method's description here. Creation date: (6/27/00 17:00:37)
	 *
	 * @param orientation int
	 * @param element int
	 * @param selected java.lang.Boolean
	 */
    public void setSelected(Orientation orientation, int element, Boolean selected) {
        this.selected[orientation.value()][element] = selected;
        fireSelectionChanged(orientation);
    }
}
