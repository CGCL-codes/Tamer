package nk.visual;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xml.XMLTree;
import xml.exc.NodeNotFoundException;
import nk.domain.Session;
import nk.visual.bricks.SpringUtilities;
import nk.visual.style.JImagePanel;

/**
 * @author <a href="mailto:nad7ir@yahoo.com">Alin NISTOR</a>
 */
public class JConfigFrame extends JFrame {

    private static final long serialVersionUID = 8561730968845213420L;

    protected static final String TYPE = "type";

    protected static final String NAME = "name";

    protected XMLTree xml;

    protected JMenuBar jmenu;

    protected JPanel mainPanel;

    protected JTabbedPane jtabs;

    protected Map<String, List<JComponent>> jmcomps;

    public JConfigFrame() {
        super("Never Know Config Area");
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
        mainPanel = new JImagePanel(Session.JTEXTFRAME_BG, new BorderLayout());
        setContentPane(mainPanel);
        setIconImage(Session.GREEN_CLOVER_ICON);
        setLocationRelativeTo(null);
        jtabs = new JTabbedPane();
        jtabs.setOpaque(false);
        add(jtabs, BorderLayout.CENTER);
        getJMenu();
        setJMenuBar(jmenu);
        setSize(250, 350);
    }

    public JConfigFrame(XMLTree xml) {
        this();
        this.xml = xml;
        try {
            initXmlTreeValues();
        } catch (NodeNotFoundException e) {
            throw new RuntimeException(e);
        }
        setVisible(true);
    }

    private void initXmlTreeValues() throws NodeNotFoundException {
        jmcomps = new HashMap<String, List<JComponent>>();
        Node root = xml.getRoot();
        if (root == null) throw new NodeNotFoundException();
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node nd = nl.item(i);
            if (nd.getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) nd;
                String firstLvName = el.getTagName();
                JPanel jptab = new JPanel(new SpringLayout());
                JScrollPane jsp = new JScrollPane(jptab);
                jtabs.addTab(firstLvName, jsp);
                List<JComponent> list = new ArrayList<JComponent>();
                jmcomps.put(firstLvName, list);
                NodeList nls = el.getChildNodes();
                int count = 0;
                for (int j = 0; j < nls.getLength(); j++) {
                    Node node = nls.item(j);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        count++;
                        Element elem = (Element) node;
                        String nodeName = elem.getTagName();
                        String type = elem.getAttribute(TYPE);
                        String value = elem.getFirstChild().getNodeValue();
                        JComponent jc = createJComponent(type, nodeName, value);
                        list.add(jc);
                        JLabel l = new JLabel(nodeName, JLabel.TRAILING);
                        jptab.add(l);
                        l.setLabelFor(jc);
                        jptab.add(jc);
                    }
                }
                SpringUtilities.makeCompactGrid(jptab, count, 2, 6, 6, 6, 6);
            }
        }
    }

    private JComponent createJComponent(String type, String nodeName, String value) {
        JComponent jc = null;
        if (XMLTree.BOOLEAN_TYPE.equals(type)) {
            jc = new JCheckBox(nodeName, Boolean.parseBoolean(value));
        } else if (XMLTree.INTEGER_TYPE.equals(type)) {
            jc = new JTextField(value);
        } else if (XMLTree.LONG_TYPE.equals(type)) {
            jc = new JTextField(value);
        } else if (XMLTree.STRING_TYPE.equals(type)) {
            jc = new JTextField(value);
        } else {
            jc = new JTextField(value);
        }
        if (jc != null) {
            jc.putClientProperty(TYPE, type);
            jc.putClientProperty(NAME, nodeName);
        }
        return jc;
    }

    private void saveXmlTreeValues() throws NodeNotFoundException {
        Iterator<Map.Entry<String, List<JComponent>>> it = jmcomps.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<JComponent>> pairs = it.next();
            String firstLvName = pairs.getKey();
            for (JComponent jc : pairs.getValue()) {
                String type = (String) jc.getClientProperty(TYPE);
                String name = (String) jc.getClientProperty(NAME);
                String value = null;
                if (jc instanceof JCheckBox) {
                    value = "" + ((JCheckBox) jc).isSelected();
                } else if (jc instanceof JTextField) {
                    value = ((JTextField) jc).getText();
                }
                Node node = xml.getSecondLevelNode(firstLvName, name);
                checkFieldValidity(type, value);
                node.setNodeValue(value);
            }
        }
        try {
            xml.persist();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkFieldValidity(String type, String value) {
        if (XMLTree.BOOLEAN_TYPE.equals(type)) {
            Boolean.parseBoolean(value);
        } else if (XMLTree.INTEGER_TYPE.equals(type)) {
            Integer.parseInt(value);
        } else if (XMLTree.LONG_TYPE.equals(type)) {
            Long.parseLong(value);
        }
    }

    private JMenuBar getJMenu() {
        if (jmenu == null) {
            jmenu = new JMenuBar();
            JMenu jmn1 = new JMenu("NK");
            JMenuItem jmi2 = new JMenuItem("Save");
            jmi2.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        saveXmlTreeValues();
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(JConfigFrame.this, e1);
                    }
                }
            });
            jmn1.add(jmi2);
            JMenuItem jmi3 = new JMenuItem("Close");
            jmi3.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            jmn1.add(jmi3);
            jmenu.add(jmn1);
        }
        return jmenu;
    }
}
