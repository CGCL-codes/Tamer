package game.visualizations;

import org.apache.log4j.Logger;
import javax.vecmath.Point3d;
import java.util.LinkedList;
import game.neurons.GMDHnetwork;
import game.data.TreeData;
import game.data.InputFactor;
import game.data.GlobalData;
import game.gui.GraphCanvas;
import game.gui.Controls;
import game.gui.InputControls;
import game.models.Model;

/**
 * This class encapsulates GMDHNetwork and other data object provided by the
 * application, to ease their use.
 * 
 * It might be viewed as another data layer.
 */
public class KopsaClassification3Dmodel {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(KopsaClassification3Dmodel.class);

    private static final String NOT_ENOUGH_INPUT_FACTORS = "At least 3 input factors have to be selected!";

    Model myNetwork;

    TreeData myData;

    double[] vect;

    int paramX = -1;

    int paramY = -1;

    int paramZ = -1;

    Object[][] valuesPos = new Object[3][];

    Object[][] valuesVals = new Object[3][];

    /**
	 * This is following silly design of the application - the argument being
	 * passed is the GraphCanvas containing references to data stuff.
	 * 
	 * @param gc
	 */
    public static KopsaClassification3Dmodel createModel(GraphCanvas gc) throws ModelException {
        KopsaClassification3Dmodel model = new KopsaClassification3Dmodel();
        model.myData = GlobalData.getInstance();
        model.myNetwork = Controls.getInstance().getGnet();
        model.vect = new double[GlobalData.getInstance().getINumber()];
        model.resolveVisualizingDimensions(gc);
        model.resolveInputFactors(gc);
        model.recomputeAxis(0);
        model.recomputeAxis(1);
        model.recomputeAxis(2);
        return model;
    }

    /**
	 * This method is invoked when application data changes.
	 * 
	 * If the change affects the 3d model, and it needs to be recreated, the
	 * method returns true. These changes are: - another network selected, -
	 * input factors selection changes, - non-visualized factor value changes.
	 * 
	 * @param gc
	 * @return
	 * @throws ModelException
	 */
    public boolean updateFromGC(GraphCanvas gc) throws ModelException {
        boolean modelChanged = false;
        if (Controls.getInstance().getGnet() != myNetwork) {
            myNetwork = Controls.getInstance().getGnet();
            modelChanged = true;
        }
        if (resolveVisualizingDimensions(gc)) {
            modelChanged = true;
        }
        if (resolveInputFactors(gc)) {
            modelChanged = true;
        }
        if (modelChanged) {
            recomputeAxis(0);
            recomputeAxis(1);
            recomputeAxis(2);
        }
        return modelChanged;
    }

    /**
	 * Computes positions (in the 3d world) and values for axis marks.
	 * 
	 * @param dimension
	 */
    private void recomputeAxis(int dimension) {
        LinkedList listPos = new LinkedList();
        LinkedList listString = new LinkedList();
        double[] point = new double[] { -0.5, -0.5, -0.5 };
        Point3d point3d = new Point3d();
        InputFactor ifa;
        ifa = (InputFactor) myData.iFactor.elementAt(paramX);
        switch(dimension) {
            case 0:
                ifa = (InputFactor) myData.iFactor.elementAt(paramX);
                break;
            case 1:
                ifa = (InputFactor) myData.iFactor.elementAt(paramY);
                break;
            case 2:
                ifa = (InputFactor) myData.iFactor.elementAt(paramZ);
                break;
        }
        float oneDeg = computeOneDeg(ifa.getMin(), ifa.getMax());
        float range = (float) ifa.getMax() - (float) ifa.getMin();
        float first = (float) ifa.getMin() - ((float) ifa.getMin() % oneDeg);
        float val = 0;
        do {
            val = (float) Math.round((val + oneDeg) * 100) / 100;
            float pos = (val / range);
            listPos.add(-0.5 + pos);
            listString.add(Float.toString((float) Math.round((first + val) * 100) / 100));
        } while (first + val < ifa.getMax());
        valuesPos[dimension] = listPos.toArray();
        valuesVals[dimension] = listString.toArray();
    }

    /**
	 * Computes the value of one degree on the axis.
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
    private static float computeOneDeg(double min, double max) {
        float range = (float) (max - min);
        float one10th = range / 10;
        double log = StrictMath.log(one10th) / StrictMath.log(10);
        long rlog = (long) Math.floor(log);
        float oneDeg = (float) Math.pow(10, rlog);
        int mul = 1;
        if (oneDeg * 5 <= one10th) mul = 5; else if (oneDeg * 2 <= one10th) mul = 2;
        if (range / (oneDeg * mul) > 15) {
            switch(mul) {
                case 1:
                    mul = 2;
                    break;
                case 2:
                    mul = 5;
                    break;
                case 5:
                    mul = 1;
                    oneDeg = oneDeg * 10;
            }
        }
        return oneDeg * mul;
    }

    /**
	 * Reads current selection of input factors and determines which will be
	 * used for 3D.
	 * 
	 * @param gc
     * @return True if the selected dimensions have changed.
	 * @throws ModelException
	 *             If insufficient number of input factors is selected.
	 */
    private boolean resolveVisualizingDimensions(GraphCanvas gc) throws ModelException {
        boolean changed = false;
        int dimensionToBeSet = 1;
        boolean[] sel = InputControls.getInstance().getSelectedFactorsList();
        for (int i = 0; i < sel.length; i++) {
            if (sel[i]) {
                switch(dimensionToBeSet) {
                    case 1:
                        if (paramX != i) {
                            paramX = i;
                            changed = true;
                        }
                        break;
                    case 2:
                        if (paramY != i) {
                            paramY = i;
                            changed = true;
                        }
                        break;
                    case 3:
                        if (paramZ != i) {
                            paramZ = i;
                            changed = true;
                        }
                        break;
                }
                dimensionToBeSet++;
                if (dimensionToBeSet == 4) {
                    break;
                }
            }
        }
        if (dimensionToBeSet != 4) throw new ModelException(NOT_ENOUGH_INPUT_FACTORS);
        return changed;
    }

    /**
	 * Reads current values of input factors from the application and stores
	 * them into vect[].
	 * 
	 * @param gc
     * @return True If non-visualized (used for 3D) factor has changed. In that
	 *         case, graph needs to be reconstructed.
	 */
    private boolean resolveInputFactors(GraphCanvas gc) {
        boolean changed = false;
        for (int f = 0; f < GlobalData.getInstance().getINumber(); f++) {
            if (GlobalData.getInstance().iFactor.elementAt(f) != null) {
                double newval = ((InputFactor) GlobalData.getInstance().iFactor.elementAt(f)).getStandardValue();
                if (vect[f] != newval) {
                    vect[f] = newval;
                    if ((f != paramX) && (f != paramY) && (f != paramZ)) changed = true;
                }
            }
        }
        return changed;
    }

    /**
	 * This exception is raised by the Classification3Dmodel constructor if
	 * something with underlying data is wrong (i.e. not enough selected input
	 * factors).
	 */
    public static class ModelException extends Exception {

        /**
		 * Logger for this class
		 */
        private static final Logger logger = Logger.getLogger(ModelException.class);

        public ModelException(String message) {
            super(message);
        }
    }

    /**
	 * Normalizes the value in a way that it is in the range of <0.0, 1.0>.
	 * 
	 * @param value
	 * @return normalized value
	 */
    static double getNorm(double value) {
        if (value < 0) {
            return 0.0;
        }
        if (value > 1.0) {
            return 1.0;
        }
        return value;
    }
}
