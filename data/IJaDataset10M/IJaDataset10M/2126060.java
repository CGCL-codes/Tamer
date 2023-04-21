package net.sourceforge.jisocreator.util;

import java.io.File;
import java.io.InputStream;
import net.sourceforge.jisocreator.model.isoexplorer.decl.ITreeNode;
import net.sourceforge.jisocreator.model.osexplorer.OSExplorer;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;

public class ImageUtils {

    private static ImageUtils instance;

    private ImageRegistry imageRegistry;

    private ImageUtils() {
        imageRegistry = new ImageRegistry(Display.getDefault());
    }

    public static ImageUtils getInstance() {
        if (instance == null) {
            instance = new ImageUtils();
        }
        return instance;
    }

    public Image loadImage(Program program) {
        Image image = (Image) imageRegistry.get(program.getName());
        if (image == null) {
            image = new Image(Display.getCurrent(), program.getImageData());
            imageRegistry.put(program.getName(), image);
        }
        return image;
    }

    public Image loadImage(String name) {
        Image image = (Image) imageRegistry.get(name);
        if (image == null) {
            InputStream stream = getClass().getResourceAsStream("res/img/".concat(name));
            image = new Image(Display.getDefault(), stream);
            imageRegistry.put(name, image);
        }
        return image;
    }

    public Image loadImage(File file) {
        if (OSExplorer.getInstance().isRoot(file)) {
            return loadImage("drive.png");
        } else if (file.isDirectory()) {
            return loadImage("folder.png");
        } else {
            String extension = OSExplorer.getInstance().getExtension(file);
            if (extension.equals("")) {
                return loadImage("file.png");
            } else {
                Program program = Program.findProgram(extension);
                if (program == null) {
                    return loadImage("file.png");
                } else {
                    return loadImage(program);
                }
            }
        }
    }

    public Image loadImage(ITreeNode node) {
        if (node.isRoot()) {
            return loadImage("iso.png");
        } else {
            return loadImage((File) node.getElement());
        }
    }

    public ImageDescriptor loadImageDescriptor(String imagePath) {
        return ImageDescriptor.createFromImage(loadImage(imagePath));
    }
}
