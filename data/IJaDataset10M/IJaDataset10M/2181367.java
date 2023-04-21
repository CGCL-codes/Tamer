package diet.server.experimentmanager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/**
 *
 * @author user
 */
public class MenuItem extends JMenuItem implements ActionListener {

    FileTreeNode dftn;

    FileSystemModelDefaultTreeModel fsmdtm;

    public MenuItem(String name, FileSystemModelDefaultTreeModel fsmdtm, FileTreeNode dftn) {
        super(name);
        this.dftn = dftn;
        this.fsmdtm = fsmdtm;
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        fsmdtm.performActionFromMenu(e.getActionCommand(), dftn);
    }
}
