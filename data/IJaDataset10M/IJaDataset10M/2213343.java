package test;

import view.jTree.*;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import operating.Manager;

/**
 *
 * @author William Correa - spm.projecting
 */
public class SpmJTree extends JFrame {

    private static final long serialVersionUID = 22;

    /**
     * 
     */
    public SpmJTree() {
        Manager.instanceAll();
        Manager.openProject("");
        IconNode[] nodes = Manager.getProjectIconNodes();
        DefaultTreeModel model = new DefaultTreeModel(nodes[0]);
        JTree tree = new JTree(model);
        tree.putClientProperty("JTree.icons", makeIcons());
        tree.setCellRenderer(new IconNodeRenderer());
        JScrollPane sp = new JScrollPane(tree);
        getContentPane().add(sp, BorderLayout.CENTER);
    }

    /**
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    private Hashtable makeIcons() {
        Hashtable icons = new Hashtable();
        icons.put("projeto", new ImageIcon(getClass().getResource("/view/resources/tree/projeto.png")));
        icons.put("process", new ImageIcon(getClass().getResource("/view/resources/tree/process.png")));
        icons.put("phase", new ImageIcon(getClass().getResource("/view/resources/tree/phase.png")));
        icons.put("phases", new ImageIcon(getClass().getResource("/view/resources/tree/processpackage.png")));
        icons.put("iterations", new ImageIcon(getClass().getResource("/view/resources/tree/iteration.png")));
        return icons;
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        SpmJTree frame = new SpmJTree();
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.setSize(300, 150);
        frame.setVisible(true);
    }
}
