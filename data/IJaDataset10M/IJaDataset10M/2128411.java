package ca.uhn.hl7v2.view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.PipeParser;

/**
 * This is a Swing panel that displays the contents of a Message object in a JTree.
 * The tree currently only expands to the field level (components shown as one node).
 * @author Bryan Tripp (bryan_tripp@sourceforge.net)
 * @deprecated
 */
@SuppressWarnings("serial")
public class TreePanel extends javax.swing.JPanel {

    private static final Logger log = LoggerFactory.getLogger(TreePanel.class);

    private EncodingCharacters encChars;

    private Message message;

    /** Creates new TreePanel */
    public TreePanel() {
        this.encChars = new EncodingCharacters('|', null);
    }

    /**
     * Updates the panel with a new Message.
     */
    public void setMessage(Message message) {
        this.message = message;
        if (message == null) {
            this.removeAll();
            this.revalidate();
            return;
        }
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(message.getClass().getName());
        addChildren(message, top);
        JTree tree = new JTree(top);
        this.removeAll();
        this.add(tree);
        this.revalidate();
    }

    /**
     * Returns the message that is currently displayed in the tree panel.
     */
    public Message getMessage() {
        return this.message;
    }

    /**
     * Adds the children of the given group under the given tree node.
     */
    private void addChildren(Group messParent, MutableTreeNode treeParent) {
        String[] childNames = messParent.getNames();
        int currChild = 0;
        for (int i = 0; i < childNames.length; i++) {
            try {
                Structure[] childReps = messParent.getAll(childNames[i]);
                for (int j = 0; j < childReps.length; j++) {
                    DefaultMutableTreeNode newNode = null;
                    if (childReps[j] instanceof Group) {
                        String groupName = childReps[j].getClass().getName();
                        groupName = groupName.substring(groupName.lastIndexOf('.') + 1, groupName.length());
                        newNode = new DefaultMutableTreeNode(groupName + " (rep " + j + ")");
                        addChildren((Group) childReps[j], newNode);
                    } else if (childReps[j] instanceof Segment) {
                        newNode = new DefaultMutableTreeNode(PipeParser.encode((Segment) childReps[j], encChars));
                        addChildren((Segment) childReps[j], newNode);
                    }
                    treeParent.insert(newNode, currChild++);
                }
            } catch (HL7Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Add fields of a segment to the tree ...
     */
    private void addChildren(Segment messParent, MutableTreeNode treeParent) {
        int n = messParent.numFields();
        int currChild = 0;
        for (int i = 1; i <= n; i++) {
            try {
                Type[] reps = messParent.getField(i);
                for (int j = 0; j < reps.length; j++) {
                    String field = PipeParser.encode(reps[j], encChars);
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("Field " + i + " rep " + j + " (" + getLabel(reps[j]) + "): " + field);
                    addChildren(reps[j], newNode);
                    treeParent.insert(newNode, currChild++);
                }
            } catch (HL7Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds children to the tree.  If the Type is a Varies, the Varies data are 
     * added under a new node called "Varies".  If there are extra components, 
     * these are added under a new node called "ExtraComponents"
     */
    private void addChildren(Type messParent, MutableTreeNode treeParent) {
        if (Varies.class.isAssignableFrom(messParent.getClass())) {
            Type data = ((Varies) messParent).getData();
            DefaultMutableTreeNode dataNode = new DefaultMutableTreeNode(getLabel(data));
            treeParent.insert(dataNode, 0);
            addChildren(data, dataNode);
        } else {
            if (Composite.class.isAssignableFrom(messParent.getClass())) {
                addChildren((Composite) messParent, treeParent);
            } else if (Primitive.class.isAssignableFrom(messParent.getClass())) {
                addChildren((Primitive) messParent, treeParent);
            }
            if (messParent.getExtraComponents().numComponents() > 0) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("ExtraComponents");
                treeParent.insert(newNode, treeParent.getChildCount());
                for (int i = 0; i < messParent.getExtraComponents().numComponents(); i++) {
                    DefaultMutableTreeNode variesNode = new DefaultMutableTreeNode("Varies");
                    newNode.insert(variesNode, i);
                    addChildren(messParent.getExtraComponents().getComponent(i), variesNode);
                }
            }
        }
    }

    /**
     * Adds components of a composite to the tree ...
     */
    private void addChildren(Composite messParent, MutableTreeNode treeParent) {
        Type[] components = messParent.getComponents();
        for (int i = 0; i < components.length; i++) {
            DefaultMutableTreeNode newNode;
            newNode = new DefaultMutableTreeNode(getLabel(components[i]));
            addChildren(components[i], newNode);
            treeParent.insert(newNode, i);
        }
    }

    /**
     * Adds single text value to tree as a leaf
     */
    private void addChildren(Primitive messParent, MutableTreeNode treeParent) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(messParent.getValue());
        treeParent.insert(newNode, 0);
    }

    /**
     * Returns the unqualified class name as a label for tree nodes. 
     */
    private static String getLabel(Object o) {
        String name = o.getClass().getName();
        return name.substring(name.lastIndexOf('.') + 1, name.length());
    }

    /**
     * A convenience method for displaying a message by creating a new
     * TreePanel and displaying the given message in a new window.
     * Currently only works with v2.4 messages.
     */
    public static void showInNewWindow(Message message) {
        JFrame frame = new JFrame(message.getClass().getName());
        try {
            TreePanel panel = new TreePanel();
            panel.setMessage(message);
            JScrollPane scroll = new JScrollPane(panel);
            frame.getContentPane().add(scroll, BorderLayout.CENTER);
            frame.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            System.err.println("Can't display message in new window: ");
            e.printStackTrace();
        }
    }

    /**
     * Opens window and displays a message in a file (file named in command line arg).
     */
    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("Usage: TreePanel msg_file_name");
            System.exit(1);
        }
        try {
            PipeParser parser = new PipeParser();
            File messageFile = new File(args[0]);
            long fileLength = messageFile.length();
            FileReader r = new FileReader(messageFile);
            char[] cbuf = new char[(int) fileLength];
            System.out.println("Reading message file ... " + r.read(cbuf) + " of " + fileLength + " chars");
            r.close();
            String messString = String.valueOf(cbuf);
            Message mess = parser.parse(messString);
            System.out.println("Got message of type " + mess.getClass().getName());
            showInNewWindow(mess);
            System.out.println(parser.encode(mess, "VB"));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
    }
}
