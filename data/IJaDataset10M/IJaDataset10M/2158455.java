package astcentric.editor.eclipse.plugin;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

public class ASTImageDescriptor extends CompositeImageDescriptor {

    public static final Image ERROR = ASTEditorPlugin.getImageDescriptor("/icons/error.gif").createImage();

    public static final Image WARNING = ASTEditorPlugin.getImageDescriptor("/icons/warning.gif").createImage();

    public static final Image AST_ICON = ASTEditorPlugin.getImageDescriptor("/icons/ast.gif").createImage();

    public static final Image AST_READ_ONLY_ICON = ASTEditorPlugin.getImageDescriptor("/icons/ast_ro.png").createImage();

    private static final Point SIZE = new Point(16, 16);

    private static final Image ERROR_SMALL = ASTEditorPlugin.getImageDescriptor("/icons/errorSmall.gif").createImage();

    @Override
    protected void drawCompositeImage(int width, int height) {
        drawImage(AST_ICON.getImageData(), 0, 0);
        Point size = getSize();
        ImageData data = ERROR_SMALL.getImageData();
        drawImage(data, 0, size.y - data.height);
    }

    @Override
    protected Point getSize() {
        return SIZE;
    }
}
