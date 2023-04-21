package ingenias.editor.editiondialog;

import ingenias.editor.GraphManager;
import ingenias.editor.entities.Entity;
import ingenias.editor.widget.DnDJTree;
import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

public class GeneralEditionFrame extends javax.swing.JDialog implements java.io.Serializable {

    private JScrollPane mainscroll = new JScrollPane();

    public static final int CANCELLED = 0;

    public static final int ACCEPTED = 1;

    public static final int PROGRESSING = 2;

    private int status = PROGRESSING;

    public GeneralEditionFrame(ingenias.editor.Editor editor, ingenias.editor.ObjectManager om, GraphManager gm, final Frame dialogOwner, String title, final Entity ent) {
        super(dialogOwner, title, true);
        final JPanel main = new JPanel();
        BorderLayout bl = new BorderLayout();
        main.setLayout(bl);
        this.getContentPane().add(main);
        main.addContainerListener(new java.awt.event.ContainerAdapter() {

            public void componentAdded(java.awt.event.ContainerEvent ce) {
                pack();
            }
        });
        final JDialog self = this;
        final GeneralEditionPanel gep = new GeneralEditionPanel(editor, dialogOwner, om, gm, ent);
        main.add(mainscroll, BorderLayout.CENTER);
        mainscroll.getViewport().add(gep, null);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent ae) {
                gep.undo();
                self.setVisible(false);
                status = CANCELLED;
            }
        });
        JButton accept = new JButton("Accept");
        accept.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent ae) {
                gep.confirmActions();
                System.out.println("Changes applied " + ent);
                self.setVisible(false);
                status = ACCEPTED;
            }
        });
        JPanel southButtons = new JPanel();
        southButtons.add(accept);
        southButtons.add(cancel);
        main.add(southButtons, BorderLayout.SOUTH);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (dialogOwner != null) dialogOwner.requestFocusInWindow();
            }
        });
        self.requestFocusInWindow();
        setLocation(dialogOwner.getLocation().x + getSize().width / 2, dialogOwner.getLocation().y + getSize().height / 2);
    }

    public void pack() {
        super.pack();
        final JDialog jd = this;
        this.setSize(this.getSize().width + 30, 300);
    }

    public static void main(String args[]) {
        System.err.println("hola");
        ingenias.editor.entities.INGENIASCodeComponent a = new ingenias.editor.entities.INGENIASCodeComponent("mio");
        a.setCode("hola que tal");
        GraphManager.initInstance(new DefaultMutableTreeNode(), new DnDJTree());
        GeneralEditionFrame ge = new GeneralEditionFrame(null, ingenias.editor.ObjectManager.initialise(new DefaultMutableTreeNode(), new JTree()), ingenias.editor.GraphManager.initInstance(new DefaultMutableTreeNode(), new DnDJTree()), null, "hola", a);
        ge.setVisible(true);
        System.out.println(a.getDescription());
    }

    public int getStatus() {
        return status;
    }
}
