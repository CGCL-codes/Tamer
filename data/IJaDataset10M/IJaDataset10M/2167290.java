package es.eucm.eadventure.editor.control.controllers.conversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import es.eucm.eadventure.common.auxiliar.AssetsConstants;
import es.eucm.eadventure.common.data.chapter.conversation.Conversation;
import es.eucm.eadventure.common.data.chapter.conversation.GraphConversation;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.conversation.node.ConversationNode;
import es.eucm.eadventure.common.data.chapter.conversation.node.ConversationNodeView;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.controllers.ConditionsController;
import es.eucm.eadventure.editor.control.controllers.EffectsController;
import es.eucm.eadventure.editor.control.controllers.Searchable;
import es.eucm.eadventure.editor.control.tools.conversation.DeleteConversationNodeTool;
import es.eucm.eadventure.editor.control.tools.conversation.LinkConversationNodeTool;
import es.eucm.eadventure.editor.data.support.VarFlagSummary;

public class GraphConversationDataControl extends ConversationDataControl {

    /**
     * Reference to the graph conversation.
     */
    private GraphConversation graphConversation;

    /**
     * A list with each conversation line conditions controller
     */
    private Map<ConversationNodeView, List<ConditionsController>> allConditions;

    /**
     * Constructor.
     * 
     * @param graphConversation
     *            Contained graph conversation
     */
    public GraphConversationDataControl(GraphConversation graphConversation) {
        this.graphConversation = graphConversation;
        storeAllConditions();
    }

    @Override
    public int getType() {
        return Controller.CONVERSATION_GRAPH;
    }

    @Override
    public String getId() {
        return graphConversation.getId();
    }

    @Override
    public ConversationNodeView getRootNode() {
        return graphConversation.getRootNode();
    }

    @Override
    public void updateAllConditions() {
        allConditions.clear();
        List<ConversationNodeView> nodes = getAllNodesViews();
        for (ConversationNodeView node : nodes) {
            ArrayList<ConditionsController> nodeConditions = new ArrayList<ConditionsController>();
            for (int i = 0; i < node.getLineCount(); i++) {
                nodeConditions.add(new ConditionsController(node.getLineConditions(i), (node.getType() == ConversationNodeView.DIALOGUE ? Controller.CONVERSATION_DIALOGUE_LINE : Controller.CONVERSATION_OPTION_LINE), Integer.toString(i)));
            }
            allConditions.put(node, nodeConditions);
        }
    }

    /**
     * Store all line conditions in allConditions
     */
    private void storeAllConditions() {
        allConditions = new HashMap<ConversationNodeView, List<ConditionsController>>();
        updateAllConditions();
    }

    /**
     * Returns the conditions controller associated to the given conversation
     * line
     * 
     * @param convLine
     * @return Conditions controller
     * 
     */
    public ConditionsController getLineConditionController(ConversationNodeView node, int line) {
        return (allConditions.get(node)).get(line);
    }

    @Override
    public int getConversationLineCount() {
        int lineCount = 0;
        List<ConversationNodeView> nodes = getAllNodesViews();
        for (ConversationNodeView node : nodes) lineCount += node.getLineCount();
        return lineCount;
    }

    @Override
    public int[] getAddableNodes(ConversationNodeView nodeView) {
        int[] addableNodes = null;
        if (nodeView.getType() == ConversationNodeView.DIALOGUE) addableNodes = new int[] { ConversationNodeView.DIALOGUE, ConversationNodeView.OPTION }; else if (nodeView.getType() == ConversationNodeView.OPTION) addableNodes = new int[] { ConversationNodeView.DIALOGUE };
        return addableNodes;
    }

    @Override
    public boolean canAddChild(ConversationNodeView nodeView, int nodeType) {
        boolean canAddChild = false;
        if (nodeView.getType() == ConversationNodeView.DIALOGUE && nodeView.isTerminal()) canAddChild = true;
        if (nodeView.getType() == ConversationNodeView.OPTION && nodeType == ConversationNodeView.DIALOGUE) canAddChild = true;
        return canAddChild;
    }

