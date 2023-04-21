package tr.view.action.recurrence.modify;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import tr.appl.Constants;
import tr.model.action.Intervals;
import tr.model.action.Period;
import tr.model.action.PeriodDay;
import tr.model.action.PeriodMonth;
import tr.model.action.PeriodType;
import tr.model.action.PeriodWeek;
import tr.model.action.PeriodWeekday;
import tr.model.action.PeriodYear;
import tr.model.action.Recurrence;
import tr.view.action.PeriodMonthlyPanel;
import tr.view.action.PeriodWeeklyPanel;
import tr.view.action.PeriodYearlyPanel;

public final class RecurrenceRegularPanelModify extends JPanel {

    private Recurrence modRecurrence;

    private Date recurrenceStartDate;

    private Date actionScheduledDate;

    private Dialog dialog;

    private long counter;

    public RecurrenceRegularPanelModify() {
        initComponents();
    }

    public void setModel(Recurrence modRecurrence, Date actionScheduledDate, Date recurrenceStartDate, boolean basisChanged) {
        this.modRecurrence = modRecurrence;
        this.recurrenceStartDate = recurrenceStartDate;
        this.actionScheduledDate = actionScheduledDate;
        initPeriods();
        initPanel(basisChanged);
        setEnabled(isEnabled());
    }

