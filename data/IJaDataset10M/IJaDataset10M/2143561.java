package net.sf.refactorit.source.preview;

import net.sf.refactorit.source.SourceHolder;
import net.sf.refactorit.source.edit.FileEraser;
import net.sf.refactorit.source.edit.FilesystemEditor;
import net.sf.refactorit.source.edit.Line;
import net.sf.refactorit.ui.PackageModel.PackageNode;
import net.sf.refactorit.ui.treetable.BinTreeTableNode;
import java.util.List;
import java.util.Map;

/**
 * Changes source file/line content map according model selections
 *
 * @author Tonis Vaga
 */
public class PreviewModelSelectionProcessor {

    private ChangesPreviewModel model;

    private Map map;

    public PreviewModelSelectionProcessor(ChangesPreviewModel model, Map sources) {
        this.model = model;
        this.map = sources;
    }

    public Map process() {
        BinTreeTableNode root = (BinTreeTableNode) model.getRoot();
        addSelectedItemsToMap(root, true);
        return this.map;
    }

    private void addSelectedItemsToMap(final BinTreeTableNode node, boolean parentNodeSelected) {
        if (node instanceof SourceNode) {
            processSourceNode((SourceNode) node, parentNodeSelected);
        } else {
            boolean currentNodeSelected = parentNodeSelected;
            if (!(node instanceof PackageNode)) {
                currentNodeSelected = parentNodeSelected && node.isSelected();
            }
            if (node instanceof EditorNode) {
                FilesystemEditor editor = ((EditorNode) node).getEditor();
                if (editor instanceof FileEraser) {
                    if (!node.isSelected()) {
                        editor.disable();
                    }
                }
            }
            for (int index = 0, max = node.getChildCount(); index < max; ++index) {
                BinTreeTableNode item = (BinTreeTableNode) node.getChildAt(index);
                addSelectedItemsToMap(item, currentNodeSelected);
            }
        }
    }

    private void processSourceNode(final SourceNode node, final boolean parentNodeSelected) {
        final SourceHolder sourceHolder = node.getSource();
        List children = node.getAllChildren();
        final boolean currentNodeSelected = parentNodeSelected && node.isSelected();
        if (!currentNodeSelected) {
            map.remove(sourceHolder);
            for (int index = 0; index < children.size(); ++index) {
                if (children.get(index) instanceof SourceLineNode) {
                    SourceLineNode sourceLineNode = (SourceLineNode) children.get(index);
                    final Line currentLine = sourceLineNode.getLine();
                    if (!sourceLineNode.isSelected()) {
                        currentLine.restoreOriginalContent();
                    }
                }
                if (children.get(index) instanceof EditorNode) {
                    if (!((EditorNode) children.get(index)).isSelected()) {
                        ((EditorNode) children.get(index)).getEditor().disable();
                    }
                }
            }
        } else {
            for (int index = 0; index < children.size(); ++index) {
                if (children.get(index) instanceof SourceLineNode) {
                    SourceLineNode sourceLineNode = (SourceLineNode) children.get(index);
                    final Line currentLine = sourceLineNode.getLine();
                    if (!sourceLineNode.isSelected()) {
                        currentLine.restoreOriginalContent();
                    }
                }
                if (children.get(index) instanceof EditorNode) {
                    if (!((EditorNode) children.get(index)).isSelected()) {
                        ((EditorNode) children.get(index)).getEditor().disable();
                    }
                }
            }
        }
    }
}
