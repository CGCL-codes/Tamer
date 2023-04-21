package viewer.geometry.list.operation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import viewer.core.Viewer;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class ShowingTranslateList extends ShowingOperationList {

    private static final long serialVersionUID = 704986111160705317L;

    CollectionTranslateEvaluator evaluator;

    private Controls controls;

    /**
	 * Create a list for geometries that may be translated with control items. The result of the
	 * operation will be displayed.
	 * 
	 * @param viewer
	 *            the viewer to display the result on.
	 */
    public ShowingTranslateList(Viewer viewer) {
        super(new CollectionTranslateEvaluator(0.0, 0.0), viewer);
        evaluator = (CollectionTranslateEvaluator) getEvaluator();
        controls = new Controls();
        setOperationControls(controls);
        update();
    }

    void update() {
        double x = (Double) controls.spinnerX.getModel().getValue();
        double y = (Double) controls.spinnerY.getModel().getValue();
        evaluator.setX(x);
        evaluator.setY(y);
        calculateResult();
    }

    private class Controls extends JPanel {

        private static final long serialVersionUID = 8396868872466746725L;

        JSpinner spinnerX;

        JSpinner spinnerY;

        public Controls() {
            SpinnerNumberModel modelX = new SpinnerNumberModel(evaluator.getX(), null, null, 0.001);
            SpinnerNumberModel modelY = new SpinnerNumberModel(evaluator.getY(), null, null, 0.001);
            spinnerX = new JSpinner(modelX);
            spinnerY = new JSpinner(modelY);
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0;
            add(spinnerX, c);
            add(spinnerY, c);
            spinnerX.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    ShowingTranslateList.this.update();
                }
            });
            spinnerY.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    ShowingTranslateList.this.update();
                }
            });
        }
    }
}
