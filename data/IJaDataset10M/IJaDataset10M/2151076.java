package Galaxy.Tree.Tool.Input.Control;

import java.util.List;
import Galaxy.Tree.Tool.Input.Input;
import Galaxy.Visitor.DFSToolVisitor.ToolImplVisitor;
import Galaxy.Visitor.util.LoniContext;

public class Repeat extends Input {

    List<Input> inputs;

    String name;

    String title;

    public List<Input> getInputs() {
        return inputs;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object accept(ToolImplVisitor toolVisitor, LoniContext context) {
        return toolVisitor.visit(this, context);
    }
}
