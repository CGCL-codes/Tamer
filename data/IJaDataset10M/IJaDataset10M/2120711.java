package com.googlecode.sarasvati.editor.command;

import java.awt.Point;
import com.googlecode.sarasvati.editor.model.EditorExternal;
import com.googlecode.sarasvati.editor.model.EditorScene;

public class AddExternalCommand extends AbstractCommand {

    private final EditorScene scene;

    private final Point location;

    private final EditorExternal external;

    public AddExternalCommand(final EditorScene scene, final Point location, final EditorExternal external) {
        this.scene = scene;
        this.location = location;
        this.external = external;
    }

    @Override
    public void performAction() {
        external.setOrigin(new Point(location));
        scene.addNode(external);
        scene.getGraph().addExternal(external);
    }

    @Override
    public void undoAction() {
        scene.removeNode(external);
        scene.getGraph().removeExternal(external);
    }

    @Override
    public String getName() {
        return "Add External";
    }
}
