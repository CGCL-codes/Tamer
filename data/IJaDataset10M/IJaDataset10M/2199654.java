package savior.gui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import savior.Profile;

@SuppressWarnings("serial")
public class ProfileList extends JComponent implements ActionListener {

    private Viewer parentViewer;

    private JPanel profiles;

    public ProfileList(Viewer _v) {
        JLabel tmplbl;
        JSeparator tmpsep;
        JButton tmpbut;
        int y = 0;
        int width = 400;
        parentViewer = _v;
        JLabel title = new JLabel("Profiles List");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16));
        title.setBounds(1, y, width, 20);
        y += 30;
        add(title);
        tmpsep = new JSeparator();
        tmpsep.setBounds(1, y, width, 5);
        y += 5;
        add(tmpsep);
        tmplbl = new JLabel("New profile:");
        tmplbl.setFont(tmplbl.getFont().deriveFont(Font.BOLD, 14));
        tmplbl.setBounds(1, y, width, 15);
        y += 20;
        add(tmplbl);
        tmpbut = new JButton("Create a new profile");
        tmpbut.addActionListener(this);
        tmpbut.setActionCommand("action|newprofile");
        tmpbut.setBounds(0, y, 150, 20);
        y += 25;
        add(tmpbut);
        tmpsep = new JSeparator();
        tmpsep.setBounds(1, y, width, 5);
        y += 5;
        add(tmpsep);
        tmplbl = new JLabel("Open profile:");
        tmplbl.setFont(tmplbl.getFont().deriveFont(Font.BOLD, 14));
        tmplbl.setBounds(1, y, width, 15);
        y += 20;
        add(tmplbl);
        profiles = new JPanel(new GridLayout(0, 2));
        JScrollPane tmpscr = new JScrollPane(profiles);
        tmpscr.setBounds(1, y, width - 15, 300);
        y += 305;
        add(tmpscr);
        RefreshList();
        parentViewer.AnimateChangeComponent(this, width, y + 50);
    }

    public void RefreshList() {
        String[] list = Profile.ListProfiles();
        profiles.removeAll();
        for (int i = 0; i < list.length; i++) {
            JButton tmpbut;
            tmpbut = new JButton(list[i]);
            tmpbut.setActionCommand("execute|" + list[i]);
            tmpbut.addActionListener(this);
            profiles.add(tmpbut);
            tmpbut = new JButton("Delete");
            tmpbut.setActionCommand("delete|" + list[i]);
            tmpbut.addActionListener(this);
            profiles.add(tmpbut);
        }
        profiles.validate();
        if (profiles.getComponentCount() == 0) parentViewer.ToStart();
    }

    public void actionPerformed(ActionEvent arg0) {
        String[] command = arg0.getActionCommand().split("\\|");
        if (command[0].compareTo("action") == 0) {
            if (command[1].compareTo("newprofile") == 0) {
                new NewProject(parentViewer);
            }
        }
        if (command[0].compareTo("delete") == 0) {
            Profile.DeleteProfile(command[1]);
            RefreshList();
        }
        if (command[0].compareTo("execute") == 0) {
            String profile = Profile.LoadProfile(command[1]);
            new Backup(profile, parentViewer);
        }
    }
}
