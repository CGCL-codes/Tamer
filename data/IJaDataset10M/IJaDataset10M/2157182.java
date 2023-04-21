package org.neuroph.netbeans.main.easyneurons.dialog;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.jdesktop.application.Action;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.neuroph.netbeans.main.easyneurons.NeuralNetworkTraining;
import org.neuroph.nnet.learning.LMS;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.neuroph.netbeans.main.easyneurons.dialog//SupervisedTrainingMonitorFrame//EN", autostore = false)
public final class SupervisedTrainingMonitorFrameTopComponent extends TopComponent implements Observer {

    private static SupervisedTrainingMonitorFrameTopComponent instance;

    private static final String PREFERRED_ID = "SupervisedTrainingMonitorFrameTopComponent";

    public SupervisedTrainingMonitorFrameTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(SupervisedTrainingMonitorFrameTopComponent.class, "CTL_SupervisedTrainingMonitorFrameTopComponent"));
        setToolTipText(NbBundle.getMessage(SupervisedTrainingMonitorFrameTopComponent.class, "HINT_SupervisedTrainingMonitorFrameTopComponent"));
    }

    private void initComponents() {
        totalNetErrorField = new javax.swing.JTextField();
        errLabel = new javax.swing.JLabel();
        iterationLabel = new javax.swing.JLabel();
        currentIterationField = new javax.swing.JTextField();
        stopButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        totalNetErrorField.setColumns(18);
        org.openide.awt.Mnemonics.setLocalizedText(errLabel, org.openide.util.NbBundle.getMessage(SupervisedTrainingMonitorFrameTopComponent.class, "SupervisedTrainingMonitorFrameTopComponent.errLabel.text"));
        org.openide.awt.Mnemonics.setLocalizedText(iterationLabel, org.openide.util.NbBundle.getMessage(SupervisedTrainingMonitorFrameTopComponent.class, "SupervisedTrainingMonitorFrameTopComponent.iterationLabel.text"));
        currentIterationField.setColumns(10);
        org.openide.awt.Mnemonics.setLocalizedText(stopButton, org.openide.util.NbBundle.getMessage(SupervisedTrainingMonitorFrameTopComponent.class, "SupervisedTrainingMonitorFrameTopComponent.stopButton.text"));
        stopButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        org.openide.awt.Mnemonics.setLocalizedText(pauseButton, org.openide.util.NbBundle.getMessage(SupervisedTrainingMonitorFrameTopComponent.class, "SupervisedTrainingMonitorFrameTopComponent.pauseButton.text"));
        pauseButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(errLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(totalNetErrorField, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(iterationLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(currentIterationField, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)))).addGroup(layout.createSequentialGroup().addGap(49, 49, 49).addComponent(pauseButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(stopButton))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(errLabel).addComponent(totalNetErrorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(iterationLabel).addComponent(currentIterationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(pauseButton).addComponent(stopButton)).addContainerGap(197, Short.MAX_VALUE)));
    }

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {
        stop();
    }

    private void pauseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        pause();
    }

    private javax.swing.JTextField currentIterationField;

    private javax.swing.JLabel errLabel;

    private javax.swing.JLabel iterationLabel;

    private javax.swing.JButton pauseButton;

    private javax.swing.JButton stopButton;

    private javax.swing.JTextField totalNetErrorField;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized SupervisedTrainingMonitorFrameTopComponent getDefault() {
        if (instance == null) {
            instance = new SupervisedTrainingMonitorFrameTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the SupervisedTrainingMonitorFrameTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized SupervisedTrainingMonitorFrameTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(SupervisedTrainingMonitorFrameTopComponent.class.getName()).warning("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof SupervisedTrainingMonitorFrameTopComponent) {
            return (SupervisedTrainingMonitorFrameTopComponent) win;
        }
        Logger.getLogger(SupervisedTrainingMonitorFrameTopComponent.class.getName()).warning("There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
        this.stop();
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    NeuralNetworkTraining trainingController;

    boolean userPaused = false;

    ConcurrentLinkedQueue dataQueueBuffer;

    TrainerSwingWorker worker;

    public void setSupervisedTrainingMonitorFrameVariables(NeuralNetworkTraining controller) {
        this.trainingController = controller;
        initComponents();
        dataQueueBuffer = new ConcurrentLinkedQueue<LearningInfo>();
    }

    @Override
    public void update(Observable o, Object arg) {
        LMS learningRule = (LMS) o;
        LearningInfo learningInfo = new LearningInfo(learningRule.getCurrentIteration(), learningRule.getTotalNetworkError());
        currentIterationField.setText(learningInfo.getIteration().toString());
        totalNetErrorField.setText(learningInfo.getError().toString());
        if (!userPaused) learningRule.resume();
        if (learningRule.isStopped()) {
            learningRule.deleteObserver(this);
        }
    }

    public void observe(Observable learningRule) {
        learningRule.addObserver(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private class LearningInfo {

        Integer iteration;

        Double error;

        public LearningInfo(Integer iteration, Double error) {
            this.iteration = iteration;
            this.error = error;
        }

        public Double getError() {
            return error;
        }

        public Integer getIteration() {
            return iteration;
        }
    }

    public void stop() {
        if (this.trainingController != null) this.trainingController.stopTraining();
    }

    private class TrainerSwingWorker extends SwingWorker<Void, LearningInfo> {

        boolean hasData = false;

        boolean finnished = false;

        public TrainerSwingWorker() {
        }

        public void setHasData(boolean hasData) {
            this.hasData = hasData;
            if (hasData == true) {
                synchronized (this) {
                    notify();
                }
            }
        }

        public boolean hasData() {
            return this.hasData;
        }

        public void setFinnished(boolean finnished) {
            this.finnished = finnished;
        }

        @Override
        protected Void doInBackground() throws Exception {
            while (!finnished) {
                synchronized (this) {
                    while (!hasData) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                        }
                    }
                }
                publish((LearningInfo) dataQueueBuffer.poll());
                if (dataQueueBuffer.isEmpty()) hasData = false;
            }
            return null;
        }

        @Override
        protected void process(List<LearningInfo> chunks) {
            LearningInfo li = chunks.get(chunks.size() - 1);
            currentIterationField.setText(li.getIteration().toString());
            totalNetErrorField.setText(li.getError().toString());
        }

        @Override
        protected void done() {
            System.out.println("The monitor thread is done!");
        }
    }

    public void pause() {
        if (!userPaused) {
            userPaused = true;
            trainingController.pause();
            pauseButton.setText("Resume");
            stopButton.setEnabled(false);
        } else {
            trainingController.resume();
            userPaused = false;
            pauseButton.setText("Pause");
            stopButton.setEnabled(true);
        }
    }

    public void closeFrame() {
        this.trainingController.getNetwork().getLearningRule().deleteObservers();
        this.close();
    }
}
