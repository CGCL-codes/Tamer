package toxTree.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import toxTree.core.IDecisionCategories;
import toxTree.core.IDecisionResult;
import toxTree.data.ActionList;
import toxTree.data.ToxTreeActions;
import toxTree.data.ToxTreeModule;
import toxTree.exceptions.DecisionResultException;
import toxTree.tree.CategoriesList;
import toxTree.ui.tree.categories.CategoriesPanel;

/**
 * The panel at the right of {@link toxTree.apps.ToxTreeApp} main screen.
 * Displays estimation result and details.
 * @author Nina Jeliazkova <br>
 * <b>Modified</b> 2005-4-30
 */
public class HazardPanel extends DataModulePanel {

    protected Action editAction;

    protected ToxTreeModule model;

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 8769660495123570173L;

    JLabel methodLabel;

    GridBagLayout gridBag;

    JButton estimateButton;

    JCheckBox explainOption;

    JTextArea explainArea;

    TitledBorder tBorder;

    CategoriesPanel cPanel;

    /**
	 * 
	 */
    public HazardPanel(ToxTreeModule toxModule) {
        super(toxModule);
        model = toxModule;
        setBackground(Color.black);
        setLayout(gridBag);
        setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.black));
    }

    public void update(Observable o, Object arg) {
        methodLabel.setText("<html><b> by <u>" + model.getRules().toString() + "</u></b></html>");
        tBorder.setTitle(model.getRules().toString());
        IDecisionCategories assignedCategories = model.getTreeResult().getAssignedCategories();
        for (int i = 0; i < assignedCategories.size(); i++) model.getRules().getCategories().setSelected(assignedCategories.get(i));
        cPanel.setData(model.getRules().getCategories(), model.getTreeResult());
        StringBuffer b = new StringBuffer();
        try {
            b = model.getTreeResult().explain(explainOption.isSelected());
        } catch (DecisionResultException x) {
            b.append(x.getMessage());
        }
        explainArea.setText(b.toString());
        repaint();
    }

    @Override
    protected void addWidgets(ActionList actions) {
        model = (ToxTreeModule) dataModule;
        gridBag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JLabel label = new JLabel("<html><b>Toxic Hazard</b></html>");
        label.setOpaque(true);
        label.setBackground(Color.black);
        label.setForeground(Color.white);
        label.setSize(120, 32);
        label.setAlignmentX(CENTER_ALIGNMENT);
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridBag.setConstraints(label, c);
        add(label);
        methodLabel = new JLabel("");
        methodLabel.setOpaque(true);
        methodLabel.setBackground(Color.black);
        methodLabel.setForeground(Color.white);
        methodLabel.setSize(120, 32);
        methodLabel.setAlignmentX(CENTER_ALIGNMENT);
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(methodLabel, c);
        add(methodLabel);
        editAction = ((ToxTreeActions) actions).getViewMethodAction();
        methodLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                editAction.actionPerformed(new ActionEvent(this, 0, ""));
            }
        });
        estimateButton = new JButton(((ToxTreeActions) actions).getEstimateAction());
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.setConstraints(estimateButton, c);
        add(estimateButton);
        JPanel propertiesPanel = new JPanel(new BorderLayout());
        cPanel = new CategoriesPanel(model.getRules().getCategories(), model.getTreeResult());
        cPanel.setMinimumSize(new Dimension(256, 3 * 48 + 2));
        cPanel.setPreferredSize(new Dimension(256, 3 * 48 + 2));
        propertiesPanel.add(cPanel, BorderLayout.CENTER);
        explainOption = new JCheckBox("Verbose explanation");
        explainOption.setSelected(true);
        propertiesPanel.add(explainOption, BorderLayout.SOUTH);
        c.fill = GridBagConstraints.BOTH;
        c.weighty = Integer.MAX_VALUE;
        explainArea = new JTextArea("Cramer Rules");
        explainArea.setAutoscrolls(true);
        tBorder = BorderFactory.createTitledBorder("");
        explainArea.setBorder(tBorder);
        JScrollPane scrollText = new JScrollPane(explainArea);
        JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, propertiesPanel, scrollText);
        splitPanel.setBackground(Color.black);
        splitPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        splitPanel.setOneTouchExpandable(false);
        splitPanel.setDividerLocation(200);
        gridBag.setConstraints(splitPanel, c);
        add(splitPanel);
    }

    public void addActionListener(ActionListener l) {
        estimateButton.addActionListener(l);
    }

    public IDecisionResult getModel() {
        return model.getTreeResult();
    }
}
