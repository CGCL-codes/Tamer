package fbench.graph.popup;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.CENTER;
import static javax.swing.GroupLayout.Alignment.LEADING;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import fbench.tree.DOMTree;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Dialog for adding new Algorithms.
 * 
 * @author DG
 * @version 20070524/DG
 *
 */
public class AlgorithmDialog extends DialogModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4672846716575732683L;

    class AddAlgorithmButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            String newName = nameField.getText().toLowerCase();
            if (!checkName()) return;
            algElement = document.createElement("Algorithm");
            algElement.setAttribute("Name", newName);
            if (commentField.getText().length() > 0) algElement.setAttribute("Comment", commentField.getText());
            String language = (String) algTypeComboBox.getSelectedItem();
            Element childElement = null;
            if (language.equals("Function Block Diagram")) {
                childElement = document.createElement("FBD");
            } else if (language.equals("Structured Text")) {
                childElement = document.createElement("ST");
            } else if (language.equals("Ladder Logic Diagram")) {
                childElement = document.createElement("LD");
            } else {
                childElement = document.createElement("Other");
                childElement.setAttribute("Language", language);
            }
            algElement.appendChild(childElement);
            childElement.setAttribute("Text", "");
            Node fbElement = document.getElementsByTagName("BasicFB").item(0);
            if (fbElement != null) {
                fbElement.appendChild(algElement);
            }
            DOMTree tree = (DOMTree) me.getMouseEvent().getComponent();
            JScrollPane sp = (JScrollPane) tree.getParent().getParent();
            JViewport vp = new JViewport();
            DOMTree nTree = new DOMTree(document);
            vp.setView(nTree);
            sp.setViewport(vp);
            dispose();
        }
    }

    private JLabel nameLabel = new JLabel("Name :");

    private JLabel typeLabel = new JLabel("Type :");

    private JLabel commentLabel = new JLabel("Comment :");

    private JTextField nameField = new JTextField();

    private JTextField commentField = new JTextField();

    private String[] algTypeList = { "Function Block Diagram", "Java", "C", "Ladder Logic Diagram", "Structured Text", "Other" };

    private JComboBox algTypeComboBox = new JComboBox(algTypeList);

    private JButton acceptButton = new JButton("Add");

    private JButton cancelButton = new JButton("Cancel");

    private JButton helpButton = new JButton("Help");

    private Element algElement;

    public AlgorithmDialog(Element element, MouseEvent mouseEvt) {
        super(element, mouseEvt);
    }

    @Override
    public void create(String command) {
        if (command.equals("Add Algorithm")) {
            createDialogForAdd();
        }
    }

    private void createDialogForAdd() {
        setTitle("Add Algorithm");
        setDialogSize(350, 140);
        acceptButton.addActionListener(new AddAlgorithmButtonListener());
        cancelButton.addActionListener(new CancelButtonActionListener());
        helpButton.addActionListener(null);
        setLayoutForAlgorithm();
        setVisible(true);
    }

    private void setLayoutForAlgorithm() {
        layout.setHorizontalGroup(layout.createParallelGroup(CENTER).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(LEADING).addComponent(nameLabel)).addGroup(layout.createParallelGroup(LEADING).addComponent(nameField).addGap(10))).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(LEADING).addComponent(commentLabel)).addGroup(layout.createParallelGroup(LEADING).addComponent(commentField).addGap(10))).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(LEADING).addComponent(typeLabel)).addGroup(layout.createParallelGroup(LEADING).addComponent(algTypeComboBox).addGap(10))).addGroup(layout.createSequentialGroup().addGap(10)).addGroup(layout.createSequentialGroup().addComponent(acceptButton).addComponent(cancelButton).addComponent(helpButton)));
        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(BASELINE).addComponent(nameLabel).addComponent(nameField)).addGroup(layout.createParallelGroup(BASELINE)).addGroup(layout.createParallelGroup(BASELINE).addComponent(typeLabel).addComponent(algTypeComboBox)).addGroup(layout.createParallelGroup(BASELINE)).addGroup(layout.createParallelGroup(BASELINE).addComponent(commentLabel).addComponent(commentField)).addGroup(layout.createParallelGroup(BASELINE)).addGroup(layout.createParallelGroup(BASELINE).addComponent(acceptButton).addComponent(cancelButton).addComponent(helpButton)));
    }

    private boolean checkName() {
        String newName = nameField.getText();
        if (newName.replaceAll(" ", "").length() == 0) {
            JOptionPane.showMessageDialog(me, "Name field is empty", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (newName.replaceAll(" ", "").length() == 0) {
            JOptionPane.showMessageDialog(me, "Name field is empty", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String[] list = { "Algorithm" };
        for (String listname : list) {
            if (doesListContainName(listname, newName)) return false;
        }
        return true;
    }

    private boolean doesListContainName(String listName, String newName) {
        NodeList list = null;
        try {
            list = document.getElementsByTagName(listName);
        } catch (NullPointerException e) {
            return false;
        }
        for (int i = 0; i < list.getLength(); i++) {
            if (((Element) list.item(i)).getAttribute("Name").equalsIgnoreCase(newName)) {
                JOptionPane.showMessageDialog(me, "There is an Algorithm with the same name", "Event Warning", JOptionPane.WARNING_MESSAGE);
                return true;
            }
        }
        return false;
    }
}
