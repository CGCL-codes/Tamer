package eu.haslgruebler.util.annotator.component;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import eu.haslgruebler.util.annotator.Annotator;

/**
 * @author Michael Haslgrübler, uni-michael@haslgruebler.eu
 *
 */
public class GraphControl extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3599025898963303930L;

    private Annotator annotator;

    public GraphControl(Annotator annotator) {
        super();
        this.annotator = annotator;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        JPanel col = new JPanel();
        col.setLayout(new GridLayout(3, 1));
        col.add(getGraphControlStart());
        col.add(getGraphControlEnd());
        JButton cur = new JButton("CurTime");
        cur.setEnabled(false);
        col.add(cur);
        row.add(col);
        col = new JPanel();
        col.setLayout(new GridLayout(3, 1));
        col.add(getGraphControlStartTime());
        col.add(getGraphControlEndTime());
        col.add(getGraphControlCurTime());
        row.add(col);
        this.add(row);
        this.add(Box.createVerticalGlue());
        row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.add(getGraphControlLabel());
        this.add(row);
        this.add(Box.createVerticalGlue());
        row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.add(getGraphControlPause());
        this.add(row);
        this.add(Box.createVerticalGlue());
        this.add(getGraphControlSelAct());
        row = new JPanel();
        row.add(getGraphControlCreateAct());
        row.add(getGraphControlConfirm());
        this.add(row);
        this.add(Box.createVerticalGlue());
        addItems();
        addListener();
    }

    /**
	 * 
	 */
    private void addItems() {
        graphControlSelAct.addItem(new String("unlabeled"));
        graphControlSelAct.addItem(new String("a_running"));
        graphControlSelAct.addItem(new String("a_walking"));
        graphControlSelAct.addItem(new String("a_squat"));
        graphControlSelAct.addItem(new String("a_jumping_jack"));
        graphControlSelAct.addItem(new String("a_pushup"));
        graphControlSelAct.addItem(new String("a_sit-up"));
        graphControlSelAct.addItem(new String("a_jumping"));
        graphControlSelAct.addItem(new String("g_sitting"));
        graphControlSelAct.addItem(new String("g_standing"));
        graphControlSelAct.addItem(new String("g_arm_horizontal"));
        graphControlSelAct.addItem(new String("g_arm_vertical"));
        graphControlSelAct.addItem(new String("g_arm_horizontal"));
        graphControlSelAct.addItem(new String("g_arm_horizontal_forward"));
        graphControlSelAct.addItem(new String("g_arm_horizontal_backward"));
        graphControlSelAct.addItem(new String("a_arm_vertical_rotating_90_forwards"));
    }

    private void addListener() {
        graphControlPause.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (annotator.getDataReader().isPause() == true) {
                    annotator.getDataReader().setPause(false);
                    graphControlPause.setText("Pause");
                } else {
                    annotator.getDataReader().setPause(true);
                    graphControlPause.setText("Play");
                }
            }
        });
        annotator.getGraph().getDrawingPane().addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                String text = new String(annotator.getGraph().getTime(arg0.getX()));
                graphControlCurTime.setText(text);
                graphControlLabel.setText(annotator.getArffWriter().getLabel(arg0.getX(), text));
                if (arg0.getModifiersEx() == InputEvent.CTRL_DOWN_MASK) {
                    graphControlStartTime.setText(text);
                } else if (arg0.getModifiersEx() == InputEvent.SHIFT_DOWN_MASK) {
                    graphControlEndTime.setText(text);
                }
            }
        });
        graphControlStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                graphControlStartTime.setText(graphControlCurTime.getText());
            }
        });
        graphControlEnd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                graphControlEndTime.setText(graphControlCurTime.getText());
            }
        });
        graphControlCreateAct.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String line = JOptionPane.showInputDialog(null, "Enter new Activity", "Create Activity", JOptionPane.OK_CANCEL_OPTION);
                if (line != null) {
                    line = line.trim();
                    line = line.replace(' ', '_');
                    graphControlSelAct.addItem(line);
                    graphControlSelAct.setSelectedItem(line);
                }
            }
        });
        graphControlConfirm.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    long endTime = Long.parseLong(graphControlEndTime.getText());
                    long startTime = Long.parseLong(graphControlStartTime.getText());
                    if (endTime <= startTime) {
                        JOptionPane.showMessageDialog(annotator.getFrame(), "End time is before Start Time");
                    } else {
                        String activity = (String) graphControlSelAct.getSelectedItem();
                        annotator.getArffWriter().update(graphControlStartTime.getText(), graphControlEndTime.getText(), activity);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private JLabel graphControlStartTime;

    private JLabel getGraphControlStartTime() {
        if (graphControlStartTime == null) {
            graphControlStartTime = new JLabel("0000000000000");
        }
        return graphControlStartTime;
    }

    public void reset() {
        graphControlStartTime.setText("0000000000000");
        graphControlCurTime.setText("0000000000000");
        graphControlEndTime.setText("0000000000000");
        graphControlLabel.setText("no data");
    }

    private JLabel graphControlCurTime;

    private JLabel getGraphControlCurTime() {
        if (graphControlCurTime == null) {
            graphControlCurTime = new JLabel("0000000000000");
        }
        return graphControlCurTime;
    }

    public String getCurTime() {
        return graphControlCurTime.getText();
    }

    private JLabel graphControlEndTime;

    private JLabel getGraphControlEndTime() {
        if (graphControlEndTime == null) {
            graphControlEndTime = new JLabel("0000000000000");
        }
        return graphControlEndTime;
    }

    private JButton graphControlCreateAct;

    private JButton getGraphControlCreateAct() {
        if (graphControlCreateAct == null) {
            graphControlCreateAct = new JButton("CreateAct");
        }
        return graphControlCreateAct;
    }

    private JComboBox graphControlSelAct;

    private JComboBox getGraphControlSelAct() {
        if (graphControlSelAct == null) {
            graphControlSelAct = new JComboBox();
        }
        return graphControlSelAct;
    }

    public void updateLabels(List<String> labels) {
        if (labels.size() < 1) return;
        for (int i = 0; i < graphControlSelAct.getItemCount(); i++) {
            int index = labels.indexOf(graphControlSelAct.getItemAt(i).toString());
            if (index >= 0) {
                labels.remove(index);
            }
        }
        for (String string : labels) {
            graphControlSelAct.addItem(string);
        }
    }

    private JButton graphControlConfirm;

    private JButton getGraphControlConfirm() {
        if (graphControlConfirm == null) {
            graphControlConfirm = new JButton("Confirm");
        }
        return graphControlConfirm;
    }

    private JButton graphControlStart;

    private JButton getGraphControlStart() {
        if (graphControlStart == null) {
            graphControlStart = new JButton("Start");
        }
        return graphControlStart;
    }

    private JButton graphControlEnd;

    private JButton getGraphControlEnd() {
        if (graphControlEnd == null) {
            graphControlEnd = new JButton("End");
        }
        return graphControlEnd;
    }

    private JButton graphControlPause;

    public JButton getGraphControlPause() {
        if (graphControlPause == null) {
            graphControlPause = new JButton("Pause");
        }
        return graphControlPause;
    }

    private JLabel graphControlLabel;

    private JLabel getGraphControlLabel() {
        if (graphControlLabel == null) {
            graphControlLabel = new JLabel("no data");
        }
        return graphControlLabel;
    }
}
