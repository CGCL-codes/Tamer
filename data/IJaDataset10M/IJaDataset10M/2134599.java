package de.uka.ipd.Ogre.Compiler.Graph;

import java.util.ArrayList;
import java.util.Iterator;

public class MessageHandler {

    public static final int AMBIGUOUS_HEAD = 0;

    public static final int REFERENCE_MISSING = 1;

    public static final int TYPE_MISSING = 2;

    public static final int ROLE_MISSING = 3;

    public static final int ATTRIBUTE_MISSING = 4;

    public static final int NEGATIVE_ATTRIBUTE = 5;

    public static final int REDEFINED_NAME = 100;

    private ArrayList<Message> messages = new ArrayList<Message>();

    public void addMessage(int t, String m, GraphElement e) {
        messages.add(new Message(t, m, e));
    }

    public boolean process() {
        boolean ret = false;
        for (Iterator<Message> it = messages.iterator(); it.hasNext(); ) {
            StringBuffer sb = new StringBuffer();
            Message message = it.next();
            switch(message.getType()) {
                case AMBIGUOUS_HEAD:
                    sb.append("More than one head defined in superedge: ");
                    break;
                case REFERENCE_MISSING:
                    sb.append("Undefined reference: ");
                    break;
                case TYPE_MISSING:
                    sb.append("No type defined while using strict typing: ");
                    break;
                case ROLE_MISSING:
                    sb.append("Roles enforced, but no role defined for: " + message.getMessage());
                    break;
                case ATTRIBUTE_MISSING:
                    sb.append("Expected another attribute after \",\": ");
                    break;
                case NEGATIVE_ATTRIBUTE:
                    sb.append("Attribut with value may not have \"!\": ");
                    break;
                case REDEFINED_NAME:
                    sb.append("Name has already been defined, renaming" + message.getMessage() + ": ");
                    break;
            }
            if (message.getType() < 100) {
                ret = true;
                sb.append(generateDefinition(message));
                System.err.println("ERROR: " + sb.toString());
            } else {
                sb.append(generateDefinition(message));
                System.out.println("WARNING: " + sb.toString());
            }
        }
        return ret;
    }

    private String generateDefinition(Message message) {
        StringBuffer sb = new StringBuffer();
        GraphElement el = message.getElement();
        sb.append("\"");
        sb.append(el.getName());
        if (el.getTypeName() != null) sb.append(":" + el.getTypeName());
        if (!el.getValue().equals("")) sb.append("[" + el.getValue() + "]");
        sb.append("\"");
        sb.append(" (line:" + message.getLine() + ",Column:" + message.getColumn() + ")");
        return sb.toString();
    }
}
