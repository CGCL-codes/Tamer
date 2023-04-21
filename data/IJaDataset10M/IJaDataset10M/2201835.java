package drawingtools;

import gui.DrawingPanel;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import diagramobjects.DiagramObjects;
import diagramobjects.ArrowsTextLine;
import diagramobjects.RefineLine;
import diagramobjects.Text;
import program.DatabaseConnection;
import utils.Color;
import utils.Point;

public class RefineTool extends DrawingTool {

    private DiagramObjects DiagramObjs;

    private RefineLine tempLine;

    private DrawingPanel DrawPanel;

    public RefineTool(DiagramObjects go, DrawingPanel dp) {
        DiagramObjs = go;
        DrawPanel = dp;
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        super.mouseEntered(arg0);
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        super.mouseExited(arg0);
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        super.mousePressed(arg0);
        System.out.println("mouseClicked, clickcount:" + ClickCount);
        if (ClickCount == 0) {
            DiagramObjs.AddGeometricObjectToPanel(new RefineLine(0, new Point(arg0.getX(), arg0.getY()), new Point(arg0.getX(), arg0.getY()), 10, new Text("<< Refine >>", new Point(arg0.getX() - 10, arg0.getY() - 10), new Color(0, 250, 30), 10), false, true));
            DrawPanel.addSelected();
            tempLine = (RefineLine) DiagramObjs.GetLastGeometricbjectFromPanel();
            tempLine.Print();
            IncreaseClickCount();
        } else if (ClickCount == 1) {
            ZeroClickCount();
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        super.mouseReleased(arg0);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
    }

    /**
	 * W przypadku rysowania, gdy user przesunie wskaznik myszy przesuwa koniec 
	 * linii i zapisuje jej polozenie w pamieci
	 */
    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        if (ClickCount == 1) {
            tempLine.MoveEndTo(e.getX(), e.getY(), false, true);
            DrawPanel.repaint();
        }
    }
}
