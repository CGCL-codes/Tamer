package evolution.view;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import evolution.Evolution;
import evolution.Range;
import evolution.mlp.MLP;
import evolution.tutors.Tutor;
import evolution.tutors.TutorFactory;

/**
 * 
 * @author camille
 */
public class Revo extends javax.swing.JFrame {

    /**
	 * SVUID
	 */
    private static final long serialVersionUID = 1L;

    private Graph graph;

    private RevoWorker currentWorker;

    PropertyChangeListener pcl;

    /** Creates new form Revo */
    public Revo() {
        initComponents();
        fillTutors();
        graph = new Graph("Fitness evolution", "Generation", "Fitness");
        jPanelGraph.add(graph.getChartPanel());
        jTabbedPaneDisplays.repaint();
        jButtonStop.setEnabled(false);
        jSpinnerInput.setValue(3);
        jSpinnerHidden.setValue(16);
        jSpinnerOutput.setValue(2);
        setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
    }

    private void fillTutors() {
        for (Tutor t : TutorFactory.getInstance().getAllTutors()) {
            jComboBoxTutors.addItem(t.getName());
        }
    }

    private boolean canLaunch() {
        return (Integer.parseInt(jTextFieldRangeMax.getText()) > Integer.parseInt(jTextFieldRangeMin.getText()));
    }

