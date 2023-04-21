package org.vmasterdiff.gui.filediff.highlighter.highlight;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

public class SingleLineHighlightPainter implements Highlighter.HighlightPainter {

    Color color = null;

    public SingleLineHighlightPainter(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
	* Paints a highlight over full width of a window.
	* Looks at the first and last point and makes sure every line in between is
	* highlighted from the left to the right.  It does not stop at \n but continues
	* to the right-most edge of the JTextcomponent Area.  nice for diff tools.
	* <br>NOTE: There does seem to be a painting bug in Suns code where I 
	* highlight one line and it doesn't highlight all the way across.  If you 
	* invoke a revalidate and repaint though, it fixes the problem.
	*
	* @param g the graphics context
	* @param offs0 the starting model offset >= 0
	* @param offs1 the ending model offset >= offs1
	* @param bounds the bounding box for the highlight
	* @param c the editor
	*/
    public void paint(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c) {
        Rectangle alloc = bounds.getBounds();
        Rectangle area = c.getBounds();
        try {
            TextUI mapper = c.getUI();
            Rectangle rect1 = mapper.modelToView(c, offs0);
            Rectangle rect2 = mapper.modelToView(c, offs1);
            if (color == null) g.setColor(c.getSelectionColor()); else g.setColor(color);
            g.fillRect(0, rect1.y, alloc.width, rect2.y + rect2.height - rect1.y);
        } catch (BadLocationException e) {
        }
    }
}
