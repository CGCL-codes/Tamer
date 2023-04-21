package jasci.ui;

public class HBox extends FlowingContainer {

    public HBox() {
        super(FlowingContainer.X_AXIS, 1);
    }

    public HBox(int space) {
        super(FlowingContainer.X_AXIS, space);
    }

    public HBox(ContainerConstructorElement... elements) {
        super(FlowingContainer.X_AXIS, 1);
        constructorAdd(elements);
    }

    public HBox(int space, ContainerConstructorElement... elements) {
        super(FlowingContainer.X_AXIS, space);
        constructorAdd(elements);
    }
}
