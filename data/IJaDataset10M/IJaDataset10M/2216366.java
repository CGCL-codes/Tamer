package org.outerj.pollo.engine.cocoon;

import org.outerj.pollo.xmleditor.model.XmlModel;
import org.w3c.dom.Element;
import org.jaxen.XPath;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.dom.DOMXPath;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Vector;

/**
 * Dialog that shows the patterns of the various matchers in a sitemap.
 *
 * @author Bruno Dumon
 */
public class GoToMatcherDialog extends JDialog {

    protected final String SITEMAP_NS = "http://apache.org/cocoon/sitemap/1.0";

    protected JComboBox matcherTypesCombo;

    protected JList matchersList;

    protected XmlModel xmlModel;

    protected SimpleNamespaceContext namespaceContext;

    protected boolean ok;

    public GoToMatcherDialog(Frame parent, XmlModel xmlModel) {
        super(parent, "Go To Matcher");
        this.xmlModel = xmlModel;
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        panel.setLayout(new BorderLayout(12, 12));
        setContentPane(panel);
        matcherTypesCombo = new JComboBox();
        matcherTypesCombo.addActionListener(new ComboSelectionListener());
        panel.add(matcherTypesCombo, BorderLayout.NORTH);
        matchersList = new JList();
        JScrollPane scrollPane = new JScrollPane(matchersList);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        panel.add(scrollPane, BorderLayout.CENTER);
        Box buttonBox = new Box(BoxLayout.X_AXIS);
        panel.add(buttonBox, BorderLayout.SOUTH);
        buttonBox.add(Box.createHorizontalGlue());
        ButtonListener buttonListener = new ButtonListener();
        JButton okButton = new JButton("Goto");
        okButton.addActionListener(buttonListener);
        okButton.setActionCommand("goto");
        buttonBox.add(okButton);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(buttonListener);
        closeButton.setActionCommand("close");
        buttonBox.add(Box.createHorizontalStrut(6));
        buttonBox.add(closeButton);
        namespaceContext = new SimpleNamespaceContext();
        namespaceContext.addNamespace("map", SITEMAP_NS);
        setModal(true);
    }

    public Element showIt() {
        this.ok = false;
        matcherTypesCombo.removeAllItems();
        try {
            String matcherTypesExpr = "/map:sitemap/map:components/map:matchers/map:matcher";
            XPath matcherTypesXPath = new DOMXPath(matcherTypesExpr);
            matcherTypesXPath.setNamespaceContext(namespaceContext);
            java.util.List list = matcherTypesXPath.selectNodes(xmlModel.getDocument().getDocumentElement());
            Iterator matcherTypesIt = list.iterator();
            while (matcherTypesIt.hasNext()) {
                Object object = matcherTypesIt.next();
                if (object instanceof Element) {
                    matcherTypesCombo.addItem(new MatcherTypeElementWrapper((Element) object));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pack();
        setLocationRelativeTo(getParent());
        show();
        if (ok == false) {
            return null;
        } else {
            int selectedIndex = matchersList.getSelectedIndex();
            if (selectedIndex == -1) return null;
            ElementWrapper selected = (ElementWrapper) matchersList.getModel().getElementAt(selectedIndex);
            if (selected != null) return selected.getElement(); else return null;
        }
    }

    public class ComboSelectionListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            MatcherTypeElementWrapper selected = (MatcherTypeElementWrapper) matcherTypesCombo.getSelectedItem();
            if (selected != null) {
                try {
                    String matchersExpr = "/map:sitemap/map:pipelines//map:match[@type='" + selected.getTypeName() + "'" + (selected.isDefault() ? " or not(@type) " : "") + "]";
                    XPath matchersXPath = new DOMXPath(matchersExpr);
                    matchersXPath.setNamespaceContext(namespaceContext);
                    java.util.List list = matchersXPath.selectNodes(xmlModel.getDocument().getDocumentElement());
                    Iterator matcherTypesIt = list.iterator();
                    Vector vector = new Vector();
                    while (matcherTypesIt.hasNext()) {
                        Object object = matcherTypesIt.next();
                        if (object instanceof Element) {
                            vector.add(new ElementWrapper((Element) object));
                        }
                    }
                    matchersList.setListData(vector);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class ElementWrapper {

        private final Element element;

        private final String displayString;

        public ElementWrapper(Element element) {
            this.element = element;
            displayString = element.getAttribute("pattern");
        }

        public Element getElement() {
            return element;
        }

        public String toString() {
            return displayString;
        }
    }

    private class MatcherTypeElementWrapper {

        private final Element element;

        private String typeName;

        public MatcherTypeElementWrapper(Element element) {
            this.element = element;
            this.typeName = element.getAttribute("name");
            if (typeName == null) typeName = "";
        }

        public String getTypeName() {
            return typeName;
        }

        public boolean isDefault() {
            Element parent = (Element) element.getParentNode();
            String defaultType = parent.getAttribute("default");
            if (typeName.equals(defaultType)) return true; else return false;
        }

        public String toString() {
            return typeName;
        }
    }

    public class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("goto")) {
                ok = true;
                hide();
            } else if (e.getActionCommand().equals("close")) {
                ok = false;
                hide();
            }
        }
    }
}
