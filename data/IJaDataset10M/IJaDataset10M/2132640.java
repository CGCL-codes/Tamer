package de.jmda.fx.sandbox;

import javafx.application.Application;
import javafx.scene.Group;
import de.jmda.fx.node.Line;

public class SandboxJMDALines extends Sandbox {

    private Group group;

    @Override
    protected Group getGroup() {
        if (group == null) {
            group = new Group();
        }
        return group;
    }

    @Override
    protected void populateGroup() {
        Line lineDraggable = new Line(10, 10, 20, 10);
        lineDraggable.activateHighlighting();
        lineDraggable.activateDragging();
        group.getChildren().add(lineDraggable);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
