package uk.ac.lkl.migen.system.expresser;

public interface PositionedObjectHolder {

    /**
     * Returns the bounding box of the given component. 
     * 
     * @param componentId the component's ID
     */
    Rectangle getPosition(String componentId);

    /**
     * Simple reimplementation of java.awt.Rectangle. 
     * 
     * AWT is not always available (e.g. GWT), so this class
     * provides an independent well-known data structure. If 
     * AWT is available, transform this into a java.awt.Rectangle
     * is trivial. 
     * 
     * @author sergut
     *
     */
    class Rectangle {

        public int x;

        public int y;

        public int width;

        public int height;

        public Rectangle(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}
