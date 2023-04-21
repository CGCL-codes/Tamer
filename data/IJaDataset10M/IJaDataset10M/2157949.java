package com.bbn.vessel.author.conversationEditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.jdom.Element;
import com.bbn.vessel.author.addStrategyWizard.modalDecisionSequence.InteractionTreeNode;
import com.bbn.vessel.author.addStrategyWizard.modalDecisionSequence.LinkTreeNode;
import com.bbn.vessel.author.addStrategyWizard.modalDecisionSequence.ModalDecisionSequence;
import com.bbn.vessel.author.addStrategyWizard.modalDecisionSequence.ModalDecisionSequencePane;
import com.bbn.vessel.author.addStrategyWizard.modalDecisionSequence.ResponseTreeNode;
import com.bbn.vessel.author.logicEditor.editor.LogicEditorFrame;
import com.bbn.vessel.author.models.Graph;
import com.bbn.vessel.author.models.GraphNode;
import com.bbn.vessel.author.models.GroupNode;
import com.bbn.vessel.author.situationEditor.Situation;
import com.bbn.vessel.author.situationEditor.SituationEditor;
import com.bbn.vessel.author.workspace.AuthoringTool;
import com.bbn.vessel.author.workspace.FileManager;
import com.bbn.vessel.author.workspace.Workspace;
import com.bbn.vessel.author.workspace.dataPointer.DataChangeListener;
import com.bbn.vessel.author.workspace.dataPointer.DataChangedEvent;
import com.bbn.vessel.author.workspace.dataPointer.DataChangedEventType;
import com.bbn.vessel.core.util.PrettyPrintable;
import com.bbn.vessel.core.util.XMLHelper;

/**
 * an editor for conversations. this editor edits the graph as changes are made
 * at the high level
 *
 * @author jostwald
 *
 */
@SuppressWarnings("serial")
public class ConversationEditor extends AuthoringTool implements PrettyPrintable {

    private final HashMap<ModalDecisionSequence, ModalDecisionSequencePane> conversationPanes = new HashMap<ModalDecisionSequence, ModalDecisionSequencePane>();

    private JComboBox situationComboBox;

    private final JPanel topPanel = new JPanel();

    private static final String filename = "conversations.xml";

    private static final String TAG_CONVERSATIONS = "conversations";

    /**
     * name of this editor, as it appears in tools.xml ("Conversation Editor")
     */
    public static final String NAME = "Conversation Editor";

    Map<Integer, ModalDecisionSequence> conversationsByGroupId = new HashMap<Integer, ModalDecisionSequence>();

    ModalDecisionSequence currentConversation = null;

    private JMenu editMenu;

    public ConversationEditor(Workspace workspace, String name, Integer priority) {
        super(workspace, name, priority);
        if (!workspace.isEmbedded()) {
            buildGui();
        }
    }

    private void buildGui() {
        topPanel.removeAll();
        topPanel.setLayout(new BorderLayout());
        Box northPane = new Box(BoxLayout.Y_AXIS);
        if (currentConversation == null) {
            if (conversationsByGroupId.size() > 0) {
                currentConversation = conversationsByGroupId.values().iterator().next();
            }
        }
        Box conversationSelectorPane = buildConversationSelectorPane();
        northPane.add(conversationSelectorPane);
        topPanel.add(northPane, BorderLayout.NORTH);
        if (currentConversation == null) {
            getWorkspace().validate();
            return;
        }
        Box situationSelectorPane = buildSituationSelectorPane();
        northPane.add(situationSelectorPane);
        ModalDecisionSequencePane conversationPane = getConversationPane(currentConversation);
        topPanel.add(conversationPane, BorderLayout.CENTER);
    }

    private ModalDecisionSequencePane getConversationPane(ModalDecisionSequence c) {
        ModalDecisionSequencePane p;
        if (conversationPanes.containsKey(c)) {
            p = conversationPanes.get(c);
        } else {
            p = new ModalDecisionSequencePane("Conversation", c, this, false, getWorkspace(), c.getSituation(), null);
            conversationPanes.put(c, p);
        }
        return p;
    }

