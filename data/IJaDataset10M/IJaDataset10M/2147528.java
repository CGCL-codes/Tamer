package structure.drawElement;

import java.util.Set;
import filesystem.FSElement;
import filesystem.FastDirectory;

/**
 * This class just provide static functions to create the different
 * {@link DrawElement}s.
 */
public class DrawElementFactory {

    /**
	 * Returns a new {@link RectangleElement} created from the given
	 * {@link FastDirectory}.
	 * 
	 * @param directory
	 *            the directory to create the {@link RectangleElement} from.
	 * @return the {@link RectangleElement}.
	 */
    public static RectangleElement getRectangleElement(FastDirectory directory) {
        RectangleElement rootElement = new RectangleElement(directory);
        addFSElements(directory.getChildren(), rootElement);
        return rootElement;
    }

    /**
	 * Adds all {@link FSElement}s to the root. This is done recursively.
	 * 
	 * @param elements
	 *            the elements which are the children of the given root element.<br>
	 *            Must not be null.
	 * @param rootElement
	 *            the root element. The given elements will be added a children.<br>
	 *            Must not be null.
	 */
    private static void addFSElements(Set<FSElement> elements, RectangleElement rootElement) {
        for (FSElement element : elements) {
            RectangleElement newRoot = new RectangleElement(element, rootElement);
            rootElement.addChild(newRoot);
            if (element instanceof FastDirectory) addFSElements(((FastDirectory) element).getChildren(), newRoot);
        }
    }
}