    @Override
    public boolean canLinkNode(ConversationNodeView nodeView) {
        boolean canLinkNode = false;
        if (nodeView != graphConversation.getRootNode()) {
            if (nodeView.getType() == ConversationNodeView.DIALOGUE && nodeView.isTerminal()) canLinkNode = true;
            if (nodeView.getType() == ConversationNodeView.OPTION) canLinkNode = true;
        }
        return canLinkNode;
    }

    @Override
    public boolean canDeleteLink(ConversationNodeView nodeView) {
        boolean canLinkNode = false;
        if (nodeView != graphConversation.getRootNode()) {
            if (nodeView.getType() == ConversationNodeView.DIALOGUE && nodeView.isTerminal()) canLinkNode = true;
            if (nodeView.getType() == ConversationNodeView.OPTION) canLinkNode = true;
        }
        return !canLinkNode && this.getAllNodesViews().size() > 1;
    }

    @Override
    public boolean canLinkNodeTo(ConversationNodeView fatherView, ConversationNodeView childView) {
        boolean canLinkNodeTo = false;
        if (fatherView != childView) {
            if (fatherView.getType() == ConversationNodeView.DIALOGUE && fatherView.isTerminal() && !isDirectFather(childView, fatherView)) canLinkNodeTo = true;
            if (fatherView.getType() == ConversationNodeView.OPTION && childView.getType() == ConversationNodeView.DIALOGUE) canLinkNodeTo = true;
        }
        return canLinkNodeTo;
    }

    @Override
    public boolean canDeleteNode(ConversationNodeView nodeView) {
        return nodeView != graphConversation.getRootNode();
    }

    @Override
    public boolean canMoveNode(ConversationNodeView nodeView) {
        return false;
    }

    @Override
    public boolean canMoveNodeTo(ConversationNodeView nodeView, ConversationNodeView hostNodeView) {
        return false;
    }

    @Override
    public boolean linkNode(ConversationNodeView fatherView, ConversationNodeView childView) {
        return controller.addTool(new LinkConversationNodeTool(this, fatherView, childView));
    }

    @Override
    public boolean deleteNode(ConversationNodeView nodeView) {
        return controller.addTool(new DeleteConversationNodeTool(nodeView, (GraphConversation) getConversation(), allConditions));
    }

    @Override
    public boolean moveNode(ConversationNodeView nodeView, ConversationNodeView hostNodeView) {
        return false;
    }

    /**
     * Returns a list with all the nodes in the conversation.
     * 
     * @return List with the nodes of the conversation
     */
    public List<ConversationNodeView> getAllNodesViews() {
        List<ConversationNode> nodes = graphConversation.getAllNodes();
        List<ConversationNodeView> nodeViews = new ArrayList<ConversationNodeView>();
        for (ConversationNode node : nodes) nodeViews.add(node);
        return nodeViews;
    }

    /**
     * Returns a list with all the nodes in the conversation.
     * 
     * @return List with the nodes of the conversation
     */
    public List<SearchableNode> getAllSearchableNodes() {
        List<ConversationNode> nodes = graphConversation.getAllNodes();
        List<SearchableNode> nodeViews = new ArrayList<SearchableNode>();
        for (ConversationNode node : nodes) nodeViews.add(new SearchableNode(node));
        return nodeViews;
    }

    /**
     * Returns if the given father has a direct line of dialogue nodes to get to
     * the child node.
     * 
     * @param fatherView
     *            Father node
     * @param childView
     *            Child node
     * @return True if the father is related to child following only dialogue
     *         nodes, false otherwise
     */
    private boolean isDirectFather(ConversationNodeView fatherView, ConversationNodeView childView) {
        boolean isDirectFather = false;
        if (fatherView.getType() == ConversationNodeView.DIALOGUE && childView.getType() == ConversationNodeView.DIALOGUE) {
            if (!fatherView.isTerminal()) {
                if (fatherView.getChildView(0) == childView) isDirectFather = true; else isDirectFather = isDirectFather(fatherView.getChildView(0), childView);
            }
        }
        return isDirectFather;
    }