    private Box buildSituationSelectorPane() {
        Box situationSelectorPane = new Box(BoxLayout.X_AXIS);
        situationSelectorPane.add(new JLabel("Situation:"));
        Vector<Situation> sits = getSituationsForComboBox();
        situationComboBox = new JComboBox(sits);
        Situation sit = currentConversation.getSituation();
        situationComboBox.setSelectedItem(sit);
        situationComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Situation selectedSituation = (Situation) situationComboBox.getSelectedItem();
                Situation currentSituation = currentConversation.getSituation();
                if (((currentSituation != null) && (currentSituation.equals(selectedSituation))) || ((currentSituation == null) && (selectedSituation == null))) {
                    return;
                }
                if (currentSituation != null) {
                    currentSituation.removeConversation(currentConversation);
                }
                if (selectedSituation != null) {
                    selectedSituation.addConversation(currentConversation);
                }
                currentConversation.setSituation(selectedSituation);
                conversationPanes.remove(currentConversation);
                buildGui();
                setModified(true);
            }
        });
        Dimension comboBoxSize = new Dimension(300, 26);
        situationComboBox.setPreferredSize(comboBoxSize);
        situationSelectorPane.add(situationComboBox);
        situationSelectorPane.add(Box.createHorizontalGlue());
        findSituationEditor().addDataListener(new DataChangeListener<Situation>() {

            @Override
            public void dataChanged(DataChangedEvent<Situation> evt) {
                if (currentConversation == null) {
                    return;
                }
                Vector<Situation> situationsForComboBox = getSituationsForComboBox();
                situationComboBox.setModel(new DefaultComboBoxModel(situationsForComboBox));
                Situation currentSituation = currentConversation.getSituation();
                if ((currentSituation != null) && (evt.getType() == DataChangedEventType.DELETED) && (evt.getSource().equals(currentSituation))) {
                    currentConversation.setSituation(null);
                    situationComboBox.setSelectedIndex(0);
                    setModified(true);
                } else {
                    situationComboBox.setSelectedItem(currentSituation);
                }
                situationComboBox.revalidate();
            }
        });
        return situationSelectorPane;
    }

    private Vector<Situation> getSituationsForComboBox() {
        Vector<Situation> sits = new Vector<Situation>(findSituationEditor().getAllData(Situation.class));
        sits.add(0, null);
        return sits;
    }

    private SituationEditor findSituationEditor() {
        final SituationEditor situationEditor = (SituationEditor) getWorkspace().getToolForName(SituationEditor.NAME);
        return situationEditor;
    }

    private Box buildConversationSelectorPane() {
        Box conversationSelectorPane = new Box(BoxLayout.X_AXIS);
        conversationSelectorPane.add(new JLabel("Conversation:"));
        JComboBox conversationComboBox = new JComboBox(conversationsByGroupId.values().toArray(new ModalDecisionSequence[0]));
        if (currentConversation != null) {
            conversationComboBox.setSelectedItem(currentConversation);
        }
        conversationComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                currentConversation = (ModalDecisionSequence) cb.getSelectedItem();
                getConversationPane(currentConversation).getTreeTable().getTreeSelectionModel().setSelectionPath(null);
                getWorkspace().populateMenuBar();
                buildGui();
            }
        });
        Dimension comboBoxSize = new Dimension(300, 26);
        conversationComboBox.setPreferredSize(comboBoxSize);
        conversationSelectorPane.add(conversationComboBox);
        conversationSelectorPane.add(new JButton(new AbstractAction("New Conversation") {

            @Override
            public void actionPerformed(ActionEvent e) {
                doNewConversation();
            }
        }));
        if (currentConversation != null) {
            conversationSelectorPane.add(new JButton(new AbstractAction("Delete Conversation") {

                @Override
                public void actionPerformed(ActionEvent e) {
                    removeConversation(currentConversation);
                }
            }));
        }
        conversationSelectorPane.add(Box.createHorizontalGlue());
        return conversationSelectorPane;
    }

    protected void doNewConversation() {
        getWorkspace().getToolForName(LogicEditorFrame.NAME);
        String groupName = JOptionPane.showInputDialog("Enter Conversation Name");
        if ((groupName == null) || (groupName.length() == 0)) {
            return;
        }
        Graph graph = getWorkspace().getGraph();
        final GroupNode conversationGroup = ModalDecisionSequencePane.createConversationGroupNode(groupName, "Conversation", graph, null);
        ModalDecisionSequence newConversation = new ModalDecisionSequence(conversationGroup, getWorkspace().getId());
        conversationsByGroupId.put(conversationGroup.getDataElementId(), newConversation);
        currentConversation = newConversation;
        ModalDecisionSequencePane cp = getConversationPane(newConversation);
        cp.getTreeTable().getTreeSelectionModel().setSelectionPath(null);
        buildGui();
        getWorkspace().validate();
        topPanel.repaint();
        setModified(true);
    }

    @Override
    public Component getComponent() {
        return topPanel;
    }

    @Override
    public void load(FileManager fm) {
        try {
            Element rootElement = fm.load(filename);
            List<Element> conversationElements = XMLHelper.getChildren(rootElement, ModalDecisionSequence.TAG_CONVERSATION);
            for (Element conversationElement : conversationElements) {
                ModalDecisionSequence newConvo = new ModalDecisionSequence(conversationElement, getWorkspace().getId());
                if (newConvo.getGroupNode() != null) {
                    conversationsByGroupId.put(newConvo.getId(), newConvo);
                } else {
                    String message = "couldn't load conversation " + newConvo.getId() + "!!!";
                    logger.error(message, new Exception());
                    JOptionPane.showMessageDialog(topPanel, new Exception(message));
                }
            }
            if (!getWorkspace().isEmbedded()) {
                buildGui();
            }
        } catch (FileNotFoundException e) {
        } catch (Exception e) {
            popupError("Unable to load " + filename, e);
        }
    }

    @Override
    public void save(FileManager fm, boolean autosave) {
        try {
            Element rootElement = new Element(TAG_CONVERSATIONS);
            for (ModalDecisionSequence conv : conversationsByGroupId.values()) {
                rootElement.addContent(conv.toXML());
            }
            fm.save(filename, rootElement, null);
            if (!autosave) {
                setModified(false);
            }
        } catch (IOException e) {
            popupError("Unable to save", e);
        }
    }

    @Override
    public boolean wantsToLoad(FileManager fm) {
        try {
            return fm.listFiles().contains(filename);
        } catch (IOException e) {
            popupError("Unable to check for " + filename, e);
            return false;
        }
    }

    @Override
    public void populateMenuBar() {
        populateEditMenu(getWorkspace().getMenu("Edit"));
    }

    private void populateEditMenu(JMenu editMenu) {
        if (currentConversation == null) return;
        editMenu.removeAll();
        final ModalDecisionSequencePane conversationPane = getConversationPane(currentConversation);
        final AbstractAction copyAction = conversationPane.getCopyAction();
        editMenu.add(copyAction);
        copyAction.setEnabled(false);
        final AbstractAction pasteAsLinkAction = conversationPane.getPasteAsLinkAction();
        editMenu.add(pasteAsLinkAction);
        pasteAsLinkAction.setEnabled(false);
    }

    /**
     * get the Conversation corresponding to a group in the graph
     *
     * @param fromElement
     *            a group node that corresponds to a conversation
     * @return a conversation
     */
    public ModalDecisionSequence getConversationForGroup(GraphNode fromElement) {
        return conversationsByGroupId.get(fromElement.getDataElementId());
    }

    /**
     * start considering a group to be a conversation
     *
     * @param groupNode
     *            a group that is a conversation. the conversation editor will
     *            freely assume that it has constructed this group itself and
     *            that all its naming and wiring conventions will be respected.
     *            use of this method on hand generated groups is at your own
     *            risk
     */
    public void addConversationForGroup(GroupNode groupNode) {
        conversationsByGroupId.put(groupNode.getDataElementId(), new ModalDecisionSequence(groupNode, getWorkspace().getId()));
        buildGui();
        getWorkspace().validate();
    }

    @Override
    public void setModified(boolean modified) {
        super.setModified(modified);
        if (modified) {
            getWorkspace().getToolForName(LogicEditorFrame.NAME).setModified(true);
        }
    }

    /**
     * remove the conversation from the conversation editor and the
     * corresponding group from the graph
     *
     * @param c
     *            the conversation
     */
    public void removeConversation(ModalDecisionSequence c) {
        GroupNode conversationGroup = c.getGroupNode();
        conversationsByGroupId.remove(conversationGroup.getDataElementId());
        conversationGroup.getGraph().removeGraphElement(conversationGroup);
        if (c.equals(currentConversation)) {
            currentConversation = null;
        }
        buildGui();
        setModified(true);
    }

    /**
     * update what group corresponds to a conversation
     *
     * @param conversation
     *            the conversation
     * @param newGroupNode
     *            the group
     */
    public void setConversationGroupNode(ModalDecisionSequence conversation, GroupNode newGroupNode) {
        conversationsByGroupId.remove(conversation.getId());
        conversation.setGroupNode(newGroupNode);
        conversationsByGroupId.put(conversation.getId(), conversation);
    }

    @Override
    public String printableView() {
        StringBuilder sb = new StringBuilder();
        for (ModalDecisionSequence c : conversationsByGroupId.values()) {
            sb.append("<p><font size=\"5\"><b><a name=\"convo:");
            sb.append(c.getName());
            sb.append("\">");
            sb.append(c.getName());
            sb.append("</a></b></font>");
            sb.append("<ul>");
            JXTreeTable tree = getConversationPane(c).getTreeTable();
            TreePath rootPath = tree.getPathForRow(0);
            InteractionTreeNode root = (InteractionTreeNode) rootPath.getPath()[0];
            addConversationTreeToStringBuffer(root, sb);
            sb.append("</ul>");
        }
        return sb.toString();
    }

    private void addConversationTreeToStringBuffer(InteractionTreeNode root, StringBuilder sb) {
        sb.append("<li>");
        sb.append("<font color=\"green\"><a name=\"interaction:");
        sb.append(root.hashCode());
        sb.append("\"><b>NPC:</b>  ");
        sb.append(root.getInteractionPromptText());
        sb.append("</a></font>");
        sb.append("<ul>");
        for (int i = 0; i < root.getChildCount(); i++) {
            ResponseTreeNode responseNode = (ResponseTreeNode) root.getChildAt(i);
            sb.append("<li>");
            sb.append("<b>PLAYER:</b>  ");
            sb.append(responseNode.getResponseText());
            sb.append("<ul>");
            if (responseNode.getChildCount() > 0) {
                TreeTableNode child = responseNode.getChildAt(0);
                if (child instanceof InteractionTreeNode) {
                    addConversationTreeToStringBuffer((InteractionTreeNode) child, sb);
                } else {
                    LinkTreeNode linkNode = (LinkTreeNode) child;
                    InteractionTreeNode linkTarget = linkNode.getLinkTarget();
                    sb.append("<li><i><a href=\"#interaction:");
                    sb.append(linkTarget.hashCode());
                    sb.append("\">");
                    sb.append(linkTarget.getInteractionPromptText());
                    sb.append("</a></i></li>");
                }
            }
            sb.append("</ul></li>");
        }
        sb.append("</ul></li>");
    }
}
