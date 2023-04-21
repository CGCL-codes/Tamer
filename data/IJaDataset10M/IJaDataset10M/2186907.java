package ho.module.training.ui.comp;

import ho.core.constants.TrainingType;
import ho.core.datatype.CBItem;
import ho.core.db.DBManager;
import ho.core.model.HOVerwaltung;
import ho.module.training.FutureTrainingWeek;
import ho.module.training.TrainingPanel;
import ho.module.training.ui.model.FutureTrainingsTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * Panel for Settings all the future training week
 *
 * @author <a href=mailto:draghetto@users.sourceforge.net>Massimiliano Amato</a>
 */
public class FutureSettingPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4872598003436712955L;

    private FutureTrainingsTableModel futureModel;

    private JComboBox intensity;

    private JComboBox staminaTrainingPart;

    private JComboBox training;

    /**
     * Creates a new FutureSettingPanel object.
     *
     * @param fm The futureTraining table model, used to update it when needed
     */
    public FutureSettingPanel(FutureTrainingsTableModel fm) {
        super();
        futureModel = fm;
        jbInit();
    }

    /**
     * Populate the Future training table with the future training
     */
    protected void resetFutureTrainings() {
        List<FutureTrainingWeek> futureTrainings = DBManager.instance().getFutureTrainingsVector();
        for (Iterator<FutureTrainingWeek> iter = futureTrainings.iterator(); iter.hasNext(); ) {
            FutureTrainingWeek train = iter.next();
            train.setTrainingIntensity(intensity.getSelectedIndex());
            train.setStaminaTrainingPart(staminaTrainingPart.getSelectedIndex());
            train.setTrainingType(((CBItem) training.getSelectedItem()).getId());
            DBManager.instance().saveFutureTraining(train);
        }
        futureModel.populate();
        TrainingPanel.getTrainPanel().updateUI();
        TrainingPanel.refreshPlayerDetail();
    }

    /**
     * Initializes the state of this instance.
     */
    private void jbInit() {
        List<FutureTrainingWeek> futureTrainings = DBManager.instance().getFutureTrainingsVector();
        FutureTrainingWeek firstFutureTraining = futureTrainings.get(0);
        training = new TrainingComboBox();
        final int ttyp = firstFutureTraining.getTrainingType();
        training.setSelectedItem(new CBItem(TrainingType.toString(ttyp), ttyp));
        intensity = new IntensityComboBox();
        intensity.setSelectedIndex(firstFutureTraining.getTrainingIntensity());
        staminaTrainingPart = new IntensityComboBox();
        staminaTrainingPart.setSelectedIndex(firstFutureTraining.getStaminaTrainingPart());
        JButton button = new JButton(HOVerwaltung.instance().getLanguageString("Aendern"));
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                resetFutureTrainings();
            }
        });
        add(training);
        add(intensity);
        add(staminaTrainingPart);
        add(button);
    }
}
