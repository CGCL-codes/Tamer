package bookshelf.jrender;

import java.util.ArrayList;
import java.util.Iterator;
import bookshelf.jrender.element.IElement;
import bookshelf.jrender.element.Line;
import bookshelf.jrender.element.Space;

/**
 * @author Anton Krasovsky <ak1394@mail.ru>
 *  
 */
public class LineJustifier extends AbstractVisitor implements ILineDecorator {

    private ArrayList spaceList;

    private int width;

    public LineJustifier() {
        spaceList = new ArrayList();
    }

    public Line decorate(Line line) throws Exception {
        int lineWidth = line.getWidth();
        int freeSpace = width - lineWidth;
        if (freeSpace > 0) {
            for (Iterator iterator = line.childIterator(); iterator.hasNext(); ) {
                ((IElement) iterator.next()).visit(this);
            }
            if (spaceList.size() > 0) {
                int perSpace = freeSpace / spaceList.size();
                int lastSpace = perSpace + freeSpace % spaceList.size();
                for (int i = 0; i < spaceList.size() - 1; i++) {
                    Space s = (Space) spaceList.get(i);
                    s.setWidth(s.getWidth() + perSpace);
                }
                Space s = (Space) spaceList.get(spaceList.size() - 1);
                s.setWidth(s.getWidth() + lastSpace);
                spaceList.clear();
            }
        }
        return line;
    }

    public void addDecorator(ILineDecorator decorator) {
    }

    public void visitSpace(Space space) throws Exception {
        if (space.isResizable()) {
            spaceList.add(space);
        }
    }

    /**
     * @return Returns the width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width
     *            The width to set.
     */
    public void setWidth(int width) {
        this.width = width;
    }
}
