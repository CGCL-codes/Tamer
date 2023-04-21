package ursus.server.plugins.core;

import edu.emory.mathcs.backport.java.util.concurrent.ConcurrentHashMap;
import ursus.common.Listener;
import ursus.common.Communicator;
import java.util.Map;

/**
 * This is the instance of Listener that handles all communications for communications
 * of type "text".  This TextListener maps TextCommand objects according the value of
 * their getName() method returns.
 * 
 * @author Anthony
 */
public class TextListener implements Listener {

    /**
     * final static variable that defines the type of communication this listener handles.
     */
    public static final String TYPE = "text";

    protected Map actions;

    public TextListener() {
        actions = new ConcurrentHashMap();
    }

    public String getType() {
        return TYPE;
    }

    /**
     * When a communicator executes TextListener the object passed will
     * be the full String of the command the communicator issued. For example if the 
     * communicator is a remote client and they issue the command: "echo hello",
     * the Object passed will be a String that's value is "echo hello"
     *
     * @param entity The communicator that issued a communication of type "text" that is 
     * now being handled by this listener.
     * @param object The full text of the command issued by a communicator.
     * 
     */
    public void execute(Communicator entity, Object object) {
        String content = (String) object;
        String command;
        int spaceIndex = content.indexOf(" ");
        if (spaceIndex != -1) command = content.substring(0, spaceIndex); else command = content;
        TextCommand a = (TextCommand) actions.get(command.toLowerCase());
        if (a != null) {
            String args = content.substring(content.indexOf(" ") + 1, content.length());
            a.execute(entity, args);
        } else {
            entity.write(TYPE, command + ": is not a valid command");
        }
    }

    /**
     * Adds a TextCommand to be mapped by this TextListener object
     *
     * @param textCommand to add to this TextListener object
     */
    public void addCommand(TextCommand textCommand) {
        actions.put(textCommand.getName(), textCommand);
    }
}
