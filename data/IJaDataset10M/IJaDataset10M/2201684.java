package com.foursoft.foureveredit.view.impl;

import java.util.HashSet;
import java.util.Set;
import com.foursoft.fourever.objectmodel.HTMLInstance;
import com.foursoft.fourever.objectmodel.Instance;
import com.foursoft.fourever.objectmodel.InstanceObserver;
import com.foursoft.foureveredit.controller.command.CheckpointCommand;
import com.foursoft.foureveredit.controller.command.SetFocusedInstanceCommand;
import com.foursoft.foureveredit.controller.command.SetFocusedLinkCommand;
import com.foursoft.foureveredit.controller.command.SetFocusedViewCommand;
import com.foursoft.foureveredit.controller.command.SetSimpleInstanceValueCommand;
import com.foursoft.foureveredit.view.HTMLView;
import com.foursoft.foureveredit.view.UnsupportedAttributeException;
import com.foursoft.mvc.controller.Controller;
import com.foursoft.mvc.view.AbstractView;

/**
 * @author kivlehan_adm
 * 
 * Basic implementation of the core functionality in HTMLView
 */
public abstract class HTMLViewBase extends AbstractView implements HTMLView, InstanceObserver {

    /** the instance whose value is displayed/ edited */
    HTMLInstance myInstance = null;

    /**
	 * the SetSimpleInstanceValue command to be used in the implementation
	 */
    protected SetSimpleInstanceValueCommand setSimpleInstanceValueCommand = null;

    /**
	 * the SetFocusedInstance command to be used in the implementation
	 *  
	 */
    protected SetFocusedInstanceCommand setFocusedInstanceCommand = null;

    /**
	 * the setFocusedViewCommand to be used in the implementation
	 */
    protected SetFocusedViewCommand setFocusedViewCommand = null;

    /**
	 * the setFocusedLinkCommand to be used in the implementation
	 */
    protected SetFocusedLinkCommand setFocusedLinkCommand = null;

    /**
	 * the Checkpoint command to be used in the implementation
	 */
    protected CheckpointCommand checkpointCommand = null;

    /***************************************************************************
	 * Set of all the supported attributes in the implementation.
	 **************************************************************************/
    protected Set<String> supportedAttributes = null;

    /**
	 *  
	 */
    protected HTMLViewBase(Controller c) {
        super(c);
        String[] supportedAttributeArray = new String[] { "bold", "italic", "underline", "unorderedlist", "orderedlist", "indentmore", "indentless", "image", "link" };
        supportedAttributes = new HashSet<String>();
        for (int i = 0; i < supportedAttributeArray.length; i++) {
            supportedAttributes.add(supportedAttributeArray[i]);
        }
        setSimpleInstanceValueCommand = (SetSimpleInstanceValueCommand) c.getCommand("SetSimpleInstanceValue");
        checkpointCommand = (CheckpointCommand) c.getCommand("Checkpoint");
        setFocusedInstanceCommand = (SetFocusedInstanceCommand) c.getCommand("SetFocusedInstance");
        setFocusedLinkCommand = (SetFocusedLinkCommand) c.getCommand("SetFocusedLink");
        setFocusedViewCommand = (SetFocusedViewCommand) c.getCommand("SetFocusedView");
    }

    public void setHTMLInstance(HTMLInstance instance) {
        Instance oldInstance = myInstance;
        myInstance = instance;
        if (oldInstance != null) {
            oldInstance.deregister(this);
        }
        if (myInstance != null) {
            myInstance.register(this, InstanceObserver.VALUE_CHANGED);
        }
    }

    /**
	 * Check if the passed attribute is supported by the Implementaton
	 */
    public void checkAttributeSupported(String attributeName) throws UnsupportedAttributeException {
        if ((attributeName == null) || !supportedAttributes.contains(attributeName)) {
            throw new UnsupportedAttributeException("Attribute Name \"" + attributeName + "\" not supported");
        }
    }

    public HTMLInstance getHTMLInstance() {
        return myInstance;
    }

    public void destroy() {
        if (myInstance != null) {
            myInstance.deregister(this);
        }
        myInstance = null;
        setFocusedLinkCommand = null;
    }
}
