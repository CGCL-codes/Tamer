package com.vircon.myajax.web;

import java.util.List;
import com.vircon.myajax.web.event.Command;
import com.vircon.myajax.web.event.PropertyChangeCommand;

public class UnorderedList extends BaseContainer {

    @Override
    public void writeChildren(XMLWriter aWriter) {
        if (listModel != null) {
            listModel.write(aWriter);
        }
    }

    public UnorderedList(String id, ListModel aModel) {
        super(id);
        listModel = aModel;
    }

    public ComponentType getComponentType() {
        return ComponentType.List;
    }

    public void updateState() {
        sendCommand(new PropertyChangeCommand(getId(), getComponentType(), "value", listModel.getAsString()));
    }

    public void setModel(ListModel aModel) {
        listModel = aModel;
        updateState();
    }

    public ListModel getModel() {
        return listModel;
    }

    public void appendState(List<Command> lstCommand) {
        lstCommand.add(new PropertyChangeCommand(getId(), getComponentType(), "value", listModel.getAsString()));
    }

    private ListModel listModel;

    private static final long serialVersionUID = 1L;
}
