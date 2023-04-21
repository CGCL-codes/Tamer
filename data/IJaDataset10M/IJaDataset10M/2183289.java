package com.k42b3.zubat.basic;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.k42b3.neodym.Http;
import com.k42b3.neodym.Message;
import com.k42b3.zubat.Zubat;
import com.k42b3.zubat.basic.form.CheckboxList;
import com.k42b3.zubat.basic.form.FormElementInterface;
import com.k42b3.zubat.basic.form.Input;
import com.k42b3.zubat.basic.form.Select;
import com.k42b3.zubat.basic.form.SelectItem;
import com.k42b3.zubat.basic.form.Textarea;

/**
 * FormPanel
 *
 * @author     Christoph Kappestein <k42b3.x@gmail.com>
 * @license    http://www.gnu.org/licenses/gpl.html GPLv3
 * @link       http://code.google.com/p/delta-quadrant
 * @version    $Revision: 225 $
 */
public class FormPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    protected String url;

    protected String requestMethod;

    protected String requestUrl;

    protected LinkedHashMap<String, FormElementInterface> requestFields = new LinkedHashMap<String, FormElementInterface>();

    protected Container body;

    protected JButton btnSend;

    protected SearchPanel searchPanel;

    protected HashMap<String, ReferenceItem> referenceFields = new HashMap<String, ReferenceItem>();

    protected Logger logger = Logger.getLogger("com.k42b3.zubat");

    public FormPanel(String url) throws Exception {
        this.url = url;
        this.setLayout(new BorderLayout());
        this.buildComponent();
    }

    protected void buildComponent() throws Exception {
        body = new JPanel();
        body.setLayout(new GridLayout(0, 1));
        this.request(url);
        this.add(new JScrollPane(body), BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        this.btnSend = new JButton("Send");
        this.btnSend.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    Document doc = sendRequest();
                    Element rootElement = (Element) doc.getDocumentElement();
                    Message msg = Message.parseMessage(rootElement);
                    if (msg.hasSuccess()) {
                        JOptionPane.showMessageDialog(null, msg.getText(), "Response", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        throw new Exception(msg.getText());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Response", JOptionPane.ERROR_MESSAGE);
                    Zubat.handleException(ex);
                }
            }
        });
        buttons.setLayout(new FlowLayout(FlowLayout.LEADING));
        buttons.add(this.btnSend);
        this.add(buttons, BorderLayout.SOUTH);
    }

    protected void request(String url) throws Exception {
        Document doc = Zubat.getHttp().requestXml(Http.GET, url);
        try {
            Element rootElement = (Element) doc.getDocumentElement();
            rootElement.normalize();
            Message msg = Message.parseMessage(rootElement);
            if (msg != null && !msg.hasSuccess()) {
                JPanel errorPanel = new JPanel();
                errorPanel.setLayout(new FlowLayout());
                errorPanel.add(new JLabel(msg.getText()));
                body.add(errorPanel);
            } else {
                body.add(this.parse(rootElement));
            }
        } catch (SAXException e) {
            JLabel error = new JLabel(e.getMessage());
            body.add(error);
        }
    }

    protected Document sendRequest() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        doc.setXmlStandalone(true);
        Set<String> keys = requestFields.keySet();
        Element root = doc.createElement("request");
        for (String key : keys) {
            Element e = doc.createElement(key);
            e.setTextContent(requestFields.get(key).getValue());
            root.appendChild(e);
        }
        doc.appendChild(root);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        String requestContent = result.getWriter().toString();
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-type", "application/xml");
        header.put("X-HTTP-Method-Override", requestMethod);
        return Zubat.getHttp().requestXml(Http.POST, requestUrl, header, requestContent);
    }

    protected Container parse(Node node) throws Exception {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Node nodeClass = this.getChildNode(node, "class");
            String nodeName = nodeClass.getTextContent().toLowerCase();
            if (nodeName.equals("form")) {
                return parseForm(node);
            } else if (nodeName.equals("input")) {
                return parseInput(node);
            } else if (nodeName.equals("textarea")) {
                return parseTextarea(node);
            } else if (nodeName.equals("select")) {
                return parseSelect(node);
            } else if (nodeName.equals("tabbedpane")) {
                return parseTabbedPane(node);
            } else if (nodeName.equals("panel")) {
                return parsePanel(node);
            } else if (nodeName.equals("reference")) {
                return parseReference(node);
            } else if (nodeName.equals("checkboxlist")) {
                return parseCheckboxList(node);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    protected Container parseForm(Node node) throws Exception {
        Node nodeMethod = this.getChildNode(node, "method");
        Node nodeAction = this.getChildNode(node, "action");
        Node nodeItem = this.getChildNode(node, "item");
        if (nodeMethod != null) {
            requestMethod = nodeMethod.getTextContent();
        } else {
            throw new Exception("Request method is missing");
        }
        if (nodeAction != null) {
            requestUrl = nodeAction.getTextContent();
        } else {
            throw new Exception("Request value is missing");
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        Container con = this.parse(nodeItem);
        if (con != null) {
            panel.add(con, BorderLayout.CENTER);
        }
        return panel;
    }

    protected Container parseInput(Node node) {
        Node nodeRef = this.getChildNode(node, "ref");
        Node nodeLabel = this.getChildNode(node, "label");
        Node nodeValue = this.getChildNode(node, "value");
        Node nodeDisabled = this.getChildNode(node, "disabled");
        Node nodeType = this.getChildNode(node, "type");
        JPanel item = new JPanel();
        item.setLayout(new FlowLayout());
        JLabel label = new JLabel(nodeLabel.getTextContent());
        label.setPreferredSize(new Dimension(100, 22));
        Input input = new Input();
        input.setPreferredSize(new Dimension(300, 22));
        if (nodeValue != null) {
            input.setText(nodeValue.getTextContent());
        }
        if (nodeDisabled != null && nodeDisabled.getTextContent().equals("true")) {
            input.setEnabled(false);
        }
        item.add(label);
        item.add(input);
        requestFields.put(nodeRef.getTextContent(), input);
        if (nodeType != null && nodeType.getTextContent().equals("hidden")) {
            return null;
        } else {
            return item;
        }
    }

    protected Container parseSelect(Node node) {
        Node nodeRef = this.getChildNode(node, "ref");
        Node nodeLabel = this.getChildNode(node, "label");
        Node nodeValue = this.getChildNode(node, "value");
        Node nodeDisabled = this.getChildNode(node, "disabled");
        Node nodeChildren = this.getChildNode(node, "children");
        JPanel item = new JPanel();
        item.setLayout(new FlowLayout());
        JLabel label = new JLabel(nodeLabel.getTextContent());
        label.setPreferredSize(new Dimension(100, 22));
        if (nodeChildren != null) {
            SelectItem[] items = this.getSelectOptions(nodeChildren);
            if (items != null) {
                DefaultComboBoxModel<SelectItem> model = new DefaultComboBoxModel<SelectItem>(items);
                Select input = new Select(model);
                input.setPreferredSize(new Dimension(300, 22));
                if (nodeValue != null) {
                    for (int i = 0; i < model.getSize(); i++) {
                        SelectItem boxItem = (SelectItem) model.getElementAt(i);
                        if (boxItem.getKey().equals(nodeValue.getTextContent())) {
                            input.setSelectedIndex(i);
                        }
                    }
                }
                if (nodeDisabled != null && nodeDisabled.getTextContent().equals("true")) {
                    input.setEnabled(false);
                }
                item.add(label);
                item.add(input);
                requestFields.put(nodeRef.getTextContent(), input);
                return item;
            }
        }
        return null;
    }

    protected SelectItem[] getSelectOptions(Node node) {
        ArrayList<Node> options = this.getChildNodes(node, "item");
        SelectItem[] items = new SelectItem[options.size()];
        for (int i = 0; i < options.size(); i++) {
            Node nodeLabel = this.getChildNode(options.get(i), "label");
            Node nodeValue = this.getChildNode(options.get(i), "value");
            if (nodeLabel != null && nodeValue != null) {
                items[i] = new SelectItem(nodeValue.getTextContent(), nodeLabel.getTextContent());
            }
        }
        return items;
    }

    protected Container parseTextarea(Node node) {
        Node nodeRef = this.getChildNode(node, "ref");
        Node nodeLabel = this.getChildNode(node, "label");
        Node nodeValue = this.getChildNode(node, "value");
        JPanel item = new JPanel();
        item.setLayout(new FlowLayout());
        JLabel label = new JLabel(nodeLabel.getTextContent());
        label.setPreferredSize(new Dimension(100, 22));
        Textarea input = new Textarea();
        JScrollPane scpInput = new JScrollPane(input);
        scpInput.setPreferredSize(new Dimension(300, 200));
        if (nodeValue != null) {
            input.setText(nodeValue.getTextContent());
        }
        item.add(label);
        item.add(scpInput);
        requestFields.put(nodeRef.getTextContent(), input);
        return item;
    }

    protected Container parseTabbedPane(Node node) throws Exception {
        JTabbedPane item = new JTabbedPane();
        Node nodeChildren = this.getChildNode(node, "children");
        if (nodeChildren != null) {
            ArrayList<Node> items = this.getChildNodes(nodeChildren, "item");
            for (int i = 0; i < items.size(); i++) {
                Node nodeLabel = this.getChildNode(items.get(i), "label");
                if (nodeLabel != null) {
                    Container con = this.parse(items.get(i));
                    if (con != null) {
                        item.addTab(nodeLabel.getTextContent(), con);
                    }
                }
            }
        }
        return item;
    }

    protected Container parsePanel(Node node) throws Exception {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));
        JPanel item = new JPanel();
        item.setPreferredSize(new Dimension(600, 600));
        item.setLayout(new FlowLayout(FlowLayout.LEADING));
        Node nodeChildren = this.getChildNode(node, "children");
        if (nodeChildren != null) {
            ArrayList<Node> items = this.getChildNodes(nodeChildren, "item");
            for (int i = 0; i < items.size(); i++) {
                Container con = this.parse(items.get(i));
                if (con != null) {
                    item.add(con);
                }
            }
        }
        panel.add(item);
        return panel;
    }

    protected Container parseReference(Node node) {
        Node nodeRef = this.getChildNode(node, "ref");
        Node nodeLabel = this.getChildNode(node, "label");
        Node nodeValue = this.getChildNode(node, "value");
        Node nodeDisabled = this.getChildNode(node, "disabled");
        Node nodeValueField = this.getChildNode(node, "valueField");
        Node nodeLabelField = this.getChildNode(node, "labelField");
        Node nodeSrc = this.getChildNode(node, "src");
        JPanel item = new JPanel();
        item.setLayout(new FlowLayout());
        JLabel label = new JLabel(nodeLabel.getTextContent());
        label.setPreferredSize(new Dimension(100, 22));
        Input input = new Input();
        input.setPreferredSize(new Dimension(255, 22));
        JButton button = new JButton(nodeRef.getTextContent());
        button.setPreferredSize(new Dimension(40, 22));
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    JButton source = (JButton) e.getSource();
                    String key = source.getText();
                    if (referenceFields.containsKey(key)) {
                        ReferenceItem item = referenceFields.get(key);
                        SearchPanel panel = item.getPanel();
                        if (panel == null) {
                            panel = new SearchPanel(item);
                            item.setPanel(panel);
                        }
                        panel.setVisible(true);
                        panel.toFront();
                    }
                } catch (Exception ex) {
                    logger.warning(ex.getMessage());
                }
            }
        });
        if (nodeValue != null) {
            input.setText(nodeValue.getTextContent());
        }
        if (nodeDisabled != null && nodeDisabled.getTextContent().equals("true")) {
            input.setEnabled(false);
        }
        item.add(label);
        item.add(input);
        item.add(button);
        referenceFields.put(nodeRef.getTextContent(), new ReferenceItem(nodeValueField.getTextContent(), nodeLabelField.getTextContent(), nodeSrc.getTextContent(), input));
        requestFields.put(nodeRef.getTextContent(), input);
        return item;
    }

    protected Container parseCheckboxList(Node node) {
        Node nodeRef = this.getChildNode(node, "ref");
        Node nodeLabel = this.getChildNode(node, "label");
        Node nodeSrc = this.getChildNode(node, "src");
        JPanel item = new JPanel();
        item.setLayout(new BorderLayout());
        JLabel label = new JLabel(nodeLabel.getTextContent());
        label.setPreferredSize(new Dimension(100, 22));
        CheckboxList checkboxlist = new CheckboxList(nodeSrc.getTextContent());
        checkboxlist.setPreferredSize(new Dimension(255, 22));
        item.add(checkboxlist, BorderLayout.CENTER);
        requestFields.put(nodeRef.getTextContent(), checkboxlist);
        return item;
    }

    protected ArrayList<Node> getChildNodes(Node node, String nodeName) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        NodeList childList = node.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            Node childNode = childList.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (childNode.getNodeName().equals(nodeName)) {
                nodes.add(childNode);
            }
        }
        return nodes;
    }

    protected Node getChildNode(Node node, String nodeName) {
        NodeList childList = node.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            Node childNode = childList.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (childNode.getNodeName().equals(nodeName)) {
                return childNode;
            }
        }
        return null;
    }
}
