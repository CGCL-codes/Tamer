package edu.ucsd.ncmir.jinx.events.gui.workspace;

import edu.ucsd.ncmir.asynchronous_event.AsynchronousEvent;
import edu.ucsd.ncmir.jinx.gui.workspace.JxWorkspace;
import edu.ucsd.ncmir.spl.minixml.Element;

public class JxCreateToolXMLElementEvent extends AsynchronousEvent {

    private Element _element;

    public JxCreateToolXMLElementEvent(JxWorkspace workspace, Element element) {
        super(workspace);
        this._element = element;
    }

    public synchronized void addElement(Element element) {
        this._element.addContent(element);
    }
}