    @Override
    public String getName() {
        return org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "change.regular.recurrence");
    }

    private void initPeriods() {
        setPeriod(new PeriodWeekday());
        setPeriod(new PeriodDay());
        setPeriod(new PeriodWeek());
        setPeriod(new PeriodMonth());
        setPeriod(new PeriodYear());
        setPeriod(modRecurrence.getPeriod());
        setAdvanceNbr(modRecurrence.getPeriod().getType(), modRecurrence.getAdvanceNbr());
    }

    private void initComponents() {
        terminationButtonGroup = new javax.swing.ButtonGroup();
        startDatebuttonGroup = new javax.swing.ButtonGroup();
        periodLabel = new javax.swing.JLabel();
        frequencySpinner = new javax.swing.JSpinner();
        periodPanel = new javax.swing.JPanel();
        advanceLabel1 = new javax.swing.JLabel();
        advanceSpinner = new javax.swing.JSpinner();
        advanceLabel2 = new javax.swing.JLabel();
        endLabel = new javax.swing.JLabel();
        endNeverRadioButton = new javax.swing.JRadioButton();
        endNbrRadioButton = new javax.swing.JRadioButton();
        endNbrSpinner = new javax.swing.JSpinner();
        endNbrLabel = new javax.swing.JLabel();
        endDateRadioButton = new javax.swing.JRadioButton();
        endDateField = new tr.swing.date.field.DateField();
        periodComboBox = new tr.view.action.recurrence.PeriodTypeComboBox();
        startLabel = new javax.swing.JLabel();
        fillerLabel = new javax.swing.JLabel();
        startStartDateRadioButton = new javax.swing.JRadioButton();
        startActionDateRadioButton = new javax.swing.JRadioButton();
        startEnterRadioButton = new javax.swing.JRadioButton();
        startEnterDateField = new tr.swing.date.field.DateField();
        errorLabel1 = new javax.swing.JLabel();
        errorLabel2 = new javax.swing.JLabel();
        setMaximumSize(new java.awt.Dimension(1024, 380));
        setMinimumSize(new java.awt.Dimension(680, 380));
        setPreferredSize(new java.awt.Dimension(680, 380));
        periodLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(periodLabel, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "periods"));
        periodLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        periodLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        periodLabel.setPreferredSize(new java.awt.Dimension(100, 23));
        frequencySpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        frequencySpinner.setMaximumSize(new java.awt.Dimension(50, 23));
        frequencySpinner.setMinimumSize(new java.awt.Dimension(50, 23));
        frequencySpinner.setPreferredSize(new java.awt.Dimension(50, 23));
        frequencySpinner.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                frequencySpinnerStateChanged(evt);
            }
        });
        periodPanel.setMaximumSize(new java.awt.Dimension(530, 26));
        periodPanel.setMinimumSize(new java.awt.Dimension(530, 26));
        periodPanel.setPreferredSize(new java.awt.Dimension(530, 26));
        periodPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        advanceLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(advanceLabel1, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "generate"));
        advanceLabel1.setMaximumSize(new java.awt.Dimension(100, 23));
        advanceLabel1.setMinimumSize(new java.awt.Dimension(100, 23));
        advanceLabel1.setPreferredSize(new java.awt.Dimension(100, 23));
        advanceSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        advanceSpinner.setMaximumSize(new java.awt.Dimension(50, 23));
        advanceSpinner.setMinimumSize(new java.awt.Dimension(50, 23));
        advanceSpinner.setPreferredSize(new java.awt.Dimension(50, 23));
        advanceSpinner.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                advanceSpinnerStateChanged(evt);
            }
        });
        org.openide.awt.Mnemonics.setLocalizedText(advanceLabel2, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "periods.in.advance"));
        advanceLabel2.setMaximumSize(new java.awt.Dimension(67, 23));
        advanceLabel2.setMinimumSize(new java.awt.Dimension(67, 23));
        advanceLabel2.setPreferredSize(new java.awt.Dimension(67, 23));
        endLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(endLabel, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "terminate"));
        endLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        endLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        endLabel.setPreferredSize(new java.awt.Dimension(100, 23));
        terminationButtonGroup.add(endNeverRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(endNeverRadioButton, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "never"));
        endNeverRadioButton.setMaximumSize(new java.awt.Dimension(90, 23));
        endNeverRadioButton.setMinimumSize(new java.awt.Dimension(90, 23));
        endNeverRadioButton.setPreferredSize(new java.awt.Dimension(90, 23));
        endNeverRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endNeverRadioButtonActionPerformed(evt);
            }
        });
        terminationButtonGroup.add(endNbrRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(endNbrRadioButton, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "after"));
        endNbrRadioButton.setMaximumSize(new java.awt.Dimension(70, 23));
        endNbrRadioButton.setMinimumSize(new java.awt.Dimension(70, 23));
        endNbrRadioButton.setPreferredSize(new java.awt.Dimension(70, 23));
        endNbrRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endNbrRadioButtonActionPerformed(evt);
            }
        });
        endNbrSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(2), Integer.valueOf(2), null, Integer.valueOf(1)));
        endNbrSpinner.setMaximumSize(new java.awt.Dimension(50, 23));
        endNbrSpinner.setMinimumSize(new java.awt.Dimension(50, 23));
        endNbrSpinner.setPreferredSize(new java.awt.Dimension(50, 23));
        endNbrSpinner.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                endNbrSpinnerStateChanged(evt);
            }
        });
        org.openide.awt.Mnemonics.setLocalizedText(endNbrLabel, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "periods"));
        endNbrLabel.setMaximumSize(new java.awt.Dimension(80, 23));
        endNbrLabel.setMinimumSize(new java.awt.Dimension(80, 23));
        endNbrLabel.setPreferredSize(new java.awt.Dimension(80, 23));
        terminationButtonGroup.add(endDateRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(endDateRadioButton, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "after"));
        endDateRadioButton.setMaximumSize(new java.awt.Dimension(70, 23));
        endDateRadioButton.setMinimumSize(new java.awt.Dimension(70, 23));
        endDateRadioButton.setPreferredSize(new java.awt.Dimension(70, 23));
        endDateRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endDateRadioButtonActionPerformed(evt);
            }
        });
        endDateField.setMaximumSize(new java.awt.Dimension(110, 23));
        endDateField.setMinimumSize(new java.awt.Dimension(110, 23));
        endDateField.setPreferredSize(new java.awt.Dimension(110, 23));
        endDateField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                endDateFieldPropertyChange(evt);
            }
        });
        periodComboBox.setMaximumSize(new java.awt.Dimension(125, 23));
        periodComboBox.setMinimumSize(new java.awt.Dimension(125, 23));
        periodComboBox.setPreferredSize(new java.awt.Dimension(125, 23));
        periodComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                periodActionPerformed(evt);
            }
        });
        org.openide.awt.Mnemonics.setLocalizedText(startLabel, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "delete.and.regenerate.recurrent.actions.from"));
        startLabel.setMaximumSize(new java.awt.Dimension(286, 23));
        startLabel.setMinimumSize(new java.awt.Dimension(286, 23));
        startLabel.setPreferredSize(new java.awt.Dimension(286, 23));
        fillerLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        fillerLabel.setMaximumSize(new java.awt.Dimension(100, 23));
        fillerLabel.setMinimumSize(new java.awt.Dimension(100, 23));
        fillerLabel.setPreferredSize(new java.awt.Dimension(100, 23));
        fillerLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        startDatebuttonGroup.add(startStartDateRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(startStartDateRadioButton, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "recurrence.start.date"));
        startStartDateRadioButton.setMaximumSize(new java.awt.Dimension(154, 23));
        startStartDateRadioButton.setMinimumSize(new java.awt.Dimension(154, 23));
        startStartDateRadioButton.setPreferredSize(new java.awt.Dimension(154, 23));
        startStartDateRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        startStartDateRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startStartDateRadioButtonActionPerformed(evt);
            }
        });
        startDatebuttonGroup.add(startActionDateRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(startActionDateRadioButton, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "selected.action.date"));
        startActionDateRadioButton.setMaximumSize(new java.awt.Dimension(150, 23));
        startActionDateRadioButton.setMinimumSize(new java.awt.Dimension(150, 23));
        startActionDateRadioButton.setPreferredSize(new java.awt.Dimension(150, 23));
        startActionDateRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        startActionDateRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionDateRadioButtonActionPerformed(evt);
            }
        });
        startDatebuttonGroup.add(startEnterRadioButton);
        org.openide.awt.Mnemonics.setLocalizedText(startEnterRadioButton, org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "this.date"));
        startEnterRadioButton.setMaximumSize(new java.awt.Dimension(90, 23));
        startEnterRadioButton.setMinimumSize(new java.awt.Dimension(90, 23));
        startEnterRadioButton.setPreferredSize(new java.awt.Dimension(90, 23));
        startEnterRadioButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        startEnterRadioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startEnterRadioButtonActionPerformed(evt);
            }
        });
        startEnterDateField.setMaximumSize(new java.awt.Dimension(110, 23));
        startEnterDateField.setMinimumSize(new java.awt.Dimension(110, 23));
        startEnterDateField.setPreferredSize(new java.awt.Dimension(110, 23));
        startEnterDateField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                startEnterDateFieldPropertyChange(evt);
            }
        });
        errorLabel1.setForeground(java.awt.Color.red);
        errorLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        errorLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        errorLabel1.setMaximumSize(new java.awt.Dimension(1024, 23));
        errorLabel1.setMinimumSize(new java.awt.Dimension(16, 23));
        errorLabel1.setOpaque(true);
        errorLabel1.setPreferredSize(new java.awt.Dimension(16, 23));
        errorLabel2.setForeground(java.awt.Color.red);
        errorLabel2.setMaximumSize(new java.awt.Dimension(1024, 23));
        errorLabel2.setMinimumSize(new java.awt.Dimension(0, 23));
        errorLabel2.setPreferredSize(new java.awt.Dimension(0, 23));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(endLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(advanceLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(periodLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(advanceSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(advanceLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 271, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(layout.createSequentialGroup().add(endNeverRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(endNbrRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(endNbrSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(endNbrLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(endDateRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(endDateField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(layout.createSequentialGroup().addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(periodPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(layout.createSequentialGroup().add(frequencySpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(periodComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))).add(layout.createSequentialGroup().add(fillerLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(startEnterRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(startEnterDateField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(startActionDateRadioButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE).add(startStartDateRadioButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE))))).add(layout.createSequentialGroup().add(56, 56, 56).add(startLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)).add(layout.createSequentialGroup().addContainerGap().add(errorLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)).add(layout.createSequentialGroup().addContainerGap().add(errorLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, periodComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.TRAILING, frequencySpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.TRAILING, periodLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(periodPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(advanceLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(advanceSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(advanceLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, endDateField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.TRAILING, endDateRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.TRAILING, endNbrLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.TRAILING, endNbrSpinner, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.TRAILING, endNbrRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.TRAILING, endNeverRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.TRAILING, endLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(18, 18, 18).add(startLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(fillerLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(startStartDateRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(2, 2, 2).add(startActionDateRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(6, 6, 6).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(startEnterRadioButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(startEnterDateField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(18, 18, 18).add(errorLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(errorLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(37, Short.MAX_VALUE)));
        layout.linkSize(new java.awt.Component[] { frequencySpinner, periodLabel }, org.jdesktop.layout.GroupLayout.VERTICAL);
        layout.linkSize(new java.awt.Component[] { advanceLabel1, advanceLabel2, advanceSpinner }, org.jdesktop.layout.GroupLayout.VERTICAL);
        layout.linkSize(new java.awt.Component[] { endDateField, endDateRadioButton, endLabel, endNbrLabel, endNbrRadioButton, endNbrSpinner, endNeverRadioButton }, org.jdesktop.layout.GroupLayout.VERTICAL);
    }

    private void endNbrRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setEndValues();
        setEnabled(isEnabled());
    }

    private void periodActionPerformed(java.awt.event.ActionEvent evt) {
        PeriodType periodType = (PeriodType) periodComboBox.getSelectedItem();
        Period period = getPeriod(periodType);
        Integer advanceNbr = getAdvanceNbr(periodType);
        modRecurrence.setPeriod(period);
        modRecurrence.setAdvanceNbr(advanceNbr);
        stateChange();
        periodPanel.removeAll();
        switch(periodType) {
            case WEEK:
                {
                    periodPanel.add(onLabel);
                    if (periodWeeklyPanel == null) {
                        periodWeeklyPanel = new PeriodWeeklyPanel();
                        periodWeeklyPanel.setPeriodWeek((PeriodWeek) period);
                    }
                    periodWeeklyPanel.notifyStartDate(modRecurrence.getStartDate());
                    periodPanel.add(periodWeeklyPanel);
                    break;
                }
            case MONTH:
                {
                    if (periodMonthlyPanel == null) {
                        periodMonthlyPanel = new PeriodMonthlyPanel(getDialog(), (PeriodMonth) period);
                    }
                    periodMonthlyPanel.notifyStartDate(modRecurrence.getStartDate());
                    periodPanel.add(periodMonthlyPanel);
                    break;
                }
            case YEAR:
                {
                    if (periodYearlyPanel == null) {
                        periodYearlyPanel = new PeriodYearlyPanel(getDialog(), (PeriodYear) period);
                    }
                    periodYearlyPanel.notifyStartDate(modRecurrence.getStartDate());
                    periodPanel.add(periodYearlyPanel);
                    break;
                }
        }
        advanceSpinner.setValue(advanceNbr);
        setEndValues();
        periodPanel.validate();
        periodPanel.repaint();
    }

    public void setDialog(Dialog dialog) {
        assert (dialog != null);
        this.dialog = dialog;
    }

    private Dialog getDialog() {
        if (dialog == null) {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof Dialog) {
                dialog = (Dialog) window;
            } else if (window instanceof Frame) {
                dialog = new Dialog((Frame) window);
            } else {
                dialog = new Dialog(new Frame());
            }
        }
        assert (dialog != null);
        return dialog;
    }

    private void advanceSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {
        Integer oldValue = getAdvanceNbr(modRecurrence.getPeriod().getType());
        Integer newValue = (Integer) advanceSpinner.getValue();
        if (oldValue != newValue) {
            modRecurrence.setAdvanceNbr(newValue);
            stateChange();
            setAdvanceNbr(modRecurrence.getPeriod().getType(), modRecurrence.getAdvanceNbr());
        }
    }

    private void endNeverRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setEndValues();
        setEnabled(isEnabled());
    }

    private void endDateRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setEndValues();
        setEnabled(isEnabled());
    }

    private void endNbrSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {
        setEndValues();
    }

    private void endDateFieldPropertyChange(java.beans.PropertyChangeEvent evt) {
        setEndValues();
    }

    private void startStartDateRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        modRecurrence.setStartDate(recurrenceStartDate);
        setEndValues();
        stateChange();
        setEnabled(isEnabled());
    }

    private void startActionDateRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        modRecurrence.setStartDate(actionScheduledDate);
        setEndValues();
        stateChange();
        setEnabled(isEnabled());
    }

    private void startEnterDateFieldPropertyChange(java.beans.PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("value")) {
            modRecurrence.setStartDate(startEnterDateField.getDate());
            setEndValues();
            stateChange();
        }
    }

    private void startEnterRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        modRecurrence.setStartDate(startEnterDateField.getDate());
        setEndValues();
        stateChange();
        setEnabled(isEnabled());
    }

    private void frequencySpinnerStateChanged(javax.swing.event.ChangeEvent evt) {
        modRecurrence.setFrequency((Integer) frequencySpinner.getValue());
        setEndValues();
        stateChange();
    }

    private Integer getAdvanceNbr(PeriodType type) {
        if (mapAdvanceNbrs == null) {
            mapAdvanceNbrs = new HashMap<PeriodType, Integer>();
        }
        Integer n = mapAdvanceNbrs.get(type);
        if (n == null || n < 1) {
            n = getPeriod(type).getDefaultAdvanceNbr();
        }
        return n;
    }

    private void setAdvanceNbr(PeriodType type, Integer advanceNbr) {
        if (mapAdvanceNbrs == null) {
            mapAdvanceNbrs = new HashMap<PeriodType, Integer>();
        }
        mapAdvanceNbrs.put(type, advanceNbr);
    }

    private Period getPeriod(PeriodType type) {
        if (mapPeriods == null) {
            mapPeriods = new HashMap<PeriodType, Period>();
        }
        return mapPeriods.get(type);
    }

    private void setPeriod(Period period) {
        if (mapPeriods == null) {
            mapPeriods = new HashMap<PeriodType, Period>();
        }
        mapPeriods.put(period.getType(), period);
    }

    public void initPanel(boolean basisChanged) {
        onLabel = new JLabel(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "on"));
        frequencySpinner.setValue(modRecurrence.getFrequency());
        periodComboBox.setSelectedItem(modRecurrence.getPeriod().getType());
        advanceSpinner.setValue(modRecurrence.getAdvanceNbr());
        endNbrRadioButton.setSelected(modRecurrence.getEndNbr() != null);
        endNbrSpinner.setValue(modRecurrence.getEndNbr() == null ? 2 : modRecurrence.getEndNbr());
        endDateRadioButton.setSelected(modRecurrence.getEndDate() != null);
        endDateField.setDate(modRecurrence.getEndDate());
        endNeverRadioButton.setSelected(!endNbrRadioButton.isSelected() && !endDateRadioButton.isSelected());
        if (basisChanged) {
            startLabel.setText(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "generate.recurrent.actions.from"));
            startStartDateRadioButton.setVisible(false);
            startStartDateRadioButton.setEnabled(false);
            startStartDateRadioButton.setSelected(false);
        } else {
            startLabel.setText(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "delete.and.regenerate.recurrent.actions.from"));
            startStartDateRadioButton.setVisible(true);
            startStartDateRadioButton.setEnabled(true);
            String strStartDate = Constants.DATE_FORMAT_FIXED.format(recurrenceStartDate);
            startStartDateRadioButton.setText(startStartDateRadioButton.getText() + " (" + strStartDate + ")");
        }
        String strActionDate = Constants.DATE_FORMAT_FIXED.format(actionScheduledDate);
        startActionDateRadioButton.setText(startActionDateRadioButton.getText() + " (" + strActionDate + ")");
        startActionDateRadioButton.setSelected(true);
        modRecurrence.setStartDate(actionScheduledDate);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        advanceLabel1.setEnabled(enabled);
        advanceLabel2.setEnabled(enabled);
        advanceSpinner.setEnabled(enabled);
        endDateRadioButton.setEnabled(enabled);
        endDateField.setEnabled(enabled && endDateRadioButton.isSelected());
        endLabel.setEnabled(enabled);
        endNbrLabel.setEnabled(enabled);
        endNbrRadioButton.setEnabled(enabled);
        endNbrSpinner.setEnabled(enabled && endNbrRadioButton.isSelected());
        endNeverRadioButton.setEnabled(enabled);
        frequencySpinner.setEnabled(enabled);
        periodComboBox.setEnabled(enabled);
        periodLabel.setEnabled(enabled);
        periodPanel.setEnabled(enabled);
        if (periodWeeklyPanel != null) {
            onLabel.setEnabled(enabled);
            periodWeeklyPanel.setEnabled(enabled);
        }
        if (periodMonthlyPanel != null) {
            periodMonthlyPanel.setEnabled(enabled);
        }
        if (periodYearlyPanel != null) {
            periodYearlyPanel.setEnabled(enabled);
        }
        startLabel.setEnabled(enabled);
        startStartDateRadioButton.setEnabled(enabled);
        startActionDateRadioButton.setEnabled(enabled);
        startEnterRadioButton.setEnabled(enabled);
        startEnterDateField.setEnabled(enabled && startEnterRadioButton.isSelected());
    }

    private void setEndValues() {
        if (endNbrRadioButton.isSelected()) {
            modRecurrence.setEndNbr((Integer) endNbrSpinner.getValue());
            modRecurrence.setEndDate(null);
            Date start = modRecurrence.getStartDate();
            if (start != null) {
                Period per = modRecurrence.getPeriod();
                int freq = modRecurrence.getFrequency();
                int endNbr = modRecurrence.getEndNbr();
                Intervals intervals = new Intervals(per, freq, start);
                Date endDate = Recurrence.getTerminationEndDate(intervals, endNbr);
                endDateField.setDate(endDate);
            }
        } else if (endDateRadioButton.isSelected()) {
            modRecurrence.setEndNbr(null);
            modRecurrence.setEndDate(endDateField.getDate());
        } else if (endNeverRadioButton.isSelected()) {
            modRecurrence.setEndNbr(null);
            modRecurrence.setEndDate(null);
        }
        stateChange();
    }

    public void stateChange() {
        putClientProperty("state.change", Long.valueOf(++counter));
    }

    public boolean isValidForm() {
        if (endDateRadioButton.isSelected() && endDateField.getDate() == null) {
            errorLabel1.setText(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "termination.date.must.be.entered"));
            errorLabel2.setText("");
            return false;
        }
        if (startEnterRadioButton.isSelected() && startEnterDateField.getDate() == null) {
            errorLabel1.setText(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "delete.and.regenerate.date.must.be.entered"));
            errorLabel2.setText("");
            return false;
        }
        if (startEnterRadioButton.isSelected() && startEnterDateField.getDate().before(actionScheduledDate)) {
            errorLabel1.setText(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "delete.and.regenerate.date.can.not.be.before.selected.action.date"));
            errorLabel2.setText(org.openide.util.NbBundle.getMessage(RecurrenceRegularPanelModify.class, "delete.and.regenerate.date.can.not.be.before.selected.action.date.hint"));
            return false;
        }
        errorLabel1.setText("");
        errorLabel2.setText("");
        return true;
    }

    private javax.swing.JLabel advanceLabel1;

    private javax.swing.JLabel advanceLabel2;

    private javax.swing.JSpinner advanceSpinner;

    private tr.swing.date.field.DateField endDateField;

    private javax.swing.JRadioButton endDateRadioButton;

    private javax.swing.JLabel endLabel;

    private javax.swing.JLabel endNbrLabel;

    private javax.swing.JRadioButton endNbrRadioButton;

    private javax.swing.JSpinner endNbrSpinner;

    private javax.swing.JRadioButton endNeverRadioButton;

    private javax.swing.JLabel errorLabel1;

    private javax.swing.JLabel errorLabel2;

    private javax.swing.JLabel fillerLabel;

    private javax.swing.JSpinner frequencySpinner;

    private tr.view.action.recurrence.PeriodTypeComboBox periodComboBox;

    private javax.swing.JLabel periodLabel;

    private javax.swing.JPanel periodPanel;

    private javax.swing.JRadioButton startActionDateRadioButton;

    private javax.swing.ButtonGroup startDatebuttonGroup;

    private tr.swing.date.field.DateField startEnterDateField;

    private javax.swing.JRadioButton startEnterRadioButton;

    private javax.swing.JLabel startLabel;

    private javax.swing.JRadioButton startStartDateRadioButton;

    private javax.swing.ButtonGroup terminationButtonGroup;

    private PeriodWeeklyPanel periodWeeklyPanel;

    private PeriodMonthlyPanel periodMonthlyPanel;

    private PeriodYearlyPanel periodYearlyPanel;

    private Map<PeriodType, Integer> mapAdvanceNbrs;

    private Map<PeriodType, Period> mapPeriods;

    private JLabel onLabel;
}
