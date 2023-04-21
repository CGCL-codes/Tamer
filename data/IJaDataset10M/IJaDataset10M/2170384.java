package tico.interpreter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellView;
import org.jgraph.graph.VertexView;
import tico.board.TBoard;
import tico.board.TBoardConstants;
import tico.board.components.TCell;
import tico.board.components.TComponent;
import tico.board.components.TGridCell;
import tico.board.components.TLabel;
import tico.board.components.TLine;
import tico.board.components.TOval;
import tico.board.components.TRectangle;
import tico.board.components.TRoundRect;
import tico.board.components.TTextArea;
import tico.interpreter.actions.TInterpreterRun;
import tico.interpreter.board.TInterpreterCellRenderer;
import tico.interpreter.board.TInterpreterCellView;
import tico.interpreter.board.TInterpreterGridCellView;
import tico.interpreter.board.TInterpreterLabelView;
import tico.interpreter.board.TInterpreterLineView;
import tico.interpreter.board.TInterpreterOvalView;
import tico.interpreter.board.TInterpreterRectangleView;
import tico.interpreter.board.TInterpreterRoundRectView;
import tico.interpreter.board.TInterpreterTextAreaRenderer;
import tico.interpreter.board.TInterpreterTextAreaView;
import tico.interpreter.environment.TEnvironmentExecution;
import tico.interpreter.threads.TThreads;

/**
 * New version of <code>BasicMarqueeHandler</code> which add the creation of
 * popup menus with right button and the opening of components dialogs with
 * double click.
 * 
 * @author Antonio Rodríguez
 * @version 1.0 Nov 20, 2006
 */
public class TInterpreterMarqueeHandler extends BasicMarqueeHandler {

    public static TThreads barrido;

    private static TInterpreter interpreter;

    public int modo;

    public CellView currentView;

    public static TInterpreterCellRenderer render;

    public TBoard board;

    public static TBoard followingBoard;

    static CellView[] views;

    public static int cuentaa = 0;

    /**
	 * Creates a new <code>TInterpreterMarqueeHandler</code> for the specified
	 * <code>boardContainer</code>.
	 * 
	 * @param boardContainer
	 *            The specified <code>boardContainer</code>
	 */
    public TInterpreterMarqueeHandler(TInterpreter interpreter) {
        super();
        this.interpreter = interpreter;
    }

    public void mouseDragged(MouseEvent e) {
    }

    public boolean isForceMarqueeEvent(MouseEvent event) {
        return true;
    }

