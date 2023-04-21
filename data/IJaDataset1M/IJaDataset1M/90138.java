package sidekick;

import org.gjt.sp.jedit.buffer.FoldHandler;
import org.gjt.sp.jedit.buffer.JEditBuffer;
import javax.swing.text.Segment;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
* Provides a <code>FoldHandler</code> based on the {@link sidekick.Asset}s parsed from the buffer.  Each <code>Asset</code> will become a fold.
 */
public class SideKickFoldHandler extends FoldHandler {

    public SideKickFoldHandler() {
        super("sidekick");
    }

    public int getFoldLevel(JEditBuffer buffer, int lineIndex, Segment seg) {
        if (lineIndex == 0) return 0;
        SideKickParsedData data = (SideKickParsedData) buffer.getProperty(SideKickPlugin.PARSED_DATA_PROPERTY);
        if (data == null) return 0;
        FoldHandler override = data.getFoldHandler();
        if (override != null) return override.getFoldLevel(buffer, lineIndex, seg);
        int lineStartOffset = buffer.getLineStartOffset(lineIndex);
        TreePath path = data.getTreePathForPosition(lineStartOffset);
        if (path == null) return 0; else {
            TreeNode treeNode = (TreeNode) path.getLastPathComponent();
            IAsset asset = data.getAsset(treeNode);
            if (asset.getStart().getOffset() == lineStartOffset) return path.getPathCount() - 2;
            return path.getPathCount() - 1;
        }
    }
}
