package org.vesuf.runtime.presentation.html;

import org.vesuf.model.presentation.*;
import org.vesuf.model.uml.behavior.statemachines.Event;
import org.vesuf.runtime.uml.foundation.core.*;
import org.vesuf.runtime.uml.foundation.vepl.*;
import org.vesuf.runtime.uml.behavior.statemachines.*;
import org.vesuf.runtime.presentation.*;
import java.io.*;
import java.util.*;

/**
 *	ButtonEventDelegate presents a submit button of a form,
 *  that is associated to a specific event.
 *
 *  @property event	The name of the event.
 *  @property label	The label of the button.
 *      Uses event name, if not specified.
 */
public class ButtonEventDelegate extends PartInstance implements IHTMLPartInstance, IDelegateInstance {

    /** The button event delegate controller. */
    protected ButtonEventDelegateController controller;

    /** The html text (cached). */
    protected String text;

    /**
	 *	Create a new ButtonEventDelegate.
	 *  @param part	The part of this instance.
	 *  @param runtime	The presentation runtime.
	 *  @param parent	The parent part instance (if any).
	 *  @param pathinstance	The path instance to use as start for resolving
	 *    the path of the part. May already contain the complete path instance
	 *    eg. for a root part, or parts like panel, that have the same
	 *    path as its parent.
	 */
    public ButtonEventDelegate(IPart part, PresentationRuntime runtime, IPartInstance parent, IPathElementInstance pathinstance) {
        super(part, runtime, parent, pathinstance);
        this.text = part.getProperty("label");
        if (this.text == null) {
            text = getPart().getProperty("event");
        }
        text = "<INPUT type=\"submit\"" + " name=\"" + part.getPartname() + "\" value=\"" + text + "\">";
        controller = new ButtonEventDelegateController(this);
    }

    /**
	 *  Update the representation.
	 *  May not be applicable eg. to html delegates.
	 */
    public void update() {
    }

    /**
	 *  Flush the value(s) to the model.
	 *  @param args	Arguments to the flush operation.
	 *    The arguments are implementation specific
	 *    (eg. gui-delegates may not need args,
	 *    html-delegates may get a http-request as arg).
	 */
    public void flush(Object args) {
        controller.handleAction(args);
    }

    /**
	 *  Is the state valid.
	 *  @return True, if state is valid.
	 */
    public boolean isValid() {
        return controller.isValid();
    }

    /**
	 *  Get the error.
	 *  @return The error. Null when valid.
	 */
    public Exception getError() {
        return controller.getError();
    }

    /**
	 *  Dispose this part. Remove all listeners.
	 */
    public void dispose() {
        controller.dispose();
    }

    /**
	 *  Print this component to the writer.
	 */
    public void printHTML(PrintWriter writer) {
        writer.print(text);
        if (!isValid()) {
            if (getError() instanceof ObjectAccessException) {
                writer.print(((ObjectAccessException) getError()).getException().getMessage());
            } else {
                writer.print(getError().getMessage());
            }
        }
    }

    /**
	 *  The button event delegate controller.
	 */
    public class ButtonEventDelegateController extends UIController {

        /**
		 *  Create a new button delegate controller.
		 */
        public ButtonEventDelegateController(IPartInstance partinstance) {
            super(partinstance, false);
        }

        /**
		 *  Process client request.
		 *  @param parameters	The requests name / value pairs.
		 *    If a parameter has more than a single value,
		 *    a string array is stored.
		 *  @throws Exception, when the request could not be processed.
		 */
        public void action(Object parameters) throws Exception {
            if (((Hashtable) parameters).get(getPart().getPartname()) != null) {
                Event event = (Event) getRuntime().getApplication().getNavigationRuntime().getModel().getOwnedElement(getPart().getProperty("event"));
                IStateVertexInstance context = (IStateVertexInstance) getPresentationRuntime().getContext(ButtonEventDelegate.this);
                IEventInstance eventinstance = new EventInstance(event, getRuntime().getApplication().getNavigationRuntime(), new Object[0], (StateMachineInstance) context.getStateMachineInstance());
                eventinstance.trigger(context);
            }
        }
    }
}
