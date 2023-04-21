package it.cnr.stlab.xd.plugin.editor.commands;

import it.cnr.stlab.xd.plugin.editor.model.WorkingOntology;
import it.cnr.stlab.xd.plugin.editor.model.ModelFactory;
import it.cnr.stlab.xd.plugin.editor.model.NodeClass;
import it.cnr.stlab.xd.plugin.editor.model.NodeEntity;
import org.eclipse.draw2d.geometry.Rectangle;

public class ClassCreateCommand extends NodeCreateCommand {

    public ClassCreateCommand(NodeClass newNode, WorkingOntology parent, Rectangle bounds) {
        super(newNode, parent, bounds);
        if (newNode == null || !(newNode instanceof NodeClass)) setNode(ModelFactory.createNewNodeClass(parent));
        setChanges(parent.getOWLModelDelegate().getNewClassCreationChanges(((NodeEntity) getNode()).getName()));
    }
}
