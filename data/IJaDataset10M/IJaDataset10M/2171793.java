package imp.util;

import imp.Constants;
import imp.ImproVisor;
import imp.com.OpenLeadsheetCommand;
import imp.com.PlayScoreCommand;
import imp.data.*;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author  Martin Hunt
 */
public class LeadsheetPreview extends javax.swing.JPanel implements PropertyChangeListener, MidiPlayListener, ActionListener, Constants {

    File file = null;

    static MidiSynth midiSynth = null;

    ImageIcon pauseIcon = new ImageIcon(getClass().getResource("/imp/gui/graphics/toolbar/pause.gif"));

    ImageIcon playIcon = new ImageIcon(getClass().getResource("/imp/gui/graphics/toolbar/play.gif"));

    JFileChooser parent;

    PreviewTableModel previewTableModel;

    PlaybackSliderManager playbackManager;

    private void ensureMidiSynth() {
        if (midiSynth == null) {
            midiSynth = new MidiSynth(ImproVisor.getMidiManager());
        }
    }

    /** Creates new form LeadsheetPreview */
    public LeadsheetPreview(JFileChooser chooser) {
        parent = chooser;
        parent.addPropertyChangeListener(this);
        parent.addActionListener(this);
        ensureMidiSynth();
        initComponents();
        playbackManager = new PlaybackSliderManager(midiSynth, timeLabel, totalTimeLabel, locSlider);
        previewTableModel.update();
        TableColumn c0 = previewTable.getColumnModel().getColumn(0);
        c0.setCellRenderer(new RightAlignRenderer());
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jLabel2 = new javax.swing.JLabel();
        playerPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        playBtn = new javax.swing.JButton();
        stopBtn = new javax.swing.JButton();
        timeLabel = new javax.swing.JLabel();
        locSlider = new javax.swing.JSlider();
        totalTimeLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        volumeLabel = new javax.swing.JLabel();
        volSlider = new javax.swing.JSlider();
        jSeparator3 = new javax.swing.JSeparator();
        checkbox = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        previewTable = new javax.swing.JTable();
        setLayout(new java.awt.GridBagLayout());
        setMinimumSize(new java.awt.Dimension(220, 200));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(220, 300));
        jLabel2.setFont(new java.awt.Font("Dialog", 1, 12));
        jLabel2.setText("Preview");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jLabel2, gridBagConstraints);
        playerPanel.setLayout(new java.awt.GridBagLayout());
        jPanel1.setLayout(new java.awt.GridBagLayout());
        playBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imp/gui/graphics/toolbar/play.gif")));
        playBtn.setMaximumSize(new java.awt.Dimension(32, 32));
        playBtn.setMinimumSize(new java.awt.Dimension(32, 32));
        playBtn.setOpaque(false);
        playBtn.setPreferredSize(new java.awt.Dimension(32, 32));
        playBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(playBtn, gridBagConstraints);
        stopBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imp/gui/graphics/toolbar/stop.gif")));
        stopBtn.setMaximumSize(new java.awt.Dimension(32, 32));
        stopBtn.setMinimumSize(new java.awt.Dimension(32, 32));
        stopBtn.setPreferredSize(new java.awt.Dimension(32, 32));
        stopBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(stopBtn, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 5, 0);
        playerPanel.add(jPanel1, gridBagConstraints);
        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        timeLabel.setText("0:00");
        timeLabel.setPreferredSize(new java.awt.Dimension(50, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        playerPanel.add(timeLabel, gridBagConstraints);
        locSlider.setMaximum(10000);
        locSlider.setValue(0);
        locSlider.setMinimumSize(new java.awt.Dimension(128, 25));
        locSlider.setPreferredSize(new java.awt.Dimension(128, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        playerPanel.add(locSlider, gridBagConstraints);
        totalTimeLabel.setText("0:00");
        totalTimeLabel.setMinimumSize(new java.awt.Dimension(30, 14));
        totalTimeLabel.setPreferredSize(new java.awt.Dimension(30, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        playerPanel.add(totalTimeLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        playerPanel.add(jSeparator1, gridBagConstraints);
        volumeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        volumeLabel.setText("Volume");
        volumeLabel.setPreferredSize(new java.awt.Dimension(50, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        playerPanel.add(volumeLabel, gridBagConstraints);
        volSlider.setMaximum(127);
        volSlider.setValue(127);
        volSlider.setMinimumSize(new java.awt.Dimension(128, 25));
        volSlider.setPreferredSize(new java.awt.Dimension(128, 25));
        volSlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                volSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        playerPanel.add(volSlider, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(playerPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jSeparator3, gridBagConstraints);
        checkbox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkbox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        add(checkbox, gridBagConstraints);
        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);
        jScrollPane2.setOpaque(false);
        jScrollPane2.setViewport(null);
        previewTable.setModel((previewTableModel = new PreviewTableModel(previewTable)));
        previewTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane2.setViewportView(previewTable);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        add(jScrollPane2, gridBagConstraints);
    }

    private void volSliderStateChanged(javax.swing.event.ChangeEvent evt) {
        midiSynth.setMasterVolume(volSlider.getValue());
    }

    private void stopBtnActionPerformed(java.awt.event.ActionEvent evt) {
        midiSynth.stop("stop button");
    }

    private void playBtnActionPerformed(java.awt.event.ActionEvent evt) {
        if (status != MidiPlayListener.Status.STOPPED) {
            midiSynth.pause();
        } else {
            playLeadsheet(0);
        }
    }

    public void loadLeadsheet() {
        if (file == null || !file.exists() || file.isDirectory()) {
            for (int i = 0; i < previewTable.getRowCount(); i++) previewTable.setValueAt("", i, 1);
        } else {
            boolean wasPlaying = (getPlaying() == MidiPlayListener.Status.PLAYING);
            midiSynth.stop("load leadsheet");
            previewTableModel.loadScore(file);
            playbackManager.setTotalTimeSeconds(previewTableModel.score.getTotalTime());
            if (wasPlaying) playLeadsheet(0);
        }
    }

    public JCheckBox getCheckbox() {
        return checkbox;
    }

    public String getTitle() {
        return previewTableModel.score.getTitle();
    }

    public void playLeadsheet(int transposition) {
        Score score = previewTableModel.score;
        if (score == null) {
            return;
        }
        long time = playbackManager.getMicrosecondsFromSlider();
        midiSynth.setTempo((float) score.getTempo());
        new PlayScoreCommand(previewTableModel.score, time, true, midiSynth, this, 0, transposition).execute();
    }

    public void propertyChange(PropertyChangeEvent e) {
        boolean update = false;
        String prop = e.getPropertyName();
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
            file = null;
            update = true;
        } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
            file = (File) e.getNewValue();
            update = true;
        }
        if (update) {
            if (isShowing()) {
                loadLeadsheet();
                ensureMidiSynth();
                repaint();
            }
        }
    }

    MidiPlayListener.Status status = MidiPlayListener.Status.STOPPED;

    public void setPlaying(MidiPlayListener.Status playing, int transposition) {
        playbackManager.setPlaying(playing, transposition);
        MidiPlayListener.Status oldStatus = status;
        status = playing;
        switch(playing) {
            case PLAYING:
                playBtn.setIcon(pauseIcon);
                break;
            case STOPPED:
                playBtn.setIcon(playIcon);
                break;
            case PAUSED:
                playBtn.setIcon(pauseIcon);
                break;
        }
    }

    public MidiPlayListener.Status getPlaying() {
        return status;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command == null) return;
        if (command.equals(JFileChooser.APPROVE_SELECTION)) quit(); else if (command.equals(JFileChooser.CANCEL_SELECTION)) quit();
    }

    public void quit() {
        midiSynth.stop("quit");
    }

    private javax.swing.JCheckBox checkbox;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator3;

    private javax.swing.JSlider locSlider;

    private javax.swing.JButton playBtn;

    private javax.swing.JPanel playerPanel;

    private javax.swing.JTable previewTable;

    private javax.swing.JButton stopBtn;

    private javax.swing.JTextArea textPreview;

    private javax.swing.JLabel timeLabel;

    private javax.swing.JLabel totalTimeLabel;

    private javax.swing.JSlider volSlider;

    private javax.swing.JLabel volumeLabel;

    class RightAlignRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

        Font font = null;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            ((JLabel) c).setHorizontalAlignment(JLabel.RIGHT);
            if (font == null) {
                font = new Font(((JLabel) c).getFont().getName(), Font.BOLD, ((JLabel) c).getFont().getSize());
            }
            ((JLabel) c).setFont(font);
            return c;
        }
    }

    class PreviewTableModel extends AbstractTableModel {

        JTable table;

        public Score score = null;

        public void loadScore(File file) {
            score = new Score();
            (new OpenLeadsheetCommand(file, score)).execute();
            update();
        }

        public void update() {
            fireTableDataChanged();
            AutofitTableColumns.autoResizeTable(table, false);
        }

        public PreviewTableModel(JTable table) {
            this.table = table;
        }

        public int getColumnCount() {
            return 2;
        }

        public String getColumnName(int col) {
            return "";
        }

        public Class getColumnClass(int col) {
            return String.class;
        }

        public int getRowCount() {
            return 8;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public Object getValueAt(int row, int col) {
            String returnValue = "";
            if (col == 0) {
                switch(row) {
                    case 0:
                        returnValue = "Title";
                        break;
                    case 1:
                        returnValue = "Composer";
                        break;
                    case 2:
                        returnValue = "Style";
                        break;
                    case 3:
                        returnValue = "Duration";
                        break;
                    case 4:
                        returnValue = "Tempo";
                        break;
                    case 5:
                        returnValue = "TimeSig";
                        break;
                    case 6:
                        returnValue = "Key";
                        break;
                    case 7:
                        returnValue = "Chords";
                        break;
                }
                returnValue += " ";
            } else {
                if (score != null) {
                    int[] metre = score.getMetre();
                    ChordPart chords = score.getChordProg();
                    switch(row) {
                        case 0:
                            returnValue = score.getTitle();
                            break;
                        case 1:
                            returnValue = score.getComposer();
                            break;
                        case 2:
                            {
                                Style style = score.getChordProg().getStyle();
                                String styleName = style == null ? "unknown" : style.getName();
                                returnValue = styleName;
                                break;
                            }
                        case 3:
                            returnValue = PlaybackSliderManager.formatSecond(score.getTotalTime());
                            break;
                        case 4:
                            returnValue = String.valueOf(score.getTempo()) + " BPM";
                            break;
                        case 5:
                            returnValue = metre[0] + "/" + metre[1];
                            break;
                        case 6:
                            returnValue = String.valueOf(score.getKeySignature());
                            break;
                        case 7:
                            Chord.initSaveToLeadsheet();
                            int beatValue = WHOLE / metre[1];
                            int measureLength = metre[0] * beatValue;
                            int startIndex = 0;
                            int stopIndex = measureLength * 4;
                            Writer writer = new StringWriter();
                            BufferedWriter out = new BufferedWriter(writer);
                            try {
                                for (int index = startIndex; index <= stopIndex; index++) {
                                    Unit unit = chords.getUnit(index);
                                    if (unit != null) {
                                        unit.saveLeadsheet(out, metre, false);
                                        out.write(" ");
                                    }
                                }
                                out.flush();
                                writer.flush();
                            } catch (IOException e) {
                            }
                            returnValue = writer.toString();
                            break;
                    }
                }
                returnValue = " " + returnValue;
            }
            return returnValue;
        }
    }
}
