package freemind.modes.filemode;

import freemind.modes.MindMap;
import freemind.modes.MindMapNode;
import freemind.modes.MindMapEdge;
import freemind.modes.MapAdapter;
import java.io.File;

public class FileMapModel extends MapAdapter {

    public FileMapModel() {
        setRoot(new FileNodeModel(new File(File.separator)));
    }

    public FileMapModel(File root) {
        setRoot(new FileNodeModel(root));
    }

    public void save(File file) {
    }

    public void load(File file) {
    }

    public boolean isSaved() {
        return true;
    }

    public String toString() {
        return "File: " + getRoot().toString();
    }

    public void changeNode(MindMapNode node, String newText) {
    }
}
