package issrg.utils;

import issrg.editor2.ReadablePERMISXML;
import issrg.editor2.XMLEditorInstantiable;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author Christian Azzopardi
 */
public class ACPolicyViewer extends ACViewer implements ActionListener {

    ReadablePERMISXML viewXML;

    boolean policyExists;

    String xmlpolicy = "";

    TreeSelectionEvent tse = null;

    /** Creates a new instance of ACPolicyViewer */
    public ACPolicyViewer() {
    }

    protected void init(int splitOrientation) {
        tree.addTreeSelectionListener(this);
        tree.setRootVisible(false);
        setLayout(new BorderLayout());
        viewXML = new ReadablePERMISXML(null, "", "text/html");
        JPanel tr = new JPanel(new BorderLayout());
        JPanel fv = new JPanel(new BorderLayout());
        tr.setPreferredSize(new Dimension(200, 150));
        fv.setPreferredSize(new Dimension(200, 150));
        tr.add(new JLabel("X.509 ACs:"), BorderLayout.NORTH);
        tr.add(scp, BorderLayout.CENTER);
        JComboBox combo = new JComboBox();
        combo.addItem("Readable Policy");
        combo.addItem("XML");
        combo.addActionListener(this);
        JPanel temp = new JPanel(new FlowLayout());
        temp.add(new JLabel("Policy:"));
        temp.add(combo);
        fv.add(temp, BorderLayout.NORTH);
        fv.add(viewXML, BorderLayout.CENTER);
        JSplitPane sp = new JSplitPane(splitOrientation, tr, fv);
        sp.setResizeWeight(0.5);
        add(sp, BorderLayout.CENTER);
    }

    public void valueChanged(TreeSelectionEvent e) {
        selectedACIndex = -1;
        this.tse = e;
        TreePath tp = e.getPath();
        if (tp != null) {
            FieldValue fv = (FieldValue) ((DefaultMutableTreeNode) tp.getLastPathComponent()).getUserObject();
            selectedACIndex = fv.getIndex();
            String tmp = fv.getValue();
            try {
                int index = tmp.indexOf("<X.509_PMI_RBAC_Policy");
                int lastIndex = tmp.indexOf("</X.509_PMI_RBAC_Policy>");
                if (index == -1) throw new Exception();
                String policy = fv.getValue().substring(index, lastIndex + 24);
                XMLEditorInstantiable xml = new XMLEditorInstantiable();
                viewXML.setXMLEditor(xml);
                xml.createDOM(policy);
                viewXML.repaint();
                viewXML.revalidate();
                setXML(policy);
                setPolicyExists(true);
            } catch (Exception ex) {
                viewXML.that.setText("No Policy To Display... Try the Attribute Node");
                setXML("");
                setPolicyExists(false);
            }
        }
    }

    public void setPolicy() {
    }

    public void setPolicyExists(boolean b) {
        policyExists = b;
    }

    public void setXML(String str) {
        xmlpolicy = str;
    }

    public String getXML() {
        return xmlpolicy;
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = ((JComboBox) e.getSource());
        if (cb.getSelectedItem().toString().intern().equals("XML")) {
            viewXML.that.setContentType("text/plain");
            viewXML.mimeType = "text/plain";
        } else if (cb.getSelectedItem().toString().intern().equals("Readable Policy")) {
            viewXML.that.setContentType("text/html");
            viewXML.mimeType = "text/html";
        }
        if (viewXML.getXMLEditor() != null) viewXML.getXML();
    }
}
