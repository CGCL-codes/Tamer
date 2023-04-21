package de.hattrickorganizer.gui.menu.option;

import de.hattrickorganizer.gui.templates.ImagePanel;
import de.hattrickorganizer.logik.TrainingsManager;
import de.hattrickorganizer.model.HOVerwaltung;
import de.hattrickorganizer.model.OptionManager;
import java.awt.GridLayout;
import javax.swing.JLabel;

/**
 * Optionen für das Training
 */
final class TrainingsOptionenPanel extends ImagePanel {

    private static final long serialVersionUID = 1L;

    private TrainingAdjustmentPanel m_tapAgeFactor;

    private TrainingAdjustmentPanel m_jtapAssisstantFactor;

    private TrainingAdjustmentPanel m_jtapIntensityFactor;

    private TrainingAdjustmentPanel m_jtapCoachFactor;

    private TrainingAdjustmentPanel m_jtapWinger;

    private TrainingAdjustmentPanel m_jtapPassing;

    private TrainingAdjustmentPanel m_jtapPlaymaking;

    private TrainingAdjustmentPanel m_jtapSetPieces;

    private TrainingAdjustmentPanel m_jtapScoring;

    private TrainingAdjustmentPanel m_jtapGoalkeeping;

    private TrainingAdjustmentPanel m_jtapDefending;

    /**
     * Creates a new TrainingsOptionenPanel object.
     */
    protected TrainingsOptionenPanel() {
        initComponents();
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param changeEvent TODO Missing Method Parameter Documentation
     */
    public final void refresh() {
        gui.UserParameter.temp().TRAINING_OFFSET_GOALKEEPING = m_jtapGoalkeeping.getValue();
        gui.UserParameter.temp().TRAINING_OFFSET_DEFENDING = m_jtapDefending.getValue();
        gui.UserParameter.temp().TRAINING_OFFSET_PLAYMAKING = m_jtapPlaymaking.getValue();
        gui.UserParameter.temp().TRAINING_OFFSET_PASSING = m_jtapPassing.getValue();
        gui.UserParameter.temp().TRAINING_OFFSET_WINGER = m_jtapWinger.getValue();
        gui.UserParameter.temp().TRAINING_OFFSET_SCORING = m_jtapScoring.getValue();
        gui.UserParameter.temp().TRAINING_OFFSET_SETPIECES = m_jtapSetPieces.getValue();
        gui.UserParameter.temp().TRAINING_OFFSET_AGE = m_tapAgeFactor.getValue();
        gui.UserParameter.temp().TrainerFaktor = m_jtapCoachFactor.getValue();
        gui.UserParameter.temp().TRAINING_OFFSET_ASSISTANTS = m_jtapAssisstantFactor.getValue();
        gui.UserParameter.temp().TRAINING_OFFSET_INTENSITY = m_jtapIntensityFactor.getValue();
        OptionManager.instance().setReInitNeeded();
    }

    /**
     * TODO Missing Method Documentation
     */
    private void initComponents() {
        setLayout(new GridLayout(13, 1, 4, 4));
        JLabel label = new JLabel("   " + de.hattrickorganizer.model.HOVerwaltung.instance().getLanguageString("VoraussichtlicheTrainingwochen"));
        add(label);
        m_jtapGoalkeeping = new TrainingAdjustmentPanel(HOVerwaltung.instance().getLanguageString("training.goalkeeping"), TrainingsManager.BASE_DURATION_GOALKEEPING, gui.UserParameter.temp().TRAINING_OFFSET_GOALKEEPING, this);
        add(m_jtapGoalkeeping);
        m_jtapDefending = new TrainingAdjustmentPanel(HOVerwaltung.instance().getLanguageString("training.defending"), TrainingsManager.BASE_DURATION_DEFENDING, gui.UserParameter.temp().TRAINING_OFFSET_DEFENDING, this);
        add(m_jtapDefending);
        m_jtapPlaymaking = new TrainingAdjustmentPanel(HOVerwaltung.instance().getLanguageString("training.playmaking"), TrainingsManager.BASE_DURATION_PLAYMAKING, gui.UserParameter.temp().TRAINING_OFFSET_PLAYMAKING, this);
        add(m_jtapPlaymaking);
        m_jtapPassing = new TrainingAdjustmentPanel(HOVerwaltung.instance().getLanguageString("training.short_passes"), TrainingsManager.BASE_DURATION_PASSING, gui.UserParameter.temp().TRAINING_OFFSET_PASSING, this);
        add(m_jtapPassing);
        m_jtapWinger = new TrainingAdjustmentPanel(HOVerwaltung.instance().getLanguageString("training.crossing"), TrainingsManager.BASE_DURATION_WINGER, gui.UserParameter.temp().TRAINING_OFFSET_WINGER, this);
        add(m_jtapWinger);
        m_jtapScoring = new TrainingAdjustmentPanel(HOVerwaltung.instance().getLanguageString("training.scoring"), TrainingsManager.BASE_DURATION_SCORING, gui.UserParameter.temp().TRAINING_OFFSET_SCORING, this);
        add(m_jtapScoring);
        m_jtapSetPieces = new TrainingAdjustmentPanel(HOVerwaltung.instance().getLanguageString("training.set_pieces"), TrainingsManager.BASE_DURATION_SET_PIECES, gui.UserParameter.temp().TRAINING_OFFSET_SETPIECES, this);
        add(m_jtapSetPieces);
        label = new JLabel("   " + de.hattrickorganizer.model.HOVerwaltung.instance().getLanguageString("TrainingFaktoren"));
        add(label);
        m_tapAgeFactor = new TrainingAdjustmentPanel(HOVerwaltung.instance().getLanguageString("FaktorSpieleralter"), TrainingsManager.BASE_AGE_FACTOR, gui.UserParameter.temp().TRAINING_OFFSET_AGE, this);
        add(m_tapAgeFactor);
        m_jtapCoachFactor = new TrainingAdjustmentPanel(HOVerwaltung.instance().getLanguageString("FaktorTrainerfertigkeit"), TrainingsManager.BASE_COACH_FACTOR, gui.UserParameter.temp().TrainerFaktor, this);
        add(m_jtapCoachFactor);
        m_jtapAssisstantFactor = new TrainingAdjustmentPanel(HOVerwaltung.instance().getLanguageString("FaktorCoTraineranzahl"), TrainingsManager.BASE_ASSISTANT_COACH_FACTOR, gui.UserParameter.temp().TRAINING_OFFSET_ASSISTANTS, this);
        add(m_jtapAssisstantFactor);
        m_jtapIntensityFactor = new TrainingAdjustmentPanel(HOVerwaltung.instance().getLanguageString("FaktorIntensitaet"), TrainingsManager.BASE_INTENSITY_FACTOR, gui.UserParameter.temp().TRAINING_OFFSET_INTENSITY, this);
        add(m_jtapIntensityFactor);
    }
}
