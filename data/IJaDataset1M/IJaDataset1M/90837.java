package com.example.client.data;

import com.example.client.Command;

public class CommandTreeNode extends ExplorerTreeNode {

    public CommandTreeNode(String name, String nodeID, String parentNodeID, String icon, Command command, boolean enabled, String idSuffix) {
        super(name, nodeID, parentNodeID, icon, null, enabled, idSuffix);
        setCommand(command);
    }

    public void setCommand(Command command) {
        setAttribute("command", command);
    }

    public Command getCommand() {
        return (Command) getAttributeAsObject("command");
    }
}
