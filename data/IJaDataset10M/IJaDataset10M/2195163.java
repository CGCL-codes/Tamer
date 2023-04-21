package annone.ui;

public class AbstractView implements View {

    private Pane parent;

    @Override
    public Pane getParent() {
        return parent;
    }

    protected void setParent(Pane parent) {
        this.parent = parent;
    }
}
