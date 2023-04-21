package org.aubit4gl.remote_client.connection.command;

import org.aubit4gl.remote_client.connection.ClientUICommand;
import org.w3c.dom.Element;

public class ProgramStartup implements ClientUICommand {

    private String programName;

    private int id;

    public ProgramStartup() {
        super();
    }

    /**
	 * Constructor to build the command from attributes explicitly 
	 * 
	 * @param programName The name of the program that is starting
	 * @param id The ID of the program
	 */
    public ProgramStartup(String programName, int id) {
        super();
        this.programName = programName;
        this.id = id;
    }

    /**
	 * Constructor that receives the dom and put the data of the xml in each correct place 
	 * TODO : Not necessary. Don't know how to execute with argument using reflection.
	 * @param dom The dom where the data should be getted
	 */
    public ProgramStartup(Element dom) {
        initFromDom(dom);
    }

    /**
	 * Initialize the UI command from the corresponding element on the
	 * DOM.
	 * 
	 * @param dom The Document Object Model that contains the information
	 */
    public void initFromDom(Element dom) {
        programName = dom.getAttribute("PROGRAMNAME");
        id = Integer.parseInt(dom.getAttribute("ID"));
    }

    /**
	 * Build and return a Document Object Model that contains the
	 * representation of the UI command.
	 * 
	 * TODO : Just for testing purpose i am creating the DOM with text 
	 * 
	 * @return The XML as string representation of the UI command. 
	 */
    public String getXml() {
        StringBuffer xml = new StringBuffer();
        xml.append("<PROGRAMSTARTUP PROGRAMNAME=\"" + programName + "\" ID =\"" + id + "\"/>");
        return xml.toString();
    }

    /**
	 * TODO : Implement it. This should work with events
	 */
    public void execute() {
    }

    /**
	 * Convert the command to a string
	 */
    public String toString() {
        return "ProgramStartup[name=" + programName + ",id=" + id + "]";
    }

    public String getProgramName() {
        return programName;
    }

    public int getId() {
        return id;
    }
}
