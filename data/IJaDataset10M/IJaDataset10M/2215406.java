package es.eucm.eadventure.common.data.chapter.conversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import es.eucm.eadventure.common.data.HasId;
import es.eucm.eadventure.common.data.chapter.conversation.node.ConversationNode;

/**
 * Implements Tree and Graph conversations
 */
public abstract class Conversation implements Cloneable, HasId {

    /**
     * Constant for tree conversations.
     */
    public static final int TREE = 0;

    /**
     * Constant for graph conversations.
     */
    public static final int GRAPH = 1;

    /**
     * Type of the conversation.
     */
    private int conversationType;

    /**
     * Reference name of the conversation
     */
    private String conversationId;

    /**
     * Root of the conversation
     */
    private ConversationNode root;

    /**
     * Constructor
     * 
     * @param conversationType
     *            Type of the conversation
     * @param conversationId
     *            Identifier of the conversation
     * @param root
     *            Root node (start) of the conversation
     */
    protected Conversation(int conversationType, String conversationId, ConversationNode root) {
        this.conversationType = conversationType;
        this.conversationId = conversationId;
        this.root = root;
    }

    /**
     * Returns the type of the conversation.
     * 
     * @return Conversation's type
     */
    public int getType() {
        return conversationType;
    }

    /**
     * Returns the name of the conversation.
     * 
     * @return Conversation's name
     */
    public String getId() {
        return conversationId;
    }

    /**
     * Returns the initial node of the conversation, the one which starts the
     * conversation.
     * 
     * @return First node of the conversation
     */
    public ConversationNode getRootNode() {
        return root;
    }

    /**
     * Sets the a new identifier for the conversation.
     * 
     * @param id
     *            New identifier
     */
    public void setId(String id) {
        this.conversationId = id;
    }

    public List<ConversationNode> getAllNodes() {
        List<ConversationNode> nodes = new ArrayList<ConversationNode>();
        getAllNodes(root, nodes);
        return nodes;
    }

    private void getAllNodes(ConversationNode firstNode, List<ConversationNode> nodes) {
        for (int i = -1; i < firstNode.getChildCount(); i++) {
            ConversationNode child = null;
            if (i == -1) child = firstNode; else child = firstNode.getChild(i);
            boolean isInList = false;
            for (ConversationNode aNode : nodes) {
                if (aNode == child) {
                    isInList = true;
                    break;
                }
            }
            if (!isInList) {
                nodes.add(child);
                getAllNodes(child, nodes);
            }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Conversation c = (Conversation) super.clone();
        c.conversationId = (conversationId != null ? new String(conversationId) : null);
        c.conversationType = conversationType;
        HashMap<ConversationNode, ConversationNode> clonedNodes = new HashMap<ConversationNode, ConversationNode>();
        c.root = (root != null ? (ConversationNode) root.clone() : null);
        clonedNodes.put(root, c.root);
        List<ConversationNode> nodes = new ArrayList<ConversationNode>();
        List<ConversationNode> visited = new ArrayList<ConversationNode>();
        nodes.add(root);
        while (!nodes.isEmpty()) {
            ConversationNode temp = nodes.get(0);
            ConversationNode cloned = clonedNodes.get(temp);
            nodes.remove(0);
            visited.add(temp);
            for (int i = 0; i < temp.getChildCount(); i++) {
                ConversationNode tempCloned = clonedNodes.get(temp.getChild(i));
                if (tempCloned == null) {
                    tempCloned = (ConversationNode) temp.getChild(i).clone();
                    clonedNodes.put(temp.getChild(i), tempCloned);
                }
                cloned.addChild(tempCloned);
                if (!visited.contains(temp.getChild(i)) && !nodes.contains(temp.getChild(i))) nodes.add(temp.getChild(i));
            }
        }
        return c;
    }
}
