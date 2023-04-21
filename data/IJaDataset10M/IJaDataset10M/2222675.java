package linelayer;

import nngl.common.util.Parser;
import nngl.common.layer.AbstractLayer;
import nngl.common.layer.GraphLayer;
import nngl.common.geom.Circle;
import nngl.common.geom.Point;
import nngl.common.graph.Graph;
import java.util.Properties;
import java.io.*;

public class LineLayer extends AbstractLayer implements GraphLayer {

    public boolean[][] layout(Graph g, int width, int height) {
        Properties p = new Properties();
        try {
            p.load(getResource("config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Parser parser = new Parser(p);
        int indent = (int) (width * parser.getDouble("indent"));
        int radius = (int) ((width - 2 * indent) * 1.0 / (2 * (3 * g.getNumberVertexes() - 2)));
        return layout(g, width, height, radius, indent);
    }

    public boolean[][] layout(Graph g, int width, int height, int radius, int indent) {
        int n = g.getNumberVertexes();
        Point v = new Point(indent, height / 2);
        Circle[] vertexes = new Circle[n];
        for (int i = 0; i < n; i++) {
            vertexes[i] = new Circle(v, radius);
            v = new Point(v.x + 3 * radius, v.y);
        }
        return layout(g, width, height, vertexes);
    }
}
