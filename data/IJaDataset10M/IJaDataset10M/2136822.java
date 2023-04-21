package sk.tuke.ess.editor.simulation.schemymod.document.gui.experiment;

import com.jidesoft.swing.JideButton;
import sk.tuke.ess.editor.base.components.logger.Logger;
import sk.tuke.ess.editor.simulation.schemymod.document.gui.SchemaDocumentWindow;
import sk.tuke.ess.editor.simulation.schemymod.document.gui.experiment.actions.ExperimentActionsFactory;
import sk.tuke.ess.editor.simulation.schemymod.logic.ExperimentWrapper;
import sk.tuke.ess.editor.simulation.schemymod.logic.gui.ConnectionDialog;
import sk.tuke.ess.sim.Experiment;
import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: zladovan
 * Date: 14.2.2012
 * Time: 22:24
 * To change this template use File | Settings | File Templates.
 */
public class ExperimentToolbar extends JToolBar {

    private JButton playPauseResumeLocalButton;

    private JButton playStopGRIDButton;

    private JButton stopButton;

    private JSpinner startTimeSpinner;

    private JSpinner stopTimeSpinner;

    private JProgressBar progressBar;

    private Experiment experiment;

    private RunLocalExperimentTask runLocalExperimentTask;

    private RunGridExperimentTask runGRIDExperimentTask;

    private ConnectionDialog connectionDialog;

    private ExperimentActionsFactory actionsFactory;

    public ExperimentToolbar(SchemaDocumentWindow documentWindow) {
        super("Nástroje experimentov", JToolBar.HORIZONTAL);
        connectionDialog = new ConnectionDialog();
        actionsFactory = new ExperimentActionsFactory(this, documentWindow);
        initComponents();
    }

    private void initComponents() {
        initButtons();
        initSpinners();
        initProgressbar();
    }

    private void initButtons() {
        playPauseResumeLocalButton = createButton(actionsFactory.createPlayLocalAction());
        playStopGRIDButton = createButton(actionsFactory.createPlayGRIDAction());
        stopButton = createButton(actionsFactory.createStopLocalAction());
        stopButton.setVisible(false);
        add(playPauseResumeLocalButton);
        add(playStopGRIDButton);
        add(stopButton);
    }

    private JButton createButton(Action action) {
        JButton button = new JideButton();
        setActionToButton(button, action);
        return button;
    }

    private void initSpinners() {
        startTimeSpinner = createSpinner("Počiatočný čas simulácie");
        stopTimeSpinner = createSpinner("Koncový čas simulácie");
        startTimeSpinner.setModel(new SimTimeSpinnerModel(ExperimentWrapper.DEFAULT_TIME_START, stopTimeSpinner, true));
        stopTimeSpinner.setModel(new SimTimeSpinnerModel(ExperimentWrapper.DEFAULT_TIME_END, startTimeSpinner, false));
        add(startTimeSpinner);
        add(new JLabel(" > "));
        add(stopTimeSpinner);
    }

    private JSpinner createSpinner(String toolTipText) {
        JSpinner spinner = new JSpinner() {

            @Override
            public Dimension getPreferredSize() {
                Dimension dimension = super.getPreferredSize();
                dimension.width = 50;
                return dimension;
            }
        };
        spinner.setToolTipText(toolTipText);
        return spinner;
    }

    private void initProgressbar() {
        progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        progressBar.setVisible(false);
        progressBar.setStringPainted(true);
        add(progressBar);
    }

    public void onLocalStart() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                progressBar.setIndeterminate(false);
                setProgressBarProgress(0);
                progressBar.setVisible(true);
                stopButton.setVisible(true);
                playStopGRIDButton.setVisible(false);
                setActionToButton(playPauseResumeLocalButton, actionsFactory.createPauseLocalAction());
                setActionToButton(stopButton, actionsFactory.createStopLocalAction());
            }
        });
    }

    public void onLocalPause() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                setActionToButton(playPauseResumeLocalButton, actionsFactory.createResumeLocalAction());
                stopButton.setVisible(true);
                Logger.getLogger().addInfo("Simulácia pozastavená v čase <b>%f</b>", experiment.getActualTime());
            }
        });
    }

    public void onLocalResume() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                setActionToButton(playPauseResumeLocalButton, actionsFactory.createPauseLocalAction());
                stopButton.setVisible(true);
            }
        });
    }

    public void onStop() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                setActionToButton(playPauseResumeLocalButton, actionsFactory.createPlayLocalAction());
                setActionToButton(playStopGRIDButton, actionsFactory.createPlayGRIDAction());
                stopButton.setVisible(false);
                progressBar.setVisible(false);
                playPauseResumeLocalButton.setVisible(true);
                playStopGRIDButton.setVisible(true);
                Logger.getLogger().addInfo("Simulácia ukončená v čase <b>%f</b>", getExperiment().getActualTime());
            }
        });
    }

    public void onGRIDStart() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                progressBar.setVisible(true);
                progressBar.setIndeterminate(true);
                progressBar.setString("");
                setActionToButton(playStopGRIDButton, actionsFactory.createStopGRIDAction());
                playPauseResumeLocalButton.setVisible(false);
            }
        });
    }

    private void setActionToButton(JButton button, Action action) {
        button.setAction(action);
        button.setToolTipText(button.getText());
        button.setText(null);
    }

    public RunLocalExperimentTask getRunLocalExperimentTask() {
        return runLocalExperimentTask;
    }

    public RunGridExperimentTask getRunGRIDExperimentTask() {
        return runGRIDExperimentTask;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public void setRunLocalExperimentTask(RunLocalExperimentTask runLocalExperimentTask) {
        this.runLocalExperimentTask = runLocalExperimentTask;
    }

    public void setRunGRIDExperimentTask(RunGridExperimentTask runGRIDExperimentTask) {
        this.runGRIDExperimentTask = runGRIDExperimentTask;
    }

    public void setProgressBarProgress(final int progress) {
        progressBar.setValue(progress);
        progressBar.setString(String.format("%d %%", progress));
    }

    public void changeProgressBarTextAndTooltip(final String text, final String tooltip) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                progressBar.setString(text);
                progressBar.setToolTipText(tooltip);
            }
        });
    }

    public double getStartTimeValue() {
        return ((Number) startTimeSpinner.getValue()).doubleValue();
    }

    public double getStopTimeValue() {
        return ((Number) stopTimeSpinner.getValue()).doubleValue();
    }

    public ConnectionDialog getConnectionDialog() {
        return connectionDialog;
    }
}
