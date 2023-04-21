package fiswidgets.fisdesktop;

import fiswidgets.fisgui.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

class FlowOptions extends FisFrame implements ItemListener, ActionListener {

    private Container c;

    private FisPanel checkpanel, listpanel, optionspanel, buttonpanel;

    private FisCheckBox breakpoints;

    private FisButton use_singlestep;

    private FisButton clear_breakpoints;

    private JList breaks;

    private JScrollPane scroll;

    private DefaultListModel model;

    private FisButton accept, cancel;

    private Color selfore;

    private FisDesktop pipe;

    private Vector flowVector;

    private static final byte ACTION_ACCEPT = 0;

    private static final byte ACTION_CANCEL = 1;

    private static final byte ACTION_SET_ALL_BREAKS = 2;

    private static final byte ACTION_CLEAR_ALL_BREAKS = 3;

    public FlowOptions(FisDesktop p) {
        super();
        setTitle("Run Options");
        pipe = p;
        setLocation(400, 400);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        checkpanel = new FisPanel();
        listpanel = new FisPanel();
        optionspanel = new FisPanel();
        optionspanel.setBorder(new BevelBorder(BevelBorder.RAISED));
        breakpoints = new FisCheckBox("Set Breakpoints", pipe.usingbreaks, checkpanel);
        breakpoints.addItemListener(this);
        checkpanel.newLine();
        use_singlestep = new FisButton("Set all breakpoints", checkpanel);
        use_singlestep.setEnabled(pipe.usingbreaks);
        use_singlestep.addActionListener(this);
        use_singlestep.setActionCommand(Byte.toString(ACTION_SET_ALL_BREAKS));
        use_singlestep.setToolTipText("This button will set all breakpoints to " + pipe.BREAK + ".\nUse this button to run in single step mode.");
        checkpanel.newLine();
        clear_breakpoints = new FisButton("Clear all breakpoints", checkpanel);
        clear_breakpoints.setEnabled(pipe.usingbreaks);
        clear_breakpoints.addActionListener(this);
        clear_breakpoints.setActionCommand(Byte.toString(ACTION_CLEAR_ALL_BREAKS));
        clear_breakpoints.setToolTipText("This button will set all breakpoints to " + pipe.NO_BREAK + ".");
        model = new DefaultListModel();
        flowVector = pipe.getFlowPositionVector();
        FlowPosition tmp;
        for (int x = 0; x < flowVector.size(); x++) {
            tmp = (FlowPosition) flowVector.elementAt(x);
            model.addElement(tmp.title);
            model.addElement((tmp.breakon) ? pipe.BREAK : pipe.NO_BREAK);
        }
        breaks = new JList();
        selfore = breaks.getSelectionForeground();
        breaks.setCellRenderer(new MyListCellRenderer());
        breaks.setModel(model);
        breaks.setFixedCellWidth(120);
        scroll = new JScrollPane(breaks);
        breaks.setEnabled(pipe.usingbreaks);
        MouseListener mouseListener = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && breaks.isEnabled()) {
                    int index = breaks.locationToIndex(e.getPoint());
                    if (index != -1) {
                        String item = (String) model.elementAt(index);
                        if (item.equals(pipe.NO_BREAK) && breaks.isEnabled()) {
                            model.setElementAt(pipe.BREAK, index);
                            breaks.repaint();
                        } else if (item.equals(pipe.BREAK) && breaks.isEnabled()) {
                            model.setElementAt(pipe.NO_BREAK, index);
                            breaks.repaint();
                        }
                    }
                }
            }
        };
        breaks.addMouseListener(mouseListener);
        FisComponent.addToPanel(scroll, listpanel);
        optionspanel.addFisPanel(checkpanel, 1, 1);
        optionspanel.addFisPanel(listpanel, 1, 1);
        addFisPanel(optionspanel, 1, 1);
        newLine();
        buttonpanel = new FisPanel();
        buttonpanel.setLayout(new FlowLayout());
        accept = new FisButton("Accept Settings", buttonpanel);
        accept.addActionListener(this);
        accept.setActionCommand(Byte.toString(ACTION_ACCEPT));
        cancel = new FisButton("Cancel", buttonpanel);
        cancel.addActionListener(this);
        cancel.setActionCommand(Byte.toString(ACTION_CANCEL));
        addFisPanel(buttonpanel, 1, 1);
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        switch(Byte.parseByte(e.getActionCommand())) {
            case ACTION_ACCEPT:
                pipe.usingbreaks = breakpoints.isSelected();
                if (breakpoints.isSelected()) {
                    for (int x = 0; x < flowVector.size(); x++) {
                        ((FlowPosition) flowVector.elementAt(x)).breakon = (((String) model.elementAt(2 * x + 1)).equals(pipe.BREAK));
                    }
                }
                setVisible(false);
                break;
            case ACTION_CANCEL:
                setVisible(false);
                break;
            case ACTION_SET_ALL_BREAKS:
                for (int x = 0; x < model.getSize(); x++) {
                    if (model.elementAt(x).equals(pipe.NO_BREAK)) {
                        model.setElementAt(pipe.BREAK, x);
                    }
                }
                breaks.repaint();
                break;
            case ACTION_CLEAR_ALL_BREAKS:
                for (int x = 0; x < model.getSize(); x++) {
                    if (model.elementAt(x).equals(pipe.BREAK)) {
                        model.setElementAt(pipe.NO_BREAK, x);
                    }
                }
                breaks.repaint();
                break;
        }
    }

    public void itemStateChanged(ItemEvent e) {
        use_singlestep.setEnabled(breakpoints.isSelected());
        clear_breakpoints.setEnabled(breakpoints.isSelected());
        breaks.setEnabled(breakpoints.isSelected());
        breaks.repaint();
    }

    class MyListCellRenderer extends JLabel implements ListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            String s = value.toString();
            setText(s);
            if (s.equals(pipe.NO_BREAK) && breaks.isEnabled()) setForeground(Color.blue); else if (s.equals(pipe.BREAK) && breaks.isEnabled()) setForeground(Color.red); else if (breaks.isEnabled()) setForeground(Color.black); else setForeground(Color.lightGray);
            return this;
        }
    }
}