    private void showAsTable(final MLP mlp) {
        AbstractTableModel model = new AbstractTableModel() {

            private static final long serialVersionUID = 0L;

            @Override
            public String getColumnName(int col) {
                if (col == 0) {
                    return "Neurons";
                } else {
                    if (col <= mlp.getInputLayer().size()) {
                        return "Input" + col;
                    } else {
                        return "Output" + (col - mlp.getInputLayer().size());
                    }
                }
            }

            @Override
            public int getColumnCount() {
                return mlp.getInputLayer().size() + mlp.getOutputLayer().size() + 1;
            }

            @Override
            public int getRowCount() {
                return mlp.getHiddenLayer().size();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex == 0) {
                    return "Hidden " + (rowIndex + 1);
                } else {
                    if (columnIndex <= mlp.getInputLayer().size()) {
                        return mlp.getHiddenLayer().get(rowIndex).getIn().get(mlp.getInputLayer().get(columnIndex - 1));
                    } else {
                        return mlp.getHiddenLayer().get(rowIndex).getOut().get(mlp.getOutputLayer().get(columnIndex - 1 - mlp.getInputLayer().size()));
                    }
                }
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
            }
        };
        jTableMLP.setModel(model);
        jTableMLP.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableMLP.repaint();
        jScrollPaneMLPTable.repaint();
    }

    private void appendInfo(final String info) {
        Runnable append = new Runnable() {

            public void run() {
                jTextAreaConsole.append("\n" + info);
                jTextAreaConsole.setCaretPosition(jTextAreaConsole.getDocument().getLength());
                jTextAreaConsole.setText(jTextAreaConsole.getText());
                jScrollPaneConsole.invalidate();
            }
        };
        SwingUtilities.invokeLater(append);
    }

    private void initComponents() {
        jPanelCenter = new javax.swing.JPanel();
        jTabbedPaneDisplays = new javax.swing.JTabbedPane();
        jPanelGraph = new javax.swing.JPanel();
        jPanelTable = new javax.swing.JPanel();
        jScrollPaneMLPTable = new javax.swing.JScrollPane();
        jTableMLP = new javax.swing.JTable();
        jPanelSouth = new javax.swing.JPanel();
        jScrollPaneConsole = new javax.swing.JScrollPane();
        jTextAreaConsole = new javax.swing.JTextArea();
        jPanelWest = new javax.swing.JPanel();
        jTabbedPaneParameters = new javax.swing.JTabbedPane();
        jPanelEvo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldN = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldInitRate = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldRangeMin = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldRangeMax = new javax.swing.JTextField();
        jPanelTutor = new javax.swing.JPanel();
        jComboBoxTutors = new javax.swing.JComboBox();
        MLP = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jSpinnerInput = new javax.swing.JSpinner();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jSpinnerHidden = new javax.swing.JSpinner();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jSpinnerOutput = new javax.swing.JSpinner();
        jButtonLaunch = new javax.swing.JButton();
        jButtonStop = new javax.swing.JButton();
        jToolBar = new JToolBar();
        jProgressBarEvo = new JProgressBar(0, 100);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jPanelCenter.setLayout(new javax.swing.BoxLayout(jPanelCenter, javax.swing.BoxLayout.LINE_AXIS));
        jPanelGraph.setLayout(new javax.swing.BoxLayout(jPanelGraph, javax.swing.BoxLayout.LINE_AXIS));
        jTabbedPaneDisplays.addTab("Progression", jPanelGraph);
        jPanelTable.setLayout(new javax.swing.BoxLayout(jPanelTable, javax.swing.BoxLayout.LINE_AXIS));
        jTableMLP.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { {}, {}, {}, {} }, new String[] {}));
        jScrollPaneMLPTable.setViewportView(jTableMLP);
        jPanelTable.add(jScrollPaneMLPTable);
        jTabbedPaneDisplays.addTab("Weights", jPanelTable);
        jPanelCenter.add(jTabbedPaneDisplays);
        getContentPane().add(jPanelCenter, java.awt.BorderLayout.CENTER);
        jPanelSouth.setBorder(javax.swing.BorderFactory.createTitledBorder("Console"));
        jPanelSouth.setPreferredSize(new java.awt.Dimension(100, 150));
        jPanelSouth.setLayout(new javax.swing.BoxLayout(jPanelSouth, javax.swing.BoxLayout.PAGE_AXIS));
        jTextAreaConsole.setColumns(20);
        jTextAreaConsole.setEditable(false);
        jTextAreaConsole.setRows(5);
        jTextAreaConsole.setBorder(null);
        jScrollPaneConsole.setViewportView(jTextAreaConsole);
        jPanelSouth.add(jScrollPaneConsole);
        jToolBar.setFloatable(false);
        jToolBar.add(jProgressBarEvo);
        jPanelSouth.add(jToolBar);
        getContentPane().add(jPanelSouth, java.awt.BorderLayout.SOUTH);
        jPanelWest.setBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"));
        jPanelWest.setAlignmentX(0.0F);
        jPanelWest.setAlignmentY(0.0F);
        jPanelWest.setMaximumSize(new java.awt.Dimension(250, 2147483647));
        jPanelWest.setMinimumSize(new java.awt.Dimension(250, 123));
        jPanelWest.setPreferredSize(new java.awt.Dimension(250, 100));
        jPanelWest.setLayout(new javax.swing.BoxLayout(jPanelWest, javax.swing.BoxLayout.PAGE_AXIS));
        jPanelEvo.setLayout(new javax.swing.BoxLayout(jPanelEvo, javax.swing.BoxLayout.PAGE_AXIS));
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));
        jLabel1.setText("Generation number");
        jLabel1.setMaximumSize(new java.awt.Dimension(130, 64));
        jLabel1.setMinimumSize(new java.awt.Dimension(130, 64));
        jLabel1.setPreferredSize(new java.awt.Dimension(130, 64));
        jPanel1.add(jLabel1);
        jTextFieldN.setText("200");
        jTextFieldN.setAlignmentX(0.0F);
        jTextFieldN.setMaximumSize(new java.awt.Dimension(100, 16));
        jTextFieldN.setPreferredSize(new java.awt.Dimension(100, 22));
        jTextFieldN.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNActionPerformed(evt);
            }
        });
        jPanel1.add(jTextFieldN);
        jPanelEvo.add(jPanel1);
        jPanel2.setAlignmentX(0.0F);
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));
        jLabel2.setText("Initial rate");
        jLabel2.setMaximumSize(new java.awt.Dimension(64, 64));
        jLabel2.setMinimumSize(new java.awt.Dimension(64, 64));
        jLabel2.setPreferredSize(new java.awt.Dimension(70, 64));
        jPanel2.add(jLabel2);
        jTextFieldInitRate.setText("0.00005");
        jTextFieldInitRate.setAlignmentX(0.0F);
        jTextFieldInitRate.setMaximumSize(new java.awt.Dimension(100, 16));
        jTextFieldInitRate.setPreferredSize(new java.awt.Dimension(100, 22));
        jTextFieldInitRate.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldInitRateActionPerformed(evt);
            }
        });
        jPanel2.add(jTextFieldInitRate);
        jPanelEvo.add(jPanel2);
        jPanel3.setAlignmentX(0.0F);
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));
        jPanelEvo.add(jPanel3);
        jPanel4.setAlignmentX(0.0F);
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));
        jLabel3.setText("Range min.");
        jLabel3.setMaximumSize(new java.awt.Dimension(80, 64));
        jLabel3.setMinimumSize(new java.awt.Dimension(80, 64));
        jLabel3.setPreferredSize(new java.awt.Dimension(80, 64));
        jPanel4.add(jLabel3);
        jTextFieldRangeMin.setText("-10");
        jTextFieldRangeMin.setMaximumSize(new java.awt.Dimension(32, 16));
        jTextFieldRangeMin.setMinimumSize(new java.awt.Dimension(32, 16));
        jTextFieldRangeMin.setPreferredSize(new java.awt.Dimension(32, 16));
        jTextFieldRangeMin.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRangeMinActionPerformed(evt);
            }
        });
        jPanel4.add(jTextFieldRangeMin);
        jPanelEvo.add(jPanel4);
        jPanel5.setAlignmentX(0.0F);
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));
        jLabel4.setText("Range max.");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel4.setMaximumSize(new java.awt.Dimension(80, 64));
        jLabel4.setMinimumSize(new java.awt.Dimension(80, 64));
        jLabel4.setPreferredSize(new java.awt.Dimension(80, 64));
        jPanel5.add(jLabel4);
        jTextFieldRangeMax.setText("10");
        jTextFieldRangeMax.setMaximumSize(new java.awt.Dimension(32, 16));
        jTextFieldRangeMax.setMinimumSize(new java.awt.Dimension(32, 16));
        jTextFieldRangeMax.setPreferredSize(new java.awt.Dimension(32, 22));
        jPanel5.add(jTextFieldRangeMax);
        jPanelEvo.add(jPanel5);
        jTabbedPaneParameters.addTab("Evolution", jPanelEvo);
        jPanelTutor.setLayout(new javax.swing.BoxLayout(jPanelTutor, javax.swing.BoxLayout.LINE_AXIS));
        jComboBoxTutors.setModel(new javax.swing.DefaultComboBoxModel(new String[] {}));
        jComboBoxTutors.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxTutorsItemStateChanged(evt);
            }
        });
        jComboBoxTutors.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxTutorsActionPerformed(evt);
            }
        });
        jPanelTutor.add(jComboBoxTutors);
        jTabbedPaneParameters.addTab("Tutors", jPanelTutor);
        MLP.setLayout(new javax.swing.BoxLayout(MLP, javax.swing.BoxLayout.PAGE_AXIS));
        jPanel6.setAlignmentX(0.0F);
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));
        jLabel6.setText("Input layer size");
        jLabel6.setMaximumSize(new java.awt.Dimension(130, 64));
        jLabel6.setMinimumSize(new java.awt.Dimension(130, 64));
        jLabel6.setPreferredSize(new java.awt.Dimension(130, 64));
        jPanel6.add(jLabel6);
        jSpinnerInput.setMaximumSize(new java.awt.Dimension(50, 24));
        jSpinnerInput.setMinimumSize(new java.awt.Dimension(50, 24));
        jSpinnerInput.setPreferredSize(new java.awt.Dimension(50, 24));
        jSpinnerInput.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerInputStateChanged(evt);
            }
        });
        jPanel6.add(jSpinnerInput);
        MLP.add(jPanel6);
        jPanel7.setAlignmentX(0.0F);
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));
        jLabel7.setText("Hidden layer size");
        jLabel7.setMaximumSize(new java.awt.Dimension(130, 64));
        jLabel7.setMinimumSize(new java.awt.Dimension(130, 64));
        jLabel7.setPreferredSize(new java.awt.Dimension(130, 64));
        jPanel7.add(jLabel7);
        jSpinnerHidden.setMaximumSize(new java.awt.Dimension(50, 24));
        jSpinnerHidden.setMinimumSize(new java.awt.Dimension(50, 24));
        jSpinnerHidden.setPreferredSize(new java.awt.Dimension(50, 24));
        jSpinnerHidden.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerHiddenStateChanged(evt);
            }
        });
        jPanel7.add(jSpinnerHidden);
        MLP.add(jPanel7);
        jPanel8.setAlignmentX(0.0F);
        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.LINE_AXIS));
        jLabel8.setText("Output layer size");
        jLabel8.setMaximumSize(new java.awt.Dimension(130, 64));
        jLabel8.setMinimumSize(new java.awt.Dimension(130, 64));
        jLabel8.setPreferredSize(new java.awt.Dimension(130, 64));
        jPanel8.add(jLabel8);
        jSpinnerOutput.setMaximumSize(new java.awt.Dimension(50, 24));
        jSpinnerOutput.setMinimumSize(new java.awt.Dimension(50, 24));
        jSpinnerOutput.setPreferredSize(new java.awt.Dimension(50, 24));
        jSpinnerOutput.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerOutputStateChanged(evt);
            }
        });
        jPanel8.add(jSpinnerOutput);
        MLP.add(jPanel8);
        jTabbedPaneParameters.addTab("MLP", MLP);
        jPanelWest.add(jTabbedPaneParameters);
        jButtonLaunch.setText("Launch");
        jButtonLaunch.setAlignmentX(0.5F);
        jButtonLaunch.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLaunchActionPerformed(evt);
            }
        });
        jButtonLaunch.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonLaunchMousePressed(evt);
            }
        });
        jPanelWest.add(jButtonLaunch);
        jButtonStop.setText("Stop");
        jButtonStop.setAlignmentX(0.5F);
        jButtonStop.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopActionPerformed(evt);
            }
        });
        jButtonStop.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonStopMousePressed(evt);
            }
        });
        getContentPane().add(jPanelWest, java.awt.BorderLayout.WEST);
        pack();
    }

    private void jTextFieldNActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextFieldInitRateActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jTextFieldRangeMinActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButtonLaunchActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButtonStopActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButtonLaunchMousePressed(java.awt.event.MouseEvent evt) {
        if (canLaunch() && jButtonLaunch.isEnabled()) {
            jButtonLaunch.setEnabled(false);
            jButtonStop.setEnabled(true);
            appendInfo(Calendar.getInstance().getTime() + " - Initializing experiment");
            MLP mlp = new MLP((Integer) jSpinnerInput.getValue(), (Integer) jSpinnerHidden.getValue(), (Integer) jSpinnerOutput.getValue());
            mlp.initConnexions();
            Tutor tutor = TutorFactory.getInstance().getTutor((String) jComboBoxTutors.getSelectedItem());
            Evolution evo = new Evolution(Integer.parseInt(jTextFieldN.getText()), Double.parseDouble(jTextFieldInitRate.getText()), new Range(Double.parseDouble(jTextFieldRangeMin.getText()), Double.parseDouble(jTextFieldRangeMax.getText())));
            currentWorker = new RevoWorker(mlp, tutor, evo);
            pcl = new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(RevoWorker.PROGRESS)) {
                        int progress = (Integer) evt.getNewValue();
                        if (progress == 100) {
                            jProgressBarEvo.setValue(progress);
                            jButtonLaunch.setEnabled(true);
                            appendInfo(Calendar.getInstance().getTime() + " - End of experiment");
                        } else {
                            jProgressBarEvo.setValue(progress);
                        }
                    }
                    if (evt.getPropertyName().equals(RevoWorker.INFO)) {
                        appendInfo("\t" + (String) evt.getNewValue());
                    }
                    if (evt.getPropertyName().equals(RevoWorker.DATA)) {
                        graph.addPoint((Integer) evt.getOldValue(), (Double) evt.getNewValue());
                    }
                    if (evt.getPropertyName().equals(RevoWorker.MLP)) {
                        showAsTable((MLP) evt.getNewValue());
                    }
                }
            };
            currentWorker.addPropertyChangeListener(pcl);
            graph.clear();
            appendInfo("\tRunning experiment " + tutor.getName() + " ...");
            graph.getChartPanel().getChart().setTitle("Fitness evolution (" + tutor.getName() + ")");
            currentWorker.execute();
        }
    }

    private void jButtonStopMousePressed(java.awt.event.MouseEvent evt) {
        if (jButtonStop.isEnabled()) {
            currentWorker.cancel(true);
            jButtonLaunch.setEnabled(true);
            jButtonStop.setEnabled(false);
        }
    }

    private void jComboBoxTutorsActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jSpinnerInputStateChanged(javax.swing.event.ChangeEvent evt) {
        if ((Integer) jSpinnerInput.getValue() < 0) {
            jSpinnerInput.setValue(0);
        }
    }

    private void jSpinnerHiddenStateChanged(javax.swing.event.ChangeEvent evt) {
        if ((Integer) jSpinnerHidden.getValue() < 0) {
            jSpinnerInput.setValue(0);
        }
    }

    private void jSpinnerOutputStateChanged(javax.swing.event.ChangeEvent evt) {
        if ((Integer) jSpinnerOutput.getValue() < 0) {
            jSpinnerInput.setValue(0);
        }
    }

    private void jComboBoxTutorsItemStateChanged(java.awt.event.ItemEvent evt) {
    }

    /**
	 * @param args
	 *            the command line arguments
	 */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Revo().setVisible(true);
            }
        });
    }

    private javax.swing.JPanel MLP;

    private javax.swing.JButton jButtonLaunch;

    private javax.swing.JButton jButtonStop;

    private javax.swing.JComboBox jComboBoxTutors;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JPanel jPanelCenter;

    private javax.swing.JPanel jPanelEvo;

    private javax.swing.JPanel jPanelGraph;

    private javax.swing.JPanel jPanelSouth;

    private javax.swing.JPanel jPanelTable;

    private javax.swing.JPanel jPanelTutor;

    private javax.swing.JPanel jPanelWest;

    private javax.swing.JScrollPane jScrollPaneConsole;

    private javax.swing.JScrollPane jScrollPaneMLPTable;

    private javax.swing.JSpinner jSpinnerHidden;

    private javax.swing.JSpinner jSpinnerInput;

    private javax.swing.JSpinner jSpinnerOutput;

    private javax.swing.JTabbedPane jTabbedPaneDisplays;

    private javax.swing.JTabbedPane jTabbedPaneParameters;

    private javax.swing.JTable jTableMLP;

    private javax.swing.JTextArea jTextAreaConsole;

    private javax.swing.JTextField jTextFieldInitRate;

    private javax.swing.JTextField jTextFieldN;

    private javax.swing.JTextField jTextFieldRangeMax;

    private javax.swing.JTextField jTextFieldRangeMin;

    private javax.swing.JToolBar jToolBar;

    private javax.swing.JProgressBar jProgressBarEvo;
}
