package astcentric.editor.common.view.tree;

import astcentric.editor.common.view.Recorder;
import astcentric.editor.common.view.graphic.Point;
import astcentric.editor.common.view.graphic.Rectangle;
import astcentric.editor.common.view.graphic.Size;
import astcentric.editor.common.view.graphic.context.GraphicsContext;

public class MockTreeNodeFrameFactory implements TreeNodeFrameFactory {

    private final Recorder _recorder;

    private static final class MockTreeNodeFrame extends AbstractTreeNodeFrame {

        private final Recorder _recorder;

        public MockTreeNodeFrame(TreeNode treeNode, Recorder recorder) {
            super(treeNode);
            _recorder = recorder;
        }

        public void drawConnection(GraphicsContext context, int x, int y, int yPrevious) {
            _recorder.record("drawConnection(" + x + "," + y + "," + yPrevious + ")\n");
        }

        public void drawFrame(GraphicsContext context, int x, int y) {
            _recorder.record("drawFrame(" + x + "," + y + ")\n");
        }

        public Point getContentAnchor() {
            return new Point(2, 3);
        }

        public Size getFrameSize() {
            return new Size(17, 1);
        }

        public int getHeightForLayout() {
            return 2;
        }

        public int getIndentation() {
            return 3;
        }

        public int getWidthWithoutContent() {
            return 4;
        }

        public int getYForConnectionToChild() {
            return 5;
        }

        public int getYForConnectionToNextSibling() {
            return 6;
        }

        public boolean isInsideBody(Point position) {
            return new Rectangle(2, 3, 0, 0).isInside(position);
        }

        public boolean isInsideNodeHandle(Point position) {
            return new Rectangle(1, 1, 0, 0).isInside(position);
        }
    }

    MockTreeNodeFrameFactory(Recorder recorder) {
        _recorder = recorder;
    }

    public String getName() {
        return "Mock";
    }

    public TreeNodeFrame create(TreeNode treeNode) {
        return new MockTreeNodeFrame(treeNode, _recorder);
    }
}
