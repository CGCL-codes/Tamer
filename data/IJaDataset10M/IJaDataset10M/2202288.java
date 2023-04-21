package pl.wat.wcy.sna.gui;

import java.io.File;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author  ruffus
 */
public class SNANewEdge extends javax.swing.JFrame {

    public static final long serialVersionUID = 1;

    private File f;

    /** Creates new form SNANewNode */
    public SNANewEdge(File fA) {
        initComponents();
        this.f = fA;
        FillComponent();
    }

    private void initComponents() {
        jButtonCANCEL = new javax.swing.JButton();
        jButtonOK = new javax.swing.JButton();
        jLabelSource = new javax.swing.JLabel();
        jComboBoxSource = new javax.swing.JComboBox();
        jLabelTarget = new javax.swing.JLabel();
        jComboBoxTarget = new javax.swing.JComboBox();
        jLabelLength = new javax.swing.JLabel();
        jLabelWeight = new javax.swing.JLabel();
        jCBWeight = new javax.swing.JComboBox();
        jCBLegth = new javax.swing.JComboBox();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Edge");
        setAlwaysOnTop(true);
        setLocationByPlatform(true);
        setModalExclusionType(getModalExclusionType());
        setName("framenode");
        setResizable(false);
        jButtonCANCEL.setText("Anuluj");
        jButtonCANCEL.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonCANCELMouseClicked(evt);
            }
        });
        jButtonOK.setText("Dodaj");
        jButtonOK.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonOKMouseClicked(evt);
            }
        });
        jLabelSource.setText("Source");
        jLabelTarget.setText("Target");
        jLabelLength.setText("Length");
        jLabelWeight.setText("Weight");
        jCBWeight.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        jCBLegth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jButtonOK).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButtonCANCEL)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabelLength).addComponent(jLabelTarget).addComponent(jLabelSource).addComponent(jLabelWeight)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jCBLegth, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jCBWeight, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jComboBoxTarget, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jComboBoxSource, 0, 190, Short.MAX_VALUE)))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(14, 14, 14).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBoxSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabelSource)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBoxTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabelTarget)).addGap(6, 6, 6).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCBWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabelWeight)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabelLength).addComponent(jCBLegth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButtonCANCEL).addComponent(jButtonOK)).addGap(17, 17, 17)));
        pack();
    }

    private void jButtonCANCELMouseClicked(java.awt.event.MouseEvent evt) {
        this.dispose();
    }

    private void jButtonOKMouseClicked(java.awt.event.MouseEvent evt) {
        if (SprawdzDroge() && (String) jComboBoxSource.getSelectedItem() != (String) jComboBoxTarget.getSelectedItem()) {
            try {
                DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
                DocumentBuilder build = fact.newDocumentBuilder();
                org.w3c.dom.Document doc = build.parse(f);
                org.w3c.dom.Element el = doc.createElement("edge");
                el.setAttribute("source", (String) jComboBoxSource.getSelectedItem());
                el.setAttribute("target", (String) jComboBoxTarget.getSelectedItem());
                Element eleW = doc.createElement("data");
                eleW.setAttribute("key", "weight");
                eleW.setTextContent((String) jCBWeight.getSelectedItem());
                el.appendChild(eleW);
                Element eleL = doc.createElement("data");
                eleL.setAttribute("key", "lenght");
                eleL.setTextContent((String) jCBLegth.getSelectedItem());
                el.appendChild(eleL);
                NodeList nl = doc.getElementsByTagName("graph");
                Node no = nl.item(0);
                no.appendChild(el);
                DOMSource sourc = new DOMSource(doc);
                StreamResult res = new StreamResult(f);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer trans = tf.newTransformer();
                trans.transform(sourc, res);
            } catch (Exception ex) {
            }
            this.dispose();
        } else JOptionPane.showMessageDialog(this, "�le wytyczona kraw�d� !");
    }

    private boolean SprawdzDroge() {
        return true;
    }

    private void FillComponent() {
        try {
            DefaultComboBoxModel comModelTarget = new DefaultComboBoxModel();
            DefaultComboBoxModel comModelSource = new DefaultComboBoxModel();
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            DocumentBuilder build = fact.newDocumentBuilder();
            org.w3c.dom.Document doc = build.parse(f);
            NodeList child = doc.getElementsByTagName("graph").item(0).getChildNodes();
            for (int i = 0; i < child.getLength(); i++) {
                if (child.item(i) instanceof org.w3c.dom.Element && child.item(i).getNodeName() == "node") {
                    org.w3c.dom.Element col = (org.w3c.dom.Element) child.item(i);
                    comModelTarget.addElement(col.getAttribute("id"));
                    comModelSource.addElement(col.getAttribute("id"));
                }
            }
            jComboBoxSource.setModel(comModelSource);
            jComboBoxTarget.setModel(comModelTarget);
        } catch (Exception ex) {
        }
    }

    private javax.swing.JButton jButtonCANCEL;

    private javax.swing.JButton jButtonOK;

    private javax.swing.JComboBox jCBLegth;

    private javax.swing.JComboBox jCBWeight;

    private javax.swing.JComboBox jComboBoxSource;

    private javax.swing.JComboBox jComboBoxTarget;

    private javax.swing.JLabel jLabelLength;

    private javax.swing.JLabel jLabelSource;

    private javax.swing.JLabel jLabelTarget;

    private javax.swing.JLabel jLabelWeight;
}
