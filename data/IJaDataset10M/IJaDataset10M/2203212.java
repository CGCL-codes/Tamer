package petriNet;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import biologicalElements.Elementdeclerations;
import biologicalObjects.nodes.BiologicalNodeAbstract;
import edu.uci.ics.jung.graph.Vertex;
import graph.jung.graphDrawing.VertexShapes;

public class Place extends BiologicalNodeAbstract {

    private double token = 0;

    private double tokenMin = -1;

    private double tokenMax = 100;

    private double tokenStart = 5;

    private boolean discrete = true;

    private Color plotColor;

    public boolean isDiscrete() {
        return discrete;
    }

    public void setDiscrete(boolean discrete) {
        if (discrete) {
            setBiologicalElement(Elementdeclerations.place);
            setModellicaString("PNlib.PD");
        } else {
            setBiologicalElement(Elementdeclerations.s_place);
            setModellicaString("PNlib.PC");
        }
        this.discrete = discrete;
    }

    public double getTokenStart() {
        return tokenStart;
    }

    public void setTokenStart(double tokenStart) {
        this.tokenStart = tokenStart;
    }

    private String modellicaString;

    public String getModellicaString() {
        return modellicaString;
    }

    public void setModellicaString(String modellicaString) {
        this.modellicaString = modellicaString;
    }

    private int r;

    private int b;

    private int g;

    public Place(String label, String name, Vertex vertex, double token, boolean discrete) {
        super(label, name, vertex);
        if (label.equals("")) setLabel(name);
        if (name.equals("")) setName(label);
        shapes = new VertexShapes();
        this.discrete = discrete;
        if (discrete) setShape(shapes.getEllipse(getVertex())); else setShape(shapes.getDoubleEllipse(getVertex()));
        if (discrete) {
            setBiologicalElement(Elementdeclerations.place);
        } else {
            setBiologicalElement(Elementdeclerations.s_place);
        }
        if (discrete) {
        } else {
        }
        this.token = token;
        setAbstract(false);
        setReference(false);
        setColor(Color.WHITE);
        if (discrete) {
            setModellicaString("PNlib.PD");
        } else {
            setModellicaString("PNlib.PC");
        }
    }

    @Override
    public void rebuildShape(VertexShapes vs) {
        Shape s = null;
        if (!discrete) s = vs.getDoubleEllipse(getVertex()); else s = vs.getEllipse(getVertex());
        AffineTransform transform = new AffineTransform();
        transform.scale(2, 2);
        setShape(transform.createTransformedShape(s));
    }

    public void consume(int i) {
        this.token -= i;
    }

    public double getToken() {
        return this.token;
    }

    public double getTokenMin() {
        return tokenMin;
    }

    public void setTokenMin(double tokenMin) {
        this.tokenMin = tokenMin;
    }

    public double getTokenMax() {
        return tokenMax;
    }

    public void setTokenMax(double tokenMax) {
        this.tokenMax = tokenMax;
    }

    public void setToken(double token) {
        this.token = token;
    }

    public void setRelativeColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void setPlotColor(Color plotColor) {
        this.plotColor = plotColor;
    }

    public Color getPlotColor() {
        return plotColor;
    }
}
