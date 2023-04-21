package cards;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class BacklogFigure extends Viewport {

    private BacklogFigure figure;

    private String nameField;

    private String nameLabel = "Name: ";

    public BacklogFigure() {
        figure = this;
        setLayoutManager(new FreeformLayout());
        setOpaque(true);
        this.addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent me) {
                IFigure parent = getParent();
                parent.remove(figure);
                parent.add(figure);
            }

            public void mouseReleased(MouseEvent me) {
            }

            public void mouseDoubleClicked(MouseEvent me) {
            }
        });
    }

    public void setNameText(String name) {
        this.nameField = name;
    }

    protected void paintFigure(Graphics g) {
        Rectangle clip = getClientArea();
        int nameLine = 20;
        if (isOpaque()) {
            g.setAlpha(255);
            g.setBackgroundColor(new Color(null, 120, 244, 120));
            g.fillRectangle(clip);
            g.drawText(nameLabel + nameField, clip.x + 5, clip.y + 5);
            g.setForegroundColor(new Color(null, 100, 100, 100));
            g.drawLine(clip.x, clip.y + nameLine, clip.x + clip.width, clip.y + nameLine);
            g.drawRectangle(clip.x, clip.y, clip.width - 1, clip.height - 1);
        } else {
            System.out.println("not opaque");
        }
    }
}