    /**
	 * Start the marquee at the specified startPoint. This invokes
	 * expandMarqueeToPoint to initialize marquee selection.
	 */
    public void mousePressed(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        if (interpreter.run == 1) {
            if (render != null) {
                render.mouseOver = 0;
            }
            if (e != null) {
                if (!(e.getSource() instanceof TBoard)) throw new IllegalArgumentException("MarqueeHandler cannot handle event from unknown source: " + e);
                board = (TBoard) e.getSource();
                views = board.getGraphLayoutCache().getAllViews();
            }
            if (interpreter.getActivateBar() == 0) {
                if (e != null) {
                    if (!(e.getSource() instanceof TBoard)) throw new IllegalArgumentException("MarqueeHandler cannot handle event from unknown source: " + e);
                    board = (TBoard) e.getSource();
                    views = board.getGraphLayoutCache().getAllViews();
                    for (int i = 0; i < views.length; i++) {
                        if (views[i].getBounds().contains(e.getPoint())) {
                            if (views[i].isLeaf()) {
                                if (TInterpreterConstants.vmap != null) {
                                    if (TInterpreterConstants.Original_Color != null) {
                                        TBoardConstants.setLineWidth(TInterpreterConstants.vmap, TInterpreterConstants.Original_border);
                                        TBoardConstants.setBorderColor(TInterpreterConstants.vmap, TInterpreterConstants.Original_Color);
                                    }
                                    Graphics g = interpreter.getGraphics();
                                    g.setClip(TInterpreterConstants.ejex, TInterpreterConstants.ejey, TInterpreterConstants.anch, TInterpreterConstants.alt);
                                    interpreter.update(g);
                                }
                                try {
                                    render = (TInterpreterCellRenderer) ((VertexView) views[i]).getRenderer();
                                } catch (Exception e1) {
                                }
                                TInterpreterConstants.vmap = views[i].getAllAttributes();
                                ImageIcon AlternativeImage = (ImageIcon) TBoardConstants.getAlternativeIcon(TInterpreterConstants.vmap);
                                if (AlternativeImage != null) {
                                    render.mouseOver = 1;
                                }
                                float grosor = TBoardConstants.getLineWidth(TInterpreterConstants.vmap);
                                TInterpreterConstants.Original_border = (int) grosor;
                                grosor = TBoardConstants.getChangeLineWidth(TInterpreterConstants.vmap);
                                TInterpreterConstants.Original_Color = TBoardConstants.getBorderColor(TInterpreterConstants.vmap);
                                if (TInterpreterConstants.Original_Color != null) {
                                    TBoardConstants.setLineWidth(TInterpreterConstants.vmap, grosor);
                                    Color colour = TBoardConstants.getChangeColor(TInterpreterConstants.vmap);
                                    TBoardConstants.setBorderColor(TInterpreterConstants.vmap, colour);
                                }
                                obtainPosition(views[i]);
                                Graphics g = interpreter.getGraphics();
                                g.setClip(TInterpreterConstants.ejex, TInterpreterConstants.ejey, TInterpreterConstants.anch, TInterpreterConstants.alt);
                                interpreter.update(g);
                            }
                        }
                    }
                }
            }
            if (interpreter.getActivateBar() == 1) {
                if (e != null) {
                    if (!(e.getSource() instanceof TBoard)) throw new IllegalArgumentException("MarqueeHandler cannot handle event from unknown source: " + e);
                    board = (TBoard) e.getSource();
                    views = board.getGraphLayoutCache().getAllViews();
                    for (int i = 0; i < views.length; i++) {
                        if (views[i].getBounds().contains(e.getPoint())) {
                            if (TInterpreterConstants.vmap != null) {
                                if (TInterpreterConstants.Original_Color != null) {
                                    TBoardConstants.setLineWidth(TInterpreterConstants.vmap, TInterpreterConstants.Original_border);
                                    TBoardConstants.setBorderColor(TInterpreterConstants.vmap, TInterpreterConstants.Original_Color);
                                }
                                updateScreenArea();
                            }
                            if (views[i].isLeaf() && (TInterpreterConstants.isGrid == 0) && (interpreter.BrowseGrid != 2)) {
                                try {
                                    render = (TInterpreterCellRenderer) ((VertexView) views[i]).getRenderer();
                                } catch (Exception e1) {
                                }
                                TInterpreterConstants.vmap = views[i].getAllAttributes();
                                ImageIcon AlternativeImage = (ImageIcon) TBoardConstants.getAlternativeIcon(TInterpreterConstants.vmap);
                                if (AlternativeImage != null) render.mouseOver = 1;
                                float grosor = TBoardConstants.getLineWidth(TInterpreterConstants.vmap);
                                TInterpreterConstants.Original_border = (int) grosor;
                                TInterpreterConstants.Original_Color = TBoardConstants.getBorderColor(TInterpreterConstants.vmap);
                                grosor = TBoardConstants.getChangeLineWidth(TInterpreterConstants.vmap);
                                Color colour = TBoardConstants.getChangeColor(TInterpreterConstants.vmap);
                                if (TInterpreterConstants.Original_Color != null) {
                                    TBoardConstants.setLineWidth(TInterpreterConstants.vmap, grosor);
                                    TBoardConstants.setBorderColor(TInterpreterConstants.vmap, colour);
                                }
                                obtainPosition(views[i]);
                                updateScreenArea();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void updateScreenArea() {
        Graphics g = interpreter.getGraphics();
        g.setClip(TInterpreterConstants.ejex, TInterpreterConstants.ejey, TInterpreterConstants.anch, TInterpreterConstants.alt);
        interpreter.update(g);
    }

    public static void obtainPosition(CellView view) {
        double pox = view.getBounds().getX();
        double poy = view.getBounds().getY();
        Point point = new Point();
        point.setLocation(pox, poy);
        SwingUtilities.convertPointToScreen(point, interpreter.interpretArea);
        pox = point.getX();
        poy = point.getY();
        TInterpreterConstants.ejex = (int) pox;
        TInterpreterConstants.ejey = (int) poy;
        TInterpreterConstants.alt = (int) view.getBounds().getHeight() + 10;
        TInterpreterConstants.anch = (int) view.getBounds().getWidth() + 10;
    }

    public void mouseReleased(MouseEvent e) {
        if (interpreter.run == 1) {
            if (interpreter.BrowseGrid != 2) {
                try {
                    startPoint = e.getPoint();
                } catch (Exception e1) {
                }
                if (e != null) {
                    if (!(e.getSource() instanceof TBoard)) throw new IllegalArgumentException("MarqueeHandler cannot handle event from unknown source: " + e);
                    board = (TBoard) e.getSource();
                    CellView[] views = board.getGraphLayoutCache().getAllViews();
                    for (int i = 0; i < views.length; i++) {
                        if (views[i].getBounds().contains(e.getPoint())) {
                            int into = 0;
                            if (TInterpreterConstants.ClicReleased == 1) {
                                System.out.println("ASdasd");
                                TInterpreterActionsMouseReleased(views[i]);
                                TInterpreterRun.fin = true;
                                into = 1;
                            }
                            if (!(views[i].isLeaf())) {
                                interpreter.BrowseGrid = 1;
                                interpreter.Grid = i;
                            } else {
                                if (TInterpreterConstants.isGrid == 0) if (into != 1) {
                                    TInterpreterActionsMouseReleased(views[i]);
                                }
                            }
                        }
                    }
                }
            } else {
                try {
                    startPoint = e.getPoint();
                } catch (Exception e1) {
                }
                if (e != null) {
                    if (!(e.getSource() instanceof TBoard)) throw new IllegalArgumentException("MarqueeHandler cannot handle event from unknown source: " + e);
                    board = (TBoard) e.getSource();
                    CellView[] views = board.getGraphLayoutCache().getAllViews();
                    for (int i = 0; i < views.length; i++) {
                        if (views[i].getBounds().contains(e.getPoint())) {
                            TInterpreterConstants.ClicReleased = 1;
                            TInterpreterConstants.SecondClic = i;
                        }
                    }
                }
            }
        }
    }

    private boolean isCell(CellView celda) {
        String oval = "class tico.interpreter.board.TInterpreterOvalView";
        String rectangle = "class tico.interpreter.board.TInterpreterRectangleView";
        String roundRect = "class tico.interpreter.board.TInterpreterRoundRectView";
        String line = "class tico.interpreter.board.TInterpreterLineView";
        String label = "class tico.interpreter.board.TInterpreterLabelView";
        String type = celda.getClass().toString();
        return (!((type.equals(oval)) || (type.equals(rectangle)) || (type.equals(roundRect)) || (type.equals(line)) || (type.equals(label))));
    }

    public void TInterpreterActionsMouseReleased(CellView celda) {
        if (isCell(celda)) {
            System.out.println("Celda");
            AttributeMap map = celda.getAllAttributes();
            if (TBoardConstants.isAccumulated(map)) {
                TInterpreterRun.fina = true;
                interpreter.addAccumulated((VertexView) celda);
                if (TInterpreter.AccumulatedList.size() < TInterpreterConstants.interpreterAcumulatedCells) TInterpreter.AccumulatedList.add(celda);
            }
            String environ = TBoardConstants.getEnvironmentAction(map);
            if (environ != null) {
                new TEnvironmentExecution(TBoardConstants.getEnvironmentAction(map));
                interpreter.InterpreterRobot.delay(1000);
            }
            TInterpreterConstants.ruta = TBoardConstants.getSoundFile(map);
            if (TInterpreterConstants.ruta == null) {
                TInterpreterConstants.musicOn = 0;
            } else {
                TInterpreterConstants.musicOn = 1;
            }
            TInterpreterConstants.textToSend = TBoardConstants.getSendText(map);
            if (TInterpreterConstants.textToSend == null) {
                TInterpreterConstants.sendTextOn = 0;
            } else {
                TInterpreterConstants.sendTextOn = 1;
                TInterpreterConstants.time = TBoardConstants.getSendTextTimer(map);
                TComponent component = TBoardConstants.getSendTextTarget(map);
                CellView[] vistas = board.getGraphLayoutCache().getAllViews();
                int i = 0;
                int pos = 0;
                for (i = 0; i < vistas.length; i++) {
                    if (vistas[i].getCell().equals(component)) {
                        TInterpreterConstants.positionReceiver = i;
                    }
                }
                TInterpreterConstants.textRender = (TInterpreterTextAreaRenderer) ((TInterpreterTextAreaView) vistas[pos]).getRenderer();
                TInterpreterConstants.map2 = vistas[TInterpreterConstants.positionReceiver].getAllAttributes();
                TInterpreterConstants.lastName = TBoardConstants.getText(TInterpreterConstants.map2);
            }
            followingBoard = TBoardConstants.getFollowingBoard(map);
            if (TInterpreterMarqueeHandler.followingBoard != null) {
                interpreter.BoardChange = 1;
                return;
            }
        }
    }

    public CellView SearchComponent(String name) {
        CellView[] views = interpreter.getBoard().getGraphLayoutCache().getAllViews();
        int i = 0;
        boolean encontrado = false;
        i = 0;
        while ((i < views.length) && (!encontrado)) {
            AttributeMap map = views[i].getAllAttributes();
            String identificator = TBoardConstants.getId(map);
            encontrado = (name.equals(identificator));
            if (encontrado) {
                return views[i];
            }
            i++;
        }
        return null;
    }
}
