package org.aubit4gl.remote_client.connection.command;

import org.aubit4gl.remote_client.connection.ClientUICommand;
import org.w3c.dom.Element;

/**
 * Representation of 4gl command CloseForm
 * TODO Document it
 * TODO Test it
 * TODO Review the code
 * TODO Add to command table
 * @author S�rgio Ferreira
 */
public class CloseWindow implements ClientUICommand {

    private String window;

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    public void execute() {
    }

    public String getXml() {
        return null;
    }

    public void initFromDom(Element dom) {
        window = dom.getAttribute("WINDOW");
    }
}
