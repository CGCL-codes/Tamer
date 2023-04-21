package wickettree.table;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Response;

/**
 * A border for a node component which renders nested <code>DIV</code>s to
 * simulate the structure of parental branches inside a tabular layout.
 * 
 * @see NodeModel
 * @see TreeColumn#populateItem(Item, String, IModel)
 * @author Sven Meier
 */
public class NodeBorder extends Behavior {

    private static final long serialVersionUID = 1L;

    private boolean[] branches;

    public NodeBorder(boolean[] branches) {
        this.branches = branches;
    }

    @Override
    public void beforeRender(Component component) {
        Response response = component.getResponse();
        for (int i = 0; i < branches.length; i++) {
            if (i > 0) {
                response.write("<div class=\"tree-subtree\">");
            }
            if (branches[i]) {
                response.write("<div class=\"tree-branch tree-branch-mid\">");
            } else {
                response.write("<div class=\"tree-branch tree-branch-last\">");
            }
        }
    }

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        tag.put("class", "tree-node");
    }

    @Override
    public void afterRender(Component component) {
        Response response = component.getResponse();
        for (int i = 0; i < branches.length; i++) {
            response.write("</div>");
        }
    }
}