    @Override
    public Object getContent() {
        return graphConversation;
    }

    @Override
    public String renameElement(String name) {
        boolean elementRenamed = false;
        String oldConversationId = graphConversation.getId();
        String references = String.valueOf(controller.countIdentifierReferences(oldConversationId));
        if (name != null || controller.showStrictConfirmDialog(TC.get("Operation.RenameConversationTitle"), TC.get("Operation.RenameElementWarning", new String[] { oldConversationId, references }))) {
            String newConversationId = name;
            if (name == null) newConversationId = controller.showInputDialog(TC.get("Operation.RenameConversationTitle"), TC.get("Operation.RenameConversationMessage"), oldConversationId);
            if (newConversationId != null && !newConversationId.equals(oldConversationId) && controller.isElementIdValid(newConversationId)) {
                graphConversation.setId(newConversationId);
                controller.replaceIdentifierReferences(oldConversationId, newConversationId);
                controller.getIdentifierSummary().deleteConversationId(oldConversationId);
                controller.getIdentifierSummary().addConversationId(newConversationId);
                elementRenamed = true;
            }
        }
        if (elementRenamed) return oldConversationId; else return null;
    }

    @Override
    public void updateVarFlagSummary(VarFlagSummary varFlagSummary) {
        List<ConversationNode> conversationNodes = graphConversation.getAllNodes();
        for (ConversationNode conversationNode : conversationNodes) {
            if (conversationNode.hasEffects()) EffectsController.updateVarFlagSummary(varFlagSummary, conversationNode.getEffects());
            for (int i = 0; i < conversationNode.getLineCount(); i++) {
                ConditionsController.updateVarFlagSummary(varFlagSummary, conversationNode.getLineConditions(i));
            }
        }
    }

    @Override
    public boolean isValid(String currentPath, List<String> incidences) {
        return isValidNode(graphConversation.getRootNode(), currentPath, incidences, new ArrayList<ConversationNode>());
    }

