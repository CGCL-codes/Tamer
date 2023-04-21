package net.rptools.maptool.client.tool.drawing;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import net.rptools.maptool.language.I18N;

/**
 * Tool for drawing freehand lines.
 */
public class LineTool extends AbstractLineTool implements MouseMotionListener {

    private static final long serialVersionUID = 3258132466219627316L;

    private Point tempPoint;

    public LineTool() {
        try {
            setIcon(new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("net/rptools/maptool/client/image/tool/draw-blue-strtlines.png"))));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public String getTooltip() {
        return "tool.line.tooltip";
    }

    @Override
    public String getInstructions() {
        return "tool.line.instructions";
    }

    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (getLine() == null) {
                startLine(e);
                setIsEraser(isEraser(e));
            } else {
                stopLine(e);
            }
        } else if (getLine() != null) {
            tempPoint = null;
            return;
        }
        super.mousePressed(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (getLine() == null) {
            super.mouseDragged(e);
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (getLine() != null) {
            if (tempPoint != null) {
                removePoint(tempPoint);
            }
            tempPoint = addPoint(e);
        }
        super.mouseMoved(e);
    }
}
