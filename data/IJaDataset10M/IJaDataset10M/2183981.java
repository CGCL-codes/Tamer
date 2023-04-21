package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import soct2.TSWorkspace;

public class UndoInternalFrame extends JInternalFrame implements ActionListener {

    JButton add = new JButton("add");

    JButton apply = new JButton("apply");

    Vector sids = new Vector();

    JList sites = new JList(sids);

    JComboBox site = new JComboBox();

    TSWorkspace workspace;

    Editor editor;

    UndoInternalFrame(Editor editor) {
        super("UndoManager: " + editor.getWorkspace().getWorkspaceName(), true, true, true, true);
        this.editor = editor;
        this.setSize(new Dimension(300, 300));
        workspace = editor.getWorkspace();
        JPanel siteSelection = new JPanel();
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BorderLayout());
        apply.addActionListener(this);
        timePanel.add(apply, BorderLayout.SOUTH);
        siteSelection.setLayout(new BorderLayout());
        JPanel jpanel = new JPanel();
        jpanel.setLayout(new GridLayout(1, 2));
        this.site.addActionListener(this);
        this.setLayout(new GridLayout(1, 2));
        this.add.addActionListener(this);
        fillSiteComboBox();
        jpanel.add(this.site);
        jpanel.add(this.add);
        siteSelection.add(sites, BorderLayout.CENTER);
        siteSelection.add(jpanel, BorderLayout.SOUTH);
        this.add(siteSelection);
        this.add(timePanel);
        editor.setEnabled(false);
        this.setVisible(true);
    }

    @Override
    protected void finalize() throws Throwable {
        editor.setEnabled(true);
        super.finalize();
    }

    private void fillSiteComboBox() {
        site.removeAllItems();
        site.addItem("All sites");
        System.out.println(String.valueOf(workspace.getVS().getSites()));
        for (Object id : workspace.getVS().getSites()) {
            site.addItem(String.valueOf(id));
            System.out.println(String.valueOf(id));
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.add)) {
            if (site.getSelectedItem().equals("All sites")) {
                sids.removeAllElements();
                for (Object id : workspace.getVS().getSites()) {
                    sids.add(String.valueOf(id));
                }
            } else {
                if (!sids.contains(site.getSelectedItem())) sids.add(site.getSelectedItem());
            }
            sites.updateUI();
        } else if (e.getSource().equals(this.apply)) {
            System.out.println("Annulation de 0 à " + workspace.getLog().getSize() + " des sites " + sids.toString());
            workspace.getLog().undo(0, workspace.getLog().getSize(), sids);
        }
    }

    @Override
    public void dispose() {
        editor.setEnabled(true);
        super.dispose();
    }
}
