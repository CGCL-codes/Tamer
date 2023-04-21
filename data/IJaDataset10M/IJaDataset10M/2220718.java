package com.iver.cit.gvsig.gui.cad.tools;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.geom.Point2D;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import com.iver.cit.gvsig.gui.cad.DefaultCADTool;
import com.iver.cit.gvsig.gui.cad.exception.CommandException;
import com.iver.cit.gvsig.gui.cad.tools.smc.CircleCADToolContext;
import com.iver.cit.gvsig.gui.cad.tools.smc.CircleCADToolContext.CircleCADToolState;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class CircleCADTool extends DefaultCADTool {

    protected CircleCADToolContext _fsm;

    protected Point2D center;

    protected Point2D firstPoint;

    protected Point2D secondPoint;

    protected Point2D thirdPoint;

    /**
     * Crea un nuevo LineCADTool.
     */
    public CircleCADTool() {
    }

    /**
     * M�todo de incio, para poner el c�digo de todo lo que se requiera de una
     * carga previa a la utilizaci�n de la herramienta.
     */
    public void init() {
        _fsm = new CircleCADToolContext(this);
    }

    public void transition(double x, double y, InputEvent event) {
        _fsm.addPoint(x, y, event);
    }

    public void transition(double d) {
        _fsm.addValue(d);
    }

    public void transition(String s) throws CommandException {
        if (!super.changeCommand(s)) {
            _fsm.addOption(s);
        }
    }

    /**
     * Equivale al transition del prototipo pero sin pasarle como par� metro el
     * editableFeatureSource que ya estar� creado.
     *
     * @param sel Bitset con las geometr�as que est�n seleccionadas.
     * @param x par�metro x del punto que se pase en esta transici�n.
     * @param y par�metro y del punto que se pase en esta transici�n.
     */
    public void addPoint(double x, double y, InputEvent event) {
        CircleCADToolState actualState = (CircleCADToolState) _fsm.getPreviousState();
        String status = actualState.getName();
        if (status.equals("Circle.CenterPointOr3p")) {
            center = new Point2D.Double(x, y);
        } else if (status == "Circle.PointOrRadius") {
            addGeometry(ShapeFactory.createCircle(center, new Point2D.Double(x, y)));
        } else if (status == "Circle.FirstPoint") {
            firstPoint = new Point2D.Double(x, y);
        } else if (status == "Circle.SecondPoint") {
            secondPoint = new Point2D.Double(x, y);
        } else if (status == "Circle.ThirdPoint") {
            thirdPoint = new Point2D.Double(x, y);
            addGeometry(ShapeFactory.createCircle(firstPoint, secondPoint, thirdPoint));
        }
    }

    /**
     * M�todo para dibujar la lo necesario para el estado en el que nos
     * encontremos.
     *
     * @param g Graphics sobre el que dibujar.
     * @param selectedGeometries BitSet con las geometr�as seleccionadas.
     * @param x par�metro x del punto que se pase para dibujar.
     * @param y par�metro x del punto que se pase para dibujar.
     */
    public void drawOperation(Graphics g, double x, double y) {
        CircleCADToolState actualState = _fsm.getState();
        String status = actualState.getName();
        if ((status == "Circle.CenterPointOr3p")) {
            if (firstPoint != null) {
                drawLine((Graphics2D) g, firstPoint, new Point2D.Double(x, y), DefaultCADTool.geometrySelectSymbol);
            }
        }
        if (status == "Circle.PointOrRadius") {
            Point2D currentPoint = new Point2D.Double(x, y);
            ShapeFactory.createCircle(center, currentPoint).draw((Graphics2D) g, getCadToolAdapter().getMapControl().getViewPort(), DefaultCADTool.axisReferencesSymbol);
        } else if (status == "Circle.SecondPoint") {
            drawLine((Graphics2D) g, firstPoint, new Point2D.Double(x, y), DefaultCADTool.geometrySelectSymbol);
        } else if (status == "Circle.ThirdPoint") {
            Point2D currentPoint = new Point2D.Double(x, y);
            IGeometry geom = ShapeFactory.createCircle(firstPoint, secondPoint, currentPoint);
            if (geom != null) {
                geom.draw((Graphics2D) g, getCadToolAdapter().getMapControl().getViewPort(), DefaultCADTool.axisReferencesSymbol);
            }
        }
    }

    /**
     * Add a diferent option.
     *
     * @param sel DOCUMENT ME!
     * @param s Diferent option.
     */
    public void addOption(String s) {
        CircleCADToolState actualState = (CircleCADToolState) _fsm.getPreviousState();
        String status = actualState.getName();
        if (status == "Circle.CenterPointOr3p") {
            if (s.equalsIgnoreCase(PluginServices.getText(this, "CircleCADTool.3p"))) {
            }
        }
    }

    public void addValue(double d) {
        CircleCADToolState actualState = (CircleCADToolState) _fsm.getPreviousState();
        String status = actualState.getName();
        if (status == "Circle.PointOrRadius") {
            addGeometry(ShapeFactory.createCircle(center, d));
        }
    }

    public String getName() {
        return PluginServices.getText(this, "circle_");
    }

    public String toString() {
        return "_circle";
    }

    public boolean isApplicable(int shapeType) {
        switch(shapeType) {
            case FShape.POINT:
            case FShape.MULTIPOINT:
                return false;
        }
        return true;
    }
}
