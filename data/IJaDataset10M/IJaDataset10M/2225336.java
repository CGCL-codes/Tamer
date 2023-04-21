package sdljavax.gui;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import sdljava.video.SDLRect;
import sdljava.event.SDLEvent;
import sdljava.event.SDLMouseMotionEvent;
import org.gljava.opengl.GL;

/**
 * Describe class <code>ListBox</code> here.
 *
 * @author  Ivan Z. Ganza
 * @version $Id: ListBox.java,v 1.2 2005/09/03 23:07:00 ivan_ganza Exp $
 */
public class ListBox extends Widget {

    List items = new ArrayList();

    int selectedRow = -1;

    int firstVisibleRow = 0;

    float lineHeight;

    /**
     * Construct a new ListBox.  Uses the defaultFont to display items.
     *
     * @param font a <code>FTFont</code> value
     */
    public ListBox(GL gl, String name, SDLRect dimensions, String backgroundTexture) throws IOException {
        super(gl, name, dimensions, backgroundTexture);
        lineHeight = defaultFont.lineHeight();
    }

    /**
     * Add an item to the list box
     *
     * @param i a <code>String</code> value
     */
    public void add(String i) {
        items.add(i);
    }

    public List getItems() {
        return items;
    }

    public void draw(GL gl, int x, int y) {
        super.draw(gl, x, y);
        defaultFont.faceSize(16, 72);
        for (int i = 0; i < items.size(); i++) {
            String item = (String) items.get(i);
            gl.glRasterPos2f(x + container.xPos + 4, y + height + container.yPos - (lineHeight * i + lineHeight));
            defaultFont.render(item);
        }
        if (selectedRow != -1) {
            gl.glPushMatrix();
            gl.glTranslatef(x, y, 0);
            gl.glDisable(gl.GL_TEXTURE_2D);
            gl.glBegin(gl.GL_LINES);
            {
                gl.glColor3f(1.0f, 1.0f, 1.0f);
                y = height + (int) (container.yPos - (selectedRow * lineHeight) - lineHeight - 2);
                gl.glVertex2f(0, y);
                gl.glVertex2f(width, y);
            }
            gl.glEnd();
            gl.glPopMatrix();
        }
    }

    public void handleEvent(SDLEvent event) {
        if (event instanceof SDLMouseMotionEvent) {
            handleMouseMotionEvent((SDLMouseMotionEvent) event);
        }
    }

    public void handleMouseMotionEvent(SDLMouseMotionEvent motionEvent) {
        int x = motionEvent.getX();
        int y = motionEvent.getY();
        x = x - container.xPos - xPos;
        y = y - container.yPos - yPos;
        y = height - y;
        int row = whichRow(y);
        if (items.size() > row && items.get(row) != null) {
            selectedRow = row;
        }
        System.out.println("selected row: " + selectedRow);
    }

    public int whichRow(int y) {
        return (int) (y / lineHeight);
    }
}
