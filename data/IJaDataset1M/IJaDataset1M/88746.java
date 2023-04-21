package messy.tool;

import messy.event.*;
import messy.base.*;

/**
* Chat Tool
* 
 * @author Cohan Sujay Carlos
 * @version 1.00
 * @see java.lang.Object
**/
public class ATool implements ToolInterface {

    public void launch() {
    }

    public void close() {
    }

    public void write(String str) {
        if (tc_ != null) tc_.write(str);
    }

    public boolean isId(String id) {
        return "a".equals(id);
    }

    public void setToolController(ToolController tc) {
        tc_ = tc;
    }

    public void messageReceived(MessageEvent e) {
    }

    public void messageSent(MessageEvent e) {
    }

    public void connectionClosed(MessageEvent e) {
    }

    public void connectionEstablished(MessageEvent e) {
    }

    /**
	* A printable string representation of the state of the object.
	* @return The string representation of the state of the object.
	* @see java.lang.Object
	**/
    public String toString() {
        return this.getClass().getName();
    }

    ToolController tc_ = null;

    /**
	* The main method runs a simple test of functionality.
	* @return void
	* @param argv The array of command line parameters.
	**/
    public static void main(String argv[]) {
        System.out.println("The main method is empty.");
    }
}
