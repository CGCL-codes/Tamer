package de.hu.logic.structure;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.Customizer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jgrapht.event.GraphVertexChangeEvent;
import org.jgrapht.event.VertexSetListener;
import org.jgrapht.graph.ListenableDirectedGraph;
import de.hu.gralog.structure.Structure;

public class TransitionSystemCustomizer<V extends TransitionSystemVertex, E extends TransitionSystemEdge, GB extends TransitionSystem<V, E, G>, G extends ListenableDirectedGraph<V, E>> extends JPanel implements Customizer, ActionListener, ListSelectionListener, ItemListener {

    private static final String ADD_PROPOSITION = "add";

    private static final String REMOVE_PROPOSITION = "remove";

    private static final String RENAME_PROPOSITION = "rename";

    private static final String TO_IN_LIST = "<";

    private static final String TO_OUT_LIST = ">";

    private static final String ALL_TO_IN_LIST = "<<";

    private static final String ALL_TO_OUT_LIST = ">>";

    private Structure<V, E, GB, G> transitionSystem;

    private ChoosePropositionComboBoxModel choosePropositionComboBoxModel;

    private JComboBox chooseProposition;

    private final JButton addProposition = new JButton(ADD_PROPOSITION);

    private final JButton removeProposition = new JButton(REMOVE_PROPOSITION);

    private final JButton renameProposition = new JButton(RENAME_PROPOSITION);

    private final JPanel editPanel = new JPanel();

    private final JButton toInList = new JButton(TO_IN_LIST);

    private final JButton toOutList = new JButton(TO_OUT_LIST);

    private final JButton alltoInList = new JButton(ALL_TO_IN_LIST);

    private final JButton alltoOutList = new JButton(ALL_TO_OUT_LIST);

    private JList inList;

    private JList outList;

    private Proposition currentProposition = null;

    public TransitionSystemCustomizer() {
        super();
    }

    protected void createPanel() {
        setLayout(new BorderLayout());
        add(createChoosePropositionPanel(), BorderLayout.NORTH);
        add(createEditPropositionPanel(), BorderLayout.CENTER);
    }

