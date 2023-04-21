package org.argouml.cognitive.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import org.argouml.kernel.Wizard;
import org.argouml.ui.SpacerPanel;

/** A simple non-modal wizard step that shows instructions and prompts
 *  the user to enter a string. 
 *
 * @see org.argouml.cognitive.critics.Critic
 * @see org.argouml.kernel.Wizard
 */
public class WizStepChoice extends WizStep {

    JTextArea _instructions = new JTextArea();

    ButtonGroup _group = new ButtonGroup();

    Vector _choices = new Vector();

    int _selectedIndex = -1;

    public WizStepChoice(Wizard w, String instr, Vector choices) {
        _choices = choices;
        _instructions.setText(instr);
        _instructions.setWrapStyleWord(true);
        _instructions.setEditable(false);
        _instructions.setBorder(null);
        _instructions.setBackground(_mainPanel.getBackground());
        _mainPanel.setBorder(new EtchedBorder());
        GridBagLayout gb = new GridBagLayout();
        _mainPanel.setLayout(gb);
        GridBagConstraints c = new GridBagConstraints();
        c.ipadx = 3;
        c.ipady = 3;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.EAST;
        JLabel image = new JLabel("");
        image.setIcon(WIZ_ICON);
        image.setBorder(null);
        c.gridx = 0;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;
        gb.setConstraints(image, c);
        _mainPanel.add(image);
        c.weightx = 1.0;
        c.gridx = 2;
        c.gridheight = 1;
        c.gridwidth = 3;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        gb.setConstraints(_instructions, c);
        _mainPanel.add(_instructions);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        SpacerPanel spacer = new SpacerPanel();
        gb.setConstraints(spacer, c);
        _mainPanel.add(spacer);
        c.gridx = 2;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 1;
        int size = choices.size();
        for (int i = 0; i < size; i++) {
            c.gridy = 2 + i;
            String s = (String) choices.elementAt(i);
            JRadioButton rb = new JRadioButton(s);
            _group.add(rb);
            rb.setActionCommand(s);
            rb.addActionListener(this);
            gb.setConstraints(rb, c);
            _mainPanel.add(rb);
        }
        c.gridx = 1;
        c.gridy = 3 + choices.size();
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        SpacerPanel spacer2 = new SpacerPanel();
        gb.setConstraints(spacer2, c);
        _mainPanel.add(spacer2);
    }

    public int getSelectedIndex() {
        return _selectedIndex;
    }

    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() instanceof JRadioButton) {
            String cmd = e.getActionCommand();
            if (cmd == null) {
                _selectedIndex = -1;
                return;
            }
            int size = _choices.size();
            for (int i = 0; i < size; i++) {
                String s = (String) _choices.elementAt(i);
                if (s.equals(cmd)) _selectedIndex = i;
            }
            getWizard().doAction();
            enableButtons();
        }
    }
}
