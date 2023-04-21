package itSIMPLE;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import languages.xml.XMLUtilities;
import org.jdom.Element;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.jdom.JDOMXPath;
import java.awt.Color;

/**
 *
 * @author tiago
 */
public class EditLifelineDialog extends JDialog {

    private Element lifeline = null;

    private Element diagram = null;

    private static LifelinePanel parent = null;

    private JPanel mainPanel = null;

    private ItPanel intervalsPanel;

    private JTextField ruleField;

    private JToolBar intervalsToolBar = null;

    private JList intervalsList = null;

    private DefaultListModel intervalsListModel = null;

    private ArrayList<Element> currentIntervals = null;

    private ItPanel editAndNewIntervalPanel;

    private Element selectedIntervalData = null;

    private JTextField rule;

    private JComboBox lowerboundType;

    private JComboBox lowerboundValue;

    private JComboBox upperboundType;

    private JComboBox upperboundValue;

    @SuppressWarnings("static-access")
    public EditLifelineDialog(Element timingDiagram, Element selectedlifeline, LifelinePanel parent) {
        super(ItSIMPLE.getItSIMPLEFrame());
        setTitle("Edit lifeline (" + parent.getLifelineName() + ")");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        addWindowListener(new JDialogWindowAdapter(this));
        setModal(true);
        this.lifeline = selectedlifeline;
        this.diagram = timingDiagram;
        this.parent = parent;
        this.setSize(520, 350);
        this.setLocation(280, 200);
        this.add(getMainPanel(), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh", new ImageIcon("resources/images/refresh.png"));
        refreshButton.addActionListener(refreshLifeLinePanel);
        refreshButton.setToolTipText("Refresh current lifeline");
        buttonPanel.add(refreshButton);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String dtype = diagram.getChildText("type");
                String context = diagram.getChildText("context");
                if (dtype.equals("condition")) {
                    if (context.equals("action")) {
                    }
                }
                refreshLifelineChart();
                dispose();
            }
        });
        buttonPanel.add(okButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JPanel(new BorderLayout());
            String dtype = diagram.getChildText("type");
            String context = diagram.getChildText("context");
            if (dtype.equals("condition")) {
                if (context.equals("action")) {
                }
            }
            mainPanel.add(getIntervalsPanel(), BorderLayout.CENTER);
        }
        return mainPanel;
    }

    private ItPanel getIntervalsPanel() {
        intervalsPanel = new ItPanel(new BorderLayout());
        ItPanel top = new ItPanel(new BorderLayout());
        ItPanel bottom = new ItPanel(new BorderLayout());
        ItPanel listPanel = new ItPanel(new BorderLayout());
        top.add(new JLabel("Time intervals:"), BorderLayout.NORTH);
        intervalsListModel = new DefaultListModel();
        intervalsList = new JList(intervalsListModel);
        ItListRenderer renderer = new ItListRenderer();
        renderer.setIcon(new ImageIcon("resources/images/operator.png"));
        intervalsList.setCellRenderer(renderer);
        intervalsList.setBackground(Color.WHITE);
        intervalsList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (intervalsList.getSelectedIndex() > -1) {
                        Element selected = currentIntervals.get(intervalsList.getSelectedIndex());
                        if (selected != null) {
                            selectedIntervalData = selected;
                            fillEditIntervalPanel();
                            editAndNewIntervalPanel.setVisible(true);
                        }
                    }
                }
            }
        });
        JScrollPane scrollText = new JScrollPane();
        scrollText.setViewportView(intervalsList);
        buildTimeIntervalsList();
        top.add(scrollText, BorderLayout.CENTER);
        intervalsToolBar = new JToolBar();
        intervalsToolBar.add(newTimeInterval).setToolTipText("New time interval");
        intervalsToolBar.add(deleteTimeInterval).setToolTipText("Delete time interval");
        intervalsToolBar.setFloatable(false);
        bottom.add(intervalsToolBar, BorderLayout.NORTH);
        listPanel.add(top, BorderLayout.CENTER);
        listPanel.add(bottom, BorderLayout.SOUTH);
        editAndNewIntervalPanel = new ItPanel(new BorderLayout());
        editAndNewIntervalPanel.setPreferredSize(new Dimension(250, 65));
        editAndNewIntervalPanel.add(new JLabel("Value: "), BorderLayout.WEST);
        rule = new JTextField();
        rule.setPreferredSize(new Dimension(250, 40));
        rule.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                selectedIntervalData.getChild("value").setText(rule.getText());
                intervalsListModel.set(intervalsList.getSelectedIndex(), getIntervalLabel(selectedIntervalData));
            }
        });
        editAndNewIntervalPanel.add(rule, BorderLayout.CENTER);
        JPanel bounds = new JPanel(new SpringLayout());
        JLabel intervalLabel = new JLabel("Interval: ");
        lowerboundType = new JComboBox();
        lowerboundType.addItem("[");
        lowerboundType.addItem("(");
        lowerboundType.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                String selection = (String) lowerboundType.getSelectedItem();
                if (selection.equals("[")) {
                    selectedIntervalData.getChild("durationConstratint").getChild("lowerbound").setAttribute("included", "true");
                } else {
                    selectedIntervalData.getChild("durationConstratint").getChild("lowerbound").setAttribute("included", "false");
                }
                intervalsListModel.set(intervalsList.getSelectedIndex(), getIntervalLabel(selectedIntervalData));
            }
        });
        lowerboundValue = new JComboBox();
        lowerboundValue.addItem("");
        lowerboundValue.addItem("0");
        lowerboundValue.addItem("-1");
        lowerboundValue.addItem("1");
        lowerboundValue.addItem("2");
        lowerboundValue.setEditable(true);
        lowerboundValue.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                String selection = (String) lowerboundValue.getSelectedItem();
                selectedIntervalData.getChild("durationConstratint").getChild("lowerbound").setAttribute("value", selection);
                intervalsListModel.set(intervalsList.getSelectedIndex(), getIntervalLabel(selectedIntervalData));
            }
        });
        JLabel commaLabel = new JLabel(" , ");
        upperboundType = new JComboBox();
        upperboundType.addItem("]");
        upperboundType.addItem(")");
        upperboundType.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                String selection = (String) upperboundType.getSelectedItem();
                if (selection.equals("]")) {
                    selectedIntervalData.getChild("durationConstratint").getChild("upperbound").setAttribute("included", "true");
                } else {
                    selectedIntervalData.getChild("durationConstratint").getChild("upperbound").setAttribute("included", "false");
                }
                intervalsListModel.set(intervalsList.getSelectedIndex(), getIntervalLabel(selectedIntervalData));
            }
        });
        upperboundValue = new JComboBox();
        upperboundValue.addItem("");
        upperboundValue.addItem("0");
        upperboundValue.addItem("1");
        upperboundValue.addItem("2");
        upperboundValue.setEditable(true);
        upperboundValue.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                String selection = (String) upperboundValue.getSelectedItem();
                selectedIntervalData.getChild("durationConstratint").getChild("upperbound").setAttribute("value", selection);
                intervalsListModel.set(intervalsList.getSelectedIndex(), getIntervalLabel(selectedIntervalData));
            }
        });
        bounds.add(intervalLabel);
        bounds.add(lowerboundType);
        bounds.add(lowerboundValue);
        bounds.add(commaLabel);
        bounds.add(upperboundValue);
        bounds.add(upperboundType);
        SpringUtilities.makeCompactGrid(bounds, 1, 6, 5, 5, 5, 5);
        editAndNewIntervalPanel.add(bounds, BorderLayout.SOUTH);
        editAndNewIntervalPanel.setVisible(false);
        intervalsPanel.add(listPanel, BorderLayout.CENTER);
        intervalsPanel.add(editAndNewIntervalPanel, BorderLayout.SOUTH);
        return intervalsPanel;
    }

    private void buildTimeIntervalsList() {
        if (currentIntervals == null) {
            currentIntervals = new ArrayList<Element>();
        } else {
            currentIntervals.clear();
        }
        List<?> intervals = lifeline.getChild("timeIntervals").getChildren("timeInterval");
        for (Iterator<?> iter = intervals.iterator(); iter.hasNext(); ) {
            Element interval = (Element) iter.next();
            currentIntervals.add(interval);
            intervalsListModel.addElement(getIntervalLabel(interval));
        }
    }

    private String getIntervalLabel(Element interval) {
        Element lowerbound = interval.getChild("durationConstratint").getChild("lowerbound");
        Element upperbound = interval.getChild("durationConstratint").getChild("upperbound");
        String intervalLabel = (lowerbound.getAttributeValue("included").equals("true") ? "[" : "(") + lowerbound.getAttributeValue("value") + "," + upperbound.getAttributeValue("value") + (upperbound.getAttributeValue("included").equals("true") ? "]" : ")") + " -> " + interval.getChildText("value");
        return intervalLabel;
    }

    private void fillEditIntervalPanel() {
        if (selectedIntervalData != null) {
            rule.setText(selectedIntervalData.getChildText("value"));
            Element lowerbound = selectedIntervalData.getChild("durationConstratint").getChild("lowerbound");
            Element upperbound = selectedIntervalData.getChild("durationConstratint").getChild("upperbound");
            lowerboundType.setSelectedIndex((lowerbound.getAttributeValue("included").equals("true") ? 0 : 1));
            upperboundType.setSelectedIndex((upperbound.getAttributeValue("included").equals("true") ? 0 : 1));
            lowerboundValue.setSelectedItem(lowerbound.getAttributeValue("value"));
            upperboundValue.setSelectedItem(upperbound.getAttributeValue("value"));
        }
    }

    /**
         * This action adds a new time interval
         */
    private Action newTimeInterval = new AbstractAction("New", new ImageIcon("resources/images/new.png")) {

        public void actionPerformed(ActionEvent e) {
            Element interval = (Element) ItSIMPLE.getCommonData().getChild("definedNodes").getChild("elements").getChild("model").getChild("timeInterval").clone();
            String id = String.valueOf(XMLUtilities.getId(lifeline.getChild("timeIntervals")));
            interval.getAttribute("id").setValue(id);
            interval.getChild("value").setText("true");
            lifeline.getChild("timeIntervals").addContent(interval);
            intervalsListModel.addElement(getIntervalLabel(interval));
            currentIntervals.add(interval);
            editAndNewIntervalPanel.setVisible(true);
            selectedIntervalData = interval;
            intervalsList.setSelectedIndex(intervalsListModel.size() - 1);
        }
    };

    /**
     * This action deletes a time interval
     */
    private Action deleteTimeInterval = new AbstractAction("Delete", new ImageIcon("resources/images/delete.png")) {

        public void actionPerformed(ActionEvent e) {
            int row = intervalsList.getSelectedIndex();
            if (row > -1) {
                Element selectedInterval = currentIntervals.get(row);
                lifeline.getChild("timeIntervals").removeContent(selectedInterval);
                currentIntervals.remove(row);
                intervalsListModel.removeElementAt(row);
                intervalsList.setSelectedIndex(intervalsListModel.size() - 1);
                if (intervalsListModel.size() == 0) {
                    selectedIntervalData = null;
                    editAndNewIntervalPanel.setVisible(false);
                }
            }
        }
    };

    /**
     * This action deletes a time interval
     */
    private Action refreshLifeLinePanel = new AbstractAction("Refresh", new ImageIcon("resources/images/refresh.png")) {

        public void actionPerformed(ActionEvent e) {
            refreshLifelineChart();
        }
    };

    public void refreshLifelineChart() {
        parent.refreshLifeline();
    }
}

class JDialogWindowAdapter extends WindowAdapter {

    private JDialog m_dialog = null;

    /**
	 * Constructs the adapter.
	 * @param d the dialog to listen to.
	 */
    public JDialogWindowAdapter(JDialog d) {
        m_dialog = d;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        ((EditLifelineDialog) m_dialog).refreshLifelineChart();
        super.windowClosing(e);
    }
}
