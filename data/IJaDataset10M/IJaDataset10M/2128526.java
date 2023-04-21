package projectbuilder.run;

import projectbuilder.build.BuildCommand;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import projectviewer.vpt.VPTProject;
import common.gui.ListPanel;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.Macros;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.View;

public class RunSettingsPanel extends JDialog implements ActionListener {

    private VPTProject proj;

    private ListPanel list;

    private JButton addBtn;

    private JButton removeBtn;

    private JButton modifyBtn;

    private JPanel optionsPanel;

    public RunSettingsPanel(View view, String title, VPTProject proj) {
        super(view, title);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        this.proj = proj;
        list = new ListPanel("Run commands:");
        list.setReorderable(true);
        ArrayList<String> commands = RunCommand.getCommandList(proj);
        if (commands != null) {
            for (int i = 0; i < commands.size(); i++) {
                list.addElement(commands.get(i));
            }
        }
        addBtn = new JButton(GUIUtilities.loadIcon("Plus.png"));
        removeBtn = new JButton(GUIUtilities.loadIcon("Minus.png"));
        modifyBtn = new JButton(GUIUtilities.loadIcon("ButtonProperties.png"));
        addBtn.addActionListener(this);
        removeBtn.addActionListener(this);
        modifyBtn.addActionListener(this);
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(modifyBtn);
        buttonPanel.add(Box.createHorizontalGlue());
        optionsPanel.add(buttonPanel);
        panel.add(BorderLayout.CENTER, list);
        panel.add(BorderLayout.SOUTH, optionsPanel);
        add(panel);
        KeyListener escape_listener = new KeyListener() {

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }

            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        };
        addKeyListener(escape_listener);
        list.addKeyListener(escape_listener);
        addBtn.addKeyListener(escape_listener);
        removeBtn.addKeyListener(escape_listener);
        modifyBtn.addKeyListener(escape_listener);
        optionsPanel.addKeyListener(escape_listener);
        pack();
        setLocationRelativeTo(view);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    /**
	 * Reads through the list and updates the project's run command property accordingly
	 */
    private void updateProps() {
        Object[] l = list.toArray();
        for (int i = 0; i < l.length; i++) {
            proj.setProperty("projectBuilder.command.run." + i, (String) l[i]);
        }
        for (int j = l.length; true; j++) {
            if (proj.getProperty("projectBuilder.command.run." + j) == null) break;
            proj.removeProperty("projectBuilder.command.run." + j);
        }
        jEdit.saveSettings();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == addBtn) {
            String cmd = GUIUtilities.input(jEdit.getActiveView(), "projectBuilder.msg.add-run-command", null);
            if (cmd == null || cmd.length() == 0) return;
            list.addElement(cmd);
            updateProps();
        } else if (source == removeBtn) {
            for (Object ob : list.getSelectedValues()) {
                list.removeElement(ob);
            }
            updateProps();
        } else if (source == modifyBtn) {
            String old = (String) list.getSelectedValues()[0];
            String cmd = GUIUtilities.input(jEdit.getActiveView(), "projectBuilder.msg.modify-run-command", old);
            if (cmd == null || cmd.length() == 0) return;
            list.removeElement(old);
            list.addElement(cmd);
            updateProps();
        }
    }
}
