package geovista.matrix.visclass;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.EventListenerList;
import geovista.common.classification.ClassifierMLClassify;
import geovista.common.classification.MultiGaussian;
import geovista.common.classification.MultiGaussianModel;
import geovista.common.data.DataSetForApps;

public class ClassifierMaximumLikelihood {

    private String[] attributesDisplay;

    private Vector[] trainingDataVector;

    private int classnumber = 5;

    private MultiGaussian[] multiGaussian;

    private MultiGaussianModel multiGaussianModel;

    private ClassifierMLClassify classify;

    private boolean visualDisplay = true;

    private final EventListenerList listenerListAction = new EventListenerList();

    public ClassifierMaximumLikelihood() {
    }

    public void setTrainingData(Vector[] data) {
        trainingDataVector = data;
        classnumber = trainingDataVector.length;
        multiGaussianModel = new MultiGaussianModel();
        if (visualDisplay == true) {
            drawVisualDisplay1D();
            fireActionPerformed();
        }
        multiGaussianModel.setClassNumber(classnumber);
        multiGaussianModel.setTrainingData(trainingDataVector);
        multiGaussian = multiGaussianModel.getMultiGaussianModel();
    }

    /**
	 * @param data
	 * 
	 *            This method is deprecated becuase it wants to create its very
	 *            own pet DataSetForApps. This is no longer allowed, to allow
	 *            for a mutable, common data set. Use of this method may lead to
	 *            unexpected program behavoir. Please use setDataSet instead.
	 */
    @Deprecated
    public void setDataObject(Object[] data) {
        setDataSet(new DataSetForApps(data));
    }

    public void setDataSet(DataSetForApps data) {
        if (visualDisplay == true) {
            fireActionPerformed();
        }
        classify = new ClassifierMLClassify();
        classify.setClassNumber(classnumber);
        classify.setClassificationModel(multiGaussian);
        classify.setDataSet(data);
    }

    public void setSingleTuple(double[] tuple) {
        classify = new ClassifierMLClassify();
        classify.setClassNumber(classnumber);
        classify.setClassificationModel(multiGaussian);
        classify.setSingleTuple(tuple);
    }

    public int getClassTuple() {
        return classify.getClassTuple();
    }

    public void setClassNumber(int classNumber) {
        classnumber = classNumber;
    }

    public int[] getClassificaiton() {
        return classify.getClassificaiton();
    }

    public void setVisualDisplay(boolean visualDisplay) {
        this.visualDisplay = visualDisplay;
    }

    public String[] getVariableNames() {
        return attributesDisplay;
    }

    public Vector[] getTrainingData() {
        return trainingDataVector;
    }

    JFrame dummyFrame = new JFrame("Data Distribution");

    JDialog graph;

    private void drawVisualDisplay1D() {
        double[][] data;
        int len = trainingDataVector[0].size();
        data = new double[len][1];
        for (int i = 0; i < len; i++) {
            data[i][0] = ((double[]) trainingDataVector[0].get(i))[0];
        }
        dummyFrame.setSize(100, 400);
        dummyFrame.setVisible(true);
        dummyFrame.setVisible(true);
    }

    /**
	 * adds an ActionListener to the button
	 */
    public void addActionListener(ActionListener l) {
        listenerListAction.add(ActionListener.class, l);
    }

    /**
	 * removes an ActionListener from the button
	 */
    public void removeActionListener(ActionListener l) {
        listenerListAction.remove(ActionListener.class, l);
    }

    /**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
    public void fireActionPerformed() {
        Object[] listeners = listenerListAction.getListenerList();
        ActionEvent e2 = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "OK");
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                ((ActionListener) listeners[i + 1]).actionPerformed(e2);
            }
        }
    }
}
