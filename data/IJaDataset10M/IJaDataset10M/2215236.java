package de.jmda.fx.node.behaviour;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import de.jmda.fx.node.ArrowOrthogonal;
import de.jmda.fx.node.ArrowOrthogonal.Orientation;
import de.jmda.util.gui.awt.graphics.RelationEndpoint.Style;

public class TestArrowSizing extends Application {

    private static final Logger LOGGER = Logger.getLogger(TestArrowSizing.class);

    private Scene scene;

    private Group group;

    private ArrowOrthogonal arrow;

    private Button buttonGrow;

    private Button buttonShrink;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        group = new Group();
        scene = new Scene(group, 640, 480);
        stage.setScene(scene);
        stage.show();
        buttonGrow = new Button("grow");
        buttonGrow.setLayoutX(200);
        buttonGrow.setLayoutY(100);
        buttonGrow.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                LOGGER.debug("\n" + "arrow.head.x: " + arrow.headProperty().get().getX() + " " + "arrow.tail.x: " + arrow.tailProperty().get().getX());
                arrow.setLength(arrow.getLength() + 10);
                LOGGER.debug("\n" + "arrow.head.x: " + arrow.headProperty().get().getX() + " " + "arrow.tail.x: " + arrow.tailProperty().get().getX());
            }
        });
        buttonShrink = new Button("shrink");
        buttonShrink.setLayoutX(200);
        buttonShrink.setLayoutY(130);
        buttonShrink.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                LOGGER.debug("\n" + "arrow.head.x: " + arrow.headProperty().get().getX() + " " + "arrow.tail.x: " + arrow.tailProperty().get().getX());
                arrow.setLength(arrow.getLength() - 10);
                LOGGER.debug("\n" + "arrow.head.x: " + arrow.headProperty().get().getX() + " " + "arrow.tail.x: " + arrow.tailProperty().get().getX());
            }
        });
        arrow = new ArrowOrthogonal(new Point2D(10, 10), 10, Style.OPEN, false, 4, Orientation.UP);
        group.getChildren().add(buttonGrow);
        group.getChildren().add(buttonShrink);
        group.getChildren().add(arrow);
    }
}
