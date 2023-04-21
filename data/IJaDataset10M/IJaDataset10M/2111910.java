package net.sf.dz.util.wizard;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A helper class that provides a panel with subpanels and arbitrary number
 * of buttons in the middle.
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 2004
 * @version $Id: DoublePanel.java,v 1.4 2007-03-01 07:08:10 vtt Exp $
 */
public class DoublePanel {

    protected final JPanel parent;

    protected final JPanel buttonPanel = new JPanel();

    public final JPanel leftPanel = new RigidPanel();

    public final JPanel rightPanel = new RigidPanel();

    protected List<JButton> buttonList = new LinkedList<JButton>();

    protected Map<String, JButton> buttonMap = new TreeMap<String, JButton>();

    public DoublePanel(JPanel parent, WizardPage listener, List<String> buttonNames) {
        this.parent = parent;
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints cs = new GridBagConstraints();
        parent.setLayout(layout);
        cs.gridx = 0;
        cs.gridy = 0;
        cs.fill = GridBagConstraints.BOTH;
        cs.gridwidth = 1;
        cs.gridheight = 1;
        cs.weightx = 1;
        cs.weighty = 1;
        layout.setConstraints(leftPanel, cs);
        parent.add(leftPanel);
        cs.gridx++;
        cs.weightx = 0;
        cs.fill = GridBagConstraints.VERTICAL;
        layout.setConstraints(buttonPanel, cs);
        parent.add(buttonPanel);
        createButtons(buttonNames, listener);
        cs.gridx++;
        cs.fill = GridBagConstraints.BOTH;
        cs.weightx = 1;
        layout.setConstraints(rightPanel, cs);
        parent.add(rightPanel);
    }

    private void createButtons(List<String> buttonNames, WizardPage listener) {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints cs = new GridBagConstraints();
        buttonPanel.setLayout(layout);
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        cs.gridheight = 1;
        cs.weightx = 0;
        cs.weighty = 1;
        cs.fill = GridBagConstraints.BOTH;
        JPanel fillerTop = new JPanel();
        layout.setConstraints(fillerTop, cs);
        buttonPanel.add(fillerTop);
        cs.gridy++;
        cs.weightx = 0;
        cs.fill = GridBagConstraints.HORIZONTAL;
        for (Iterator<String> i = buttonNames.iterator(); i.hasNext(); ) {
            String name = i.next();
            if ("<separator>".equals(name)) {
                JLabel filler = new JLabel();
                cs.weighty = 1;
                layout.setConstraints(filler, cs);
                buttonPanel.add(filler);
            } else {
                JButton b = new JButton(name);
                cs.weighty = 0;
                layout.setConstraints(b, cs);
                buttonPanel.add(b);
                b.addActionListener(listener);
                buttonMap.put(name, b);
                buttonList.add(b);
            }
            cs.gridy++;
        }
        cs.gridy++;
        cs.fill = GridBagConstraints.BOTH;
        cs.weightx = 1;
        cs.weighty = 1;
        JPanel fillerBottom = new JPanel();
        layout.setConstraints(fillerBottom, cs);
        buttonPanel.add(fillerBottom);
    }

    public JButton getButton(String name) {
        return (JButton) buttonMap.get(name);
    }

    public JButton getButton(int offset) {
        return (JButton) buttonList.get(offset);
    }

    public final void add(JPanel parent, JComponent child) {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints cs = new GridBagConstraints();
        parent.setLayout(layout);
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        cs.gridheight = 1;
        cs.weightx = 1;
        cs.weighty = 1;
        cs.fill = GridBagConstraints.BOTH;
        layout.setConstraints(child, cs);
        parent.add(child);
    }

    protected class RigidPanel extends JPanel {

        private boolean inside = false;

        public synchronized Dimension getPreferredSize() {
            Container parent = getParent();
            Dimension self = super.getPreferredSize();
            if (inside) {
                return self;
            } else {
                inside = true;
                if (parent != null) {
                    Dimension parentDimension = parent.getPreferredSize();
                    Dimension buttonDimension = buttonPanel.getPreferredSize();
                    self.width = (parentDimension.width - buttonDimension.width) / 2;
                }
                inside = false;
            }
            return self;
        }
    }
}
