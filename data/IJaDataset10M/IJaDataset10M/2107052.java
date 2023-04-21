package es.eucm.eadventure.editor.control.tools.conversation;

import java.util.List;
import es.eucm.eadventure.common.data.chapter.conversation.line.ConversationLine;
import es.eucm.eadventure.common.data.chapter.conversation.node.ConversationNode;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.tools.Tool;

public class AddNamesTagInConversationLines extends Tool {

    private List<ConversationNode> nodeList;

    public AddNamesTagInConversationLines(List<ConversationNode> nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public boolean doTool() {
        for (ConversationNode node : nodeList) {
            for (int i = 0; i < node.getLineCount(); i++) {
                ConversationLine cl = node.getLine(i);
                String text = cl.getText();
                if (!text.contains("[]")) {
                    if (text.startsWith("#:*")) {
                        text = text.subSequence(0, 3) + " [] " + text.subSequence(4, text.length());
                    } else if (text.startsWith("#O") || text.startsWith("#!")) text = text.subSequence(0, 2) + " [] " + text.subSequence(3, text.length()); else text = "[] " + text;
                    cl.setText(text);
                }
            }
        }
        Controller.getInstance().updatePanel();
        return true;
    }

    @Override
    public boolean redoTool() {
        return doTool();
    }

    @Override
    public boolean undoTool() {
        for (ConversationNode node : nodeList) {
            for (int i = 0; i < node.getLineCount(); i++) {
                ConversationLine cl = node.getLine(i);
                String text = cl.getText();
                if (text.contains(" [] ")) cl.setText(text.replace(" [] ", ""));
                if (text.contains("[] ")) cl.setText(text.replace("[] ", ""));
            }
        }
        Controller.getInstance().updatePanel();
        return true;
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public boolean combine(Tool other) {
        return false;
    }
}