    @Override
    public int countAssetReferences(String assetPath) {
        int count = 0;
        List<ConversationNode> conversationNodes = graphConversation.getAllNodes();
        for (ConversationNode conversationNode : conversationNodes) {
            if (conversationNode.hasEffects()) count += EffectsController.countAssetReferences(assetPath, conversationNode.getEffects());
            for (int i = 0; i < conversationNode.getLineCount(); i++) {
                if (conversationNode.hasAudioPath(i)) {
                    String audioPath = conversationNode.getAudioPath(i);
                    if (audioPath.equals(assetPath)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    @Override
    public void getAssetReferences(List<String> assetPaths, List<Integer> assetTypes) {
        List<ConversationNode> conversationNodes = graphConversation.getAllNodes();
        for (ConversationNode conversationNode : conversationNodes) {
            if (conversationNode.hasEffects()) EffectsController.getAssetReferences(assetPaths, assetTypes, conversationNode.getEffects());
            for (int i = 0; i < conversationNode.getLineCount(); i++) {
                if (conversationNode.hasAudioPath(i)) {
                    String audioPath = conversationNode.getAudioPath(i);
                    boolean add = true;
                    for (String asset : assetPaths) {
                        if (asset.equals(audioPath)) {
                            add = false;
                            break;
                        }
                    }
                    if (add) {
                        int last = assetPaths.size();
                        assetPaths.add(last, audioPath);
                        assetTypes.add(last, AssetsConstants.CATEGORY_AUDIO);
                    }
                }
            }
        }
    }

    @Override
    public void deleteAssetReferences(String assetPath) {
        List<ConversationNode> conversationNodes = graphConversation.getAllNodes();
        for (ConversationNode conversationNode : conversationNodes) {
            if (conversationNode.hasEffects()) EffectsController.deleteAssetReferences(assetPath, conversationNode.getEffects());
            for (int i = 0; i < conversationNode.getLineCount(); i++) {
                if (conversationNode.hasAudioPath(i)) {
                    String audioPath = conversationNode.getAudioPath(i);
                    if (audioPath.equals(assetPath)) {
                        conversationNode.getLine(i).setAudioPath(null);
                    }
                }
            }
        }
    }

    @Override
    public int countIdentifierReferences(String id) {
        int count = 0;
        List<ConversationNode> conversationNodes = graphConversation.getAllNodes();
        for (ConversationNode conversationNode : conversationNodes) {
            if (conversationNode.getType() == ConversationNodeView.DIALOGUE) {
                for (int i = 0; i < conversationNode.getLineCount(); i++) {
                    ConversationLine conversationLine = conversationNode.getLine(i);
                    if (conversationLine.getName().equals(id)) count++;
                }
                if (conversationNode.hasEffects()) count += EffectsController.countIdentifierReferences(id, conversationNode.getEffects());
            }
        }
        for (List<ConditionsController> conditions : allConditions.values()) for (ConditionsController condition : conditions) count += condition.countIdentifierReferences(id);
        return count;
    }

    @Override
    public void replaceIdentifierReferences(String oldId, String newId) {
        List<ConversationNode> conversationNodes = graphConversation.getAllNodes();
        for (ConversationNode conversationNode : conversationNodes) {
            if (conversationNode.getType() == ConversationNodeView.DIALOGUE) {
                for (int i = 0; i < conversationNode.getLineCount(); i++) {
                    ConversationLine conversationLine = conversationNode.getLine(i);
                    if (conversationLine.getName().equals(oldId)) conversationLine.setName(newId);
                }
                if (conversationNode.hasEffects()) EffectsController.replaceIdentifierReferences(oldId, newId, conversationNode.getEffects());
            }
            for (List<ConditionsController> conditions : allConditions.values()) for (ConditionsController condition : conditions) condition.replaceIdentifierReferences(oldId, newId);
        }
    }

    @Override
    public void deleteIdentifierReferences(String id) {
        List<ConversationNode> conversationNodes = graphConversation.getAllNodes();
        for (ConversationNode conversationNode : conversationNodes) {
            if (conversationNode.getType() == ConversationNodeView.DIALOGUE) {
                int i = 0;
                while (i < conversationNode.getLineCount()) {
                    if (conversationNode.getLine(i).getName().equals(id)) conversationNode.removeLine(i); else i++;
                }
                if (conversationNode.hasEffects()) EffectsController.deleteIdentifierReferences(id, conversationNode.getEffects());
            }
            for (List<ConditionsController> conditions : allConditions.values()) for (ConditionsController condition : conditions) condition.deleteIdentifierReferences(id);
        }
    }

    @Override
    public boolean canBeDuplicated() {
        return true;
    }

    @Override
    public void recursiveSearch() {
        check(this.getId(), "ID");
        for (SearchableNode cnv : this.getAllSearchableNodes()) {
            cnv.recursiveSearch();
        }
    }

    @Override
    public Conversation getConversation() {
        return graphConversation;
    }

    @Override
    public void setConversation(Conversation conversation) {
        if (conversation instanceof GraphConversation) {
            graphConversation = (GraphConversation) conversation;
        }
    }

    @Override
    public List<Searchable> getPathToDataControl(Searchable dataControl) {
        List<Searchable> path = getPathFromChild(dataControl, this.getAllSearchableNodes());
        if (path != null) return path;
        if (dataControl == this) {
            path = new ArrayList<Searchable>();
            path.add(this);
            return path;
        }
        return null;
    }

    /**
     * @return the allConditions
     */
    public Map<ConversationNodeView, List<ConditionsController>> getAllConditions() {
        return allConditions;
    }

    @Override
    public List<ConversationNodeView> getAllNodes() {
        return this.getAllNodesViews();
    }
}
