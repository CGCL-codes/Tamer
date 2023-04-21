package astcentric.editor.console;

import astcentric.structure.basic.Node;

public class ShowChildrenCommand extends AbstractCommand {

    public static final Command SHOW_CHILDREN = new ShowChildrenCommand();

    public String getShortDescription() {
        return "Show all child nodes with ID and name";
    }

    public void execute(String[] arguments, CommandEnvironment environment) {
        Node currentNode = environment.getCurrentNode();
        if (currentNode != null) {
            currentNode.traverseNodes(new NodeNameAndIDPrinter(environment));
        }
    }
}
