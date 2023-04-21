package de.mogwai.kias;

import java.util.Vector;
import de.mogwai.kias.forms.FormContext;

/**
 * A CommandList is a collection of Command objects.
 * 
 * The CommandList holds the Commands and is also responsible for creation of
 * internationalized commands. For this, it provides factory methods for Command
 * object creation.
 * 
 * @author Mirko Sertic <mail@mirkosertic.de>
 */
public class CommandList extends Vector<Command> {

    private FormContext context;

    /**
	 * Constructor.
	 * 
	 * For internationalization, the resource bundle is taken from the
	 * FormContext object given by this constructor.
	 * 
	 * @param aContext
	 *            the context the CommandList belongs to
	 */
    public CommandList(FormContext aContext) {
        context = aContext;
    }

    /**
	 * Add a command to the list.
	 * 
	 * @param aText
	 *            the text decribing the command in plain text
	 * @param aCommand
	 *            the command to be executed. Matches a method name of the
	 *            controller.
	 * @return the created command object
	 */
    public Command addPlainText(String aText, String aCommand) {
        Command theError = new Command(aText, aCommand);
        add(theError);
        return theError;
    }

    /**
	 * Add a command to the list.
	 * 
	 * @param aKey
	 *            the key used to retrieve the description from the resource
	 *            bundle.
	 * @param aCommand
	 *            the command to be executed. Matches a method name of the
	 *            controller.
	 * @return the created command object
	 */
    public Command addResource(String aKey, String aCommand) {
        Command theError = new Command(context.getResourceFromKey(aKey), aCommand);
        add(theError);
        return theError;
    }
}
