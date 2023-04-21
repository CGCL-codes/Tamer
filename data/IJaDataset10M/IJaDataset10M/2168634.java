package de.jmda.fx.node.behaviour;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.lang.model.element.TypeElement;
import de.jmda.fx.node.LineH;
import de.jmda.mproc.TypeElementFactory;
import de.jmda.mview.ElementFactory;
import de.jmda.mview.fx.node.typeshape.Typeshape;
import de.jmda.mview.typeshape.DisplayOptionsModelUtil;

public class TestBehaviours extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(getScene());
        stage.show();
    }

    private Scene getScene() {
        Group group = new Group();
        Scene scene = new Scene(group, 640, 480);
        scene.getStylesheets().add("typeshape.css");
        LineH lineH = new LineH(10, 10, 60);
        Pane pane = new Pane();
        pane.getStyleClass().add("typeshape");
        lineH.layoutXProperty().addListener(new CL(pane.layoutXProperty()));
        pane.setLayoutX(50);
        pane.setLayoutY(50);
        group.getChildren().add(pane);
        Typeshape typeshape = new Typeshape(getTypeElement(), DisplayOptionsModelUtil.create());
        lineH.activateHighlighting();
        lineH.activateDragging();
        typeshape.setLayoutX(100);
        typeshape.setLayoutY(100);
        group.getChildren().add(lineH);
        group.getChildren().add(typeshape);
        return scene;
    }

    private TypeElement getTypeElement() {
        return TypeElementFactory.get("jmda.mview", ElementFactory.class);
    }

    private class CL implements ChangeListener<Number> {

        private DoubleProperty bound;

        private CL(DoubleProperty bound) {
            this.bound = bound;
        }

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            bound.set(newValue.doubleValue());
        }
    }
}
