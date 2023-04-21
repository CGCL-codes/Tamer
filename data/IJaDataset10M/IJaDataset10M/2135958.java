package org.gems.designer.model.commands;

import org.eclipse.gef.commands.Command;

public interface ToolAssistant {

    public static final String EXTENSION_POINT = "org.gems.designer.dsml.toolassistant";

    public void commandInitiated(Command c);

    public void commandCompleted(Command c);
}
