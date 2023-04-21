package maplab.dto;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import maplab.core.Workspace;
import maplab.gui.MapView;
import maplab.interfaces.Paintable;

/**
 * A class that represents a transit route with a name and list of vertices.
 */
public class Route implements Iterable<Coordinate>, Paintable {

    /** Default color to be given to created routes. */
    public static final Color DEFAULT_COLOR = Color.black;

    /** The name of the route. */
    public final String name;

    /** Whether route is displayed on the map. */
    public boolean visible = true;

    /** Coordinates of the route. The route passes trough all these coordinates. */
    private List<Coordinate> vertices;

    /** Color of the route and it's labels. */
    private Color color;

    private List<List<Point2D>> edges = null;

    /**
   * Constructs an empty route with a name.
   * 
   * @param name
   *          The name of the route. This will be used in all its labels
   */
    public Route(String name) {
        this.name = name;
        this.vertices = new ArrayList<Coordinate>();
        this.color = DEFAULT_COLOR;
    }

    /**
   * Adds a coordinate to the end of route.
   * 
   * @param coord
   *          The coordinate to add to the route
   */
    public void addVertex(Coordinate coord) {
        this.vertices.add(coord);
    }

    /**
   * Returns the coordinate of a vertex at a specified index.
   * 
   * @param index
   *          The number of the vertex coordinate to return The starting point
   *          is always at index 0
   * @return The coordinate of the requested vertex
   */
    public Coordinate getVertex(int index) {
        return this.vertices.get(index);
    }

    /** Returns the number of vertices on the route. */
    public int vertexCount() {
        return this.vertices.size();
    }

    /** Returns list of coordinates used by the route. */
    public List<Coordinate> getVertices() {
        return vertices;
    }

    /** Returns a string representation of the route. */
    public String toString() {
        return "Route: " + name + " ; " + vertices.toString();
    }

    /** Returns the length of the route. */
    public double length() {
        int limit = vertices.size() - 1;
        double length = 0;
        for (int i = 0; i < limit; ) {
            length += vertices.get(i).distance(vertices.get(++i));
        }
        return length;
    }

    /**
   * Sets the color of the route.
   * 
   * @param color
   *          new route color
   */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
   * Gets a new route color from RGB string and sets it as the color of this
   * route.
   * 
   * @param nm
   *          String of form "RRGGBB", where R,G and B are hex digits
   */
    public void setColor(String nm) {
        if (nm.length() != 6) return;
        String red = nm.substring(0, 2);
        String green = nm.substring(2, 4);
        String blue = nm.substring(4, 6);
        int r = Integer.parseInt(red, 16);
        int g = Integer.parseInt(green, 16);
        int b = Integer.parseInt(blue, 16);
        this.color = new Color(r, g, b);
    }

    /** Returns the color of the route. */
    public Color getColor() {
        return this.color;
    }

    public String getABGRColor() {
        return String.format("%02x%02x%02x%02x", color.getAlpha(), color.getBlue(), color.getGreen(), color.getRed());
    }

    /** Returns the color of the route as RGB string. */
    public String getColorAsText() {
        String rgb = Integer.toHexString(color.getRGB());
        return rgb.substring(2, rgb.length());
    }

    public Iterator<Coordinate> iterator() {
        return vertices.iterator();
    }

    public List<List<Point2D>> getEdges() {
        return edges;
    }

    public void setEdges(List<List<Point2D>> edgeList) {
        this.edges = edgeList;
    }

    @Override
    public void paint(Graphics2D g) {
        if (Workspace.SHOW_COLORS) {
            g.setColor(getColor());
        }
        GeneralPath path = new GeneralPath();
        if (getEdges() != null) {
            for (List<Point2D> edge : getEdges()) {
                if (edge.size() < 1) {
                    continue;
                }
                Point2D p = edge.get(0);
                path.moveTo(p.getX(), p.getY());
                for (Point2D point : edge.subList(1, edge.size())) {
                    path.lineTo(point.getX(), point.getY());
                }
            }
        } else {
            if (getVertices().size() == 0) {
                return;
            }
            Coordinate v = getVertex(0);
            path.moveTo(v.x, v.y);
            for (Coordinate c : vertices.subList(1, vertices.size())) {
                path.lineTo(c.x, c.y);
            }
            if (vertices.size() >= 2) {
                int size = 50;
                int s2 = size / 2;
                g.fillOval((int) vertices.get(0).x - s2, (int) vertices.get(0).y - s2, size, size);
                g.fillOval((int) vertices.get(vertices.size() - 1).x - s2, (int) vertices.get(vertices.size() - 1).y - s2, size, size);
            }
        }
        Stroke stroke = new BasicStroke(MapView.ROUTE_WIDTH / (float) MapView.COORDINATE_SCALE);
        g.setStroke(stroke);
        g.draw(path);
    }

    public Rectangle2D getBoundingBox() {
        Rectangle2D.Double boundingBox = new Rectangle2D.Double();
        if (getEdges() != null) {
            for (List<Point2D> edge : getEdges()) {
                for (Point2D point : edge) {
                    if (!boundingBox.contains(point)) {
                        boundingBox.add(point);
                    }
                }
            }
        } else {
            for (Coordinate coordinate : vertices) {
                if (!boundingBox.contains(coordinate)) {
                    boundingBox.add(coordinate);
                }
            }
        }
        return boundingBox;
    }
}