    protected JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(Box.createHorizontalGlue());
        addProposition.setActionCommand(ADD_PROPOSITION);
        addProposition.addActionListener(this);
        addProposition.setToolTipText("Adds a proposition to your transitionsystem.");
        panel.add(addProposition);
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        removeProposition.setActionCommand(REMOVE_PROPOSITION);
        removeProposition.addActionListener(this);
        removeProposition.setToolTipText("Removes the current proposition from your transitionsystem.");
        panel.add(removeProposition);
        panel.add(Box.createRigidArea(new Dimension(2, 0)));
        renameProposition.setActionCommand(RENAME_PROPOSITION);
        renameProposition.addActionListener(this);
        renameProposition.setToolTipText("Renames the current proposition.");
        panel.add(renameProposition);
        return panel;
    }

    protected JPanel createEditPropositionPanel() {
        editPanel.setLayout(new GridBagLayout());
        inList = new JList(new InOutPropositionListModel(true));
        inList.setToolTipText("This list shows all the states contained in your proposition.");
        outList = new JList(new InOutPropositionListModel(false));
        outList.setToolTipText("This list shows all the states that are not contained in your proposition.");
        inList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        outList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        inList.getSelectionModel().addListSelectionListener(this);
        outList.getSelectionModel().addListSelectionListener(this);
        JPanel inListPanel = new JPanel();
        inListPanel.setLayout(new BorderLayout());
        inListPanel.setBorder(BorderFactory.createTitledBorder("In-List"));
        inListPanel.add(new JScrollPane(inList));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        editPanel.add(inListPanel, c);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        toInList.setActionCommand(TO_IN_LIST);
        toInList.addActionListener(this);
        toInList.setToolTipText("<html>Adds all states that are selected in <b>Out-List</b> to your proposition.</html>");
        buttonPanel.add(toInList);
        toOutList.setActionCommand(TO_OUT_LIST);
        toOutList.addActionListener(this);
        toOutList.setToolTipText("<html>Removes all states that are selected in <b>In-List</b> from your proposition.</html>");
        buttonPanel.add(toOutList);
        buttonPanel.add(Box.createVerticalGlue());
        alltoInList.setActionCommand(ALL_TO_IN_LIST);
        alltoInList.addActionListener(this);
        alltoInList.setToolTipText("Adds all available states to your proposition.");
        buttonPanel.add(alltoInList);
        alltoOutList.setActionCommand(ALL_TO_OUT_LIST);
        alltoOutList.addActionListener(this);
        alltoOutList.setToolTipText("Removes all states from your proposition.");
        buttonPanel.add(alltoOutList);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1;
        c.weightx = 0.01;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.CENTER;
        editPanel.add(buttonPanel, c);
        JPanel outListPanel = new JPanel();
        outListPanel.setLayout(new BorderLayout());
        outListPanel.setBorder(BorderFactory.createTitledBorder("Out-List"));
        outListPanel.add(new JScrollPane(outList));
        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        c.weighty = 1;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        editPanel.add(outListPanel, c);
        return editPanel;
    }

    protected JPanel createChoosePropositionPanel() {
        JPanel choosePropositionPanel = new JPanel();
        choosePropositionPanel.setLayout(new BoxLayout(choosePropositionPanel, BoxLayout.X_AXIS));
        JLabel choosePropositionLabel = new JLabel("Choose Proposition:");
        choosePropositionPanel.add(choosePropositionLabel);
        choosePropositionPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        choosePropositionComboBoxModel = new ChoosePropositionComboBoxModel();
        chooseProposition = new JComboBox(choosePropositionComboBoxModel);
        chooseProposition.setPreferredSize(new Dimension(100, (int) chooseProposition.getPreferredSize().getHeight()));
        chooseProposition.addItemListener(this);
        chooseProposition.setToolTipText("Please select the proposition you want to edit.");
        choosePropositionPanel.add(chooseProposition);
        choosePropositionPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(choosePropositionPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(createButtonPanel());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        return panel;
    }

    public void setObject(Object bean) {
        transitionSystem = (Structure<V, E, GB, G>) bean;
        createPanel();
        updateControls();
    }

    protected Proposition getProposition() {
        if (currentProposition == null) {
            String name = (String) chooseProposition.getSelectedItem();
            if (name == null) return null;
            currentProposition = transitionSystem.getStructureBean().getProposition(name);
        }
        return currentProposition;
    }

    private class InOutPropositionListModel extends AbstractListModel implements PropertyChangeListener, VertexSetListener {

        protected Proposition currentProposition = null;

        protected ArrayList<TransitionSystemVertex> vertexes = null;

        protected boolean in;

        public InOutPropositionListModel(boolean in) {
            this.in = in;
            transitionSystem.getPropertyChangeSupport().addPropertyChangeListener(this);
            transitionSystem.getGraph().addVertexSetListener(this);
        }

        protected ArrayList<TransitionSystemVertex> getVertexes() {
            if (vertexes == null) {
                if (in && getProposition() == null) vertexes = new ArrayList<TransitionSystemVertex>(); else {
                    if (in) vertexes = getProposition().getVertices(); else {
                        Set<TransitionSystemVertex> vertexSet = new HashSet<TransitionSystemVertex>(transitionSystem.getGraph().vertexSet());
                        if (getProposition() != null) vertexSet.removeAll(getProposition().getVertices());
                        vertexes = new ArrayList<TransitionSystemVertex>(vertexSet);
                    }
                }
            }
            return vertexes;
        }

        public int getSize() {
            return getVertexes().size();
        }

        public Object getElementAt(int index) {
            return getVertexes().get(index);
        }

        public void selectedPropositionChanged() {
            vertexes = null;
            fireContentsChanged(this, 0, getSize());
        }

        public void propertyChange(PropertyChangeEvent e) {
            vertexes = null;
            fireContentsChanged(this, 0, getSize());
        }

        public void vertexAdded(GraphVertexChangeEvent arg0) {
            vertexes = null;
            fireContentsChanged(this, 0, getSize());
        }

        public void vertexRemoved(GraphVertexChangeEvent arg0) {
            vertexes = null;
            fireContentsChanged(this, 0, getSize());
        }
    }

    private class ChoosePropositionComboBoxModel extends AbstractListModel implements ComboBoxModel, PropertyChangeListener {

        private String selectedPropostionName;

        public ChoosePropositionComboBoxModel() {
            transitionSystem.getPropertyChangeSupport().addPropertyChangeListener(this);
        }

        public void setSelectedItem(Object anItem) {
            selectedPropostionName = (String) anItem;
        }

        public Object getSelectedItem() {
            return selectedPropostionName;
        }

        public int getSize() {
            return transitionSystem.getStructureBean().getPropositions().length;
        }

        public Object getElementAt(int index) {
            return transitionSystem.getStructureBean().getPropositions(index).getName();
        }

        public void propertyChange(PropertyChangeEvent e) {
            if (getSelectedItem() != null && transitionSystem.getStructureBean().getProposition((String) getSelectedItem()) == null) {
                chooseProposition.setSelectedItem(null);
                chooseProposition.repaint();
            }
            fireContentsChanged(this, 0, getSize());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(ADD_PROPOSITION)) {
            String name = JOptionPane.showInputDialog(this, "name: ");
            if (name != null) {
                if (transitionSystem.getStructureBean().getProposition(name) == null) transitionSystem.getStructureBean().addProposition(new Proposition(name));
                chooseProposition.setSelectedItem(name);
                chooseProposition.repaint();
            }
        }
        if (e.getActionCommand().equals(REMOVE_PROPOSITION)) {
            transitionSystem.getStructureBean().removeProposition(choosePropositionComboBoxModel.selectedPropostionName);
            chooseProposition.setSelectedItem(null);
            chooseProposition.repaint();
        }
        if (e.getActionCommand().equals(RENAME_PROPOSITION)) {
            String name = JOptionPane.showInputDialog(this, "name: ");
            if (name != null) {
                if (transitionSystem.getStructureBean().getProposition(name) == null) transitionSystem.getStructureBean().getProposition(choosePropositionComboBoxModel.selectedPropostionName).setName(name);
                chooseProposition.setSelectedItem(name);
                chooseProposition.repaint();
            }
        }
        if (e.getActionCommand().equals(TO_IN_LIST)) {
            for (Object vertex : outList.getSelectedValues()) getProposition().addVertex((TransitionSystemVertex) vertex);
            inList.clearSelection();
            outList.clearSelection();
        }
        if (e.getActionCommand().equals(TO_OUT_LIST)) {
            for (Object vertex : inList.getSelectedValues()) getProposition().removeVertex((TransitionSystemVertex) vertex);
            inList.clearSelection();
            outList.clearSelection();
        }
        if (e.getActionCommand().equals(ALL_TO_IN_LIST)) {
            for (TransitionSystemVertex vertex : transitionSystem.getGraph().vertexSet()) {
                if (!getProposition().containsVertex(vertex)) getProposition().addVertex(vertex);
            }
            inList.clearSelection();
            outList.clearSelection();
        }
        if (e.getActionCommand().equals(ALL_TO_OUT_LIST)) {
            for (TransitionSystemVertex vertex : new ArrayList<TransitionSystemVertex>(getProposition().getVertices())) getProposition().removeVertex(vertex);
            inList.clearSelection();
            outList.clearSelection();
        }
    }

    protected void updateControls() {
        if (getProposition() == null) {
            removeProposition.setEnabled(false);
            renameProposition.setEnabled(false);
            inList.setEnabled(false);
            outList.setEnabled(false);
            toInList.setEnabled(false);
            toOutList.setEnabled(false);
            alltoInList.setEnabled(false);
            alltoOutList.setEnabled(false);
        } else {
            removeProposition.setEnabled(true);
            renameProposition.setEnabled(true);
            inList.setEnabled(true);
            outList.setEnabled(true);
            if (inList.getSelectedValues().length == 0) toOutList.setEnabled(false); else toOutList.setEnabled(true);
            if (outList.getSelectedValues().length == 0) toInList.setEnabled(false); else toInList.setEnabled(true);
            if (inList.getModel().getSize() == 0) alltoOutList.setEnabled(false); else alltoOutList.setEnabled(true);
            if (outList.getModel().getSize() == 0) alltoInList.setEnabled(false); else alltoInList.setEnabled(true);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        updateControls();
    }

    public void itemStateChanged(ItemEvent e) {
        currentProposition = null;
        ((InOutPropositionListModel) inList.getModel()).selectedPropositionChanged();
        ((InOutPropositionListModel) outList.getModel()).selectedPropositionChanged();
        updateControls();
    }
}
